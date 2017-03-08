package uk.gov.companieshouse.taf.hooks;

import javax.inject.Inject;

import io.magentys.cinnamon.conf.Env;
import uk.gov.companieshouse.taf.pages.LandingPage;
import cucumber.api.java.Before;

import static io.magentys.cinnamon.webdriver.Browser.open;
import static io.magentys.cinnamon.webdriver.conditions.ElementConditions.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class Hooks {

    private final Env env;
    private final LandingPage landingPage;

    @Inject
    public Hooks(final Env env, final LandingPage landingPage) {
        this.env = env;
        this.landingPage = landingPage;
    }

    // The website URL is automatically navigated to before each scenario tagged with @web
    @Before("@web")
    public void openWebSite() throws Throwable {
        open(env.config.getString("base-url"));
        assertThat("Cannot open the website", landingPage.mainMenu.waitUntil(displayed).isPresent(), equalTo(true));
    }
}