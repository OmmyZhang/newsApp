package org.attentiveness.news.detail;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import org.attentiveness.news.R;
import org.attentiveness.news.base.BaseActivity;
import org.attentiveness.news.data.source.StoriesDataRepository;
import org.attentiveness.news.data.source.local.LocalStoriesDataSource;
import org.attentiveness.news.data.source.remote.RemoteStoriesDataSource;
import org.attentiveness.news.list.StoryListFragment;

import java.util.Locale;

import butterknife.BindDrawable;
import butterknife.ButterKnife;

public class StoryDetailActivity extends BaseActivity {

    private static final String INSTANCE_STORY_ID = "story_id";

    private String mStoryId;
    TextToSpeech readTTS;

    private MenuItem it_fav;
    boolean fav_ed;

    @BindDrawable(R.mipmap.ic_fav)
    Drawable ic_fav_no;

    @BindDrawable(R.mipmap.ic_fav_ed)
    Drawable ic_fav_yes;

    private  StoryDetailFragment mStoryDetailFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story_detail);

        ButterKnife.bind(this);
        setup();

        if (savedInstanceState == null) {
            this.mStoryId = getIntent().getStringExtra(StoryListFragment.EXTRA_ID);
        } else {
            this.mStoryId = savedInstanceState.getString(INSTANCE_STORY_ID);
        }

        mStoryDetailFragment = (StoryDetailFragment) getSupportFragmentManager().findFragmentById(R.id.fl_container);
        if (mStoryDetailFragment == null) {
            mStoryDetailFragment = StoryDetailFragment.newInstance();
            addFragment(getSupportFragmentManager(), R.id.fl_container, mStoryDetailFragment);
        }
        StoriesDataRepository repository = StoriesDataRepository.getInstance(
                RemoteStoriesDataSource.getInstance(this), LocalStoriesDataSource.getInstance(this));
        StoryDetailPresenter presenter = new StoryDetailPresenter(this.mStoryId, repository, mStoryDetailFragment);

        readTTS = new TextToSpeech(StoryDetailActivity.this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    readTTS.setLanguage(Locale.CHINA);
                }
            }
        });

        fav_ed = false; // 这里应该检查是否已经被收藏了

        setTitle("新闻详情");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.detail_menu, menu);

        it_fav = menu.findItem(R.id.fav);
        it_fav.setIcon(fav_ed ? ic_fav_yes : ic_fav_no);

        return true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(INSTANCE_STORY_ID, this.mStoryId);
    }

    public void read_out(MenuItem it) {
        Toast.makeText(StoryDetailActivity.this, "开始朗读", Toast.LENGTH_SHORT).show();
        readTTS.speak(mStoryDetailFragment.getText(), TextToSpeech.QUEUE_FLUSH, null);
    }
    public void share(MenuItem it) {
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Title");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, "www.baidu.com");

        startActivity(Intent.createChooser(sharingIntent, "分享新闻到"));
    }

    public void fav(MenuItem it) {
        fav_ed = !fav_ed;
        it.setIcon(fav_ed ? ic_fav_yes : ic_fav_no);
        if (fav_ed) {
            Toast.makeText(StoryDetailActivity.this, "已加入收藏", Toast.LENGTH_SHORT).show();
            //加入收藏
        } else {
            Toast.makeText(StoryDetailActivity.this, "已取消收藏", Toast.LENGTH_SHORT).show();
            //从收藏中删除
        }
    }

    @Override
    public void onPause() {
        super.onPause();
//        readTTS.stop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (readTTS != null)
            readTTS.shutdown();
    }
}