package com.java.group11.data.source;

import android.os.Looper;
import android.support.annotation.NonNull;

import com.java.group11.data.Story;
import com.java.group11.data.StoryDetail;
import com.java.group11.globalSetting.JSONStore;
import com.java.group11.list.StoryListFragment;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Predicate;

public class StoriesDataRepository implements StoriesDataSource {

    private static StoriesDataRepository INSTANCE = null;

    private final StoriesDataSource mStoriesRemoteDataSource;

    private final StoriesDataSource mStoriesLocalDataSource;

    /**
     * This variable has package local visibility so it can be accessed from tests.
     */
    private Map<String, Story> mCachedStories;

    /**
     * Marks the cache as invalid, to force an update the next time data is requested. This variable
     * has package local visibility so it can be accessed from tests.
     */
    private boolean mCacheIsDirty = false;

    // Prevent direct instantiation.
    private StoriesDataRepository(@NonNull StoriesDataSource storiesRemoteDataSource,
                                  @NonNull StoriesDataSource storiesLocalDataSource) {
        this.mStoriesRemoteDataSource = storiesRemoteDataSource;
        this.mStoriesLocalDataSource = storiesLocalDataSource;
        this.mCachedStories = new HashMap<>();
    }

    public static StoriesDataRepository getInstance(StoriesDataSource storiesRemoteDataSource,
                                                    StoriesDataSource storiesLocalDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new StoriesDataRepository(storiesRemoteDataSource, storiesLocalDataSource);
        }
        return INSTANCE;
    }

    @Override
    public Observable<List<Story>> getStories(String date) {
        if (this.mCachedStories != null && !this.mCacheIsDirty) {
            List<Story> storyList = (List<Story>) this.mCachedStories.values();
            return Observable.just(storyList);
        }
        Observable<List<Story>> remoteStoryList;
        remoteStoryList = this.getAndSaveStoryListFromRemote(date);
        Observable<List<Story>> localStoryList;
        localStoryList = new Observable<List<Story>>() {
            @Override
            protected void subscribeActual(Observer<? super List<Story>> observer) {
                Looper.prepare();
                JSONStore listLoader = new JSONStore(StoryListFragment.newInstance("").getActivity());
                List<Story> storyList = listLoader.loadList();
                observer.onNext(storyList);
            }
        };
        if (this.mCacheIsDirty) {
            return remoteStoryList;
        } else {

            return Observable.concat(localStoryList, remoteStoryList)
                    .filter(new Predicate<List<Story>>() {
                        @Override
                        public boolean test(@io.reactivex.annotations.NonNull List<Story> storyList) throws Exception {
                            return storyList != null && storyList.size() > 0;
                        }
                    })
                    .firstElement()
                    .toObservable();
        }
    }

    /**
     * Get story detail. Now it just gets data from remote source, not from local source and not
     * cache the story detail.
     *
     * @param storyId Each story has one unique id.
     * @return Observable wraps story detail.
     */
    @Override
    public Observable<StoryDetail> getStoryDetail(String storyId) {
        return this.mStoriesRemoteDataSource.getStoryDetail(storyId);
    }

    @Override
    public void saveStories(@NonNull List<Story> storyList) {
        this.mStoriesLocalDataSource.saveStories(storyList);
        this.mStoriesRemoteDataSource.saveStories(storyList);
        if (this.mCachedStories == null) {
            this.mCachedStories = new HashMap<>();
        }
        for (Story story : storyList) {
            this.mCachedStories.put(story.getId(), story);
        }
    }

    @Override
    public void refreshStories() {
        this.mCacheIsDirty = true;
    }

    @Override
    public void deleteAllStories() {
        this.mStoriesLocalDataSource.deleteAllStories();
        this.mStoriesRemoteDataSource.deleteAllStories();
        if (this.mCachedStories == null) {
            this.mCachedStories = new HashMap<>();
        }
        this.mCachedStories.clear();
    }

    private Observable<List<Story>> getAndSaveStoryListFromRemote(String date) {
        return this.mStoriesRemoteDataSource.getStories(date)
                .doOnNext(new Consumer<List<Story>>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull List<Story> storyList) throws Exception {
                        mStoriesLocalDataSource.saveStories(storyList);
                        /*for (Story story : storyList) {
                            mCachedStories.put(story.getId(), story);
                        }*/
                    }
                })
                .doOnComplete(new Action() {
                    @Override
                    public void run() throws Exception {
                        mCacheIsDirty = true;
                    }
                });
    }

    private Observable<List<Story>> getAndSaveStoryListFromLocal(String date) {
        return this.mStoriesLocalDataSource.getStories(date)
                .doOnNext(new Consumer<List<Story>>() {
            @Override
            public void accept(@io.reactivex.annotations.NonNull List<Story> storyList) throws Exception {
                for (Story story : storyList) {
                    mCachedStories.put(story.getId(), story);
                }
            }
        });
    }

}
