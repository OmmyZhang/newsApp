package com.java.group11.detail;

import android.support.annotation.NonNull;

import com.java.group11.data.source.StoriesDataRepository;
import com.java.group11.util.schedulers.SchedulerProvider;
import com.java.group11.data.StoryDetail;
import com.java.group11.util.schedulers.BaseSchedulerProvider;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;

class StoryDetailPresenter implements StoryDetailContract.Presenter {

    private String mStoryId;
    @NonNull
    private StoriesDataRepository mRepository;
    @NonNull
    private StoryDetailContract.View mView;
    private CompositeDisposable mDisposables;
    private BaseSchedulerProvider mSchedulerProvider;

    StoryDetailPresenter(String storyId, @NonNull StoriesDataRepository repository, @NonNull StoryDetailContract.View view) {
        this.mStoryId = storyId;
        this.mRepository = repository;
        this.mView = view;
        this.mDisposables = new CompositeDisposable();
        this.mSchedulerProvider = SchedulerProvider.getInstance();
        this.mView.setPresenter(this);
    }

    @Override
    public void subscribe() {
        this.requestStoryDetail(this.mStoryId);
    }

    @Override
    public void unsubscribe() {
        this.mDisposables.clear();
    }

    private void requestStoryDetail(String storyId) {
        this.mRepository.getStoryDetail(storyId).subscribeOn(this.mSchedulerProvider.io())
                .observeOn(this.mSchedulerProvider.ui())
                .subscribe(
                        new Consumer<StoryDetail>() {
                            @Override
                            public void accept(@io.reactivex.annotations.NonNull StoryDetail storyDetail) throws Exception {
                                mView.showStoryDetail(storyDetail);
                            }
                        },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(@io.reactivex.annotations.NonNull Throwable throwable) throws Exception {
                                mView.showError(throwable.getMessage());
                            }
                        }
                );
    }

}
