package org.attentiveness.news.list;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerTabStrip;
import android.view.MenuItem;
import android.view.View;

import org.attentiveness.news.R;

import butterknife.ButterKnife;

/**
 * Created by shols on 2017/9/13.
 */

public class SearchActivity extends StoryListActivity {

    private SearchFragment mSearchFragment;
    private String keyWord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story_list);
        ButterKnife.bind(this);
        setup(R.drawable.ic_menu);
        PagerTabStrip pagerTabStrip = (PagerTabStrip)findViewById(R.id.vpg_title);
        pagerTabStrip.setVisibility(View.INVISIBLE);

        mSearchFragment = SearchFragment.newInstance(keyWord);
        addFragment(getSupportFragmentManager(), R.id.vpg_container, mSearchFragment);

        Intent intent = getIntent();
        keyWord = intent.getStringExtra("key_word");
        System.out.println(keyWord);
    }

    @Override
    public void refresh(MenuItem it) {
        mSearchFragment.refresh_from_menu();
    }
}
