package android.vaunt.activities;

import android.app.usage.ConfigurationStats;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.vaunt.Constants;
import android.vaunt.R;
import android.vaunt.utility.Utils;
import android.view.Window;
import android.widget.ImageView;


public class SplashActivity extends BaseActivity {
    ImageView imgvSpinner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash);
        Utils.InitAppPath();
        Utils.initImageLoader(this);

        imgvSpinner = (ImageView)findViewById(R.id.imgvSpinner);

        AnimationDrawable spinner = (AnimationDrawable) imgvSpinner.getBackground();
        spinner.start();

        Thread m_Thread = new Thread(new Timer());
        m_Thread.start();
    }

    private class Timer implements Runnable{

        @Override
        public void run() {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
            boolean flag = pref.getBoolean(Constants.kUDLoggedIn, false);

            if(!flag){
                startActivity(new Intent(SplashActivity.this, OnboardingActivity.class));
            }else{
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
            }


//            startActivity(new Intent(SplashActivity.this, CameraActivity.class));
//            finish();
        }
    }
}
