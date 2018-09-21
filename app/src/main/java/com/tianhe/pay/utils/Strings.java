package com.tianhe.pay.utils;

import android.support.annotation.Nullable;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Pattern;

public class Strings {

    public static final Charset UTF_8 = Charset.forName("UTF-8");

    public static String createMD5Hash(String src) {
        return createHash(src, "MD5");
    }

    public static String createSHA1Hash(String src)
    {
        return createHash(src, "SHA-1");
    }

    private static String createHash(String src, String algorithm) {
        try {
            MessageDigest md = MessageDigest.getInstance(algorithm);
            md.update(getBytes(src));
            byte[] hashBytes = md.digest();
            StringBuilder sb = new StringBuilder();
            for (int i = 0, length = hashBytes.length; i< length; i++) {
                sb.append(Integer.toHexString(hashBytes[i] & 0xFF));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new AssertionError("Unable to construct MessageDigest for " + algorithm);
        }
    }

    public static byte[] getBytes(String str) {
        return str.getBytes(UTF_8);
    }

    public static String getString(byte[] bytes) {
        return new String(bytes, UTF_8);
    }

    public static boolean isEmpty(@Nullable CharSequence charSequence) {
        return (charSequence == null) || (charSequence.length() == 0);
    }

    public static boolean isNotEmpty(@Nullable CharSequence charSequence) {
        return !isEmpty(charSequence);
    }

    public static boolean isBlank(@Nullable CharSequence charSequence) {
        if (!isEmpty(charSequence)) {
            for (int i = 0, length = charSequence.length();
                    i < length; i++) {
                if (!Character.isWhitespace(charSequence.charAt(i))) {
                    return false;
                }
            }
        }
        return true;
    }

    public static String valueOrDefault(String value, String defaultValue) {
        if (isBlank(value)) {
            return defaultValue;
        }
        return value;
    }

    public static String nullToEmpty(String value) {
        if (value == null) {
            return "";
        }
        return value;
    }

    public static String ipAddressToString(int paramInt) {
        ByteBuffer buffer = ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN);
        buffer.putInt(paramInt);
        try {
            String hostAddress = InetAddress.getByAddress(buffer.array()).getHostAddress();
            return hostAddress;
        } catch (UnknownHostException e) {
//            Timber.d("Couldn't parse %s as an IP address", paramInt);
        }
        return null;
    }

    public static String replaceAll(CharSequence charSequence, Pattern pattern, String replacement) {
        return pattern.matcher(charSequence).replaceAll(replacement);
    }
}
