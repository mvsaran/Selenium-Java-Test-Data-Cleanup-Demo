package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class ContactDetailsPage {
    private WebDriver driver;
    private WebDriverWait wait;

    private By deleteButton = By.id("delete");

    public ContactDetailsPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void deleteContact() {
        wait.until(ExpectedConditions.elementToBeClickable(deleteButton)).click();
        driver.switchTo().alert().accept();
    }
}
