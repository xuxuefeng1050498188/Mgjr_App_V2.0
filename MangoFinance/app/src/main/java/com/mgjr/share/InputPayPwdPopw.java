package com.mgjr.share;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mgjr.R;
import com.mgjr.Utils.CommonToastUtils;
import com.mgjr.Utils.KeyBoardUtils;
import com.mgjr.Utils.MyActivityManager;
import com.mgjr.Utils.PopwUtils;
import com.mgjr.view.invester.InvestConfirmActivity;
import com.mgjr.view.profile.accountsetting.ProfileFindTradePwdActivity;

/**
 * Created by xuxuefeng on 2016/11/9.
 */

public class InputPayPwdPopw {


    private EditText etInputTradePwd;
    private TextView dialogCanclebtn;
    private TextView dialogConfirmbtn;
    private TextView tv_forgetpaypwd;
    private LinearLayout layout_forgetpaypwd;
    private LinearLayout ll_cancle_confirm_btn;
    private RelativeLayout rl_checking_anim;
    private ImageView iv_checking;
    private TextView tv_paypwd_checking;
    private TextView tv_paypwd_state;
    private PopupWindow inputTradePwdPopw;
    private ObjectAnimator rotateAnim;
    private TextView dialog_title;

    public TextView getDialog_title() {
        return dialog_title;
    }

    public void setDialog_title(TextView dialog_title) {
        this.dialog_title = dialog_title;
    }

    public void setDialogCanclebtn(TextView dialogCanclebtn) {
        this.dialogCanclebtn = dialogCanclebtn;
    }

    public void setDialogConfirmbtn(TextView dialogConfirmbtn) {
        this.dialogConfirmbtn = dialogConfirmbtn;
    }

    public void setTv_forgetpaypwd(TextView tv_forgetpaypwd) {
        this.tv_forgetpaypwd = tv_forgetpaypwd;
    }

    public void setLayout_forgetpaypwd(LinearLayout layout_forgetpaypwd) {
        this.layout_forgetpaypwd = layout_forgetpaypwd;
    }

    public void setLl_cancle_confirm_btn(LinearLayout ll_cancle_confirm_btn) {
        this.ll_cancle_confirm_btn = ll_cancle_confirm_btn;
    }

    public void setRl_checking_anim(RelativeLayout rl_checking_anim) {
        this.rl_checking_anim = rl_checking_anim;
    }

    public void setIv_checking(ImageView iv_checking) {
        this.iv_checking = iv_checking;
    }

    public void setTv_paypwd_checking(TextView tv_paypwd_checking) {
        this.tv_paypwd_checking = tv_paypwd_checking;
    }

    public void setTv_paypwd_state(TextView tv_paypwd_state) {
        this.tv_paypwd_state = tv_paypwd_state;
    }


    public EditText getEtInputTradePwd() {
        return etInputTradePwd;
    }

    public TextView getDialogCanclebtn() {
        return dialogCanclebtn;
    }

    public TextView getDialogConfirmbtn() {
        return dialogConfirmbtn;
    }

    public TextView getTv_forgetpaypwd() {
        return tv_forgetpaypwd;
    }

    public LinearLayout getLayout_forgetpaypwd() {
        return layout_forgetpaypwd;
    }

    public LinearLayout getLl_cancle_confirm_btn() {
        return ll_cancle_confirm_btn;
    }

    public RelativeLayout getRl_checking_anim() {
        return rl_checking_anim;
    }

    public ImageView getIv_checking() {
        return iv_checking;
    }

    public TextView getTv_paypwd_checking() {
        return tv_paypwd_checking;
    }

    public TextView getTv_paypwd_state() {
        return tv_paypwd_state;
    }

    public PopupWindow getInputTradePwdPopw() {
        return inputTradePwdPopw;
    }

    private ClickBtnListener clickBtnListener;


    public void setClickBtnListener(ClickBtnListener clickBtnListener) {
        this.clickBtnListener = clickBtnListener;
    }


    public interface ClickBtnListener {
        void clickConfirmBtn();

        void clickForgetPwdBtn();
    }

