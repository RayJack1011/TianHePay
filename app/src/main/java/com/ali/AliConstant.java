package com.ali;

public class AliConstant {
    /** 天和支付宝帐号 */
    public static final String PID = "2088821495046450";
    /** 测试帐号 */
//    public static final String PID = "2088102175272924";

    /** 支付宝正式接口URL */
    public static final String API_URL = "https://openapi.alipay.com/gateway.do";
    /** 支付宝测试接口URL */
//    public static final String API_URL = "https://openapi.alipaydev.com/gateway.do";

    /** 天和移动收银APPID */
    public static final String APP_ID = "2018030602323561";
    /** 测试用的APPID */
//    public static final String APP_ID = "2016091200492895";

    // RSA私钥，用于对商户请求报文加签
    /** 天和的应用私钥 */
    public static final String PRIVATE_KEY = "MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQCBgEeGAh2bTwrQ8k5DrHeM8U98e6pFGwyfmAajBpk22HYhOY8uTba/YBu4SMZhP6pcrewLmLdHOUf1boOfx32OheYj8Qimqthzf7z3KmJOZJ2Y3WIY1kQrb3/SZ1/CxGojF9a1Td3fvzxmvwHJ6Y4BtmrJAY7uu2jqWuyFKNtqp2pB0QHDh+BoMyH7jzcTU8/mWd6Pvsff2M+HuO8kLWN5pvh6F4kdj5OeLnWmBw9tOtEAzSleSIa047u9kbKONnkuPDRtMsV6eQyVxjUrEnKK7MUrNopLhI2oGOf3ZfRXM/e+xkCfPl0ZtMl5H7dm0aRuUz9mSfOy0voXFgg6N2qxAgMBAAECggEAHyzjiwY9XGMl5nE+mBoP2bVJ/lzC+8nsOwtdJaf0AHkRzyPNLUd2JBId+jpCz6yE9bTE63HDRXkrspD9TNy4hDQF2EcxXvYoD34D3risW9Tq/57j+7ixRoecDOnn6rpgFrypapIpRLED+HQpGROJpS3l/SfL4frp4ekroU7Zq43xAee5JqTrCwOmUH+7TvpGklAl+Q6O+Opp+wYfEGvPaS+nVGkafqnRKmuqkoqm8FeUMYppcal84kPkg8CxscjRJEzX64tXDuNZ5lT8CbfkeWtn+nIPY45XaNJy96taaWB6hEXfIjwKuexbFq5/hnBscn+T729S40m9HBytnekM9QKBgQDwI6jAq63R9afwc29kiSW2hP4LDGHRnyE7E7Ro0GSwlSwPEqXIoGNJM47bU7/f2Byxzd0ZU/KZRVniamzqVGqqNJhfQtbbavtIn/MKnsxxk0hcftJLfez7d7V0r7AAeV1AC/LW6bBjUbyey7I/jvWK3eHtCAlEj8rPmHaH1AmPuwKBgQCKDetS/BJNJXl4HPMnqrhivtNsr0dZJLypDaUtrM18/UGkKoITs1CK7YXFWYsyttfrOYQHeMWelKKUxmkvMGzi/w9LfxqgcFKCjspqD9o5/0lj0v4zpU6R9Y4D0h1YJgfaImxKBI6Pe/LT/ypaeZIHgXtThSBXFXToPuPiCd+6gwKBgDFZYqiVn1bygNytHEsw1As6MWrb1I5bgVy+o+iWStrvDOrcDi24qC625bi08U1zABKaF8HIuqY81kftTwyo5HKt9FI/TiAxUAqQtEWOdL0yY0B+34CcFENpSmT8ZdLzHfK3geQgihsfBQAtJChjnJo5FuC7Mgxg8HDRCCrJrF1ZAoGAF1aJDKtOULpgEX0bYf5LU24dAceT0S8TsaR74v/OcS3DFgSVnsz2dbX3okgj7vvX/oznk/fHuKo53dq412BhV/J0XHftQWgbmrSW5V0usBVHC9gUXxi38pbHXj/78/Xh5+9fzRd+HiV/BHWjbE4W8Kvj9FeSAwa+j7BqB8FRkA8CgYAOas+R0GR6NoCHNUy2FScWepk8MvoD7YlR4uAAB32Rg6d3Mva1D7DWuQH3sK1Deu8hLQCMnprOUNFGQhod2Uc2kJgd21PSDXQxm+yN+5zLVST78zndBsB2kR6caGNlzhmXond6zaYug8wjQSeqDVURH9M34eqae30UQmlzhxrCpg==";
    /** 测试用的应用私钥 */
//    public static final String PRIVATE_KEY = "MIIEvwIBADANBgkqhkiG9w0BAQEFAASCBKkwggSlAgEAAoIBAQCqhwUzD6f8mtrE1H0VyzozyU6mZ/nnKyy+SZcJ5iPD5Rc2tDrnyF2z7ID4Eh6kh6mHzksFcFmN7qi+pAzFaHYdsQT+UfyMaNXLG8VGVn2/u+vWYvfxyNxG8GxpSZ5VyUC0wOUuleiuuSN6PivtivZUgiyXoHjYgY9PHYR1P32MS0tM2adtk7ZMOmdYJDH1Fx5sLtLhZwVtczUCCbJphfm+s2EoPzd6RaXK6LnClsOc1mHJ3qeeNLDER4kXFduqId2sGpJgIuyJ5oyeliH5U7wR199Zu7Rywu8ipnyqRRFKBJHTOMxZWa/A6+reU9hxVijc7rPmD980gt0/bQPkTMgHAgMBAAECggEBAKU4JpB/Zco4GyhDnGHmQqD0NVDDQvx392TylfVQVmyxZNlqq/vwROFTju1LWQceLeJXvJYrcYkzp6j19y249V/1KBpkQyWjWSjt4igxGqsPHwPbZdGMQ+gzf0ZPvqntUNUQY7zMH+JRnI8O966ySdI9QYkzQIK8nKYRukmDV1f58HqvKNXdJRqcKIYfXNR63AXAFJ0niZb1uWobY0qWQ0dSbMjGnxLM4/4NSqP4R1817SniIq4yHEyewUXxzpMSpKbwyexu/ozEHxukl2/RHcxAVSaCcW67wd+fRW2ldnLv3JMGLhXHBbTdBHFCYyIsSASc8+mitd637qWb8woJd7kCgYEA6DvGwJgqk2OOtyCMvDH0bcXqIqWq90hKC9gnYf/j0FO5b25gWxm3TVw8gz3pSa/UHC1sURjYZK8KIq4CD2UyRT4ESd4ZWAekt65zfUTvuCwL4qT2yyCeKt63WscMHMtdHSp80mAfOseQwsDdyBoJJW+lWdxkGaP38NhDsA+uvl0CgYEAu/qfcbaKRuaV0N9oMl18YH0xbXWoP6TqZNyJdEIqP3TtgIT/VCDvqLpFDCDI7TjV6/7c6Mf4B/weQXUNFkyyL5ZzVMFdC02tXpW0r9apbSGZKoXZnUZFAGeNsLn+sVlz1wlpPN0J0hxJqG86f4Q8gLttxZe9tvjjNj26u03HkbMCgYAoVEEmrwYtxEtDaAaoo/Azo/wN+kEfYlFhjy7/qMVyJyf7vupCx8t6RrtHe1fo8Pzfy8+UocDqcy9KaHd5HvTE64kpHsW98M3dDBGIPpF2pUeHG2i0S7zL6xNiU1C6K2LHLtqwWfryZTexanbZShQuiR3o5goIHd5v186hY4fQDQKBgQC5MCqsKB0JxFiLscxA8gB6PlvXbjYWnS2dt/ZiZlygr1zbZCr2gowYOYmUbSgFLBzzpd2UJoyfsS1NZzdBZMWWRC3SrbwwSpIlwG6FXIEFVjhCwCKm4ndP8rSQCmxz46BPlKHsqAXozJ10JkAHKAtC92MpalgoTgHpid7Qb8vQKQKBgQCypflxTl3t+AnR3E0JmahcHpvLxKDOwJNcqDs3tCFDCHTuNE+d8aGBv9RPgkRyY7wWISETPFmbH1bWh+z/pLV1h9Ae0Y4KKxWnoryjtD9WOO5G7t+HQwVJfw+3l/ETQ2ym6Jcauf/PPvFgSWfiPtu4ade5U7JljnxgiGzHPdlQYw==";

