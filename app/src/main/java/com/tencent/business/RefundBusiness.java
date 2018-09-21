package com.tencent.business;

import android.support.annotation.NonNull;

import com.tencent.common.Configure;
import com.tencent.common.Log;
import com.tencent.common.Signature;
import com.tencent.common.Util;
import com.tencent.common.report.ReporterFactory;
import com.tencent.common.report.protocol.ReportReqData;
import com.tencent.common.report.service.ReportService;
import com.tencent.protocol.refund_protocol.RefundReqData;
import com.tencent.protocol.refund_protocol.RefundResData;
import com.tencent.service.RefundService;

public class RefundBusiness {

    public interface ResultListener {
        //API返回ReturnCode不合法，支付请求逻辑错误，请仔细检测传过去的每一个参数是否合法，或是看API能否被正常访问
        void onFailByReturnCodeError(RefundResData refundResData);

        //API返回ReturnCode为FAIL，支付API系统返回失败，请检测Post给API的数据是否规范合法
        void onFailByReturnCodeFail(RefundResData refundResData);

        //支付请求API返回的数据签名验证失败，有可能数据被篡改了
        void onFailBySignInvalid(RefundResData refundResData);

        //退款失败
        void onRefundFail(RefundResData refundResData);

        //退款成功
        void onRefundSuccess(RefundResData refundResData);

    }

    private RefundService refundService;

    public RefundBusiness() throws IllegalAccessException, ClassNotFoundException, InstantiationException {
        refundService = new RefundService();
    }

    /**
     * 调用退款业务逻辑
     *
     * @param refundReqData  这个数据对象里面包含了API要求提交的各种数据字段
     * @param resultListener 业务逻辑可能走到的结果分支，需要商户处理
     * @throws Exception
     */
    public void run(RefundReqData refundReqData, ResultListener resultListener) throws Exception {

        //--------------------------------------------------------------------
        //构造请求“退款API”所需要提交的数据
        //--------------------------------------------------------------------

        //API返回的数据
        String refundServiceResponseString;

        long costTimeStart = System.currentTimeMillis();


        // 退款查询API返回的数据
        refundServiceResponseString = refundService.request(refundReqData);

        long costTimeEnd = System.currentTimeMillis();
        long totalTimeCost = costTimeEnd - costTimeStart;

        //将从API返回的XML数据映射到Java对象
        RefundResData refundResData = (RefundResData) Util.getObjectFromXML(refundServiceResponseString,
                RefundResData.class);
        ReportReqData reportReqData = createRefundResponseReport(totalTimeCost, refundResData);

        report(costTimeStart, reportReqData);

        if (refundResData == null || refundResData.getReturn_code() == null) {
            // "Case1:退款API请求逻辑错误，请仔细检测传过去的每一个参数是否合法，或是看API能否被正常访问"
            resultListener.onFailByReturnCodeError(refundResData);
            return;
        }

        if (refundResData.getReturn_code().equals("FAIL")) {
            // Case2：一般这里返回FAIL是出现系统级参数错误，请检测Post给API的数据是否规范合法
            resultListener.onFailByReturnCodeFail(refundResData);
        } else {
            // 先验证一下返回数据有没有被第三方篡改
            if (!Signature.checkIsSignValidFromResponseString(refundServiceResponseString)) {
                // "Case3:退款请求API返回的数据签名验证失败，有可能数据被篡改了"
                resultListener.onFailBySignInvalid(refundResData);
                return;
            }
            if (refundResData.getResult_code().equals("FAIL")) {
                // "Case4:【退款失败】"
                // 退款失败时再怎么延时查询退款状态都没有意义，这个时间建议要么再手动重试一次，依然失败的话请走投诉渠道进行投诉
                resultListener.onRefundFail(refundResData);
            } else {
                //退款成功
                resultListener.onRefundSuccess(refundResData);
            }
        }
    }

    @NonNull
    private ReportReqData createRefundResponseReport(long totalTimeCost, RefundResData response) {

        ReportReqData.Builder builder = new ReportReqData.Builder();
        builder.appid(Configure.getAppid())
                .mch_id(Configure.getMchid())
                .sub_mch_id(Configure.getSubMchid())
                .device_info(response.getDevice_info())
                .interface_url(Configure.REFUND_API)
                .execute_time_cost((int) totalTimeCost)
                .result_code(response.getReturn_code())
                .return_msg(response.getReturn_msg())
                .err_code(response.getErr_code())
                .err_code_des(response.getErr_code_des())
                .out_trade_no(response.getOut_trade_no())
                .user_ip(Configure.getIP());
        return builder.build();
    }

    private void report(long costTimeStart, ReportReqData reportReqData) throws Exception {
        long timeAfterReport;
        if (Configure.isUseThreadToDoReport()) {
            ReporterFactory.getReporter(reportReqData).run();
            timeAfterReport = System.currentTimeMillis();
            Util.log("pay+report总耗时（异步方式上报）：" + (timeAfterReport - costTimeStart) + "ms");
        } else {
            ReportService.request(reportReqData);
            timeAfterReport = System.currentTimeMillis();
            Util.log("pay+report总耗时（同步方式上报）：" + (timeAfterReport - costTimeStart) + "ms");
        }
    }

    public void setRefundService(RefundService service) {
        refundService = service;
    }

}
