package com.yunniao.appiumtest;


import com.yunniao.appiumtest.bean.Action;
import com.yunniao.appiumtest.bean.Element;
import com.yunniao.appiumtest.bean.TestCase;
import com.yunniao.appiumtest.utils.LogUtil;
import com.yunniao.appiumtest.utils.PhotoUtil;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import io.appium.java_client.remote.MobileCapabilityType;
import org.openqa.selenium.By;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.File;
import java.io.IOException;
import java.net.URL;


/**
 * Created by melinda on 1/11/16.
 */
public class IntegrationAndroidTest extends IntegrationTest<AndroidElement, AndroidDriver<AndroidElement>> {


    public IntegrationAndroidTest(TestCase testCase, String urlAddrLocal, String udid, Integer isQuit) {
        super(testCase, urlAddrLocal, udid, isQuit);
        this.testCase.setUdid(udid);
    }

    @Override
    public void stopDriver(boolean stopAll) throws IOException {
        if (driver != null) {
            if(isQuit != 2) {
                driver.quit();
                LogUtil.i("quit app");
            } else {
                driver.closeApp();
                LogUtil.i("close app");
            }
        }
        super.stopDriver(stopAll);
    }

    @Override
    public void onOneOperationEnd() throws Exception {
        if (browserName == null) {
            try {
                LogUtil.i("currentActivity: " + driver.currentActivity(), false);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    protected void setup(boolean isInstall, boolean isLaunch, String pr, String os, String udid, String browserName) throws Exception {

        if (driver != null) {
            driver.launchApp();
            LogUtil.i("launch app");
        } else {
            DesiredCapabilities capabilities = new DesiredCapabilities();
            capabilities.setCapability(MobileCapabilityType.BROWSER_NAME, browserName);
            capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, osInfo.get(Constants.OS_NAME));
            capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, osInfo.get(Constants.DEVICE_NAME));
            capabilities.setCapability(MobileCapabilityType.PLATFORM_VERSION, osInfo.get(Constants.PLATFORM_VERSION));
            //install并且launch
            if (isInstall && browserName == null) {
                File app = new File("package/" + osInfo.get(Constants.APP_FILE));
                capabilities.setCapability(MobileCapabilityType.APP, app.getAbsolutePath());
            }

            //launch
            if (isLaunch && browserName == null) {
                capabilities.setCapability(MobileCapabilityType.APP_PACKAGE, osInfo.get(Constants.PACKAGE));
                capabilities.setCapability(MobileCapabilityType.APP_ACTIVITY, osInfo.get(Constants.ACTIVITY));
            }

//        capabilities.setCapability("autoLaunch", false);

            //support Chinese
            capabilities.setCapability(MobileCapabilityType.UNICODE_KEYBOARD, "True");
            capabilities.setCapability("resetKeyboard", "True");

            if (udid != null) {
                capabilities.setCapability(MobileCapabilityType.UDID, udid);
            }

            driver = new AndroidDriver<AndroidElement>(new URL("http://" + this.urlAddrLocal + "/wd/hub"), capabilities);
        }
    }

    public void launchApp() throws Exception {
        driver.launchApp();
    }

    protected void takeScreenShot(Integer type) throws Exception {
        try {
            PhotoUtil.takeScreenShot(driver, type);
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.i("截屏失败");
        }
    }

    public void elementSearchWait(Element element, int duration) throws Exception {
        Integration.androidElementSearchWait(driver, element, duration);
    }

    public void setLogin(String url) throws Exception {
        By by = Integration.getByXpath("//android.widget.Button[contains(@text,'登')]");
        boolean isPresent = Integration.isElementPresentAndroid(driver, by, Constants.WAIT_DURATION_DEFAULT);
        if (isPresent) {
            runSubCases(url);
        }
    }

    public void setLogOut(String url) throws Exception{
        By by = Integration.getByXpath("//android.widget.Button[contains(@text,'登')]");
        boolean isPresent = Integration.isElementPresentAndroid(driver, by, Constants.WAIT_DURATION_DEFAULT);
        if (!isPresent) {
            runSubCases(url);
        }
    }


    public void setAction(Element element, Action action) throws Exception {
        String actionName = action.getName();
        int elementIndex = action.getElementIndex();

        Integration.setSleep(driver, Global.WaitSleepCount);

        if ("click".equals(actionName)) {
            setClick(element, elementIndex, action.getRepeatCount());
        } else if (("tap").equals(actionName)) {
            setTap(element, elementIndex, action);
        } else if ("sendKeys".equals(actionName)) {
            setSendKeys(element, elementIndex, action.getText(), action.getVars());
        } else if (("scrollTo").equals(actionName)) {
            String str = Integration.getVarsValue(action.getText(), action.getVars());
            Integration.setScrollTo(driver, str);
            Thread.sleep(Global.ScrollSleepCount * 1000);
        } else if (("scrollToExact").equals(actionName)) {
            String str = Integration.getVarsValue(action.getText(), action.getVars());
            Integration.setScrollToExact(driver, str);
            Thread.sleep(Global.ScrollSleepCount * 1000);
        } else if (("hideKeyboard").equals(actionName)) {
            Integration.setHideKeyboard(driver);
        } else if (("swipe").equals(actionName)) {
            //Thread.sleep(Global.SleepCount * 1000);
            Integration.setSwipe(driver, action);
            //Thread.sleep(Global.SleepCount * 1000);
        } else if (("pressKeyCode").equals(actionName)) {
            Integration.setPressKeyCode(driver, action.getKeyCode());
        } else if (("alertCancel").equals(actionName)) {
            Integration.setAlertCancel(driver, Constants.WAIT_DURATION_DEFAULT);
        } else if (("alertConfirm").equals(actionName)) {
            Integration.setAlertConfirm(driver, Constants.WAIT_DURATION_DEFAULT);
        } else if (("swipeToUp").equals(actionName)) {
            Thread.sleep(Global.ScrollSleepCount * 1000);
            Integration.swipeToUp(driver, action.getDuration());
            Thread.sleep(Global.ScrollSleepCount * 1000);
        } else if (("swipeToDown").equals(actionName)) {
            Thread.sleep(Global.ScrollSleepCount * 1000);
            Integration.swipeToDown(driver, action.getDuration());
            Thread.sleep(Global.ScrollSleepCount * 1000);
        } else if (("swipeToLeft").equals(actionName)) {
            Thread.sleep(Global.ScrollSleepCount * 1000);
            Integration.swipeToLeft(driver, action.getDuration());
            Thread.sleep(Global.ScrollSleepCount * 1000);
        } else if (("swipeToRight").equals(actionName)) {
            Thread.sleep(Global.ScrollSleepCount * 1000);
            Integration.swipeToRight(driver, action.getDuration());
            Thread.sleep(Global.ScrollSleepCount * 1000);
        } else if (("get").equals(actionName)) {
            driver.get(action.getText());
        } else if (("launch").equals(actionName)) {
            launchApp();
        } else if (("takePhoto").equals(actionName)) {
            takeScreenShot(4);
        }
    }


}
