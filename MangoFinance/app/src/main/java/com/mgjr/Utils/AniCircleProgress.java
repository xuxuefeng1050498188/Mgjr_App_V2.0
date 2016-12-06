package com.mgjr.Utils;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

import com.mgjr.R;

/**
 * Created by Administrator on 2016/7/18.
 */
public class AniCircleProgress extends View {

    private Context context;

    /**
     * 画笔对象的引用
     */
    private Paint paint,arcPaint;

    private TextPaint textPaint;
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

    private String text;


    /**
     * 圆环的宽度
     */
    private float roundWidth = 24;

    /**
     * 最大进度
     */
    private int max;

    /**
     * 当前进度
     */
    private int progress;

    /**
     * 第二进度
     */
    private int secondProgress;
    /**
     * 是否显示中间的进度
     */
    private boolean textIsDisplayable;

    /**
     * 进度的风格，实心或者空心
     */
    private int style;

    private int degree = 0;
    public static final int STROKE = 0;
    public static final int FILL = 1;

    public AniCircleProgress(Context context) {
        this(context, null);
    }

    public AniCircleProgress(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AniCircleProgress(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RoundProgressBar);
        roundProgressColor = typedArray.getColor(R.styleable.RoundProgressBar_roundProgressColor, Color.WHITE);
        roundWidth = typedArray.getDimension(R.styleable.RoundProgressBar_roundWidth, 4);
        textIsDisplayable = typedArray.getBoolean(R.styleable.RoundProgressBar_textIsDisplayable, true);
        style = typedArray.getInt(R.styleable.RoundProgressBar_style, 0);
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
        paint = new Paint();
        arcPaint = new Paint();
        arcPaint.setStyle(Paint.Style.FILL);
        arcPaint.setAntiAlias(false);

        /*
        * 外层圆
        * */
        int center = getWidth() / 4 ;  //圆心的x坐标
        int radius = (int) (center - roundWidth / 2 -24);//半径
        paint.setAntiAlias(true);                   //消除锯齿
        paint.setStrokeJoin(Paint.Join.ROUND);      //平滑效果

        //定义的圆弧的形状和大小的界限
        RectF oval = new RectF(radius + 36, 50, center
                + radius * 2 +30, center + radius + 46);
        degree += 10;
        paint.setColor(roundColor);
        paint.setStrokeWidth(1);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        canvas.drawArc(oval, 270, 360 * progress / max , true, paint);



    }


    public synchronized int getMax() {
        return max;
    }

    /**
     * 设置进度的最大值
     *
     * @param max
     */
    public synchronized void setMax(int max) {
        if (max < 0) {
            max = 0;
        }
        this.max = max;
    }

    /**
     * 获取进度.需要同步
     *
     * @return
     */
    public synchronized int getProgress() {
        return progress;
    }

    /**
     * 设置进度，此为线程安全控件，由于考虑多线的问题，需要同步
     * 刷新界面调用postInvalidate()能在非UI线程刷新
     *
     * @param progress
     */
    public synchronized void setProgress(int progress) {
        if (progress < 0) {
//            throw new IllegalArgumentException("progress not less than 0");
            progress = 0;
        }
        if (progress > max) {
            progress = max;
        }
        if (progress <= max) {
            this.progress = progress;
            postInvalidate();
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
