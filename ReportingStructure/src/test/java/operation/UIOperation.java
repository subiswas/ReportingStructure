package operation;

import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.UnexpectedAlertBehaviour;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.firefox.internal.ProfilesIni;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import actions.ResultUtil;
import common.ExtendedLibrary;

public class UIOperation extends ExtendedLibrary {
	static WebDriver driver;
	DesiredCapabilities capability = null;
	static WebDriverWait wait;
	static String project_path = System.getProperty("user.dir");
	static int MAX_TIMEOUT = 60;
	static int STABLE_WAIT = 1000;
	
	public void open_Browser(String browserName) throws MalformedURLException{
		try{
			if(browserName.equalsIgnoreCase("Firefox")){
				System.setProperty("webdriver.gecko.driver",project_path+"\\drivers\\geckodriver64.exe");
				FirefoxProfile profile = new FirefoxProfile();
				capability = DesiredCapabilities.firefox();
				profile.setAcceptUntrustedCertificates(false);
				profile.setAssumeUntrustedCertificateIssuer(true);
				profile.setPreference("browser.download.folderList", 2);
				profile.setPreference("browser.helperApps.alwaysAsk.force", false);
				profile.setPreference("browser.download.manager.showWhenStarting",false);
				profile.setPreference("browser.download.dir", project_path + "\\downloads"); 
				profile.setPreference("browser.download.downloadDir",project_path + "\\downloads"); 
				profile.setPreference("browser.download.defaultFolder",project_path + "\\downloads"); 
				profile.setPreference("browser.helperApps.neverAsk.saveToDisk","text/anytext ,text/plain,text/html,application/plain" );
				capability.setCapability(FirefoxDriver.PROFILE, profile);
				driver = new FirefoxDriver(capability);
			} else if(browserName.equalsIgnoreCase("chrome")){
				System.setProperty("webdriver.chrome.driver",project_path+"\\drivers\\chromedriver.exe");
				capability = DesiredCapabilities.chrome();
				driver = new ChromeDriver(capability);
			} else if(browserName.equalsIgnoreCase("IE")){
				System.setProperty("webdriver.ie.driver",project_path+"\\drivers\\IEDriverServer32.exe");
				capability = DesiredCapabilities.internetExplorer();
				capability.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
                capability.setCapability(CapabilityType.SUPPORTS_ALERTS, true);
                capability.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
                capability.setCapability(CapabilityType.UNEXPECTED_ALERT_BEHAVIOUR, UnexpectedAlertBehaviour.ACCEPT);
				driver = new InternetExplorerDriver(capability);
			}
			driver.manage().window().maximize();
			ResultUtil.report("PASS", "Verify Browser launch", "Browser should be launched", "Browser is launched", driver);
		} catch (WebDriverException e){
			ResultUtil.report("FAIL", "Verify Browser launch", "Browser should be launched", "Browser is not launched", driver);
			System.out.println(e.getMessage());
		}
	}
	
	public void start_process(String tc_id, Map<String,String> dataMap) throws Exception{
		ResultUtil.createReport(tc_id, dataMap);
	}
	
	public void enter_URL(String URL){
		try{
			driver.navigate().to(URL);
			ResultUtil.report("INFO", "Go to " + URL, "", "", driver);
		} catch (Exception e){
			ResultUtil.report("FAIL", "Exception at enter_URL", "", e.getMessage(), driver);
		}
	}
	
	public By locatorValue(String locatorTpye, String value){
		By by;
		switch(locatorTpye){
		case "id":
			by = By.id(value);
			break;
			
		case "name":
			by = By.name(value);
			break;
			
		case "class":
			by = By.className(value);
			break;
			
		case "xpath":
			by = By.xpath(value);
			break;
			
		case "css":
			by = By.cssSelector(value);
			break;
			
		case "linkText":
			by = By.linkText(value);
			break;
			
		case "partialLinkText":
			by = By.partialLinkText(value);
			break;
			
		case "tagName":
			by = By.tagName(value);
			break;
			
		default:
			by = null;
			break;
		}
		return by;
	}
	
	public void enter_Text(String locatorType, String value, String text){
		try{
			By locator;
			locator = locatorValue(locatorType, value);
			WebElement element = driver.findElement(locator);
			element.clear();
			element.sendKeys(text);
			ResultUtil.report("PASS", "Verify send text - "+text, "'"+text+"' should be entered", "'"+text+"' is entered", driver);
		} catch (NoSuchElementException e){
			System.err.format("No Element Found to enter text" + e);
			ResultUtil.report("FAIL", "Exception occured at 'enter_Text'", "", "", driver);
		}
		
	}
	
