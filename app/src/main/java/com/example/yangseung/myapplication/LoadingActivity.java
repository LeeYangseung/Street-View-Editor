package com.example.yangseung.myapplication;

/**
 * Created by Yangseung on 2016-11-23.
 */

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class LoadingActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        Handler hd = new Handler();
        hd.postDelayed(new splashhandler() , 3000); // 3초 후에 hd Handler 실행
    }

    private class splashhandler implements Runnable{
        public void run() {
            startActivity(new Intent(getApplication(), MapsActivity.class)); // 로딩이 끝난후 이동할 Activity
            LoadingActivity.this.finish(); // 로딩페이지 Activity Stack에서 제거
        }
    }

}