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

import io.appium.java_client.MobileBy;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import io.appium.java_client.remote.MobileCapabilityType;
import org.junit.*;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.File;
import java.net.URL;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Test -android uiautomator locator strategy
 */
public class AndroidUIAutomatorTest {

	private AndroidDriver<AndroidElement> driver;

	@BeforeClass
	public static void beforeClass() throws Exception {
	}

	@Before
	public void setup() throws Exception {
		File app = new File("ApiDemos-debug.apk");
		DesiredCapabilities capabilities = new DesiredCapabilities();
		capabilities.setCapability(MobileCapabilityType.BROWSER_NAME, "");
		capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, "Android Emulator");
		capabilities.setCapability(MobileCapabilityType.APP, app.getAbsolutePath());
		driver = new AndroidDriver<AndroidElement>(new URL("http://localhost:4723/wd/hub"), capabilities);
	}

	@After
	public void tearDown() throws Exception {
		driver.quit();
	}

	@Test
	public void findElementTest() {
		WebElement element = driver.findElementByAndroidUIAutomator("new UiSelector().index(0)");
		assertEquals("android.widget.FrameLayout", element.getTagName());
	}

	@Test
	public void findElementsTest() {
		List<AndroidElement> elements = driver.findElementsByAndroidUIAutomator("new UiSelector().clickable(true)");
		assertTrue(elements.size() > 10);
	}

	@Test
	public void findElementByTest() {
		AndroidElement element = driver.findElement(MobileBy.AndroidUIAutomator("new UiSelector().index(0)"));
		assertEquals("android.widget.FrameLayout", element.getTagName());
	}

	@Test
	public void findElementsByTest() {
		List<AndroidElement> elements = driver.findElements(MobileBy.AndroidUIAutomator("new UiSelector().clickable(true)"));
		assertTrue(elements.size() > 10);
	}

	@Test
	public void findChainedElementsTest() {
		AndroidElement el1 = driver.findElementByAndroidUIAutomator("resourceId(\"android:id/content\")");
		MobileElement el2 = el1.findElement(MobileBy.AndroidUIAutomator("text(\"Accessibility\")"));
		el2.click();
		AndroidElement el3 = driver.findElementByAndroidUIAutomator("text(\"Custom View\")");
		assertTrue(el3.isDisplayed());
	}

	@Test
	public void replaceValue() {
		String originalValue = "original value";
		String replacedValue = "replaced value";

		driver.scrollToExact("Views").click();
		driver.findElementByAndroidUIAutomator("text(\"Controls\")").click();
		driver.findElementByAndroidUIAutomator("text(\"1. Light Theme\")").click();

		AndroidElement editElement = driver.findElementByAndroidUIAutomator("resourceId(\"io.appium.android.apis:id/edit\")");

		editElement.sendKeys(originalValue);

		assertEquals(originalValue, editElement.getText());

		editElement.replaceValue(replacedValue);

		assertEquals(replacedValue, editElement.getText());
	}

	@Test(expected = IllegalArgumentException.class)
	public void ErrorTest() {
		driver.findElementByAndroidUIAutomator(null);
	}


	@AfterClass
	public static void afterClass() {
	}
}