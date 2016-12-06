package com.mgjr.Utils;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.mgjr.R;
import com.mgjr.mangofinance.MainApplication;

/**
 * Created by Administrator on 2016/7/18.
 */
public class PropertyCircleProgress extends View {

    private Context context;

    /**
     * 画笔对象的引用
     */
    private Paint paint, arcPaint, sweepPaint;

    private Paint textPaint;
    /**
     * 圆环的颜色
     */
    private int roundColor;

    /**
     * 圆环进度的颜色
     */
    private int roundProgressColor;

    /**
     * 中间进度百分比的字符串的颜色
     */
    private int textColor;

    /**
     * 中间进度百分比的字符串的字体
     */
    private float textSize;

    private String text = "";


    /**
     * 圆环的宽度
     */
    private float roundWidth;

    /**
     * 最大进度
     */
    private Double max;

    /**
     * 当前进度
     */
    private Double progress;

    private Double[] progresses;

    private Double balance, withdrawlock, hqb, redeem, jmg, investLock, mgb;

    /**
     * 进度的风格，实心或者空心
     */
    private int style;


    public static final int STROKE = 0;
    public static final int FILL = 1;
    private Paint amountPaint;

    public PropertyCircleProgress(Context context) {
        this(context, null);
    }

    public PropertyCircleProgress(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PropertyCircleProgress(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RoundProgressBar);
        roundColor = typedArray.getColor(R.styleable.RoundProgressBar_roundColor, Color.WHITE);
        roundProgressColor = typedArray.getColor(R.styleable.RoundProgressBar_roundProgressColor, Color.WHITE);
        roundWidth = typedArray.getDimension(R.styleable.RoundProgressBar_roundWidth, 4);
        style = typedArray.getInt(R.styleable.RoundProgressBar_style, 0);
        textPaint = new Paint();
        paint = new Paint();
        arcPaint = new Paint();
        initTextPaint();
        typedArray.recycle();

    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    }

    //画圆
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        arcPaint.setStyle(Paint.Style.FILL);
        arcPaint.setAntiAlias(false);
        arcPaint.setStrokeJoin(Paint.Join.ROUND);      //平滑效果


        /*
        * 外层圆
        * */
        int center = getWidth() / 4;  //圆心的x坐标
        int radius = (int) (center - roundWidth / 2 - 20);//半径
        paint.setColor(roundColor);                //圆环颜色
        paint.setStyle(Paint.Style.STROKE);         //空心模式
        paint.setStrokeWidth(roundWidth);          //圆环宽度
        paint.setAntiAlias(true);                   //消除锯齿
        paint.setStrokeJoin(Paint.Join.ROUND);      //平滑效果
        canvas.drawCircle(center + radius, center + 40, radius, paint);


        //定义的圆弧的形状和大小的界限
//        RectF oval = new RectF(radius + 55, 62, center
//                + radius * 2 + 30, center + radius + 40);
//        //内部扇形的形状和大小界限
//        RectF arcoval = new RectF(radius + roundWidth / 4 + 60, roundWidth / 2 + 60, center
//                + radius * 2 - roundWidth / 2 + 30, center + radius - roundWidth / 2 + 40);

        RectF oval = new RectF(getWidth() / 2 - radius, getWidth() / 4 - radius + roundWidth/2, getWidth() / 2 + radius, getWidth() / 4 + radius + roundWidth/2);
        RectF arcoval = new RectF(getWidth() / 2 - radius, getWidth() / 4 - radius + roundWidth/2, getWidth() / 2 + radius, getWidth() / 4 + radius + roundWidth/2);



