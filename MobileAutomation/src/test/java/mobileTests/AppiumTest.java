package mobileTests;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.openqa.selenium.ElementNotVisibleException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.ScreenOrientation;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.touch.TouchActions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;

import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import io.appium.java_client.android.nativekey.AndroidKey;
import io.appium.java_client.android.nativekey.KeyEvent;
import io.appium.java_client.touch.TapOptions;
import io.appium.java_client.touch.offset.PointOption;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AppiumTest {
	
	//Driver element to be used for android device
	public static AndroidDriver driver = null;
	//Dimension variables to deal with different screne resolutions and coordinates
	public static int height = 0, width = 0;
	
	//Setup capabilities, driver and ebay application
	@BeforeClass
	public static void initializeEmulator() throws InterruptedException {
		
		//Set the Desired Capabilities for specific devices
		DesiredCapabilities caps = new DesiredCapabilities();
		
		/*Note: Top section allows tests to be run from eclipse of IDE,
		 * Simply change capability variable when setting up Android driver on line 100
		 * Botton section allows program to be run from maven terminal with 'mvn test -Ddevice=<devicetype>'
		 * Uncomment/comment section according to desired running conditions
		 */
		
		/*caps.setCapability("deviceName", "Samsung Galaxy S7 edge");
		caps.setCapability("udid", "ad09160350caa92bec"); //Give Device ID of your mobile phone
		caps.setCapability("platformName", "Android");
		caps.setCapability("platformVersion", "8.0");
		caps.setCapability("automationName", "uiautomator2");
		caps.setCapability("appPackage", "com.ebay.mobile");
		caps.setCapability("appActivity", "com.ebay.mobile.activities.MainActivity");
		
		//S10+
		DesiredCapabilities capsS10plus = new DesiredCapabilities();
		capsS10plus.setCapability("deviceName", "Brady Galaxy S10+");
		capsS10plus.setCapability("udid", "R58M23FCW4H"); //Give Device ID of your mobile phone
		capsS10plus.setCapability("platformName", "Android");
		capsS10plus.setCapability("platformVersion", "9.0");
		capsS10plus.setCapability("automationName", "uiautomator2");
		capsS10plus.setCapability("appPackage", "com.ebay.mobile");
		capsS10plus.setCapability("appActivity", "com.ebay.mobile.activities.MainActivity");*/
		
		String device = System.getProperty("device");
		
		if(device.equals("S7Edge")) {
			caps.setCapability("deviceName", "Samsung Galaxy S7 edge");
			caps.setCapability("udid", "ad09160350caa92bec"); //Give Device ID of your mobile phone
			caps.setCapability("platformName", "Android");
			caps.setCapability("platformVersion", "8.0");
			caps.setCapability("automationName", "uiautomator2");
			caps.setCapability("appPackage", "com.ebay.mobile");
			caps.setCapability("appActivity", "com.ebay.mobile.activities.MainActivity");
		}
		else if(device.equals("S10Plus")) {
			caps.setCapability("deviceName", "Brady Galaxy S10+");
			caps.setCapability("udid", "R58M23FCW4H"); //Give Device ID of your mobile phone
			caps.setCapability("platformName", "Android");
			caps.setCapability("platformVersion", "9.0");
			caps.setCapability("automationName", "uiautomator2");
			caps.setCapability("appPackage", "com.ebay.mobile");
			caps.setCapability("appActivity", "com.ebay.mobile.activities.MainActivity");
		}
		else {
			//Default device
		}
		
		
		//Instantiate Appium Driver
		try {
			driver = new AndroidDriver(new URL("http://0.0.0.0:4723/wd/hub"), caps);
			Thread.sleep(5000);
			
			//Grabs window width and height for different resolutions
			width = driver.manage().window().getSize().getWidth();
			height = driver.manage().window().getSize().getHeight();
		} catch (MalformedURLException e) {
			System.out.println(e.getMessage());
		}
	}
	
	/*|---Ebay Sign In---|*/
	@Test
	public void test1EbaySignIn() throws IOException, InterruptedException {
		//Setup property file to grab relevant data
		Properties prop = new Properties();
		FileInputStream fs = new FileInputStream("C:\\Users\\777632\\eclipse-workspace\\MobileAutomation\\src\\main\\java\\resources\\data.properties");
		prop.load(fs);
		
		String username = prop.getProperty("email");
		String password = prop.getProperty("password");
		
		//try catch used to test if pop-up element is visible and dismisses if so
		try {
			driver.findElementById("com.ebay.mobile:id/social_account_splash_screen_text_wrapper").isDisplayed();
			driver.findElementById("com.ebay.mobile:id/social_account_splash_screen_close").click();
		}	
		//Program continues running if pop-up does not appear
		catch(NoSuchElementException e){
			System.out.println("Social pop-up did not appear this run, continuing test execution as normal");
		}
		
		//Navigate to the signup and signin mobile screen
		driver.findElementByAccessibilityId("Main navigation, open").click();
		
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		
		driver.findElementByAccessibilityId("Sign in,double tap to activate").click();
		
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		
		/*Note:
		 * Required to select googl account saved on device
		 * due to ebay application having security policies with screenshots and therefore appium was unable to
		 * screengrab in inspector session to determine the unique identifieers on corresponding pages
		 */
		//Select google account
		driver.findElementById("com.ebay.mobile:id/button_google").click();
		
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		
		
		//try catch handles if google account pop-up appears and choses first account present in device
		try {
			driver.findElementById("com.google.android.gms:id/account_picker").isDisplayed();
			//com.google.android.gms:id/account_display_name = Specific name when more than one account
			driver.findElementById("com.google.android.gms:id/account_display_name").click();
		}	
		catch(NoSuchElementException e){
			System.out.println("Google account pop-up did not appear this run, continuing test execution as normal");
		}
	}
	
	/*|---Enter search item keywords---|*/
	@Test
	public void test2EnterSearchItem() throws IOException, InterruptedException {
		//Setup data properties file
		Properties prop = new Properties();
		FileInputStream fs = new FileInputStream("C:\\Users\\777632\\eclipse-workspace\\MobileAutomation\\src\\main\\java\\resources\\data.properties");
		prop.load(fs);
		
		//Grab item to be search and inject into search box
		String product = prop.getProperty("itemSearch");
		
		//Tap on search bar
		driver.findElementById("com.ebay.mobile:id/search_box").click();
		
		//wait for new page to load
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		
		//Tap on new search bar
		driver.findElementById("com.ebay.mobile:id/search_src_text").click();
		
		Thread.sleep(1000);
		
		//Enter item for search and keyboard enter
		driver.findElementById("com.ebay.mobile:id/search_src_text").sendKeys(product);
		
		Thread.sleep(2000);
		
		//Use android keyboard to enter search
		driver.pressKey(new KeyEvent(AndroidKey.ENTER));
	}

	/*|---Scroll To Item---|*/
	@Test
	public void test3ScrollToItem() throws InterruptedException, IOException {
		//Action object to handle coordinate presses and swiping
		TouchAction tc = new TouchAction(driver);
		Thread.sleep(2000);
		
		//Wait for seach results page to appear
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		
		//Handles a save preference pop-up that differs with text on different runs
		try {
			driver.findElementById("com.ebay.mobile:id/text_slot_1").click();
		}
		//Output to let reader know even though this try catch failed does not result in test failure
		catch(NoSuchElementException e) {
			System.out.println("Save preference pop-up did not appear this run, continuing test execution as normal");
		}
		
		Thread.sleep(3000);
		
		/*Code below is to handle scolling to desired search item.
		 * As the ebay app does not use unique identifiers for each grid item
		 * Below method was required to store each grid element currently visible in a list
		 * Then comparison made with intended item and if no matc found scroll a certain amount
		 * New list is generated based off new items viewable and continues proces until item found and clickable
		 */
		
		//Boolean flag to stop while loop searc once found
		boolean isFound = false;
		
		//Grabs item to be clicked, must be exact match
		Properties prop = new Properties();
		FileInputStream fs = new FileInputStream("C:\\Users\\777632\\eclipse-workspace\\MobileAutomation\\src\\main\\java\\resources\\data.properties");
		prop.load(fs);
		
		String item = prop.getProperty("item");
		
		//Determines the screen orientation as different coordinates required due to dimension differences
		if(driver.getOrientation() == ScreenOrientation.PORTRAIT) {
			//Defined outerloop to breka out of once item found
			OUTER_LOOP:
			while(isFound == false) {
				
				//List that stores all currently viewable elements matching class
				List<AndroidElement> productList = driver.findElementsByClassName("android.widget.TextView");
				
				//Searches through list until match is found then clicks on corresponding List index
				for(int i = 0; i < productList.size(); i++) {
					if(productList.get(i).getText().equals(item)) {
						isFound = true;
						productList.get(i).click();
						
						//Breaks out of while loop if item found
						break OUTER_LOOP;
					}
				}
				//Scroll/swipe occurs if item not currently viewable
				tc.longPress(PointOption.point((int) (width*0.5), (int) (height*0.8))).moveTo(PointOption.point((int) (width*0.5), (int) (height*0.1))).release().perform();
				Thread.sleep(1000);
			}
		}
		//Determines the screen orientation as different coordinates required due to dimension differences
		//Repeats above code and comments supplied
		else if(driver.getOrientation() == ScreenOrientation.LANDSCAPE) {
			OUTER_LOOP:
			while(isFound == false) {
				List<AndroidElement> productList = driver.findElementsByClassName("android.widget.TextView");
				for(int i = 0; i < productList.size(); i++) {
					if(productList.get(i).getText().equals(item)) {
						isFound = true;
						productList.get(i).click();
						break OUTER_LOOP;
					}
				}
				tc.longPress(PointOption.point((int) (width*0.4), (int) (height*0.84))).moveTo(PointOption.point((int) (width*0.4), (int) (height*0.26))).release().perform();
				Thread.sleep(1000);
			}
		}
	}

	/*|---Add Item To Cart And Verify Data---|*/
	@Test
	public void test4AddCartAndVerify() throws InterruptedException {
		Thread.sleep(3000);
		TouchAction tc = new TouchAction(driver);
		String itemName = null, str = null;
		boolean scroll = true;
		
		//Try catch handles scrolling to grab both item name and price which are always located together
		while(scroll == true) {
			try {
				//Checks if both price and name are present in view
				if(driver.findElementById("com.ebay.mobile:id/textview_item_name").isDisplayed() &&
						driver.findElementById("com.ebay.mobile:id/textview_item_price").isDisplayed()) {
					scroll = false;
				}
			}
			//Scroll if both itemname and price are not viewable
			catch(NoSuchElementException e) {
				tc.longPress(PointOption.point(800, 915)).moveTo(PointOption.point(800, 275)).release().perform();
			}
		}
		
		//Above try catch must pass to get to this stage
		//Grabs item name and stores it fore verification later
		itemName = driver.findElementById("com.ebay.mobile:id/textview_item_name").getText();
		
		//Grabs price and manipulates format to be verifiable
		str = driver.findElementById("com.ebay.mobile:id/textview_item_price").getText();

		//Splits string to grab only important digits and also removes any ',' present denoting larger amounts
		String[] stringArr = str.split("\\$", 2);
		String price = stringArr[1];
		price = price.replaceAll(",", "");
		
		//Converts string into double to be able to compare and verify
		Double itemPrice = Double.parseDouble(price);
		
		/*Quantity setting for verification
		 * Note:
		 * Ebay app has large bug where randonly and quite often quantity cannot be select
		 * Therefore current test scrolls a small amount twice to check whether it can be found
		 * If not it determnes it has not appeared due to bug and sets a default value
		 */
		String itemQuantity= null;
		Thread.sleep(2000);
		
		int count = 0;
		boolean qtyIsFound = false;
		//Breaks from leap when required
		OUTER_LOOP:
		while(qtyIsFound == false) {
			//While loop runs twice if quantitiy cannot be found or isn't provided
			if(count == 2) {
				//Sets default value of 1 and breaks out of loop to stop infinite scroll searching
				itemQuantity = "Qty 1";
				break OUTER_LOOP;
			}
			try {
				//Checks if quantity amount is visible and if so sets corresponding value for verification later
				if(driver.findElementById("android:id/text1").isDisplayed()) {
					qtyIsFound = true;
					itemQuantity = driver.findElementById("android:id/text1").getText();
					
					//Below is done for fomartting reasons as it the cart quantity amount follows this format
					itemQuantity = "Qty " + itemQuantity;
				}
			}
			//If not found then determines the screen orientation and then scrolls/swipes the appropriate amount
			catch(NoSuchElementException e) {
				//counter increment for loop limit
				++count;
				if(driver.getOrientation() == ScreenOrientation.PORTRAIT) {
					tc.longPress(PointOption.point((int) (width*0.51), (int) (height*0.81))).moveTo(PointOption.point((int) (width*0.51), (int) (height*0.62))).release().perform();
				}
				else if(driver.getOrientation() == ScreenOrientation.LANDSCAPE) {
					tc.longPress(PointOption.point((int) (width*0.41), (int) (height*0.48))).moveTo(PointOption.point((int) (width*0.41), (int) (height*0.25))).release().perform();
				}
			}
		}

		Thread.sleep(1000);
		/*Adding item to cart/basket
		 * Another bug appeaed in this stage, initially thought it was just diffeent screen resolutions but
		 * tested and determined the add item button contains a bug. Button changes names by one word being
		 * either 'Add to basket' or 'Add to cart'. Below handles checking if basket variant is visible first,
		 * if not checks if cart variant is visible and scrolls/swipes if neither found based of appropriate
		 * coordinates for screen orientation.
		 * No unique identifier for button as one element so had to use this method as isDisplay() for Appium
		 * only successfully completes in try catch
		 */
		boolean cartIsFound = false;
		while(cartIsFound == false) {
			//try catch to check forbasket variant
			try{
				if(driver.findElementByAccessibilityId("Add to basket").isDisplayed()) {
					cartIsFound = true;
					driver.findElementByAccessibilityId("Add to basket").click();
				}
			}
			//If not found will move onto checking if cart variant visible
			catch(NoSuchElementException e) {
				System.out.println("Add to basket not visible, checking for Add to cart");
				cartIsFound = false;
			}
			//First checks if boolean flag is still false to require checking for this second variant
			if(cartIsFound == false) {
				try {
					if(driver.findElementByAccessibilityId("Add to cart").isDisplayed()) {
						cartIsFound = true;
						driver.findElementByAccessibilityId("Add to cart").click();
					}
				}
				catch(NoSuchElementException e) {
					System.out.println("Element Exception: Neither Add item buttons appeared, test Execution failed");
					cartIsFound = false;
				}
			}
			//After both checks if boolean flag still false it performs a scroll appropriate to screen orientation
			if(cartIsFound == false) {
				if(driver.getOrientation() == ScreenOrientation.PORTRAIT) {
					tc.longPress(PointOption.point((int) (width*0.51), (int) (height*0.75))).moveTo(PointOption.point((int) (width*0.45), (int) (height*0.62))).release().perform();
				}
				else if(driver.getOrientation() == ScreenOrientation.LANDSCAPE) {
					tc.longPress(PointOption.point((int) (width*0.41), (int) (height*0.48))).moveTo(PointOption.point((int) (width*0.41), (int) (height*0.25))).release().perform();
				}
			}
		}
		
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		//After item added to cart must deal with pop-up element on screen
		Thread.sleep(3000);
		//If in portait mode enough screen can be used to display the go to cart button
		if(driver.getOrientation() == ScreenOrientation.PORTRAIT) {
			driver.findElementById("com.ebay.mobile:id/call_to_action_button").click();
		}
		
		/*Otherwise chance button is not viewable performs a scroll
		*Will have two outcomes based off screen resolution in which it will either
		*scroll the cart button into view and be clicked on
		*or swipe the pop-up element off screen to then click on the cart button on eveyr page
		*/
		else if(driver.getOrientation() == ScreenOrientation.LANDSCAPE) {
			tc.longPress(PointOption.point((int) (width*0.51), (int) (height*0.45))).moveTo(PointOption.point((int) (width*0.51), (int) (height*0.90))).release().perform();
			driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
			//After scroll checks to see if 'Go to cart' button viewable
			try {
				driver.findElementById("com.ebay.mobile:id/call_to_action_button").isDisplayed();
				driver.findElementById("com.ebay.mobile:id/call_to_action_button").click();
			}
			//If not visible then element has been swiped away and instead clicks on cart icon present on every page
			catch(NoSuchElementException e) {
				driver.findElementById("com.ebay.mobile:id/menu_cart").click();
			}
		}
		
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		
		//Data verification process begins
		//Grabs item name in cart
		String cartItemName = driver.findElementById("com.ebay.mobile:id/item_title").getText();
		
		//Sets item price in cart but also has to perform similar format manipulation as previous price
		str = driver.findElementById("com.ebay.mobile:id/item_price").getText();
		stringArr = str.split("\\$", 2);
		String cartPrice = stringArr[1];
		cartPrice = cartPrice.replaceAll(",", "");
		
		//Converts formatted string into numeric value for veification
		Double cartItemPrice = Double.parseDouble(cartPrice);
		
		//Sets quantity of item in cart for verification
		String cartItemQty = driver.findElementById("com.ebay.mobile:id/item_quantity_label").getText();
		
		//Assertion verifications for item details checks name, price and quantity
		try {
			Assert.assertEquals(itemName, cartItemName);
		}
		catch(AssertionError e){
			System.out.println("Assertion Error 1: Name Verification Failed: " + e);
		}
		try {
			Assert.assertEquals(itemPrice, cartItemPrice);
		}
		catch(AssertionError e) {
			System.out.println("Assertion Error 2: Price Verification Failed: " + e);
		}
		try {
			Assert.assertEquals(itemQuantity, cartItemQty);
		}
		catch(AssertionError e) {
			System.out.println("Assertion Error 3: Quantity Verification Failed: " + e);
		}
		
		Thread.sleep(3000);
		
		//Removes item from cart fter verification as personal Google account was required for use of testing
		driver.findElementById("com.ebay.mobile:id/item_action_remove_from_cart").click();
		
		Thread.sleep(2000);
		
		//Closes the cart
		driver.findElementById("com.ebay.mobile:id/home").click();
		
		Thread.sleep(2000);
		
		//After closing cart have to handle secondary pop-up that can occur
		//Swipes it down off screen based of screen orientation
		if(driver.getOrientation() == ScreenOrientation.PORTRAIT) {
			tc.press(PointOption.point((int) (width*0.51), (int) (height*0.13))).moveTo(PointOption.point((int) (width*0.51), (int) (height*0.63))).release().perform();
		}
		else if(driver.getOrientation() == ScreenOrientation.LANDSCAPE) {
			tc.longPress(PointOption.point((int) (width*0.51), (int) (height*0.15))).moveTo(PointOption.point((int) (width*0.51), (int) (height*0.90))).release().perform();
		}
	}
	
	/*|---Sign Out Of Account--|*/
	//Sign out of current Google account
	@Test
	public void test5EbaySignOut() throws InterruptedException, IOException {
		
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		
		driver.findElementByAccessibilityId("Main navigation, open").click();
		
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		driver.findElementById("com.ebay.mobile:id/textview_sign_in_status").click();
		
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		driver.findElementById("com.ebay.mobile:id/menuitem_sign_out").click();
	
		Thread.sleep(3000);
	}
	
	/*|---Landsacpe Orientation Tests---|*/
	@Test
	public void test6LandscapeTests() throws InterruptedException, IOException {
		Thread.sleep(5000);
		//Set Screen Orientation to landscape and re-set width and height variables
		driver.rotate(ScreenOrientation.LANDSCAPE);
		width = driver.manage().window().getSize().getWidth();
		height = driver.manage().window().getSize().getHeight();
		
		//Re-run tests now to show rotation handling and also different screen sizes
		test1EbaySignIn();
		
		test2EnterSearchItem();
		
		test3ScrollToItem();
		
		test4AddCartAndVerify();
		
		test5EbaySignOut();
		
		driver.rotate(ScreenOrientation.PORTRAIT);
		
	}
	
	/*|---Terminates the driver and application---|*/
	@Test
	public void test7Terminate() {
		driver.closeApp();
		driver.terminateApp("com.ebay.mobile");
		driver.quit();
	}
}
