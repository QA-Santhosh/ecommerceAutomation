package base;

import org.openqa.selenium.WebDriver;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.ITestResult;
import org.testng.annotations.*;
import utils.ConfigReader;
import utils.ExtentManager;
import utils.ScreenshotUtil;
import io.github.bonigarcia.wdm.WebDriverManager;
import java.lang.reflect.Method;
import java.time.Duration;

public class BaseClass {

	public static WebDriver driver;
	public static ExtentReports extent;
	public static ExtentTest test;

	@BeforeSuite
	public void startReport() {
		extent = ExtentManager.getInstance();
	}

	@BeforeMethod
	@Parameters("browser")
	public void setUp(@Optional("browser") String browser, Method method) {
		test = extent.createTest(method.getName());
		String br = browser.equals("") ? ConfigReader.getProperty("browser") : browser;

		switch (br.toLowerCase()) {

		case "chrome":
			WebDriverManager.chromedriver().setup();
			driver = new ChromeDriver();
			break;

		case "firefox":
			WebDriverManager.firefoxdriver().setup();
			driver = new FirefoxDriver();
			break;
		case "edge":
			WebDriverManager.edgedriver().setup();
			driver = new EdgeDriver();
			break;
		default:
			throw new IllegalArgumentException("Browser not supported: " + br);

		}
		
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
		driver.get(ConfigReader.getProperty("url"));

	}
	
	@AfterMethod
	public void tearDown(ITestResult result) {
		try {
			
			if(result.getStatus() == ITestResult.FAILURE) {
				String screenshotPath = ScreenshotUtil.takeScreenshot(result.getName());
				test.fail("Test Failed: "+ result.getThrowable());
				test.addScreenCaptureFromPath(screenshotPath);
			}else if(result.getStatus() == ITestResult.SUCCESS) {
				test.pass("Test Passed");
			}else {
				test.skip("Test Skipped");
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		finally {
			if(driver!=null) {
				driver.quit();
			}
		}
	}
	
	@AfterSuite
	public void endReport() {
		extent.flush();
	}

}
