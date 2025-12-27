package tests;

import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;
import pages.AddContactPage;
import pages.ContactDetailsPage;
import pages.ContactListPage;
import pages.LoginPage;

public class CleanupDemoTest extends BaseTest {

    private String firstName = "Cleanup" + System.currentTimeMillis(); // Unique name
    private String lastName = "Demo";
    private boolean isContactCreated = false;

    @Test
    public void testCleanupOnFailureDemo() {
        System.out.println(">>> STARTING TEST: testCleanupOnFailureDemo");

        driver.get("https://thinking-tester-contact-list.herokuapp.com/");

        System.out.println("Step 1: Logging into the application...");
        LoginPage loginPage = new LoginPage(driver);
        // Using a test account.
        loginPage.login("test22@test.com", "ABCabc123");

        System.out.println("Step 2: Navigating to Add Contact page...");
        ContactListPage contactListPage = new ContactListPage(driver);

        // Wait for page to load after login
        try {
            contactListPage.clickAddContact();
        } catch (Exception e) {
            System.out.println("RETRY: Login might have failed or page slow. Trying to go directly to add contact...");
            driver.get("https://thinking-tester-contact-list.herokuapp.com/addContact");
        }

        System.out.println("Step 3: Adding a new contact...");
        AddContactPage addContactPage = new AddContactPage(driver);
        addContactPage.addContact(firstName, lastName, "cleanup." + System.currentTimeMillis() + "@example.com",
                "1234567890");

        // Mark as created so the cleanup logic knows there is something to delete
        isContactCreated = true;
        System.out.println("SUCCESS: Contact created: " + firstName + " " + lastName);

        System.out.println("Step 4: Simulating a test failure (Assertion Failure)...");
        // This assertion is designed to fail to demonstrate that cleanup still runs.
        Assert.fail("FORCED FAILURE: This test failed as expected to demonstrate automated data cleanup.");
    }

    /**
     * This method is the core of the cleanup demo.
     * The 'alwaysRun = true' attribute ensures that this method executes even if
     * the test fails.
     */
    @AfterMethod(alwaysRun = true)
    public void cleanupTestData() {
        System.out.println("\n>>> AFTER_METHOD: Entering cleanup block...");
        if (isContactCreated) {
            System.out.println(">>> STATUS: Data was created. Proceeding with cleanup...");
            try {
                // Ensure we are on the contact list page
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
                // In a real scenario, you might want to log this or retry.
            } finally {
                isContactCreated = false;
            }
        } else {
            System.out.println(">>> STATUS: No data was created; skipping cleanup.");
        }
    }
}
