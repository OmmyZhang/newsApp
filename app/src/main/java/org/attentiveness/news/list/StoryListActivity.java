package org.attentiveness.news.list;

import android.app.AlertDialog;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import org.attentiveness.news.R;
import org.attentiveness.news.base.BaseActivity;
import org.attentiveness.news.data.source.StoriesDataRepository;
import org.attentiveness.news.data.source.local.LocalStoriesDataSource;
import org.attentiveness.news.data.source.remote.RemoteStoriesDataSource;
import org.attentiveness.news.net.GetNews;
import org.attentiveness.news.util.DateUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;

import butterknife.ButterKnife;

public class StoryListActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story_list);
        ButterKnife.bind(this);
        setup(R.drawable.ic_menu);

        StoryListFragment newsListFragment = (StoryListFragment) getSupportFragmentManager().findFragmentById(R.id.fl_container);
        if (newsListFragment == null) {
            String today = DateUtil.getToday();
            newsListFragment = StoryListFragment.newInstance(today);
            addFragment(getSupportFragmentManager(), R.id.fl_container, newsListFragment);
        }

        new Thread(runnable).start(); // show how to use GetNews. To save user's time, put into SplashActivity maybe better

        StoriesDataRepository repository = StoriesDataRepository.getInstance(
                RemoteStoriesDataSource.getInstance(this), LocalStoriesDataSource.getInstance(this));
        new StoryListPresenter(repository, newsListFragment);
    }

    Runnable runnable = new Runnable() { // show how to use GetNews
        @Override
        public void run() {

            try {
                HashSet<String> tags = new HashSet<String>();
                tags.add("科技");
                tags.add("教育");
                tags.add("文化");

                HashSet<String> notShow = new HashSet<String>();
                notShow.add("北京");

                /*GetNews gn = new GetNews(tags, notShow, 5);

                System.out.println("[latest]:");
                ArrayList<HashMap> list1 = gn.getMore();
                for (HashMap news : list1) {
                    System.out.println("--------");
                    for (Object key : news.keySet()) {
                        String k = (String) key, v = (String) news.get(k);
                        System.out.println(k + ":" + v);
                    }
                }


                System.out.println("[search]:");
                gn.search("清华大学");
                ArrayList<HashMap> list2 = gn.searchMore();
                for (HashMap news : list2) {
                    System.out.println("--------");
                    for (Object key : news.keySet()) {
                        String k = (String) key, v = (String) news.get(k);
                        System.out.println(k + ":" + v);
                    }
                }*/

            } catch (Exception e) {
                System.out.println(e.toString());
            }
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // Open the navigation drawer when the home icon is selected from the toolbar.
                if (this.mDrawerLayout != null) {
                    this.mDrawerLayout.openDrawer(GravityCompat.START);
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                switch (itemId) {
                    case R.id.nav_news_list:
                        //do nothing
                        break;
                    case R.id.nav_feedback:
                        break;
                    case R.id.nav_about:
                    {
                        AlertDialog.Builder builder = new AlertDialog.Builder(StoryListActivity.this);
                        builder.setMessage("作者:(字典序)\n\nssq\nyjy\nzyn\n")
                                .setTitle("关于")
                                .setPositiveButton("确定", null);

                        AlertDialog dialog_about = builder.create();
                        dialog_about.show();

                    }
                    case R.id.nav_settings:
                        break;
                    default:
                        break;
                }
                // Close the navigation drawer when an item is selected.
                item.setChecked(true);
                if (mDrawerLayout != null) {
                    mDrawerLayout.closeDrawers();
                }
                return true;
            }
        });
    }

}
