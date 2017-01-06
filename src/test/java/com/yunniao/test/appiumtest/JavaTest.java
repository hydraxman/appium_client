package com.yunniao.test.appiumtest;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yunniao.appiumtest.ApiTest;
import com.yunniao.appiumtest.utils.LogUtil;
import org.junit.Test;

import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

/**
 * Created by MrBu on 2016/3/26.
 */
public class JavaTest {
	@Test
	public void allocateElements() {
		int numElements = 54687413;
		int initialCapacity = 10;
		// Find the best power of two to hold elements.
		// Tests "<=" because arrays aren't kept full.
		initialCapacity = numElements;
		initialCapacity |= (initialCapacity >>> 1);
		System.out.println(initialCapacity);
		initialCapacity |= (initialCapacity >>> 2);
		System.out.println(initialCapacity);
		initialCapacity |= (initialCapacity >>> 4);
		System.out.println(initialCapacity);
		initialCapacity |= (initialCapacity >>> 8);
		System.out.println(initialCapacity);
		initialCapacity |= (initialCapacity >>> 16);
		initialCapacity++;

		if (initialCapacity < 0)   // Too many elements, must back off
			initialCapacity >>>= 1;// Good luck allocating 2 ^ 30 elements
		System.out.println(initialCapacity);
	}

	private static final int MIN_CAPACITY_INCREMENT = 12;

	@Test
	public void listCapacity() {
		int s = 102;
		int newSize = s + (s < (MIN_CAPACITY_INCREMENT / 2) ? MIN_CAPACITY_INCREMENT : s >> 1);
		System.out.println(newSize);
	}

	@Test
	public void testMd5() throws IOException {
		ArrayList<String> headerKeys = new ArrayList<String>(Arrays.asList(new String[]{
				"x-cli-ch",
				"sessionid",
				"x-cli-ver",
				"x-cli-model",
				"x-cli-imei",
				"x-cli-os",
				"version",
				"channel"
		}));
		File f = new File("beeper_api线上log-0421.txt");
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(f), Charset.forName("utf-8")));
		String line;
		String headers;
		String body;
		String oneKey;
		String[] content;
		while ((line = bufferedReader.readLine()) != null) {
			content = line.split("headers : ")[1].split(",body : ");
			headers = content[0];
			body = content[1];
			JSONObject headersObject = JSON.parseObject(headers);
			String timestamp = headersObject.getString("timestamp");
			if(timestamp==null||timestamp.length()<13){
				continue;
			}
			JSONObject bodyObject = JSON.parseObject(body);
			Iterator<String> iterator = headersObject.keySet().iterator();
			JSONObject params = new JSONObject();
			while (iterator.hasNext()) {
				oneKey = iterator.next();
				if (headerKeys.contains(oneKey)) {
					params.put(oneKey, headersObject.get(oneKey));
				}
			}
			iterator = bodyObject.keySet().iterator();
			while (iterator.hasNext()) {
				oneKey = iterator.next();
				if(oneKey.endsWith("sign")){
					continue;
				}
				params.put(oneKey, headersObject.get(oneKey));
			}
			ApiTest at = new ApiTest();
			String sign = at.getSign(params, timestamp);
			String signSrc = bodyObject.getString("sign");
			LogUtil.i("得出："+sign+",实际："+signSrc+",timestamp:"+timestamp);
		}
	}
}
