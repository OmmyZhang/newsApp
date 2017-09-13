package org.attentiveness.news.detail;


import android.annotation.SuppressLint;
import android.content.Context;
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
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.attentiveness.news.R;
import org.attentiveness.news.base.BaseFragment;
import org.attentiveness.news.data.StoryDetail;

import java.util.List;
import java.util.regex.Pattern;

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
    @BindView(R.id.detail_img)
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

        Picasso.with(mContext)
                .load(mStoryImg)
                .placeholder(R.drawable.ic_image_black_24dp)
                .error(R.drawable.ic_broken_image_black_24dp)
                .into(detailImage);

        titleText.setText(storyDetail.getTitle());
        categoryText.setText(storyDetail.getCategory());

        urlText.setAutoLinkMask(Linkify.ALL);
        urlText.setText("原文地址 " + storyDetail.getUrl());

        contentText.setText(content);
        Pattern words = storyDetail.getNames();
        Linkify.addLinks(contentText, words, "https://baike.baidu.com/item/");

    }

    @Override
    public boolean isActive() {
        return isAdded();
    }
}
