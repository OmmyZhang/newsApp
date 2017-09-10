package org.attentiveness.news.net;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Created by zhangyn on 17-9-8.
 */

public class RemoteDataManager implements  Runnable {
    private int mPageSize;
    private HashSet<String> classTag , notShow;
    private static RemoteDataManager INSTANTCE = null;
    private static GetNews gn = null;

    private RemoteDataManager(int pageSize) {

        mPageSize = pageSize;
        classTag = new HashSet<String>();

    }

    static public RemoteDataManager getINSTANCE(int pageSize) {

        if (INSTANTCE == null)
            INSTANTCE = new RemoteDataManager(pageSize);
        return INSTANTCE;
    }

    @Override
    public void run() {
        gn.clear();
        gn.getInstance(classTag , notShow ,mPageSize);

    }

    public ArrayList<HashMap> getMore() throws Exception
    {
        return gn.getMore();
    }


}
