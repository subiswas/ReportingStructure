package actions;

import common.ExtendedLibrary;
import intialize.Prerequsite;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.firefox.internal.ProfilesIni;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.internal.Locatable;
import org.openqa.selenium.remote.Augmenter;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import util.StringUtils;

import java.net.URL;
import java.util.concurrent.TimeUnit;


public class Browser {
    protected static WebDriver driver;
    protected static String BrowserName;

    public static WebDriver launchURL(String strMachine, String strPort, String strBrowser, String strURL) {
        DesiredCapabilities capability = null;
        URL url = null;
        try {
            url = new URL("http://" + strMachine + ":" + strPort + "/wd/hub");
            if (strBrowser.equalsIgnoreCase("FIREFOX")) {
                BrowserName = BrowserType.FireFox.getType();
                FirefoxProfile profile = new ProfilesIni().getProfile("default");
                capability = DesiredCapabilities.firefox();
                capability.setCapability(FirefoxDriver.PROFILE, profile);
               // capability.setCapability("network.automatic-ntlm-auth.trusted-uris", "https://nasso.wiley.com");
            } else if (strBrowser.equalsIgnoreCase("IE")) {
                BrowserName = BrowserType.InternetExplore.getType();
                capability = DesiredCapabilities.internetExplorer();
                capability.setCapability("ignoreZoomSetting", true);
                capability.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
//                capabilities.setCapability(InternetExplorerDriver.IE_SWITCHES, "-private");
                capability.setCapability(InternetExplorerDriver.FORCE_CREATE_PROCESS, true);
                capability.setCapability(InternetExplorerDriver.IE_ENSURE_CLEAN_SESSION, true);
                capability.setCapability(CapabilityType.SUPPORTS_ALERTS, true);
                capability.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
                capability.setCapability(CapabilityType.UNEXPECTED_ALERT_BEHAVIOUR, UnexpectedAlertBehaviour.ACCEPT);

            } else if (strBrowser.equalsIgnoreCase("CHROME")) {
                BrowserName = BrowserType.Chrome.getType();
                capability = DesiredCapabilities.chrome();
            } else if (strBrowser.equalsIgnoreCase("SAFARI")) {
                BrowserName = BrowserType.Safari.getType();
                capability = DesiredCapabilities.safari();
            }
            driver = new RemoteWebDriver(url, capability);

//            driver = new Augmenter().augment(driver);
            driver.manage().window().maximize();
            driver.get(strURL);
           /* if (StringUtils.compare(strBrowser, "IE")) {
                waitInMilliSeconds(2500);
                driver.get("javascript:document.getElementById('overridelink').click();");
            }*/
            Sync.waitForSeconds(5, driver);
            driver.navigate().refresh();
            ResultUtil.report("PASS", "Opened <<" + strURL + ">> URL on " + strBrowser + " browser Machine:" + strMachine, "", "", driver);
        } catch (Exception e) {
            ResultUtil.report("FAIL", "Unable to open URL-Exception occurred:" + e.getMessage(), "", "", driver);
        }
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        return driver;
    }

    public static WebDriver navigateURL(WebDriver driver, String strURL) {
        try {
            driver.manage().deleteAllCookies();
            driver.navigate().to(strURL);
            ResultUtil.report("PASS", "Navigated <<" + strURL + ">> URL","","", driver);
            Sync.waitForSeconds(5, driver);
        } catch (Exception e) {
            ResultUtil.report("FAIL", "Unable to navigate URL-Exception occurred:" + e.getMessage(), "", "", driver);
        }
        return driver;
    }

    public static boolean runScript(WebDriver driver, String strScript) {
        boolean blResult = false;
        try {
            JavascriptExecutor javaScriptExecutor = ((JavascriptExecutor) driver);
            javaScriptExecutor.executeScript(strScript);
            ResultUtil.report("PASS", "Executed javascript script:<<" + strScript + ">>","", "", driver);
            blResult = true;
        } catch (Exception e) {
            ResultUtil.report("FAIL", "Unable to execute script-Exception occurred:" + e.getMessage(), "", "", driver);
        }
        return blResult;
    }

    public static boolean scrollToObject(String strLogicalName, WebElement element, WebDriver driver) {
        boolean blResult = false;
        if (element.isDisplayed()) {
            if (element.isEnabled()) {
                try {
                    int y = ((Locatable) element).getCoordinates().onPage().getY();
                    ((JavascriptExecutor) driver).executeScript("window.scrollBy(0," + y + ");");
                    try {
                        Thread.sleep(1000);
                    } catch (Exception e) {
                    }
                    blResult = true;
                    ResultUtil.report("PASS", "Scrolled to <<" + strLogicalName + ">> object", "", "", driver);
                } catch (Exception e) {
                    ResultUtil.report("FAIL", "Unable to scroll to <<" + strLogicalName + ">> object Exception occurred:" + e.getMessage(), "", "", driver);
                }

            } else {
                ResultUtil.report("FAIL", "Unable to scroll to-<<" + strLogicalName + ">> object is disabled", "", "", driver);
            }
        } else {
            ResultUtil.report("FAIL", "Unable to scroll to-<<" + strLogicalName + ">> object does not exist", "", "", driver);
        }
        return blResult;
    }

