package com.yunniao.appiumtest;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yunniao.appiumtest.bean.Action;
import com.yunniao.appiumtest.bean.Element;
import com.yunniao.appiumtest.bean.Time;
import com.yunniao.appiumtest.bean.Var;
import com.yunniao.appiumtest.utils.*;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.WebElement;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Created by melinda on 1/27/16.
 */
public class Integration {

    public static void setScrollTo(AppiumDriver driver, String text) throws Exception {
        driver.scrollTo(text);
    }

    public static void setIOSScrollTo(IOSDriver driver, String text) throws Exception {
        driver.scrollTo(text);
    }

    public static void setScrollToExact(AppiumDriver driver, String text) throws Exception {
        driver.scrollToExact(text);
    }

    public static void setPressKeyCode(AndroidDriver driver, int keyCode) throws Exception {
        driver.pressKeyCode(keyCode);
    }

    public static void setAlertCancel(AndroidDriver driver, int timeout) throws Exception {
        if (isAlertPresentAndroid(driver, timeout)) {
            driver.switchTo().alert().dismiss();
        }
        while (isAlertPresent(driver)) {
            driver.switchTo().alert().dismiss();
            Thread.sleep(timeout);
        }
    }

    public static void setAlertConfirm(AndroidDriver driver, int timeout) throws Exception {
        if (isAlertPresentAndroid(driver, timeout)) {
            driver.switchTo().alert().accept();
        }
        while (isAlertPresent(driver)) {
            driver.switchTo().alert().accept();
            LogUtil.i(driver.switchTo().alert().getText());
            Thread.sleep(timeout);
        }

    }

    public static void setAlertCancelIOS(IOSDriver driver, int timeout) throws Exception {
        if (isAlertPresentIOS(driver, timeout)) {
            driver.switchTo().alert().dismiss();
        }
        while (isAlertPresent(driver)) {
            driver.switchTo().alert().dismiss();
            Thread.sleep(timeout);
        }
    }

    public static void setAlertConfirmIOS(IOSDriver driver, int timeout) throws Exception {
        if (isAlertPresentIOS(driver, timeout)) {
            driver.switchTo().alert().accept();
        }
        while (isAlertPresent(driver)) {
            driver.switchTo().alert().accept();
            LogUtil.i(driver.switchTo().alert().getText());
            Thread.sleep(timeout);
        }

    }


    public static boolean isAlertPresent(AppiumDriver driver) {
        try {
            driver.switchTo().alert();
            return true;
        } catch (NoAlertPresentException Ex) {
            return false;
        }
    }

    public static void setHideKeyboard(AppiumDriver driver) throws Exception {
        driver.hideKeyboard();
    }

    public static void setHideKeyboardIOS(IOSDriver driver, String value) throws Exception {
        driver.hideKeyboard();
//        driver.hideKeyboard(HideKeyboardStrategy.PRESS_KEY, value);
    }

    public static void setSwipe(AppiumDriver driver, Action action) throws Exception {
        if (action.getStartX() < 1) {
            int during = CommonUtil.getDefaultCount(2, action.getDuration());
            double width = driver.manage().window().getSize().width;
            double height = driver.manage().window().getSize().height;
            int sx = (int) (width * action.getStartX());
            int sy = (int) (height * action.getStartY());
            int ex = (int) (width * action.getEndX());
            int ey = (int) (height * action.getEndY());
            driver.swipe(sx, sy, ex, ey, during);
        } else {
            driver.swipe((int) action.getStartX(), (int) action.getStartY(), (int) action.getEndX(), (int) action.getEndY(), action.getDuration());
        }
        setSleep(driver, Global.SleepCount);
    }

    public static void swipeToUp(AppiumDriver driver, int during) throws Exception {
        during = CommonUtil.getDefaultCount(2, during);
        int width = driver.manage().window().getSize().width;
        int height = driver.manage().window().getSize().height;
        driver.swipe(width / 2, height * 3 / 4, width / 2, height / 4, during);
        setSleep(driver, Global.SleepCount);
    }

    public static void swipeToDown(AppiumDriver driver, int during) throws Exception {
        during = CommonUtil.getDefaultCount(2, during);
        int width = driver.manage().window().getSize().width;
        int height = driver.manage().window().getSize().height;
        driver.swipe(width / 2, height / 4, width / 2, height * 3 / 4, during);
        // wait for page loading
        setSleep(driver, Global.SleepCount);
    }

    public static void swipeToLeft(AppiumDriver driver, int during) throws Exception {
        during = CommonUtil.getDefaultCount(2, during);
        int width = driver.manage().window().getSize().width;
        int height = driver.manage().window().getSize().height;
        driver.swipe(width * 4 / 5, height / 2, width / 5, height / 2, during);
        // wait for page loading
        setSleep(driver, Global.SleepCount);
    }

