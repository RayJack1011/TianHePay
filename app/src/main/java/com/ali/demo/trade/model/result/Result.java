package com.ali.demo.trade.model.result;

public interface Result {

    /**
     * 判断交易是否在业务上成功.
     * @return true, 一定成功; false, 并不代表业务不成功！因为还有unknown的状态可能业务已经成功了
     */
    public boolean isTradeSuccess();
}
