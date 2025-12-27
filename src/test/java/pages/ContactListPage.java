package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.util.List;

public class ContactListPage {
    private WebDriver driver;
    private WebDriverWait wait;

    private By addContactButton = By.id("add-contact");
    private By contactRows = By.className("contactTableBodyRow");
    private By logoutButton = By.id("logout");

    public ContactListPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void clickAddContact() {
        wait.until(ExpectedConditions.elementToBeClickable(addContactButton)).click();
    }

    public List<WebElement> getContacts() {
        return driver.findElements(contactRows);
    }

    public void clickOnContactByName(String firstName, String lastName) {
        By contactLink = By.xpath("//td[contains(text(), '" + firstName + " " + lastName + "')]");
        wait.until(ExpectedConditions.elementToBeClickable(contactLink)).click();
    }

    public void logout() {
        driver.findElement(logoutButton).click();
    }
}
