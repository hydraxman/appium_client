package com.yunniao.appiumtest.bean;
import com.alibaba.fastjson.JSONObject;
import com.yunniao.appiumtest.Constants;

import java.util.Map;

/**
 * Created by melinda on 3/11/16.
 */
public class ApiParam {
    private String desc;
    private String url;
    private String method;
    private JSONObject params;
    private JSONObject vars;
    private JSONObject results;
    private boolean ignore;
    private int port = Constants.SAN_TI_PORT;

    public boolean isIgnore() {
        return ignore;
    }

    public void setIgnore(boolean ignore) {
        this.ignore = ignore;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public JSONObject getResults() {
        return results;
    }

    public void setResults(JSONObject results) {
        this.results = results;
    }

    public JSONObject getVars() {
        return vars;
    }

    public void setVars(JSONObject vars) {
        this.vars = vars;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public JSONObject getParams() {
        return params;
    }

    public void setParams(JSONObject params) {
        this.params = params;
    }
}
