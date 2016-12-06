package com.mgjr.httpclient.callback;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by wim on 16/8/17.
 */
public abstract class BitmapCallback extends Callback<Bitmap>
{
    @Override
    public Bitmap parseResponse(Response response) throws Exception {
        return BitmapFactory.decodeStream(response.body().byteStream());
    }

}
