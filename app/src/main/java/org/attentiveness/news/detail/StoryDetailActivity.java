package org.attentiveness.news.detail;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import org.attentiveness.news.R;
import org.attentiveness.news.base.BaseActivity;
import org.attentiveness.news.data.StoryDetail;
import org.attentiveness.news.data.source.StoriesDataRepository;
import org.attentiveness.news.data.source.local.LocalStoriesDataSource;
import org.attentiveness.news.data.source.remote.RemoteStoriesDataSource;
import org.attentiveness.news.list.StoryListFragment;
import org.attentiveness.news.splash.SplashActivity;

import java.util.Locale;

import butterknife.ButterKnife;

public class StoryDetailActivity extends BaseActivity {

    private static final String INSTANCE_STORY_ID = "story_id";

    private int mStoryId;
    TextToSpeech readTTS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story_detail);

        ButterKnife.bind(this);
        setup();

        if (savedInstanceState == null) {
            this.mStoryId = getIntent().getIntExtra(StoryListFragment.EXTRA_ID, 0);
        } else {
            this.mStoryId = savedInstanceState.getInt(INSTANCE_STORY_ID);
        }

        StoryDetailFragment storyDetailFragment = (StoryDetailFragment) getSupportFragmentManager().findFragmentById(R.id.fl_container);
        if (storyDetailFragment == null) {
            storyDetailFragment = StoryDetailFragment.newInstance();
            addFragment(getSupportFragmentManager(), R.id.fl_container, storyDetailFragment);
        }
        StoriesDataRepository repository = StoriesDataRepository.getInstance(
                RemoteStoriesDataSource.getInstance(this), LocalStoriesDataSource.getInstance(this));
        StoryDetailPresenter presenter = new StoryDetailPresenter(this.mStoryId, repository, storyDetailFragment);

        readTTS = new TextToSpeech(StoryDetailActivity.this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    readTTS.setLanguage(Locale.CHINA);
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.detail_menu, menu);
        return true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(INSTANCE_STORY_ID, this.mStoryId);
    }

    public void read_out(MenuItem it) {
        readTTS.speak("朗读朗读朗读，我能朗读",TextToSpeech.QUEUE_FLUSH,null);
  }
}
