package com.mgjr.Utils;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mgjr.R;
import com.mgjr.mangofinance.MainApplication;

public class CommonToastUtils {

    private static Toast commontToast;
    private static Toast successToast;

    public static void showToast(Context context, String content) {
        View layout = null;
        TextView text = null;

        if (context == null) {
            return;
        }
        if (layout == null) {
            layout = LayoutInflater.from(context).inflate(R.layout.layout_toast_xml, null);
            text = (TextView) layout.findViewById(R.id.text);
            ImageView mImageView = (ImageView) layout.findViewById(R.id.iv);
            mImageView.setBackgroundResource(R.drawable.toast_exception_notify);
            text.setText(content);
        } else {
            text.setText(content);
        }
        if (commontToast == null) {
            commontToast = Toast.makeText(context, content, Toast.LENGTH_SHORT);
        }
        commontToast.setView(layout);
        commontToast.show();
    }

    public static void showSuccessToast(String content) {
        View layout = null;
        TextView text = null;

        if (layout == null) {
            layout = LayoutInflater.from(MainApplication.getContext()).inflate(R.layout.layout_success_toast_xml, null);
            text = (TextView) layout.findViewById(R.id.text);
            text.setText(content);
        } else {
            text.setText(content);
        }
        if (successToast == null) {
            successToast = Toast.makeText(MainApplication.getContext(), content, Toast.LENGTH_SHORT);
            successToast.setGravity(Gravity.CENTER, 0, 0);
        }
        successToast.setView(layout);
        successToast.show();
    }
}