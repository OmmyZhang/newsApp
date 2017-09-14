package com.java.group11.data.source;

import android.support.annotation.NonNull;

import com.java.group11.data.Story;
import com.java.group11.data.StoryDetail;

import java.util.List;

import io.reactivex.Observable;

public interface StoriesDataSource {

    Observable<List<Story>> getStories(String date);

    Observable<StoryDetail> getStoryDetail(String storyId);

    void saveStories(@NonNull List<Story> storyList);

    void refreshStories();

    void deleteAllStories();

}
