package com.ali.demo.api;

import java.util.Map;

public interface AlipayUploadRequest<T extends AlipayResponse> extends AlipayRequest<T> {

    /**
     * 获取所有的Key-Value形式的文件请求参数集合。其中：
     * <ul>
     * <li>Key: 请求参数名</li>
     * <li>Value: 请求参数文件元数据</li>
     * </ul>
     *
     * @return 文件请求参数集合
     */
    public Map<String, FileItem> getFileParams();

}