    // RSA公钥，仅用于验证开发者网关, 并没卵用(给支付宝用的)
    /** 天和的应用公钥 */
    public static final String PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAgYBHhgIdm08K0PJOQ6x3jPFPfHuqRRsMn5gGowaZNth2ITmPLk22v2AbuEjGYT+qXK3sC5i3RzlH9W6Dn8d9joXmI/EIpqrYc3+89ypiTmSdmN1iGNZEK29/0mdfwsRqIxfWtU3d3788Zr8ByemOAbZqyQGO7rto6lrshSjbaqdqQdEBw4fgaDMh+483E1PP5lnej77H39jPh7jvJC1jeab4eheJHY+Tni51pgcPbTrRAM0pXkiGtOO7vZGyjjZ5Ljw0bTLFenkMlcY1KxJyiuzFKzaKS4SNqBjn92X0VzP3vsZAnz5dGbTJeR+3ZtGkblM/ZknzstL6FxYIOjdqsQIDAQAB";
    /** 测试用的应用公钥 */
//    public static final String PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAqocFMw+n/JraxNR9Fcs6M8lOpmf55yssvkmXCeYjw+UXNrQ658hds+yA+BIepIeph85LBXBZje6ovqQMxWh2HbEE/lH8jGjVyxvFRlZ9v7vr1mL38cjcRvBsaUmeVclAtMDlLpXorrkjej4r7Yr2VIIsl6B42IGPTx2EdT99jEtLTNmnbZO2TDpnWCQx9RcebC7S4WcFbXM1AgmyaYX5vrNhKD83ekWlyui5wpbDnNZhyd6nnjSwxEeJFxXbqiHdrBqSYCLsieaMnpYh+VO8EdffWbu0csLvIqZ8qkURSgSR0zjMWVmvwOvq3lPYcVYo3O6z5g/fNILdP20D5EzIBwIDAQAB";

