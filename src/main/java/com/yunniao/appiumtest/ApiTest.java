package com.yunniao.appiumtest;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.squareup.okhttp.*;
import com.squareup.okhttp.internal.http.RealResponseBody;
import com.yunniao.appiumtest.bean.Time;
import com.yunniao.appiumtest.utils.KeyValueUtil;
import com.yunniao.appiumtest.utils.LogUtil;
import okio.BufferedSink;
import okio.BufferedSource;
import okio.Okio;
import org.apache.commons.lang3.ArrayUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

/**
 * Created by melinda on 3/14/16.
 */
public class ApiTest {
    private ArrayList<String> headerKeys;

    public ApiTest() {
        headerKeys = new ArrayList<String>(Arrays.asList("x-cli-ch",
                "sessionid",
                "x-cli-ver",
                "x-cli-model",
                "x-cli-imei",
                "x-cli-os",
                "version",
                "channel"));
    }

    /**
     * 获取md5
     *
     * @param text
     * @return
     */
    public static String getMD5(String text) {
        byte[] hash;
        try {
            hash = MessageDigest.getInstance("md5").digest(text.getBytes(StandardCharsets.UTF_8));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Huh, MD5 should be supported?", e);
        }

        StringBuilder hex = new StringBuilder(hash.length * 2);
        for (byte b : hash) {
            if ((b & 0xFF) < 0x10) hex.append("0");
            hex.append(Integer.toHexString(b & 0xFF));
        }
        return hex.toString();

    }

    public void getapi(String url, int port, String method, JSONObject params, JSONObject vars, boolean ignore) throws Exception {

        String hostUrl = getHostUrl();
        url = hostUrl + ":" + port + url;
        Response response = null;

        try {
            params = paramVars(params);
            if (("POST").equalsIgnoreCase(method)) {
                response = post(url, params);
            } else if (("GET").equalsIgnoreCase(method)) {
                response = get(url, params);
            }
        } catch (Exception e) {
            if (!ignore) {
                throw new Exception(e.getMessage());
            }
        }

        LogUtil.i(response.toString());
        String body = response.body().string();
        JSONObject object = JSON.parseObject(body);
        LogUtil.i(object.toString());
        int code = object.getInteger("code");
        if (response.isSuccessful() & code == 0) {
            try {
                vars(vars, object);
            } catch (Exception e) {
                if (!ignore) {
                    throw new Exception("api返回数据有问题" + e.getMessage());
                }
            }
        } else {
            if (!ignore) {
                throw new Exception("Unexpected code " + object.getString("msg") + response.toString());
            }
        }
    }

    public String getHostUrl() {
        String host = KeyValueUtil.get("host");
        String hostUrl = Constants.HOST;
        if ((Constants.ONLINE_HOST).equals(host)) {
            hostUrl = Constants.ONLINE_HOST;
        } else if ((Constants.HOST_101).equals(host)) {
            hostUrl = Constants.HOST_101;
        } else if ((Constants.HOST_195).equals(host)) {
            hostUrl = Constants.HOST_195;
        }
        return hostUrl;
    }

