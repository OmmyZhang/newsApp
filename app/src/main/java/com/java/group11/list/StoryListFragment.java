package com.java.group11.list;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.java.group11.util.DateUtil;
import com.java.group11.base.BaseFragment;
import com.java.group11.data.Story;
import com.java.group11.detail.StoryDetailActivity;

import com.java.group11.globalSetting.JSONStore;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * News list fragment.
 */
public class StoryListFragment extends BaseFragment implements StoryListContract.View, StoryListAdapter.OnItemClickListener {

    public static final String EXTRA_ID = "id";
    public static final String EXTRA_DATE = "date";
    public static final String EXTRA_IMG = "img";

    @BindView(com.java.group11.R.id.rv_story_list)
    RecyclerView mStoriesView;
    @BindView(com.java.group11.R.id.rl_swipe_fresh_view)
    ScrollChildSwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(com.java.group11.R.id.tv_loading_error)
    TextView mLoadingErrorView;

    private StoryListContract.Presenter mPresenter;
    private StoryListAdapter mStoriesAdapter;
    private LoadMoreListener mLoadMoreListener;
    private String mOriginalDate;
    private String mDate;
    private int mLoadingCount;

    private LinearLayoutManager linearLayoutManager;

    public boolean needRefresh = true;

    public static StoryListFragment newInstance(String date) {
        StoryListFragment storyListFragment = new StoryListFragment();
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_DATE, date);
        storyListFragment.setArguments(bundle);
        return storyListFragment;
    }

    public StoryListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mStoriesAdapter = new StoryListAdapter();
        this.mStoriesAdapter.setOnItemClickListener(this);
        Bundle bundle = getArguments();
        if (bundle != null && bundle.containsKey(EXTRA_DATE)) {
            this.mOriginalDate = bundle.getString(EXTRA_DATE);
        }
        mStoriesAdapter.getResources(getActivity().getResources());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(com.java.group11.R.layout.fragment_story_list, container, false);
        ButterKnife.bind(this, rootView);

        linearLayoutManager = new LinearLayoutManager(getActivity());
        this.mStoriesView.setLayoutManager(linearLayoutManager);
        this.mStoriesView.setAdapter(this.mStoriesAdapter);

        this.mLoadingCount = 0;
        this.mDate = this.mOriginalDate;
//        LogUtil.e(mDate);
        this.mLoadMoreListener = new LoadMoreListener(linearLayoutManager) {

            @Override
            void onLoadMore() {
                mLoadingCount++;
                mDate = DateUtil.getDate(mLoadingCount);
                mPresenter.loadNewsList(mDate, false, true);
//                LogUtil.e(mDate);
            }
        };

        this.mSwipeRefreshLayout.setColorSchemeColors(
                ContextCompat.getColor(getActivity(), com.java.group11.R.color.colorPrimary),
                ContextCompat.getColor(getActivity(), com.java.group11.R.color.colorAccent),
                ContextCompat.getColor(getActivity(), com.java.group11.R.color.colorPrimaryDark)
        );
        this.mSwipeRefreshLayout.setChildView(this.mStoriesView);
        this.mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mLoadingCount = 0;
                mDate = mOriginalDate;
                if(linearLayoutManager.getItemCount() <= linearLayoutManager.getChildCount())
                    mPresenter.loadNewsList(mDate,false,true);
                else {
                    mLoadMoreListener.refreshed();
                    mPresenter.loadNewsList(mDate, false, false);
                }
            }
        });



        setHasOptionsMenu(true);
        return rootView;
    }

    @Override
    public void setPresenter(@NonNull StoryListContract.Presenter presenter) {
        this.mPresenter = presenter;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(needRefresh) {
            this.mPresenter.subscribe();
            needRefresh = false;
        }
        this.mStoriesView.addOnScrollListener(this.mLoadMoreListener);
    }

    @Override
    public void onPause() {
        super.onPause();
        this.mPresenter.unsubscribe();
        this.mStoriesView.removeOnScrollListener(this.mLoadMoreListener);
    }

    @Override
    public void showRetry() {
        this.mLoadingErrorView.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideRetry() {
        this.mLoadingErrorView.setVisibility(View.GONE);
    }

    @Override
    public void showError(String message) {
        showMessage(this.mSwipeRefreshLayout, message);
    }

    @Override
    public void setLoadingIndicator(final boolean active) {
        if (!this.isActive() || getView() == null) {
            return;
        }

        // Make sure setRefreshing() is called after the layout is done with everything else.
        this.mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(active);
            }
        });
    }

    @Override
    public void showStoryList(List<Story> storyList) {
        this.mStoriesView.setVisibility(View.VISIBLE);
        JSONStore listSaver = new JSONStore(getActivity());
        listSaver.saveList(storyList);
        //System.out.println("data check: "+storyList.size());
        this.mStoriesAdapter.setItemList(storyList);
    }

    @Override
    public void appendStoryList(List<Story> storyList) {
        this.mStoriesView.setVisibility(View.VISIBLE);
        //JSONStore listSaver = new JSONStore(getActivity());
        //listSaver.saveList(storyList);
        this.mStoriesAdapter.addItemList(storyList);
    }

    @Override
    public void hideStoryList() {
        this.mStoriesView.setVisibility(View.GONE);
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }

    @Override
    public void onStoryClicked(Story story) {
        if (story != null && !story.getId().equals("")) {
            Intent intent = new Intent(this.getActivity(), StoryDetailActivity.class);
            intent.putExtra(EXTRA_ID, story.getId());
            intent.putExtra(EXTRA_IMG,story.getImage());
            startActivity(intent);
            this.setLoadingIndicator(false);
        }
    }

    @OnClick(com.java.group11.R.id.tv_loading_error)
    void reload() {
        this.mDate = this.mOriginalDate;
        this.mLoadingCount = 0;
        this.mPresenter.loadNewsList(this.mDate, true, false);
    }

    void refresh_from_menu() {
        mLoadMoreListener.refreshed();
        mPresenter.loadNewsList(mDate, false, false);
    }
}
