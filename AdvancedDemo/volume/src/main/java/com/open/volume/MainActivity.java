package com.open.volume;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.view.View;

import com.open.applib.activity.BaseActivity;

public class MainActivity extends BaseActivity {

    private CircleVolume circleVolume;
    private final int MSG_CIRCLE = 10000;
    private boolean isStop = false;
    private int count;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_CIRCLE:
                    circleVolume.setVolume((int) msg.obj);
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };

    @Override
    protected void initData() {
        count = getResources().getInteger(R.integer.unit_count);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_main);

        circleVolume = (CircleVolume) findViewById(R.id.circle_volume);
        findViewById(R.id.start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setCircleVolume();
            }
        });
    }

    @Override
    protected void loadData() {

    }

    private void setCircleVolume() {
        Thread td = new Thread(new Runnable() {
            @Override
            public void run() {
                dealCircleVolume();
            }
        });
        td.start();
    }

    /**
     * position 0~360
     * speed 1~100
     */
    private void dealCircleVolume() {
        int vol = 0;
        while (!isStop) {
            // volume 0 ~ count
            vol++;
            vol = vol % (count + 1);
            // send msg to update ui
            Message msg = mHandler.obtainMessage(MSG_CIRCLE, vol);
            mHandler.sendMessage(msg);
            SystemClock.sleep(500);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        isStop = true;

        if (mHandler != null) {
            mHandler.removeMessages(MSG_CIRCLE);
        }
    }
}
