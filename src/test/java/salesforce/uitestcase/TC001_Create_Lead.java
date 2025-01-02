package salesforce.uitestcase;

import com.framework.selenium.salesforcepages.HomePage;
import com.framework.testng.api.base.BaseMethods;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class TC001_Create_Lead extends BaseMethods {

    @BeforeTest
    public void setFileName() {
        excelFileName = "CreateLead";
        testcaseName = "Create Leads";
        testDescription = "Create, Edit, and Delete Lead Test Cases";
        authors = "Hari R";
        category = "Regression";
    }

    @Test(dataProvider = "fetchData")
    public void createLeadTest(String fName, String lName, String company) throws InterruptedException {
        new HomePage()
                .openAppLauncher()
                .clickViewAll()
                .searchApp("Leads")
                .selectApp("Leads")
                .clickNew()
                .enterLeadDetails(fName, lName, company)
                .saveLead(lName);
    }
}
