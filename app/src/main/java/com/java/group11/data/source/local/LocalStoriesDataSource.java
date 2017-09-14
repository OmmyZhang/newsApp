package com.java.group11.data.source.local;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;

import com.java.group11.data.Story;
import com.java.group11.data.StoryDetail;
import com.java.group11.data.source.StoriesDataSource;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;

public class LocalStoriesDataSource implements StoriesDataSource {

    private static LocalStoriesDataSource INSTANCE;

    private StoriesDbHelper mDbHelper;

    // Prevent direct instantiation.
    private LocalStoriesDataSource(@NonNull Context context) {
        this.mDbHelper = new StoriesDbHelper(context);
    }

    public static LocalStoriesDataSource getInstance(@NonNull Context context) {
        if (INSTANCE == null) {
            INSTANCE = new LocalStoriesDataSource(context);
        }
        return INSTANCE;
    }

    @Override
    public Observable<List<Story>> getStories(String date) {
        List<Story> storyList = new ArrayList<>();
        SQLiteDatabase database = this.mDbHelper.getReadableDatabase();
        String[] projections = {
                StoriesPersistenceContract.StoryEntry.COLUMN_NAME_STORY_ID,
                StoriesPersistenceContract.StoryEntry.COLUMN_NAME_TITLE};
        Cursor cursor = database.query(StoriesPersistenceContract.StoryEntry.TABLE_NAME, projections, null, null, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                String storyId = cursor.getString(cursor.getColumnIndexOrThrow(StoriesPersistenceContract.StoryEntry.COLUMN_NAME_STORY_ID));
                String title = cursor.getString(cursor.getColumnIndexOrThrow(StoriesPersistenceContract.StoryEntry.COLUMN_NAME_TITLE));
                Story story = new Story(storyId, title,"",""); // 记得修改
                storyList.add(story);
            }
        }
        if (cursor != null) {
            cursor.close();
        }
        database.close();
        if (storyList.size() == 0) {
            return Observable.empty();
        } else {
            return Observable.just(storyList);
        }
    }

    @Override
    public Observable<StoryDetail> getStoryDetail(String storyId) {
        //Do not save story detail in local storage, so do nothing
        return null;
    }

    @Override
    public void saveStories(@NonNull List<Story> storyList) {
        SQLiteDatabase database = this.mDbHelper.getWritableDatabase();
        for (Story story : storyList) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(StoriesPersistenceContract.StoryEntry.COLUMN_NAME_STORY_ID, story.getId());
            contentValues.put(StoriesPersistenceContract.StoryEntry.COLUMN_NAME_TITLE, story.getTitle());
            database.insertOrThrow(StoriesPersistenceContract.StoryEntry.TABLE_NAME, null, contentValues);
        }
        database.close();
    }

    @Override
    public void refreshStories() {
        //do nothing
    }

    @Override
    public void deleteAllStories() {
        SQLiteDatabase database = this.mDbHelper.getWritableDatabase();
        database.delete(StoriesPersistenceContract.StoryEntry.TABLE_NAME, null, null);
        database.close();
    }
}
