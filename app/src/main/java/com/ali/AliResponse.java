package com.ali;

/**
 * 支付宝接口返回公共数据. 需要从{@link AliResult#getResponseData()}解析JSON获得
 *
 * @see AliResult
 */
public class AliResponse {

    /**
     * 公共返回码
     * @see AliConstant.ResultCode
     */
    protected String code;
    /**
     * 公共返回码描述
     */
    protected String msg;
    /**
     * 业务返回码. 反映调用接口失败的具体原因
     */
    protected String sub_code;
    /**
     * 业务返回码描述.
     */
    protected String sub_msg;


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getSub_code() {
        return sub_code;
    }

    public void setSub_code(String sub_code) {
        this.sub_code = sub_code;
    }

    public String getSub_msg() {
        return sub_msg;
    }

    public void setSub_msg(String sub_msg) {
        this.sub_msg = sub_msg;
    }

    /**
     * 业务是否成功.
     * @return true, 一定成功; false, 不代表业务不成功, 撤销支付有可能业务处于unknown状态但实际上已经成功.
     */
    public boolean isTradeSuccess() {
        return AliConstant.ResultCode.SUCCESS.equals(code);
    }

    /**
     * 请求的业务是不是正在处理
     */
    public boolean isProcessing() {
        return AliConstant.ResultCode.PROCESSING.equals(code);
    }

    public boolean isAliError() {
        return false;
    }
}