    /**
     * 输入交易密码的弹窗
     */
    public PopupWindow showInputTradePwdPopw(final Activity activity, View rootView) {
        View contentView = initContentView(activity);
        initPopw(activity, rootView, contentView);
        inputTradePwdPopw.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                PopwUtils.backgroundAlpha(1f, activity);
            }
        });
        dialogCanclebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (inputTradePwdPopw != null) {
                    KeyBoardUtils.closeKeybord(etInputTradePwd, activity);
                    inputTradePwdPopw.dismiss();
                }
            }
        });
        tv_forgetpaypwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clickBtnListener != null) {
                    clickBtnListener.clickForgetPwdBtn();
                }
            }
        });
        dialogConfirmbtn.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    //提交数据,请求数据
                                                    if (clickBtnListener != null) {
                                                        clickBtnListener.clickConfirmBtn();
                                                    }
                                                    KeyBoardUtils.closeKeybord(etInputTradePwd, activity);
                                                }
                                            }

        );
        return inputTradePwdPopw;
    }

    private void initPopw(final Activity activity, View rootView, View contentView) {
        inputTradePwdPopw = new PopupWindow(contentView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        //设置动画所对应的style
        inputTradePwdPopw.setAnimationStyle(R.style.calcAnim);
        //点击空白处时，隐藏掉pop窗口
        inputTradePwdPopw.setBackgroundDrawable(new ColorDrawable());
        inputTradePwdPopw.setFocusable(true);
        inputTradePwdPopw.setOutsideTouchable(true);

        PopwUtils.backgroundAlpha(0.5f, activity);
        //自动弹出键盘以及上移弹窗,一定要注意顺序
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                KeyBoardUtils.openKeybord(etInputTradePwd, activity);
            }
        }, 800);
        inputTradePwdPopw.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        inputTradePwdPopw.showAtLocation(rootView, Gravity.CENTER, 0, 0);
    }

    @NonNull
    private View initContentView(Activity activity) {
        View contentView = LayoutInflater.from(activity).inflate(R.layout.input_trade_pwd, null);
        etInputTradePwd = (EditText) contentView.findViewById(R.id.et_input_trade_pwd);
        dialogCanclebtn = (TextView) contentView.findViewById(R.id.dialog_canclebtn);
        dialogConfirmbtn = (TextView) contentView.findViewById(R.id.dialog_confirmbtn);
        tv_forgetpaypwd = (TextView) contentView.findViewById(R.id.tv_forgetpaypwd);
        layout_forgetpaypwd = (LinearLayout) contentView.findViewById(R.id.layout_forgetpaypwd);
        ll_cancle_confirm_btn = (LinearLayout) contentView.findViewById(R.id.ll_cancle_confirm_btn);
        rl_checking_anim = (RelativeLayout) contentView.findViewById(R.id.rl_checking_anim);
        iv_checking = (ImageView) contentView.findViewById(R.id.iv_checking);
        tv_paypwd_checking = (TextView) contentView.findViewById(R.id.tv_paypwd_checking);
        tv_paypwd_state = (TextView) contentView.findViewById(R.id.tv_paypwd_state);
        dialog_title = (TextView) contentView.findViewById(R.id.dialog_title);
        return contentView;
    }


    public void startCheckingAnim() {
        layout_forgetpaypwd.setVisibility(View.GONE);
        ll_cancle_confirm_btn.setVisibility(View.GONE);
        rl_checking_anim.setVisibility(View.VISIBLE);
        rotateAnim = ObjectAnimator.ofFloat(iv_checking, "rotation", 0f, 360f);
        rotateAnim.setDuration(1500);
        rotateAnim.setRepeatCount(ValueAnimator.RESTART);
        rotateAnim.setRepeatCount(10000);
        rotateAnim.setInterpolator(new LinearInterpolator());
        rotateAnim.start();
    }

    public void paypwdCheckingError(String errorMsg) {
        if (inputTradePwdPopw == null) {
            return;
        }
        layout_forgetpaypwd.setVisibility(View.VISIBLE);
        ll_cancle_confirm_btn.setVisibility(View.VISIBLE);
        rl_checking_anim.setVisibility(View.GONE);
        if (rotateAnim != null) {
            rotateAnim.cancel();
        }
        tv_paypwd_state.setVisibility(View.VISIBLE);
        tv_paypwd_state.setText(errorMsg);
    }


    public void paypwdCheckingSuccess() {
        if (rotateAnim != null) {
            rotateAnim.cancel();
        }
        iv_checking.clearAnimation();
        iv_checking.setRotation(0);
        iv_checking.setImageResource(R.drawable.paypwd_check_success_bg);
        tv_paypwd_checking.setText("");
    }
}
