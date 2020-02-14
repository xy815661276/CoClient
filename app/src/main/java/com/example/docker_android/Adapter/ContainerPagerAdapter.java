package com.example.docker_android.Adapter;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.docker_android.Fragment.ContainerRunFragment;
import com.example.docker_android.Fragment.ContainerStopFragment;
import com.example.docker_android.R;


/**
 * 用于首页的Viewpager
 */
public class ContainerPagerAdapter extends FragmentPagerAdapter {

    private final String[] TITLES;
    private Fragment[] mFragments;

    public ContainerPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        TITLES = context.getResources().getStringArray(R.array.maintab_sections);//默认tab名称，后期考虑更改。
        mFragments = new Fragment[TITLES.length];
    }

    @Override
    public Fragment getItem(int position) {
        if (mFragments[position] == null) {
            switch (position) {
                case 0:
                    mFragments[position] = new ContainerRunFragment();
                    break;
                case 1:
                    mFragments[position] = new ContainerStopFragment();
                    break;
                default:
                    break;
            }
        }
        return mFragments[position];
    }

    @Override
    public int getCount() {
        return TITLES.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return TITLES[position];
    }
}
