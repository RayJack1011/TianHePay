package com.tianhe.pay.data.crm;

import android.util.Log;

import com.tianhe.pay.CommomData;
import com.tianhe.pay.data.DataSource;
import com.tianhe.pay.data.Sign;
import com.tianhe.pay.data.crm.coupon.Coupon;
import com.tianhe.pay.data.crm.coupon.QueryCoupon;
import com.tianhe.pay.data.crm.gift.ProcessGift;
import com.tianhe.pay.data.crm.gift.ProcessGiftResult;
import com.tianhe.pay.data.crm.member.Member;
import com.tianhe.pay.data.crm.member.QueryMember;
import com.tianhe.pay.data.crm.payment.ProcessPayment;
import com.tianhe.pay.data.crm.payment.ProcessPaymentResult;
import com.tianhe.pay.data.crm.storedvaluecard.QueryStoredValueCard;
import com.tianhe.pay.data.crm.storedvaluecard.StoredValueCard;
import com.tianhe.pay.ui.setting.Settings;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import okhttp3.ResponseBody;

public class ForwardCrmApiImpl implements CrmApi {
    private Sign md5Sign;
    private DataSource dataSource;
    private String url;

    public ForwardCrmApiImpl(Sign md5Sign, DataSource dataSource, Settings settings) {
        this.md5Sign = md5Sign;
        this.dataSource = dataSource;
        String hostAndPort = settings.getServerUrl();
        if (hostAndPort == null) {
            hostAndPort = DataSource.HOST_AND_PORT;
        }
        this.url = DataSource.SCHEME + hostAndPort + DataSource.CRM;
    }

    @Override
    public Observable<Member> queryMember(QueryMember request) {
        return Observable.just(request).map(new Function<QueryMember, CrmRequest>() {
            @Override
            public CrmRequest apply(@NonNull QueryMember query) throws Exception {
                query.signed(md5Sign);
                return query;
            }
        }).flatMap(new Function<CrmRequest, ObservableSource<ResponseBody>>() {
            @Override
            public ObservableSource<ResponseBody> apply(@NonNull CrmRequest request) throws Exception {
                return dataSource.crmAdapter(url, request.getRequestXml());
            }
        }).map(new Function<ResponseBody, Member>() {
            @Override
            public Member apply(@NonNull ResponseBody body) throws Exception {
                CrmResponse<Member> response =
                        CrmResponse.parseFromXml(body.string(), Member.class);
                if (response.isSuccess()) {
                    return response.getSingleData();
                } else {
                    throw new CrmFailException(response.errorMessage());
                }
            }
        });
    }

    @Override
    public Observable<StoredValueCard> queryStoredValueCard(QueryStoredValueCard request) {
        return Observable.just(request).map(new Function<QueryStoredValueCard, CrmRequest>() {
            @Override
            public CrmRequest apply(@NonNull QueryStoredValueCard query) throws Exception {
                query.signed(md5Sign);
                return query;
            }
        }).flatMap(new Function<CrmRequest, ObservableSource<ResponseBody>>() {
            @Override
            public ObservableSource<ResponseBody> apply(@NonNull CrmRequest request) throws Exception {
                return dataSource.crmAdapter(url, request.getRequestXml());
            }
        }).map(new Function<ResponseBody, StoredValueCard>() {
            @Override
            public StoredValueCard apply(@NonNull ResponseBody body) throws Exception {
                CrmResponse<StoredValueCard> response =
                        CrmResponse.parseFromXml(body.string(), StoredValueCard.class);
                if (response.isSuccess()) {
                    return response.getSingleData();
                } else {
                    throw new CrmFailException(response.errorMessage());
                }
            }
        });
    }

    @Override
    public Observable<Coupon> queryCoupon(QueryCoupon request) {
        return Observable.just(request).map(new Function<QueryCoupon, CrmRequest>() {
            @Override
            public CrmRequest apply(@NonNull QueryCoupon query) throws Exception {
                query.signed(md5Sign);
                return query;
            }
        }).flatMap(new Function<CrmRequest, ObservableSource<ResponseBody>>() {
            @Override
            public ObservableSource<ResponseBody> apply(@NonNull CrmRequest request) throws Exception {
                Log.e("qqq",request.getRequestXml()+":::::::qinsiucanshu");
                return dataSource.crmAdapter(url, request.getRequestXml());
            }
        }).map(new Function<ResponseBody, Coupon>() {
            @Override
            public Coupon apply(@NonNull ResponseBody body) throws Exception {
                CrmResponse<Coupon> response =
                        CrmResponse.parseFromXml(body.string(), Coupon.class);
                if (response.isSuccess()) {
                    return response.getSingleData();
                } else {
                    throw new CrmFailException(response.errorMessage());
                }
            }
        });
    }

    @Override
    public Observable<ProcessPaymentResult> submitPayment(final ProcessPayment request) {
//        if(CommomData.isCouponReturn){
//            for(int i = 0;i < request.getCoupons().size();i ++){
//                request.getCoupons().get(i).setCouponState("04");
//            }
//        }
        return Observable.just(request).map(new Function<ProcessPayment, CrmRequest>() {
            @Override
            public CrmRequest apply(@NonNull ProcessPayment payment) throws Exception {
                payment.signed(md5Sign);
                return payment;
            }
        }).flatMap(new Function<CrmRequest, ObservableSource<ResponseBody>>() {
            @Override
            public ObservableSource<ResponseBody> apply(@NonNull CrmRequest request) throws Exception {
                Log.e("qqq","券回填入参-----》"+request.getRequestXml());
                return dataSource.crmAdapter(url, request.getRequestXml());
            }
        }).map(new Function<ResponseBody, ProcessPaymentResult>() {
            @Override
            public ProcessPaymentResult apply(@NonNull ResponseBody body) throws Exception {
                CrmResponse<ProcessPaymentResult> response =
                        CrmResponse.parseFromXml(body.string(), ProcessPaymentResult.class);
                if (response.isSuccess()) {
                    return response.getSingleData();
                } else {
                    throw new CrmFailException(response.errorMessage());
                }
            }
        });
    }

    @Override
    public Observable<ProcessGiftResult> submitGift(ProcessGift gift) {
        return Observable.just(gift).map(new Function<ProcessGift, CrmRequest>() {
            @Override
            public CrmRequest apply(@NonNull ProcessGift payment) throws Exception {
                payment.signed(md5Sign);
                return payment;
            }
        }).flatMap(new Function<CrmRequest, ObservableSource<ResponseBody>>() {
            @Override
            public ObservableSource<ResponseBody> apply(@NonNull CrmRequest request) throws Exception {
                Log.e("qqq",request.getRequestXml()+"::::requet");
                return dataSource.crmAdapter(url, request.getRequestXml());
            }
        }).map(new Function<ResponseBody, ProcessGiftResult>() {
            @Override
            public ProcessGiftResult apply(ResponseBody body) throws Exception {
                CrmResponse<ProcessGiftResult> response =
                        CrmResponse.parseFromXml(body.string(), ProcessGiftResult.class);
                if (response.isSuccess()) {
                    if(response.getSingleData().getGiftCoupons().size() > 0){
                        return response.getSingleData();
                    }else{
                        return  null;
//                        response.messageStr("暂无活动");
//                        throw new CrmFailException(response.errorMessage());
                    }
                } else {
                    throw new CrmFailException(response.errorMessage());
                }
            }
        });
    }
}
