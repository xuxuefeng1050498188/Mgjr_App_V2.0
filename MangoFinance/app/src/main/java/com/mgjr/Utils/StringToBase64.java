package com.mgjr.Utils;

import android.util.Base64;

/**
 * Created by wim on 16/8/16.
 */
public class StringToBase64 {

    private static String TAG = "StringToBase64";

    public static String stringToBase64(String string) {

        String base64Token = Base64.encodeToString(string.trim().getBytes(),
                Base64.NO_WRAP);

        return base64Token;


    }
}
