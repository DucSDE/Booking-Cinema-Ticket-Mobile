package com.dream.dreamtheather.admin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.dream.dreamtheather.activity.AdminActivity;
import com.dream.dreamtheather.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MovieManagement extends Fragment {
    public static MovieManagement newInstance() {
        return new MovieManagement();
    }
    @BindView(R.id.back_button)
    ImageView mBackButton;
    @BindView(R.id.title)
    TextView mTitle;

    @OnClick(R.id.back_button)
    void back() {

    }

    @OnClick(R.id.see_all_movie_panel)
    void goToAllMoviePage() {
        ((AdminActivity) getActivity()).loadFragment(AllMovie.newInstance());
    }

    @OnClick({R.id.cncm_movie, R.id.next_cncm})
    void goToChooseMoviesForNowShowing() {
        ((AdminActivity) getActivity()).loadFragment(ChooseMovie.newInstance(ChooseMovie.MODE.NOW_SHOWING));
    }

    @OnClick({R.id.choose_feature_movie, R.id.next_feature_movie})
    void goToChooseFeatureMovies() {
        ((AdminActivity) getActivity()).loadFragment(ChooseMovie.newInstance(ChooseMovie.MODE.FEATURE));
    }

    @OnClick({R.id.choose_upcoming_movie, R.id.next_choose_upcoming})
    void goToChooseMoviesForUpComing() {
        ((AdminActivity) getActivity()).loadFragment(ChooseMovie.newInstance(ChooseMovie.MODE.UP_COMING));
    }

    @OnClick(R.id.add_new_movie_panel)
    void goToAddNewMovie() {
        ((AdminActivity) getActivity()).loadFragment(AddNewMovie.newInstance());
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this,view);
    }


    @Override
    @Nullable
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.admin_movie_dashboard,container,false);
    }
}
