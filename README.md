# 🧹 Selenium Java - Test Data Cleanup Demo

[![Java](https://img.shields.io/badge/Java-11+-orange.svg)](https://www.oracle.com/java/)
[![Selenium](https://img.shields.io/badge/Selenium-4.27.0-green.svg)](https://www.selenium.dev/)
[![TestNG](https://img.shields.io/badge/TestNG-7.10.2-red.svg)](https://testng.org/)
[![Maven](https://img.shields.io/badge/Maven-3.9+-blue.svg)](https://maven.apache.org/)

A comprehensive demonstration of **automated test data cleanup** in Selenium Java projects using TestNG's `@AfterMethod` annotation. This project showcases best practices for ensuring test environments remain clean, regardless of test outcomes.

---

## 📋 Table of Contents
- [Overview](#overview)
- [Key Features](#key-features)
- [How Test Data Cleanup Works](#how-test-data-cleanup-works)
- [Project Structure](#project-structure)
- [Getting Started](#getting-started)
- [Running Tests](#running-tests)
- [Understanding the Demo](#understanding-the-demo)
- [Demo Video](#demo-video)
- [Author](#author)

---

## 🎯 Overview

In automated testing, **test data pollution** is a common problem where data created during tests persists in the system, causing:
- ❌ False positives/negatives in subsequent test runs
- ❌ Database bloat
- ❌ Unreliable test results
- ❌ Difficult debugging

This project demonstrates a **fail-safe cleanup mechanism** that ensures test data is removed **even when tests fail**.

---

## ✨ Key Features

- **Fail-Safe Cleanup**: Uses TestNG's `@AfterMethod(alwaysRun = true)` to guarantee cleanup execution
- **Page Object Model (POM)**: Clean, maintainable code architecture
- **Real-World Demo**: Tests against [Thinking Tester Contact List App](https://thinking-tester-contact-list.herokuapp.com/)
- **Comprehensive Logging**: Detailed console output showing each step
- **Two Test Scenarios**:
  - ✅ **Passing Test with Cleanup** (`CleanupSuccessTest`)
  - ❌ **Failing Test with Cleanup** (`CleanupDemoTest`)

---

## 🔧 How Test Data Cleanup Works

### The Problem
Without proper cleanup, test data accumulates:
```java
@Test
public void testCreateContact() {
    // Create contact
    contactPage.addContact("John", "Doe");
    // Test ends - data remains in system ❌
}
```

### The Solution
Using `@AfterMethod(alwaysRun = true)`:

```java
public class CleanupDemoTest extends BaseTest {
    private boolean isContactCreated = false;

    @Test
    public void testCreateContact() {
        // Create contact
        contactPage.addContact("John", "Doe");
        isContactCreated = true; // Track creation
        
        // Test logic (may pass or fail)
        Assert.assertTrue(someCondition);
    }

    @AfterMethod(alwaysRun = true)
    public void cleanupTestData() {
        if (isContactCreated) {
            // This ALWAYS runs, even if test fails ✅
            contactPage.deleteContact("John", "Doe");
            System.out.println("Cleanup successful!");
        }
    }
}
```

### Key Mechanisms

| Component | Purpose |
|-----------|---------|
| `@AfterMethod(alwaysRun = true)` | Ensures cleanup runs regardless of test outcome |
| `isContactCreated` flag | Tracks whether data was successfully created |
| `try-catch-finally` | Handles cleanup errors gracefully |
| Detailed logging | Provides visibility into cleanup process |

---

## 📁 Project Structure

```
Cleanupdata/
├── src/test/java/
│   ├── pages/                      # Page Object Model
│   │   ├── LoginPage.java          # Login functionality
│   │   ├── ContactListPage.java    # Contact list operations
│   │   ├── AddContactPage.java     # Add contact form
│   │   └── ContactDetailsPage.java # Contact deletion
│   └── tests/                      # Test classes
│       ├── BaseTest.java           # WebDriver setup/teardown
│       ├── CleanupSuccessTest.java # ✅ Passing test demo
│       └── CleanupDemoTest.java    # ❌ Failing test demo
├── pom.xml                         # Maven dependencies
├── testng.xml                      # TestNG suite configuration
└── README.md                       # This file
```

---

## 🚀 Getting Started

### Prerequisites
- **Java 11+** ([Download](https://adoptium.net/))
- **Maven 3.9+** ([Download](https://maven.apache.org/download.cgi))
- **Google Chrome** (latest version)

### Installation

1. **Clone the repository**
   ```bash
   git clone <your-repo-url>
   cd Cleanupdata
   ```

2. **Verify Java and Maven**
   ```bash
   java -version
   mvn -version
   ```

3. **Install dependencies**
   ```bash
   mvn clean install -DskipTests
   ```

---

## ▶️ Running Tests

### Run All Tests
```bash
mvn test
```

### Run Specific Test Class
```bash
# Run passing test demo
mvn test -Dtest=CleanupSuccessTest

# Run failing test demo
mvn test -Dtest=CleanupDemoTest
```

### Switch Between Demos
Edit `testng.xml` to choose which test to run:

```xml
<suite name="Cleanup Data Demo Suite">
    <test name="Cleanup Tests">
        <classes>
            <!-- Uncomment the test you want to run -->
            <class name="tests.CleanupSuccessTest"/>
            <!-- <class name="tests.CleanupDemoTest"/> -->
        </classes>
    </test>
</suite>
```

---

## 🎬 Understanding the Demo

### Scenario 1: Passing Test with Cleanup ✅

**Test**: `CleanupSuccessTest.java`

**Flow**:
1. Login to application
2. Create a new contact
3. Verify contact exists
4. **Test PASSES** ✅
5. Cleanup removes the contact

**Expected Output**:
```text
>>> STARTING TEST: testCleanupOnSuccessDemo
Step 1: Logging into the application...
Step 2: Navigating to Add Contact page...
Step 3: Adding a new contact...
SUCCESS: Contact created: Success1735300186 Test
Step 4: Verifying contact was created...
SUCCESS: Test passed! Contact is visible in the list.

>>> AFTER_METHOD: Entering cleanup block...
>>> STATUS: Data was created. Proceeding with cleanup...
Cleanup Step A: Finding the contact 'Success1735300186 Test' in the list...
Cleanup Step B: Deleting the contact...
>>> SUCCESS: Cleanup completed. The environment is clean.
```

**Result**: `BUILD SUCCESS` (if assertion passes) or `BUILD FAILURE` (if timing issues), but **cleanup always happens**.

---

### Scenario 2: Failing Test with Cleanup ❌

**Test**: `CleanupDemoTest.java`

**Flow**:
1. Login to application
2. Create a new contact
3. **Force test to FAIL** using `Assert.fail()`
4. Cleanup **still executes** and removes the contact

**Expected Output**:
```text
>>> STARTING TEST: testCleanupOnFailureDemo
Step 1: Logging into the application...
Step 2: Navigating to Add Contact page...
Step 3: Adding a new contact...
SUCCESS: Contact created: Cleanup1735300186 Demo
Step 4: Simulating a test failure (Assertion Failure)...

>>> AFTER_METHOD: Entering cleanup block...
>>> STATUS: Data was created. Proceeding with cleanup...
Cleanup Step A: Finding the contact 'Cleanup1735300186 Demo' in the list...
Cleanup Step B: Deleting the contact...
>>> SUCCESS: Cleanup completed. The environment is clean.
```

**Result**: `BUILD FAILURE` (as expected), but **cleanup happened successfully**.

---

## 🎥 Demo Video

> **Note**: A browser recording showing the cleanup process is available at:
> `cleanup_demo_video_1766836568606.webp`

The video demonstrates:
- Browser opening and navigating to the Contact List App
- Login process
- Contact creation
- Cleanup execution (deletion)

---

## 🔍 Key Takeaways

### Why `alwaysRun = true` is Critical

```java
@AfterMethod(alwaysRun = true)  // ✅ Runs even on failure
public void cleanupTestData() { ... }

@AfterMethod  // ❌ Skips cleanup if test fails
public void cleanupTestData() { ... }
```

### Best Practices Demonstrated

1. **Track Data Creation**: Use boolean flags to know when cleanup is needed
2. **Unique Identifiers**: Use timestamps to avoid conflicts (`"Contact" + System.currentTimeMillis()`)
3. **Graceful Error Handling**: Wrap cleanup in try-catch to log failures
4. **Detailed Logging**: Print each step for debugging
5. **Idempotent Cleanup**: Ensure cleanup can run multiple times safely

---

## 👨‍💻 Author

**Your Name**
- GitHub: [@yourusername](https://github.com/yourusername)
- LinkedIn: [Your Profile](https://linkedin.com/in/yourprofile)

---

## 📝 License

This project is for educational purposes and demonstration of test automation best practices.

---

## 🤝 Contributing

Contributions, issues, and feature requests are welcome! Feel free to check the issues page.

---

## ⭐ Show Your Support

If this project helped you understand test data cleanup, give it a ⭐!

---

**Happy Testing! 🚀**
