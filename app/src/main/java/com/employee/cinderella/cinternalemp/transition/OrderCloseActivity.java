package com.employee.cinderella.cinternalemp.transition;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.employee.cinderella.cinternalemp.R;

public class OrderCloseActivity extends AppCompatActivity {

    AnimationDrawable rocketAnimation;
    private static int SPLASH_TIME_OUT = 3000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_close);


        final Animation animation_1 = AnimationUtils.loadAnimation(getBaseContext(),R.anim.frombottom);
        final Animation animation_2 = AnimationUtils.loadAnimation(getBaseContext(),R.anim.antirotate);
        final Animation animation_3 = AnimationUtils.loadAnimation(getBaseContext(),R.anim.abc_fade_out);

        ImageView rocketImage = (ImageView) findViewById(R.id.rocket_image);
       // rocketImage.setBackgroundResource(R.drawable.animation1);
        //rocketAnimation = (AnimationDrawable) rocketImage.getBackground();

        TextView title = findViewById(R.id.textView90);
      // rocketAnimation.start();

        rocketImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

       rocketImage.startAnimation(animation_2);
        animation_2.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

                //rocketImage.startAnimation(animation_2);
            }

            @Override
            public void onAnimationEnd(Animation animation) {

                title.startAnimation(animation_1);
               // rocketAnimation.start();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        new Handler().postDelayed(new Runnable() {



            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity

                finish();
            }
        }, SPLASH_TIME_OUT);
    }
}
