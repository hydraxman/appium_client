package com.yunniao.appiumtest;

import com.yunniao.appiumtest.bean.Action;
import com.yunniao.appiumtest.bean.Element;
import com.yunniao.appiumtest.bean.TestCase;
import com.yunniao.appiumtest.utils.LogUtil;
import com.yunniao.appiumtest.utils.PhotoUtil;
import io.appium.java_client.MobileElement;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.ios.IOSElement;
import io.appium.java_client.remote.MobileCapabilityType;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebElement;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;

/**
 * Created by melinda on 1/11/16.
 */
public class IntegrationIosTest extends IntegrationTest<IOSElement, IOSDriver<IOSElement>> {

    public IntegrationIosTest(TestCase testCase, String urlAddrLocal, String udid, Integer isQuit) {

        super(testCase, urlAddrLocal, udid, isQuit);
    }

    @Override
    public void stopDriver(boolean stopAll) throws IOException {
        if (driver != null) {
            if (isQuit != 2) {
                driver.quit();
                LogUtil.i("quit app");
            } else {
                driver.closeApp();
                LogUtil.i("close app");
            }
        }
        super.stopDriver(stopAll);
    }

    protected void setup(boolean isInstall, boolean isLaunch, String pr, String os, String udid, String browserName) throws Exception {

        if (driver != null) {
            driver.launchApp();
            LogUtil.i("launch app");
        } else {
            DesiredCapabilities capabilities = new DesiredCapabilities();

            if (isInstall && browserName == null) {
                File appDir = new File(System.getProperty("user.dir"), "/package/");
                File app = new File(appDir, osInfo.get(Constants.APP_FILE));
                capabilities.setCapability(MobileCapabilityType.APP, app.getAbsolutePath());
            }

            if (isLaunch && browserName == null) {
                capabilities.setCapability(MobileCapabilityType.APP, osInfo.get(Constants.PACKAGE));
            }

            capabilities.setCapability(MobileCapabilityType.BROWSER_NAME, browserName);
            capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, osInfo.get(Constants.OS_NAME));
            capabilities.setCapability(MobileCapabilityType.PLATFORM_VERSION, osInfo.get(Constants.PLATFORM_VERSION));
            capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, osInfo.get(Constants.DEVICE_NAME));

            capabilities.setCapability(MobileCapabilityType.UDID, udid);

            capabilities.setCapability("noReset", true);
            //this auto accepts the Notification alert
            //capabilities.setCapability("autoAcceptAlerts", true);
            //capabilities.setCapability("waitForAppScript", "$.delay(5000); $.acceptAlert();");

            //support Chinese
            capabilities.setCapability(MobileCapabilityType.UNICODE_KEYBOARD, "True");
            capabilities.setCapability("resetKeyboard", "True");

            driver = new IOSDriver<IOSElement>(new URL("http://" + this.urlAddrLocal + "/wd/hub"), capabilities);
        }
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
        Integration.iosElementSearchWait(driver, element, duration);
    }


    public void setLogin(String url) throws Exception {
        By by = Integration.getByXpath("//UIAButton[contains(@label, '登')]");
        boolean isPresent = Integration.isElementPresentIOS(driver, by, Constants.WAIT_DURATION_DEFAULT);
        if (isPresent) {
            runSubCases(url);
        }
    }

    public void setLogOut(String url) throws Exception {
        By by = Integration.getByXpath("//UIAButton[contains(@label, '登')]");
        boolean isPresent = Integration.isElementPresentIOS(driver, by, Constants.WAIT_DURATION_DEFAULT);
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
        } else if ("sendKeysWithAction".equals(actionName)) {
            setSendKeysWithAction(element, elementIndex, action.getText());
        } else if (("scrollTo").equals(actionName)) {
            Integration.setIOSScrollTo(driver, action.getText());
            Thread.sleep(Global.ScrollSleepCount * 1000);
        } else if (("scrollToForIOS").equals(actionName)) {
            scrollTo(element);
            Thread.sleep(Global.ScrollSleepCount * 1000);
        }
        if (("scrollToExact").equals(actionName)) {
            Integration.setScrollToExact(driver, action.getText());
            Thread.sleep(Global.ScrollSleepCount * 1000);
        } else if (("hideKeyboard").equals(actionName)) {
            Integration.setHideKeyboardIOS(driver, action.getText());
        } else if (("swipe").equals(actionName)) {
            //Thread.sleep(Global.SleepCount * 1000);
            Integration.setSwipe(driver, action);
            //Thread.sleep(Global.SleepCount * 1000);

        } else if (("alertCancel").equals(actionName)) {
            Integration.setAlertCancelIOS(driver, Constants.WAIT_DURATION_DEFAULT);
        } else if (("alertConfirm").equals(actionName)) {
            Integration.setAlertConfirmIOS(driver, Constants.WAIT_DURATION_DEFAULT);
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
        } else if (("switchTrue").equals(actionName)) {
            switchTrue(element, elementIndex);
        } else if (("switchFalse").equals(actionName)) {
            switchFalse(element, elementIndex);
        } else if (("scrollTo").equals(actionName)) {
            String str = Integration.getVarsValue(action.getText(), action.getVars());
            Integration.setScrollTo(driver, str);
        } else if (("scrollToExact").equals(actionName)) {
            String str = Integration.getVarsValue(action.getText(), action.getVars());
            Integration.setScrollToExact(driver, str);
        } else if (("get").equals(actionName)) {
            driver.get(action.getText());
        } else if (("takePhoto").equals(actionName)) {
            takeScreenShot(4);
        }

        //todo 添加异常处理
    }

    public void switchTrue(Element element, int elementIndex) throws Exception {
        checkElements(element);
        String value = getElements().get(elementIndex).getAttribute("value");
        if (("0").equals(value)) {
            getElements().get(elementIndex).click();
        }
    }

    public void switchFalse(Element element, int elementIndex) throws Exception {
        checkElements(element);
        String value = getElements().get(elementIndex).getAttribute("value");
        if (("1").equals(value)) {
            getElements().get(elementIndex).click();
        }
    }

    public void setSendKeysWithAction(Element element, int elementIndex, String text) throws Exception {
        checkElements(element);
        Actions actions = new Actions(driver);
        MobileElement elementOne = getElements().get(elementIndex);
        actions.sendKeys(elementOne, text).perform();
    }

    public void scrollTo(Element element) throws Exception {
        checkElements(element);
        JavascriptExecutor js = driver;

        HashMap<String, String> scrollObject = new HashMap<String, String>();

        scrollObject.put("element", getElements().get(0).getId());

        js.executeScript("mobile: scroll", scrollObject);
    }
}
