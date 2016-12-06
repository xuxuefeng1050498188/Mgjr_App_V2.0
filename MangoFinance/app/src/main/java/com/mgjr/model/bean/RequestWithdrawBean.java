package com.mgjr.model.bean;


/**
 * Created by xuxuefeng on 2016/8/31.
 */
public class RequestWithdrawBean extends BaseBean {

    /**
     * actname : 王杰海
     * address :
     * area :
     * bankname : ICBC
     * cardno : 6222021901010955902
     * city :
     * ctime : 1472456233000
     * id : 624
     * mid : 741
     * province :
     * rechargeamount : null
     * status : 0
     * withdrawamount : null
     */

    private AccountBankcardBean accountBankcard;
    /**
     * accountBankcard : {"actname":"王杰海","address":"","area":"","bankname":"ICBC","cardno":"6222021901010955902","city":"","ctime":1472456233000,"id":624,"mid":741,"province":"","rechargeamount":null,"status":0,"withdrawamount":null}
     * amount : 110400.33
     * cardCode : ICBC
     * cardName : 工商银行
     * channel : 29
     * remark : 单笔5千，单日5万
     */

    private double amount;
    private double accountAmount;

    public double getAccountAmount() {
        return accountAmount;
    }

    public void setAccountAmount(double accountAmount) {
        this.accountAmount = accountAmount;
    }

    private String cardCode;
    private String cardName;
    private int channel;
    private String remark;
    private String bankicon;

    public String getBankicon() {
        return bankicon;
    }

    public void setBankicon(String bankicon) {
        this.bankicon = bankicon;
    }

    /**
     * ainfo : null
     * amount : 2
     * atime : null
     * code : 160914175759840799
     * ctime : 1473847079885
     * fee : 1
     * flowno : null
     * fromsource : 5
     * id : null
     * info : {"actName":"许雪峰","bankAddress":"","bankCode":"CBC","bankName":"CBC","cardNo":"6236682920007176421","citycode":"340100","provincecode":"340000","type":0}
     * mid : 2923
     * status : 0
     * thdata : null
     */

    private AccountWithdrawBean accountWithdraw;

    public AccountBankcardBean getAccountBankcard() {
        return accountBankcard;
    }

    public void setAccountBankcard(AccountBankcardBean accountBankcard) {
        this.accountBankcard = accountBankcard;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getCardCode() {
        return cardCode;
    }

    public void setCardCode(String cardCode) {
        this.cardCode = cardCode;
    }

    public String getCardName() {
        return cardName;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    public int getChannel() {
        return channel;
    }

    public void setChannel(int channel) {
        this.channel = channel;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public AccountWithdrawBean getAccountWithdraw() {
        return accountWithdraw;
    }

    public void setAccountWithdraw(AccountWithdrawBean accountWithdraw) {
        this.accountWithdraw = accountWithdraw;
    }


    public static class AccountWithdrawBean {
        private Object ainfo;
        private int amount;
        private long atime;
        private String code;
        private long ctime;
        private int fee;
        private String flowno;
        private int fromsource;
        private String id;
        private String info;
        private int mid;
        private int status;
        private String thdata;

        public Object getAinfo() {
            return ainfo;
        }

        public void setAinfo(Object ainfo) {
            this.ainfo = ainfo;
        }

        public int getAmount() {
            return amount;
        }

        public void setAmount(int amount) {
            this.amount = amount;
        }

        public long getAtime() {
            return atime;
        }

        public void setAtime(long atime) {
            this.atime = atime;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public long getCtime() {
            return ctime;
        }

        public void setCtime(long ctime) {
            this.ctime = ctime;
        }

        public int getFee() {
            return fee;
        }

        public void setFee(int fee) {
            this.fee = fee;
        }

        public String getFlowno() {
            return flowno;
        }

        public void setFlowno(String flowno) {
            this.flowno = flowno;
        }

        public int getFromsource() {
            return fromsource;
        }

        public void setFromsource(int fromsource) {
            this.fromsource = fromsource;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getInfo() {
            return info;
        }

        public void setInfo(String info) {
            this.info = info;
        }

        public int getMid() {
            return mid;
        }

        public void setMid(int mid) {
            this.mid = mid;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getThdata() {
            return thdata;
        }

        public void setThdata(String thdata) {
            this.thdata = thdata;
        }
    }
}
