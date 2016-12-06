package com.mgjr.model.bean;

import java.util.List;

/**
 * Created by xuxuefeng on 2016/8/22.
 */
public class InvestBean extends BaseBean {

    /**
     * `id` int(11) NOT NULL AUTO_INCREMENT,
     * `code` varchar(50) COLLATE utf8_unicode_ci NOT NULL COMMENT '外部编号',
     * `title` varchar(100) COLLATE utf8_unicode_ci NOT NULL COMMENT '借款标题',
     * `summary` text COLLATE utf8_unicode_ci COMMENT '借款相关描述',
     * `amount` decimal(15,2) NOT NULL DEFAULT '0.00' COMMENT '借款金额',
     * `balance` decimal(15,2) NOT NULL DEFAULT '0.00' COMMENT '投标余额',
     * `zy` decimal(8,1) DEFAULT '0.0' COMMENT '增益',
     * `rate` decimal(8,2) NOT NULL DEFAULT '0.00' COMMENT '年化率',
     * `rtype` tinyint(4) NOT NULL DEFAULT '0' COMMENT '还款方式  4 天标',
     * `period` int(11) NOT NULL DEFAULT '0' COMMENT '借款期限',
     * `mtype` int(11) NOT NULL DEFAULT '0' COMMENT '模型类型 0为基本借贷模块 其他为扩展模块',
     * `typeid` int(11) NOT NULL DEFAULT '0' COMMENT '该借款标所属产品类型',
     * `mid` int(11) NOT NULL,
     * `payno` int(11) NOT NULL DEFAULT '0' COMMENT '已还期数',
     * `bstime` datetime DEFAULT NULL COMMENT '招标开始时间',
     * `betime` datetime DEFAULT NULL COMMENT '招标预计结束时间',
     * `bdtime` datetime DEFAULT NULL COMMENT '招标完成时间',
     * `cstime` datetime DEFAULT NULL COMMENT '初审时间',
     * `fstime` datetime DEFAULT NULL COMMENT '复审时间',
     * `nxtime` datetime DEFAULT NULL COMMENT '下个还款日',
     * `mint` decimal(15,2) NOT NULL DEFAULT '0.00' COMMENT '最低投资金额',
     * `maxt` decimal(15,2) NOT NULL DEFAULT '0.00' COMMENT '最高投资金额',
     * `dtinfo` text COLLATE utf8_unicode_ci COMMENT '定投信息',
     * `exts` text COLLATE utf8_unicode_ci COMMENT '项目扩展信息',
     * `ctime` datetime DEFAULT NULL COMMENT '提交时间',
     * `status` int(11) NOT NULL DEFAULT '0' COMMENT '借款状态   0 ：未提交1：初审中，11：初审拒绝，2：招标中，3：复审中，30：流标，33 ：复审拒绝，100：还款中，200 ：还款完成，-1：关闭  ',
     */

    private List<LoanBean> loan;
    private HqbBean hqb;

    public HqbBean getHqb() {
        return hqb;
    }

    public void setHqb(HqbBean hqb) {
        this.hqb = hqb;
    }

    public List<LoanBean> getLoan() {
        return loan;
    }

    public void setLoan(List<LoanBean> loan) {
        this.loan = loan;
    }


}
