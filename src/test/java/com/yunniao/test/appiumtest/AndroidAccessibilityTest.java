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
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.File;
import java.net.URL;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class AndroidAccessibilityTest {

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
	public void findElementsTest() {
		List<AndroidElement> elements = driver.findElementsByAccessibilityId("Accessibility");
		assertTrue(elements.size() > 0);
	}

	@Test
	public void findElementTest() {
		//WebElement element =
		MobileElement element = driver.findElementByAccessibilityId("Accessibility");
		assertNotNull(element);
	}

	@Test
	public void MobileElementByTest() {
		MobileElement element = driver.findElement(MobileBy.AccessibilityId("Accessibility"));
		assertNotNull(element);
	}

	@Test
	public void MobileElementsByTest() {
		List<AndroidElement> elements = driver.findElements(MobileBy.AccessibilityId("Accessibility"));
		assertTrue(elements.size() > 0);
	}

	@AfterClass
	public static void afterClass() {
	}

}
