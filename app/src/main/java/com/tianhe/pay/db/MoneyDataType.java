package com.tianhe.pay.db;

import com.j256.ormlite.field.FieldType;
import com.j256.ormlite.field.SqlType;
import com.j256.ormlite.field.types.BaseDataType;
import com.j256.ormlite.support.DatabaseResults;
import com.tianhe.pay.utils.money.Money;

import java.sql.SQLException;

class MoneyDataType extends BaseDataType {

    public MoneyDataType() {
        super(SqlType.STRING);
    }

    @Override
    public Object parseDefaultString(FieldType fieldType, String defaultStr) throws SQLException {
        return Money.createAsYuan(defaultStr);
    }

    @Override
    public Object resultToSqlArg(FieldType fieldType, DatabaseResults results, int columnPos) throws SQLException {
        String money = results.getString(columnPos);
        if (money == null) {
            return null;
        }
        return Money.createAsYuan(money);
    }

    @Override
    public Object javaToSqlArg(FieldType fieldType, Object javaObject) throws SQLException {
        if (javaObject == null) {
            return null;
        }
        Money money = (Money) javaObject;
        return money.toString();
    }

    @Override
    public boolean isEscapedValue() {
        return false;
    }

    @Override
    public boolean isAppropriateId() {
        return false;
    }

    @Override
    public Class<?> getPrimaryClass() {
        return Money.class;
    }
}