    //处理传递的参数
    public JSONObject paramVars(JSONObject params) throws Exception {
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
                if (originValue.contains("$md5(")) {
                    //md5
                    String subStr = originValue.replace("$md5(", "");
                    String vaule = getMD5(subStr.substring(0, subStr.length() - 1));
                    params.put(key, vaule);
                } else if (originValue.contains("$")) {//获取内存里的数据
                    String vaule = KeyValueUtil.get(originValue.replace("$", ""));
                    if (key.equals("customer_id")) {
                        params.put(key, Integer.parseInt(vaule));
                    } else {
                        params.put(key, vaule);
                    }
                }
            }
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }

        return params;
    }

    public void vars(JSONObject results, JSONObject object) throws Exception {
        if (results != null) {
            String Value;
            String Key;
            Set<String> resultStrings = results.keySet();
            for (String key : resultStrings) {
                Key = results.getString(key).replace("$", "");
                if (("info").equalsIgnoreCase(key)) {
                    Value = object.getString(key);
                } else {
                    String[] keys = key.split(":");
                    Value = getValue(keys, object.getJSONObject("info"));
                }
                LogUtil.i("Key=" + Key + ";Value=" + Value);
                KeyValueUtil.put(Key, Value);
            }
        }
    }

    public String getValue(String[] keys, JSONObject infoObject) {
        int length = keys.length;
        String value = null;
        for (int i = 0; i < length; i++) {
            String keyString = keys[i];
            if (i == length - 1) {
                value = infoObject.getString(keyString);
            } else {
                String[] a = ArrayUtils.removeElement(keys, keyString);
                String[] keyArray = keyString.split("\\|");
                Object b = infoObject.get(keyArray[0]);
                JSONObject bObject = null;
                if (b instanceof JSONArray) {
                    JSONArray bArray = infoObject.getJSONArray(keyArray[0]);
                    String[] where = keyArray[1].split(";");
                    boolean isTrue = true;
                    for (int j = 0, size = bArray.size(); j < size; j++) {
                        isTrue = true;
                        bObject = (JSONObject) bArray.get(j);
                        for (int k = 0, ksize = where.length; k < ksize; k++) {
                            String whereString = where[k];
                            String[] whereArray = whereString.split("=");
                            if (("index").equals(whereArray[0])) {//index的话为数组索引
                                int index = Integer.parseInt(whereArray[1]);
                                bObject = (JSONObject) bArray.get(index);
                                isTrue = true;
                            } else {
                                String whereValue = whereArray[1];
                                if (whereValue.contains("$")) {
                                    String whereKey = whereValue.replace("$", "");
                                    whereValue = KeyValueUtil.get(whereKey);
                                }
                                if (whereValue != null) {
                                    boolean isEqual = (bObject.getString(whereArray[0])).equals(whereValue);
                                    isTrue = isTrue & isEqual;
                                } else {
                                    isTrue = isTrue & false;
                                }

                            }
                        }
                        if (isTrue) {
                            break;
                        }
                    }
                    if (!isTrue) {
                        bObject = null;
                    }
                } else {
                    bObject = (JSONObject) b;
                }
                return getValue(a, bObject);
            }
        }
        return value;
    }

    public Response get(String url, JSONObject params) throws IOException {
        OkHttpClient client = new OkHttpClient();

        url += "?" + signParams(params, params);

        Request request = new Request.Builder()
                .url(url)
                .get()
                .addHeader("content-type", "application/json")
                .addHeader("cache-control", "no-cache")
                .build();

        Response response = client.newCall(request).execute();
        return response;
    }

    public Response post(String url, JSONObject params) throws IOException {

        JSONObject urlParams = new JSONObject();
//        url += "?" + signParams(params, urlParams);

        OkHttpClient client = new OkHttpClient();
//        client.interceptors().add(new LoggerInterceptor());
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, params.toJSONString());
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .addHeader("content-type", "application/json")
                .addHeader("cache-control", "no-cache")
                .build();

        Response response = client.newCall(request).execute();
        return response;
    }

    public Response post1(String url, JSONObject params) throws IOException {

        JSONObject urlParams = new JSONObject();
        url += "?" + signOpenParams(params, urlParams);

        OkHttpClient client = new OkHttpClient();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, params.toJSONString());
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .addHeader("content-type", "application/json")
                .addHeader("cache-control", "no-cache")
                .build();

        Response response = client.newCall(request).execute();
        return response;
    }

    public String setUrlParams(JSONObject params) {
        String paramString = "";
        params = setPublicParams(params);
        Set<String> strings = params.keySet();
        for (String key : strings) {
            paramString += key + "=";
            paramString += params.getString(key);
            paramString += "&";
        }
        paramString = paramString.substring(0, paramString.length() - 1);

        return paramString;
    }

    public String signOpenParams(JSONObject params, JSONObject urlParams) {
        String token = "fc09df92837834175a9c61216d64ce56";
        String sign = getOpenSign(params, token);
        urlParams.put("source_name", "melindae");
        urlParams.put("sk", sign);
        String url = setUrlParams(urlParams);

        return url;
    }

    public String signParams(JSONObject params, JSONObject urlParams) {
//        String timeStamp = Integration.timeStampMin();
//        String sign = getSign(params, timeStamp);
//
//        urlParams.put("timestamp", timeStamp);
//        urlParams.put("sign", sign);
//        JSONArray value = new JSONArray();
//        value.add(sign);
//        value.add(sign);
//        params.put("sign", value);
        String url = setUrlParams(urlParams);

        return url;
    }

    /**
     * @param headersObject 司机端的请求头，需要包含timestamp字段，不然无法算出sign
     * @param bodyObject
     * @return
     */
    public String getSignForDriverApi(JSONObject headersObject, JSONObject bodyObject) {
        String timestamp = headersObject.getString("timestamp");
        String oneKey;
        if (timestamp == null || timestamp.length() < 13) {
            return null;
        }
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
            if (oneKey.endsWith("sign")) {
                continue;
            }
            params.put(oneKey, headersObject.get(oneKey));
        }
        return getSign(params, timestamp);
    }

    public String getOpenSign(JSONObject params, String token) {
//        JSONObject a = (JSONObject) params.clone();
        ArrayList<String> strings = new ArrayList<>(params.keySet());
        String paramString = "";
        for (String key : strings) {
            paramString += key + "=";
            Object obj = params.get(key);
            if (obj == null || obj instanceof JSONArray || obj instanceof JSONObject) {
                continue;
            }
            paramString += obj.toString();
            paramString += "&";
        }
        paramString += "token=" + token;
        String md5String = getMD5(paramString);
        return md5String;
    }

    public String getSign(JSONObject params, String timeStamp) {
        JSONObject a = (JSONObject) params.clone();
        a = setPublicParams(a);
        ArrayList<String> strings = new ArrayList<>(a.keySet());
        Collections.sort(strings);
        String paramString = "";
        for (String key : strings) {
            paramString += key + "=";
            Object obj = a.get(key);
            if (obj == null || obj instanceof JSONArray || obj instanceof JSONObject) {
                continue;
            }
            paramString += obj.toString();
            paramString += "^";
        }
        paramString = paramString.substring(0, paramString.length() - 1);
        paramString += timeStamp.substring(1, timeStamp.length() - 1);
        String md5String = getMD5(paramString);
        return md5String;
    }

    public JSONObject setPublicParams(JSONObject params) {
//        params.put("channel", "rel");
//        params.put("osname", "Android");
//        params.put("osversion", "4.1.1");
//        params.put("model", "Google");
//        params.put("udid", "5284047f-4ffb-3e04-824a-2fd1d1f0cd62");
//        params.put("devtoken", "5284047f-4ffb-3e04-824a-2fd1d1f0cd62");
//        params.put("user_phone_name", "tel123");
//        params.put("version", 300010000);
        return params;
    }

    public JSONObject setDriverPublicParams(JSONObject params) {
        params.put("version", "200090000");
        params.put("x-cli-ch", "rel");
        params.put("x-cli-ver", "200090000");
        params.put("x-cli-model", "huawei");
        params.put("x-cli-imei", 73284342);
        params.put("x-cli-os", "Android4.4");
        params.put("sessionid", "10123456789_J7N0LyoK8z");
        return params;
    }

    /**
     * 用于全面的打log
     */
    static class LoggerInterceptor implements Interceptor {

        public static final int MAX_CONTENT_SIZE = 50 * 1024;//最大打印100kb的数据
        private static final String LOGGER_TAG = "oh yeah ";

        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            RequestBody reqBody = request.body();
            LogUtil.i(LOGGER_TAG + "用户请求头：" + request.headers().toString());
            if (reqBody != null) {
                long contentLength = reqBody.contentLength();
                if (contentLength < MAX_CONTENT_SIZE) {
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    BufferedSink buffer = Okio.buffer(Okio.sink(out));
                    reqBody.writeTo(buffer);
                    buffer.flush();
                    LogUtil.i(LOGGER_TAG + request.url().toString());
                    LogUtil.i(LOGGER_TAG + ",请求体：" + new String(out.toByteArray(), StandardCharsets.UTF_8));
                    buffer.close();
                } else {
                    LogUtil.i(LOGGER_TAG + request.url().toString() + ",请求体长度大于" + MAX_CONTENT_SIZE + "byte");
                }
            } else {
                LogUtil.i(LOGGER_TAG + request.url().toString() + "  ::" + request.method() + "请求");
            }
            Response response = chain.proceed(request);
            ResponseBody respBody = response.body();
            LogUtil.i(LOGGER_TAG + "成功返回：响应码：" + response.code() + " 响应头：" + response.headers().toString());
            if (respBody != null) {
                long contentLength = respBody.contentLength();
                LogUtil.i(LOGGER_TAG + "成功返回响应体长度：" + contentLength + "  响应类型：" + respBody.contentType());
                if (contentLength < MAX_CONTENT_SIZE) {
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    BufferedSink buffer = Okio.buffer(Okio.sink(out));
                    respBody.source().readAll(buffer);
                    buffer.flush();
                    LogUtil.i(LOGGER_TAG + "成功返回响应体：" + new String(out.toByteArray(), StandardCharsets.UTF_8));
                    BufferedSource source = Okio.buffer(Okio.source(new ByteArrayInputStream(out.toByteArray())));
                    response = response.newBuilder().body(new RealResponseBody(response.headers(), source)).build();
                } else {
                    LogUtil.i(LOGGER_TAG + "返回内容大小超过" + MAX_CONTENT_SIZE + "byte");
                }
            }
            return response;
        }
    }
}