        /*
        * 画圆弧1、扇形
        * */
        paint.setColor(Color.parseColor("#1ae361"));
        arcPaint.setColor(Color.parseColor("#441ae361"));
        float angle1 = 0;
        if (balance != null) {
            if (balance == 0) {
                angle1 = 0;
            } else {
                angle1 = (float) (360 * balance / max);
            }
            canvas.drawArc(oval, 270, -angle1, false, paint);
            canvas.drawArc(arcoval, 270, -angle1, true, arcPaint);
        }
        /*
        * 画圆弧2
        * */
        paint.setColor(Color.parseColor("#a3efbe"));
        arcPaint.setColor(Color.parseColor("#44a3efbe"));
        float angle2 = 0;
        if (withdrawlock != null) {
            if (withdrawlock == 0) {
                angle2 = 0;
            } else {
                angle2 = (float) (360 * withdrawlock / max);
            }
            canvas.drawArc(oval, 270 - angle1, -angle2, false, paint);
            canvas.drawArc(arcoval, 270 - angle1, -angle2, true, arcPaint);
        }
        /*
        * 画圆弧3
        * */
        paint.setColor(Color.parseColor("#00a1e9"));
        arcPaint.setColor(Color.parseColor("#4400a1e9"));
        float angle3 = 0;
        if (hqb != null) {
            if (hqb == 0) {
                angle3 = 0;
            } else {
                angle3 = (float) (360 * hqb / max);
            }
            canvas.drawArc(oval, 270 - (angle1 + angle2), -angle3, false, paint);
            canvas.drawArc(arcoval, 270 - (angle1 + angle2), -angle3, true, arcPaint);
        }
        /*
        * 画圆弧4
        * */
        paint.setColor(Color.parseColor("#b5e5fc"));
        arcPaint.setColor(Color.parseColor("#44b5e5fc"));
        float angle4 = 0;
        if (redeem != null) {
            if (redeem == 0) {
                angle4 = 0;
            } else {
                angle4 = (float) (360 * redeem / max);
            }
            canvas.drawArc(oval, 270 - (angle1 + angle2 + angle3), -angle4, false, paint);
            canvas.drawArc(arcoval, 270 - (angle1 + angle2 + angle3), -angle4, true, arcPaint);
        }
        /*
        * 画圆弧5
        * */
        paint.setColor(Color.parseColor("#efa800"));
        arcPaint.setColor(Color.parseColor("#44efa800"));
        float angle5 = 0;
        if (jmg != null) {
            if (jmg == 0) {
                angle5 = 0;
            } else {
                angle5 = (float) (360 * jmg / max);
            }
            canvas.drawArc(oval, 270 - (angle1 + angle2 + angle3 + angle4), -angle5, false, paint);
            canvas.drawArc(arcoval, 270 - (angle1 + angle2 + angle3 + angle4), -angle5, true, arcPaint);
        }
        /*
        * 画圆弧6
        * */
        paint.setColor(Color.parseColor("#fedc95"));
        arcPaint.setColor(Color.parseColor("#44fedc95"));
        float angle6 = 0;
        if (investLock != null) {
            if (investLock == 0) {
                angle6 = 0;
            } else {
                angle6 = (float) (360 * investLock / max);
            }
            canvas.drawArc(oval, 270 - (angle1 + angle2 + angle3 + angle4 + angle5), -angle6, false, paint);
            canvas.drawArc(arcoval, 270 - (angle1 + angle2 + angle3 + angle4 + angle5), -angle6, true, arcPaint);
        }

        /*
        * 画圆弧7
        * */
        paint.setColor(Color.parseColor("#fedc95"));
        arcPaint.setColor(Color.parseColor("#44fedc95"));
        float angle7 = 0;
        if (mgb != null) {
            if (mgb == 0) {
                angle7 = 0;
            } else {
                angle7 = (float) (360 * mgb / max);
            }
            canvas.drawArc(oval, 270 - (angle1 + angle2 + angle3 + angle4 + angle5 + angle6), -angle7, false, paint);
            canvas.drawArc(arcoval, 270 - (angle1 + angle2 + angle3 + angle4 + angle5 + angle6), -angle7, true, arcPaint);
        }
        /*
        * text
        * */


