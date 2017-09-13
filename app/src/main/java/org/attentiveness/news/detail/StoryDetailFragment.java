package org.attentiveness.news.detail;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.TextView;

import org.attentiveness.news.R;
import org.attentiveness.news.base.BaseFragment;
import org.attentiveness.news.data.StoryDetail;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * News Detail Fragment
 */
public class StoryDetailFragment extends BaseFragment implements StoryDetailContract.View {

    @BindView(R.id.fl_root_view)
    FrameLayout mRootView;
    @BindView(R.id.detail_content)
    TextView contentText;
    @BindView(R.id.detail_title)
    TextView titleText;
    @BindView(R.id.detail_category)
    TextView categoryText;
    @BindView(R.id.detail_url)
    TextView urlText;

    private StoryDetailContract.Presenter mPresenter;

    public static StoryDetailFragment newInstance() {
        return new StoryDetailFragment();
    }

    public StoryDetailFragment() {
        // Required empty public constructor
    }

    public String getText() {
        return titleText.getText() + "。 " + contentText.getText();
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_story_detail, container, false);
        ButterKnife.bind(this, view);
        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void setPresenter(@NonNull StoryDetailContract.Presenter presenter) {
        this.mPresenter = presenter;
    }

    @Override
    public void onResume() {
        super.onResume();
        this.mPresenter.subscribe();
    }

    @Override
    public void onPause() {
        super.onPause();
        this.mPresenter.unsubscribe();
    }

    @Override
    public void showError(String message) {
        this.showMessage(this.mRootView, message);
    }

    @Override
    public void showStoryDetail(StoryDetail storyDetail) {
        System.out.println("Show Detail");
        if (!this.isActive() || getView() == null) {
            return;
        }
        if (storyDetail == null) {
            return;
        }

        String content = storyDetail.getContent();

        content = content.replaceAll("  ","　").replaceAll("　　","　").replaceAll("　","\n　　");
        while(content.charAt(0) == '\n') {
            content = content.substring(1);
        }
        if(content.charAt(0) != '　')
            content = "　　" + content;

        titleText.setText(storyDetail.getTitle());
        categoryText.setText(storyDetail.getCategory());
        contentText.setText(content);
        urlText.setAutoLinkMask(Linkify.ALL);
        urlText.setText("原文地址 "+storyDetail.getUrl());
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }
}
