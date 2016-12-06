package com.mgjr.share;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import com.mgjr.R;

/**
 * Created by wim on 16/7/19.
 */
public class LineView extends View {
    public LineView(Context context) {
        super(context);
        init(context);
    }

    public LineView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public LineView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context){
        this.setBackgroundColor(ContextCompat.getColor(context,R.color.line_color));

    }
}
