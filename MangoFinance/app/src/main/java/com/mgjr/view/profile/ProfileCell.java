package com.mgjr.view.profile;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mgjr.R;
import com.mgjr.Utils.ScreenUtils;
import com.mgjr.share.LineView;

/**
 * Created by wim on 16/7/18.
 */
public class ProfileCell extends RelativeLayout {

    private int mLogoImage;
    private String mTitle;
    private String mDetail;
    private boolean isShowPoint;

    private ImageView logoImageView;
    private TextView titleTextView;
    private TextView detailTextView;
    private ImageView pointImgaeView;
    private ImageView arrowImgaeView;

    private int detailColor = Color.parseColor("#999999");

    public ProfileCell(Context context) {
        super(context);
    }

    public ProfileCell(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ProfileCell(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ProfileCell);
        int count = typedArray.getIndexCount();
        for (int i = 0; i < count; i++){
            int attr = typedArray.getIndex(i);
            switch (attr){
                case R.styleable.ProfileCell_cellLogo:
                    mLogoImage = typedArray.getResourceId(attr, 0);
                    break;
                case R.styleable.ProfileCell_cellTitle:
                    mTitle = typedArray.getString(attr);
                    break;
                case R.styleable.ProfileCell_cellDetail:
                    mDetail = typedArray.getString(attr);
                    break;
                case R.styleable.ProfileCell_cellRedPoint:
                    isShowPoint = typedArray.getBoolean(attr,false);
                    break;
                case R.styleable.ProfileCell_cellDetailColor:
                    detailColor = typedArray.getColor(attr,detailColor);
                    break;
            }
        }
        typedArray.recycle();

        logoImageView.setImageResource(mLogoImage);
        titleTextView.setText(mTitle);
        detailTextView.setText(mDetail);
        if (isShowPoint){
            pointImgaeView.setVisibility(VISIBLE);
        }
        else {
            pointImgaeView.setVisibility(GONE);
        }
        detailTextView.setTextColor(detailColor);
    }

    private void init(Context context){
        RelativeLayout.LayoutParams lineParams = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT,1);
        lineParams.addRule(RelativeLayout.ALIGN_PARENT_TOP,RelativeLayout.TRUE);
        LineView lineView = new LineView(context);
        this.addView(lineView,lineParams);



        RelativeLayout.LayoutParams logoParams = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.MATCH_PARENT);
        logoParams.leftMargin = ScreenUtils.dipToPx(context,12);
        logoImageView = new ImageView(context);
        logoImageView.setScaleType(ImageView.ScaleType.CENTER);
        logoImageView.setId(R.id.cell_logo_view);
        this.addView(logoImageView, logoParams);

        RelativeLayout.LayoutParams titleParams = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
        titleParams.addRule(RelativeLayout.RIGHT_OF,R.id.cell_logo_view);
        titleParams.addRule(RelativeLayout.CENTER_VERTICAL,RelativeLayout.TRUE);
        titleParams.leftMargin = ScreenUtils.dipToPx(context,12);
        titleTextView = new TextView(context);
        titleTextView.setTextColor(Color.parseColor("#666666"));
        titleTextView.setGravity(Gravity.CENTER);
        titleTextView.setId(R.id.cell_title_view);
        float size = getResources().getDimension(R.dimen.register_hinttext_size);
        titleTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP,15);
//        size = ScreenUtils.pxTodip(context,size);
//        titleTextView.setTextSize(34);
        this.addView(titleTextView, titleParams);

        RelativeLayout.LayoutParams pointParams = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
        pointParams.addRule(RelativeLayout.RIGHT_OF,R.id.cell_title_view);
        pointParams.addRule(RelativeLayout.ALIGN_TOP,R.id.cell_title_view);
        pointImgaeView = new ImageView(context);
        pointImgaeView.setScaleType(ImageView.ScaleType.CENTER);
        pointImgaeView.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.red_point_logo));
        pointImgaeView.setVisibility(View.GONE);
        this.addView(pointImgaeView, pointParams);

        RelativeLayout.LayoutParams arrowParams = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.MATCH_PARENT);
        arrowParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT,RelativeLayout.TRUE);
        arrowParams.rightMargin = ScreenUtils.dipToPx(context,14);
        arrowImgaeView = new ImageView(context);
        arrowImgaeView.setScaleType(ImageView.ScaleType.CENTER);
        arrowImgaeView.setId(R.id.cell_detail_view);
        arrowImgaeView.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.right_arrow_logo));
        this.addView(arrowImgaeView,arrowParams);

        RelativeLayout.LayoutParams detailParams = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
        detailParams.addRule(RelativeLayout.LEFT_OF,R.id.cell_detail_view);
        detailParams.rightMargin = ScreenUtils.dipToPx(context,10);
        detailTextView = new TextView(context);
        detailTextView.setGravity(Gravity.CENTER|Gravity.RIGHT);
        detailTextView.setTextColor(detailColor);
        float detailSize = getResources().getDimensionPixelSize(R.dimen.profilecell_tetails_size);
        detailSize = ScreenUtils.pxTodip(context,24);
        detailTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP,12);
//        detailTextView.setTextSize(detailSize);
        this.addView(detailTextView, detailParams);
    }
    public void setDetailTextView(String text){
        detailTextView.setText(text);

    }
}
