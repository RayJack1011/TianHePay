package com.tianhe.pay.data.crm.member;

import com.tianhe.pay.data.crm.CrmConstants;
import com.tianhe.pay.data.crm.CrmDataName;
import com.tianhe.pay.data.crm.CrmRequest;

public class QueryMember extends CrmRequest {
    @CrmDataName("ooef001")
    String shopNo;  // 门店编号
    @CrmDataName("mmaq001")
    String queryNo; // 查询号(会员卡号/会员手机号)

    public QueryMember(String shopNo) {
        super(CrmConstants.QUERY_MEMBER, shopNo);
        this.shopNo = shopNo;
    }

    public String getQueryNo() {
        return queryNo;
    }

    public void setQueryNo(String queryNo) {
        this.queryNo = queryNo;
    }
}
