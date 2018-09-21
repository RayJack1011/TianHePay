package com.tianhe.pay.ui.modify;

/**
 * Created by wangya3 on 2018/3/27.
 */

public class ModifyReq {
    public String sign;

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public class Data{
        public String userNo;
        public String oldPasswd;
        public String newPasswd;
        public String shopNo;
        public Data(String userNo,String oldPasswd,String newPasswd,String shopNo){
            this.userNo = userNo;
            this.oldPasswd = oldPasswd;
            this.newPasswd = newPasswd;
            this.shopNo = shopNo;
        }

        public void setUserNo(String userNo) {
            this.userNo = userNo;
        }

        public void setShopNo(String shopNo) {
            this.shopNo = shopNo;
        }

        public void setOldPasswd(String oldPasswd) {
            this.oldPasswd = oldPasswd;
        }

        public void setNewPasswd(String newPasswd) {
            this.newPasswd = newPasswd;
        }

        public String getNewPasswd() {
            return newPasswd;
        }

        public String getOldPasswd() {
            return oldPasswd;
        }

        public String getShopNo() {
            return shopNo;
        }

        public String getUserNo() {
            return userNo;
        }
    }
    }

