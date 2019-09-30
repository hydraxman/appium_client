package com.yunniao.appiumtest.bean;

import java.util.ArrayList;

/**
 * Created by MrBu on 2016/1/13.
 */
public class TestCase {
    private String product;
    private String os;
    private String desc;
    private String name;
    private ArrayList<Operation> operations;
    private String version;
    private boolean isLaunch;
    private boolean isInstall;
    private String udid;
    private String browserName;

    public String getBrowserName() {
        return browserName;
    }

    public void setBrowserName(String browserName) {
        this.browserName = browserName;
    }

    public boolean getIsLaunch() {
        return isLaunch;
    }

    public void setIsLaunch(boolean launch) {
        isLaunch = launch;
    }

    public boolean getIsInstall() {
        return isInstall;
    }

    public void setIsInstall(boolean install) {
        isInstall = install;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Operation> getOperations() {
        return operations;
    }

    public void setOperations(ArrayList<Operation> operations) {
        this.operations = operations;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }


    public String getUdid() {
        return udid;
    }

    public void setUdid(String udid) {
        this.udid = udid;
    }
}
