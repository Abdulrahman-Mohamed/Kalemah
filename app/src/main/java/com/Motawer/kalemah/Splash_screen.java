package com.Motawer.kalemah;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.Motawer.kalemah.Auth.SignIn_Activity;

public class Splash_screen extends AppCompatActivity {

    private static  int SPLASH_SCREEN = 2000;


    Animation topAnim,bottomAnim;
    ImageView imageView;
    TextView logo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_splash_screen);

        topAnim = AnimationUtils.loadAnimation(this,R.anim.top_animation);
        bottomAnim = AnimationUtils.loadAnimation(this,R.anim.bottom_animation);

        imageView =findViewById(R.id.splash_image);
        logo =findViewById(R.id.logo);

        imageView.setAnimation(topAnim);
        logo.setAnimation(bottomAnim);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent =new Intent(Splash_screen.this, SignIn_Activity.class);
                startActivity(intent);
                finish();


            }
        }, SPLASH_SCREEN);


    }
}