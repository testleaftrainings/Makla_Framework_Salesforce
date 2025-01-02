package salesforce.api;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import com.framework.restassured.api.base.RESTAssuredBase;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import io.restassured.response.Response;


public class TC_001_Create_Lead extends RESTAssuredBase {
    @BeforeTest
    public void setfileName() {
        testcaseName = "Create Lead";
        testDescription = "Create Lead using REST API";
        authors = "Hari R";
        category = "REST";
        //dataProvider
        dataFileName = "TC001";
        dataFileType = "JSON";

    }

    @Test(dataProvider = "fetchData")
    public void createLead(File file) {
        Map<String,String> headers = new HashMap<>();
        headers.put("Authorization","Bearer "+getAuthToken());

        Response response = postWithHeaderAndFileBody( headers,file);
        response.prettyPrint();
        verifyResponseCode(response, 201);
        id = response.jsonPath().getString("id");
        System.out.println("Extracted ID: " + id);

    }

}
