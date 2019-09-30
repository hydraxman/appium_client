package com.yunniao.appiumtest.bean;

public class LogItem {

    public String date;
    public String time;
    public String tag;
    public String pid;
    public String content;

    public LogItem(String line) {

    }

    @Override
    public String toString() {
        return date + "::" + time + "," + tag + "," + pid + "," + content;
    }
}
