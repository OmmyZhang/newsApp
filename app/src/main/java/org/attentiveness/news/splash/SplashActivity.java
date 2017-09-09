package org.attentiveness.news.splash;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.speech.tts.TextToSpeech;
import android.support.design.widget.NavigationView;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import org.attentiveness.news.R;
import org.attentiveness.news.base.BaseActivity;
import org.attentiveness.news.list.StoryListActivity;

import java.util.Locale;

import butterknife.ButterKnife;

public class SplashActivity extends BaseActivity{

    private CountDownTimer mTimer;
    TextToSpeech myTTS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);

        myTTS = new TextToSpeech(SplashActivity.this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                // 如果装载TTS引擎成功
                if (status == TextToSpeech.SUCCESS) {
                    Toast.makeText(SplashActivity.this,
                            "朗读功能可用", Toast.LENGTH_LONG).show();
                    // 设置使用美式英语朗读
                    int result = myTTS.setLanguage(Locale.CHINA);
                    myTTS.speak("搞个大新闻" , TextToSpeech.QUEUE_FLUSH,null);
                    // 如果不支持所设置的语言
                    if (result != TextToSpeech.LANG_COUNTRY_AVAILABLE
                            && result != TextToSpeech.LANG_AVAILABLE) {
                        Toast.makeText(SplashActivity.this,
                                "暂时不支持这种语言的朗读！", Toast.LENGTH_LONG).show();
                    }
                }
                else
                {
                    Toast.makeText(SplashActivity.this,
                            "朗读功能不可用！", Toast.LENGTH_LONG).show();
                }
            }
        });

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
        if(myTTS != null)
            myTTS.shutdown();
    }

}
