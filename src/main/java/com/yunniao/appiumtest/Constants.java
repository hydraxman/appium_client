package com.yunniao.appiumtest;

/**
 * Created by MrBu on 2016/1/12.
 */
public interface Constants {
    String ENCODING = "UTF-8";
    int OPRATION_TYPE_ELEMENT = 1;
    int OPRATION_TYPE_VERIFY = 2;
    int OPRATION_TYPE_SLEEP = 3;
    int OPRATION_TYPE_SUB_CASE = 4;
    int OPRATION_TYPE_API = 5;
    int OPRATION_TYPE_SET_VAR = 6;

    int TEXT_MATCH_TYPE_CONTAINS = 0;
    int TEXT_MATCH_TYPE_STARTSWITH = 1;
    int TEXT_MATCH_TYPE_ENDSWITH = 2;

    String TEXT_MATCH_TYPE_NAME_CONTAINS = "contains";
    String TEXT_MATCH_TYPE_NAME_STARTSWITH = "starts-with";
    String TEXT_MATCH_TYPE_NAME_ENDSWITH = "ends-with";

    int VERIFY_LOGIN = 1;
    int VERIFY_ELEMENT_SEARCH = 2;
    int VERIFY_VALUE_EQUALS = 3;
    int VERIFY_VALUE_CONTAIN = 4;
    int VERIFY_ELEMENT_SEARCH_WAIT = 5;
    int VERIFY_ELEMENT_EXIST = 6;
    int VERIFY_LOGOUT = 7;

    String HOST = "http://192.168.200.103";
    String ONLINE_HOST = "http://staging.yunniao.me";
    String HOST_101 = "http://192.168.200.101";
    String HOST_195 = "http://192.168.200.195";
    int SAN_TI_PORT = 7500;
    int BASI_API_PORT = 7000;
    int CUSTOMER_API_PORT = 6500;
    int OPEN_API_PORT = 10499;
    String PACKAGE = "package";
    String ACTIVITY = "activity";
    String ACTIVITY_LOGIN = "loginActivity";

    String PACKAGE_CUSTOMER = "com.yunniaohuoyun.customer";
    String PACKAGE_CUSTOMER_ACTIVITY = ".ui.activity.StartActivity";
    String PACKAGE_CUSTOMER_LOGIN_ACTIVITY = ".ui.activity.LoginActivity";

    String PACKAGE_XIAZAI = "com.yunniao.android.libsamples.x";
    String PACKAGE_XIAZAI_ACTIVITY = "com.yunniao.android.libsamples.MainActivity";

    String PACKAGE_S = "com.yunniaohuoyun.driver";
    String PACKAGE_S_ACTIVITY = ".ui.LoadingActivity";
    String PACKAGE_DRIVER_LOGIN_ACTIVITY = ".ui.LoginActivity";

    String PACKAGE_F = "com.yunniao.fcc";
    String PACKAGE_F_ACTIVITY = ".ui.LoadingActivity";
    String PACKAGE_F_LOGIN_ACTIVITY = ".ui.LoginActivity";

    String PACKAGE_Y = "com.yunniaohuoyun.admin";
    String PACKAGE_Y_ACTIVITY = ".ui.StartActivity";
    String PACKAGE_Y_LOGIN_ACTIVITY = ".ui.LoginActivity";


    String PACKAGE_CUSTOMER_IOS = "me.yunniao.YNBusiness";
    String PACKAGE_S_IOS = "me.yunniao.Driver";


    String APP_FILE_CUSTOMER_IOS = "YNBusiness.ipa";
    String APP_FILE_S_IOS = "YNDriver.ipa";
    String APP_FILE_XIAZAI_ANDROID = "down.apk";
    String APP_FILE_CUSTOMER_ANDROID = "AndroidB3V1.apk";
    String APP_FILE_S_ANDROID = "AndroidSMaster.apk";
    String APP_FILE_Y_ANDROID = "AndroidYMaster.apk";
    String APP_FILE_F_ANDROID = "AndroidFMaster.apk";
    String APP_FILE = "app_file";

    String APP_PATH_CUSTOMER_IOS = "YNBusiness.app";
    String APP_PAHT_DRIVER_IOS = "YNDriver.app";
    String APP_PATH = "app_path";


    String DEVICE_NAME = "device_name";
    String DEVICE_NAME_SIMULATOR = "device_name_simulator";
    String DEVICE_ANDROID_NAME = "Android Emulator";
    String DEVICE_IOS_NAME = "test-iPhone6";
    String DEVICE_IOS_NAME_SIMULATOR = "iPhone 6";
    String OS_ANDROID = "android";
    String OS_IOS = "ios";
    String OS_NAME = "os_name";
    String PLATFORM_VERSION_IOS = "9.2";
    String PLATFORM_VERSION_IOS_SIMULATOR = "8.4";

    String PLATFORM_VERSION_ANDROID = "4.4";
    String PLATFORM_VERSION = "platform_version";
    String PLATFORM_VERSION_SIMULATOR = "platform_version_simulator";


    String IOS_ELEMENT_TEXT_TYPE_LABEL = "label";
    String IOS_ELEMENT_TEXT_TYPE_VALUE = "value";
    String IOS_ELEMENT_TEXT_TYPE_NAME = "name";
    String ANDROID_ELEMENT_TEXT_TYPE_TEXT = "text";
    String ANDROID_ELEMENT_TYPE_ID = "resource-id";
    String ANDROID_ELEMENT_TYPE_DESC = "content-desc";

    String REPORT_NAME = "reportName";

    int SWIPE_DURATION_DEFAULT = 500;
    int WAIT_DURATION_DEFAULT = 10;


}