	public void click_On_Link(String locatorType, String value) throws InterruptedException {
		try {
			By locator;
			locator = locatorValue(locatorType, value);
			WebElement element = driver.findElement(locator);
			((JavascriptExecutor) driver).executeScript("window.scrollTo(0,"+element.getLocation().x+")");
			element.click();
			Thread.sleep(STABLE_WAIT);
			ResultUtil.report("PASS", "Verify clicking button - "+ value, "Button should be clicked", "Button is clicked", driver);
		} catch (NoSuchElementException e) {
			System.err.format("No Element Found to perform click" + e);
			ResultUtil.report("FAIL", "Exception occured at 'click_On_Button'", "", "", driver);
		}
	}
	
	public void verify_element_displayed(String locatorType, String value) {
		try {
			By locator;
			locator = locatorValue(locatorType, value);
			WebElement element = driver.findElement(locator);
			if(element.isDisplayed()){
				ResultUtil.report("PASS", "Verify element displayed - " + value, "Should display - " + value, "Displayed - " + value, driver);
			} else {
				ResultUtil.report("FAIL", "Verify element displayed - " + value, "Should display - " + value, "Not Displayed - " + value, driver);
			}
		} catch (NoSuchElementException e) {
			System.err.format("No Element Found to enter text" + e);
			ResultUtil.report("FAIL", "Exception occured at 'verify_element_displayed'", "", "", driver);
		}
	}
	
	public void click_On_Button(String locatorType, String value) throws InterruptedException {
		try {
			By locator;
			locator = locatorValue(locatorType, value);
			WebElement element = driver.findElement(locator);
			((JavascriptExecutor) driver).executeScript("window.scrollTo(0,"+element.getLocation().x+")");
			element.click();
			Thread.sleep(STABLE_WAIT);
			ResultUtil.report("PASS", "Verify clicking button - "+ value, "Button should be clicked", "Button is clicked", driver);
		} catch (NoSuchElementException e) {
			System.err.format("No Element Found to perform click" + e);
			ResultUtil.report("FAIL", "Exception occured at 'click_On_Button'", "", "", driver);
		}
	}
	
	public void wait_for_element(String locatorType, String value){
		try {
			By locator;
			locator = locatorValue(locatorType, value);
			WebElement element = new WebDriverWait(driver, MAX_TIMEOUT).until(ExpectedConditions.visibilityOfElementLocated(locator));
			ResultUtil.report("INFO", "Waiting for element displayed", "", "", driver);
		} catch (NoSuchElementException e) {
			System.err.format("No Element Found." + e);
			ResultUtil.report("FAIL", "Exception occured at 'wait_for_element'", "", "", driver);
		}
	}
	
	public void wait_for_loading(String locatorType, String value) throws InterruptedException{
		try {
			By locator;
			locator = locatorValue(locatorType, value);
			new WebDriverWait(driver, MAX_TIMEOUT).until(ExpectedConditions.invisibilityOfElementLocated(locator));
			Thread.sleep(STABLE_WAIT);
			ResultUtil.report("INFO", "Waiting for loading", "", "", driver);
		} catch (NoSuchElementException e) {
			System.err.format("No Element Found." + e);
			ResultUtil.report("FAIL", "Exception occured at 'wait_for_loading'", "", "", driver);
		}
	}
	
	public String get_Text(String locatorType, String value){
		String text = null;
		try {
			By locator;
			locator = locatorValue(locatorType, value);
			WebElement element = driver.findElement(locator);
			text = element.getText();
			return text;
		} catch (NoSuchElementException e) {
			System.err.format("No Element Found to get text" + e);
		}
		return text;
	}
	
	public void operation_mousehover_event(String locatorType1, String locatorType2, String value1, String value2){
		try {
			By locator1;
			By locator2;
			locator1 = locatorValue(locatorType1, value1);
			locator2 = locatorValue(locatorType2, value2);
			Actions action = new Actions(driver);
			WebElement element1 = driver.findElement(locator1);
			action.moveToElement(element1).moveToElement(driver.findElement(locator2)).click().build().perform();
		} catch (NoSuchElementException e){
			System.err.format("No Element Found." + e);
		}
	}
	
	public void spl_click_on_link(String locatorType, String value){
		try {
			By locator;
			locator = locatorValue(locatorType, value);
			Actions action = new Actions(driver);
			WebElement element = driver.findElement(locator);
			action.moveToElement(element).click().build().perform();
		} catch (NoSuchElementException e){
			System.err.format("No Element Found." + e);
		}
	}
	
