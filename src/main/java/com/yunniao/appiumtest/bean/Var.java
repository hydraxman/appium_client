package com.yunniao.appiumtest.bean;

import com.alibaba.fastjson.JSONObject;

/**
 * Created by melinda on 2/22/16.
 */
public class Var {
    private String varName;
    private String subText;
    private Object varValue;

    public Object getVarValue() {
        return varValue;
    }

    public void setVarValue(Object varValue) {
        this.varValue = varValue;
    }

    public String getSubText() {
        return subText;
    }

    public void setSubText(String subText) {
        this.subText = subText;
    }

    public String getVarName() {
        return varName;
    }

    public void setVarName(String varName) {
        this.varName = varName;
    }
}
