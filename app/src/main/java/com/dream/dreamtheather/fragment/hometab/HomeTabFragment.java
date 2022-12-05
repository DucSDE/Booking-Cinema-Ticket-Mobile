package com.dream.dreamtheather.fragment.hometab;

import android.annotation.SuppressLint;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager2.widget.ViewPager2;

import com.dream.dreamtheather.R;
import com.dream.dreamtheather.fragment.NowShowingFragment;
import com.dream.dreamtheather.fragment.UpcomingFragment;
import com.dream.dreamtheather.fragment.hometab.adapter.HomeTabViewPagerAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeTabFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "HomeTab";

    FragmentContainerView fragmentContainerView;
    ViewPager2 viewPager;

    ColorStateList def;
    TextView itemNowShowing;
    TextView itemUpcomingShowing;
    TextView select;

    HomeTabViewPagerAdapter homeTabViewPagerAdapter;

    public HomeTabFragment newInstance() {
        return new HomeTabFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home_tab, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        initView(view);
    }

    private void initView(View view) {
        View customTabView = view.findViewById(R.id.layout_custom_tabs);
        fragmentContainerView = view.findViewById(R.id.home_fragment_container);
        initViewCustomTab(customTabView);
    }

    private void loadFragment(Fragment fragment ) {
        // load fragment
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(fragmentContainerView.getId(), fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void initViewCustomTab(View customTabView) {

        itemNowShowing = customTabView.findViewById(R.id.itemNowShowing);
        itemUpcomingShowing = customTabView.findViewById(R.id.itemUpcomingShowing);
        select = customTabView.findViewById(R.id.select);

        itemNowShowing.setOnClickListener(this);
        itemUpcomingShowing.setOnClickListener(this);
        def = itemUpcomingShowing.getTextColors();
    }

    @Override
    public void onClick(View customTabView) {
        fragmentContainerView.removeAllViewsInLayout();
        if (customTabView.getId() == R.id.itemNowShowing) {
            select.animate().x(0).setDuration(100);
            itemNowShowing.setTextColor(Color.WHITE);
            itemUpcomingShowing.setTextColor(def);
            loadFragment(new NowShowingFragment());
        } else if (customTabView.getId() == R.id.itemUpcomingShowing) {
            itemNowShowing.setTextColor(def);
            itemUpcomingShowing.setTextColor(Color.WHITE);
            int size = itemUpcomingShowing.getWidth();
            select.animate().x(size).setDuration(100);
            loadFragment(new UpcomingFragment());
        }
    }
}