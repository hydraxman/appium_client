package com.yunniao.appiumtest;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.yunniao.appiumtest.bean.ResultOutPut;
import com.yunniao.appiumtest.bean.TestCase;
import com.yunniao.appiumtest.ui.MainUI;
import com.yunniao.appiumtest.utils.*;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.*;

/**
 * Created by MrBu on 2016/1/12.
 */
public class ClientStarter{
	public static String url = "127.0.0.1:4723";
	public static IntegrationTest currentTest;
	public static IntegrationTest currentTestApi;
	public static IntegrationTest currentTestAndroid;
	public static IntegrationTest currentTestIOS;
	public static boolean inWindowMode = false;
	public static ClientStarter instance = null;
	public static Integer isQuit = 1;
	public static String reportName = "report";
	private static MainUI mUI;
	public static void main(String[] args) {
		ClientStarter clientStarter = new ClientStarter();
		instance = clientStarter;
		reportName = PhotoUtil.setFile();
		KeyValueUtil.put(Constants.REPORT_NAME, reportName);
		if (args != null && args.length > 0) {
			commandMode(args);
		} else {
			try {
				windowMode();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private static void windowMode() throws Exception {
		inWindowMode = true;
		mUI=new MainUI();
		mUI.setVisible(true);
	}

	private static void commandMode(String[] args) {
		inWindowMode=false;
		File file = new File(args[0]).getAbsoluteFile();
		String udid = null;
		if (args.length > 1) {
			url = args[1];
			LogUtil.i(url);
		}
		if (args.length > 2) {
			udid = args[2];
			LogUtil.i(udid);
		}

		if (args.length > 3) {
			isQuit = Integer.parseInt(args[3]);
			LogUtil.i(Integer.toString(isQuit));
		}
		try {
			if (file.isDirectory()) {
				ArrayList<File> fileList = getMultiJsonFiles(file);
				jsonMultiCase(fileList, url, udid);
			} else if (file.exists() && isJsonFile(file)) {
				jsonCase(ParseUtil.readFile(file), url, udid, isQuit);
			} else{
				throw new RuntimeException("文件不存在或不是json类型");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	private static void jsonCase(String sJson, String ipAddrLocal, String udid, Integer isQuit) {
		TestCase testCase = null;
		try {
			testCase = JSON.parseObject(sJson, TestCase.class);
			startClientCase(testCase, ipAddrLocal, udid, isQuit);
		} catch (JSONException e) {
			LogUtil.e("json文件解析错误!可能是文件格式错误,某个key的value类型不对!");
		}
	}

	private static void jsonMultiCase(ArrayList<File> fileList, String ipAddrLocal, String udid) {
		TestCase testCase = null;
		try {
			ArrayList<TestCase> caseList = new ArrayList<>();
			for (File ff : fileList) {
				testCase = JSON.parseObject(ParseUtil.readFile(ff), TestCase.class);
				caseList.add(testCase);
			}
			startClientMultiCase(caseList, ipAddrLocal, udid, isQuit);
		} catch (JSONException e) {
			LogUtil.e("json文件解析错误!可能是文件格式错误,某个key的value类型不对!");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void startClientCase(TestCase testCase, String urlAddrLocal, String udid, Integer isQuit) {
		try {
			if(("api").equalsIgnoreCase(testCase.getProduct())) {
				if(currentTestApi != null) {
					throw new Exception("正在运行中。。。请先stop当前任务");
				} else {
					currentTestApi = new IntegrationApiTest(testCase, urlAddrLocal, udid, isQuit);
					currentTest = currentTestApi;
					currentTestApi.start();
				}
			} else if ((Constants.OS_IOS).equalsIgnoreCase(testCase.getOs())) {
				if (currentTestIOS != null) {
					currentTestIOS.testCase = testCase;
					currentTestIOS.urlAddrLocal = urlAddrLocal;
					currentTestIOS.udid = udid;
				} else {
					currentTestIOS = new IntegrationIosTest(testCase, urlAddrLocal, udid, isQuit);
				}
				currentTest = currentTestIOS;
				currentTestIOS.start();
			} else {
				if (currentTestAndroid != null) {
					currentTestAndroid.testCase = testCase;
					currentTestAndroid.urlAddrLocal = urlAddrLocal;
					currentTestAndroid.udid = udid;
				} else {
					currentTestAndroid = new IntegrationAndroidTest(testCase, urlAddrLocal, udid, isQuit);
				}
				currentTest = currentTestAndroid;
				currentTestAndroid.start();
			}
		} catch (Exception e) {
			LogUtil.e(e);
		}
	}

	private static java.util.List<TestCase> multiCases = null;
	private static int multiCasesIndex = 0;
	private static String multiCasesUrl = null;
	private static String multiCasesUdid = null;

	private static void startClientMultiCase(java.util.List<TestCase> caseList, String urlAddrLocal, String udid, Integer isQuit) {
		multiCases = caseList;
		multiCasesUrl = urlAddrLocal;
		multiCasesUdid = udid;
		multiCasesIndex = 0;
		startClientCase(caseList.get(0), multiCasesUrl, multiCasesUdid, isQuit);
	}
	public ClientStarter() {
		KeyValueUtil.put("json-result-start", Integration.timeStamp());
	}
	private static ArrayList<File> getMultiJsonFiles(File f) {
		ArrayList<File> finalFileList = new ArrayList<File>();
		dealWithDirFile(f, finalFileList);
		if (finalFileList.size() <= 0) {
			tip("所选的文件夹中没有有效的json文件");
		}
		return finalFileList;
	}

	private static void dealWithDirFile(File f, ArrayList<File> finalFileList) {
		File[] files = f.listFiles(new FileFilter() {
			@Override
			public boolean accept(File file) {
				return file.isDirectory() || isJsonFile(file);
			}
		});
		ArrayList<File> currentList = new ArrayList<File>(Arrays.asList(files));
		Collections.sort(currentList, new Comparator<File>() {
			@Override
			public int compare(File f1, File f2) {
				return f1.getName().compareTo(f2.getName());
			}
		});
		for (File ff : currentList) {
			if (isJsonFile(ff)) {
				finalFileList.add(ff);
			} else if (ff.isDirectory()) {
				dealWithDirFile(ff, finalFileList);
			}
		}
	}

	private static boolean isJsonFile(File file) {
		return file != null && file.getName().endsWith(".json");
	}

	public File[] getFiles() {
		File f = new File("testdata");
		if (f.exists()) {
			return f.listFiles();
		}
		return null;
	}

	public void log(String log) {
		if (inWindowMode) {
			mUI.log(log);
		} else {
			System.out.print(log);
		}
	}

	public static void tip(String log) {
		if (inWindowMode) {
			mUI.tip(log);
		} else {
			System.out.print(log);
		}
	}

	public void onTestStart() {
		if(inWindowMode) {
			mUI.onTestStart();
		}
	}

	public void onTestStop(boolean stopAll) throws IOException{
		currentTestApi = null;
		if(isQuit != 2) {
			currentTestIOS = null;
			currentTestAndroid =  null;
		}
		if(inWindowMode) {
			mUI.onTestStop(stopAll);
		}
		checkMultiCase(stopAll);
	}

	/**
	 * 检查是否执行完所有的case
	 *
	 * @param stopAll
	 */
	private void checkMultiCase(boolean stopAll) throws IOException{
		if (stopAll) {
			onAllStop();
			return;
		}
		if (multiCases != null && multiCasesIndex < multiCases.size() - 1) {
			multiCasesIndex++;
			startClientCase(multiCases.get(multiCasesIndex), multiCasesUrl, multiCasesUdid, isQuit);
		} else {
			onAllStop();
		}

	}

	private void onAllStop() throws IOException {
		multiCasesIndex = 0;
		multiCases = null;
		setReport();

		CommonUtil.zipReport();
//		CommonUtil.scpZip();
		KeyValueUtil.clear();
	}

	public void startTestFromUI(File f, String url, String udid) throws IOException {
		ClientStarter.url=url;
		if (f.isDirectory()) {
			ArrayList<File> fileList = getMultiJsonFiles(f);
			jsonMultiCase(fileList, url, udid);
		} else {
			jsonCase(ParseUtil.readFile(f), url, udid, isQuit);
		}
	}

	private void setReport() throws IOException {
		LogUtil.stopFileLog();
		String fileName = "Report"+ KeyValueUtil.get("logName");
		LogUtil.i("app自动化Report:", true, fileName);
		int endTime = Integer.parseInt(Integration.timeStamp());
		String starttime = KeyValueUtil.get("json-result-start");
		int startTime = Integer.parseInt(starttime);
		int time = endTime - startTime;
		ArrayList<ResultOutPut> results = KeyValueUtil.get("json-result");
		int errorCount = 0;
		ArrayList<Object> resultsAll = new ArrayList<>();
		String name = null;
		ArrayList<ResultOutPut> subResults = new ArrayList<>();
		int size1 = results.size();
		for (int j=0; j < size1; j++) {
			ResultOutPut b = results.get(j);
			if(name == null) {
				name = b.getResultName();
			}
			if(name.equals(b.getResultName())) {
				subResults.add(b);
			} else {
				resultsAll.add(subResults);
				name = b.getResultName();
				subResults = new ArrayList<>();
				subResults.add(b);
			}
		}
		if(size1 > 0) {
			resultsAll.add(subResults);
		}

		for (int i=0,size = resultsAll.size(); i < size; i++) {
			ArrayList<ResultOutPut> subResult = (ArrayList<ResultOutPut>) resultsAll.get(i);
			String flag = "passed";
			for(int j=0,subsize = subResult.size(); j < subsize; j++) {
				name = subResult.get(j).getResultName();
				if(subResult.get(j).getResultStatus() == 2) {
					flag = "fail";
					errorCount++;
					break;
				}
			}
			LogUtil.i(i + " " + name + " " + flag, true, fileName);
			for(int j=0,subsize = subResult.size(); j < subsize; j++) {
				LogUtil.i(" " + subResult.get(j).getResultDesc() + " " + subResult.get(j).getResultString(), true, fileName);
			}
		}
		LogUtil.i("run cases :" + resultsAll.size() + ", " + errorCount + " fail, " + (resultsAll.size() - errorCount) + " passed.", true, fileName);

//		for (int i = 0, size = results.size(); i < size; i++) {
//			ResultOutPut a = results.get(i);
//
//			if(a.getResultStatus() == 2) {
//				errorCount++;
//			}
//			LogUtil.i(i + a.getResultDesc());
//			LogUtil.i(a.getResultString());
//		}
//		LogUtil.i("run cases :" + results.size() + ", " + errorCount + " fail, " + (results.size() - errorCount) + " success.");
		LogUtil.i("run times :" + time + "s", true, fileName);
		LogUtil.stopFileLog();
	}
}
