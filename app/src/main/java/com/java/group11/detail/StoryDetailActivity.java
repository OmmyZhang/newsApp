package com.java.group11.detail;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.ActivityCompat;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.java.group11.base.BaseActivity;
import com.java.group11.data.source.StoriesDataRepository;
import com.java.group11.data.source.local.LocalStoriesDataSource;
import com.java.group11.data.source.remote.RemoteStoriesDataSource;
import com.java.group11.list.StoryListFragment;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import com.java.group11.globalSetting.GlobalSetting;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Locale;

import butterknife.BindDrawable;
import butterknife.ButterKnife;

public class StoryDetailActivity extends BaseActivity {

    private static final String INSTANCE_STORY_ID = "story_id";

    private String mStoryId;
    private String mStoryImg;
    TextToSpeech readTTS;

    private MenuItem it_fav;
    boolean fav_ed;

    @BindDrawable(com.java.group11.R.mipmap.ic_fav)
    Drawable ic_fav_no;

    @BindDrawable(com.java.group11.R.mipmap.ic_fav_ed)
    Drawable ic_fav_yes;

    private  StoryDetailFragment mStoryDetailFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.java.group11.R.layout.activity_story_detail);

        ButterKnife.bind(this);
        setup();

        if (savedInstanceState == null) {
            mStoryId = getIntent().getStringExtra(StoryListFragment.EXTRA_ID);
            mStoryImg = getIntent().getStringExtra(StoryListFragment.EXTRA_IMG);
            System.out.println("Detail get img: " + mStoryImg);
        } else {
            mStoryId = savedInstanceState.getString(INSTANCE_STORY_ID);
        }

        mStoryDetailFragment = (StoryDetailFragment) getSupportFragmentManager().findFragmentById(com.java.group11.R.id.fl_container);
        if (mStoryDetailFragment == null) {
            mStoryDetailFragment = StoryDetailFragment.newInstance();
            addFragment(getSupportFragmentManager(), com.java.group11.R.id.fl_container, mStoryDetailFragment);
        }
        StoriesDataRepository repository = StoriesDataRepository.getInstance(
                RemoteStoriesDataSource.getInstance(this), LocalStoriesDataSource.getInstance(this));
        StoryDetailPresenter presenter = new StoryDetailPresenter(this.mStoryId, repository, mStoryDetailFragment);

        mStoryDetailFragment.setImg(mStoryImg);
        mStoryDetailFragment.setContext(this);

        readTTS = new TextToSpeech(StoryDetailActivity.this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    readTTS.setLanguage(Locale.CHINA);
                }
            }
        });

        fav_ed = GlobalSetting.getINSTANCE().isFav(mStoryId);

        setTitle("新闻详情");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(com.java.group11.R.menu.detail_menu, menu);

        it_fav = menu.findItem(com.java.group11.R.id.fav);
        it_fav.setIcon(fav_ed ? ic_fav_yes : ic_fav_no);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == com.java.group11.R.id.home) {
            System.out.println("Destroy");
            onDestroy();
            return true;
        }
        return super.onOptionsItemSelected(item);
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

        String imgUrl = mStoryDetailFragment.getmStoryImg();

        Target target = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {

                try {
                    File f = new File(Environment.getExternalStoragePublicDirectory(
                            Environment.DIRECTORY_PICTURES), mStoryId + ".png");

                    System.out.println(f.getAbsolutePath());

                    if(!f.exists()) {
                        f.createNewFile();

                        System.out.println("Creat");
                        FileOutputStream out = new FileOutputStream(f);
                        System.out.println("Open");
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
                        System.out.println("Write");
                        out.flush();
                        out.close();

                        Toast.makeText(StoryDetailActivity.this, "正在保存图片以备分享..", Toast.LENGTH_SHORT).show();
                    }
                    Intent sharingIntent = new Intent(Intent.ACTION_SEND);

                    sharingIntent.setType("image/*");
                    sharingIntent.putExtra(Intent.EXTRA_TITLE, mStoryDetailFragment.getTitle());
                    sharingIntent.putExtra(Intent.EXTRA_TEXT, mStoryDetailFragment.shareText());
                    sharingIntent.putExtra("Kdescription", mStoryDetailFragment.shareText());

                    Uri uri = Uri.fromFile(f);

                    sharingIntent.putExtra(Intent.EXTRA_STREAM, uri);

                    startActivity(Intent.createChooser(sharingIntent, "分享新闻到.."));

                } catch (Exception e) {
                    System.out.println("save and share: " + e);
                    ActivityCompat.requestPermissions(StoryDetailActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                }
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
            }
        };

        Picasso picasso = Picasso.with(StoryDetailActivity.this);
        picasso.load(imgUrl).into(target);
    }

    public void fav(MenuItem it) {
        fav_ed = !fav_ed;
        it.setIcon(fav_ed ? ic_fav_yes : ic_fav_no);
        if (fav_ed) {
            Toast.makeText(StoryDetailActivity.this, "已加入收藏", Toast.LENGTH_SHORT).show();
            GlobalSetting.getINSTANCE().addFavStoryId(mStoryId);
        } else {
            Toast.makeText(StoryDetailActivity.this, "已取消收藏", Toast.LENGTH_SHORT).show();
            GlobalSetting.getINSTANCE().delFavStoryId(mStoryId);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if(!GlobalSetting.getINSTANCE().isAllowBackstageVoice())
            readTTS.stop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (readTTS != null)
            readTTS.shutdown();

        File f = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), mStoryId + ".png");

        System.out.println(f.getAbsolutePath());

        if(f.exists())
        {
            f.delete();
            Toast.makeText(StoryDetailActivity.this, "已清理图片", Toast.LENGTH_SHORT).show();
        }
    }
}