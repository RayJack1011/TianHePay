package com.tianhe.pay.data.crm;

import android.util.Log;

import org.kxml2.io.KXmlParser;
import org.xmlpull.v1.XmlPullParser;

import java.io.ByteArrayInputStream;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.tianhe.pay.data.crm.CrmConstants.SUCCESS_SRVCODE;
import static com.tianhe.pay.data.crm.CrmConstants.SUCCESS_STATUS_CODE;

public class CrmResponse<DATA> {
    private String srvcode;
    private String statusCode;
    private String description;     // 错误信息

    private Class<DATA> dataClass;
    private List<Map<String, Object>> dataFields;

    public CrmResponse(Class<DATA> dataClass) {
        this.dataClass = dataClass;
    }

    public boolean isSuccess() {
        return SUCCESS_SRVCODE.equals(srvcode) && SUCCESS_STATUS_CODE.equals(statusCode);
    }

    public String errorMessage() {
        return description;
    }

    public void messageStr(String msg){
        description = msg;
//        return description;
    }

    public DATA getSingleData() {
        if (dataFields == null || dataFields.size() == 0) {
            return null;
        }
        return fillData(dataFields.get(0), dataClass);
    }

    public List<DATA> getDataList() {
        if (dataFields == null || dataFields.size() == 0) {
            return null;
        }
        List<DATA> dataList = new ArrayList<>(dataFields.size());
        for (Map<String, Object> fieldMap : dataFields) {
            dataList.add(fillData(fieldMap, dataClass));
        }
        return dataList;
    }

    private <T> T fillData(Map<String, Object> fieldMap, Class<T> clazz) {
        try {
            T data = clazz.newInstance();
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                CrmDataName dataName = field.getAnnotation(CrmDataName.class);
                if (dataName != null) {
                    String name = dataName.value();
                    Class type = field.getType();
                    if (type == String.class) {
                        field.set(data, fieldMap.get(name));
                    } else {
                        Object value = fieldMap.get(name);
                        if (value == null) {
                            continue;
                        }
                        boolean isList = type.isAssignableFrom(List.class);
                        if (isList) {
                            ParameterizedType parameterizedType = (ParameterizedType) field.getGenericType();
                            Type[] types = parameterizedType.getActualTypeArguments();
                            Class detailType = (Class) types[0];
                            List list = new ArrayList();
                            if (value instanceof List) {
                                List<Map<String, Object>> detailFields = (List<Map<String, Object>>) fieldMap.get(name);
                                for (Map<String, Object> map : detailFields) {
                                    Object detail = fillData(map, detailType);
                                    list.add(detail);
                                }
                            }
                            else {
                                Map<String, Object> detailFields = (Map<String, Object>) fieldMap.get(name);
                                Object detail = fillData(detailFields, detailType);
                                list.add(detail);
                            }
                            field.set(data, list);
                        } else {
                            if (value instanceof List) {
                                List<Map<String, Object>> detailFields = (List<Map<String, Object>>) fieldMap.get(name);
                                if (detailFields.size() > 0) {
                                    Object detail = fillData(detailFields.get(0), type);
                                    field.set(data, detail);
                                }
                            }
                            else {
                                Map<String, Object> detailFields = (Map<String, Object>) fieldMap.get(name);
                                Class detailType = field.getType();
                                Object detail = fillData(detailFields, detailType);
                                field.set(data, detail);
                            }
                        }
                    }
                }
            }
            return data;
        } catch (InstantiationException e) {
        } catch (IllegalAccessException e) {
        }
        return null;
    }

    public static <DATA> CrmResponse<DATA> parseFromXml(String xml, Class<DATA> dataClass) throws Exception {
        CrmResponse<DATA> response = new CrmResponse<>(dataClass);
//        XmlPullParser pullParser = Xml.newPullParser();
        XmlPullParser pullParser = new KXmlParser();
        Log.e("qqq",xml+"::::::::::::::::要解析的数据");
        pullParser.setInput(new ByteArrayInputStream(xml.getBytes("UTF-8")), "UTF-8");
        // 取得事件
        int event = pullParser.getEventType();
        while (event != XmlPullParser.END_DOCUMENT) {
            String nodeName = pullParser.getName();
            switch (event) {
                case XmlPullParser.START_TAG: // 标签开始
                    if (nodeName.equals("srvcode")) {
                        response.srvcode = pullParser.nextText();
                        break;
                    }
                    if (nodeName.equals("Status")) {
                        response.statusCode = pullParser.getAttributeValue(0);
                        response.description = pullParser.getAttributeValue(2);
                        break;
                    }
                    if (nodeName.equals("param")) {
                        String responseContent = pullParser.nextText();
                        pullParser.setInput(new ByteArrayInputStream(responseContent.getBytes()), "UTF-8");
                        break;
                    }
                    if (nodeName.equals("RecordSet")) {
//                    if (nodeName.equals("Record")) {
//                        int count = Integer.valueOf(pullParser.getAttributeValue(null, "id"));
//                        response.dataFields = new ArrayList<>(count);
//                        for (int index = 0; index < count; index++) {
//                            response.dataFields.add(parseSingleData(pullParser));
//                        }
                        response.dataFields = parseData(pullParser);
                        break;
                    }
                    break;
            }
            event = pullParser.next();
        }
        return response;
    }

    private static List<Map<String, Object>> parseData(XmlPullParser pullParser) throws Exception {
        List<Map<String, Object>> list = null;
        Map<String, Object> data = null;
        int count = 0;
        int event = pullParser.getEventType();
        String nodeName = pullParser.getName();
        while (!(event == XmlPullParser.END_DOCUMENT)) {
            switch (event) {
                case XmlPullParser.START_TAG:
                    if (nodeName.equals("Record")) {
                        if (count == 0) {
                            list = new ArrayList<>();
                        }
                        data = new HashMap<>();
                        break;
                    }
                    if (nodeName.equals("Field")) {
                        String name = pullParser.getAttributeValue(null, "name");
                        String value = pullParser.getAttributeValue(null, "value");
                        data.put(name, value);
                        break;
                    }
                    if (nodeName.equals("Detail")) {
                        String detailName = pullParser.getAttributeValue(null, "name");
                        pullParser.nextTag();
                        Object detail = parseData(pullParser);
                        data.put(detailName, detail);
                        break;
                    }
                    break;
                case XmlPullParser.END_TAG:
                    if (nodeName.equals("Record")) {
                        list.add(data);
                        count++;
                    } else if (nodeName.equals("Detail")
                            || nodeName.equals("Master")) {
                        return list;
                    }
                    break;
            }
            event = pullParser.nextTag();
            nodeName = pullParser.getName();
        }
        return list;
    }

}
