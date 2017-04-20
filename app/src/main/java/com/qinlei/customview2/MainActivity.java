package com.qinlei.customview2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    private CustomView2 customView2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        customView2 = (CustomView2) findViewById(R.id.view);
    }

    public void start(View view) {
        customView2.start();
    }

    public void stop(View view) {
        customView2.stop();
    }

    public void reset(View view) {
        customView2.reset();
    }
}
