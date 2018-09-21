package com.ali.utils;

import android.util.Base64;

import com.ali.AlipayApiException;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AlipayEncrypt {
    private static final String AES_ALG = "AES";
    private static final String AES_CBC_PCK_ALG = "AES/CBC/PKCS5Padding";
    private static final byte[] AES_IV = initIv(AES_CBC_PCK_ALG);
    public static final int BASE64_MODE = Base64.NO_WRAP;

    public static String encryptContent(String content, String encryptType, String encryptKey, String charset) throws AlipayApiException {
        if(AES_ALG.equals(encryptType)) {
            return aesEncrypt(content, encryptKey, charset);
        } else {
            throw new AlipayApiException("当前不支持该算法类型：encrypeType=" + encryptType);
        }
    }

    public static String decryptContent(String content, String encryptType, String encryptKey, String charset) throws AlipayApiException {
        if(AES_ALG.equals(encryptType)) {
            return aesDecrypt(content, encryptKey, charset);
        } else {
            throw new AlipayApiException("当前不支持该算法类型：encrypeType=" + encryptType);
        }
    }

    private static String aesEncrypt(String content, String aesKey, String charset) throws AlipayApiException {
        try {
            Cipher cipher = Cipher.getInstance(AES_CBC_PCK_ALG);
            IvParameterSpec iv = new IvParameterSpec(AES_IV);
            cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(Base64.decode(aesKey.getBytes(), BASE64_MODE), AES_ALG), iv);
            byte[] encryptBytes = cipher.doFinal(content.getBytes(charset));
            return new String(Base64.encode(encryptBytes, BASE64_MODE));
        } catch (Exception var6) {
            throw new AlipayApiException("AES加密失败：Aescontent = " + content + "; charset = " + charset, var6);
        }
    }

    private static String aesDecrypt(String content, String key, String charset) throws AlipayApiException {
        try {
            Cipher cipher = Cipher.getInstance(AES_CBC_PCK_ALG);
            IvParameterSpec iv = new IvParameterSpec(initIv(AES_CBC_PCK_ALG));
            cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(Base64.decode(key.getBytes(), BASE64_MODE), AES_ALG), iv);
            byte[] cleanBytes = cipher.doFinal(Base64.decode(content.getBytes(), BASE64_MODE));
            return new String(cleanBytes, charset);
        } catch (Exception var6) {
            throw new AlipayApiException("AES解密失败：Aescontent = " + content + "; charset = " + charset, var6);
        }
    }

    private static byte[] initIv(String fullAlg) {
        byte[] iv;
        int i;
        try {
            Cipher cipher = Cipher.getInstance(fullAlg);
            int blockSize = cipher.getBlockSize();
            iv = new byte[blockSize];

            for(i = 0; i < blockSize; ++i) {
                iv[i] = 0;
            }

            return iv;
        } catch (Exception var5) {
            int blockSize = 16;
            iv = new byte[blockSize];

            for(i = 0; i < blockSize; ++i) {
                iv[i] = 0;
            }

            return iv;
        }
    }
}
