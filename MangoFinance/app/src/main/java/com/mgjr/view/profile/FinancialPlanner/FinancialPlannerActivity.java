package com.mgjr.view.profile.FinancialPlanner;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.mgjr.R;
import com.mgjr.Utils.CommonToastUtils;
import com.mgjr.Utils.MyActivityManager;
import com.mgjr.Utils.SPUtils;
import com.mgjr.mangofinance.MainApplication;
import com.mgjr.model.bean.FinancialPlannerBean;
import com.mgjr.presenter.impl.FinancialPlannerPresenterImpl;
import com.mgjr.presenter.listeners.OnPresenterListener;
import com.mgjr.share.ActionbarActivity;
import com.mgjr.share.CircleImageView;
import com.mgjr.share.SocialClient;
import com.mgjr.share.WeiboShareActivity;
import com.mgjr.view.common.CommonWebViewActivity;
import com.mgjr.view.listeners.ViewListener;
import com.sina.weibo.sdk.api.share.BaseResponse;
import com.sina.weibo.sdk.api.share.IWeiboHandler;
import com.sina.weibo.sdk.constant.WBConstants;
import com.squareup.picasso.Picasso;
import com.tencent.connect.common.Constants;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/10/8.
 */

public class FinancialPlannerActivity extends ActionbarActivity implements View.OnClickListener, IWeiboHandler.Response, ViewListener<FinancialPlannerBean>, IUiListener {
    //用户头像
    private CircleImageView user_head_img;
    //理财师等级
    private ImageView img_planner_level;
    //昵称
    private TextView tv_nickname;

    //已邀请好友数
    private TextView tv_friendsCount;
    //已邀投资好友数
    private TextView tv_friendsTenderCount;
    //累计收益
    private TextView tv_amount;
    //升级需要邀请好友数
    private TextView tv_needInvitate_friendCounts;
    //升级提示
    private LinearLayout layout_levelup_tips;
    //奖励明细
    private LinearLayout layout_tenderRewardList;
    //好友列表
    private LinearLayout layout_friendList;
    /*第三方邀请*/
    private LinearLayout layout_addressbook, layout_weixin, layout_moments, layout_qq, layout_weibo;
    private FrameLayout layout_planner_details;
    private LinearLayout layout_planner_level;
    private TextView tv_newlcs_tips;
    private FinancialPlannerPresenterImpl financialPlannerPresenter;
    private int friendsCount;
    private int friendsTenderCount;
    private double jlAmount;


    private String headImgUrl;
    private PopupWindow loadingPopupWindow;
    private FinancialPlannerBean financialPlannerBean;

    private String shareContent;
    private String shareTitel;
    private String shareUrl;

    private SocialClient socialClient;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBaseContent(R.layout.activity_layout_financialplanner, this);
        actionbar.setCenterTextView("芒果理财师");
        actionbar.setRightTextView("规则攻略", this);
        actionbar.setLeftImageView(R.drawable.actionbar_gray_backbtn, this);
        actionbar.rightTextView.setTextSize(14);
        actionbar.rightTextView.setTextColor(Color.parseColor("#999999"));
        socialClient = new SocialClient(this);

        initView();