	public void take_SnapShot(String fileWithPath) throws Exception{
		try {
			TakesScreenshot scrShot =((TakesScreenshot)driver);
			File SrcFile=scrShot.getScreenshotAs(OutputType.FILE);
			File DestFile=new File(fileWithPath);
			FileUtils.copyFile(SrcFile, DestFile);
		} catch (Exception e){
			//System.err.format("Failed to capture snapshot " + e);
		}
	}
	
	public void sync(int val) throws InterruptedException{
		Thread.sleep(val);
		ResultUtil.report("INFO", "Waiting for "+ val +" miliseconds", "", "", driver);
	}
	
	public void text_verification(String locatorType, String value, String text){
		try{
			String actual = get_Text(locatorType, value);
			String expected = text;
			if(actual.contains(expected)){
				ResultUtil.report("PASS", "Verify text - " +text, "'"+text+"' should be displayed", "'"+text+"' is displayed", driver);
			} else {
				ResultUtil.report("FAIL", "Verify text - " +text, "'"+text+"' should be displayed", "'"+text+"' is not displayed", driver);
			}
		} catch (Exception e){
			ResultUtil.report("FAIL", "Exception occured at 'text_verification'","", e.getMessage(), driver);
		}
	}
	
	public void select_checkbox_table_content_by_rownum(String locatorType, String value, String text){
		try{
			By locator;
			locator = locatorValue(locatorType, value);
			List<WebElement> allCheckboxes = driver.findElements(locator);
			if(allCheckboxes.size() > 0){
				allCheckboxes.get(Integer.parseInt(text)).click();
				ResultUtil.report("PASS", "Clicking checkbox at row "+text,"Check box should be clicked", "Check box is clicked", driver);
			} else {
				ResultUtil.report("FAIL", "Clicking checkbox at row "+text,"Check box should be clicked", "No checkbox found", driver);
			}
		} catch(Exception e){
			ResultUtil.report("FAIL", "Exception occured at 'select_checkbox_table_content_by_rownum'","", e.getMessage(), driver);
		}
	}
	
	public void select_checkbox_table_content_by_text(String locatorType, String value, String text){
		try{
			By locator;
			int row = 0;
			locator = locatorValue(locatorType, value);
			List<WebElement> allItem = driver.findElements(locator);
			for(WebElement item:allItem){
				String content = item.getText().toString();
				if(content.contentEquals(text)){
					break;
				}
				row++;
			}
			String[] locator_parts = value.split(":");
			value = locator_parts[0] + " input";
			locator = locatorValue(locatorType, value);
			List<WebElement> allCheckboxes = driver.findElements(locator);
			if(allCheckboxes.size() > 0){
				allCheckboxes.get(row - 1).click();
				ResultUtil.report("PASS", "Clicking checkbox at content "+text,"Check box should be clicked", "Check box is clicked", driver);
			} else {
				ResultUtil.report("FAIL", "Clicking checkbox at content "+text,"Check box should be clicked", "No checkbox found", driver);
			}
		} catch(Exception e){
			ResultUtil.report("FAIL", "Exception occured at 'select_checkbox_table_content_by_text'","", e.getMessage(), driver);
		}
	}
	
	public void verify_table_content(String locatorType, String value, String text){
		try{
			By locator;
			int flag = 0;
			locator = locatorValue(locatorType, value);
			List<WebElement> allItem = driver.findElements(locator);
			for(WebElement item:allItem){
				String content = item.getText().toString();
				if(content.contentEquals(text)){
					flag = 1;
					break;
				}
			}
			if(flag == 1){
				ResultUtil.report("PASS", "Verify table content - "+text,"'"+text+"' should be present", "'"+text+"' is present", driver);
			} else {
				ResultUtil.report("FAIL", "Verify table content - "+text,"'"+text+"' should be present", "'"+text+"' is not present", driver);
			}
		} catch(Exception e){
			ResultUtil.report("FAIL", "Exception occured at 'verify_table_content'","", e.getMessage(), driver);
		}
	}
	
	public void partial_text_verification(String locatorType, String value, String text){
		String actual = get_Text(locatorType, value);
		String expected = text;
		Assert.assertTrue(actual.contains(expected));
	}
	
