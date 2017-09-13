package org.attentiveness.news.list;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import org.attentiveness.news.R;
import org.attentiveness.news.base.BaseActivity;
import org.attentiveness.news.data.source.StoriesDataRepository;
import org.attentiveness.news.data.source.local.LocalStoriesDataSource;
import org.attentiveness.news.data.source.remote.RemoteStoriesDataSource;
import org.attentiveness.news.util.DateUtil;

import java.util.ArrayList;

import butterknife.ButterKnife;

public class StoryListActivity extends BaseActivity {

    private ViewPager mVP;
    private PagerTabStrip mPTS;
    private SlidePagerAdapter spa;
    private ArrayList<Fragment> fList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story_list);
        ButterKnife.bind(this);
        setup(R.drawable.ic_menu);

        String today = DateUtil.getToday();
        StoryListFragment newsListFragment = StoryListFragment.newInstance(today);

        StoriesDataRepository repository = StoriesDataRepository.getInstance(
                RemoteStoriesDataSource.getInstance(this), LocalStoriesDataSource.getInstance(this));
        new StoryListPresenter(repository, newsListFragment);

        YouMayLikeFragment newMayLikeFragement = YouMayLikeFragment.newInstance();

        mVP = (ViewPager) findViewById(R.id.vpg);
        mPTS = (PagerTabStrip) findViewById(R.id.vpg_title);

        fList = new ArrayList<Fragment>();
        fList.add(newsListFragment);
        fList.add(newMayLikeFragement);

        spa = new SlidePagerAdapter(getSupportFragmentManager(),fList);
        mVP.setAdapter(spa);

    }

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
