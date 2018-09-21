package com.ali.utils;

import com.ali.AliConstant;
import com.ali.AliRequest;
import com.ali.AlipayApiException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.crypto.Cipher;

public class AlipaySignature {
    private static final int MAX_ENCRYPT_BLOCK = 117;
    private static final int MAX_DECRYPT_BLOCK = 128;

    private static final String SHA256_WITH_RSA = "SHA256WithRSA";
    private static final String SHA1_WITH_RSA = "SHA1WithRSA";
    private static final String KEY_SIGN = "sign";
    private static final String KEY_SIGN_TYPE = "sign_type";
    private static final String KEY_CHARSET = "charset";
    private static final String KEY_BIZ_CONTENT = "biz_content";
//    private static final int BASE64_MODE = Base64.DEFAULT;


    public static String rsaSign(AliRequest aliRequest, String privateKey, String charset) throws
            AlipayApiException {
        String signContent = getSignContent(getSortedMap(aliRequest));
        return rsaSign(signContent, privateKey, charset);
    }

    public static String rsa256Sign(AliRequest aliRequest, String privateKey, String charset) throws
            AlipayApiException {
        String signContent = getSignContent(getSortedMap(aliRequest));
        return rsa256Sign(signContent, privateKey, charset);
    }

    /**
     * 根据请求获取参数Map
     *
     * @param request
     * @return
     */
    public static Map<String, String> getSortedMap(AliRequest request) {
        Map<String, String> sortedParams = new TreeMap<>();
        List<Field> fields = new ArrayList<>();
//        for (Class c = request.getClass(); c != null; c = c.getSuperclass()) {
            Collections.addAll(fields, AliRequest.class.getDeclaredFields());
//        }
        for (Field field : fields) {
            field.setAccessible(true);
            String value;
            try {
                value = (String) field.get(request);
                if (value != null) {
                    sortedParams.put(field.getName(), value);
                }
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return sortedParams;
    }

    /**
     * 根据排序后的参数, 拼接需要签名的参数字符串
     *
     * @param sortedParams
     * @return
     */
    public static String getSignContent(Map<String, String> sortedParams) {
        StringBuilder content = new StringBuilder();
        List<String> keys = new ArrayList<>(sortedParams.keySet());
        Collections.sort(keys);
        int index = 0;

        for (int i = 0; i < keys.size(); ++i) {
            String key = keys.get(i);
            String value = sortedParams.get(key);
            if (value != null && !"".equals(value)) {
                content.append((index == 0 ? "" : "&") + key + "=" + value);
                ++index;
            }
        }
        return content.toString();
    }

    /**
     * 对需要上传服务端的业务数据，根据私钥采用RSA2签名
     * @param content
     * @param privateKey
     * @param charset
     * @param signType
     * @return
     * @throws AlipayApiException
     */
    public static String rsaSign(String content, String privateKey, String charset, String signType) throws
            AlipayApiException {
        if (AliConstant.SignType.RSA.equals(signType)) {
            return rsaSign(content, privateKey, charset);
        } else if (AliConstant.SignType.RSA2.equals(signType)) {
            return rsa256Sign(content, privateKey, charset);
        } else {
            throw new AlipayApiException("Sign Type is Not Support : signType=" + signType);
        }
    }

    /**
     * 对需要上传服务端的业务数据，根据私钥采用{@link AliConstant.SignType#RSA2 }签名
     *
     * @param content    业务数据字符串
     * @param privateKey 私钥
     * @param charset    需求签名的字符串的字符集格式
     * @return 签名后的字符串
     * @throws AlipayApiException
     */
    public static String rsa256Sign(String content, String privateKey, String charset) throws AlipayApiException {
        try {
            PrivateKey priKey = getPrivateKeyFromPKCS8(AliConstant.SignType.RSA,
                    new ByteArrayInputStream(privateKey.getBytes(charset)));
            Signature signature = Signature.getInstance(SHA256_WITH_RSA);
            signature.initSign(priKey);
            if (StringUtils.isEmpty(charset)) {
                signature.update(content.getBytes());
            } else {
                signature.update(content.getBytes(charset));
            }
            byte[] signed = signature.sign();
            return new String(Base64.encodeBase64(signed));
        } catch (InvalidKeySpecException e) {
            throw new AlipayApiException("RSA私钥格式不正确，请检查是否正确配置了PKCS8格式的私钥", e);
        } catch (Exception e) {
            throw new AlipayApiException("RSAcontent = " + content + "; charset = " + charset, e);
        }
    }

//    public static void main(String[] args) {
//        String content = "app_id=2088821495046450&biz_content={\"auth_code\":\"283165763041156611\"," +
//                "\"body\":\"0061802271129170009\",\"operator_id\":\"9998\",\"out_trade_no\":\"0061802271129170009\"," +
//                "\"terminal_id\":\"006\",\"total_amount\":\"0.01\"}&charset=UTF-8&format=JSON&method=alipay.trade" +
//                ".pay&timestamp=2018-02-27 11:30:55&version=1.0";
//        String privateKey = AliConstant.PRIVATE_KEY;
//        String charset = "UTF-8";
//        try {
//            String signed = rsa256Sign(content,privateKey,charset);
//            System.out.println(signed);
//        } catch (AlipayApiException e) {
//            e.printStackTrace();
//        }
//    }

    /**
     * 根据私钥采用RSA签名
     *
     * @param content    需要签名的字符串
     * @param privateKey 私钥
     * @param charset    需求签名的字符串的字符集格式
     * @return 签名后的字符串
     * @throws AlipayApiException
     */
    public static String rsaSign(String content, String privateKey, String charset) throws AlipayApiException {
        try {
            PrivateKey priKey = getPrivateKeyFromPKCS8(AliConstant.SignType.RSA,
                    new ByteArrayInputStream(privateKey.getBytes()));
            Signature signature = Signature.getInstance(SHA1_WITH_RSA);
            signature.initSign(priKey);
            if (StringUtils.isEmpty(charset)) {
                signature.update(content.getBytes());
            } else {
                signature.update(content.getBytes(charset));
            }
            byte[] signed = signature.sign();
            return new String(Base64.encodeBase64(signed));
        } catch (InvalidKeySpecException e) {
            throw new AlipayApiException("RSA私钥格式不正确，请检查是否正确配置了PKCS8格式的私钥", e);
        } catch (Exception e) {
            throw new AlipayApiException("RSAcontent = " + content + "; charset = " + charset, e);
        }
    }

    private static PrivateKey getPrivateKeyFromPKCS8(String algorithm, InputStream ins) throws Exception {
        if (ins == null || algorithm == null || "".equals(algorithm)) {
            return null;
        }
        KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
        byte[] encodedKey = IOUtils.readStream(ins).getBytes();
        encodedKey = Base64.decodeBase64(encodedKey);
        KeySpec keySpec = new PKCS8EncodedKeySpec(encodedKey);
        return keyFactory.generatePrivate(keySpec);
    }

    /**
     * 根据给定的集合, 拼接成需要验签的参数字符串.
     * 拼接验签字符串前，(如果集合中包含签名)会先移除签名
     *
     * @param params
     * @return
     */
    private static String getSignCheckContent(Map<String, String> params) {
        if (params == null) {
            return null;
        } else {
            params.remove(KEY_SIGN);
            params.remove(KEY_SIGN_TYPE);
            StringBuilder content = new StringBuilder();
            List<String> keys = new ArrayList<>(params.keySet());
            Collections.sort(keys);
            for (int i = 0; i < keys.size(); ++i) {
                String key = keys.get(i);
                String value = params.get(key);
                content.append((i == 0 ? "" : "&") + key + "=" + value);
            }
            return content.toString();
        }
    }

    /**
     * 验证签名. 默认采用{@link AliConstant.SignType#RSA }进行验签.
     * 先将给定的Map中键值对按key的降序拼接成验证原字符串
     *
     * @param params    需要验签的集合
     * @param publicKey 公钥
     * @param charset   签名字符串使用的字符集
     * @return
     * @throws AlipayApiException
     */
    public static boolean rsaCheck(Map<String, String> params, String publicKey, String charset) throws
            AlipayApiException {
        String sign = params.get(KEY_SIGN);
        String content = getSignCheckContent(params);
        return rsaCheck(content, sign, publicKey, charset, AliConstant.SignType.RSA);
    }

    /**
     * 根据指定的签名方式，进行验证签名.
     * 先将给定的Map中键值对按key的降序拼接成验证原字符串
     *
     * @param params    需要验签的集合
     * @param publicKey 公钥
     * @param charset   签名字符串使用的字符集
     * @param signType  签名方式
     * @return
     * @throws AlipayApiException
     * @see AliConstant.SignType
     * @see AliConstant.Charset
     */
    public static boolean rsaCheck(Map<String, String> params, String publicKey, String charset, String signType)
            throws AlipayApiException {
        String sign = params.get(KEY_SIGN);
        String content = getSignCheckContent(params);
        return rsaCheck(content, sign, publicKey, charset, signType);
    }

    /**
     * 根据指定的签名方式, 进行验证签名
     * @param content   需要验证签名的原字符串
     * @param sign      签名后的字符串
     * @param publicKey 公钥
     * @param charset   签名字符串使用的字符集
     * @param signType  签名方式
     * @return
     * @throws AlipayApiException
     * @see AliConstant.SignType
     * @see AliConstant.Charset
     */
    public static boolean rsaCheck(String content, String sign, String publicKey, String charset, String signType)
            throws AlipayApiException {
        if (AliConstant.SignType.RSA.equals(signType)) {
            return rsaCheckContent(content, sign, publicKey, charset);
        } else if (AliConstant.SignType.RSA2.equals(signType)) {
            return rsa256CheckContent(content, sign, publicKey, charset);
        } else {
            throw new AlipayApiException("Sign Type is Not Support : signType=" + signType);
        }
    }

    public static boolean rsa256CheckContent(String content, String sign, String publicKey, String charset) throws
            AlipayApiException {
        try {
            PublicKey pubKey = getPublicKeyFromX509(AliConstant.SignType.RSA,
                    new ByteArrayInputStream(publicKey.getBytes()));
            Signature signature = Signature.getInstance(SHA256_WITH_RSA);
            signature.initVerify(pubKey);
            if (charset == null || "".equals(charset)) {
                signature.update(content.getBytes());
            } else {
                signature.update(content.getBytes(charset));
            }

            return signature.verify(Base64.decodeBase64(sign.getBytes()));
        } catch (Exception var6) {
            throw new AlipayApiException("RSAcontent = " + content + ",sign=" + sign + ",charset = " + charset, var6);
        }
    }

    public static boolean rsaCheckContent(String content, String sign, String publicKey, String charset) throws
            AlipayApiException {
        try {
            PublicKey pubKey = getPublicKeyFromX509(AliConstant.SignType.RSA,
                    new ByteArrayInputStream(publicKey.getBytes()));
            Signature signature = Signature.getInstance(SHA1_WITH_RSA);
            signature.initVerify(pubKey);
            if (StringUtils.isEmpty(charset)) {
                signature.update(content.getBytes());
            } else {
                signature.update(content.getBytes(charset));
            }
            return signature.verify(Base64.decodeBase64(sign.getBytes()));
        } catch (Exception var6) {
            throw new AlipayApiException("RSAcontent = " + content + ",sign=" + sign + ",charset = " + charset, var6);
        }
    }

    private static PublicKey getPublicKeyFromX509(String algorithm, InputStream ins) throws Exception {
        KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
        byte[] encodedKey = IOUtils.readStream(ins).getBytes();
        encodedKey = Base64.decodeBase64(encodedKey);
        return keyFactory.generatePublic(new X509EncodedKeySpec(encodedKey));
    }


    /**
     * 验签并解析还原业务数据.
     * @param params            返回的结果
     * @param alipayPublicKey   支付宝公钥
     * @param cusPrivateKey     商户私钥
     * @param isCheckSign       是否需要验证签名
     * @param isDecrypt         业务数据是否需要解密
     * @return
     * @throws AlipayApiException
     */
    public static String checkSignAndDecrypt(Map<String, String> params,
                                             String alipayPublicKey, String cusPrivateKey,
                                             boolean isCheckSign, boolean isDecrypt) throws AlipayApiException {
        String charset = params.get(KEY_CHARSET);
        String bizContent = params.get(KEY_BIZ_CONTENT);
        if (isCheckSign && !rsaCheck(params, alipayPublicKey, charset)) {
            throw new AlipayApiException("rsaCheck failure:rsaParams=" + params);
        } else {
            return isDecrypt ? rsaDecrypt(bizContent, cusPrivateKey, charset) : bizContent;
        }
    }

    /**
     * 验签，并解密还原业务数据
     *
     * @param params          返回的结果
     * @param alipayPublicKey 支付宝公钥
     * @param cusPrivateKey   商户私钥
     * @param isCheckSign     是否需要验证签名
     * @param isDecrypt       业务数据已经经过RSA加密
     * @param signType        签名的类型
     * @return
     * @throws AlipayApiException
     */
    public static String checkSignAndDecrypt(Map<String, String> params,
                                             String alipayPublicKey, String cusPrivateKey,
                                             boolean isCheckSign, boolean isDecrypt, String signType) throws
            AlipayApiException {
        String charset = params.get(KEY_CHARSET);
        String bizContent = params.get(KEY_BIZ_CONTENT);
        if (isCheckSign && !rsaCheck(params, alipayPublicKey, charset, signType)) {
            throw new AlipayApiException("rsaCheck failure:rsaParams=" + params);
        } else {
            return isDecrypt ? rsaDecrypt(bizContent, cusPrivateKey, charset) : bizContent;
        }
    }

    /**
     * 将业务数据用RSA加密
     *
     * @param content   需要加密的业务数据串
     * @param publicKey 支付宝公钥
     * @param charset   业务数据字符集
     * @return
     * @throws AlipayApiException
     */
    public static String rsaEncrypt(String content, String publicKey, String charset) throws AlipayApiException {
        try {
            PublicKey pubKey = getPublicKeyFromX509(AliConstant.SignType.RSA, new ByteArrayInputStream(publicKey
                    .getBytes()));
            Cipher cipher = Cipher.getInstance(AliConstant.SignType.RSA);
            cipher.init(Cipher.ENCRYPT_MODE, pubKey);
            byte[] data = StringUtils.isEmpty(charset) ? content.getBytes() : content.getBytes(charset);
            int inputLen = data.length;
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            int offSet = 0;

            for (int i = 0; inputLen - offSet > 0; offSet = i * MAX_ENCRYPT_BLOCK) {
                byte[] cache;
                if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {
                    cache = cipher.doFinal(data, offSet, MAX_ENCRYPT_BLOCK);
                } else {
                    cache = cipher.doFinal(data, offSet, inputLen - offSet);
                }

                out.write(cache, 0, cache.length);
                ++i;
            }
            byte[] encryptedData = Base64.encodeBase64(out.toByteArray());
            out.close();
            return StringUtils.isEmpty(charset) ? new String(encryptedData) : new String(encryptedData, charset);
        } catch (Exception var12) {
            throw new AlipayApiException("EncryptContent = " + content + ",charset = " + charset, var12);
        }
    }

    /**
     * 将业务数据进行RAS解密
     * @param content       需要解密的业务数据
     * @param privateKey    商户私钥
     * @param charset       业务数据字符集
     * @return
     * @throws AlipayApiException
     */
    public static String rsaDecrypt(String content, String privateKey, String charset) throws AlipayApiException {
        try {
            PrivateKey priKey = getPrivateKeyFromPKCS8(AliConstant.SignType.RSA, new ByteArrayInputStream(privateKey
                    .getBytes()));
            Cipher cipher = Cipher.getInstance(AliConstant.SignType.RSA);
            cipher.init(Cipher.DECRYPT_MODE, priKey);
            byte[] data = StringUtils.isEmpty(charset) ? content.getBytes() : content.getBytes(charset);
            byte[] encryptedData = Base64.decodeBase64(data);
            int inputLen = encryptedData.length;
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            int offSet = 0;
            for (int i = 0; inputLen - offSet > 0; offSet = i * MAX_DECRYPT_BLOCK) {
                byte[] cache;
                if (inputLen - offSet > MAX_DECRYPT_BLOCK) {
                    cache = cipher.doFinal(encryptedData, offSet, MAX_DECRYPT_BLOCK);
                } else {
                    cache = cipher.doFinal(encryptedData, offSet, inputLen - offSet);
                }
                out.write(cache, 0, cache.length);
                ++i;
            }
            byte[] decryptedData = out.toByteArray();
            out.close();
            return StringUtils.isEmpty(charset) ? new String(decryptedData) : new String(decryptedData, charset);
        } catch (Exception var12) {
            throw new AlipayApiException("EncodeContent = " + content + ",charset = " + charset, var12);
        }
    }
}
