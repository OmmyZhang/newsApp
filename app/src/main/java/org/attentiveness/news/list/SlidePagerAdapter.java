package org.attentiveness.news.list;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by zhangyn on 17-9-9.
 */

public class SlidePagerAdapter extends FragmentPagerAdapter {

    private ArrayList<Fragment> fLists;

    public SlidePagerAdapter(FragmentManager fm, ArrayList<Fragment> list) {
        super(fm);
        fLists = list;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return (position == 0) ? "最新" : "猜你喜欢";
    }


    @Override
    public int getCount() {
        return fLists.size();
    }

    @Override
    public Fragment getItem(int position){
        return fLists.get(position);
    }

}
