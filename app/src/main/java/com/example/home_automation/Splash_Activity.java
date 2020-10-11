package com.example.home_automation;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

public class Splash_Activity extends AppCompatActivity {
    private static final String TAG ="splash";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                    Intent i = new Intent(Splash_Activity.this,Main_Page.class);
                    startActivity(i);
                    finish();
                } catch (InterruptedException e) {
                    Log.d(TAG,""+e.getMessage());
                }
            }
        });
        thread.start();
    }

    @Override
    public void onBackPressed() {

    }
}
