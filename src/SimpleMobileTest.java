import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.remote.*;

import com.sun.xml.internal.messaging.saaj.util.Base64;

public class SimpleMobileTest {
	
	public static void main(String[] args) throws MalformedURLException, UnsupportedEncodingException {

		System.out.println("Run started");
		String browserName = "safari";
		DesiredCapabilities capabilities = new DesiredCapabilities(browserName, "", Platform.ANY);
		String host = "demo.perfectomobile.com";
		String user = URLEncoder.encode("olgas@perfectomobile.com");
		String password = URLEncoder.encode("Tekhnenko0615");
		//capabilities.setCapability("platformName", "Android");
		//capabilities.setCapability("platformVersion", "4.4");
		String deviceID="DBF70BF4BE6919E583D19619B6DC773A37269561";
		capabilities.setCapability("deviceName", deviceID);
	
		RemoteWebDriver driver = new RemoteWebDriver(new URL("https://" + user + ':' + password + '@' + host + "/nexperience/wd/hub"), capabilities);
		driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
		
		
		try {
         // write your code here
			driver.navigate().to("http://m.bbc.co.uk");
			driver.findElement(By.xpath("/html[1]/body[1]/header[1]/div[1]/div[1]/nav[1]/div[1]/ul[1]/li[2]/a[1]")).click();
			sleep(5000);
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				
				driver.close();
			
				downloadReport(driver, "html", "C:\\temp\\selenium\\reports\\java\\report", ".html", deviceID);
				
			} catch (Exception repException) {
				repException.printStackTrace();
			}
			
			driver.quit();
		}
		
		System.out.println("Run ended");
	}

    private static void downloadReport(RemoteWebDriver driver, String type, String fileName, String suffix, String deviceID)
    { 
    	try {
    		
			DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd-HHmmss");
			Calendar cal = Calendar.getInstance();
			String reportTime = dateFormat.format(cal.getTime());
    		
            String command = "mobile:report:download"; 
            Map<String, Object> Parms = new HashMap<String, Object>();
            Parms.put("type", type); 
            String report = (String) driver.executeScript(command, Parms);
            String reportBytes = Base64.base64Decode(report);
            File reportFile = new File(fileName + "-" + deviceID + "-" +reportTime + suffix);
            FileUtils.write(reportFile, reportBytes, "UTF-8");
    		}
    		catch (Exception repEx) {
    			repEx.printStackTrace();
    		}
    }
	
	private static void sleep(long millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
		}
	}

	private void switchToContext(RemoteWebDriver driver, String context) {
		RemoteExecuteMethod executeMethod = new RemoteExecuteMethod(driver);
		Map<String,String> params = new HashMap<String,String>();
		params.put("name", context);
		executeMethod.execute(DriverCommand.SWITCH_TO_CONTEXT, params);
	}
	
	private String getCurrentContextHandle(RemoteWebDriver driver) {		  
		RemoteExecuteMethod executeMethod = new RemoteExecuteMethod(driver);
		String context =  (String) executeMethod.execute(DriverCommand.GET_CURRENT_CONTEXT_HANDLE, null);
		return context;
	}
	
	private List<String> getContextHandles(RemoteWebDriver driver) {		  
		RemoteExecuteMethod executeMethod = new RemoteExecuteMethod(driver);
		List<String> contexts =  (List<String>) executeMethod.execute(DriverCommand.GET_CONTEXT_HANDLES, null);
		return contexts;
	}
}