    public static void swipeToRight(AppiumDriver driver, int during) throws Exception {
        during = CommonUtil.getDefaultCount(2, during);
        int width = driver.manage().window().getSize().width;
        int height = driver.manage().window().getSize().height;
        driver.swipe(width / 5, height / 2, width * 4 / 5, height / 2, during);
        // wait for page loading
        setSleep(driver, Global.SleepCount);
    }

    public static void setSleep(AppiumDriver driver, int sleepCount) throws Exception {
        int sleepSec = CommonUtil.getDefaultCount(1, sleepCount);
        driver.manage().timeouts().implicitlyWait(sleepSec, TimeUnit.SECONDS);
    }

    public static String setXPathForWeb(String os, String path, Element element) throws Exception {
        String elementName = element.getClassName();
        String elementXpath = element.getxPath();
        String attribute = element.getAttribute();
        String value = element.getValue();
        Object elementIndexOrigin = element.getClassIndex();
        int elementIndex = 0;
        if (elementIndexOrigin != null) {
            elementIndex = Integer.parseInt(getTextValue(String.valueOf(elementIndexOrigin)));
        }

        Element siblingElement = element.getSiblingElement();
        if (siblingElement != null) {
            String siblingPath = setXPath(os, "", siblingElement).replace("/", "");
            path += "[" + siblingPath + "]";
        }

        if (CommonUtil.isExist(elementName)) {
            path += "/";
            path += elementName;
            if (CommonUtil.isExist(attribute)) {
                path += "[@" + attribute + "='" + value + "']";
            }

            if (elementIndex > 0) {
                path += "[" + elementIndex + "]";
            }

        } else if (CommonUtil.isExist(elementXpath)) {
            path = path + elementXpath;
        }

        Element subElement = element.getSubElement();
        if (subElement == null) {
            return path;
        } else {
            return setXPath(os, path, subElement);
        }
    }

    public static String setXPath(String os, String path, Element element) throws Exception {
        String elementName = element.getClassName();
        String elementText = element.getText();
        String elementXpath = element.getxPath();
        int elementTextType = element.getTextType();
        int elementTextMatchType = element.getTextMatchType();
        Object elementIndexOrigin = element.getClassIndex();
        int elementIndex = 0;
        if (elementIndexOrigin != null) {
            elementIndex = Integer.parseInt(getTextValue(String.valueOf(elementIndexOrigin)));
        }

        ArrayList<Var> vars = element.getVars();
        elementText = getVarsValue(elementText, vars);

        Element siblingElement = element.getSiblingElement();
        if (siblingElement != null) {
            String siblingPath = setXPath(os, "", siblingElement).replaceFirst("/", "");
            path += "[" + siblingPath + "]";
        }

        if (CommonUtil.isExist(elementName)) {
            path += "/";
            if ((Constants.OS_ANDROID).equalsIgnoreCase(os)) {
                path += "android.widget.";
            }
            path += elementName;
            if (CommonUtil.isExist(elementText)) {
                String matchTypeName = CommonUtil.getTextMatchName(elementTextMatchType);
                path += "[" + matchTypeName + "(@" + CommonUtil.getTextTypeName(os, elementTextType) + ",'" + elementText + "')]";
            }

            if (elementIndex > 0) {
                path += "[" + elementIndex + "]";
            }

        } else if (CommonUtil.isExist(elementXpath)) {
            path = path + elementXpath;
        }

        Element subElement = element.getSubElement();
        if (subElement == null) {
            return path;
        } else {
            return setXPath(os, path, subElement);
        }

    }

    public static By getById(String id) {

        By by = new By.ById(id);

        return by;
    }

    public static By getByXpath(String XPath) {

        By by = new By.ByXPath(XPath);

        return by;
    }

    public static boolean isElementPresentIOS(IOSDriver driver, final By by, int timeOut) {
        try {
            new IOSDriverWait(driver, timeOut)
                    .until(new IOSCondition<WebElement>() {
                        public WebElement apply(IOSDriver d) {
                            return d.findElement(by);
                        }

                    });
            return true;
        } catch (Exception e) {
            return false;
        }

    }

