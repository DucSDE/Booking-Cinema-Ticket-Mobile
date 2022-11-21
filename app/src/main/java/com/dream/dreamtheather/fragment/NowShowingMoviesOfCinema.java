package com.dream.dreamtheather.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.dream.dreamtheather.activity.DreamLocation;
import com.dream.dreamtheather.activity.MainActivity;
import com.dream.dreamtheather.Model.Movie;
import com.dream.dreamtheather.R;
import com.dream.dreamtheather.adapter.NowShowingAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NowShowingMoviesOfCinema extends Fragment implements OnCompleteListener<QuerySnapshot>, OnFailureListener { // implement 2 cai nay
    private static final String TAG ="ShowingMoviesOfCinema";
    private ArrayList<Integer> mMovies;
    private String CinemaName;
    private String hotline;
    private String address;

    @BindView(R.id.swipe_layout)
    SwipeRefreshLayout swipeLayout;

    @BindView(R.id.errorMessage)
    TextView mErrorTextView;

    @BindView(R.id.noMovie)
    TextView mNoMovieTextView;

    @BindView(R.id.title)
    TextView mTitle;

    @BindView(R.id.recycle_view)
    RecyclerView mRecyclerView;

    @BindView(R.id.btnCall)
    ImageButton btnCall;

    @BindView(R.id.btnMap)
    FloatingActionButton btnMap;


    NowShowingAdapter mAdapter;

    FirebaseFirestore db;

    @OnClick(R.id.back_button)
    void back() {
        super.getActivity().onBackPressed();
    }

    public static NowShowingMoviesOfCinema newInstance(ArrayList<Integer> Movies,
                                                       String CinemaName,
                                                       String hotline,
                                                       String address) {
        NowShowingMoviesOfCinema fragment = new NowShowingMoviesOfCinema();

        fragment.mMovies = Movies;
        fragment.CinemaName = CinemaName;
        fragment.hotline = hotline;
        fragment.address = address;
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.now_showing_for_cinema, container,false);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onViewCreated: ");
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this,view);



        mTitle.setText(CinemaName);

        db = ((MainActivity)getActivity()).firebaseFirestore;
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL,false);
        mRecyclerView.setLayoutManager(layoutManager);

        mAdapter = new NowShowingAdapter(getActivity());
        mRecyclerView.setAdapter(mAdapter);
        swipeLayout.setOnRefreshListener(this::refreshData);

        refreshData();
    }

    public void refreshData() {
        swipeLayout.setRefreshing(true);
        db.collection("now_showing")
                .get()
                .addOnCompleteListener(this)
                .addOnFailureListener(this);
    }

    @Override
    public void onComplete(@NonNull Task<QuerySnapshot> task) {

        if(swipeLayout.isRefreshing())
            swipeLayout.setRefreshing(false);

        mErrorTextView.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.VISIBLE);

        if (task.isSuccessful()) {
            QuerySnapshot querySnapshot = task.getResult();

            List<Movie> listMovie = querySnapshot.toObjects(Movie.class);

//            // Lọc lại danh sách Movies
//            int i = 0;
//
//            while (i < listMovie.size())
//            {
//                // Nếu ID phim không khớp với ID trong danh sách Cinema thì xoá phim đó
//                if (!mMovies.contains(listMovie.get(i).getId()))
//                {
//                    listMovie.remove(i);
//                }
//                else
//                    i++;
//            }

            if (listMovie.isEmpty()) // Nếu danh sách Movies rỗng thì hiện thông báo rỗng
            {
                mRecyclerView.setVisibility(View.GONE);
                mNoMovieTextView.setVisibility(View.VISIBLE);
            }
            else { // Nếu đã rỗng thì khỏi sort luôn
                Collections.sort(listMovie, new Comparator<Movie>() {
                    @Override
                    public int compare(Movie o1, Movie o2) {
                        return o1.getId() - o2.getId();
                    }
                });
            }

            if(mAdapter!=null)
                mAdapter.setData(listMovie);

        } else
            Log.w(TAG, "Error getting documents.", task.getException());
    }

    @OnClick(R.id.btnCall)
    public void callCinema(){
//        int permissionCheck = ContextCompat
//                .checkSelfPermission(getContext(),Manifest.permission.CALL_PHONE);
//
//        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat
//                    .requestPermissions(getActivity(),new String[]{Manifest.permission.CALL_PHONE}, 123);
//        } else {
//            startActivity(new Intent(Intent.ACTION_CALL).setData(Uri.parse("tel:12345678901")));
//        }

        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:"+ hotline));
        Log.i(TAG,"complete intent");
        startActivity(intent);
    }


    @OnClick(R.id.btnMap)
    public void LoadMap() {
        Intent intent = new Intent(getContext(), DreamLocation.class);
        intent.putExtra("cinemaName", mTitle.getText().toString());
        intent.putExtra("cinemaHotLine", hotline);
        intent.putExtra("cinemaAddress", this.address);

        Log.v(TAG,"get CinemaName" + mTitle.getText().toString());
        startActivity(intent);

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){

        }
    }

    @Override
    public void onFailure(@NonNull Exception e) {
        Log.d(TAG, "onFailure");
        if(swipeLayout.isRefreshing())
            swipeLayout.setRefreshing(false);

        mRecyclerView.setVisibility(View.GONE);
        mErrorTextView.setVisibility(View.VISIBLE);

    }

}
