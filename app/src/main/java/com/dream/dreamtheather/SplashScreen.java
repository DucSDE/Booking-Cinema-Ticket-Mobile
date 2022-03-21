package com.dream.dreamtheather;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        introAnimation();


    }

    public void gotoNextActivity(){
        new Handler().postDelayed(() -> {
            openActivity();
        }, 1000);
    }

    public void openActivity() {
        Intent intent = new Intent(this, Test.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        //don't allow to return to this activity
        this.finish();
    }

    private void introAnimation() {
        ImageView logoImg = (ImageView) findViewById(R.id.mainLogo);

        RotateAnimation animation = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF,
                0.5f, Animation.RELATIVE_TO_SELF, 0.5f);

        animation.setStartOffset(500);
        animation.setDuration(2000);
        logoImg.setAnimation(animation);
        logoImg.startAnimation(animation);

        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                TextView swipeText = (TextView)findViewById(R.id.swipeSuggestionText);

                //Slide TextView and set opacity to 100
                swipeText.startAnimation(AnimationUtils.loadAnimation(SplashScreen.this, R.anim.right_to_left_swipe));
                swipeText.animate().alpha(1f).setDuration(1000);
                gotoNextActivity();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                if(!true){
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    openActivity();
                }


            }

        });
    }
}