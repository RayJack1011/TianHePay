package com.tianhe.pay.data.print;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.List;

public interface PrintInfo {

    public List<Line> getContents();

    public void addLine(String line);

    public void addLineBold(String line);

    public void addLine(Bitmap bitmap);


    public static class Line {
        boolean isBold = false;
        Object content;

        public Line(boolean isBold, String content) {
            this.isBold = isBold;
            this.content = content;
        }
        public Line(Bitmap bitmap) {
            this.content = bitmap;
        }
    }

    public static class Default implements PrintInfo {


        private List<Line> contents;

        public Default() {
            contents = new ArrayList<>();
        }

        @Override
        public List<Line> getContents() {
            return contents;
        }

        @Override
        public void addLine(String line) {
            contents.add(new Line(false, line));
        }

        @Override
        public void addLineBold(String line) {
            contents.add(new Line(true, line));
        }

        @Override
        public void addLine(Bitmap bitmap) {
            contents.add(new Line(bitmap));
        }

    }


}
