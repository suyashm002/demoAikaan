package com.example.aikaanapp.adapters;

import android.os.Parcelable;
import android.util.SparseArray;
import android.view.ViewGroup;

import com.example.aikaanapp.fragments.DeviceFragment;
import com.example.aikaanapp.fragments.HomeFragment;
import com.example.aikaanapp.fragments.StatisticsFragment;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class TabAdapter extends FragmentStatePagerAdapter {

    public static final int NUM_TABS = 3;

    public static final int TAB_HOME      = 0;
    public static final int TAB_MY_DEVICE = 1;
    public static final int TAB_CHARTS    = 2;
    //private static final int TAB_HISTORY   = 3;
    // private static final int TAB_ABOUT     = 3;

    private final SparseArray<Fragment> mFragments = new SparseArray<>(NUM_TABS);

    public TabAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
        // work around "Fragment no longer exists for key" Android bug
        // by catching the IllegalStateException
        // https://code.google.com/p/android/issues/detail?id=42601
        try {
            super.restoreState(state, loader);
        } catch (IllegalStateException e) {
            // nop
        }
    }

    @Override
    public int getCount() {
        return NUM_TABS;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case TAB_HOME:
                return HomeFragment.newInstance();
            case TAB_MY_DEVICE:
                return DeviceFragment.newInstance();
            case TAB_CHARTS:
                return StatisticsFragment.newInstance();
//            case TAB_HISTORY:
//                return HistoryFragment.newInstance();
//            case TAB_ABOUT:
//                return AboutFragment.newInstance();
            default:
                return null;
        }
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Object item = super.instantiateItem(container, position);
        if (item instanceof Fragment) {
            mFragments.put(position, (Fragment) item);
        }
        return item;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        mFragments.remove(position);
        super.destroyItem(container, position, object);
    }

    public Fragment getFragment(int position) {
        if (isValidPosition(position)) {
            return mFragments.get(position);
        } else {
            return null;
        }
    }

    public String getTabName(int position) {
        switch (position) {
            case TAB_HOME:
                return "Home";
            case TAB_MY_DEVICE:
                return "My Device";
            case TAB_CHARTS:
                return "Statistics";
            default:
                return "default";
        }
    }

    private boolean isValidPosition(int position) {
        return (position >= 0 && position < NUM_TABS);
    }
}