package com.tianhe.pay.utils.money;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

public class MoneyGsonAdapter extends TypeAdapter<Money> {

    @Override
    public void write(JsonWriter out, Money value) throws IOException {
        if (value == null) {
            out.value((String) null);
        } else {
            out.value(value.toString());
        }
    }

    @Override
    public Money read(JsonReader in) throws IOException {
        JsonToken token = in.peek();
        if (token == JsonToken.NULL) {
            in.nextNull();
//            return null;
            return Money.zeroMoney();
        }
        return Money.createAsYuan(in.nextString());
    }
}
