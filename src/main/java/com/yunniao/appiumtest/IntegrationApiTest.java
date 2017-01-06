package com.yunniao.appiumtest;

import com.yunniao.appiumtest.bean.Action;
import com.yunniao.appiumtest.bean.Element;
import com.yunniao.appiumtest.bean.TestCase;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.ios.IOSElement;

import java.io.IOException;

/**
 * Created by melinda on 1/11/16.
 */
public class IntegrationApiTest extends IntegrationTest<IOSElement,IOSDriver<IOSElement>> {

    public IntegrationApiTest(TestCase testCase, String urlAddrLocal, String udid, Integer isQuit) {
        super(testCase, urlAddrLocal, udid, isQuit);
    }

    @Override
    public void stopDriver(boolean stopAll) throws IOException {
        super.stopDriver(stopAll);
    }

    public void setup(boolean isInstall, boolean isLaunch, String pr, String os, String udid, String browserName) throws Exception {}

    public  void elementSearchWait(Element element, int duration) throws Exception{}

    public void setLogin(String url) throws Exception{}
    public void setLogOut(String url) throws Exception{}
    public void takeScreenShot(Integer type) throws Exception {}


    protected void setAction(Element element, Action action) throws Exception{}


}
