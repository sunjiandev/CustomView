package com.sunkaisens.radarscan;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.Random;

public class FlightDegree extends View {

    private static final String TAG = FlightDegree.class.getCanonicalName();

    private float degree = 30;
    private int width;
    private int height;
    private Paint mpaintline;
    private Paint mpaintcircle;
    private Paint mpainttext;
    private static final int NEED_INVALIDATE = 0x23;
    public static final int SOUTHEAST = 100;//东南
    public static final int NORTHEAST = 101;//东北
    public static final int SOUTHWEST = 102;//西南
    public static final int NORTHWEST = 103;//西北
    private Handler mhandler = new Handler() {
        @RequiresApi(api = Build.VERSION_CODES.N)
        public void handleMessage(Message msg) {

//            if (msg.obj != null) {
//
//            }
            degree = (float) msg.obj;
            Log.d(TAG,degree+"---");
            if (msg.what == NEED_INVALIDATE) {
                Random random = new Random();
                degree = random.nextInt(30) + 30;
                Log.d(TAG, degree + "");
                //重新绘制界面
                invalidate();//告诉UI主线程重新绘制
                //再次发送消息，递归调用，再次监测秒针
                Message mag = Message.obtain();
                msg.arg1 = NEED_INVALIDATE;
                Log.d(TAG, "此处执行了");
//                mhandler.sendEmptyMessageDelayed(NEED_INVALIDATE, 1000);
            }
            switch (msg.arg1) {
                case SOUTHEAST://东南
                    invalidate();

                    break;
                case SOUTHWEST://西南
                    invalidate();
                    break;
                case NORTHEAST://东北
                    invalidate();
                    break;
                case NORTHWEST://西北
                    invalidate();
                    break;
                default:
                    break;
            }
        }

        ;

    };
    private Handler handler;


    public FlightDegree(Context context) {
        super(context);

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public FlightDegree(Context context, AttributeSet attrs) {              //为了不是每次都创建Paint，我们在这里创建
        super(context, attrs);
        mpaintline = new Paint();
        mpaintline.setColor(Color.BLACK);
        //设置线宽
        mpaintline.setStrokeWidth(1);
        //设置抗锯齿
        mpaintline.setAntiAlias(true);


        mpaintcircle = new Paint();
        mpaintcircle.setColor(0xff3278B4);
        mpaintcircle.setStrokeWidth(1);
//        mpaintcircle.setStyle(Paint.Style.STROKE);//设置画的圆是空心
        mpainttext = new Paint();

        mpainttext.setTextSize(10);
        mpainttext.setTextAlign(Paint.Align.CENTER);
        mpainttext.setColor(Color.BLACK);

//        mhandler.sendEmptyMessage(NEED_INVALIDATE);
    }

    //在此方法中进行绘制
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onDraw(Canvas canvas) {

        super.onDraw(canvas);
        //绘制圆形
        canvas.drawCircle(width / 2, height / 2, width / 2 - 20, mpaintcircle);
        Log.d(TAG, width + "---" + height + "--" + (width / 2 - 20));
        //绘制表芯
//        canvas.drawCircle(width/2, height/2, 5, mpaintcircle);

        //绘制表盘时我们采用旋转画布的思想，让画布进行旋转一定角度，绘制
        for (int i = 1; i <= 12; i++) {
            //保存画布当前状态
            canvas.save();
            //指定旋转角度与旋转点
            canvas.rotate(360 / 12 * i, width / 2, height / 2);
            //绘制表盘
            canvas.drawLine(width / 2, height / 2 - (width / 2 - 20), width / 2, height / 2 - width / 2 + 30, mpaintline);
            if (i * 30 == 360) {
                //绘制文字
                canvas.drawText("北", width / 2, height / 2 - width / 2 + 10, mpainttext);
            } else if (i * 30 == 90) {
                canvas.drawText("东", width / 2, height / 2 - width / 2 + 10, mpainttext);
            } else if (i * 30 == 180) {
                canvas.drawText("南", width / 2, height / 2 - width / 2 + 10, mpainttext);
            } else if (i * 30 == 270) {
                canvas.drawText("西", width / 2, height / 2 - width / 2 + 10, mpainttext);
            } else {
                canvas.drawText(i * 30 + "°", width / 2, height / 2 - width / 2 + 10, mpainttext);
            }

            //恢复开始位置
            canvas.restore();
        }

        canvas.drawLine(width / 10 / 2, height / 2, width - width / 10 / 2, height / 2, mpaintline);// 绘制0°~180°对角线
        canvas.drawLine(width / 2, height - width / 10 / 2, width / 2, width / 10 / 2,
                mpaintline);// 绘制90°~270°对角线

        initDegree(canvas);

    }

    private void initDegree(Canvas canvas) {
        canvas.save();
        canvas.rotate(degree, width / 2, height / 2);
        canvas.drawLine(width / 2, height / 2, width/2,width/2-(width/2-20), mpaintline);
        canvas.restore();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //固定
        width = getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec);
        height = getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec);
    }

    public void setHandler(Handler handler, float degree) {

        handler = mhandler;
        Message msg = Message.obtain();
        msg.obj = degree;
        msg.arg1 = NORTHWEST;
        handler.sendMessage(msg);
    }

}