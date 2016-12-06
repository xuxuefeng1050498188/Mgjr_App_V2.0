package com.mgjr.share;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mgjr.R;

import static com.mgjr.R.id.imgbtn_center_actionbar;

/**
 * Created by wim on 16/5/27.
 */
public class CustomActionbar extends RelativeLayout {

    private RelativeLayout actionBar;
    public ImageButton leftImageView;
    public TextView centerTextView;
    public ImageButton rightImageView;
    public ImageButton secondRightImageView;
    public ImageView centerImgBtn;
    public TextView rightTextView;
    public TextView leftTextView;
    private LinearLayout actionbar_centerlayout;
    public CustomActionbar(Context context) {
        this(context, null);
    }

    public CustomActionbar(Context context, AttributeSet attrs) {
        super(context, attrs);
        setup(context);
    }

    public void setup(Context context) {
        actionBar = (RelativeLayout) LayoutInflater.from(context).inflate(R.layout.layout_actionbar, null);
        leftImageView = (ImageButton) actionBar.findViewById(R.id.ib_left_image_actionbar);
        leftTextView = (TextView) actionBar.findViewById(R.id.tv_left_text_actionbar);
        rightImageView = (ImageButton) actionBar.findViewById(R.id.ib_right_image_actionbar);
        secondRightImageView = (ImageButton) actionBar.findViewById(R.id.ib_second_right_image_actionbar);
        rightTextView = (TextView) actionBar.findViewById(R.id.tv_right_text_actionbar);
        centerTextView = (TextView) actionBar.findViewById(R.id.tv_center_text_actionbar);
        centerImgBtn = (ImageView) actionBar.findViewById(imgbtn_center_actionbar);
        actionbar_centerlayout = (LinearLayout) actionBar.findViewById(R.id.actionbar_centerlayout);
        this.addView(actionBar);
    }

    public void setCenterLayoutOnClick(OnClickListener listener){
        actionbar_centerlayout.setOnClickListener(listener);
        centerImgBtn.setVisibility(VISIBLE);
    }

    public void setLeftImageView(int resID, OnClickListener listener) {
        leftImageView.setImageResource(resID);
        leftImageView.setOnClickListener(listener);
    }
    public void setleftTextView(String text, OnClickListener listener) {
        leftImageView.setVisibility(GONE);
        leftTextView.setText(text);
        leftTextView.setOnClickListener(listener);
    }

    public void setCenterTextView(String title) {
        centerTextView.setText(title);
    }

    public void setCenterTextView(String title,OnClickListener listener) {
        centerTextView.setText(title);
        centerImgBtn.setOnClickListener(listener);
    }

    public void setCenterTextColor(int color) {
        centerTextView.setTextColor(color);
    }

    public void setCenterImgBtn(int resID, OnClickListener listener) {
        centerImgBtn.setVisibility(VISIBLE);
        centerImgBtn.setImageResource(resID);
        centerImgBtn.setOnClickListener(listener);
    }

    public void setRightImageView(int resID, OnClickListener listener) {
//        rightTextView.setVisibility(GONE);
        rightImageView.setImageResource(resID);
        rightImageView.setOnClickListener(listener);
    }

    public void setBackgroundColor(int color) {
        actionBar.setBackgroundColor(color);
    }

    public void setRightTextView(String text, OnClickListener listener) {
//        rightImageView.setVisibility(GONE);
        rightTextView.setText(text);
        rightTextView.setOnClickListener(listener);
    }

    public void setSecondRightImageView(int resID, OnClickListener listener) {
        secondRightImageView.setImageResource(resID);
        secondRightImageView.setOnClickListener(listener);
    }


}
