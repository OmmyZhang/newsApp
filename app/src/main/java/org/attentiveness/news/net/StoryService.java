package org.attentiveness.news.net;

import org.attentiveness.news.data.News;
import org.attentiveness.news.data.StoryDetail;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface StoryService {

    @GET("latest")
    Observable<News> getStories(@Query("pageNo") int pn, @Query("pageSize") int ps, @Query("category") int cg);

    @GET("detail")
    Observable<StoryDetail> getStoryDetail(@Query("newsId") String storyId);

}
