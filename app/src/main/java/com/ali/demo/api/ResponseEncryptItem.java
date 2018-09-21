package com.ali.demo.api;

import java.io.Serializable;

public class ResponseEncryptItem implements Serializable {
    /**
     * 响应返回
     */
    private String respContent;

    /**
     * 处理完后的返回
     */
    private String realContent;

    /**
     * @param respContent
     * @param realContent
     */
    public ResponseEncryptItem(String respContent, String realContent) {
        super();
        this.respContent = respContent;
        this.realContent = realContent;
    }

    /**
     * Getter method for property <tt>respContent</tt>.
     *
     * @return property value of respContent
     */
    public String getRespContent() {
        return respContent;
    }

    /**
     * Getter method for property <tt>realContent</tt>.
     *
     * @return property value of realContent
     */
    public String getRealContent() {
        return realContent;
    }
}
