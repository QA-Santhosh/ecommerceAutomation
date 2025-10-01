package tests;

import java.util.Arrays;
import java.util.List;

import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;

import com.aventstack.extentreports.Status;

import base.BaseClass;
import pages.SearchHeadphone;
import utils.ConfigReader;

public class HeadphoneTest extends BaseClass {
	
	String filePath = "src/test/resources/topBrand.xlsx";
	String sheetName = "BrandList"; 
	String columnName = "Brand";

    @Test
    public void getHeadphoneByPriceAndBrand() {

        driver.get(ConfigReader.getProperty("url"));

        SearchHeadphone searchHeadphone = new SearchHeadphone();

        searchHeadphone.searchHeadphones("Bluetooth Headphone");
        
        searchHeadphone.getProductNameAndPriceSorted(filePath,sheetName,columnName);

    }
}
