package com.tianhe.pay.data.crm.member;

import com.tianhe.pay.data.crm.CrmDataName;

import java.io.Serializable;

public class Member implements Serializable {

    public static final Member NONE = new Member();

    @CrmDataName("mmaf001")
    private String memeberNo;       // 会员编号
    @CrmDataName("mmaf008")
    private String memeberName;     // 会员姓名
    @CrmDataName("mmaf014")
    private String memeberMoblieNo; // 会员手机
    @CrmDataName("mmafstus")
    private String isValid;         // 是否有效
    @CrmDataName("mmag004_2024")
    private String level;           // 会员等级
    @CrmDataName("mmag004_2025")
    private String type;            // 会员类型
    @CrmDataName("mmaq_t")
    private Card card;              // 会员卡

    public String getMemeberNo() {
        return memeberNo;
    }

    public void setMemeberNo(String memeberNo) {
        this.memeberNo = memeberNo;
    }

    public String getMemeberName() {
        return memeberName;
    }

    public void setMemeberName(String memeberName) {
        this.memeberName = memeberName;
    }

    public String getMemeberMoblieNo() {
        return memeberMoblieNo;
    }

    public void setMemeberMoblieNo(String memeberMoblieNo) {
        this.memeberMoblieNo = memeberMoblieNo;
    }

    public String getIsValid() {
        return isValid;
    }

    public void setIsValid(String isValid) {
        this.isValid = isValid;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getVipNo() {
        // 返回的是关联的卡号而不是会员编号
        if (card == null) {
            return null;
        }
        return card.cardNo;
    }

    public Card getCard() {
        return card;
    }

    public void setCard(Card card) {
        this.card = card;
    }

    public static class Card implements Serializable {
        @CrmDataName("mmaq001")
        public String cardNo;              // 卡号
        @CrmDataName("mmaq002")
        public String cardType;            // 卡种
        @CrmDataName("mmaq005")
        public String validDate;           // 有效(截止)日期
        @CrmDataName("mmaq006")
        public String cardState;           // 卡状态
        @CrmDataName("mmaq018")
        public String remainingPoint;      // 剩余积点数

        public Card() {
        }

        private Card(Builder builder) {
            cardNo = builder.cardNo;
            cardType = builder.cardType;
            validDate = builder.validDate;
            cardState = builder.cardState;
            remainingPoint = builder.remaining;
        }

        public static final class Builder {
            private String cardNo;
            private String cardType;
            private String validDate;
            private String cardState;
            private String remaining;

            public Builder() {
            }

            public Builder cardNo(String val) {
                cardNo = val;
                return this;
            }

            public Builder cardType(String val) {
                cardType = val;
                return this;
            }

            public Builder validDate(String val) {
                validDate = val;
                return this;
            }

            public Builder cardState(String val) {
                cardState = val;
                return this;
            }

            public Builder remaining(String val) {
                remaining = val;
                return this;
            }

            public Card build() {
                return new Card(this);
            }
        }
    }

}
