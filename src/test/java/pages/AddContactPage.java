package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class AddContactPage {
    private WebDriver driver;
    private WebDriverWait wait;

    private By firstNameField = By.id("firstName");
    private By lastNameField = By.id("lastName");
    private By birthdateField = By.id("birthdate");
    private By emailField = By.id("email");
    private By phoneField = By.id("phone");
    private By street1Field = By.id("street1");
    private By submitButton = By.id("submit");

    public AddContactPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void addContact(String firstName, String lastName, String email, String phone) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(firstNameField)).sendKeys(firstName);
        driver.findElement(lastNameField).sendKeys(lastName);
        driver.findElement(emailField).sendKeys(email);
        driver.findElement(phoneField).sendKeys(phone);
        driver.findElement(submitButton).click();
    }
}
