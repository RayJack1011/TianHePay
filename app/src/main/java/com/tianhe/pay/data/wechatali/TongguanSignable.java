package com.tianhe.pay.data.wechatali;

import com.tianhe.pay.data.Sign;
import com.tianhe.pay.data.Signable;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class TongguanSignable implements Signable {

    protected String key;

    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public String signed(Sign sign) {
        return sign.sign(prepareSignSource());
    }

    // 拼接需要加密的原文
    private String prepareSignSource() {
        Map<String, Object> map = getSignField();
        ArrayList<String> list = new ArrayList<String>();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            if (entry.getValue() != "") {
                list.add(entry.getKey() + "=" + entry.getValue() + "&");
            }
        }
        int size = list.size();
        String [] arrayToSort = list.toArray(new String[size]);
        Arrays.sort(arrayToSort, String.CASE_INSENSITIVE_ORDER);
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < size; i ++) {
            sb.append(arrayToSort[i]);
        }
        // add sign key
//        sb.append("key=").append(TongguanConstant.SIGN_KEY);
        sb.append("key=").append(this.key);
        return sb.toString();
    }

    // 获取需要加密的字段
    private Map<String, Object> getSignField() {
        Map<String, Object> map = new HashMap<String, Object>();
        List<Field> fields = new ArrayList<>();
        for (Class c = this.getClass(); (c != null && TongguanSignable.class.isAssignableFrom(c)); c = c.getSuperclass()) {
            Collections.addAll(fields, c.getDeclaredFields());
        }
//        Field[] fields = this.getClass().getFields();
        for (Field field : fields) {
            SkipSign skipSign = field.getAnnotation(SkipSign.class);
            if (skipSign != null) {
                continue;
            }
            field.setAccessible(true);
            Object obj;
            try {
                obj = field.get(this);
                if (obj != null) {
                    map.put(field.getName(), obj);
                }
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return map;
    }
}
