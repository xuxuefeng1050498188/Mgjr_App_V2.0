package com.mgjr.share;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Administrator on 2016/10/19.
 */

public class JmgProjectProgressView extends View {

    private int partCount;
    private int progress;
    private int max;

    private Paint paint;

    public JmgProjectProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
    }

    public void initProgress(int partCount,int progress){
        this.partCount = partCount;
        this.progress = progress;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);


    }

    public int getPartCount() {
        return partCount;
    }

    public void setPartCount(int partCount) {
        this.partCount = partCount;
    }

    public int getProgress() {
        return progress;
    }

    public synchronized void setProgress(int progress) {
        if (progress < 0) {
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

    public int getMax() {
        return max;
    }

    public synchronized void setMax(int max) {
        if (max < 0) {
            max = 0;
        }
        this.max = max;
    }
}
