package com.mgjr.model.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by xuxuefeng on 2016/8/30.
 */
public class bankBean extends BaseBean implements Serializable {


    /**
     * bankcode : ABC
     * bankname : 农业银行
     * cardtype : 0
     * daymax : 0
     * id : 0
     * monthmax : 0
     * onemax : 0
     * remark : 单笔5千，单日5千
     * type : 0
     */

    private List<AuthCardInfo> bankList;

    public List<AuthCardInfo> getBankList() {
        return bankList;
    }

    public void setBankList(List<AuthCardInfo> bankList) {
        this.bankList = bankList;
    }


}
