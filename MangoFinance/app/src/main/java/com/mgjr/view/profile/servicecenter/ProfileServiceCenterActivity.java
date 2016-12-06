package com.mgjr.view.profile.servicecenter;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mgjr.R;
import com.mgjr.Utils.MyActivityManager;
import com.mgjr.Utils.SPUtils;
import com.mgjr.share.ActionbarActivity;
import com.mgjr.view.common.CommonWebViewActivity;

public class ProfileServiceCenterActivity extends ActionbarActivity implements View.OnClickListener {
    private static final int REQUEST_CODE_ASK_CALL_PHONE = 0;
    private RelativeLayout layout_ol_customer, layout_wechat_customer,layout_feedback;

    private LinearLayout layout_platformsecurity,
            layout_aboutus, layout_commonquestion;

    private ImageView imgbtn_call;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBaseContent(R.layout.activity_profile_service_center, this);
        actionbar.setCenterTextView("服务中心");
        initViews();
    }

    private void initViews() {
        layout_feedback = (RelativeLayout) findViewById(R.id.layout_feedback);
        layout_ol_customer = (RelativeLayout) findViewById(R.id.layout_ol_customer);
        layout_wechat_customer = (RelativeLayout) findViewById(R.id.layout_wechat_customer);
        layout_platformsecurity = (LinearLayout) findViewById(R.id.layout_platformsecurity);
        layout_aboutus = (LinearLayout) findViewById(R.id.layout_aboutus);
        layout_commonquestion = (LinearLayout) findViewById(R.id.layout_commonquestion);

        imgbtn_call = (ImageView) findViewById(R.id.imgbtn_call);

        layout_ol_customer.setOnClickListener(this);
        layout_commonquestion.setOnClickListener(this);
        layout_aboutus.setOnClickListener(this);
        layout_platformsecurity.setOnClickListener(this);
        layout_wechat_customer.setOnClickListener(this);
        imgbtn_call.setOnClickListener(this);
        layout_feedback.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        if (v == layout_commonquestion) {
            MyActivityManager.getInstance().startNextActivity(CommonQuestionActivity.class);
        } else if (v == layout_aboutus) {
            MyActivityManager.getInstance().startNextActivity(CommonWebViewActivity.class, "aboutUs");
        } else if (v == layout_platformsecurity) {
            MyActivityManager.getInstance().startNextActivity(CommonWebViewActivity.class, "platformSecurity");
        } else if (v == layout_wechat_customer) {
            MyActivityManager.getInstance().startNextActivity(CommonWebViewActivity.class, "wxCustomerProblem");
        } else if (v == layout_ol_customer) {
            //在线客服
            String phone = (String) SPUtils.get(this, "LOGIN", "mobilePhone", "");
            String to_url = "http://vipwebchat6303.tq.cn/pageinfo.jsp?version=vip&admiuin=8868809&ltype=1&iscallback=1&page_templete_id=41824&is_message_sms=0&is_send_mail=0&action=acd&acd=1&type_code=1&clientid=" + phone;
            MyActivityManager.getInstance().startNextActivity(CommonWebViewActivity.class, to_url);
        } else if (v == imgbtn_call) {
//            final CustomCommonDialog dialog = new CustomCommonDialog(this, "是否拨打客服热线?", "400-8976-5555", "确定", "取消", false);
//            dialog.show();
//            dialog.setClicklistener(new CustomCommonDialog.ClickListenerInterface() {
//                @Override
//                public void doConfirm() {
//                    //拨打电话
//                    doCall();
//                    dialog.dismiss();
//                }
//
//                @Override
//                public void doCancel() {
//                    dialog.dismiss();
//                }
//            });
            View rootView = LayoutInflater.from(ProfileServiceCenterActivity.this).inflate(R.layout.activity_profile_service_center, null);
            showCallPopw(ProfileServiceCenterActivity.this, rootView);

        }else if(v == layout_feedback){

            MyActivityManager.getInstance().startNextActivity(ProfileFeedBackActivity.class);

        }
    }

    private void doCall() {
        Intent mIntent = new Intent(Intent.ACTION_CALL,
                Uri.parse("tel:400-8976-555"));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        startActivity(mIntent);
    }


    /**
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha
     */
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getWindow().setAttributes(lp);
    }

    /**
     * 拨打电话弹窗
     *
     * @param activity
     * @param rootView
     * @return
     */
    public PopupWindow showCallPopw(final Activity activity, View rootView) {
        View contentView = LayoutInflater.from(activity).inflate(R.layout.layout_call_phonenum, null);
        TextView dialog_canclebtn = (TextView) contentView.findViewById(R.id.dialog_canclebtn);
        TextView dialog_confirmbtn = (TextView) contentView.findViewById(R.id.dialog_confirmbtn);
        final PopupWindow callPopw = new PopupWindow(contentView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        //设置动画所对应的style
        callPopw.setAnimationStyle(R.style.calcAnim);
        //点击空白处时，隐藏掉pop窗口
        callPopw.setBackgroundDrawable(new ColorDrawable());
        callPopw.setFocusable(true);
        callPopw.setOutsideTouchable(true);
        backgroundAlpha(0.5f);
        callPopw.showAtLocation(rootView, Gravity.CENTER_HORIZONTAL, 0, 0);
        callPopw.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1f);
            }
        });

        dialog_canclebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (callPopw != null) {
                    callPopw.dismiss();
                }
            }
        });
        dialog_confirmbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= 23) {
                    int checkCallPhonePermission = ContextCompat.checkSelfPermission(ProfileServiceCenterActivity.this, Manifest.permission.CALL_PHONE);
                    if (checkCallPhonePermission != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(ProfileServiceCenterActivity.this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CODE_ASK_CALL_PHONE);
                        return;
                    } else {
                        //上面已经写好的拨号方法
                        doCall();
                    }
                } else {
                    //上面已经写好的拨号方法
                    doCall();
                }
                callPopw.dismiss();
            }
        });
        return callPopw;
    }

}