    //Close the driver
    public static void closeDriver(WebDriver driver) {
        driver.close();
    }

    public static void clearAllCookies(WebDriver driver) {
        //Clear cookies
        driver.manage().deleteAllCookies();
    }

    public static boolean horizontalScrollToObject(String strLogicalName, WebElement element, WebDriver driver) {
        boolean blResult = false;
        if (element.isDisplayed()) {
            if (element.isEnabled()) {
                try {
                    int x = ((Locatable) element).getCoordinates().onPage().getX();
                    ((JavascriptExecutor) driver).executeScript("window.scrollBy(0," + x + ");");
                    try {
                        Thread.sleep(1000);
                    } catch (Exception e) {
                    }
                    blResult = true;
                    ResultUtil.report("PASS", "Scrolled to <<" + strLogicalName + ">> object", "", "", driver);
                } catch (Exception e) {
                    ResultUtil.report("FAIL", "Unable to scroll to <<" + strLogicalName + ">> object Exception occurred:" + e.getMessage(), "", "", driver);
                }

            } else {
                ResultUtil.report("FAIL", "Unable to scroll to-<<" + strLogicalName + ">> object is disabled", "", "", driver);
            }
        } else {
            ResultUtil.report("FAIL", "Unable to scroll to-<<" + strLogicalName + ">> object does not exist", "", "", driver);
        }
        return blResult;
    }

    public static void waitInMilliSeconds(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            throw new AssertionError("InterruptedException occurred while calling Thread.sleep for " + milliseconds + " milliseconds." + e);
        }
    }

    public static WebDriver launchURLWithoutWindowMaximize(String strMachine, String strPort, String strBrowser, String strURL) {
        WebDriver driver = null;
        DesiredCapabilities capability = null;
        URL url = null;
        try {
            url = new URL("http://" + strMachine + ":" + strPort + "/wd/hub");
            if (strBrowser.equalsIgnoreCase("FIREFOX")) {
                capability = DesiredCapabilities.firefox();
            } else if (strBrowser.equalsIgnoreCase("IE")) {
                capability = DesiredCapabilities.internetExplorer();
                capability.setCapability("ignoreZoomSetting", true);
                capability.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
//                capabilities.setCapability(InternetExplorerDriver.IE_SWITCHES, "-private");
                capability.setCapability(InternetExplorerDriver.FORCE_CREATE_PROCESS, true);
                capability.setCapability(InternetExplorerDriver.IE_ENSURE_CLEAN_SESSION, true);
                capability.setCapability(CapabilityType.SUPPORTS_ALERTS, true);
                capability.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
                capability.setCapability(CapabilityType.UNEXPECTED_ALERT_BEHAVIOUR, UnexpectedAlertBehaviour.ACCEPT);
            } else if (strBrowser.equalsIgnoreCase("CHROME")) {
                capability = DesiredCapabilities.chrome();
            } else if (strBrowser.equalsIgnoreCase("SAFARI")) {
                capability = DesiredCapabilities.safari();
            }
            driver = new RemoteWebDriver(url, capability);

            driver = new Augmenter().augment(driver);
            driver.get(strURL);
            Sync.waitForSeconds(5, driver);


            ResultUtil.report("PASS", "Opened <<" + strURL + ">> URL on " + strBrowser + " browser Machine:" + strMachine,"", "", driver);
        } catch (Exception e) {
            ResultUtil.report("FAIL", "Unable to open URL-Exception occurred:" + e.getMessage(), "", "", driver);
        }
        return driver;
    }

    public WebDriver getDriver() {
        return driver;
    }

    public enum BrowserType {
        FireFox("*firefox"),
        Chrome("*chrome"),
        InternetExplore("*iexplore"),
        Safari("*safari"),
        Opera("*opera"),
        Android("*android"),;

        private String type;

        BrowserType(String type) {
            this.type = type;
        }

        public static BrowserType getBrowserType(String type) {
            if (type == null) {
                return BrowserType.FireFox;
            }
            for (BrowserType browserType : BrowserType.values()) {
                if (browserType.getType().contains(type.toLowerCase())) {
                    return browserType;
                }
            }
            return BrowserType.FireFox;
        }

        public String getType() {
            return this.type;
        }

    }

}
