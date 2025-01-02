package com.framework.selenium.salesforcepages;

import com.framework.selenium.api.design.Locators;
import com.framework.testng.api.base.BaseMethods;

public class HomePage extends BaseMethods {

	public HomePage openAppLauncher() throws InterruptedException {
		Thread.sleep(3000);
        click(locateElement(Locators.XPATH, "//div[@class='slds-icon-waffle']"));
        reportStep("App Launcher icon clicked Succesfully","Pass",true);
        return this;
    }

    public HomePage clickViewAll() {
        click(locateElement(Locators.XPATH, "//button[text()='View All']"));
        reportStep("View All icon clicked Succesfully","Pass",true);
        return this;
    }

    public HomePage searchApp(String appName) {
        clearAndType(locateElement(Locators.XPATH, "//input[contains(@id, 'input-') and @placeholder='Search apps or items...']"), appName);
        reportStep("Search app clicked Succesfully","Pass",true);
        return this;
    }

    public LeadPage selectApp(String appName) {
        click(locateElement(Locators.XPATH, "//mark[text()='" + appName + "']"));
        reportStep("App selected Succesfully","Pass",true);
        return new LeadPage();
    }
}
