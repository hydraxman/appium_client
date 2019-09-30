package com.yunniao.appiumtest.bean;

import java.util.ArrayList;

/**
 * Created by MrBu on 2016/1/13.
 */
public class Verify {
    private int type;
    private String value;
    private String text;
    private Element element;
    private int duration;
    private ArrayList<Var> vars;
    private boolean ignore = false;
    private String desc;
    private ArrayList<String> subText;


    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public boolean isIgnore() {
        return ignore;
    }

    public void setIgnore(boolean ignore) {
        this.ignore = ignore;
    }

    public ArrayList<Var> getVars() {
        return vars;
    }

    public void setVars(ArrayList<Var> vars) {
        this.vars = vars;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }


    public Element getElement() {
        return element;
    }

    public void setElement(Element element) {
        this.element = element;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
