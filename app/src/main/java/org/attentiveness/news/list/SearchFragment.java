package org.attentiveness.news.list;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.attentiveness.news.R;
import org.attentiveness.news.base.BaseFragment;
import org.attentiveness.news.data.Story;
import org.attentiveness.news.detail.StoryDetailActivity;
import org.attentiveness.news.net.GetNews;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by shols on 2017/9/11.
 */

public class SearchFragment extends BaseFragment implements StoryListContract.View, StoryListAdapter.OnItemClickListener{

    @BindView(R.id.rv_search_list)
    RecyclerView mSearchListView;

    private StoryListAdapter mStoriesAdapter;
    private LoadMoreListener mLoadMoreListener;
    private StoryListContract.Presenter mPresenter;
    private static String mKeyWord;

    public static SearchFragment newInstance(String keyWord) {
        SearchFragment searchFragment = new SearchFragment();
        mKeyWord = keyWord;
        return searchFragment;
    }

    public SearchFragment() {
    }

    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mStoriesAdapter = new StoryListAdapter();
        this.mStoriesAdapter.setOnItemClickListener(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_search_layout, container, false);
        ButterKnife.bind(this, rootView);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        this.mSearchListView.setLayoutManager(linearLayoutManager);
        this.mSearchListView.setAdapter(mStoriesAdapter);

        this.mLoadMoreListener = new LoadMoreListener(linearLayoutManager) {
            @Override
            void onLoadMore() {
                mPresenter.loadNewsList("", false, true);
            }
        };

        setHasOptionsMenu(true);
        return rootView;
    }


    @Override
    public void setPresenter(@NonNull StoryListContract.Presenter presenter) {
        this.mPresenter = presenter;
    }

    @Override
    public void showStoryList(List<Story> storyList) {
        this.mSearchListView.setVisibility(View.VISIBLE);
        GetNews.getINSTANCE().search(mKeyWord);
        ArrayList<HashMap> stories = new ArrayList<HashMap>();
        try {
            stories = GetNews.getINSTANCE().searchMore();
        } catch (Exception e) {
            e.printStackTrace();
        }
        List<Story> list = null;
        for (HashMap item : stories) {
            list.add(new Story((String) item.get("news_ID"),
                    (String) item.get("news_Title"),
                    (String) item.get("news_Pictures"),
                    (String) item.get("news_Intro")));
        }
        this.mStoriesAdapter.setItemList(list);
    }

    @Override
    public void appendStoryList(List<Story> storyList) {
        this.mSearchListView.setVisibility(View.VISIBLE);
        this.mStoriesAdapter.addItemList(storyList);
    }

    @Override
    public void hideStoryList() {
        this.mSearchListView.setVisibility(View.GONE);
    }

    @Override
    public void setLoadingIndicator(boolean active) {
    }

    @Override
    public void showRetry() {

    }

    @Override
    public void hideRetry() {

    }

    @Override
    public void showError(String message) {
        showMessage(this.mSearchListView, message);
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }

    @Override
    public void onStoryClicked(Story story) {
        if (story != null && !story.getId().equals("")) {
            Intent intent = new Intent(this.getActivity(), StoryDetailActivity.class);
            intent.putExtra("id", story.getId());
            startActivity(intent);
        }
    }

    public void refresh_from_menu() {
        mLoadMoreListener.refreshed();
        mPresenter.loadNewsList("", false, false);
    }
}
