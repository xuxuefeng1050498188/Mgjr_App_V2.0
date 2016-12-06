package com.mgjr.Utils;

import com.mgjr.R;

/**
 * Created by xuxuefeng on 2016/9/8.
 */
public class BankCardUtils {
    /**
     * 根据后台拿到的bankcode返回logo
     *
     * @param cardCode
     * @return
     */
    public static int getBankLogoResource(String cardCode) {
        int logoResId = 0;
        switch (cardCode) {
            case "ABC":
                logoResId = R.drawable.logo_abc_0;
                break;
            case "ICBC":
                logoResId = R.drawable.logo_icbc_1;
                break;
            case "CMBC":
                logoResId = R.drawable.logo_cmbc_2;
                break;
            case "BOC":
                logoResId = R.drawable.logo_boc_3;
                break;
            case "CBC":
                logoResId = R.drawable.logo_cbc_4;
                break;
            case "CEB":
                logoResId = R.drawable.logo_ceb_5;
                break;
            case "HXB":
                logoResId = R.drawable.logo_hxb_6;
                break;
            case "CNCB":
                logoResId = R.drawable.logo_cncb_7;
                break;
            case "CIB":
                logoResId = R.drawable.logo_cib_8;
                break;
            case "PAB":
                logoResId = R.drawable.logo_pab_9;
                break;
            case "SPDB":
                logoResId = R.drawable.logo_spdb_10;
                break;
            case "PSBC":
                logoResId = R.drawable.logo_psbc_11;
                break;
            case "GDB":
                logoResId = R.drawable.logo_gdb_12;
                break;
            case "BCM":
                logoResId = R.drawable.logo_bcm_13;
                break;
            case "CMSB":
                logoResId = R.drawable.logo_cmsb_14;
                break;
        }
        return logoResId;
    }

    /**
     * 判断是否是银行卡号
     * @author WJ
     * @param cardNo
     * @return
     */
    public static boolean checkBankCard(String cardNo) {
        char bit = getBankCardCheckCode(cardNo
                .substring(0, cardNo.length() - 1));
        if (bit == 'N') {
            return false;
        }
        return cardNo.charAt(cardNo.length() - 1) == bit;

    }

    private static char getBankCardCheckCode(String nonCheckCodeCardId) {
        if (nonCheckCodeCardId == null
                || nonCheckCodeCardId.trim().length() == 0
                || !nonCheckCodeCardId.matches("\\d+")) {
            // 如果传的不是数据返回N
            return 'N';
        }
        char[] chs = nonCheckCodeCardId.trim().toCharArray();
        int luhmSum = 0;
        for (int i = chs.length - 1, j = 0; i >= 0; i--, j++) {
            int k = chs[i] - '0';
            if (j % 2 == 0) {
                k *= 2;
                k = k / 10 + k % 10;
            }
            luhmSum += k;
        }
        return (luhmSum % 10 == 0) ? '0' : (char) ((10 - luhmSum % 10) + '0');
    }
}
