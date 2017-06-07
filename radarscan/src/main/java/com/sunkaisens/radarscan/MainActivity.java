package com.sunkaisens.radarscan;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;



import java.util.Random;

public class MainActivity extends Activity implements View.OnTouchListener {

    private static final String TAG = MainActivity.class.getCanonicalName();
    private RadarView mRadarView;
    private FlightDegree flightDegree;

    private float initdegree = 30;
    private boolean isAdd = true;
    private static int UPDATETSPEED = 1001;
    private int speed = 800;
    private int height = 10000;
    private static final String Tag = "当前时速：";

    private static final int addspeed = 0x00;
    private static final int addheight = 0x01;
    private static final int delspeed = 0x02;
    private static final int delheight = 0x03;
    private static final int adddegree = 0x04;
    private static final int deldegree = 0x05;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == UPDATETSPEED) {
                tvSpeed.setText(Tag + speed + "m/s");
                tvHeight.setText("当前高度为：" + height + "m");
                handler.postDelayed(runnable, 500);
            }
        }
    };
    private TextView tvSpeed;
    private TextView tvHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        mRadarView.setSearching(true);
        mRadarView.addPoint();
        mRadarView.addPoint();
    }




    private void initView() {
        mRadarView = (RadarView) findViewById(R.id.radar_view);
        Button btuBottom = (Button) findViewById(R.id.tv_bottom);
        Button btuLeft = (Button) findViewById(R.id.tv_left);
        Button btuRight = (Button) findViewById(R.id.tv_right);
        Button btuTop = (Button) findViewById(R.id.tv_top);
        Button btuUp = (Button) findViewById(R.id.tv_up);
        Button btuDown = (Button) findViewById(R.id.tv_down);
        tvSpeed = (TextView) findViewById(R.id.tv_speed);
        flightDegree = (FlightDegree) findViewById(R.id.flightDegree);
        tvHeight = (TextView) findViewById(R.id.tv_heigth);
        tvHeight.setText("当前高度为：" + height + "m");
        tvSpeed.setText(Tag + speed + "m/s");
        btuLeft.setOnTouchListener(this);
        btuRight.setOnTouchListener(this);
        btuUp.setOnTouchListener(this);
        btuDown.setOnTouchListener(this);
        btuTop.setOnTouchListener(this);
        btuBottom.setOnTouchListener(this);
        handler.postDelayed(runnable, 1000);
    }





    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {

            case MotionEvent.ACTION_DOWN:
                Log.d(TAG, "按钮按下");
                if (v.getId() == R.id.tv_up) {
                    addordel(addspeed);

                } else if (v.getId() == R.id.tv_down) {
                    addordel(delspeed);

                } else if (v.getId() == R.id.tv_top) {
                    addordel(addheight);
                } else if (v.getId() == R.id.tv_bottom) {
                    addordel(delheight);
                }else if (v.getId()==R.id.tv_left){

                    addordel(deldegree);
                }else if (v.getId()==R.id.tv_right){
                    addordel(adddegree);
                }
                break;
            case MotionEvent.ACTION_UP:

                Log.d(TAG, "按钮松开");
                isAdd = false;

                break;
        }
        return true;
    }

    private void addordel(final int speedOrgeightOrdegree) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                isAdd = true;
                while (isAdd) {

                    SystemClock.sleep(10);
                    if (speedOrgeightOrdegree == addspeed) {
                        speed++;
                        handler.sendEmptyMessage(UPDATETSPEED);
                    } else if (speedOrgeightOrdegree == addheight) {

                        height++;
                        handler.sendEmptyMessage(UPDATETSPEED);
                    } else if (speedOrgeightOrdegree == delspeed) {
                        speed--;
                        handler.sendEmptyMessage(UPDATETSPEED);
                    } else if (speedOrgeightOrdegree == delheight) {
                        height--;
                        handler.sendEmptyMessage(UPDATETSPEED);
                    }else if (speedOrgeightOrdegree==adddegree){
                        initdegree++;
                        flightDegree.setHandler(handler,initdegree);
                        Log.d(TAG,"degree"+initdegree);
                    }else if (speedOrgeightOrdegree==deldegree){
                        initdegree--;
                        flightDegree.setHandler(handler,initdegree);
                    }

                }
            }
        }) {
        }.start();

    }
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            int dt = new Random().nextInt(5) - 2;
            Log.d(TAG, dt + "dt");
            speed = speed + dt;
            height = height + dt;
            handler.sendEmptyMessage(UPDATETSPEED);
        }
    };
    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(runnable);
    }
}