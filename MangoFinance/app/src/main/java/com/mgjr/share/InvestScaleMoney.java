package com.mgjr.share;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.widget.Scroller;

import com.mgjr.R;
import com.mgjr.Utils.LogUtil;

/**
 * Created by CXX on 2016/7/25.
 */
public class InvestScaleMoney extends View {
    private Paint mLinePaint;
    private Paint mValuePaint;
    private Paint mVerticalPaint;     //垂直的线的画笔
    private Paint textPaint;

    private VelocityTracker velocityTracker;
    private Scroller scroller;
    private boolean isCanMove = true;
    private int defaultValue;
    private int moveX, lastMoveX = 0;
    private boolean isDrawText;
    private int lastX;
    private int lastCurrentMoveX;
    private boolean isLeft = false;
    private int mStartX = 0, mStartY, mStopX, mFirstX = 0;
    private int mWidth;

    private boolean isTouch;

    public InvestScaleMoney(Context context) {
        super(context);
        scroller = new Scroller(context);
        init();
    }

    public InvestScaleMoney(Context context, AttributeSet attrs) {
        super(context, attrs);
        scroller = new Scroller(context);
        init();
    }

    public InvestScaleMoney(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        scroller = new Scroller(context);
        init();
    }

    private void init() {

        mLinePaint = new Paint();
        mLinePaint.setAntiAlias(true);
        mLinePaint.setStyle(Paint.Style.STROKE);
        mLinePaint.setStrokeWidth(2);
        mLinePaint.setColor(0xFF999999);
        mVerticalPaint = new Paint();
        mVerticalPaint.setAntiAlias(true);
        mVerticalPaint.setStyle(Paint.Style.STROKE);
        mVerticalPaint.setStrokeWidth(2);
        mVerticalPaint.setColor(0xFF999999);

        textPaint = new Paint();
        textPaint.setAntiAlias(true);
        textPaint.setStrokeWidth(2);
        textPaint.setTextSize(30);
        textPaint.setColor(0xFF999999);
        textPaint.setTextAlign(Paint.Align.CENTER);

        mValuePaint = new Paint();
        mValuePaint.setAntiAlias(true);
        mValuePaint.setStyle(Paint.Style.STROKE);
        mValuePaint.setStrokeWidth(3);
        mValuePaint.setColor(0xFFFE7F1A);




    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(measureWidth(widthMeasureSpec), measureHeight(heightMeasureSpec));
//        initSize();
    }

