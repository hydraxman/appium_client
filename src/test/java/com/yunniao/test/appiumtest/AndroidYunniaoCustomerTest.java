package com.yunniao.test.appiumtest;

/**
 * Created by melinda on 1/8/16.
 */

import com.yunniao.appiumtest.Global;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidKeyCode;
import io.appium.java_client.remote.MobileCapabilityType;
import org.junit.*;
import org.openqa.selenium.By;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.File;
import java.net.URL;
import java.util.List;

/**
 * Test Mobile Driver features
 */
public class AndroidYunniaoCustomerTest {
    private AndroidDriver<MobileElement> driver;

    private int sleep_count = 5000;




    @BeforeClass
    public static void beforeClass() throws Exception {
    }

    @Before
    public void setup() throws Exception {

        File app = new File("AndroidSMaster.apk");
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability(MobileCapabilityType.BROWSER_NAME, "");
        capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, "Android Emulator");
        capabilities.setCapability(MobileCapabilityType.PLATFORM_VERSION, "5.1");
        capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, "Android");
        String path = null;
        String ipAddr = null;

        if (Global.remote) {
            path = "./AndroidSMaster.apk";
            ipAddr = "172.16.41.16";
        } else {
            path = app.getAbsolutePath();
            ipAddr = "localhost";
        }
        capabilities.setCapability(MobileCapabilityType.APP, path);
        driver = new AndroidDriver<MobileElement>(new URL("http://" + ipAddr + ":4723/wd/hub"), capabilities);
    }

    @After
    public void tearDown() throws Exception {
        driver.quit();
    }

    @Test
    public void LoginActionTest() throws InterruptedException {

        try {
            Thread.sleep(sleep_count);

            //登录操作

            driver.findElementByClassName("android.widget.EditText").sendKeys("shudongpo ");

            driver.findElement(By.xpath("//android.widget.EditText[contains(@id,\"login_password\")]]"));

            driver.findElementById("login_password").sendKeys("123456");
            driver.findElement(By.xpath("//android.widget.EditText[contains(@id,'login_password')])"));

            driver.hideKeyboard();

            driver.scrollTo("登录");

            driver.findElementById("bt_login").click();

            Thread.sleep(sleep_count);

            System.out.println(driver.currentActivity());


            driver.swipe(382,1140, 382,782 , 400);

            Thread.sleep(sleep_count);
            driver.swipe(382,1140, 382,782 , 200);



            //System.out.println(driver.getPageSource());
            //driver.pressKeyCode(AndroidKeyCode.KEYCODE_ENTER);
            driver.pressKeyCode(AndroidKeyCode.KEYCODE_DPAD_CENTER);

        } catch (Exception e) {
            e.printStackTrace();
        }

        Thread.sleep(sleep_count);
        System.out.println("login test end!");
    }

    @Test
    public void ViewListTest() throws InterruptedException {

        System.out.println(driver.currentActivity());
        LoginActionTest();
        System.out.println(driver.currentActivity());

        driver.scrollTo("text");


        try {
            Thread.sleep(sleep_count);

            //线路任务,每日运力,待办运力,我的
            MobileElement eleone = null;
            List<MobileElement> eles = driver.findElement(By.id("view_tab")).findElements(By.className("android.widget.Button"));
            for (int j = 0, size = eles.size(); j < size; j++) {
                eleone = eles.get(j);
                System.out.println(eleone.getText());
                System.out.print(eleone.isSelected());
                if (!eleone.isSelected()) {
                    eleone.click();
                }
                Thread.sleep(sleep_count);
            }

            driver.findElementByXPath("//android.widget.Button[contains(@text,'线路任务')]").click();
            Thread.sleep(sleep_count);

            //筛选状态
            driver.findElementByClassName("android.widget.CheckBox").click();
            Thread.sleep(sleep_count);

            List<MobileElement> ele_buttons = driver.findElement(By.id("gv_tasklist_dropdown")).findElements(By.className("android.widget.TextView"));
            MobileElement ele_button_one = null;
            for (int i = 0, size = ele_buttons.size(); i < size; i++) {
                ele_button_one = ele_buttons.get(i);
                System.out.println(ele_button_one.getText());
                ele_button_one.click();
                Thread.sleep(sleep_count);
                driver.findElementByClassName("android.widget.CheckBox").click();
                Thread.sleep(sleep_count);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("view list test end!");
    }

    @Test
    public void CreateTaskTest() throws InterruptedException {

        LoginActionTest();

        Assert.assertEquals("equals", "1", "1");
        try {
            Thread.sleep(sleep_count * 5000);
            //创建-取消
            driver.findElement(By.id("tv_title_function")).click();
            Thread.sleep(sleep_count * 5000);
            driver.pressKeyCode(AndroidKeyCode.KEYCODE_BACK);
            Thread.sleep(sleep_count * 5000);
            driver.findElement(By.id("btn_dialog_cancel")).click();
            driver.pressKeyCode(AndroidKeyCode.KEYCODE_BACK);
            driver.findElement(By.id("btn_dialog_confirm")).click();
            Thread.sleep(sleep_count * 5000);


        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("create task test end!");
    }





    @AfterClass
    public static void afterClass() {
    }
}
