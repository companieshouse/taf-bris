package uk.gov.companieshouse.taf.steps;

import javax.inject.Inject;

import org.openqa.selenium.WebDriver;
import uk.gov.companieshouse.taf.pages.LandingPage;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.Given;

import static org.junit.Assert.assertEquals;

public class ExampleSteps {

    @Inject
    private LandingPage landingPage;

    @Inject
    private WebDriver webDriver;

    @Given("^I visit \"([^\"]+)\"")
    public void i_visit(String url) throws Throwable {
        webDriver.navigate().to(url);
    }

    @Then("^I should be on the Companies House home page$")
    public void i_should_see_the_main_menu() throws Throwable {
        assertEquals("Companies House - GOV.UK", webDriver.getTitle());
    }
}
