package com.tencent.common;

import com.tencent.service.IServiceRequest;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.io.xml.XmlFriendlyNameCoder;

import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;

public class HttpsRequest implements IServiceRequest {

    public interface ResultListener {

        void onConnectionPoolTimeoutError();

    }

    //表示请求器是否已经做了初始化工作
    private boolean hasInit = false;

    //连接超时时间，默认10秒
    private int socketTimeout = 10000;

    //传输超时时间，默认30秒
    private int connectTimeout = 30000;

    private SSLContext sslContext;
/*
    //请求器的配置
    private RequestConfig requestConfig;

    //HTTP请求器
    private CloseableHttpClient httpClient;
*/
    public HttpsRequest() throws UnrecoverableKeyException, KeyManagementException, NoSuchAlgorithmException, KeyStoreException, IOException {
        init();
    }

    private void init() throws IOException, KeyStoreException, UnrecoverableKeyException, NoSuchAlgorithmException, KeyManagementException {

        KeyStore keyStore = KeyStore.getInstance("PKCS12");
        InputStream instream = null;
        if(!Configure.getCertLocalPath().equals("")) {
            instream = new FileInputStream(new File(Configure.getCertLocalPath()));
        } else {
//            instream = AppApplication.getContext().getResources().openRawResource(R.raw.apiclient_cert);
        }

        try {
            keyStore.load(instream, Configure.getCertPassword().toCharArray());//设置证书密码
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } finally {
            instream.close();
        }

        KeyManagerFactory kmfactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        kmfactory.init(keyStore, Configure.getCertPassword().toCharArray());
        sslContext = SSLContext.getInstance("TLSv1");
        sslContext.init(kmfactory.getKeyManagers(), null, null);

        hasInit = true;
    }


    /**
     * 通过Https往API post xml数据
     *
     * @param url    API地址
     * @param xmlObj 要提交的XML数据对象
     * @return API回包的实际数据
     * @throws IOException
     * @throws KeyStoreException
     * @throws UnrecoverableKeyException
     * @throws NoSuchAlgorithmException
     * @throws KeyManagementException
     */

    public String sendPost(String url, Object xmlObj) throws IOException, KeyStoreException, UnrecoverableKeyException, NoSuchAlgorithmException, KeyManagementException {
        if (!hasInit) {
            init();
        }
        //解决XStream对出现双下划线的bug
        XStream xStreamForRequestPostData = new XStream(new DomDriver("UTF-8", new XmlFriendlyNameCoder("-_", "_")));
        //将要提交给API的数据对象转换成XML格式数据Post给API
        String postDataXML = xStreamForRequestPostData.toXML(xmlObj);
        RequestParams params = new RequestParams(url);
        params.setSslSocketFactory(sslContext.getSocketFactory());
        params.setConnectTimeout(connectTimeout);
        params.setMaxRetryCount(0);
        params.setCharset("UTF-8");
        params.setBodyContent(postDataXML);
        try {
            String result = x.http().postSync(params, String.class);
            return result;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return null;
    }


}