    public static boolean isAlertPresentIOS(IOSDriver driver, int timeOut) {
        try {
            new IOSDriverWait(driver, timeOut)
                    .until(new IOSCondition<Boolean>() {
                        public Boolean apply(IOSDriver d) {
                            return isAlertPresent(d);
                        }

                    });
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isAlertPresentAndroid(AndroidDriver driver, int timeOut) {
        try {
            new AndroidDriverWait(driver, timeOut)
                    .until(new AndroidCondition<Boolean>() {
                        public Boolean apply(AndroidDriver d) {
                            return isAlertPresent(d);
                        }

                    });
            return true;
        } catch (Exception e) {
            return false;
        }
    }


    public static void iosElementSearchWait(IOSDriver driver, Element element, int duration) throws Exception {
        String path = Integration.setXPath("ios", "/", element);
        By by = Integration.getByXpath(path);
        int timeOut = CommonUtil.getDefaultCount(3, duration);
        boolean isElementSearch = isElementPresentIOS(driver, by, timeOut);
        if (isElementSearch) {
            LogUtil.i("元素查找等待" + timeOut + "秒操作:控件存在");
        } else {
            ErrorLogUtil.e("元素查找等待" + timeOut + "秒操作:控件不存在!");
            ErrorLogUtil.e("xpath:" + path);
        }
    }

    public static void androidElementSearchWait(AndroidDriver driver, Element element, int duration) throws Exception {
        String path = Integration.setXPath("android", "/", element);
        By by = Integration.getByXpath(path);
        int timeOut = CommonUtil.getDefaultCount(3, duration);
        boolean isElementSearch = isElementPresentAndroid(driver, by, timeOut);
        if (isElementSearch) {
            LogUtil.i("元素查找等待" + timeOut + "秒操作:控件存在");
        } else {
            ErrorLogUtil.e("元素查找等待" + timeOut + "秒操作:控件不存在!");
            ErrorLogUtil.e("xpath:" + path);
        }
    }


    public static boolean isElementPresentAndroid(AndroidDriver driver, final By by, int timeOut) {
        try {
            new AndroidDriverWait(driver, timeOut)
                    .until(new AndroidCondition<WebElement>() {
                        public WebElement apply(AndroidDriver d) {
                            return d.findElement(by);
                        }

                    });
            return true;
        } catch (Exception e) {
            return false;
        }

    }


    public static void elementSearch(List<? extends MobileElement> elements, String text, boolean isIgnore) throws Exception {
        text = getTextValue(text);
        int elementSize = elements.size();
        if (elementSize > 0) {
            LogUtil.i("text值为'" + text + "'的控件存在!:" + elementSize);
        } else {
            if (!isIgnore) {
                throw new Exception("没有查找到text值为'" + text + "'的元素");
            } else {
                ErrorLogUtil.e("查找操作,控件不存在!");
                ErrorLogUtil.e("text值为'" + text + "'");
            }
        }
    }

    public static void elementValueContains(List<? extends MobileElement> elements, String text, ArrayList<Var> vars, boolean isIgnore) throws Exception {
        boolean isEquals = false;
        String elementText = "";
        text = getTextValue(text);
        for (int i = 0, size = elements.size(); i < size; i++) {
            elementText = elements.get(i).getText();
            if (elementText.contains(text)) {
                isEquals = true;
                break;
            }
        }
        if (isEquals) {
            if (vars != null) {
                setKeyValue(vars, elementText);
            }
            LogUtil.i("验证值包含操作:text值为'" + text + "'的控件存在!");
        } else {
            if (!isIgnore) {
                throw new Exception("没有验证到包含text值为'" + text + "'的元素");
            } else {
                ErrorLogUtil.e("验证值包含操作:text值验证不正确,控件不存在!");
                ErrorLogUtil.e("text值为'" + text + "'");
            }
        }
    }

    public static void setKeyValue(ArrayList<Var> vars, String text) throws Exception {
        String key = vars.get(0).getVarName();
        String subText = vars.get(0).getSubText();
        text = text.replace(subText, "");
        KeyValueUtil.put(key, text);
        LogUtil.i(key + ":" + KeyValueUtil.get(key));
    }


    public static void elementValueEquals(List<? extends MobileElement> elements, String text, boolean isIgnore) throws Exception {
        boolean isEquals = false;
        text = getTextValue(text);
        for (int i = 0, size = elements.size(); i < size; i++) {
            String elementText = elements.get(i).getText();
            if ((text).equals(elementText)) {
                isEquals = true;
                break;
            }
        }
        if (isEquals) {
            LogUtil.i("验证值相等操作:text值为'" + text + "'的控件存在!:");
        } else {
            if (!isIgnore) {
                throw new Exception("没有验证到text值为'" + text + "'的元素");
            } else {
                ErrorLogUtil.e("验证值相等操作:text值验证不正确,text值为'" + text + "'的控件不存在!");
            }
        }
    }

    public static String getVarsValue(String text, ArrayList<Var> vars) throws Exception {
        String str;
        if (vars != null && vars.size() > 0) {
            String key = vars.get(0).getVarName();
            str = KeyValueUtil.get(key).toString();
        } else {
            str = getTextValue(text);
        }
        return str;
    }

    public static String getTextValue(String text) throws Exception {
        String str;
        if (text != null && text.contains("$")) {
            String key = text.replace("$", "");
            str = KeyValueUtil.get(key).toString();
        } else if (text != null && text.contains("{")) {
            str = getTimeValue(text);
        } else {
            str = text;
        }
        return str;
    }

    public static String getTimeValue(String str) throws Exception {
        try {
            Time jsonObject = JSON.parseObject(str, Time.class);
            str = Integration.processTime(jsonObject);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }

        return str;
    }

    public static String getValue(Object text) throws Exception {
        String value = null;
        if (text instanceof JSONObject) {
            JSONObject texts = (JSONObject) text;
            Time jsonObject = texts.getObject("varValue", Time.class);
            value = processTime(jsonObject);
        } else if (text instanceof String) {
            value = getTextValue((String) text);
        }

        return value;
    }

    public static String SetHourMin(String time, int add) throws Exception {
        time = getTextValue(time);
        String[] array = time.split("=");
        int Seconds = 0;
        Seconds += Integer.parseInt(array[0]) * 60;
        Seconds += Integer.parseInt(array[1]);
        Seconds += add;
        int Hour = Seconds / 60;
        int Min = Seconds % 60;
        if (Hour > 23) {
            Hour = Hour - 24;
        }
        String SHour;
        if (Hour < 10) {
            SHour = "0" + Hour;
        } else {
            SHour = String.valueOf(Hour);
        }
        String SMin;
        if (Min < 10) {
            SMin = "0" + Min;
        } else {
            SMin = String.valueOf(Min);
        }

        String rTime = SHour + ":" + SMin;

        return rTime;
    }

    public static String processTime(Time jsonObject) throws Exception {
        String endTime = null;
        long originSeconds = 0;
        long seconds = 0;
        String originTime = getTextValue(jsonObject.getOriginTime());
        String format = jsonObject.getFormat();
        String operate = jsonObject.getOperate();
        int time = jsonObject.getTime();
        if (originTime != null && originTime.length() == 5) {
            SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd");
            originTime = formater.format(new Date()) + " " + originTime + ":00";
        }
        //初始化日期
        if (originTime == null) {
            originSeconds = Long.valueOf(timeStamp());
        } else {
            try {
                originSeconds = Long.valueOf(date2TimeStamp(originTime, "yyyy-MM-dd HH:mm:ss"));
            } catch (Exception e) {
                throw new Exception(e.getMessage());
            }
        }

        //在日期上加减
        if (("+").equalsIgnoreCase(operate)) {
            seconds = originSeconds + time;
        } else if (("-").equalsIgnoreCase(operate)) {
            seconds = originSeconds - time;
        }

        //转换日期格式
        endTime = timeStamp2Date(String.valueOf(seconds), format);

        return endTime;
    }

    /**
     * 时间戳转换成日期格式字符串
     *
     * @param seconds 精确到秒的字符串
     * @param format
     * @return
     */
    public static String timeStamp2Date(String seconds, String format) {
        if (seconds == null || seconds.isEmpty() || seconds.equals("null")) {
            return "";
        }
        if (format == null || format.isEmpty()) format = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(new Date(Long.valueOf(seconds + "000")));
    }

    /**
     * 日期格式字符串转换成时间戳
     *
     * @param date_str 字符串日期
     * @param format   如：yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static String date2TimeStamp(String date_str, String format) throws Exception {
        String timeStamp = "";
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            timeStamp = String.valueOf(sdf.parse(date_str).getTime() / 1000);
        } catch (ParseException e) {
            throw new Exception("请正确填写初始时间,格式为:'2018-08-08 08:08:08'");
        }
        return timeStamp;
    }

    /**
     * 取得当前时间戳（精确到秒）
     *
     * @return
     */
    public static String timeStamp() {
        long time = System.currentTimeMillis();
        String t = String.valueOf(time / 1000);
        return t;
    }

    public static String timeStampMin() {
        long time = System.currentTimeMillis();
        String t = String.valueOf(time);
        return t;
    }

    public static JSONObject paramVars(JSONObject params) throws Exception {
        Set<String> resultStrings = params.keySet();
        String originValue;
        try {
            for (String key : resultStrings) {
                Object time = params.get(key);
                //时间的处理
                if (time instanceof JSONObject) {
                    Time jsonObject = params.getObject(key, Time.class);
                    String value = Integration.processTime(jsonObject);
                    params.put(key, value);
                }
                originValue = params.getString(key);
                //处理变量
                if (originValue.contains("$$")) {
                    //
                } else if (originValue.contains("$")) {//获取内存里的数据
                    String vaule = KeyValueUtil.get(originValue.replace("$", ""));
                    params.put(key, vaule);
                }
            }
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }

        return params;
    }
}
