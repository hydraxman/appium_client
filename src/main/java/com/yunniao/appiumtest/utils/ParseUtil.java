package com.yunniao.appiumtest.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yunniao.appiumtest.Constants;
import com.yunniao.appiumtest.bean.TestCase;

import java.io.*;

/**
 * Created by MrBu on 2016/1/19.
 */
public class ParseUtil {
	public static JSONObject readFileToJson(File file) throws IOException {
		return JSON.parseObject(readFile(file));
	}
	public static TestCase readFileToObj(File file) throws IOException {
		return JSON.parseObject(readFile(file), TestCase.class);
	}
	public static String readFile(File file) throws IOException {
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(file), Constants.ENCODING));
		StringBuilder stringBuffer = new StringBuilder();
		String line = null;
		while ((line = bufferedReader.readLine()) != null) {
			stringBuffer.append(line);
		}
		bufferedReader.close();
		return stringBuffer.toString();
	}
}