        float textWidth = textPaint.measureText("总资产(元)");   //测量字体宽度，我们需要根据字体的宽度设置在圆环中间
        float amountWidth = amountPaint.measureText(text);
        if (text == null) {
            text = "0.0";
        }
        canvas.drawText("总资产(元)", getWidth() / 2 - textWidth / 2, 40 + radius, textPaint);
        canvas.drawText(text, getWidth() / 2 - amountWidth / 2, radius + 100, amountPaint);


    }

    private void initTextPaint() {
        textPaint.setColor(Color.parseColor("#999999"));
        textPaint.setTextSize(ScreenUtils.dipToPx(MainApplication.getContext(), 12));
        textPaint.setStrokeWidth(3);
        textPaint.setAntiAlias(true);

        amountPaint = new Paint();
        amountPaint.setColor(Color.parseColor("#333333"));
        amountPaint.setTextSize(ScreenUtils.dipToPx(MainApplication.getContext(), 17));
        amountPaint.setAntiAlias(true);
    }


    public synchronized Double getMax() {
        return max;
    }

    /**
     * 设置进度的最大值
     *
     * @param max
     */
    public synchronized void setMax(Double max) {
        if (max != null) {
            if (max < 0) {
                max = 0.0;
            }
            this.max = max;
        }
    }

    /**
     * 获取进度.需要同步
     *
     * @return
     */
    public synchronized Double getProgress() {
        return progress;
    }

    /**
     * 设置进度，此为线程安全控件，由于考虑多线的问题，需要同步
     * 刷新界面调用postInvalidate()能在非UI线程刷新
     *
     * @param progress
     */
    public synchronized void setProgress(Double progress) {
        if (max != null) {
            if (progress < 0) {
//            throw new IllegalArgumentException("progress not less than 0");
                progress = 0.0;
            }
            if (progress > max) {
                progress = max;
            }
            if (progress <= max) {
                this.progress = progress;
                postInvalidate();
            }
        }
    }

    public Double getBalance() {
        return balance;
    }

    public synchronized void setBalance(Double balance) {
        if (balance != null && max != null) {
            if (balance < 0) {
                balance = 0.0;
            }
            if (balance > max) {
                balance = max;
            }
            if (balance <= max) {
                this.balance = balance;
                postInvalidate();
            }
        }
    }

    public Double getWithdrawlock() {
        return withdrawlock;
    }

    public synchronized void setWithdrawlock(Double withdrawlock) {
        if (max != null) {
            if (withdrawlock < 0) {
                withdrawlock = 0.0;
            }
            if (withdrawlock > max) {
                withdrawlock = max;
            }
            if (withdrawlock <= max) {
                this.withdrawlock = withdrawlock;
                postInvalidate();
            }
        }
    }

    public Double getHqb() {
        return hqb;
    }

    public synchronized void setHqb(Double hqb) {
        if (max != null) {
            if (hqb < 0) {
                hqb = 0.0;
            }
            if (hqb > max) {
                hqb = max;
            }
            if (hqb <= max) {
                this.hqb = hqb;
                postInvalidate();
            }
        }
    }

    public Double getRedeem() {
        return redeem;
    }

    public synchronized void setRedeem(Double redeem) {
        if (max != null) {
            if (redeem < 0) {
                redeem = 0.0;
            }
            if (redeem > max) {
                redeem = max;
            }
            if (redeem <= max) {
                this.redeem = redeem;
                postInvalidate();
            }
        }
    }

    public Double getJmg() {
        return jmg;
    }

    public synchronized void setJmg(Double jmg) {
        if (max != null) {
            if (jmg < 0) {
                jmg = 0.0;
            }
            if (jmg > max) {
                jmg = max;
            }
            if (jmg <= max) {
                this.jmg = jmg;
                postInvalidate();
            }
        }
    }

    public Double getInvestLock() {
        return investLock;
    }

    public synchronized void setInvestLock(Double investLock) {
        if (max != null) {
            if (investLock < 0) {
                investLock = 0.0;
            }
            if (investLock > max) {
                investLock = max;
            }
            if (investLock <= max) {
                this.investLock = investLock;
                postInvalidate();
            }
        }
    }

    public Double getMgb() {
        return mgb;
    }

    public synchronized void setMgb(Double mgb) {
        if (max != null) {
            if (mgb < 0) {
                mgb = 0.0;
            }
            if (mgb > max) {
                mgb = max;
            }
            if (mgb <= max) {
                this.mgb = mgb;
                postInvalidate();
            }
        }
    }

    public int getCricleColor() {
        return roundColor;
    }

    public void setCricleColor(int cricleColor) {
        this.roundColor = cricleColor;
    }

    public int getCricleProgressColor() {
        return roundProgressColor;
    }

    public void setCricleProgressColor(int cricleProgressColor) {
        this.roundProgressColor = cricleProgressColor;
    }

    public int getTextColor() {
        return textColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }

    public float getTextSize() {
        return textSize;
    }

    public void setTextSize(float textSize) {
        this.textSize = textSize;
    }

    public float getRoundWidth() {
        return roundWidth;
    }

    public void setRoundWidth(float roundWidth) {
        this.roundWidth = roundWidth;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
