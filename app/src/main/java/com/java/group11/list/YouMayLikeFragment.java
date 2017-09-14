package com.java.group11.list;

import com.java.group11.globalSetting.GlobalSetting;

/**
 * Created by zhangyn on 17-9-10.
 */

public class YouMayLikeFragment extends StoryListFragment {

    private YouMayLikePresenter mPresenter;

    public static YouMayLikeFragment newInstance() {
        YouMayLikeFragment youMayLikeFragment = new YouMayLikeFragment();
        return youMayLikeFragment;
    }

    public void setYMPresenter(YouMayLikePresenter presenter) {
        this.mPresenter = presenter;
    }




    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser && GlobalSetting.getINSTANCE().isAutoRefreshMayLike())
        {
            mPresenter.loadNewsList("",true,false);
        }
    }
}
