package com.dream.dreamtheather.fragment.hometab.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.dream.dreamtheather.fragment.NowShowingFragment;
import com.dream.dreamtheather.fragment.UpcomingFragment;

public class HomeTabViewPagerAdapter extends FragmentStateAdapter {
    private static final int MAX_ITEM_TAB = 2;

    public HomeTabViewPagerAdapter(Fragment fragment) {
        super(fragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0 : return new NowShowingFragment();
            case 1 : return new UpcomingFragment();
        }
        return new NowShowingFragment();
    }

    @Override
    public int getItemCount() {
        return MAX_ITEM_TAB;
    }
}
