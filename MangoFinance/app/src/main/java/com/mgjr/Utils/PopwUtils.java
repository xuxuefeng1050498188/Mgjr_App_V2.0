package com.mgjr.Utils;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.mgjr.R;
import com.mgjr.view.profile.accountsetting.ProfileRealNameAuthenticationActivity;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by xuxuefeng on 2016/9/9.
 */
public class PopwUtils {


    /**
     * 正在加载中的弹窗
     *
     * @param context
     * @param rootView
     * @return
     */
    public static PopupWindow showLoadingPopw(final Activity context, View rootView) {
        View contentView = LayoutInflater.from(context).inflate(R.layout.layout_loading_state, null);
        ImageView loading_state_bg = (ImageView) contentView.findViewById(R.id.loading_state_bg);
        ObjectAnimator rotateAnim = ObjectAnimator.ofFloat(loading_state_bg, "rotation", 0f, 360f);
        rotateAnim.setRepeatCount(Animation.INFINITE);
        rotateAnim.setRepeatMode(ValueAnimator.RESTART);
        rotateAnim.setInterpolator(new LinearInterpolator());
        rotateAnim.setDuration(1000);
        rotateAnim.start();
        PopupWindow loadingPopW = new PopupWindow(contentView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        //设置动画所对应的style
        loadingPopW.setAnimationStyle(R.style.loadingAnim);
//        //点击空白处时，隐藏掉pop窗口
//        loadingPopW.setBackgroundDrawable(new ColorDrawable());
//        loadingPopW.setFocusable(true);
//        loadingPopW.setOutsideTouchable(true);
//        backgroundAlpha(0.5f, (Activity) context);
        loadingPopW.showAtLocation(rootView, Gravity.CENTER_HORIZONTAL, 0, 0);
//        loadingPopW.setOnDismissListener(new PopupWindow.OnDismissListener() {
//            @Override
//            public void onDismiss() {
////                backgroundAlpha(1f, context);
//            }
//        });
        return loadingPopW;
    }

    public static PopupWindow showLoadingPopw(final Activity context) {
        View contentView = LayoutInflater.from(context).inflate(R.layout.layout_loading_state, null);
        ImageView loading_state_bg = (ImageView) contentView.findViewById(R.id.loading_state_bg);
        ObjectAnimator rotateAnim = ObjectAnimator.ofFloat(loading_state_bg, "rotation", 0f, 360f);
        rotateAnim.setRepeatCount(Animation.INFINITE);
        rotateAnim.setRepeatMode(ValueAnimator.RESTART);
        rotateAnim.setInterpolator(new LinearInterpolator());
        rotateAnim.setDuration(1000);
        rotateAnim.start();
        PopupWindow loadingPopW = new PopupWindow(contentView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        //设置动画所对应的style
        loadingPopW.setAnimationStyle(R.style.loadingAnim);
//        //点击空白处时，隐藏掉pop窗口
//        loadingPopW.setBackgroundDrawable(new ColorDrawable());
//        loadingPopW.setFocusable(true);
//        loadingPopW.setOutsideTouchable(true);
//        backgroundAlpha(0.5f, (Activity) context);

//        loadingPopW.setOnDismissListener(new PopupWindow.OnDismissListener() {
//            @Override
//            public void onDismiss() {
////                backgroundAlpha(1f, context);
//            }
//        });
        return loadingPopW;
    }


    /**
     * 取消弹窗
     *
     * @param loadingPopw
     */
    public static void dismissLoadingPopw(PopupWindow loadingPopw) {
        if (loadingPopw != null) {
            loadingPopw.dismiss();
            loadingPopw = null;
        }
    }

    /**
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha
     */
    public static void backgroundAlpha(float bgAlpha, Activity activity) {
        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        activity.getWindow().setAttributes(lp);
    }


    /**
     * 实名认证弹窗
     *
     * @param activity
     * @param rootView
     * @return
     */
    public static PopupWindow showRealNameAuthPopw(final Activity activity, View rootView) {
        View contentView = LayoutInflater.from(activity).inflate(R.layout.layout_realname_auth, null);
        ImageView iv_realname_auth_delete = (ImageView) contentView.findViewById(R.id.iv_realname_auth_delete);
        Button btn_realname_auth = (Button) contentView.findViewById(R.id.btn_realname_auth);

        final PopupWindow realNameAuthPopw = new PopupWindow(contentView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        //设置动画所对应的style
        realNameAuthPopw.setAnimationStyle(R.style.loadingAnim);
        //点击空白处时，隐藏掉pop窗口
        realNameAuthPopw.setBackgroundDrawable(new ColorDrawable());
        realNameAuthPopw.setFocusable(true);
        realNameAuthPopw.setOutsideTouchable(true);
        backgroundAlpha(0.5f, (Activity) activity);
        realNameAuthPopw.showAtLocation(rootView, Gravity.CENTER_HORIZONTAL, 0, 0);
        /*realNameAuthPopw.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
//                backgroundAlpha(1f, activity);
//                MyActivityManager.getInstance().finishActivity(activity);
//                realNameAuthPopw.dismiss();
            }
        });*/

        iv_realname_auth_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (realNameAuthPopw != null) {
                    MyActivityManager.getInstance().popCurrentActivity();
                    activity.overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
                    realNameAuthPopw.dismiss();
                }
            }
        });
        btn_realname_auth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyActivityManager.getInstance().popCurrentActivity();
                activity.overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
                realNameAuthPopw.dismiss();
                MyActivityManager.getInstance().startNextActivity(ProfileRealNameAuthenticationActivity.class);
            }
        });
        return realNameAuthPopw;
    }

    /**
     * 输入交易密码的弹窗
     */
    public static PopupWindow showInputTradePwdPopw(final Activity activity, View rootView) {
        final TradePwdViewHolder holder;
        View contentView = LayoutInflater.from(activity).inflate(R.layout.input_trade_pwd, null);
        holder = new TradePwdViewHolder(contentView);
        final PopupWindow inputTradePwdPopw = new PopupWindow(contentView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        //设置动画所对应的style
        inputTradePwdPopw.setAnimationStyle(R.style.calcAnim);
        //点击空白处时，隐藏掉pop窗口
        inputTradePwdPopw.setBackgroundDrawable(new ColorDrawable());
        inputTradePwdPopw.setFocusable(true);
        inputTradePwdPopw.setOutsideTouchable(true);

        backgroundAlpha(0.5f, (Activity) activity);
        //自动弹出键盘以及上移弹窗,一定要注意顺序
        KeyBoardUtils.openKeybord(holder.etInputTradePwd, activity);
        inputTradePwdPopw.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        inputTradePwdPopw.showAtLocation(rootView, Gravity.CENTER, 0, 0);
        inputTradePwdPopw.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1f, activity);
            }
        });
        holder.dialogCanclebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (inputTradePwdPopw != null) {
                    KeyBoardUtils.closeKeybord(holder.etInputTradePwd, activity);
                    inputTradePwdPopw.dismiss();
                }
            }
        });
        holder.dialogConfirmbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                KeyBoardUtils.closeKeybord(holder.etInputTradePwd, activity);
                inputTradePwdPopw.dismiss();
            }
        });
        return inputTradePwdPopw;
    }

    static class TradePwdViewHolder {
        @InjectView(R.id.dialog_title)
        TextView dialogTitle;
        @InjectView(R.id.et_input_trade_pwd)
        EditText etInputTradePwd;
        @InjectView(R.id.line)
        TextView line;
        @InjectView(R.id.dialog_canclebtn)
        TextView dialogCanclebtn;
        @InjectView(R.id.dialog_confirmbtn)
        TextView dialogConfirmbtn;

        TradePwdViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }


    /**
     * 拨打电话弹窗
     *
     * @param activity
     * @param rootView
     * @return
     */
    /*public static PopupWindow showCallPopw(final Activity activity, View rootView) {
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
        backgroundAlpha(0.5f, (Activity) activity);
        callPopw.showAtLocation(rootView, Gravity.CENTER_HORIZONTAL, 0, 0);
        callPopw.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1f, activity);
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
                //拨打电话
                String forPhoneNum = "4008976555";
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + forPhoneNum));
                if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    if (Build.VERSION.SDK_INT >= 23) {
                        int checkCallPhonePermission = ContextCompat.checkSelfPermission(activity, Manifest.permission.CALL_PHONE);
                        if (checkCallPhonePermission != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CODE_ASK_CALL_PHONE);
                            return;
                        } else {
                            //上面已经写好的拨号方法
                            activity.startActivity(intent);//内部类
                        }
                    } else {
                        //上面已经写好的拨号方法
                        activity.startActivity(intent);//内部类
                    }
                    return;
                }
                callPopw.dismiss();
            }
        });
        return callPopw;
    }*/
}