    // 支付宝RSA公钥，用于验签支付宝应答
    /** 天和的支付宝公钥 */
    public static final String ALI_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEApyEYEeVPuXkQ4xye8ANQQcAAIFtv8HBbP0hcKh+5cLWAH0uGZ7aNvYyjBamyldxXwPOzpOSVlGCtWfBuitTxQ0cBwucLbUly584Z6z2UQyefQiilErh1G1lNXhFSphhH6+stVLgJNUG2RJZuW3FwosTC5+VjXzrbM1bzZwmyWrsN7LAtXjPJ1+SqnECnLL+RHnjuaqamtuyNgVVi/4ed8sNTe7uauMFseMVbIJW4xq47Pl7K8jZ/C3TlpUla+IWMVXffSM3vf7TT/axrYvEnzXPo7tk0oGWhCEhQNu/RCBqRRYWuq18wvhePuxxEM4MLUUn9LoIEmYaxooGvgJtZUwIDAQAB";
    /** 测试用的支付宝公钥 */
//    public static final String ALI_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAzdd6Zy8akYj51qmHrMhryd5pjTN6rUtWEVpRIR7RTc54oHyriR4KjNMSVgMx0m1GkNheXhsnQo9ZR4p7AJI+Vp5exy5Abb82hsCG1BFWXd4J8h9CQmEs4Aog9WiuEA3q9mputhFAjD46xjfKe8ix4Sizchwpb8ox0FbG6K7ioriUkyTzL9e4PmjjwMk5zVgqrzkRkIDcsY8Eo6r7EADJp07b+YKArIFItLHyyUbfm3NsmKv2bZG7GmCMesvp0pG5Wo1iJER9tSmj0MyJuug/2Z3tU6uPI1y2kepHQXEDvm9vo8kH77e/5IiCWScLpUlL7J/AyA5e71dqM5IBLyYnRwIDAQAB";

    // 查询支付交易状态的最大次数
    public static final int MAX_QUERY_RETRY = 15;
    // 多次循环查询支付交易状态时的请求间隔时间
    public static final long QUERY_DURATION = 5 * 1000;
    // 无法确认订单状态而撤销订单的最大次数. 超过该次数还是无法保障订单已经撤销, 就应该人工处理
    public static final int MAX_CANCEL_RETRY = 5;
    // 多次循环撤销订单的请求间隔时间
    public static final long CANCEL_DURATION = 5 * 1000;

    /**
     * 接口版本. 先固定为1.0
     */
    public static class ApiVersion {
        public static final String V1 = "1.0";
    }

    /**
     * 交易接口名称
     */
    public static class TradeName {
        /**
         * 支付
         */
        public static final String PAY = "alipay.trade.pay";
        /**
         * 查询支付状态
         */
        public static final String QUERY = "alipay.trade.query";
        /**
         * 退款
         */
        public static final String REFUND = "alipay.trade.refund";
        /**
         * 取消
         */
        public static final String CANCEL = "alipay.trade.cancel";
    }

    /**
     * 请求参数使用的编码格式
     */
    public static class Charset {
        public static final String UTF_8 = "UTF-8";
        public static final String GBK = "GBK";
        public static final String GB2312 = "GB2312";
        public static final String DEFAULT = UTF_8;
    }

    public static class BizContenFormat {
//        public static final String JSON = "JSON";
        public static final String JSON = "json";
    }

    /**
     * 签名的加密算法
     */
    public class SignType {
        public static final String RSA = "RSA";
        /**
         * 推荐使用
         */
        public static final String RSA2 = "RSA2";
    }

