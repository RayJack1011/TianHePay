package com.tianhe.pay.data;

import java.io.File;
import java.util.List;

import com.tianhe.pay.data.auth.Auth;
import com.tianhe.pay.data.auth.QueryAuth;
import com.tianhe.pay.data.order.OrderStatistics;
import com.tianhe.pay.data.order.history.OrderSimple;
import com.tianhe.pay.data.order.history.SyncCommand;
import com.tianhe.pay.data.order.submit.SubmitOrderResult;
import com.tianhe.pay.data.order.calculate.CalculationResult;
import com.tianhe.pay.data.login.LoginReq;
import com.tianhe.pay.data.app.AppUpgrade;
import com.tianhe.pay.data.login.LoginResp;
import com.tianhe.pay.data.order.lastSaleNo.GetLastSaleNoRequest;
import com.tianhe.pay.data.order.Order;
import com.tianhe.pay.ui.modify.ModifyReq;
import com.tianhe.pay.ui.modify.ModifyResp;

import io.reactivex.Observable;

public interface Repository {

    /**
     * 检查app的版本信息
     * @return 最新的APP信息
     */
    Observable<AppUpgrade> lastApp();

    /**
     * 下载新版apk
     * @param downloadUrl   下载地址
     * @param listener      下载进度的listener
     * @return 下载完成后的Apk
     */
    Observable<File> downloadApp(String downloadUrl, ProgressResponseBody.ProgressListener listener);

    /**
     * @param request
     * @return 用户登录成功后的基础信息
     */
    Observable<LoginResp> login(LoginReq request);

    /**
     * @param request
     * @return 服务端最新可用的单号
     */
    Observable<String> getLastSaleNo(GetLastSaleNoRequest request);

    /**
     * 计算订单(折扣、随单赠送、支付限制)
     * @param order
     * @return 订单调整后的信息
     */
    Observable<CalculationResult> calculateOrder(Order order);

    /**
     * 提交账单
     * @param order
     * @return
     */
    Observable<SubmitOrderResult> submitOrder(Order order);

    /**
     * 查询账单
     * @param saleNo 销售单号
     * @return
     */
    Observable<Order> queryHistoryOrder(String saleNo);

    /**
     * 根据日期统计交易数据
     * @return
     */
    Observable<OrderStatistics> countByDate(SyncCommand command);

    /**
     * 根据日期和mac地址查询终端设备的账单简略信息
     * @return
     */
    Observable<List<OrderSimple>> queryOrderSimplesByDate(SyncCommand command);

    Observable<Auth> queryAuth(QueryAuth queryAuth);
}
