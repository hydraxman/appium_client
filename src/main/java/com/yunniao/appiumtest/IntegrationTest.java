package com.yunniao.appiumtest;

import com.alibaba.fastjson.JSONObject;
import com.yunniao.appiumtest.bean.*;
import com.yunniao.appiumtest.utils.*;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.SessionNotCreatedException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @param <E>
 * @param <T>
 */
public abstract class IntegrationTest<E extends MobileElement,T extends AppiumDriver<? extends MobileElement>> implements IIntegrationTest {
	protected String urlAddrLocal;
	protected TestCase testCase;
	protected String udid;
	public String resultMsg;
	public String browserName;
	public int resultStatus;
	protected HashMap<String, String> osInfo;
	protected Thread currentThread;
	protected T driver;
	protected List<E> mElements;
	protected Integer isQuit;
	public IntegrationTest(TestCase testCase, String urlAddrLocal, String udid, Integer isQuit) {
		this.testCase = testCase;
		this.urlAddrLocal = urlAddrLocal;
		this.udid = udid;
		if(udid == null) {
			this.udid = testCase.getUdid();
		}
		this.isQuit = isQuit;
		this.browserName = testCase.getBrowserName();
	}

	protected void notifyStart() {
		try {
			ClientStarter.instance.onTestStart();
		} catch (Exception ignored) {
		}
	}

	protected void notifyStop(boolean stopAll) {
		try {
			ClientStarter.instance.onTestStop(stopAll);
		} catch (Exception e) {
			LogUtil.i(e.getMessage());
		}
	}

	public void stopDriver(boolean stopAll) throws IOException {
		if (currentThread != null) {
			currentThread.interrupt();
		}
		ArrayList<ResultOutPut> results = KeyValueUtil.get("json-result");
		if (results == null) {
			results = new ArrayList<>();
		}
		ResultOutPut result = new ResultOutPut();
		result.setResultDesc(testCase.getDesc());
		result.setResultStatus(resultStatus);
		result.setResultString(resultMsg);
		result.setResultName(testCase.getName());
		results.add(result);
		KeyValueUtil.put("json-result", results);
		notifyStop(stopAll);
	}
	public void setOperations(ArrayList<Operation> operations) throws Exception {
		for (int i = 0, size = operations.size(); i < size; i++) {
			Operation operation = operations.get(i);
			LogUtil.i("OPERATION STEP " + (i + 1) + ": " + operation.getDesc());
			switch (operation.getType()) {
				case Constants.OPRATION_TYPE_ELEMENT:
					operateElements(operation.getElements());
					onOneOperationEnd();
					break;
				case Constants.OPRATION_TYPE_VERIFY:
					operateVerifys(operation.getVerify());
					break;
				case Constants.OPRATION_TYPE_SLEEP:
					Thread.sleep(operation.getSleepCount() * 1000);
//					Integration.setSleep(driver, operation.getSleepCount());
					break;
				case Constants.OPRATION_TYPE_SUB_CASE:
					LogUtil.i("run sub case:" + operation.getDesc());
					runSubCases(operation.getText());
					break;
				case Constants.OPRATION_TYPE_API:
					runApi(operation.getApiParams());
					break;
				case  Constants.OPRATION_TYPE_SET_VAR:
					setVars((ArrayList<? extends JSONObject>) operation.getVars());
					break;
				default:
					break;
			}
		}
	}

