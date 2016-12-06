package com.mgjr.Utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.mgjr.share.Gobal;

/**
 * Created by wim on 16/6/13.
 */
public class PreferenceUtils {

    private final String TAG = "PreferenceUtils";

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public PreferenceUtils(Context context){
        sharedPreferences = context.getSharedPreferences(TAG, context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

    }



    public void setWXAccessInfo(String token,String refreshToken,String openid){
        editor.putString(Gobal.WXTOKEN,token);
        editor.putString(Gobal.WXREFRESHTOKEN,refreshToken);
        editor.putString(Gobal.WXOPENID,openid);
        editor.commit();
    }

    public String getWXToken(){
        return sharedPreferences.getString(Gobal.WXTOKEN,"");
    }



    public String getWXRefreshToken(){
        return sharedPreferences.getString(Gobal.WXREFRESHTOKEN,"");
    }


    public String getWXOpenId(){
        return sharedPreferences.getString(Gobal.WXOPENID,"");
    }


    public void setSinaAccessInfo(String token,String refreshToken,String uid){
        editor.putString(Gobal.SINAToken,token);
        editor.putString(Gobal.SINAREFRESHTOKEN,refreshToken);
        editor.putString(Gobal.SINAUID,uid);
        editor.commit();
    }

    public String getSinaToken(){
        return sharedPreferences.getString(Gobal.SINAToken,"");
    }

    public String getSinaRefreshToken(){
        return sharedPreferences.getString(Gobal.SINAREFRESHTOKEN,"");
    }

    public String getSinaUID(){
        return sharedPreferences.getString(Gobal.SINAUID,"");
    }

    public void setQQAccessInfo(String openid,String access_token){
        editor.putString(Gobal.QQOPENID,openid);
        editor.putString(Gobal.QQTOKEN,access_token);
        editor.commit();
    }

    public String getQQToken(){
        return sharedPreferences.getString(Gobal.QQTOKEN,"");
    }

    public String getQQOpenID(){
        return sharedPreferences.getString(Gobal.QQOPENID,"");
    }

    public void removeQQInfo(){
        editor.remove(Gobal.QQTOKEN);
        editor.remove(Gobal.QQOPENID);
        editor.commit();
    }



    public void removeSinaInfo(){
        editor.remove(Gobal.SINAToken);
        editor.remove(Gobal.SINAREFRESHTOKEN);
        editor.remove(Gobal.SINAUID);
        editor.commit();
    }

    public void removeWXInfo(){
        editor.remove(Gobal.WXTOKEN);
        editor.remove(Gobal.WXREFRESHTOKEN);
        editor.remove(Gobal.WXOPENID);
        editor.commit();
    }



}
