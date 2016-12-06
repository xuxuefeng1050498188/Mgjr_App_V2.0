package com.mgjr.share;

import com.mgjr.Utils.StringToMD5;

import java.util.List;

/**
 * Created by wim on 16/8/16.
 */
public class APIBuilder {

//    public static final String baseUrl = "http://192.168.80.146:8090/mgjr-web-app/";
//    public static final String H5Url = "http://192.168.80.146:8090/mgjr-web-app/";
//
    //彭勇辉
//    public static final String baseUrl ="http://192.168.80.103:8080/mgjr-web-app/";
//    public static final String H5Url = "http://192.168.80.103:8080/mgjr-web-app/";

    /*凯秦*/
//    public static final String baseUrl = "http://192.168.80.158:8080/mgjr-web-app/";
//    public static final String H5Url = "http://192.168.80.158:8080/mgjr-web-app/";

    /*陈威*/
//    public static final String H5Url = "http://192.168.80.178:8080/mgjr-web-app/";

    /*测试 http://192.168.30.196:8080/*/
//    public static final String baseUrl = "http://192.168.30.196:8080/mgjr-web-app/";

    /*域发布*/
//    public static final String baseUrl = "https://www.testhnmgjr.com/mgjr-web-app/";
//    public static final String H5Url = "https://www.testhnmgjr.com/mgjr-web-app/";


    /*正式环境*/
    public static final String baseUrl = "https://app.hnmgjr.com/mgjr-web-app/";
    public static final String H5Url = "https://app.hnmgjr.com/mgjr-web-app/";

    public static final String version = "V2.0/";

    /*=============================================================================================*/

    //会员登录
    public static String loginUrl() {
        return "appAuth/login";
    }

    //注册短信验证码(注册)
    public static String registerGetSmsCodeUrl() {

        return "appAuth/smsByMobile";
    }

    //注册第一步
    public static String registerStepOneUrl() {
        return "appAuth/registerFrist";
    }

    //注册第二步
    public static String registerStepTwoUrl() {
        return "appAuth/register";
    }

    //忘记登录密码
    public static String resetLoginPwdUrl() {
        return "appSecurity/forgetLoginPwd";
    }

    //获取图片验证码
    public static String registerGetImgCodeUrl() {

        return "appAuth/vCode";
    }

    //广告页
    public static String advertisement() {

        return "appIndex/advertisement";
    }


    //摇一摇查昨日收益
    public static String getYestodayIncomeUrl() {

        return "appAccount/getYesterdayIncome";
    }

    //首页推荐标
    public static String homepageRecommendProjects() {

        return "appIndex/loanList";
    }

    //首页活动弹窗
    public static String homepageBanner() {

        return "appIndex/banner";
    }

    //首页活动banner
    public static String homepageEventsWindow() {

        return "appIndex/poster";
    }

    //个人中心数据
    public static String userCenterDataUrl() {

        return "appAccount/Main";
    }

    //实名认证
    public static String realNameUrl() {

        return "appSecurity/realName";
    }

    //我的体验金
    public static String myTyjUrl() {

        return "appXstyb/myTyj";
    }

    //新手体验标
    public static String newcomerTasteTenderUrl() {

        return "appXstyb/tenderConfirm";
    }

    //新手标投标

    public static String newcomerTasteTenderInvestUrl() {

        return "appXstyb/doTender";
    }

    //活动中心
    public static String eventsUrl() {

        return "appActivity/activityList";
    }

    //资金明细
    public static String capitalDetailsUrl() {

        return "appAccount/billDetails";
    }

    //红包理财券
    public static String mangoBoxUrl() {

        return "appActivityGift/myMgBox";
    }

    //我的活期宝
    public static String myHqb() {

        return "appHqb/myHqb";
    }

    //活期宝详情appHqb/detail
    public static String myHqbProjectDetails() {

        return "appHqb/tenderDetail";
    }

    //我的活期宝--持有中项目
    public static String myTenderingList() {

        return "appHqb/myTenderingList";
    }

    //活期宝单个赎回
    public static String hqbSingleRedeem() {

        return "appHqb/toRedeem";
    }

    //活期宝单个赎回确认appHqb/doTender
    public static String hqbSingleRedeemConfirm() {

        return "appHqb/doRedeem";
    }

    //活期宝已赎回
    public static String myOverTenderList() {

        return "appHqb/myOverTenderList";
    }

