package com.yunniao.test.appiumtest;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.remote.MobileCapabilityType;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.net.URL;

/**
 * Created by melinda on 1/23/16.
 */
public class IosYunniaoCustomerTest {

    private AppiumDriver driver;
    @Before
    public void setUp() throws Exception {
//        File appDir = new File(System.getProperty("user.dir"), "./");
//        File app = new File(appDir, "YNBusiness(1).ipa");
        DesiredCapabilities capabilities = new DesiredCapabilities();
//        capabilities.setCapability(MobileCapabilityType.APP, app.getAbsolutePath());
        capabilities.setCapability(MobileCapabilityType.BROWSER_NAME, "");
        capabilities.setCapability(MobileCapabilityType.PLATFORM_VERSION, "9.2");
        capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, "iPhone 6");

        driver = new IOSDriver<MobileElement>(new URL("http://127.0.0.1:4723/wd/hub"), capabilities);
    }

    @After
    public void tearDown() throws Exception {
        driver.quit();
    }

    @Test
    public void createTaskTest() throws Exception{

        Thread.sleep(5000);
//        driver.findElement(By.xpath("//UIATextField[contains(@text,\"请输入用户名\")]]")).sendKeys("蔬东坡");
//        driver.findElement(By.xpath("//UIAApplication[1]/UIAWindow[1]/UIATableView[1]/UIATableCell[1]/UIATextField[1]")).sendKeys("蔬东坡");
//        driver.findElement(By.xpath("//UIAApplication[1]/UIAWindow[1]/UIATableView[1]/UIATableCell[2]/UIASecureTextField[1]")).sendKeys("123456");
//        driver.findElement(By.xpath("//UIAApplication[1]/UIAWindow[1]/UIATableView[1]/UIAButton[1]")).click();

        //driver.findElementByClassName("UIATextField").sendKeys("蔬东坡");
        //driver.findElementByClassName("UIASecureTextField").sendKeys("123456");
        //driver.findElementByClassName("UIAButton").click();
//
//        driver.findElementByXPath("//UIATextField[contains(@value,'请输入')]").sendKeys("蔬东坡");
//        driver.findElementByXPath("//UIASecureTextField[contains(@value,'请输入密码')]").sendKeys("123456");
//        driver.findElementByXPath("//UIAButton[contains(@label,'登录')]").click();
//        Thread.sleep(5000);
//        driver.findElementByXPath("//UIAButton[contains(@label,'创建')]").click();
//        driver.findElementByXPath("//UIAButton[contains(@label,'返回')]").click();
//
//
//        driver.findElementByXPath("//UIAElement[contains(@label,'待办运力')]").click();
//
//        driver.findElementByXPath("//UIAElement[contains(@label,'线路任务')]").click();
//        LogUtil.i(driver.findElementByXPath("//UIAElement[contains(@label,'线路任务')]").getText());
//
//        driver.swipe(382,1140,382,782,200);
//        driver.switchTo().alert().accept();
//        driver.switchTo().alert().wait();
//        driver.switchTo().activeElement();








    }
}
