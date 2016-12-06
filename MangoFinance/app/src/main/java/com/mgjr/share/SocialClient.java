package com.mgjr.share;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import com.mgjr.R;
import com.mgjr.Utils.BitmapToByte;
import com.mgjr.httpclient.callback.StringCallback;
import com.sina.weibo.sdk.api.WebpageObject;
import com.sina.weibo.sdk.api.WeiboMultiMessage;
import com.sina.weibo.sdk.api.share.IWeiboShareAPI;
import com.sina.weibo.sdk.api.share.SendMultiMessageToWeiboRequest;
import com.sina.weibo.sdk.api.share.WeiboShareSDK;
import com.sina.weibo.sdk.utils.Utility;
import com.tencent.connect.share.QQShare;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.tauth.Tencent;

/**
 * Created by wim on 16/9/20.
 */

public class SocialClient {

    private static final String sinaAPPKey = "836209767";
    public IWeiboShareAPI mWeiboShareAPI;

    private static final String AppID = "wx9d6d5130a6afcc5e";
    public IWXAPI wxAPI;

    private static final String QQAPPID = "1105471116";
    public Tencent mTencent;

    private static SocialClient instance = null;
    private Context mContext;
    private WeiboMultiMessage weiboMessage;
    private SendMultiMessageToWeiboRequest sinaRequest;

    private WXMediaMessage wxMediaMessage;
    private Bundle params;

    public SocialClient(Context context) {
        mContext = context;

        mWeiboShareAPI = WeiboShareSDK.createWeiboAPI(context, sinaAPPKey, false);
        mWeiboShareAPI.registerApp();

        wxAPI = WXAPIFactory.createWXAPI(context, AppID);
        wxAPI.registerApp(AppID);

        mTencent = Tencent.createInstance(QQAPPID, context);
    }

    public static SocialClient getInstance(Context context) {
        if (instance == null) {
            synchronized (SocialClient.class) {
                if (instance == null) {
                    instance = new SocialClient(context);
                }
            }
        }

        return instance;
    }

    public SocialClient setWebWithSina(String title, String description, Bitmap bitmap, String webUrl) {
        weiboMessage = new WeiboMultiMessage();
        weiboMessage.mediaObject = getWebpageObj(title, description, bitmap, webUrl);
        return this;
    }

    public void sendRequestToSina(Activity activity) {
        sinaRequest = new SendMultiMessageToWeiboRequest();
        sinaRequest.transaction = String.valueOf(System.currentTimeMillis());
        sinaRequest.multiMessage = weiboMessage;
        mWeiboShareAPI.sendRequest(activity, sinaRequest);
    }

    public SendMultiMessageToWeiboRequest getSinaRequest() {
        return sinaRequest;
    }

    public IWeiboShareAPI getWeiboShareAPI() {
        return mWeiboShareAPI;
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


    public SocialClient setWebMediaMessage(String title, String description, Bitmap bitmap, String webUrl) {
        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = webUrl;

        wxMediaMessage = new WXMediaMessage(webpage);
        wxMediaMessage.title = title;
        wxMediaMessage.description = description;
        wxMediaMessage.thumbData = BitmapToByte.bmpToByteArray(bitmap, true);

        return this;
    }

    public void sendRequestToWX(boolean sceneSession) {
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("webpage");
        req.message = wxMediaMessage;
        req.scene = sceneSession ? SendMessageToWX.Req.WXSceneTimeline : SendMessageToWX.Req.WXSceneSession;
        wxAPI.sendReq(req);
    }

    private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }

    public Bundle setWebQQMessage(String title, String description, String bitmap, String webUrl) {
        params = new Bundle();
        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
        params.putString(QQShare.SHARE_TO_QQ_TITLE, title);
        params.putString(QQShare.SHARE_TO_QQ_SUMMARY, description);
        params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, webUrl);
        params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, bitmap);
        params.putString(QQShare.SHARE_TO_QQ_APP_NAME, "芒果金融");
        return params;
    }

    public void sendRequestToQQ(Activity activity) {

    }

    public Tencent getTencent() {
        return mTencent;
    }
}
