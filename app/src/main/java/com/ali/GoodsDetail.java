package com.ali;

/**
 * 订单包含的商品详情信息.
 */
public class GoodsDetail {
    /**
     * 商品编号
     */
    private String goods_id;
    /**
     * 商品名称
     */
    private String goods_name;
    /**
     * 商品数量
     */
    private String quantity;
    /**
     * 商品单价(单位元)
     */
    private String price;
    /**
     * 商品分类
     */
    private String goods_category;
    /**
     * 商品的描述
     */
    private String body;
    /**
     * 详情关联的url
     */
    private String show_url;

    public String getGoods_id() {
        return goods_id;
    }

    public void setGoods_id(String goods_id) {
        this.goods_id = goods_id;
    }

    public String getGoods_name() {
        return goods_name;
    }

    public void setGoods_name(String goods_name) {
        this.goods_name = goods_name;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getGoods_category() {
        return goods_category;
    }

    public void setGoods_category(String goods_category) {
        this.goods_category = goods_category;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getShow_url() {
        return show_url;
    }

    public void setShow_url(String show_url) {
        this.show_url = show_url;
    }
}
