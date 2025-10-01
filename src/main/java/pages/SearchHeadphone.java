package pages;

import java.util.*;
import java.util.Locale;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.aventstack.extentreports.Status;

import utils.ConfigReader;
import utils.ExcelUtils;
import utils.WaitUtils;
import base.BaseClass;

public class SearchHeadphone extends BaseClass {

    WebDriver driver;
    WaitUtils wait;

    @FindBy(xpath = "//button[@alt='Continue shopping']")
    private WebElement continueBtn;

    @FindBy(id = "twotabsearchtextbox")
    private WebElement searchBox;

    @FindBy(xpath = "//div[@data-component-type='s-search-result']")
    private List<WebElement> searchResult;

    @FindBy(xpath = ".//h2[text()='Results']/following::span[contains(text(),'Headphones') or contains(text(),'Bluetooth')]/following::a/span[@class='a-price']")
    private List<WebElement> productPrice;

    @FindBy(xpath = ".//h2[text()='Results']/following::span[contains(text(),'Headphones') or contains(text(),'Bluetooth')]/ancestor::h2")
    private List<WebElement> productName;

    public SearchHeadphone() {
        this.driver = BaseClass.driver;
        this.wait = new WaitUtils(driver);
        PageFactory.initElements(driver, this);
    }

    public void clickContinueButton() {
        wait.waitForElementToBeClickable(continueBtn, 5, 10);
        continueBtn.click();
    }

    public void searchHeadphones(String keyword) {
        wait.waitForElementToBeVisible(searchBox, 5, 10);
        searchBox.clear();
        searchBox.sendKeys(keyword + Keys.ENTER);
    }

    public List<WebElement> getProductContainers() {
        wait.waitForElementsToBeVisible(searchResult, 20, 500);
        return searchResult;
    }

    public String getProductTitle(WebElement product) {
        WebElement nameElement = product.findElement(By.xpath(
                ".//h2[text()='Results']/following::span[contains(text(),'Headphones') or contains(text(),'Bluetooth')]"));
        wait.waitForElementToBeVisible(nameElement, 20, 500);
        return nameElement.getText();
    }


    public static String extractBrand(String productName, List<String> topBrands) {
        if (productName == null || productName.isEmpty() || topBrands == null || topBrands.isEmpty()) {
            return "";
        }

        String lowerProduct = productName.toLowerCase(Locale.ROOT).replaceAll("[^a-z0-9 ]", " ");

        topBrands.sort((a, b) -> b.length() - a.length()); // longest brand first

        for (String brand : topBrands) {
            if (brand == null || brand.trim().isEmpty()) continue;
            String lowerBrand = brand.toLowerCase(Locale.ROOT).replaceAll("[^a-z0-9 ]", " ").trim();

            if (lowerProduct.contains(lowerBrand)) {
                return brand.trim();
            }
        }
        return "";
    }


    public void getProductNameAndPriceSorted(String filePath, String sheetName, String columnName) {
        int minPrice = Integer.parseInt(ConfigReader.getProperty("minPrice"));
        int maxPrice = Integer.parseInt(ConfigReader.getProperty("maxPrice"));

        List<String> topBrands = ExcelUtils.getColumnValues(filePath, sheetName, columnName);

        test.log(Status.INFO, "Scanning headphones between ₹" + minPrice + " – ₹" + maxPrice
                + " and filtering top brands from sheet: " + topBrands);

        List<Map<String, String>> matchedProducts = new ArrayList<>();

        try {
            for (int i = 0; i < productName.size(); i++) {
                String nameOfProduct = productName.get(i).getText();
                String priceOfProduct = productPrice.get(i).getText();
                String matchedBrand = extractBrand(nameOfProduct, new ArrayList<>(topBrands));
                int price = Integer.parseInt(priceOfProduct.replaceAll("[^0-9]", ""));

                if (!matchedBrand.isEmpty() && price >= minPrice && price <= maxPrice) {
                    Map<String, String> product = new HashMap<>();
                    product.put("brand", matchedBrand);
                    product.put("name", nameOfProduct);
                    product.put("price", priceOfProduct);
                    matchedProducts.add(product);
                }
            }

            matchedProducts.sort(Comparator.comparingInt(p -> topBrands.indexOf(p.get("brand"))));

            for (Map<String, String> p : matchedProducts) {
                System.out.println(p.get("brand") + " --> " + p.get("price"));
                test.log(Status.INFO, p.get("brand") + " --> " + p.get("price"));
            }

        } catch (Exception e) {
            test.log(Status.FAIL, "Error " + e);
        }
    }
}
