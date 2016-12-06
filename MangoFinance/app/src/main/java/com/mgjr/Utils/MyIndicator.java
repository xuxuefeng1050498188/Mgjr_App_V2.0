package com.mgjr.Utils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**
 * Created by Administrator on 2016/7/8.
 */
public class MyIndicator extends LinearLayout{

    private Paint paint;

    public MyIndicator(Context context) {
        super(context);
    }

    public MyIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        this.setBackgroundColor(Color.TRANSPARENT);
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setAntiAlias(true);
        paint.setColor(Color.GRAY);
        Path path = new Path();
        RectF rect1 = new RectF(20,80,60,120);
        RectF rect2 = new RectF(100,80,140,120);

        path.moveTo(40,80);
        path.lineTo(120,80);
        path.moveTo(120,100);
        canvas.drawArc(rect1,90,180,false,paint);
        path.moveTo(120,120);
        path.lineTo(40,120);
        path.moveTo(40,100);
        canvas.drawArc(rect2,-90,180,false,paint);
        path.moveTo(40,80);
        path.close();


    }


}
