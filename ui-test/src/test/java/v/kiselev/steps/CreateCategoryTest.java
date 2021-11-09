package v.kiselev.steps;

import cucumber.api.java.After;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import v.kiselev.uiTest.DriverInitializer;

import static org.assertj.core.api.Assertions.assertThat;

public class CreateCategoryTest {

    private WebDriver webDriver = null;

    @Given("^I open web browser$")
    public void iOpenWebBrowser() throws Throwable {
        webDriver = DriverInitializer.getDriver();
    }

    @When("^I navigate to login\\.html page$")
    public void iNavigateToLoginHtmlPage() throws Throwable {
        Thread.sleep(3000);
        webDriver.get(DriverInitializer.getProperty("login.url"));
    }

    @When("^I click on login button$")
    public void iClickOnLoginButton() throws Throwable {
        Thread.sleep(3000);
        WebElement webElement = webDriver.findElement(By.id("btn-login"));
        webElement.click();
    }

    @When("^I provide username as \"([^\"]*)\" and password as \"([^\"]*)\"$")
    public void iProvideUsernameAsAndPasswordAs(String username, String password) throws Throwable {
        Thread.sleep(3000);
        WebElement webElement = webDriver.findElement(By.id("username"));
        webElement.sendKeys(username);
        webElement = webDriver.findElement(By.id("password"));
        webElement.sendKeys(password);
    }

    @When("^I navigate to category\\.html page$")
    public void iNavigateToCategoryHtmlPage() throws Throwable {
        Thread.sleep(3000);
        webDriver.get(DriverInitializer.getProperty("category.url"));
    }

    @When("^I add new category")
    public void createNewCategory() throws Throwable {
        Thread.sleep(3000);
        WebElement webElement = webDriver.findElement(By.id("add-new-category"));
        webElement.click();
    }

    @When("^I input new category as \"([^\"]*)\"\\")
    public void inputNewCategory(String category) throws Throwable {
        Thread.sleep(3000);
        WebElement webElement = webDriver.findElement(By.id("name"));
        webElement.sendKeys(category);
    }

    @Then("^category name should be")
    public void findNewCategory(String category) throws Throwable {
        Thread.sleep(3000);
        WebElement webElement = webDriver.findElement(By.cssSelector("#datatablesSimple > tbody"));
        assertThat(webElement.getText()).isEqualTo(category);
    }
    @After
    public void quitBrowser() {
        webDriver.quit();
    }
}
