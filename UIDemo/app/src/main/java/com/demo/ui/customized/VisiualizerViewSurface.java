package com.demo.ui.customized;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.media.audiofx.Visualizer;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.demo.ui.uidemo.R;

import java.io.BufferedReader;
import java.io.Console;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by Venjee_Shen on 2016/8/8.
 */
public class VisiualizerViewSurface extends SurfaceView implements
        SurfaceHolder.Callback,
        Visualizer.OnDataCaptureListener {

    private static final String TAG = "VisiualizerViewSurface";
    private final int MSG_DRAW = 1;
    private final int DN_SW = 1;

    private int hgap = 0;
    private float vgap = 0;
    private int levelStep = 0;
    private float strokeWidth = 0;
    private final int blockWidth = (int) getResources().getDimension(R.dimen.visualizer_blockWidth);
    private SurfaceHolder holder;
    private Canvas canvas;

    /**
     * It is the max level.
     */
    private final int MAX_LEVEL = 110;

    /**
     * It is the cylinder number.
     */
    private final int CYLINDER_NUM = 90;
    // protected static int CYLINDER_NUM = 134;

    /**
     * It is the visualizer.
     */
    private Visualizer mVisualizer = null;
    private Context mContext;
    /**
     * It is the paint which is used to draw to visual effect.
     */
    private Paint mPaint = null;
    private Paint mBelowPaint = null;
    /**
     * It is the buffer of fft.
     */
    private byte[] mData = new byte[CYLINDER_NUM];

    private float[] mLines = new float[CYLINDER_NUM * 4];
    private float[] mBelowLines = new float[CYLINDER_NUM * 4];
    private float x, y, k;

    private double a, b, c, ab;
    private byte model, mCurrentData, mPreData;
    /* private int j;
     private int i;*/
    private boolean cpuControl = false;
    private BitmapDrawable dtsLogo;
    private boolean wasInitSuccessful = false;
    private int mReinitCount = 0;
    private int backgroundColor;
    private Runnable mInitVisaulizerRunnable = new Runnable() {
        @Override
        public void run() {
            mReinitCount++;
            if (mReinitCount <= 5)
                initVisualizer();
            else
                mReinitCount = 0;
        }
    };

    private HandlerThread handlerThread;

    private class DrawHandler extends Handler {

        public DrawHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            if (msg.what == MSG_DRAW) {
                drawVisualizer(msg.arg1);
            }
            super.handleMessage(msg);
        }
    }

    private DrawHandler handler;

    public VisiualizerViewSurface(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        backgroundColor = context.getResources().getColor(R.color.app_background);
        setZOrderOnTop(true);
        holder = getHolder();
        holder.setFormat(PixelFormat.TRANSLUCENT);
        holder.addCallback(this);
        if (mPaint == null) {
            mPaint = new Paint();
        }

        if (mBelowPaint == null) {
            mBelowPaint = new Paint();
        }

        cpuControl = cpuIdentification();
    }

    private void drawVisualizer(int i) {
        java.util.Arrays.fill(mLines, 0);
        java.util.Arrays.fill(mBelowLines, 0);

        for (i = 0; i < CYLINDER_NUM; i++) {
            if (mData[i] > 0) {
                y = getHeight() / 2 - toDp(1, getResources()) - (mData[i]) * vgap;
                k = getHeight() / 2 + toDp(1, getResources()) + (mData[i]) * vgap;

                x = strokeWidth / 2 + hgap + i * (hgap + strokeWidth);

                mLines[i * 4] = x;
                mLines[i * 4 + 1] = getHeight() / 2 - toDp(1, getResources());
                mLines[i * 4 + 2] = x;
                mLines[i * 4 + 3] = y;

                mBelowLines[i * 4] = x;
                mBelowLines[i * 4 + 1] = getHeight() / 2 + toDp(1, getResources());
                mBelowLines[i * 4 + 2] = x;
                mBelowLines[i * 4 + 3] = k;
            }
        }
        try {
            canvas = holder.lockCanvas();
            canvas.drawColor(backgroundColor);
            canvas.drawLines(mLines, mPaint);
            canvas.drawLines(mBelowLines, mBelowPaint);
            if (dtsLogo != null)
                canvas.drawBitmap(dtsLogo.getBitmap(), getWidth() - dtsLogo.getBitmap().getWidth(), getHeight() / 2, null);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (canvas != null) {
                holder.unlockCanvasAndPost(canvas);
            }
        }

    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        int DN_W = getWidth();
        int DN_H = getHeight();

        Shader mShader = new LinearGradient(0, DN_H / 2, 0, 0, new int[]{0xff8d36ff, 0xff35e0ff}, null, Shader.TileMode.CLAMP);
        Shader mBelowShader = new LinearGradient(0, DN_H / 2, 0, DN_H - toDp(55, getResources()), new int[]{0x7d8d36ff, 0x00000000},
                new float[]{0.15f, 1.0f}, Shader.TileMode.CLAMP);

        mPaint.setShader(mShader);
        mBelowPaint.setShader(mBelowShader);

        float xr = (float) (DN_W - (CYLINDER_NUM * blockWidth)) / (CYLINDER_NUM - 1);

        strokeWidth = DN_SW * xr; // line width
        mPaint.setStrokeWidth(strokeWidth);
        mBelowPaint.setStrokeWidth(strokeWidth);

        hgap = blockWidth;
        vgap = ((float) (DN_H / 2) / (MAX_LEVEL + 1)); //line
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        handlerThread = new HandlerThread("visiualizer");
        handlerThread.start();
        handler = new DrawHandler(handlerThread.getLooper());
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        handlerThread.quit();
        handler.removeMessages(MSG_DRAW);
    }

    private void initVisualizer() {
        levelStep = 128 / MAX_LEVEL;
        if (mVisualizer == null) {
            // Visualization cannot be hooked on audio session 0 since the audio output is USB
            try {
                mVisualizer = VisualizeManager.getInstance();
                wasInitSuccessful = true;
                removeCallbacks(mInitVisaulizerRunnable);
            } catch (RuntimeException e) {
                Log.w(TAG, "Init fail. Re-init in 1 seconds later", e);
                wasInitSuccessful = false;
                postDelayed(mInitVisaulizerRunnable, 1000);
            }
            if (wasInitSuccessful) {
                mVisualizer.setEnabled(false);
                mVisualizer.setCaptureSize(Visualizer.getCaptureSizeRange()[1]);
                mVisualizer.setDataCaptureListener(this, Visualizer.getMaxCaptureRate(), false, true);
                if (dtsLogo == null) {
                    dtsLogo = (BitmapDrawable) mContext.getResources().getDrawable(R.drawable.asus_audio_wizard_bg_dts_logo);
                }
                mReinitCount = 0;
            }
        }
    }

    public void setVisualizer(boolean control) {
        initVisualizer();
        try {
            if (mVisualizer != null && mVisualizer.getEnabled() != control) {
                mVisualizer.setEnabled(control);
                if (!control) {
                    Thread.sleep(100);
                    VisualizeManager.destoryVisualizer();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (!control) {
            mVisualizer = null;
        }
    }

    private boolean cpuIdentification() {
        String cupInfo = getCpuInfo();

        if (cupInfo.contains("Intel")) {
            return true;
        }
        return false;
    }

    private String getCpuInfo() {
        String str1 = "/proc/cpuinfo";
        String str2 = "";
        String[] cpuInfo = {"", ""};
        String[] arrayOfString;
        try {
            FileReader fr = new FileReader(str1);
            BufferedReader localBufferedReader = new BufferedReader(fr, 8192);
            str2 = localBufferedReader.readLine();
            arrayOfString = str2.split("\\s+");
            for (int i = 2; i < arrayOfString.length; i++) {
                cpuInfo[0] = cpuInfo[0] + arrayOfString[i] + " ";
            }
            str2 = localBufferedReader.readLine();
            arrayOfString = str2.split("\\s+");
            cpuInfo[1] += arrayOfString[2];
            localBufferedReader.close();
        } catch (IOException e) {
        }
        // Log.d(TAG, "cpuinfo:" + cpuInfo[0] + " " + cpuInfo[1]);
        return "1-cpu " + cpuInfo[0] + "2-cpu " + cpuInfo[1];
    }

    @Override
    public void onWaveFormDataCapture(Visualizer visualizer, byte[] waveform, int samplingRate) {
        //do nothing
    }

    private byte temp = 0;

    @Override
    public void onFftDataCapture(Visualizer visualizer, byte[] fft, int samplingRate) {

        int i = 0;
        int j = 0;
        for (i = 2, j = 0; i < fft.length && j < CYLINDER_NUM; i += 1, j++) {
            if (j > CYLINDER_NUM / 1.5) {
                a = Math.hypot(fft[i], fft[i + 1]);
                b = Math.hypot(fft[i + 2], fft[i + 3]);
                c = Math.hypot(fft[i + 4], fft[i + 5]);
                ab = Math.hypot(a, b);
                model = (byte) Math.hypot(ab, c);
                i += 5;
            } else if (j > CYLINDER_NUM / 2) {
                a = Math.hypot(fft[i], fft[i + 1]);
                b = Math.hypot(fft[i + 2], fft[i + 3]);
                ab = Math.hypot(a, b);
                model = (byte) Math.hypot(ab, fft[i + 4]);
                i += 4;
            } else {
                model = fft[i];
            }
            setData(model, j);
        }

        // Check whether there is any audio data should be updated and draw
        // Use byte:temp to control the redrawing of visualization
        for (int k = 0; k < mData.length - 1; k++) {
            temp += mData[k + 1];
        }
        if (temp > 0) {
            // redraw
            sendDrawMsg(i);
            temp = 0;
        } else if (temp == 0) {
            // Update the last time then be idled until the data is coming
            sendDrawMsg(i);
            temp = -1;
        }
    }

    private void sendDrawMsg(int i) {
        if (handler != null) {
            Message msg = handler.obtainMessage(MSG_DRAW);
            msg.arg1 = i;
            handler.sendMessage(msg);
        }
    }

    private void setData(byte model, int index) {
        mCurrentData = (byte) (Math.abs(model) / levelStep);
        if (cpuControl) {
            if (Math.abs(model) >= 1 && Math.abs(model) < 90) {
                mCurrentData += 25;
            } else if (Math.abs(model) > 0) {
            }
        } else {
            if (Math.abs(model) > 1 && Math.abs(model) < 90) {
                mCurrentData += 25;
            } else if (Math.abs(model) == 1) {
                mCurrentData = 0;
            }
        }

        mPreData = mData[index];
        if (mCurrentData > mPreData) {
            mData[index] = mCurrentData;
        } else {
            // Simple regression of each single line of visualization
            if (mPreData > 96) {
                mData[index] -= 2;
            } else if (mPreData > 32) {
                mData[index] -= 2;
            } else if (mPreData > 0) {
                mData[index] -= 1;
            }
        }

        Log.d(TAG, "mData.length :" + mData.length);
    }

    private static class VisualizeManager {

        private static Visualizer visualizer = null;

        synchronized public static Visualizer getInstance() {
            if (visualizer == null) {
                visualizer = new Visualizer(0);
            }
            return visualizer;
        }

        public static void destoryVisualizer() {
            if (visualizer != null) {
                visualizer.release();
                visualizer = null;
            }
        }
    }

    public float toDp(float input, Resources res) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, input, res.getDisplayMetrics());
    }
}
