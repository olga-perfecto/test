
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.*;
import org.openqa.selenium.interactions.internal.Coordinates;
import org.openqa.selenium.WebDriver.*;

import com.perfectomobile.selenium.*;
import com.perfectomobile.selenium.api.*;
import com.perfectomobile.selenium.by.*;
import com.perfectomobile.selenium.dom.*;
import com.perfectomobile.selenium.nativeapp.*;
import com.perfectomobile.selenium.visual.*;
import com.perfectomobile.selenium.output.*;
import com.perfectomobile.selenium.options.*;
import com.perfectomobile.selenium.options.rotate.*;
import com.perfectomobile.selenium.options.touch.*;
import com.perfectomobile.selenium.options.visual.*;
import com.perfectomobile.selenium.options.visual.image.*;
import com.perfectomobile.selenium.options.visual.text.*;

import com.perfectomobile.httpclient.MediaType;
import com.perfectomobile.httpclient.utils.FileUtils;


public class PerfectoSampleWebpageTest {
	
	public static void main(String[] args) {

		System.out.println("Run started");
		
		// The default empty constructor of MobileDriver should be used when running the code inside Eclipse.
		// The user must have the ECLIPSE role in this case.
		// Otherwise, use the constructor that receives the host, user and password. E.g.
		// MobileDriver driver = new MobileDriver(host, user, password);
		MobileDriver driver = new MobileDriver();

		try {
			MobileDeviceFindOptions options = new MobileDeviceFindOptions();
			options.setDescription("olga");
			options.setOS(MobileDeviceOS.IOS);
			IMobileDevice device = driver.findDevice(options);
			
			device.open();
			
			device.home();
			
			IMobileWebDriver browser = device.getDOMDriver(MobileBrowserType.OS);
			browser.get("http://nxc.co.il/demoaut/index.php");
			
			WebElement usernameField = browser.findElementByName("username");
			WebElement passwordField = browser.findElementByName("password");
			WebElement signInButton = browser.findElementByLinkText("Sign in");
			
			usernameField.sendKeys("John");
			passwordField.sendKeys("Perfecto1");
			signInButton.click();
			
			IMobileWebDriver visualDriver = device.getVisualDriver();
			visualDriver.manage().timeouts().implicitlyWait(40, TimeUnit.SECONDS);
		
			try {
				visualDriver.findElementByPartialLinkText("Welcome back John");
			}
			catch (NoSuchElementException e) {
				System.out.println("'Welcome back John' text not found");
				throw e;
			}
			
			//download media from the repository
			String repositoryDownloadKey = "PRIVATE:Binck/logo_binckbank_nl.png";
			InputStream fileDownload = driver.downloadMedia(repositoryDownloadKey);
			
			if (fileDownload != null) {
				File imageFile = new File ("image.png");
				FileUtils.write(fileDownload, imageFile);				
			}
			
			//upload media into repository
			String repositoryUploadKey = "PRIVATE:new_image.png";
			File imageFile = new File ("image.png");
			driver.uploadMedia(repositoryUploadKey, imageFile);
			
			//delete media from repository
			String repositoryDeleteKey = "PRIVATE:new_image.png";
			driver.deleteMedia(repositoryDeleteKey);
			
			
			
			
			
			device.home();
			
			device.close();
			
			
			
			
         // write your code here
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			driver.quit();
			downloadReport(driver);
		}
		System.out.println("Run ended");
	}
	
	private static void sleep(long millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
		}
	}
	
	/*
     * Download the report to a specified location.
     * This method should be used after calling driver.quit().
     */
    private static void downloadReport(IMobileDriver driver) {
        InputStream reportStream = driver.downloadReport(MediaType.PDF);
        if (reportStream != null) {
            String path = "report.pdf";
            File reportFile = new File(path);
            try {
                FileUtils.write(reportStream, reportFile);
            } catch (IOException e) {
                System.out.println("Failed to write report to path: " + path + " - " + e.getMessage());
            }
        }
    }
}
