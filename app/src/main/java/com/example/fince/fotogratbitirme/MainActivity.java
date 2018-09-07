package com.example.fince.fotogratbitirme;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.graphics.Region;
import android.graphics.Typeface;
import android.os.Build;
import android.os.CountDownTimer;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity {

    LinearLayout l1,l2;
    Animation uptodown,downtoup;
    TextView tw;

    int sayac=0;
    final String PREFS_NAME = "MyPrefsFile";


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkAuth();
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        l1=(LinearLayout)findViewById(R.id.l1);
        l2=(LinearLayout)findViewById(R.id.l2);
        uptodown= AnimationUtils.loadAnimation(this,R.anim.uptodown);
        downtoup= AnimationUtils.loadAnimation(this,R.anim.downtoup);
        l1.setAnimation(uptodown);
        l2.setAnimation(downtoup);
        tw=(TextView)findViewById(R.id.tw);
        //font değiştirme
        Typeface myCustomTp=Typeface.createFromAsset(getAssets(), "font/blackops.ttf");
        tw.setTypeface(myCustomTp);





            new CountDownTimer(2000,1000){

                @Override
                public void onTick(long l) {

                }

                @Override
                public void onFinish() {


                    degis();
                    SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
                    //eğer uygulamanın ilk açıldığı zaman ise
                    if (settings.getBoolean("my_first_time", true)) {
                        //the app is being launched for first time, do something
                        degis();

                        // first time task

                        // record the fact that the app has been started at least once
                        settings.edit().putBoolean("my_first_time", false).commit();
                    }
                    else
                    {
                        //eğer birden fazla açılış ise
                    }


                }
            }
.start();







    }
    void checkAuth()
    {

        FirebaseAuth mAuth= FirebaseAuth.getInstance();
        LoginManager.getInstance().logOut();
        if(mAuth!= null)
            mAuth.signOut();
    }
    public void degis()
    {

        Intent intent=new Intent(this,tutorialScreens.class);
        startActivity(intent);
        finish();
    }







}
