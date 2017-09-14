package com.java.group11.splash;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.design.widget.NavigationView;
import android.view.Window;
import android.view.WindowManager;

import com.java.group11.R;
import com.java.group11.base.BaseActivity;
import com.java.group11.globalSetting.GlobalSetting;
import com.java.group11.globalSetting.JSONStore;
import com.java.group11.list.StoryListActivity;

import butterknife.ButterKnife;

public class SplashActivity extends BaseActivity{

    private CountDownTimer mTimer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);

        JSONStore dataLoader = new JSONStore(this);
        GlobalSetting.setINSTANCE(dataLoader.loadSettings());

        init();
    }


    @Override
    protected void setupDrawerContent(NavigationView navigationView) {
        //do nothing
    }


    private void init() {

        mTimer = new CountDownTimer(2000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                navigate();
                finish();
            }
        };
        mTimer.start();
    }

    private void navigate() {
        Intent intent = new Intent(this, StoryListActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
    }

}
