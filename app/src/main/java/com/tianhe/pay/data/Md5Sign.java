package com.tianhe.pay.data;

import java.security.MessageDigest;

public class Md5Sign implements Sign {

    public String sign(String source) {

        String resultString = null;
        try {
            resultString = new String(source);
            MessageDigest md = MessageDigest.getInstance("MD5");
            resultString = byte2hexString(md.digest(resultString.getBytes())).toUpperCase();
        } catch (Exception ex) {

        }
        return resultString;
    }

    private static final String byte2hexString(byte[] bytes) {
        StringBuffer bf = new StringBuffer(bytes.length * 2);
        for (int i = 0; i < bytes.length; i++) {
            if ((bytes[i] & 0xff) < 0x10) {
                bf.append("0");
            }
            bf.append(Long.toString(bytes[i] & 0xff, 16));
        }
        return bf.toString();
    }
}