	public void change_css_attribute(String locatorType, String value, String text){
		try{
			String[] text_arr = text.split(",");
			if(locatorType.contentEquals("class")){
				((JavascriptExecutor) driver).executeScript("document.getElementsByClassName('"+value+"')[0].style."+text_arr[0]+"='"+text_arr[1]+"'");
			}
		} catch (Exception e){
			ResultUtil.report("FAIL", "Exception occured at 'change_css_attribute'","", e.getMessage(), driver);
		}
	}
	
	public void setClipboardData(String file_location){
		StringSelection stringSelection = new StringSelection(file_location);
		Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringSelection, null);
	}
	
	public void upload_file(String text){
		String file_location = project_path+"\\upload\\"+text;
		try {
			setClipboardData(file_location);
			Robot robot = new Robot();
			robot.keyPress(KeyEvent.VK_CONTROL);
            robot.keyPress(KeyEvent.VK_V);
            robot.keyRelease(KeyEvent.VK_V);
            robot.keyRelease(KeyEvent.VK_CONTROL);
            robot.keyPress(KeyEvent.VK_ENTER);
            robot.keyRelease(KeyEvent.VK_ENTER);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void perform(Properties p, String operation, String objectName, String objectType, String value, String tc_id, Map<String,String> dataMap) throws Exception{
		switch (operation.toUpperCase()) {
		case "START":
			start_process(tc_id, dataMap);
			
		case "LAUNCH":
			open_Browser(value);
			break;
			
		case "GO_TO_URL":
			enter_URL(p.getProperty(objectName));
			break;
			
		case "SET_TEXT":
			if(value.contentEquals("user_name")){
				enter_Text(objectType, p.getProperty(objectName), p.getProperty(value));
			} else if(value.contentEquals("user_pwd")){
				enter_Text(objectType, p.getProperty(objectName), p.getProperty(value));
			} else {
				enter_Text(objectType, p.getProperty(objectName), value);
			}
			break;
			
		case "CLICK_BUTTON":
			click_On_Button(objectType, p.getProperty(objectName));
			break;
			
		case "CLICK_LINK":
			click_On_Link(objectType, p.getProperty(objectName));
			break;
			
		case "WAIT":
			sync(Integer.parseInt(value));
			break;
			
		case "UPLOADFILE":
			upload_file(value);
			break;
			
		case "TERMINATE":
			close_Browser();
			break;
			
		case "VERIFY_TEXT":
			text_verification(objectType, p.getProperty(objectName), value);
			break;
			
		case "VERIFYPARTIALTEXT":
			partial_text_verification(objectType, p.getProperty(objectName), value);
			break;
			
		case "WAIT_FOR_ELEMENT":
			wait_for_element(objectType, p.getProperty(objectName));
			break;
			
		case "WAIT_FOR_LOADING":
			wait_for_loading(objectType, p.getProperty(objectName));
			break;
			
		case "VERIFY_ELEMENT_DISPLAYED":
			verify_element_displayed(objectType, p.getProperty(objectName));
			break;
			
		case "HANDLEMENU":
			String objectParts[] = objectName.split(",");
			String objectName1 = objectParts[0];
			String objectName2 = objectParts[1];
			String typeParts[] = objectType.split(",");
			String objectType1 = typeParts[0];
			String objectType2 = typeParts[1];
			operation_mousehover_event(objectType1, objectType2, p.getProperty(objectName1), p.getProperty(objectName2));
			break;
			
		case "SPLCLICKLINK":
			spl_click_on_link(objectType, p.getProperty(objectName));
			break;
			
		case "SELECT_CHECKBOX_TABLE_CONTENT_BY_ROW":
			select_checkbox_table_content_by_rownum(objectType, p.getProperty(objectName), value);
			break;
			
		case "SELECT_CHECKBOX_TABLE_CONTENT_BY_TEXT":
			select_checkbox_table_content_by_text(objectType, p.getProperty(objectName), value);
			break;
			
		case "CHANGE_CSS_ATTRIBUTE":
			change_css_attribute(objectType, p.getProperty(objectName), value);
			break;
			
		case "VERIFY_TABLE_CONTENT":
			verify_table_content(objectType, p.getProperty(objectName), value);
			break;
		default:
			break;
		}
	}
	
	public void close_Browser(){
		try{
			driver.close();
			driver.quit();
			ResultUtil.report("PASS", "Verify execution completed", "Browser should be closed and excution should be completed", "Browser is closed and excution is completed", driver);
		} catch (Exception e){
			ResultUtil.report("FAIL", "Exception occured at 'close_Browser' method", "", e.getMessage(), driver);
		}
	}
}
