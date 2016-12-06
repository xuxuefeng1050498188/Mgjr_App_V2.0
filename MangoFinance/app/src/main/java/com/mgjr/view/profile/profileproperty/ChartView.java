package com.mgjr.view.profile.profileproperty;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.View;

/**
 * Created by Administrator on 2016/7/19.
 */
public class ChartView extends View {

    private Paint paint, paint1;

    private int XPoint = 0;    //原点的X坐标
    private int YPoint;     //原点的Y坐标
    private int XScale;     //X的刻度长度
    private int YScale;     //Y的刻度长度
    private int XLength;        //X轴的长度
    private int YLength;        //Y轴的长度
    private String[] XLabel;    //X的刻度
    //    private String[] YLabel;    //Y的刻度
    public String[] Data;      //数据

    public ChartView(Context context) {
        super(context);
    }

    public void SetInfo(int xLength, int xScale, int yPoint, String[] XLabels, int Ylength, int yScale, String[] AllData) {
        XLength = xLength;
        XScale = xScale;
        YPoint = yPoint;
        XLabel = XLabels;
        YLength = Ylength;
        YScale = yScale;
        Data = AllData;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        paint = new Paint();//画X轴
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(2);
        paint.setAntiAlias(true);//去锯齿
        paint.setColor(Color.parseColor("#f15b1c"));//颜色
        paint.setTextSize(18);

        paint1 = new Paint();//画折线
        paint1.setStyle(Paint.Style.STROKE);
        paint1.setAntiAlias(true);
        paint1.setColor(Color.parseColor("#704b04"));

        Paint aniPaint = new Paint();
        aniPaint.setStyle(Paint.Style.STROKE);
        aniPaint.setAntiAlias(true);
        aniPaint.setColor(Color.BLACK);

        //画X轴
        canvas.drawLine(XPoint, YPoint, XPoint + XLength, YPoint, paint);
        for (int i = 0; i * XScale < XLength; i++) {
            try {
                //刻度
                canvas.drawLine(XPoint + i * XScale + XScale / 2, YPoint, XPoint + i * XScale + +XScale / 2, YPoint - 5, paint);
                //刻度文字
                canvas.drawText(XLabel[i], XPoint + i * XScale - 10 + XScale / 2, YPoint + 40, paint);
                //画点
                canvas.drawCircle(XPoint + i * XScale + XScale / 2,YLength - Float.parseFloat(Data[i]), 2, paint);
                //连线
                canvas.drawLine(XPoint+(i-1)*XScale + XScale / 2,YLength - Float.parseFloat(Data[i-1]), XPoint+i*XScale + XScale / 2,YLength - Float.parseFloat(Data[i]), paint);
                //文字
                canvas.drawText(Data[i],XPoint + i * XScale + XScale / 2 - 10,YLength -Float.parseFloat(Data[i]) - 20,paint1);

                Path path = new Path();
                path.moveTo(XPoint + i * XScale + XScale / 2,YLength - Float.parseFloat(Data[i]));
                canvas.drawPath(path,aniPaint);


            } catch (Exception e) {
            }
        }

    }
}
