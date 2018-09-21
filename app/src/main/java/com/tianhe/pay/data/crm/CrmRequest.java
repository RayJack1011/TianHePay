package com.tianhe.pay.data.crm;

import com.tianhe.pay.data.Sign;
import com.tianhe.pay.data.Signable;
import com.tianhe.pay.utils.Times;

import java.lang.reflect.Field;
import java.util.List;
import java.util.UUID;

public abstract class CrmRequest implements Signable {

    // 参数原型(参数名称, 参数值)
    private static final String PRARM_PROTOTYPE = "<Field name=\"%1$s\" value=\"%2$s\"/>";

    // 头部信息原型, 同时用来加密(客户端版本号, 请求时间, 服务名称)
    private static final String HADER_PROTOTYPE = "<host prod=\"MOBILEPAY\" ver=\"%1$s\" ip=\"192.168.1.1\" " +
            "id=\"MOBILEPAY\" lang=\"zh_CN\" timezone=\"+8\" " +
            "acct=\"tiptop\" timestamp=\"%2$s\"/>" +
            "<service prod=\"T100\" name=\"%3$s\" srvver=\"1.0\" ip=\"172.169.10.76\" id=\"topprd\"/>";
    //toptst 测试     topprd  正式
    // 模式("0"代表正常模式; "1"代表训练模式); GUID的值为uuid
    private static final String MODE_PROTOTYPE = "<key name=\"Mode\">%1$s</key><key name=\"GUID\">%2$s</key>";

    // 完整的请求信息原型(参数：加密后的密串, 头部信息, 门店编号, 模式选择信息, 参数信息)
    private static final String REQUEST_PROTOTYPE = "<request type=\"sync\" key=\"%1$s\">%2$s" +
            "<datakey type=\"FOM\"><key name=\"EntId\">10</key><key name=\"CompanyId\">%3$s</key>%4$s" +
            "</datakey><payload><param key=\"data\" type=\"XML\">" +
            "<![CDATA[<Request><RequestContent>%5$s</RequestContent></Request>]]></param></payload></request>";

    private static final String DEFAULT_LOCAL_VERSION = "1.0.0";

    private String header;
    private String requestTime;     // 发起请求的时间
    private String signedString;    // 加密后的密串

    /**
     * 服务名称
     */
    protected String serverName;
    /**
     * 门店编号
     */
    protected String shopNo;

    public CrmRequest(String serverName, String shopNo) {
        this.serverName = serverName;
        this.shopNo = shopNo;
    }

    @Override
    public String signed(Sign sign) {
        signedString = sign.sign(prepareSignSource());
        return signedString;
    }

    public String getRequestXml() {
        if (signedString == null) {
            throw new IllegalStateException("CrmRequest must invoke signed(Sign sign) before getRequestXml()");
        }
        return String.format(REQUEST_PROTOTYPE, signedString, header, shopNo, getMode(), getRequestParams());
    }

    /**
     * 客户端版本号
     */
    protected String localVersion() {
        return DEFAULT_LOCAL_VERSION;
    }

    /**
     * 是否包含模式选择信息.
     * true, 请求用于: 积分处理(增加/扣减)/付款处理/赠送卡券;
     * false, 请求用于: 查询
     */
    protected boolean hasMode() {
        return true;
    }

    protected boolean isTraining() {
        return false;
    }

    /**
     * 是否提交处理, 造成crm数据变更(赠品、提交销售支付).
     */
    protected boolean isProcess() {
        return false;
    }

    private String getMode() {
        if (!hasMode()) {
            return "";
        }
        String mode;
        if (isTraining()) {
            mode = CrmConstants.MODE_TRAINING;
        } else {
            mode = CrmConstants.MODE_NORMAL;
        }
        String uuid = UUID.randomUUID().toString().replaceAll("-", "").toUpperCase();
        return String.format(MODE_PROTOTYPE, mode, uuid);
    }

    private String prepareSignSource() {
        if (header == null) {
            header = String.format(HADER_PROTOTYPE, localVersion(), getRequestTime(), serverName);
        }
        return header;
    }

    private String getRequestTime() {
        if (requestTime == null) {
            requestTime = Times.nowDateForCrm();
        }
        return requestTime;
    }

    private String getRequestParams() {
        StringBuilder sb = new StringBuilder();
        // 提交数据和查询数据的格式稍微有点区别
        if (isProcess()) {
            sb.append("<Parameter><Record></Record></Parameter>");
            sb.append("<Document><RecordSet id=\"1\">");
            sb.append("<Master name=\"rtif_t\">");
            sb.append(createParamsXml(this));
            sb.append("</Master></RecordSet></Document>");
        } else {
            sb.append("<Parameter>");
            sb.append(createParamsXml(this));
            sb.append("</Parameter>");
        }
        return sb.toString();
    }

    // 将请求参数拼接
    private static String createParamsXml(Object Param) {
        StringBuilder sb = new StringBuilder();
        Field[] fields = Param.getClass().getDeclaredFields();
        try {
            sb.append("<Record>"); // 参数开始
            for (Field field : fields) {
                field.setAccessible(true);
                CrmDataName dataName = field.getAnnotation(CrmDataName.class);
                if (dataName == null) {
                    continue;
                }
                String name = dataName.value();
                Object fieldValue = field.get(Param);
                if (fieldValue == null) {
                    continue;
                }
                Class type = field.getType();
                if (type == String.class || type.isPrimitive()) {
                    sb.append(String.format(PRARM_PROTOTYPE, name, fieldValue));
                } else {
                    // 详情参数开始
                    if (type.isAssignableFrom(List.class)) {
                        List detailList = (List) fieldValue;
                        if (detailList.size() > 0) {
                            sb.append(String.format("<Detail name=\"%s\">", name));
                            for (Object detail : detailList) {
                                sb.append(createParamsXml(detail));
                            }
                            sb.append("</Detail>");
                        }
                    } else {
                        sb.append(String.format("<Detail name=\"%s\">", name));
                        sb.append(createParamsXml(fieldValue));
                        sb.append("</Detail>");
                    }
                    // 详情参数结束
                }
            }
            // 参数结束
            sb.append("</Record>");
        } catch (IllegalAccessException e) {
            // ignore
        }
        return sb.toString();
    }

}
