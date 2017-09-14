package com.java.group11.list;

import android.content.Context;
import android.os.Looper;
import android.support.annotation.NonNull;

import com.java.group11.data.source.StoriesDataRepository;
import com.java.group11.util.DateUtil;
import com.java.group11.util.schedulers.SchedulerProvider;
import com.java.group11.data.Story;
import com.java.group11.globalSetting.GlobalSetting;
import com.java.group11.globalSetting.JSONStore;
import com.java.group11.net.GetNews;
import com.java.group11.util.schedulers.BaseSchedulerProvider;

import java.util.List;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

class StoryListPresenter implements StoryListContract.Presenter {

    @NonNull
    private StoriesDataRepository mRepository;
    @NonNull
    private StoryListContract.View mNewsListView;
    @NonNull
    private final BaseSchedulerProvider mSchedulerProvider;
    @NonNull
    private CompositeDisposable mDisposables;
    private boolean mFirstLoad = true;
    private Context mContext;
    private Looper mLooper;

    StoryListPresenter(@NonNull StoriesDataRepository repository, @NonNull StoryListContract.View view, Context context) {
        this.mRepository = repository;
        this.mNewsListView = view;
        this.mSchedulerProvider = SchedulerProvider.getInstance();
        this.mDisposables = new CompositeDisposable();
        this.mNewsListView.setPresenter(this);
        mContext = context;
    }

    @Override
    public void subscribe() {
        String today = DateUtil.getToday();
        this.loadNewsList(today, false, false);
    }

    @Override
    public void unsubscribe() {
        this.mDisposables.clear();
    }

    @Override
    public void loadNewsList(@NonNull String date, boolean forceUpdate, boolean append) {
        this.loadNewsList(date, forceUpdate || this.mFirstLoad, true, append);
        this.mFirstLoad = false;
    }

    private void loadNewsList(@NonNull String date, boolean forceUpdate, final boolean showLoadingUI, final boolean append) {

        System.out.println(String.format("LOAD:: %s %b %b %b",date,forceUpdate,showLoadingUI,append));

        if(!append) {
            System.out.println("INIT_GetNews");

            GetNews.newINSTANCE(GlobalSetting.getINSTANCE().getInterestedClass() , GlobalSetting.getINSTANCE().getNotShow(), 250);

            System.out.println("interested: " + GlobalSetting.getINSTANCE().getInterestedClass());

        }

        if (showLoadingUI) {
            this.mNewsListView.setLoadingIndicator(true);
        }
        if (forceUpdate) {
            this.mRepository.refreshStories();
        }

       /* Observable<List<Story>> localStoryList;
        localStoryList = new Observable<List<Story>>() {
            @Override
            protected void subscribeActual(Observer<? super List<Story>> observer) {
                //mLooper = Looper.getMainLooper();
                //Looper.prepareMainLooper();
                Looper.prepare();
                JSONStore listLoader = new JSONStore(mContext);
                List<Story> storyList = listLoader.loadList();
                observer.onNext(storyList);
                Looper.loop();
            }
        };
        Disposable disposable1 = localStoryList.subscribeOn(this.mSchedulerProvider.computation())
                .observeOn(this.mSchedulerProvider.ui())
                .subscribe(new Consumer<List<Story>>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull List<Story> storyList) throws Exception {
                        mNewsListView.appendStoryList(storyList);
                    }
                });
        this.mDisposables.add(disposable1);*/

        Disposable disposable = this.mRepository.getStories(date)
                .subscribeOn(this.mSchedulerProvider.computation())
                .observeOn(this.mSchedulerProvider.ui())
                .subscribe(
                        new Consumer<List<Story>>() {
                            @Override
                            public void accept(@io.reactivex.annotations.NonNull List<Story> storyList) throws Exception {
                                JSONStore listLoader = new JSONStore(mContext);
                                List<Story> storyLoadList = listLoader.loadList();
                                if (storyLoadList != null)
                                    storyList.addAll(storyLoadList);
                                if (append) {
                                    mNewsListView.appendStoryList(storyList);
                                } else {
                                    mNewsListView.showStoryList(storyList);
                                }
                                mNewsListView.hideRetry();
                            }
                        },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(@io.reactivex.annotations.NonNull Throwable throwable) throws Exception {
                                JSONStore listLoader = new JSONStore(mContext);
                                List<Story> storyLoadList = listLoader.loadList();
                                mNewsListView.showStoryList(storyLoadList);
                                mNewsListView.showError(throwable.getMessage());
                                mNewsListView.showRetry();
                                //mNewsListView.hideStoryList();
                                mNewsListView.setLoadingIndicator(false);
                            }
                        },
                        new Action() {
                            @Override
                            public void run() throws Exception {
                                mNewsListView.setLoadingIndicator(false);
                            }
                        });
        this.mDisposables.add(disposable);

    }

}
