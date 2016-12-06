package com.mgjr.httpclient.cookie;

import java.util.List;

import okhttp3.Cookie;
import okhttp3.HttpUrl;

/**
 * Created by wim on 16/5/26.
 */
public interface CookieStore {
    public void add (HttpUrl url, List<Cookie> cookies);

    public List<Cookie> get(HttpUrl url);

    public List<Cookie> getCookies();

    public boolean remove (HttpUrl url, Cookie cookie);

    public boolean removeAll();
}
