package org.attentiveness.news.list;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.SearchView;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

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
    private SlidePagerAdapter spa;
    private ArrayList<Fragment> fList;
    private StoryListFragment newsListFragment;
    private SearchView mSearchView;
    YouMayLikeFragment newMayLikeFragement;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story_list);
        ButterKnife.bind(this);
        setup(R.drawable.ic_menu);

        String today = DateUtil.getToday();
        newsListFragment = StoryListFragment.newInstance(today);
        newMayLikeFragement = YouMayLikeFragment.newInstance();

        StoriesDataRepository repository = StoriesDataRepository.getInstance(
                RemoteStoriesDataSource.getInstance(this), LocalStoriesDataSource.getInstance(this));
        StoryListPresenter newListPresenter= new StoryListPresenter(repository, newsListFragment);
        YouMayLikePresenter youMayLikePresenter = new YouMayLikePresenter(repository, newMayLikeFragement);


        mVP = (ViewPager) findViewById(R.id.vpg);

        fList = new ArrayList<Fragment>();
        fList.add(newsListFragment);
        fList.add(newMayLikeFragement);

        spa = new SlidePagerAdapter(getSupportFragmentManager(),fList);
        mVP.setAdapter(spa);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        final MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        final MenuItem item = menu.findItem(R.id.edit_search);
        mSearchView = (SearchView) MenuItemCompat.getActionView(item);
        this.mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Intent intent = new Intent(StoryListActivity.this, SearchActivity.class);
                intent.putExtra("key_word", query);
                startActivity(intent);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        final MenuItem mode = menu.findItem(R.id.night_mode);
        mode.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                menuItem.setChecked(!menuItem.isChecked());
                return false;
            }
        });
        boolean modef = !mode.isChecked();
        //SkinManager.getInstance().changeSkin(modef ? "night" : "");
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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (!mSearchView.isIconified()) {
                mSearchView.onActionViewCollapsed();
                System.out.println("back successfully");
            }
            else if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
                mDrawerLayout.closeDrawer(GravityCompat.START);
            }
            else if (this.getClass() == StoryListActivity.class)
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("");
                builder.setMessage("确认退出吗>.<");
                builder.setPositiveButton("是的", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                });
                builder.setNegativeButton("再看一会儿吧", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                Dialog dialog = builder.create();
                dialog.show();
            }
            else {
                finish();
            }
        }
        return false;
    }

    public void refresh(MenuItem it)
    {
        //addFragment(getSupportFragmentManager(), R.id.search_layout, searchFragement);
        newsListFragment.refresh_from_menu();
        newMayLikeFragement.refresh_from_menu();
    }
}
