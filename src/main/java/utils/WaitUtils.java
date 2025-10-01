package utils;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;

public class WaitUtils {

    private WebDriver driver;

    public WaitUtils(WebDriver driver) {
        if (driver == null) {
            throw new IllegalArgumentException("WebDriver instance is null. Initialize driver first.");
        }
        this.driver = driver;
    }

    public WebElement waitForElementToBeVisible(WebElement element, int timeOutInSeconds, int pollingInMillis) {
        if (element == null) {
            throw new IllegalArgumentException("WebElement is null.");
        }

        FluentWait<WebDriver> wait = new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(timeOutInSeconds))
                .pollingEvery(Duration.ofMillis(pollingInMillis))
                .ignoring(NoSuchElementException.class);

        return wait.until(ExpectedConditions.visibilityOf(element));
    }

    public List<WebElement> waitForElementsToBeVisible(List<WebElement> elements, int timeOutInSeconds, int pollingInMillis) {
        if (elements == null || elements.isEmpty()) {
            throw new IllegalArgumentException("Elements list is null or empty.");
        }

        FluentWait<WebDriver> wait = new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(timeOutInSeconds))
                .pollingEvery(Duration.ofMillis(pollingInMillis))
                .ignoring(NoSuchElementException.class);

        return wait.until(ExpectedConditions.visibilityOfAllElements(elements));
    }

    public WebElement waitForElementToBeClickable(WebElement element, int timeoutInSeconds, int pollingInMillis) {
        if (element == null) {
            throw new IllegalArgumentException("WebElement is null.");
        }

        FluentWait<WebDriver> wait = new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(timeoutInSeconds))
                .pollingEvery(Duration.ofMillis(pollingInMillis))
                .ignoring(NoSuchElementException.class);

        return wait.until(ExpectedConditions.elementToBeClickable(element));
    }
}