        boolean wxAppSupportAPI = socialClient.wxAPI.isWXAppSupportAPI();
        boolean weiboAppSupportAPI = socialClient.getWeiboShareAPI().isWeiboAppSupportAPI();
//        boolean wxAppSupportAPI = SocialClient.getInstance(MainApplication.getContext()).wxAPI.isWXAppSupportAPI();
//        boolean weiboAppSupportAPI = SocialClient.getInstance(MainApplication.getContext()).getWeiboShareAPI().isWeiboAppSupportAPI();

    }

    private void initView() {
        financialPlannerPresenter = new FinancialPlannerPresenterImpl(this);

        user_head_img = (CircleImageView) findViewById(R.id.user_head_img);

        img_planner_level = (ImageView) findViewById(R.id.img_planner_level);

        tv_nickname = (TextView) findViewById(R.id.tv_nickname);
        tv_friendsCount = (TextView) findViewById(R.id.tv_friendsCount);
        tv_friendsTenderCount = (TextView) findViewById(R.id.tv_friendsTenderCount);
        tv_amount = (TextView) findViewById(R.id.tv_amount);
        tv_needInvitate_friendCounts = (TextView) findViewById(R.id.tv_needInvitate_friendCounts);
        tv_newlcs_tips = (TextView) findViewById(R.id.tv_newlcs_tips);

        layout_levelup_tips = (LinearLayout) findViewById(R.id.layout_levelup_tips);
        layout_tenderRewardList = (LinearLayout) findViewById(R.id.layout_tenderRewardList);
        layout_friendList = (LinearLayout) findViewById(R.id.layout_friendList);
        layout_addressbook = (LinearLayout) findViewById(R.id.layout_addressbook);
        layout_weixin = (LinearLayout) findViewById(R.id.layout_weixin);
        layout_moments = (LinearLayout) findViewById(R.id.layout_moments);
        layout_qq = (LinearLayout) findViewById(R.id.layout_qq);
        layout_weibo = (LinearLayout) findViewById(R.id.layout_weibo);
        layout_planner_level = (LinearLayout) findViewById(R.id.layout_planner_level);

        layout_planner_details = (FrameLayout) findViewById(R.id.layout_planner_details);

        layout_tenderRewardList.setOnClickListener(this);
        layout_friendList.setOnClickListener(this);
        layout_addressbook.setOnClickListener(this);
        layout_weixin.setOnClickListener(this);
        layout_moments.setOnClickListener(this);
        layout_qq.setOnClickListener(this);
        layout_weibo.setOnClickListener(this);


    }

    @Override
    protected void onResume() {
        super.onResume();
        //请求网络数据
        requestNetworkData();

    }


    private void setInitHeadImage() {
        headImgUrl = (String) SPUtils.get(this, "LOGIN", "headImgUrl", "");
        if (!TextUtils.isEmpty(headImgUrl)) {

            Picasso.with(this)
                    .load(headImgUrl)
                    .placeholder(R.drawable.mango_baby_4)
                    .error(R.drawable.mango_baby_4)
                    .into(user_head_img);
        } else {
            Picasso
                    .with(this)
                    .cancelRequest(user_head_img);
            user_head_img.setImageResource(R.drawable.mango_baby_4);
        }
    }

    private void requestNetworkData() {
        String mid = SPUtils.get(this, "LOGIN", "id", 0) + "";
        Map<String, String> params = new HashMap<>();
        params.put("mid", mid);
        financialPlannerPresenter.sendRequest(params, null);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        socialClient.getWeiboShareAPI().handleWeiboResponse(intent, FinancialPlannerActivity.this);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.REQUEST_QQ_SHARE) {
            Tencent.onActivityResultData(requestCode, resultCode, data, this);
        }
    }

    @Override
    public void onClick(View v) {
        if (v == actionbar.leftImageView) {
//            popActivity();
            MyActivityManager.getInstance().popCurrentActivity();
        } else if (v == layout_tenderRewardList) {
            MyActivityManager.getInstance().startNextActivity(TenderRewardListActivity.class, new DecimalFormat("###,###,##0.00").format(jlAmount));
        } else if (v == layout_friendList) {
            MyActivityManager.getInstance().startNextActivity(FriendListActivity.class, friendsCount + "", friendsTenderCount + "", new DecimalFormat("###,###,##0.00").format(jlAmount));
        } else if (v == layout_addressbook) {
            Uri smsToUri = Uri.parse("smsto:");
            Intent intent = new Intent(Intent.ACTION_SENDTO, smsToUri);
            intent.putExtra("sms_body", shareTitel + shareContent + shareUrl);
            startActivity(intent);
        } else if (v == layout_weixin) {
            if (socialClient.wxAPI.isWXAppInstalled()) {

                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.mango_logo);
                socialClient.setWebMediaMessage(shareTitel, shareContent, bitmap, shareUrl).sendRequestToWX(false);
            } else {
                CommonToastUtils.showToast(MainApplication.getContext(), "请安装微信");
            }
        } else if (v == layout_moments) {
            if (socialClient.wxAPI.isWXAppInstalled()) {
                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.mango_logo);
                socialClient.setWebMediaMessage(shareTitel, shareContent, bitmap, shareUrl).sendRequestToWX(true);

            } else {
                CommonToastUtils.showToast(MainApplication.getContext(), "请安装微信");
            }
        } else if (v == layout_qq) {

            Bundle bundle = socialClient.setWebQQMessage(shareTitel, shareContent, "", shareUrl);
            socialClient.getTencent().shareToQQ(this, bundle, this);

        } else if (v == layout_weibo) {
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.mango_logo);

            if (socialClient.mWeiboShareAPI.isWeiboAppInstalled()) {

                Bundle bundle = new Bundle();
                bundle.putString("title", shareTitel);
                bundle.putString("content", shareContent);
                bundle.putString("url", shareUrl);

                MyActivityManager.getInstance().startNextActivity(WeiboShareActivity.class, bundle);
            } else {
                CommonToastUtils.showToast(this, "请安装微博");
            }
        } else if (v == actionbar.rightTextView) {
            MyActivityManager.getInstance().startNextActivity(CommonWebViewActivity.class, "lcRaiders");
        }
    }

    @Override
    public void showLoading() {
        showLoadingDialog();
    }

    @Override
    public void hideLoading() {
//        PopwUtils.dismissLoadingPopw(loadingPopupWindow);
        dismissLoadingDialog();
    }

    @Override
    public void showError() {
//        PopwUtils.dismissLoadingPopw(loadingPopupWindow);
        dismissLoadingDialog();
        showSystemError();
    }

    @Override
    public void showError(OnPresenterListener listener, FinancialPlannerBean financialPlannerBean) {
        CommonToastUtils.showToast(this, financialPlannerBean.getMsg());
//        PopwUtils.dismissLoadingPopw(loadingPopupWindow);
        dismissLoadingDialog();
    }

    @Override
    public void responseData(OnPresenterListener listener, FinancialPlannerBean financialPlannerBean) {
        this.financialPlannerBean = financialPlannerBean;
        //设置头像
        setInitHeadImage();

        shareContent = financialPlannerBean.getShareContent();
        shareTitel = financialPlannerBean.getShareTitle();
        shareUrl = financialPlannerBean.getShareUrl();


        friendsCount = financialPlannerBean.getFriendsCount();
        friendsTenderCount = financialPlannerBean.getFriendsTenderCount();
        jlAmount = financialPlannerBean.getJlAmount();

        int lcs = financialPlannerBean.getMember().getLcs();
        String nickname = (String) SPUtils.get(this, "LOGIN", "nickname", "");
        String username = (String) SPUtils.get(this,"LOGIN","name","");
        setTextData(friendsCount, friendsTenderCount, jlAmount, nickname, lcs,username);
    }

    private void setTextData(int friendsCount, int friendsTenderCount, double jlAmount, String nickname, int lcs, String username) {
        if (!TextUtils.isEmpty(nickname)) {
            tv_nickname.setText(nickname);
        } else {
            tv_nickname.setText(username);
        }

        if (jlAmount != 0) {
            tv_amount.setText(new DecimalFormat("###,###,##0.00").format(jlAmount) + "");
        } else {
            tv_amount.setText("0.00");
        }
        if (friendsCount != 0) {
            tv_friendsCount.setText(friendsCount + "");
        } else {
            tv_friendsCount.setText("0");
        }
        if (friendsTenderCount != 0) {
            tv_friendsTenderCount.setText(friendsTenderCount + "");
        } else {
            tv_friendsTenderCount.setText("0");
        }
        int needCount = 5 - friendsTenderCount;

        if (lcs == 0) {
            tv_newlcs_tips.setVisibility(View.VISIBLE);
            layout_levelup_tips.setVisibility(View.INVISIBLE);
            img_planner_level.setVisibility(View.INVISIBLE);

        } else if (lcs == 1) {
            tv_newlcs_tips.setVisibility(View.INVISIBLE);
            layout_levelup_tips.setVisibility(View.VISIBLE);
            img_planner_level.setVisibility(View.VISIBLE);
            if (needCount > 0) {
                tv_needInvitate_friendCounts.setText(needCount + "");
            }
            img_planner_level.setBackgroundResource(R.drawable.icon_planner_normal);
        } else if (lcs == 2) {
            img_planner_level.setVisibility(View.VISIBLE);
            tv_newlcs_tips.setVisibility(View.INVISIBLE);
            layout_levelup_tips.setVisibility(View.INVISIBLE);
            img_planner_level.setBackgroundResource(R.drawable.icon_planner_senior);
            layout_planner_details.setVisibility(View.GONE);
        }

    }

    @Override
    public void onComplete(Object o) {
        CommonToastUtils.showToast(this, "分享成功");
    }

    @Override
    public void onError(UiError uiError) {
        CommonToastUtils.showToast(this, "ErrorMsg:" + uiError.toString());
    }

    @Override
    public void onCancel() {
        CommonToastUtils.showToast(this, "取消分享");
    }

    @Override
    public void onResponse(BaseResponse baseResponse) {
        if (baseResponse != null) {
            switch (baseResponse.errCode) {
                case WBConstants.ErrorCode.ERR_OK:
                    Toast.makeText(this, R.string.social_share_success, Toast.LENGTH_LONG).show();
                    break;
                case WBConstants.ErrorCode.ERR_CANCEL:
                    Toast.makeText(this, R.string.social_share_canceled, Toast.LENGTH_LONG).show();
                    break;
                case WBConstants.ErrorCode.ERR_FAIL:
                    Toast.makeText(this,
                            getString(R.string.social_share_failed) + "Error Message: " + baseResponse.errMsg,
                            Toast.LENGTH_LONG).show();
                    break;
            }
        }
    }
}