    //活期宝多项赎回列表 - 跳转到下一步
    public static String hqbMutilRedeemedList() {

        return "appHqb/toRedeemMultiList";
    }

    //活期宝多项赎回提交
    public static String hqbMutilRedeemed() {

        return "appHqb/toRedeemMulti";
    }

    //活期宝多笔赎回确认appHqb/doTender
    public static String hqbMutilRedeemConfirm() {

        return "appHqb/doRedeemMulti";
    }

    //活期宝流水
    public static String transactionDetail() {

        return "appHqb/transactionDetail";
    }

    //活期宝流水
    public static String transactionDetailJmg() {

        return "appJmg/transactionDetail";
    }


    //修改手机号(原手机能接收短信)
    public static String changeMobile() {

        return "appSecurity/changePhone";
    }

    //修改手机号(原手机不能接收短信)
    public static String changeMobileOld() {

        return "appSecurity/validateOldPhone";
    }

    //芒果理财师
    public static String appFinancialPlanner() {

        return "appFinancialPlanner/main";
    }

    //好友列表appFinancialPlanner/friendList
    public static String friendlistUrl() {

        return "appFinancialPlanner/friendList";
    }

    //理财师奖励明细
    public static String tenderRewardListUrl() {

        return "appFinancialPlanner/tenderRewardList";
    }

    //获取设备信息
    public static String getDevice() {
        return "Android";
    }

    //获取投资列表首页和历史记录
    public static String investHomeUrl() {
        return "appInvest/loanList";
    }

    //获取金芒果投资记录
    public static String investJmgRecordUrl() {
        return "appJmg/tenderingList";
    }

    //获取活期宝投资记录
    public static String investHqbRecordUrl() {
        return "appHqb/tenderingList";
    }

    //获取金芒果产品详情
    public static String investJmgDetailUrl() {
        return "appJmg/detail";
    }

    //获取活期宝产品详情
    public static String investHqbDetailUrl() {
        return "appHqb/detail";
    }


    public static String getKeyStr(List<String> list) {
        String keyStr = "";
        for (String string : list) {
            keyStr += string;
        }
        keyStr += readConfigString();
//        LogUtil.e("加密前Key" + keyStr);
        keyStr = StringToMD5.stringToMD5(keyStr);
        return keyStr;
    }


    public static String readConfigString() {
        return "d0c2993aaaff1af2bf3725f858fb8b5b";
    }


    /**
     * 获取银行卡列表的地址
     *
     * @return
     */
    public static String selectBankUrl() {
        {
            return "appAccount/getBankList";
        }
    }

    /**
     * 添加银行卡是否成功的接口
     *
     * @return
     */
    public static String addBankCardUrl() {
        return "appSecurity/bindBankCard";
    }

    /**
     * 申请充值的URL
     *
     * @return
     */
    public static String requestRechargeUrl() {
        return "appAccount/getRechargeImfo";
    }

    /**
     * 申请提现的URL
     *
     * @return
     */
    public static String requestWithdrawUrl() {
        return "appAccount/getWithdrawImfo";
    }

    /**
     * 账户设置的URL
     *
     * @return
     */
    public static String accountSettingUrl() {
        return "appAuth/accountSeting";
    }

    /**
     * 我的金芒果数据URL
     *
     * @return
     */
    public static String myJmgDataUrl() {
        return "appJmg/myJmg";
    }

    /**
     * 充值URL
     *
     * @return
     */
    public static String rechargeUrl() {
        return "appAccount/doRecharge";
    }

    /**
     * 提现URL
     *
     * @return
     */
    public static String withdrawUrl() {
        return "appAccount/doWithdraw";
    }

    /**
     * 昵称上传的地址
     *
     * @return
     */
    public static String uploadNicknameUrl() {
        return "appAuth/setNickName";
    }

    /**
     * 头像上传的URL
     *
     * @return
     */
    public static String uploadHeadImageUrl() {
        return "appAuth/doUploadHeadImg";
    }

    /**
     * 活期宝确认投资申请数据地址
     *
     * @return
     */
    public static String hqbInvestConfirmUrl() {
        return "appHqb/tenderConfirm";
    }

    /**
     * 金芒果确认投资申请数据地址
     *
     * @return
     */
    public static String jmgInvestConfirmUrl() {
        return "appJmg/tenderConfirm";
    }

