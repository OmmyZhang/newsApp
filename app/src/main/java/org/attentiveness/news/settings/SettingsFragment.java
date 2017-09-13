package org.attentiveness.news.settings;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.TextView;

import org.attentiveness.news.R;
import org.attentiveness.news.base.BaseFragment;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SettingsFragment extends BaseFragment{
    @BindView(R.id.fl_settings_view)
    FrameLayout mRootView;
    @BindView(R.id.tv_fav_settings)
    TextView mFav;
    @BindView(R.id.tv_shield_settings)
    TextView mShield;

    public static SettingsFragment newInstance() {
        return new SettingsFragment();
    }

    public SettingsFragment() {
        // Required empty public constructor
    }
}
