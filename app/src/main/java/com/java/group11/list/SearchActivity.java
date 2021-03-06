package com.java.group11.list;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerTabStrip;
import android.view.MenuItem;
import android.view.View;

import com.java.group11.data.source.StoriesDataRepository;
import com.java.group11.data.source.local.LocalStoriesDataSource;
import com.java.group11.data.source.remote.RemoteStoriesDataSource;

import butterknife.ButterKnife;

/**
 * Created by shols on 2017/9/13.
 */

public class SearchActivity extends StoryListActivity {

    private SearchFragment mSearchFragment;
    private String keyWord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(null);
        setContentView(com.java.group11.R.layout.activity_story_list);
        ButterKnife.bind(this);
        setup(com.java.group11.R.drawable.ic_menu);
        PagerTabStrip pagerTabStrip = (PagerTabStrip)findViewById(com.java.group11.R.id.vpg_title);
        pagerTabStrip.setVisibility(View.INVISIBLE);

        Intent intent = getIntent();
        keyWord = intent.getStringExtra("key_word");
        mSearchFragment = SearchFragment.newInstance(keyWord);
        StoriesDataRepository repository = StoriesDataRepository.getInstance(
                RemoteStoriesDataSource.getInstance(this), LocalStoriesDataSource.getInstance(this));
        StoryListPresenter storyListPresenter = new StoryListPresenter(repository, mSearchFragment, this);

        addFragment(getSupportFragmentManager(), com.java.group11.R.id.vpg_container, mSearchFragment);

    }

    @Override
    public void refresh(MenuItem it) {
        mSearchFragment.refresh_from_menu();
    }
}
