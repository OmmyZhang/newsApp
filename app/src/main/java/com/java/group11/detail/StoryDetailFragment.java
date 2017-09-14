package com.java.group11.detail;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import com.java.group11.base.BaseFragment;
import com.java.group11.data.StoryDetail;

import com.java.group11.globalSetting.GlobalSetting;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * News Detail Fragment
 */
public class StoryDetailFragment extends BaseFragment implements StoryDetailContract.View {

    @BindView(com.java.group11.R.id.fl_root_view)
    FrameLayout mRootView;
    @BindView(com.java.group11.R.id.detail_content)
    TextView contentText;
    @BindView(com.java.group11.R.id.detail_title)
    TextView titleText;
    @BindView(com.java.group11.R.id.detail_category)
    TextView categoryText;
    @BindView(com.java.group11.R.id.detail_url)
    TextView urlText;
    @BindView(com.java.group11.R.id.detail_img)
    ImageView detailImage;

    private String mStoryImg;
    private Context mContext;

    private StoryDetailContract.Presenter mPresenter;

    public static StoryDetailFragment newInstance() {
        return new StoryDetailFragment();
    }

    public StoryDetailFragment() {
        // Required empty public constructor
    }

    public void setImg(String s)
    {
        mStoryImg = s;
    }
    public void setContext(Context c)
    {
        mContext = c;
    }

    public String getText() {
        return titleText.getText() + "。 " + contentText.getText();
    }

    public String getTitle(){
        return titleText.getText() + "";
    }

    public String shareText() {
        return titleText.getText() + "\n" + contentText.getText() + "\n" + urlText.getText();
    }

    public  String getmStoryImg() {
        return mStoryImg;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(com.java.group11.R.layout.fragment_story_detail, container, false);
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
        if (!this.isActive() || getView() == null) {
            return;
        }
        if (storyDetail == null) {
            return;
        }

        String content = storyDetail.getContent();

        content = content.replaceAll("  ", "　").replaceAll("　　", "　").replaceAll("　", "\n　　");
        while (content.charAt(0) == '\n') {
            content = content.substring(1);
        }
        if (content.charAt(0) != '　')
            content = "　　" + content;

        if (!GlobalSetting.getINSTANCE().isNoPicture()) {
            detailImage.setVisibility(View.VISIBLE);
            Picasso.with(mContext)
                    .load(mStoryImg)
                    .placeholder(com.java.group11.R.drawable.ic_image_black_24dp)
                    .error(com.java.group11.R.drawable.ic_broken_image_black_24dp)
                    .into(detailImage);
        }
        else
            detailImage.setVisibility(View.GONE);

        titleText.setText(storyDetail.getTitle());
        categoryText.setText(storyDetail.getCategory());

        urlText.setAutoLinkMask(Linkify.ALL);
        urlText.setText("原文地址 " + storyDetail.getUrl());

        contentText.setText(content);
        Pattern words = storyDetail.getNames();
        Linkify.addLinks(contentText, words, "https://baike.baidu.com/item/");

        //JSONStore newsSaver = new JSONStore(getActivity());
        //newsSaver.saveNews(storyDetail);

    }

    @Override
    public boolean isActive() {
        return isAdded();
    }
}
