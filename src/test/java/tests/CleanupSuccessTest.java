package tests;

import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;
import pages.AddContactPage;
import pages.ContactDetailsPage;
import pages.ContactListPage;
import pages.LoginPage;

public class CleanupSuccessTest extends BaseTest {

    private String firstName = "Success" + System.currentTimeMillis();
    private String lastName = "Test";
    private boolean isContactCreated = false;

    @Test
    public void testCleanupOnSuccessDemo() {
        System.out.println(">>> STARTING TEST: testCleanupOnSuccessDemo");

        driver.get("https://thinking-tester-contact-list.herokuapp.com/");

        System.out.println("Step 1: Logging into the application...");
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login("test22@test.com", "ABCabc123");

        System.out.println("Step 2: Navigating to Add Contact page...");
        ContactListPage contactListPage = new ContactListPage(driver);

        try {
            contactListPage.clickAddContact();
        } catch (Exception e) {
            System.out.println("RETRY: Going directly to add contact page...");
            driver.get("https://thinking-tester-contact-list.herokuapp.com/addContact");
        }

        System.out.println("Step 3: Adding a new contact...");
        AddContactPage addContactPage = new AddContactPage(driver);
        addContactPage.addContact(firstName, lastName, "success." + System.currentTimeMillis() + "@example.com",
                "9876543210");

        isContactCreated = true;
        System.out.println("SUCCESS: Contact created: " + firstName + " " + lastName);

        System.out.println("Step 4: Verifying contact was created...");
        // Navigate back to contact list
        driver.get("https://thinking-tester-contact-list.herokuapp.com/contactList");

        // Verify the contact appears in the page
        Assert.assertTrue(driver.getPageSource().contains(firstName),
                "Contact should be visible in the list");

        System.out.println("SUCCESS: Test passed! Contact is visible in the list.");
        System.out.println("NOTE: Cleanup will still run to remove the test data.");
    }

    @AfterMethod(alwaysRun = true)
    public void cleanupTestData() {
        System.out.println("\n>>> AFTER_METHOD: Entering cleanup block...");
        if (isContactCreated) {
            System.out.println(">>> STATUS: Data was created. Proceeding with cleanup...");
            try {
                driver.get("https://thinking-tester-contact-list.herokuapp.com/contactList");

                ContactListPage contactListPage = new ContactListPage(driver);
                System.out.println(
                        "Cleanup Step A: Finding the contact '" + firstName + " " + lastName + "' in the list...");
                contactListPage.clickOnContactByName(firstName, lastName);

                System.out.println("Cleanup Step B: Deleting the contact...");
                ContactDetailsPage detailsPage = new ContactDetailsPage(driver);
                detailsPage.deleteContact();

                System.out.println(">>> SUCCESS: Cleanup completed. The environment is clean.");
            } catch (Exception e) {
                System.err.println(">>> ERROR: Cleanup failed: " + e.getMessage());
            } finally {
                isContactCreated = false;
            }
        } else {
            System.out.println(">>> STATUS: No data was created; skipping cleanup.");
        }
    }
}
