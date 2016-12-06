package com.mgjr.Utils;

/**
 * Created by wim on 16/5/26.
 */
public class Exceptions {
    public static void illegalArgument(String msg, Object... params)
    {
        throw new IllegalArgumentException(String.format(msg, params));
    }
}
