package com.mgjr.share;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.mgjr.R;
import com.mgjr.Utils.MyActivityManager;
import com.mgjr.view.profile.FinancialPlanner.FinancialPlannerActivity;
import com.sina.weibo.sdk.api.WebpageObject;
import com.sina.weibo.sdk.api.WeiboMultiMessage;
import com.sina.weibo.sdk.api.share.BaseResponse;
import com.sina.weibo.sdk.api.share.IWeiboHandler;
import com.sina.weibo.sdk.api.share.IWeiboShareAPI;
import com.sina.weibo.sdk.api.share.SendMultiMessageToWeiboRequest;
import com.sina.weibo.sdk.api.share.WeiboShareSDK;
import com.sina.weibo.sdk.constant.WBConstants;
import com.sina.weibo.sdk.utils.Utility;

/**
 * Created by wim on 16/11/2.
 */

public class WeiboShareActivity extends ActionbarActivity implements IWeiboHandler.Response{

    private IWeiboShareAPI mWeiboShareAPI = null;

    private WeiboMultiMessage weiboMessage;
    private SendMultiMessageToWeiboRequest sinaRequest;


    private static final String sinaAPPKey = "836209767";

    private String title;
    private String description;
    private String url;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mWeiboShareAPI = WeiboShareSDK.createWeiboAPI(this, sinaAPPKey);
        mWeiboShareAPI.registerApp();

        if (savedInstanceState != null) {
            mWeiboShareAPI.handleWeiboResponse(getIntent(),
                    WeiboShareActivity.this);
        }

        Bundle bundle = this.getIntent().getExtras();
        title = bundle.getString("title");
        description = bundle.getString("content");
        url = bundle.getString("url");

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.mango_logo);
        setWebWithSina(title,description,bitmap,url);
    }

    public void setWebWithSina(String title, String description, Bitmap bitmap, String webUrl){
        weiboMessage = new WeiboMultiMessage();
        weiboMessage.mediaObject = getWebpageObj(title,description,bitmap,webUrl);

        sendRequestToSina();
    }

    private WebpageObject getWebpageObj(String title, String description, Bitmap bitmap, String webUrl) {
        WebpageObject mediaObject = new WebpageObject();
        mediaObject.identify = Utility.generateGUID();
        mediaObject.title = title;
        mediaObject.description = description;
        mediaObject.setThumbImage(bitmap);
        mediaObject.actionUrl = webUrl;

        return mediaObject;
    }

    public void sendRequestToSina(){
        sinaRequest = new SendMultiMessageToWeiboRequest();
        sinaRequest.transaction = String.valueOf(System.currentTimeMillis());
        sinaRequest.multiMessage = weiboMessage;
        mWeiboShareAPI.sendRequest(this,sinaRequest);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        mWeiboShareAPI.handleWeiboResponse(intent, this);
    }

    @Override
    public void onResponse(BaseResponse baseResponse) {
        MyActivityManager.getInstance().popCurrentActivity();
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
