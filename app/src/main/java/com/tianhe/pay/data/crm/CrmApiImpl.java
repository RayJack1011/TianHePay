package com.tianhe.pay.data.crm;

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

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

import javax.inject.Named;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.annotations.NonNull;

/**
 * 直连天和CRM接口的实现. 只能测试用, 生产环境时不能访问内网
 */
public class CrmApiImpl implements CrmApi {
    private Sign md5Sign;

    public CrmApiImpl(@Named("md5Sign") Sign md5Sign) {
        this.md5Sign = md5Sign;
    }

    @Override
    public Observable<Member> queryMember(final QueryMember request) {
        return Observable.create(new ObservableOnSubscribe<Member>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Member> e) throws Exception {
                String xml = executeRequest(request);
                CrmResponse<Member> response = CrmResponse.parseFromXml(xml, Member.class);
                e.onNext(response.getSingleData());
            }
        });
    }

    @Override
    public Observable<StoredValueCard> queryStoredValueCard(final QueryStoredValueCard request) {
        return Observable.create(new ObservableOnSubscribe<StoredValueCard>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<StoredValueCard> e) throws Exception {
                String xml = executeRequest(request);
                CrmResponse<StoredValueCard> response = CrmResponse.parseFromXml(xml, StoredValueCard.class);
                e.onNext(response.getSingleData());
            }
        });
    }

    @Override
    public Observable<Coupon> queryCoupon(final QueryCoupon request) {
        return Observable.create(new ObservableOnSubscribe<Coupon>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Coupon> e) throws Exception {
                String xml = executeRequest(request);
                CrmResponse<Coupon> response = CrmResponse.parseFromXml(xml, Coupon.class);
                e.onNext(response.getSingleData());
            }
        });
    }

    @Override
    public Observable<ProcessPaymentResult> submitPayment(final ProcessPayment request) {
        return Observable.create(new ObservableOnSubscribe<ProcessPaymentResult>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<ProcessPaymentResult> e) throws Exception {
                String xml = executeRequest(request);
                CrmResponse<ProcessPaymentResult> response = CrmResponse.parseFromXml(xml, ProcessPaymentResult.class);
                e.onNext(response.getSingleData());
            }
        });
    }

    @Override
    public Observable<ProcessGiftResult> submitGift(final ProcessGift request) {
        return Observable.create(new ObservableOnSubscribe<ProcessGiftResult>() {
            @Override
            public void subscribe(ObservableEmitter<ProcessGiftResult> e) throws Exception {
                String xml = executeRequest(request);
                CrmResponse<ProcessGiftResult> response = CrmResponse.parseFromXml(xml, ProcessGiftResult.class);
                e.onNext(response.getSingleData());
            }
        });
    }

    public String executeRequest(CrmRequest request) throws Exception {
        SoapObject soapObject = new SoapObject(CrmConstants.NAMESPACE, CrmConstants.ACTION);
        request.signed(md5Sign);
        soapObject.addProperty("in0", request.getRequestXml());
        SoapSerializationEnvelope envelope = createEnvelope(soapObject);
        HttpTransportSE transport = new HttpTransportSE(CrmConstants.URL, CrmConstants.TIEMOUT);
        transport.debug = true;
        try {
            transport.call(null, envelope);
        } catch (IOException e) {
            throw new Exception("调用会员服务失败");
        } catch (XmlPullParserException e) {
            throw new Exception("解析服务返回数据失败");
        }
        if (envelope.bodyIn instanceof SoapFault) {
            throw new Exception("CRM服务无法正常访问");
        }
        Object response = envelope.getResponse();
        if (response instanceof SoapPrimitive) {
            return response.toString();
        }
        return response.toString();
    }

    private static SoapSerializationEnvelope createEnvelope(SoapObject request) {
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER10);
        envelope.bodyOut = request;
        envelope.setOutputSoapObject(request);
        envelope.dotNet = false; // 不是dotNet开发的WebService
        return envelope;
    }
}