    /**
     * 我的金芒果投资中项目URL
     *
     * @return
     */
    public static String myJmgInvestingProjectsUrl() {
        return "appJmg/myTenderingList";
    }


    /**
     * 我的金芒果已结束项目URL
     *
     * @return
     */
    public static String myJmgFinishedProjectsUrl() {
        return "appJmg/myOverTenderList";
    }

    /**
     * 我的金芒果投资记录详情
     *
     * @return
     */
    public static String myJmgInvestDetailUrl() {
        return "appJmg/tenderDetail";
    }

    public static String bindBankCardUrl() {
        return "appAccount/bindBankCard";
    }

    public static String myJmgTransactionDetailUrl() {
        return "appJmg/transactionDetail";
    }

    /*微信客服*/
    public static String wxCustomerProblem() {
        return "appStatic/wxCustomerProblem";
    }

    /*关于账户*/
    public static String accountProblem() {
        return "appStatic/accountProblem";
    }

    /*关于银行卡*/
    public static String bankCardProblem() {
        return "appStatic/bankCardProblem";
    }

    /*关于投资*/
    public static String investmentProblem() {
        return "appStatic/investmentProblem";
    }

    /*关于提现*/
    public static String cashProblem() {
        return "appStatic/cashProblem";
    }

    /*关于芒果体验金*/
    public static String experienceGoldProblem() {
        return "appStatic/experienceGoldProblem";
    }

    /*关于芒果宝盒*/
    public static String boxProblem() {
        return "appStatic/boxProblem";
    }

    /*关于金芒果-新手福利标*/
    public static String jmgNoviceProblem() {
        return "appStatic/jmgNoviceProblem";
    }

    /*新手福利标产品介绍*/
    public static String newscomerIntroduce() {
        return "appStatic/newscomerIntroduce";
    }

    /*关于活期宝*/
    public static String hqbProblem() {
        return "appStatic/hqbProblem";
    }

    /*关于充值*/
    public static String paycheckProblem() {
        return "appStatic/paycheckProblem";
    }

    /*常见问题 -- 热门问题*/
    public static String hotProblem() {
        return "appStatic/hotProblem";
    }

    /*常见问题 -- 分类问题*/
    public static String classProblem() {
        return "appStatic/classProblem";
    }

    /*联系我们*/
    public static String contactUs() {
        return "appStatic/contactUs";
    }

    /*赎回须知*/
    public static String redeemNotes() {
        return "appStatic/redeemNotes";
    }

    /*购买须知*/
    public static String buyNotes() {
        return "appStatic/buyNotes";
    }

    /*产品介绍 -- 活期宝*/
    public static String hqbIntroduce() {
        return "appStatic/hqbIntroduce";
    }

    /*金芒果项目介绍*/
    public static String projectIntroduce() {
        return "appJmg/projectIntroduce";
    }

    /*项目详情（金芒果）企业信息*/
    public static String businessInfo() {
        return "appJmg/businessInfo";
    }

    /*项目详情（金芒果）保障信息*/
    public static String guaranteeInfo() {
        return "appJmg/guaranteeInfo";
    }

    /*平台公告*/
    public static String notice() {
        return "appStatic/notice";
    }

    /*关于我们*/
    public static String aboutUs() {
        return "appStatic/aboutUs";
    }

    /*平台安全*/
    public static String platformSecurity() {
        return "appStatic/platformSecurity";
    }

    /*用户注册协议*/
    public static String gvrp() {
        return "appStatic/gvrp";
    }

    /*债券转让协议*/
    public static String bondTransfer() {
        return "appStatic/bondTransfer";
    }

    //规则攻略
    public static String lcRaiders() {
        return "appStatic/lcRaiders";

    }

    public static String accountManageUrl() {
        return "appSecurity/accountManager";
    }


    //检查更新
    public static String checkUpdate() {
        return "appHome/checkUpdate";
    }

    //重要通知
    public static String importantNoice() {
        return "appHome/importantNoice";

    }

    //意见反馈
    public static String saveAppOptionCollect() {
        return "appHome/saveAppOptionCollect";

    }


    public static String requestBankCardInfoUrl() {
        return "appAccount/getBankCardInfo";
    }

    public static String changeBankCardUrl() {
        return "appAccount/replaceBankCard";
    }

    public static String modifyBankCardInfoUrl() {
        return "appAccount/updateAccountBankcard";

    }

    //bug收集
    public static String crashCollect() {
        return "appHome/saveAppOptionCollect";
    }
}
