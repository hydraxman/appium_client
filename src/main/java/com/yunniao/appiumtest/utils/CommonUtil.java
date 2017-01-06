package com.yunniao.appiumtest.utils;

import com.yunniao.appiumtest.Constants;
import com.yunniao.appiumtest.Global;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

/**
 * Created by melinda on 1/14/16.
 */
public class CommonUtil {
    public static boolean isExist(String str)    {
        if (str != null && str.trim().length() != 0) {
            return true;
        }
        return false;
    }




    public static int getSleepCount(int sleepCount)
    {
        if(sleepCount > 0) {
            return sleepCount;
        }

        return Global.SleepCount;
    }

    public static int getDefaultCount(int type, int count)
    {
        if(count > 0) {
            return count;
        }

        switch (type) {
            case 1: //sleep count
                count = Global.SleepCount;
                break;
            case 2: //duration min-seconds
                count = Constants.SWIPE_DURATION_DEFAULT;
                break;
            case 3://duration 元素查找显式等待second
                count = Constants.WAIT_DURATION_DEFAULT;
                break;
        }
        return count;
    }


    public static String getTextMatchName(int matchType)
    {
        String matchTypeName;
        switch (matchType)
        {
            case Constants.TEXT_MATCH_TYPE_STARTSWITH:
                matchTypeName = Constants.TEXT_MATCH_TYPE_NAME_STARTSWITH;
                break;
            case Constants.TEXT_MATCH_TYPE_ENDSWITH:
                matchTypeName = Constants.TEXT_MATCH_TYPE_NAME_ENDSWITH;
                break;
            case Constants.TEXT_MATCH_TYPE_CONTAINS:
            default:
                matchTypeName = Constants.TEXT_MATCH_TYPE_NAME_CONTAINS;
                break;
        }
        return  matchTypeName;
    }

    public static HashMap<String,  String> getOsInfo(String os, String pr)
    {
        HashMap<String, String> stringHashMap = new HashMap<String, String>();
        String deviceName = null;
        String osName = null;
        String platformVersion = null;
        String appFile = null;
        String packageName=null;
        String activityName=null;
        String loginActivityName = null;
        String appPath = null;
        String platformVersionSimulator = null;
        String deviceNameSimulator = null;
        if((Constants.OS_ANDROID).equalsIgnoreCase(os)) {
            deviceName = Constants.DEVICE_ANDROID_NAME;
            osName = Constants.OS_ANDROID;
            platformVersion = Constants.PLATFORM_VERSION_ANDROID;
            if(("customer").equalsIgnoreCase(pr)) {
                appFile = Constants.APP_FILE_CUSTOMER_ANDROID;
                packageName = Constants.PACKAGE_CUSTOMER;
                activityName = Constants.PACKAGE_CUSTOMER_ACTIVITY;
                loginActivityName = Constants.PACKAGE_CUSTOMER_LOGIN_ACTIVITY;
            } else if(("xiazai").equalsIgnoreCase(pr)) {
                appFile = Constants.APP_FILE_XIAZAI_ANDROID;
                packageName = Constants.PACKAGE_XIAZAI;
                activityName = Constants.PACKAGE_XIAZAI_ACTIVITY;
            } else if(("driver").equalsIgnoreCase(pr)) {
                appFile = Constants.APP_FILE_S_ANDROID;
                packageName = Constants.PACKAGE_S;
                activityName = Constants.PACKAGE_S_ACTIVITY;
                loginActivityName = Constants.PACKAGE_DRIVER_LOGIN_ACTIVITY;
            } else if(("y").equalsIgnoreCase(pr)) {
                appFile = Constants.APP_FILE_Y_ANDROID;
                packageName = Constants.PACKAGE_Y;
                activityName = Constants.PACKAGE_Y_ACTIVITY;
                loginActivityName = Constants.PACKAGE_Y_LOGIN_ACTIVITY;
            } else if(("f").equalsIgnoreCase(pr)) {
                appFile = Constants.APP_FILE_F_ANDROID;
                packageName = Constants.PACKAGE_F;
                activityName = Constants.PACKAGE_F_ACTIVITY;
                loginActivityName = Constants.PACKAGE_F_LOGIN_ACTIVITY;
            }
        } else if((Constants.OS_IOS).equalsIgnoreCase(os)) {
            deviceName = Constants.DEVICE_IOS_NAME;
            deviceNameSimulator = Constants.DEVICE_IOS_NAME_SIMULATOR;
            osName = Constants.OS_IOS;
            platformVersion = Constants.PLATFORM_VERSION_IOS;
            platformVersionSimulator = Constants.PLATFORM_VERSION_IOS_SIMULATOR;
            if(("customer").equalsIgnoreCase(pr)) {
                appFile = Constants.APP_FILE_CUSTOMER_IOS;
                packageName = Constants.PACKAGE_CUSTOMER_IOS;
                appPath = Constants.APP_PATH_CUSTOMER_IOS;
            } else if(("driver").equalsIgnoreCase(pr)) {
                appFile = Constants.APP_FILE_S_IOS;
                packageName = Constants.PACKAGE_S_IOS;
                appPath = Constants.APP_PAHT_DRIVER_IOS;
            }
        }

        stringHashMap.put(Constants.DEVICE_NAME, deviceName);
        stringHashMap.put(Constants.DEVICE_NAME_SIMULATOR, deviceNameSimulator);
        stringHashMap.put(Constants.OS_NAME, osName);
        stringHashMap.put(Constants.PLATFORM_VERSION, platformVersion);
        stringHashMap.put(Constants.PLATFORM_VERSION_SIMULATOR, platformVersionSimulator);
        stringHashMap.put(Constants.APP_FILE, appFile);
        stringHashMap.put(Constants.PACKAGE, packageName);
        stringHashMap.put(Constants.ACTIVITY, activityName);
        stringHashMap.put(Constants.ACTIVITY_LOGIN, loginActivityName);
        stringHashMap.put(Constants.APP_PATH, appPath);

        return stringHashMap;
    }

    public static String getTextTypeName(String os, int $textType)
    {
        String textName = null;
        switch ($textType)
        {
            case 1:
                textName = Constants.IOS_ELEMENT_TEXT_TYPE_NAME;
                break;
            case 2:
                textName = Constants.IOS_ELEMENT_TEXT_TYPE_VALUE;
                break;
            case 4:
                textName = Constants.ANDROID_ELEMENT_TYPE_ID;
                break;
            case 5:
                textName = Constants.ANDROID_ELEMENT_TYPE_DESC;
                break;
            case 3:
            default:
                if((Constants.OS_ANDROID).equalsIgnoreCase(os)) {
                    textName = Constants.ANDROID_ELEMENT_TEXT_TYPE_TEXT;
                } else {
                    textName = Constants.IOS_ELEMENT_TEXT_TYPE_LABEL;
                }
                break;
        }
        return textName;
    }

    public static String zipReport() {
        String reportName = KeyValueUtil.get(Constants.REPORT_NAME);
        File file = new File( "report/" + reportName);
        String sourceFilePath = file.getAbsolutePath();

        String zipFilePath = "/var/tmp/";
        File directory = new File(".");
        try {
            zipFilePath = directory.getCanonicalPath();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String logName = KeyValueUtil.get("logName");
        String sourceFilePathNew = zipFilePath + "/../../automated_test_report/" + logName + "_" + reportName;
        ZipCompressor zc = new  ZipCompressor(sourceFilePathNew + ".zip");
        zc.compressExe(sourceFilePath);

        return  "";
    }

    public static void scpZip() {
        try {
            //PhotoUtil.doSCP();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
