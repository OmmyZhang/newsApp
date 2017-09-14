package com.java.group11.detail;

import com.java.group11.base.BasePresenter;
import com.java.group11.base.BaseView;
import com.java.group11.data.StoryDetail;

interface StoryDetailContract {

    interface View extends BaseView<Presenter> {

        void showStoryDetail(StoryDetail storyDetail);

        void showError(String message);

        boolean isActive();

    }

    interface Presenter extends BasePresenter {

    }
}
