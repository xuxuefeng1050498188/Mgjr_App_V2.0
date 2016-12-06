package com.mgjr.share;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mgjr.R;

import org.w3c.dom.Text;

/**
 * Created by wim on 16/9/19.
 */
public class SocialDialog extends Dialog implements View.OnClickListener{

    private Context mContext;

    private ClickListenerInterface clickListenerInterface;

    private RelativeLayout wechatLayout;
    private RelativeLayout friendLayout;
    private RelativeLayout qqLayout;
    private RelativeLayout sinaLayout;
    private Button cancelBtn;

    public SocialDialog(Context context) {
        super(context);
        mContext = context;
    }

    public SocialDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    protected SocialDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        setup();


    }

    private void setup() {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View dialogView = inflater.inflate(R.layout.social_layout, null);
        setContentView(dialogView);

        Window dialogWindow = getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        dialogWindow.setGravity(Gravity.CENTER | Gravity.BOTTOM);

        wechatLayout = (RelativeLayout)dialogView.findViewById(R.id.wechat_btn);
        friendLayout= (RelativeLayout)dialogView.findViewById(R.id.wefriend_btn);
        qqLayout = (RelativeLayout)dialogView.findViewById(R.id.qq_btn);
        sinaLayout = (RelativeLayout)dialogView.findViewById(R.id.weibo_btn);
        cancelBtn = (Button)dialogView.findViewById(R.id.cancel_btn);

        wechatLayout.setOnClickListener(this);
        friendLayout.setOnClickListener(this);
        qqLayout.setOnClickListener(this);
        sinaLayout.setOnClickListener(this);
        cancelBtn.setOnClickListener(this);
    }

    public void setClicklistener(ClickListenerInterface clickListenerInterface) {
        this.clickListenerInterface = clickListenerInterface;
    }

    @Override
    public void onClick(View v) {
        if (v == wechatLayout){
            clickListenerInterface.touchWeChatEvent();
        }
        else if (v == friendLayout){
            clickListenerInterface.touchWeFriendEvent();
        }
        else if (v == qqLayout){
            clickListenerInterface.touchQQEvent();
        }
        else if (v == sinaLayout){
            clickListenerInterface.touchWeiboEvent();
        }
        else if (v == cancelBtn){
            clickListenerInterface.touchCancelEvent();
        }
    }


    public interface ClickListenerInterface{
        public void touchWeChatEvent();
        public void touchWeFriendEvent();
        public void touchQQEvent();
        public void touchWeiboEvent();
        public void touchCancelEvent();
    }
}
