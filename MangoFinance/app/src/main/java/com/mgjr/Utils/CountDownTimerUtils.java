package com.mgjr.Utils;

import android.graphics.Color;
import android.os.CountDownTimer;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.widget.Button;

import com.mgjr.R;

/**
 * Created by LvB on 2016/8/23.
 */
public class CountDownTimerUtils extends CountDownTimer {

    //获取验证码按钮
    private Button btn_getCode;

    public CountDownTimerUtils(Button btn_getCode, long millisInFuture, long countDownInterval) {
        super(millisInFuture, countDownInterval);
        this.btn_getCode = btn_getCode;
    }

    @Override
    public void onTick(long millisUntilFinished) {
        btn_getCode.setClickable(false); //设置不可点击
        btn_getCode.setText(millisUntilFinished / 1000 + "s");  //设置倒计时时间
        btn_getCode.setTextSize(12);
        btn_getCode.setTextColor(Color.GRAY);
        btn_getCode.setBackgroundResource(R.drawable.shape_registerlayout_getsmscode); //设置按钮为灰色，这时是不能点击的

        /**
         * 超链接 URLSpan
         * 文字背景颜色 BackgroundColorSpan
         * 文字颜色 ForegroundColorSpan
         * 字体大小 AbsoluteSizeSpan
         * 粗体、斜体 StyleSpan
         * 删除线 StrikethroughSpan
         * 下划线 UnderlineSpan
         * 图片 ImageSpan
         *
         */
        SpannableString spannableString = new SpannableString(btn_getCode.getText().toString());  //获取按钮上的文字
        ForegroundColorSpan span = new ForegroundColorSpan(Color.GRAY);
        /**
         * public void setSpan(Object what, int start, int end, int flags) {
         * 主要是start跟end，start是起始位置,无论中英文，都算一个。
         * 从0开始计算起。end是结束位置，所以处理的文字，包含开始位置，但不包含结束位置。
         */
        spannableString.setSpan(span, 0, btn_getCode.getText().toString().length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);//将倒计时的时间设置为红色
        btn_getCode.setText(spannableString);
    }

    @Override
    public void onFinish() {
        btn_getCode.setText("重新获取");
        btn_getCode.setTextColor(Color.parseColor("#feaa00"));
        btn_getCode.setTextSize(12);
        btn_getCode.setClickable(true);//重新获得点击
        btn_getCode.setBackgroundResource(R.drawable.shape_capitaildetails_title);  //还原背景色
    }

    public void refresh() {
        this.cancel();
        btn_getCode.setText("获取验证码");
        btn_getCode.setTextColor(Color.parseColor("#feaa00"));
        btn_getCode.setClickable(true);//重新获得点击
        btn_getCode.setBackgroundResource(R.drawable.shape_capitaildetails_title);  //还原背景色
    }
}
