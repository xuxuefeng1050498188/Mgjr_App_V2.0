package com.mgjr.Utils;

/**
 * Created by Administrator on 2016/8/30.
 */
public class Double2int {

    public static Integer double2Int(Double d) {
        int i = 0;
        if (d != null) {
            String sd = d.toString();
            i = Integer.parseInt(sd.substring(0, sd.indexOf(".")));
        }
        return i;
    }


}
