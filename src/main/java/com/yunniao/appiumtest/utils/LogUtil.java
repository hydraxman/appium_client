package com.yunniao.appiumtest.utils;

import com.yunniao.appiumtest.ClientStarter;
import com.yunniao.appiumtest.Constants;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * Created by MrBu on 2016/1/13.
 */
public class LogUtil {
	private static BufferedWriter br;
	private static boolean running = false;
	public static void i(String msg) {
		i(msg, false);
	}
	public static void i(String msg, boolean toConsole) {
		if (!running) {
			try {
				startFileLog("log");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (toConsole) {
			printToConsole(msg);
		}
		printLog(msg, "Info");
	}


	public static void i(String msg, String fileName) {
		i(msg, true, fileName);
	}
	public static void i(String msg, boolean toConsole, String fileName) {
		if (!running) {
			try {
				startFileLog(fileName);
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
				startFileLog("log");
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
				startFileLog("log");
			} catch (Exception eCatch) {
				eCatch.printStackTrace();
			}
		}
		printToConsole(e);
		printLog(e, "error");
	}

	static synchronized void startFileLog(String fileName) throws Exception {
		if(running ){
			return;
		}
		String dateTime = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(new Date(
				System.currentTimeMillis()));
		String reportName = KeyValueUtil.get(Constants.REPORT_NAME);
		File logDir = new File("report/" + reportName + "/log").getAbsoluteFile();
		if (!logDir.exists()) {
			if(logDir.mkdir()){
				printToConsole("log文件夹创建成功");
			}
		}
		Random rand = new Random();
		int randNum = 100000+rand.nextInt(899999);
		File f = new File(logDir,fileName + dateTime + String.valueOf(randNum)+".txt").getAbsoluteFile();
		if (!f.exists()) {
			if(f.createNewFile()){
				printToConsole("log文件创建成功");
			} else {
				throw new Exception("文件创建失败");
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
			String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
			br.write(tag + " : " + date + ":" + line );
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
				printToConsole("log 文件end");
				br.close();
				br = null;
			}
		}
	}
}
