package com.mgjr.share;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import com.mgjr.R;
import com.mgjr.Utils.MyActivityManager;
import com.mgjr.view.profile.accountsetting.ProfileFindTradePwdActivity;
import com.mgjr.view.profile.accountsetting.ProfileModifyTradePwdActivity;


/**
 * Created by Administrator on 2016/7/21.
 */
public class PayPwdDialog extends Dialog {

    private Context context;
    private TextView dialog_canclebtn, dialog_confirmbtn;

    private EditText et_input_trade_pwd;
    private TextView tv_forgetpaypwd,tv_paypwd_state;

    private String  confirmText, cancleText;

    private ClickListenerInterface clickListenerInterface;


    public PayPwdDialog(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        init();
    }

    private void init() {
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.input_trade_pwd, null);
        setContentView(dialogView);
        et_input_trade_pwd = (EditText) dialogView.findViewById(R.id.et_input_trade_pwd);
        dialog_canclebtn = (TextView) dialogView.findViewById(R.id.dialog_canclebtn);
        dialog_confirmbtn = (TextView) dialogView.findViewById(R.id.dialog_confirmbtn);
        tv_forgetpaypwd = (TextView) dialogView.findViewById(R.id.tv_forgetpaypwd);
        tv_paypwd_state = (TextView) dialogView.findViewById(R.id.tv_paypwd_state);

        //绑定从构造方法传递过来的数据
        if (confirmText != null) {
            dialog_confirmbtn.setText(confirmText);
        }
        if (cancleText != null) {
            dialog_canclebtn.setText(cancleText);
        }

        tv_forgetpaypwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyActivityManager.getInstance().startNextActivity(ProfileFindTradePwdActivity.class);
            }
        });


        //为确定,取消设置点击事件
        dialog_confirmbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListenerInterface.doConfirm(et_input_trade_pwd.getText());
            }
        });
        dialog_canclebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListenerInterface.doCancel();
            }
        });

    }

    public void setClicklistener(ClickListenerInterface clickListenerInterface) {
        this.clickListenerInterface = clickListenerInterface;
    }


    public interface ClickListenerInterface {

        public void doConfirm(Editable text);

        public void doCancel();
    }
}
