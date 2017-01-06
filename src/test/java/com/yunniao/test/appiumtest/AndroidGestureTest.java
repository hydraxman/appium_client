/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * See the NOTICE file distributed with this work for additional
 * information regarding copyright ownership.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.yunniao.test.appiumtest;

import com.yunniao.appiumtest.Global;
import io.appium.java_client.*;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidKeyCode;
import io.appium.java_client.remote.MobileCapabilityType;
import org.junit.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.File;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * Test Mobile Driver features
 */
public class AndroidGestureTest {
	private AndroidDriver<MobileElement> driver;

	@BeforeClass
	public static void beforeClass() throws Exception {
	}

	@Before
	public void setup() throws Exception {
		File app = new File("ApiDemos-debug.apk");
		DesiredCapabilities capabilities = new DesiredCapabilities();
		capabilities.setCapability(MobileCapabilityType.BROWSER_NAME, "");
		capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, "Android Emulator");
		String path = null;
		String ipAddr = null;
		if(Global.remote){
			path = "./ApiDemos-debug.apk";
			ipAddr = "172.16.41.16";
		}else{
			path=app.getAbsolutePath();
			ipAddr = "localhost";
		}
		capabilities.setCapability(MobileCapabilityType.APP, path);
		driver = new AndroidDriver<MobileElement>(new URL("http://" + ipAddr + ":4723/wd/hub"), capabilities);
	}

	@After
	public void tearDown() throws Exception {
		driver.quit();
	}

	@Test
	public void MultiGestureSingleActionTest() throws InterruptedException {
		//the underlying java library for Appium doesn't like multi-gestures with only a single action.
		//but java-client should handle it, silently falling back to just performing a single action.

		MultiTouchAction multiTouch = new MultiTouchAction(driver);
		TouchAction action0 = new TouchAction(driver).tap(100, 300);
		multiTouch.add(action0).perform();
	}

	@Test
	public void dragNDropTest() {

		driver.findElementByAndroidUIAutomator("new UiScrollable(new UiSelector().scrollable(true).instance(0)).scrollIntoView(new UiSelector().description(\"Views\"))");
		driver.findElementByAccessibilityId("Media").click();
		driver.pressKeyCode(AndroidKeyCode.BACK);
		driver.findElementByAccessibilityId("Views").click();
		driver.findElement(MobileBy.AndroidUIAutomator("description(\"Drag and Drop\")")).click();
		WebElement actionBarTitle = driver.findElement(MobileBy.AndroidUIAutomator("text(\"Views/Drag and Drop\")"));

		assertEquals("Wrong title.", "Views/Drag and Drop", actionBarTitle.getText());
		WebElement dragDot1 = driver.findElement(By.id("io.appium.android.apis:id/drag_dot_1"));
		WebElement dragDot3 = driver.findElement(By.id("io.appium.android.apis:id/drag_dot_3"));
		WebElement dragDot2 = driver.findElement(By.id("io.appium.android.apis:id/drag_dot_2"));

		WebElement dragText = driver.findElement(By.id("io.appium.android.apis:id/drag_text"));
		assertEquals("Drag text not empty", "", dragText.getText());

		TouchAction dragNDrop = new TouchAction(driver).longPress(dragDot1).moveTo(dragDot2).release();
		dragNDrop.perform();
		String text = dragText.getText();
		assertNotEquals("Drag text empty", "", text);
	}

	@Test
	public void TapSingleFingerTest() throws InterruptedException {
		Thread.sleep(2500);
		driver.tap(1, 200, 300, 1000);
	}

	@Test
	public void elementGestureTest() {
		driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
		MobileElement e = driver.findElement(MobileBy.AccessibilityId("App"));
		e.tap(1, 1500);
		System.out.println("tap");
		MobileElement e2 = driver.findElementByClassName("android.widget.TextView");
		e2.zoom();
		System.out.println("zoom");
		e2.swipe(SwipeElementDirection.RIGHT, 1000);
		System.out.println("RIGHT");

		e2 = driver.findElementByClassName("android.widget.TextView");
		e2.swipe(SwipeElementDirection.RIGHT, 1, 2, 1000);
		System.out.println("RIGHT Left border + 10 Right border - 20");

		e2 = driver.findElementByClassName("android.widget.TextView");
		e2.swipe(SwipeElementDirection.LEFT, 1000);
		System.out.println("LEFT");

		e2 = driver.findElementByClassName("android.widget.TextView");
		e2.swipe(SwipeElementDirection.LEFT, 1, 2, 1000);
		System.out.println("LEFT Right border - 10 Left border + 20");

		driver.pressKeyCode(AndroidKeyCode.BACK);
		e2 = driver.findElementByClassName("android.widget.TextView");
		e2.swipe(SwipeElementDirection.DOWN, 1000);
		System.out.println("DOWN");

		e2 = driver.findElementByClassName("android.widget.TextView");
		e2.swipe(SwipeElementDirection.DOWN, 1, 2, 1000);
		System.out.println("DOWN Top - 10 Bottom + 20");

		e2 = driver.findElementByClassName("android.widget.TextView");
		e2.swipe(SwipeElementDirection.UP, 1000);
		System.out.println("UP");

		e2 = driver.findElementByClassName("android.widget.TextView");
		e2.swipe(SwipeElementDirection.UP, 1, 2, 1000);
		System.out.println("UP Bottom + 10 Top - 20");

	}

	@AfterClass
	public static void afterClass() {
	}
}