    /**
     * 接口返回的公共返回码(公共错误码)
     */
    public class ResultCode {
        /**
         * 调用接口成功
         */
        public static final String SUCCESS = "10000";
        /**
         * 调用接口成功, 正在处理业务(一般是等待用户输入支付密码)
         */
        public static final String PROCESSING = "10003";
        /**
         * 服务不可用
         */
        public static final String ALI_ERROR = "20000";
        /**
         * 授权权限不足
         */
        public static final String TOKEN_ERROR = "20001";
        /**
         * 缺少必选参数
         */
        public static final String MISS_PARAMS = "40001";
        /**
         * 无效的参数
         */
        public static final String INVALID_PATAMS = "40002";
        /**
         * 调用接口处理业务失败.
         * eg.
         */
        public static final String BIZ_ERROR = "40004";
        /**
         * 权限不足
         */
        public static final String PERMISSION_ERROR = "40006";
    }

    /**
     * 支付渠道
     */
    public class FundChannels {
        /**
         * 支付宝红包
         */
        public static final String COUPON = "COUPON";
        /**
         * 支付宝账户
         */
        public static final String ALIPAYACCOUNT = "ALIPAYACCOUNT";
        /**
         * 集分宝
         */
        public static final String POINT = "POINT";
        /**
         * 折扣券
         */
        public static final String DISCOUNT = "DISCOUNT";
        /**
         * 预付卡
         */
        public static final String PCARD = "PCARD";
        /**
         * 商家储值卡
         */
        public static final String MCARD = "MCARD";
        /**
         * 商户优惠券
         */
        public static final String MDISCOUNT = "MDISCOUNT";
        /**
         * 商户红包
         */
        public static final String MCOUPON = "MCOUPON";
        /**
         * 蚂蚁花呗
         */
        public static final String PCREDIT = "PCREDIT";
        /**
         * 银行卡
         */
        public static final String BANKCARD = "BANKCARD";
    }

    /**
     * 支付渠道为银行卡时，资金类型
     */
    public static class FundBankcardType {
        /**
         * 借记卡
         */
        public static final String DEBIT_CARD = "DEBIT_CARD";
        /**
         * 信用卡
         */
        public static final String CREDIT_CARD = "CREDIT_CARD";
        /**
         * 借贷合一卡
         */
        public static final String MIXED_CARD = "MIXED_CARD";
    }

    /**
     * 用来标记退款时, 是否发生了资金变动
     */
    public static class FundChange {
        public static final String YES = "Y";
        public static final String NO = "N";
    }

    /**
     * 调用取消支付接口后，返回的重试标记
     */
    public static class CancelRetryFlag {
        /**
         * 表示取消支付没有成功，需要重试
         */
        public static final String YES = "Y";
        /**
         * 表示取消支付成功，不再重试
         */
        public static final String NO = "N";
    }

    /**
     * 取消支付后, 触发的行为
     */
    public static class CancelTriggerAction {
        /**
         * 关闭交易, 没有退款产生.
         * 例如买家长时间不支付, 取消支付订单时会触发该行为
         */
        public static final String CLOSE = "close";
        /**
         * 退款.
         * 例如买家已经支付, 但是商户由于网络或其它原因无法确认已收到款项,
         * 取消支付订单时会触发退款
         */
        public static final String REFUND = "refund";
    }

    /**
     * 支付场景
     */
    public class SceneType {
        /**
         * 条码支付
         */
        public static final String BAR_CODE = "bar_code";
        /**
         * 声波支付
         */
        public static final String WAVE_CODE = "wave_code";
    }

    /**
     * 交易状态
     */
    public class TradeStatus {
        /**
         * 等待买家付款
         */
        public static final String WAITING_PAY = "WAIT_BUYER_PAY";
        /**
         * 交易关闭(交易超时，或已全额退款)
         */
        public static final String CLOSE = "TRADE_CLOSED";
        /**
         * 支付成功
         */
        public static final String SUCCESS = "TRADE_SUCCESS";
        /**
         * 交易结束(不可退款)
         */
        public static final String FINISHED = "TRADE_FINISHED";
        /**
         * 交易状态未知
         */
        public static final String UNKNOWN = "TRADE_UNKNOWN";
    }

    /**
     * 买家用户类型
     */
    public class BuyerUserType {
        /**
         * 个人用户
         */
        public static final String PRIVATE = "PRIVATE";
        /**
         * 企业用户
         */
        public static final String CORPORATE = "CORPORATE";
    }

    /**
     * 优惠券类型
     */
    public class VoucherType {
        /**
         * 全场代金券
         */
        public static final String FIX = "ALIPAY_FIX_VOUCHER";
        /**
         * 折扣券
         */
        public static final String DISCOUNT = "ALIPAY_DISCOUNT_VOUCHER";
        /**
         * 单品优惠
         */
        public static final String ITEM = "ALIPAY_ITEM_VOUCHER";
    }
}
