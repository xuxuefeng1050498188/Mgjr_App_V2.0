package com.mgjr.share;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mgjr.Utils.ScreenUtils;

/**
 * Created by wim on 16/5/23.
 */
public class TabCell extends LinearLayout {

    private TextView textView;
    private ImageView imageView;

    private int selectedDrawable;
    private int normalDrawable;

    private int normalColor = Color.parseColor("#8B8B8B");
    private int selectedColor = Color.parseColor("#FFA800");

    private LinearLayout.LayoutParams imageLayoutParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, 0);
    private LinearLayout.LayoutParams textLayoutParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, 0);

    public TabCell(Context context) {
        super(context);

        imageView = new ImageView(context);
        imageView.setScaleType(ImageView.ScaleType.CENTER);

        textView = new TextView(context);
        textView.setGravity(Gravity.CENTER);
        textView.setTextColor(normalColor);
        textView.setTextSize(12);

        imageLayoutParams.weight = 1;
        imageLayoutParams.topMargin = ScreenUtils.dipToPx(context, 5);
        textLayoutParams.weight = 1;

        this.addView(imageView, imageLayoutParams);
        this.addView(textView, textLayoutParams);

        this.setClickable(true);
        this.setGravity(Gravity.CENTER);
        this.setOrientation(LinearLayout.VERTICAL);
    }

    public void setContent(int drawable, int selectedDrawable, String title) {
        imageView.setImageResource(drawable);
        textView.setText(title);

        this.selectedDrawable = selectedDrawable;
        this.normalDrawable = drawable;
    }

    public void setTabSelected(boolean isSelected) {
        if (isSelected) {
            imageView.setImageResource(this.selectedDrawable);
            textView.setTextColor(selectedColor);
        } else {
            imageView.setImageResource(this.normalDrawable);
            textView.setTextColor(normalColor);
        }
    }
}