	public void onOneOperationEnd() throws Exception {
		if (browserName == null) {
			try {
				takeScreenShot(3);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	public void start() throws Exception {
		start(true);
	}
	public void start(boolean parallel) throws Exception {
		notifyStart();
		currentThread = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					//desc
					LogUtil.i(testCase.getDesc());
					//����app
					if (udid == null) {
						udid = testCase.getUdid();
					}
					setOsInfo(testCase.getOs(), testCase.getProduct());
					setup(testCase.getIsInstall(), testCase.getIsLaunch(), testCase.getProduct(), testCase.getOs(), udid, testCase.getBrowserName());
					//��ȡoperations
					ArrayList<Operation> operations = testCase.getOperations();
					setOperations(operations);
					resultMsg = "automated test success!";
					resultStatus = 1;
				} catch (NoSuchElementException | SessionNotCreatedException e) {
					LogUtil.e(e.getMessage());
					resultMsg = "automated test fail!";
					resultStatus = 2;
				} catch (Exception e) {
					LogUtil.e(e);
					resultMsg = "automated test fail!";
					resultStatus = 2;
				}
				LogUtil.i(resultMsg);
				try {
					//报错的时候截屏
					if(!("api").equals(testCase.getProduct())) {
						takeScreenShot(resultStatus);
					}
					stopDriver(false);
				} catch (Exception e) {
					LogUtil.e(e);
				}
			}
		});
		//parallel  false同步执行 true异步执行
		if(parallel){
			currentThread.start();
		}else {
			currentThread.run();
		}
		//currentThread.start();
	}

	public void runApi(ArrayList<ApiParam> apiParams) throws Exception {
		try {
			ApiTest apiTest = new ApiTest();
			ApiParam apiParam;
			for (int i = 0, size = apiParams.size(); i < size; i++) {
				apiParam = apiParams.get(i);
				if(apiParam.getDesc() != null) {
					LogUtil.i(apiParam.getDesc());
				}
				int port = 0;
				if(apiParam.getPort() == Constants.BASI_API_PORT) {
					port = apiParam.getPort();
				} else if(apiParam.getPort() == Constants.CUSTOMER_API_PORT) {
					port = Constants.CUSTOMER_API_PORT;
				}  else if(apiParam.getPort() == 5500) {
					port = 5500;
				} else if(apiParam.getPort() == Constants.OPEN_API_PORT) {
					port = Constants.OPEN_API_PORT;
				} else{
					port = Constants.SAN_TI_PORT;
				}
                apiTest.getapi(apiParam.getUrl(), port, apiParam.getMethod(), apiParam.getParams(), apiParam.getVars(), apiParam.isIgnore());

			}
		} catch (Exception e) {
			throw new Exception("api error:" + e.getMessage());
		}
	}

	public void runSubCases(String url) throws Exception{
		TestCase testCaseLogin = ParseUtil.readFileToObj(new File(url));
		ArrayList<Operation> subOperations = testCaseLogin.getOperations();
		setOperations(subOperations);
	}

	public void operateVerifys(ArrayList<Verify> verifys) throws Exception {
		for (int n = 0, verifsize = verifys.size(); n < verifsize; n++) {
			Verify verify = verifys.get(n);
			setVerify(verify);
		}
	}

	public void setVerify(Verify verify) throws Exception {
		int verifyType = verify.getType();
		if(verify.getDesc() != null) {
			LogUtil.i(verify.getDesc());
		}
		switch (verifyType) {
			case Constants.VERIFY_LOGIN:
				setLogin(verify.getText());
				break;
			case Constants.VERIFY_ELEMENT_SEARCH:
				setElements(verify.getElement());
				Integration.elementSearch(getElements(), verify.getElement().getText(), verify.isIgnore());
				break;
			case Constants.VERIFY_VALUE_EQUALS:
				setElements(verify.getElement());
				Integration.elementValueEquals(getElements(), verify.getText(), verify.isIgnore());
				break;
			case Constants.VERIFY_VALUE_CONTAIN:
				setElements(verify.getElement());
				Integration.elementValueContains(getElements(), verify.getText(), verify.getVars(), verify.isIgnore());
				break;
			case Constants.VERIFY_ELEMENT_SEARCH_WAIT:
				elementSearchWait(verify.getElement(), verify.getDuration());
				break;
			case Constants.VERIFY_ELEMENT_EXIST:

				break;
			case Constants.VERIFY_LOGOUT:
				setLogOut(verify.getText());
				break;
		}
	}


	public void setElements(Element element) throws Exception {

		String path = "/";
		if (CommonUtil.isExist(element.getId())) {
			LogUtil.i("//setElements by id! and id is " + element.getId(), false);
			mElements = (List<E>) driver.findElementsById(element.getId());
		} else {
			if(browserName == null) {
				path = Integration.setXPath(osInfo.get(Constants.OS_NAME), path, element);
			} else{
				path = Integration.setXPathForWeb(osInfo.get(Constants.OS_NAME), path, element);
			}
			LogUtil.i(path, false);
			mElements = (List<E>) driver.findElementsByXPath(path);
		}

		LogUtil.i(mElements.toString(), false);
	}



	public void checkElements(Element element) throws Exception {
		try {
			setElements(element);
		} catch (NoSuchElementException e) {
			checkIsQuit();
			throw new NoSuchElementException("not find element by id " + " id:" + element.getId());
		}

		if (getElements().size() == 0) {
			checkIsQuit();
			throw new NoSuchElementException("not find elements by path! className:" + element.toString());
		}
	}

	public void checkIsQuit() {
		By by = Integration.getByXpath("//android.widget.FrameLayout[contains(@content-desc,'云鸟司机')]");
		try {
			List<? extends MobileElement> elements = driver.findElements(by);
			if(elements.size() > 0) {
				ErrorLogUtil.i("闪退//android.widget.FrameLayout[contains(@content-desc,'云鸟司机')]");
				LogUtil.i("闪退//android.widget.FrameLayout[contains(@content-desc,'云鸟司机')]");
			}
		} catch (NoSuchElementException e) {

		}


	}

	/**
	 * 快速点击
	 *
	 * @param element
	 * @param elementIndex
	 * @param action
	 * @throws Exception
	 */
	public void setTap(Element element, int elementIndex, Action action) throws Exception {
		double x = action.getStartX();
		int fingers = action.getFingers();
		int repeatCount = action.getRepeatCount();
		if (repeatCount < 1) repeatCount = 1;
		if (x > 0) {
			int sx = 0;
			int sy = 0;
			if (x < 1) {
				double width = driver.manage().window().getSize().width;
				double height = driver.manage().window().getSize().height;
				sx = (int) (width * action.getStartX());
				sy = (int) (height * action.getStartY());
			} else {
				sx = (int) action.getStartX();
				sy = (int) action.getStartY();
			}

			for (int n = 0; n < repeatCount; n++) {
				driver.tap(fingers, sx, sy, action.getDuration());
			}
		} else {
			try {
				checkElements(element);
				for (int n = 0; n < repeatCount; n++) {
					driver.tap(fingers, getElements().get(elementIndex), action.getDuration());
				}
			} catch (Exception e) {
				if(!element.isIgnoreElement()) {
					throw new NoSuchElementException(e.getMessage());
				}
			}
		}
	}


	public void operateElements(ArrayList<Element> elements) throws Exception {
		for (int i = 0, size = elements.size(); i < size; i++) {
			Element element = elements.get(i);
				if (element.getDesc() != null) {
					LogUtil.i(element.getDesc());
			}
			ArrayList<Action> actions = element.getActions();
			for (int j = 0, a_size = actions.size(); j < a_size; j++) {
				Action action = actions.get(j);
				setAction(element, action);
			}
		}
	}
	public List<E> getElements() {
		return mElements;
	}

	public void setOsInfo(String os, String pr) {
		osInfo = CommonUtil.getOsInfo(os, pr);
	}

	public void setClick(Element element, int elementIndex, int repeatCount) throws Exception {

		try {
			checkElements(element);
			if (repeatCount < 1) repeatCount = 1;
			for (int n = 0; n < repeatCount; n++) {
				getElements().get(elementIndex).click();
			}
		} catch (Exception e) {
			if(!element.isIgnoreElement()) {
				throw new NoSuchElementException(e.getMessage());
			}
		}
	}


	public void setSendKeys(Element element, int elementIndex, String text, ArrayList<Var> vars) throws Exception {
		try {
			checkElements(element);
			getElements().get(elementIndex).clear();
			String str = Integration.getVarsValue(text, vars);
			getElements().get(elementIndex).sendKeys(str);
		} catch (Exception e) {
			throw new NoSuchElementException(e.getMessage() + " 出现该错误可能跟键盘输入法有关系");
		}
	}

	public void setVars(ArrayList<? extends JSONObject> vars) throws Exception{
		String key;
		String value;
		try {
			for (int i = 0, size = vars.size(); i < size; i++) {
				JSONObject var = vars.get(i);
				key = var.getString("varName");
				if(var.get("varValue") instanceof JSONObject) {
					Time time =  var.getObject("varValue", Time.class);
					value = Integration.processTime(time);
				} else {
					value = Integration.getTextValue(var.getString("varValue"));
				}
				KeyValueUtil.put(key, value);
				LogUtil.i("key=" + key + ",value=" + value);
			}
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}

	public abstract void elementSearchWait(Element element, int duration) throws Exception;

	public abstract void setLogin(String url) throws Exception;

	public abstract void setLogOut(String url) throws Exception;

	protected abstract void setup(boolean isInstall, boolean isLaunch, String product, String os, String udid, String browserName) throws Exception;
	protected abstract void takeScreenShot(Integer type) throws Exception;

	protected abstract void setAction(Element element, Action action) throws Exception;
}
