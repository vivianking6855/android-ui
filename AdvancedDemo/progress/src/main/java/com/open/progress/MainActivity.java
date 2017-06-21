package com.open.progress;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.view.View;

import com.open.applib.activity.BaseActivity;

import io.reactivex.disposables.CompositeDisposable;

public class MainActivity extends BaseActivity {
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    private CircleProgress circleProgress;
    private final int MSG_CIRCLE = 10000;
    private int speed = 1;
    private boolean isStop = false;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_CIRCLE:
                    circleProgress.setProgress((int)msg.obj);
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };

    @Override
    protected void initData() {
        // circle progress speed
        int sp = getResources().getInteger(R.integer.speed);
        speed = sp <= 0 ? 1 : sp;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_main);
        circleProgress = (CircleProgress) findViewById(R.id.circle_progress);
        findViewById(R.id.start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setCircleProgress();
            }
        });
    }

    @Override
    protected void loadData() {

    }

    private void setCircleProgress() {
        Thread td = new Thread(new Runnable() {
            @Override
            public void run() {
                dealCircleProgress();
            }
        });
        td.start();
    }

    /**
     * position 0~360
     * speed 1~100
     */
    private void dealCircleProgress() {
        int pos = 0;
        while (!isStop) {
            // count position 0~360
            pos++;
            pos = pos % 360;
            // send msg to update ui
            Message msg = mHandler.obtainMessage(MSG_CIRCLE, pos);
            mHandler.sendMessage(msg);
            // sleep speed time (1~100)
            SystemClock.sleep(500 / speed);
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
