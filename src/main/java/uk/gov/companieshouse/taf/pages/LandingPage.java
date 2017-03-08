package uk.gov.companieshouse.taf.pages;

import io.magentys.cinnamon.webdriver.elements.PageElement;
import org.openqa.selenium.support.FindBy;

public class LandingPage {

    @FindBy(id = "mainMenuId")
    public PageElement mainMenu;
}