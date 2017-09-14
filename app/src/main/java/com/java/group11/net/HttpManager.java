package com.java.group11.net;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.java.group11.data.Story;
import com.java.group11.data.StoryDetail;
import com.java.group11.exception.NetworkException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.Observer;

/**
 * Http Manager that responds to url request.
 */
public class HttpManager {

    private static final String BASE_URL = "http://166.111.68.66:2042/com.java.com.group11/action/query/";

    private static HttpManager INSTANCE = null;

    private Context mContext;

    public static HttpManager getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new HttpManager(context);
        }
        return INSTANCE;
    }

    /**
     * Both connection time and read time are 10,000ms by default.
     */
    private HttpManager(Context context) {
        this.mContext = context;
    }

    public Observable<List<Story>> getStoryList(String date) {
        if (!this.isConnected()) {
            return Observable.error(new Callable<Throwable>() {
                @Override
                public Throwable call() {
                    return new NetworkException(mContext.getResources().getString(com.java.group11.R.string.error_no_network_connection));
                }
            });
        }

        return new Observable<List<Story>>() {
            @Override
            protected void subscribeActual(Observer<? super List<Story>> observer) {
                List<HashMap> list;
                List<Story> sL;
                try {
                    GetNews gn = GetNews.getINSTANCE();
                    list = gn.getMore();

                    sL = new ArrayList<Story>();

                    if (list == null)
                        sL.add(new Story("", "没有更多啦~", "",""));
                    else if (list.size() == 0)
                        sL.add(new Story("", "都被滤掉啦~","","下拉加载更多或者关注更多话题并减少敏感词吧"));
                    else
                        for (HashMap e : list) {
                            sL.add(new Story((String) e.get("news_ID"),
                                    (String) e.get("news_Title"),
                                    (String) e.get("news_Pictures"),
                                    (String) e.get("news_Intro")));
                        }


                    System.out.println("GetNews : " + sL.size());

                    observer.onNext(sL);
                } catch (Exception e) {
                    System.out.println("get story list: " + e);
                    observer.onError(e);
                } finally {
                    observer.onComplete();
                }
            }

        };

    }

    public Observable<StoryDetail> getStory(final String sId) {
        if (!this.isConnected()) {
            return Observable.error(new Callable<Throwable>() {
                @Override
                public Throwable call() {
                    return new NetworkException(mContext.getResources().getString(com.java.group11.R.string.error_no_network_connection));
                }
            });
        }
        return new Observable<StoryDetail>() {
            @Override
            protected void subscribeActual(Observer<? super StoryDetail> observer) {
                GetNews gn = GetNews.getINSTANCE();
                try {
                    observer.onNext(gn.getDetail(sId));
                }catch (Exception e)
                {
                    observer.onError(e);
                }
                finally{
                    observer.onComplete();
                }


            }
        };
//        return this.mStoryService.getStoryDetail(storyId);
    }

    private boolean isConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) this.mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnectedOrConnecting();
    }

}
