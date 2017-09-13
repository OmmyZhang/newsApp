package org.attentiveness.news.net;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import org.attentiveness.news.R;
import org.attentiveness.news.data.Story;
import org.attentiveness.news.data.StoryDetail;
import org.attentiveness.news.exception.NetworkException;

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

<<<<<<< HEAD
    //private static final String BASE_URL = "http://news-at.zhihu.com/api/4/news/";
=======
>>>>>>> testTTS
    private static final String BASE_URL = "http://166.111.68.66:2042/news/action/query/";

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
                    return new NetworkException(mContext.getResources().getString(R.string.error_no_network_connection));
                }
            });
        }
<<<<<<< HEAD
        return  this.mStoryService.getStories(1, 10, 1).map(new Function<News, List<Story>>() {
=======
        return new Observable<List<Story>>() {
>>>>>>> testTTS
            @Override
            protected void subscribeActual(Observer<? super List<Story>> observer) {
                List<HashMap> list;
                List<Story> sL;
                try {
                    GetNews gn = GetNews.getINSTANCE();
                    list = gn.getMore();
/*
                    HashMap<String,Double> readRecord = new HashMap<String,Double>();
                    readRecord.put("清华",100.0);
                    readRecord.put("李克强",30.0);

                    list = gn.mayLike(readRecord);
*/
                    sL = new ArrayList<Story>();

                    if (list == null)
                        sL.add(new Story("", "没有更多啦~", "",""));
                    else if (list.size() == 0)
                        sL.add(new Story("", "都被滤掉啦~","","继续刷新或者减少敏感词吧"));
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
                    observer.onError(e);
                } finally {
                    observer.onComplete();
                }
            }
<<<<<<< HEAD
        });
        /*return this.mStoryService.getStoryList(date).map(new Function<News, List<Story>>() {
            @Override
            public List<Story> apply(@NonNull News news) throws Exception {
                return news.getStoryList();
            }
        });*/
    }

    public Observable<StoryDetail> getStory(String storyId) {
=======
        };

    }

    public Observable<StoryDetail> getStory(final String sId) {
>>>>>>> testTTS
        if (!this.isConnected()) {
            return Observable.error(new Callable<Throwable>() {
                @Override
                public Throwable call() {
                    return new NetworkException(mContext.getResources().getString(R.string.error_no_network_connection));
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
