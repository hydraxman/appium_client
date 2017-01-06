package com.yunniao.appiumtest.utils;

import com.google.common.base.Function;
import io.appium.java_client.ios.IOSDriver;

/**
 * Created by melinda on 1/28/16.
 */
public interface IOSCondition<T>  extends Function<IOSDriver, T> {

}
