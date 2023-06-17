package com.whu.tomado.ui.adapter;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.whu.tomado.R;
import com.whu.tomado.ui.fragment.ClockFragment;
import com.whu.tomado.ui.fragment.PlaceholderFragment;
import com.whu.tomado.ui.fragment.ProfileFragment;
import com.whu.tomado.ui.fragment.TeamFragment;
import com.whu.tomado.ui.fragment.TodoFragment;
import com.whu.tomado.ui.fragment.NodoFragment;

import java.time.Clock;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.tab_text_1, R.string.tab_text_2, R.string.tab_text_3, R.string.tab_text_4,R.string.tab_text_5};
    private final Context mContext;

    public SectionsPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
//        return PlaceholderFragment.newInstance(position + 1);
        // 根据位置返回对应的 PlaceholderFragment 实例
        if (position == 0) {
            return new TodoFragment(mContext);
        }else if(position == 1){
            return new NodoFragment(mContext);
        }else if(position == 2){
            return new ClockFragment(mContext);
        }else if(position == 3) {
            return new TeamFragment(mContext);
        }else if(position == 4){
            return new ProfileFragment();
        }else{
            return new PlaceholderFragment();
        }
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getResources().getString(TAB_TITLES[position]);
    }

    @Override
    public int getCount() {
        // Show all total pages.
        return 5;
    }
}