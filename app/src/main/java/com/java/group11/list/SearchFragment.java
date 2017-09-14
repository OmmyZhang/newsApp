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

import com.java.group11.base.BaseFragment;
import com.java.group11.data.Story;
import com.java.group11.detail.StoryDetailActivity;
import com.java.group11.net.GetNews;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by shols on 2017/9/11.
 */

public class SearchFragment extends BaseFragment implements StoryListContract.View, StoryListAdapter.OnItemClickListener{

    @BindView(com.java.group11.R.id.rv_search_list)
    RecyclerView mSearchListView;
    @BindView((com.java.group11.R.id.rl_search_fresh_view))
    ScrollChildSwipeRefreshLayout mSwipeRefreshLayout;

    private StoryListAdapter mStoriesAdapter;
    private LoadMoreListener mLoadMoreListener;
    private StoryListContract.Presenter mPresenter;
    private static String mKeyWord;

    private boolean needRefresh = true;

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
        mStoriesAdapter.getResources(getActivity().getResources());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(com.java.group11.R.layout.fragment_search_layout, container, false);
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
        //mPresenter.loadNewsList("", false, true);

        this.mSwipeRefreshLayout.setColorSchemeColors(
                ContextCompat.getColor(getActivity(), com.java.group11.R.color.colorPrimary),
                ContextCompat.getColor(getActivity(), com.java.group11.R.color.colorAccent),
                ContextCompat.getColor(getActivity(), com.java.group11.R.color.colorPrimaryDark)
        );
        this.mSwipeRefreshLayout.setChildView(this.mSearchListView);
        this.mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mLoadMoreListener.refreshed();
                mPresenter.loadNewsList("", false, false);
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
            this.mPresenter.loadNewsList("", true, false);
            needRefresh = false;
        }
        this.mSearchListView.addOnScrollListener(this.mLoadMoreListener);
    }

    @Override
    public void onPause() {
        super.onPause();
        this.mPresenter.unsubscribe();
        this.mSearchListView.removeOnScrollListener(this.mLoadMoreListener);
    }

    @Override
    public void showStoryList(List<Story> storyList) {
        this.mSearchListView.setVisibility(View.VISIBLE);
        Observable<List<HashMap>> observable = new Observable<List<HashMap>>() {
            @Override
            protected void subscribeActual(Observer<? super List<HashMap>> observer) {
                ArrayList<HashMap> stories = new ArrayList<>();
                GetNews.getINSTANCE().search(mKeyWord);
                System.out.println(mKeyWord);
                try {
                    stories = GetNews.getINSTANCE().searchMore();
                    observer.onNext(stories);
                } catch (Exception e) {
                    observer.onError(e);
                } finally {
                    observer.onComplete();
                }
            }
        };
        observable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<HashMap>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(List<HashMap> hashMaps) {
                        List<Story> list = new ArrayList<>();
                        for (HashMap item : hashMaps) {
                            list.add(new Story((String) item.get("news_ID"),
                                    (String) item.get("news_Title"),
                                    (String) item.get("news_Pictures"),
                                    (String) item.get("news_Intro")));
                        }
                        System.out.println("story_list_size: " + list.size());
                        mStoriesAdapter.setItemList(list);
                    }

                    @Override
                    public void onError(Throwable e) {
                        showMessage(mSearchListView, e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void appendStoryList(final List<Story> storyList) {
        this.mSearchListView.setVisibility(View.VISIBLE);
        Observable<List<HashMap>> observable = new Observable<List<HashMap>>() {
            @Override
            protected void subscribeActual(Observer<? super List<HashMap>> observer) {
                ArrayList<HashMap> stories = new ArrayList<>();
                GetNews.getINSTANCE().search(mKeyWord);
                System.out.println("Key:: " + mKeyWord);
                try {
                    stories = GetNews.getINSTANCE().searchMore();
                    observer.onNext(stories);
                } catch (Exception e) {
                    observer.onError(e);
                } finally {
                    observer.onComplete();
                }
            }
        };
        observable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<HashMap>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(List<HashMap> hashMaps) {
                        List<Story> list = new ArrayList<>();
                        System.out.println("search:: " + hashMaps.get(0).get("news_Title"));
                        for (HashMap item : hashMaps) {
                            list.add(new Story((String) item.get("news_ID"),
                                    (String) item.get("news_Title"),
                                    (String) item.get("news_Pictures"),
                                    (String) item.get("news_Intro")));
                        }

                        storyList.addAll(list);
                        System.out.println("search:: " + storyList.get(0).getTitle());
                        System.out.println("story_list_size: " + list.size());
                        mStoriesAdapter.setItemList(storyList);
                    }

                    @Override
                    public void onError(Throwable e) {
                        showMessage(mSearchListView, e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

    @Override
    public void hideStoryList() {
        this.mSearchListView.setVisibility(View.GONE);
    }

    @Override
    public void setLoadingIndicator(final boolean active) {
        if (!this.isActive() || getView() == null) {
            return;
        }
        this.mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(active);
            }
        });
    }

    @Override
    public void showRetry() {

    }

    @Override
    public void hideRetry() {

    }

    @Override
    public void showError(String message) {
        showMessage(this.mSwipeRefreshLayout, message);
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
            intent.putExtra("img", story.getImage());
            startActivity(intent);
        }
    }

    public void refresh_from_menu() {
        mLoadMoreListener.refreshed();
        mPresenter.loadNewsList("", false, false);
    }
}
