package com.ali.demo.api.internal.utils.json;

public interface JSONErrorListener {
    void start(String text);
    void error(String message, int column);
    void end();
}
