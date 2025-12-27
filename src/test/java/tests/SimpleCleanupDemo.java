package tests;

import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Simple demonstration of test data cleanup using TestNG's @AfterMethod.
 * This test simulates data creation and cleanup without external dependencies.
 */
public class SimpleCleanupDemo {

    // Simulated "database" to track created test data
    private static List<String> testDataStore = new ArrayList<>();
    private String createdData = null;

    @Test
    public void testDataCleanupOnSuccess() {
        System.out.println("\n========================================");
        System.out.println("TEST 1: Cleanup After SUCCESSFUL Test");
        System.out.println("========================================\n");

        // Step 1: Create test data
        createdData = "TestUser_" + System.currentTimeMillis();
        testDataStore.add(createdData);
        System.out.println("✓ Step 1: Created test data: " + createdData);
        System.out.println("✓ Current data store size: " + testDataStore.size());

        // Step 2: Perform test logic (this passes)
        System.out.println("✓ Step 2: Performing test assertions...");
        Assert.assertTrue(testDataStore.contains(createdData), "Data should exist");

        System.out.println("✓ Step 3: Test PASSED");
        System.out.println("\n>>> Cleanup will run next...\n");
    }

    @Test
    public void testDataCleanupOnFailure() {
        System.out.println("\n========================================");
        System.out.println("TEST 2: Cleanup After FAILED Test");
        System.out.println("========================================\n");

        // Step 1: Create test data
        createdData = "TestUser_" + System.currentTimeMillis();
        testDataStore.add(createdData);
        System.out.println("✓ Step 1: Created test data: " + createdData);
        System.out.println("✓ Current data store size: " + testDataStore.size());

        // Step 2: Simulate test failure
        System.out.println("✗ Step 2: Forcing test to FAIL...");
        System.out.println("\n>>> Cleanup will run next (even though test failed)...\n");

        Assert.fail("INTENTIONAL FAILURE: Demonstrating cleanup on failure");
    }

    /**
     * This method demonstrates the CRITICAL cleanup pattern.
     * The 'alwaysRun = true' ensures it executes even when tests fail.
     */
    @AfterMethod(alwaysRun = true)
    public void cleanupTestData() {
        System.out.println("┌─────────────────────────────────────┐");
        System.out.println("│   @AfterMethod CLEANUP TRIGGERED   │");
        System.out.println("└─────────────────────────────────────┘");

        if (createdData != null) {
            System.out.println(">>> Cleaning up test data: " + createdData);
            System.out.println(">>> Data store size BEFORE cleanup: " + testDataStore.size());

            // Remove the test data
            boolean removed = testDataStore.remove(createdData);

            if (removed) {
                System.out.println("✓ SUCCESS: Data removed from store");
                System.out.println("✓ Data store size AFTER cleanup: " + testDataStore.size());
                System.out.println("✓ Environment is CLEAN\n");
            } else {
                System.err.println("✗ ERROR: Failed to remove data");
            }

            createdData = null;
        } else {
            System.out.println(">>> No data to clean up\n");
        }
    }
}
