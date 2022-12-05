package com.dream.dreamtheather.fragment.hometab;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import com.dream.dreamtheather.R;

public class CustomTab extends FragmentActivity implements View.OnClickListener{
    public static final String TAG = "CustomTab";
    ColorStateList def;
    TextView itemNowShowing;
    TextView itemUpcomingShowing;
    TextView select;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_tabs_layout);

        itemNowShowing = findViewById(R.id.itemNowShowing);
        itemUpcomingShowing = findViewById(R.id.itemUpcomingShowing);
        itemNowShowing.setOnClickListener(this);
        itemUpcomingShowing.setOnClickListener(this);
        select = findViewById(R.id.select);
        def = itemUpcomingShowing.getTextColors();
    }
    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.itemNowShowing){
            Log.d(TAG, "onClick: Now Showing Button Click");
            select.animate().x(0).setDuration(100);
            itemNowShowing.setTextColor(Color.WHITE);
            itemUpcomingShowing.setTextColor(def);
        } else if (view.getId() == R.id.itemUpcomingShowing){
            Log.d(TAG, "onClick: Upcoming Showing Button Click");
            itemNowShowing.setTextColor(def);
            itemUpcomingShowing.setTextColor(Color.WHITE);
            int size = itemUpcomingShowing.getWidth();
            select.animate().x(size).setDuration(100);
        }
    }
}
