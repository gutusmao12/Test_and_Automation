package com.herokuapp.theinternet;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

public class LoginTests {

	private WebDriver driver;

	@Parameters({ "browser" })
	@BeforeMethod(alwaysRun = true)
	private void setUp(String browser) {
		// Create driver
		switch (browser) {
		case "chrome":
			System.setProperty("webdriver.chrome.driver", "src/main/resources/chromedriver.exe");
			driver = new ChromeDriver();
			break;

		case "firefox":
			System.setProperty("webdriver.gecko.driver", "src/main/resources/geckodriver.exe");
			driver = new FirefoxDriver();
			break;

		default:
			System.out.println("Do not know how to star " + browser + ", starting chrome instead");
			System.setProperty("webdriver.chrome.driver", "src/main/resources/chromedriver.exe");
			driver = new ChromeDriver();
			break;
		}
		// open test page
		String url = "http://the-internet.herokuapp.com/login";
		driver.get(url);

		// maximize browser window
		driver.manage().window().maximize();
	}

	@Test(priority = 1, groups = { "positiveTests", "smokeTests" })
	public void positiveLoginTest() {
		// enter username
		WebElement username = driver.findElement(By.id("username"));
		username.sendKeys("tomsmith");

		// enter password
		WebElement password = driver.findElement(By.name("password"));
		password.sendKeys("SuperSecretPassword!");

		// click login button
		WebElement logInButton = driver.findElement(By.tagName("button"));
		logInButton.click();

		// verification:

		// new url
		String expectedUrl = "http://the-internet.herokuapp.com/secure";
		String actualUrl = driver.getCurrentUrl();
		Assert.assertEquals(actualUrl, expectedUrl, "Actual page url is not the same as expected");

		// logout button is visible
		WebElement logOutButton = driver.findElement(By.xpath("//a[@class='button secondary radius']"));
		Assert.assertTrue(logOutButton.isDisplayed(), "Log Out button is not visible");

		// successful login message
		WebElement successMessage = driver.findElement(By.cssSelector("#flash"));
		String expectedMessage = "You logged into a secure area!";
		String actualMessage = successMessage.getText();
		Assert.assertTrue(actualMessage.contains(expectedMessage),
				"Actual message does not contain a expected message.");
	}

	@Parameters({ "username", "password", "expectedMessage" })
	@Test(priority = 2, groups = { "negativeTests", "smokeTests" })
	public void negativeLoginTest(String username, String password, String expectedErrorMessage) {
		// open test page
		String url = "http://the-internet.herokuapp.com/login";
		driver.get(url);

		// maximize browser window
		driver.manage().window().maximize();

		// enter username
		WebElement usernameElement = driver.findElement(By.id("username"));
		usernameElement.sendKeys(username);

		// enter password
		WebElement passwordElement = driver.findElement(By.name("password"));
		passwordElement.sendKeys(password);

		// click login button
		WebElement logInButton = driver.findElement(By.tagName("button"));
		logInButton.click();

		// verifications
		WebElement errorMessage = driver.findElement(By.id("flash"));
		String actualErrorMessage = errorMessage.getText();
		Assert.assertTrue(actualErrorMessage.contains(expectedErrorMessage),
				"Actual error message does not contain expected. \nActual: " + actualErrorMessage + "\nExpected: "
						+ expectedErrorMessage);
	}

	@AfterMethod(alwaysRun = true)
	private void tearDown() {
		// close browser
		driver.quit();
	}

}
