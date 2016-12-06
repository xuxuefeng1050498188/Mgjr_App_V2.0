package com.mgjr.share;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;

import com.mgjr.R;
import com.mgjr.Utils.MyActivityManager;
import com.mgjr.view.profile.mangobox.MangoBoxActivity;

/**
 * Created by Administrator on 2016/9/14.
 */
public class EventRewardWindow extends PopupWindow {

    private LayoutInflater inflater;
    private View contentView;
    public EventRewardWindow(Context context) {
        super(context);

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        contentView = inflater.inflate(R.layout.layout_event_reward, null);
        this.setContentView(contentView);
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        this.setTouchable(true);
        this.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });
        // 刷新状态
        this.update();
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0000000000);
        // 点back键和其他地方使其消失,设置了这个才能触发OnDismisslistener ，设置其他控件变化等操作
        this.setBackgroundDrawable(dw);

        //图片内容
        ImageView content = (ImageView) contentView.findViewById(R.id.img_eventreward_content);
        //关闭按钮
        ImageView colseBtn = (ImageView) contentView.findViewById(R.id.colseimgbtn_eventreward);
        //立即查看 按钮
        Button btn = (Button) contentView.findViewById(R.id.btn_eventreward_showdetails);



        colseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventRewardWindow.this.dismiss();
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyActivityManager.getInstance().startNextActivity(MangoBoxActivity.class);
                EventRewardWindow.this.dismiss();
            }
        });

    }
    /**
     * 显示popupWindow
     *
     * @param parent
     */
    public void showPopupWindow(View parent) {
        if (!this.isShowing()) {
            // 以下拉方式显示popupwindow
//            this.showAtLocation(parent, Gravity.CENTER,0,0);
            this.showAsDropDown(parent);
        } else {
            this.dismiss();
        }
    }
}
