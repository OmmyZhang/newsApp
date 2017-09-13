package org.attentiveness.news.list;

import android.support.annotation.NonNull;

import org.attentiveness.news.data.source.StoriesDataRepository;

/**
 * Created by shols on 2017/9/13.
 */

public class YouMayLikePresenter extends StoryListPresenter {
    YouMayLikePresenter(@NonNull StoriesDataRepository repository, @NonNull StoryListContract.View view) {
        super(repository, view);
    }

    //public void loadNewsList (List<HashMap>)
}
