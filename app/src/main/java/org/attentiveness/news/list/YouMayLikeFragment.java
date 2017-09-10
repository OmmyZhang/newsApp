package org.attentiveness.news.list;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.attentiveness.news.R;
import org.attentiveness.news.base.BaseFragment;

/**
 * Created by zhangyn on 17-9-10.
 */

public class YouMayLikeFragment extends BaseFragment {

    public static YouMayLikeFragment newInstance() {
        YouMayLikeFragment mayLike = new YouMayLikeFragment();
//        Bundle bundle = new Bundle();
//        bundle.putString(EXTRA_DATE, date);
//        storyListFragment.setArguments(bundle);
        return mayLike;
    }

    public YouMayLikeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_may_like_list, container, false);
        return rootView;
    }
}
