package com.java.group11.list;

import android.content.Context;
import android.support.annotation.NonNull;

import com.java.group11.data.Story;
import com.java.group11.data.source.StoriesDataRepository;
import com.java.group11.globalSetting.GlobalSetting;
import com.java.group11.net.GetNews;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by shols on 2017/9/13.
 */

public class YouMayLikePresenter extends StoryListPresenter {

    @NonNull
    private StoryListContract.View mNewsListView;
    @NonNull
    private StoriesDataRepository mRepository;
    //@NonNull
    //private GlobalSetting mGlobalSetting;

    private Map<String, Double> mReadRecord;
    private boolean mFirstLoad = true;


    YouMayLikePresenter(@NonNull StoriesDataRepository repository, @NonNull StoryListContract.View view, Context context) {
        super(repository, view, context);
        mRepository = repository;
        mNewsListView = view;
        mNewsListView.setPresenter(this);
        ((YouMayLikeFragment)mNewsListView).setYMPresenter(this);
    }

    @Override
    public void loadNewsList(@NonNull String date, boolean forceUpdate, boolean append) {
        this.loadNewsList( forceUpdate || this.mFirstLoad, true, append);
        this.mFirstLoad = false;
    }

    private void loadNewsList(boolean forceUpdate, final boolean showLoadingUI, final boolean append) {
        if (showLoadingUI) {
            this.mNewsListView.setLoadingIndicator(true);
        }
        if (forceUpdate) {
            this.mRepository.refreshStories();
        }
        Observable<List<HashMap>> observable = new Observable<List<HashMap>>() {
            @Override
            protected void subscribeActual(Observer<? super List<HashMap>> observer) {
                try {
                    List<HashMap> storyList = GetNews.getINSTANCE().mayLike(GlobalSetting.getINSTANCE().getReadRecord());
                    observer.onNext(storyList);
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
                        if (append) {
                            mNewsListView.appendStoryList(list);
                        } else {
                            mNewsListView.showStoryList(list);
                        }
                        mNewsListView.hideRetry();
                    }

                    @Override
                    public void onError(Throwable e) {
                        mNewsListView.showError(e.getMessage());
                        mNewsListView.showRetry();
                        mNewsListView.hideStoryList();
                        mNewsListView.setLoadingIndicator(false);
                    }

                    @Override
                    public void onComplete() {
                        mNewsListView.setLoadingIndicator(false);
                    }
                });
    }
}