    /**
     * 测量高度
     *
     * @param heightMeasureSpec
     * @return
     */
    private int measureHeight(int heightMeasureSpec) {
        int mode = MeasureSpec.getMode(heightMeasureSpec);
        int size = MeasureSpec.getSize(heightMeasureSpec);
        //默认高度,单位sp;
        defaultValue = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 150, getResources().getDisplayMetrics());
        switch (mode) {
            case MeasureSpec.AT_MOST:
                //最大值模式下(wrap_content),最大高度是150sp
                int value = Math.min(size, defaultValue);
                return value;
            case MeasureSpec.EXACTLY:
                //EXACTLY模式下,高度为指定的高度
                return size;
        }
        return size;
    }

    /**
     * 测量宽度
     *
     * @param widthMeasureSpec
     * @return
     */
    private int measureWidth(int widthMeasureSpec) {
        int mode = MeasureSpec.getMode(widthMeasureSpec);
        int size = MeasureSpec.getSize(widthMeasureSpec);
        //默认宽高;
        defaultValue = Integer.MAX_VALUE;
        switch (mode) {
            case MeasureSpec.AT_MOST:
                //最大值模式,一般来说肯定会小于Integer.MAX_VALUE,所以相当于match_parent
                int value = Math.min(size, defaultValue);
                return value;

            case MeasureSpec.EXACTLY:
                return size;

        }
        return size;

    }

    @Override
    protected void onDraw(Canvas canvas) {
        //20代表每个刻度之间的像素
        //画底部的横线
//        canvas.drawLine(0, mStartY, getWidth(), mStartY, mLinePaint);
        //画中间的值
//        canvas.drawLine(mWidth / 2, mStartY + 20, mWidth / 2, 0, mValuePaint);
        //画中间的图片
        Bitmap invest_calc_center_bg = BitmapFactory.decodeResource(getResources(), R.drawable.invest_calc_center_bg);
        canvas.drawBitmap(invest_calc_center_bg, (mWidth - invest_calc_center_bg.getWidth()) / 2, 0, mValuePaint);
//        LogUtil.e("当前值为" + (-lastMoveX + mWidth / 2 + 7) / 20 + "原值为" + (-lastMoveX + mWidth / 2));

        if (!isTouch){
            if (moveScaleInterface != null ) {

                int value = (-lastMoveX + mWidth / 2 + 7) / 20 * 100;
                moveScaleInterface.getValue(value);
            }
        }

        for (int start = 0; start < mWidth; start++) {
            mVerticalPaint.setColor(0xFFcccccc);
            //普通刻度是40px的高度
            int top = mStartY - 50;
            int bottom = mStartY;
            //lastMoveX变成负的是因为向左画为负 实际意义向左画为增大
            //lastMoveX+start是因为从屏幕左侧开始为0加lastMoveX为实际数值
            if ((-lastMoveX + start) % (20 * 10) == 0) {
                mVerticalPaint.setColor(0xFF999999);
                //含有数值刻度是80px的高度
                top = top - 20;
                bottom = bottom + 20;
                isDrawText = true;
            } else {
                isDrawText = false;
            }
            if ((-lastMoveX + start) % 20 == 0) {
                //画刻度
                if ((-lastMoveX + start) >= 0 && (-lastMoveX + start) <= 2000000 * 20) {
                    canvas.drawLine(start, bottom, start, top, mVerticalPaint);
                }

            }

            if (isDrawText) {
                //画刻度值
                if ((-lastMoveX + start) >= 0 && (-lastMoveX + start) <= 2000000 * 20)
                    canvas.drawText((-lastMoveX + start) * 100 / 20 + "", start, bottom + 30, textPaint);
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (velocityTracker == null) {
            velocityTracker = VelocityTracker.obtain();
        }
        velocityTracker.addMovement(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                isTouch = true;
                lastX = (int) event.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                moveX = (int) (event.getX() - lastX);
                if (lastCurrentMoveX == moveX) {
                    return true;
                }
                lastMoveX = lastMoveX + moveX;
                if (moveX < 0) {
                    //向左滑动
                    isLeft = true;
                    if (-lastMoveX + mWidth / 2 > 2000000 * 20) {
                        lastMoveX = lastMoveX - moveX;
                        return true;
                    }
                } else {
                    //向右滑动
                    isLeft = false;
                    if (lastMoveX > mWidth / 2) {
                        lastMoveX = lastMoveX - moveX;
                        return true;
                    }
                }
                lastCurrentMoveX = moveX;
                lastX = (int) event.getX();

                if (moveScaleInterface != null ) {

                    int value = (-lastMoveX + mWidth / 2 + 7) / 20 * 100;
                    Log.d("mWidth====",String.valueOf(mWidth)+"   "+lastMoveX+"   "+value);
                    moveScaleInterface.getValue(value);
                }

                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                isTouch = false;

                velocityTracker.computeCurrentVelocity(1000);
                float xVelocity = velocityTracker.getXVelocity();
                float yVelocity = velocityTracker.getYVelocity();
                if (Math.abs(xVelocity) < 800) {
                    return true;
                }


                scroller.fling(200, mStartY, (int) (-Math.abs(xVelocity) + 0.5), (int) (Math.abs(yVelocity) + 0.5), 000, 10080, 0, 1920);
                velocityTracker.recycle();
                velocityTracker = null;
                break;


        }
        return true;
    }

    @Override
    public void computeScroll() {
        if (scroller.computeScrollOffset()) {
            int currX = scroller.getCurrX();
            if (isLeft) {
                lastMoveX = lastMoveX - currX;
            } else {
                lastMoveX = lastMoveX + currX;
            }
            //向右滑动
            if (lastMoveX > mWidth / 2) {
                lastMoveX = lastMoveX - currX;
                return;
            }
            //向左
            if (-lastMoveX + mWidth / 2 > 2000000 * 20) {
                lastMoveX = lastMoveX - moveX;
                return;
            }
            invalidate();

        }
    }

    public void initSize() {
//        LogUtil.e("getHeight" + getHeight());
        //初始化高度为当前控件设置高度的一半
        mStartY = getHeight() / 2;
        //初始化宽度为当前控件设置的宽度
        mWidth = getWidth();
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        if (hasWindowFocus){
            initSize();
            moveScaleInterface.initValue();
        }
    }

    private MoveScaleInterface moveScaleInterface;

    public void setMoveScaleInterface(MoveScaleInterface moveScaleInterface) {
        this.moveScaleInterface = moveScaleInterface;
    }

    public interface MoveScaleInterface {
        public void getValue(int value);
        public void initValue();
    }

    public void setValue(int value){
        if (mWidth != 0){
            lastMoveX = -(int)(value/100.0*20-mWidth/2);
            invalidate();
        }

    }


}

