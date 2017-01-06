package com.yunniao.appiumtest.utils;

import com.yunniao.appiumtest.ClientStarter;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by melinda on 1/28/16.
 */
public class ErrorLogUtil {
    private static BufferedWriter br;
    private static boolean running = false;

    public static void i(String msg) {
        i(msg, false);
    }

    public static void i(String msg, boolean toConsole) {
        if (!running) {
            try {
                startFileLog();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (toConsole) {
            printToConsole(msg);
        }
        printLog(msg, "Info");
    }

    private static void printToConsole(String msg) {
        if (ClientStarter.instance != null && ClientStarter.instance.inWindowMode) {
            ClientStarter.instance.log(msg);
        } else {
            System.out.println(msg);
        }
    }

    private static void printToConsole(Exception e) {
        String s = exception2Str(e);
        printToConsole(s);
    }

    private static String exception2Str(Exception e) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintWriter writer = new PrintWriter(out);
        try {
            e.printStackTrace(writer);
            writer.flush();
            return new String(out.toByteArray(), "UTF-8");
        } catch (UnsupportedEncodingException e1) {
            return "";
        }
    }

    public static void e(String msg) {
        if (!running) {
            try {
                startFileLog();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        printToConsole(msg);
        printLog(msg, "error");
    }

    public static void e(Exception e) {
        if (!running) {
            try {
                startFileLog();
            } catch (Exception eCatch) {
                eCatch.printStackTrace();
            }
        }
        printToConsole(e);
        printLog(e, "error");
    }

    static synchronized void startFileLog() throws Exception {
        if(running ){
            return;
        }
        String dateTime = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date(
                System.currentTimeMillis()));
        File logDir = new File("log").getAbsoluteFile();
        if (!logDir.exists()) {
            if(logDir.mkdir()){
                printToConsole("log文件夹创建成功-error");
            }
        }
        File f = new File(logDir,"error" + dateTime+".txt").getAbsoluteFile();
        if (!f.exists()) {
            if(f.createNewFile()){
                printToConsole("log文件创建成功-error");
            }else{
                throw new Exception("文件创建失败-error");
            }
        }
        br = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(f),
                "UTF-8"));

        running = true;
    }

    public static void printLog(String line, String tag) {
        if (br == null || !running) {
            return;
        }
        try {
            br.write(tag + " : " + line);
            br.newLine();
            br.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void printLog(Exception e, String tag) {
        printLog(exception2Str(e), tag);
    }

    /**
     * 在程序退出时调用
     */
    public static void stopFileLog() throws IOException {
        if (running) {
            running = false;
            if (br != null) {
                br.close();
                br = null;
            }
        }
    }
}
