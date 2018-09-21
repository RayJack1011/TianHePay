package com.ali.demo.api.internal.utils.json;

public class StdoutStreamErrorListener extends BufferErrorListener {
    public void end() {
        System.out.print(buffer.toString());
    }
}

