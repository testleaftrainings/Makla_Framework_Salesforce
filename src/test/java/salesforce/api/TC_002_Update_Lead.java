package salesforce.api;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.framework.restassured.api.base.RESTAssuredBase;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import io.restassured.response.Response;


public class TC_002_Update_Lead extends RESTAssuredBase {
    @BeforeTest
    public void setValues() {
        testcaseName = "UpdateLead";
        testDescription = "Modifying a Lead and Verify response Code";
        authors = "Hari R";
        category = "REST";
        // dataProvider
        dataFileName = "TC002";
        dataFileType = "JSON";
    }

   // String id = "00QNS00000NbEoL2AV";

    @Test(dataProvider = "fetchData")
    public void editLead(File file) {

        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + getAuthToken());

        Response response = patchtWithHeaderAndFileBody(headers, file, id);
        verifyResponseCode(response, 204);
        response.prettyPrint();

    }
}
