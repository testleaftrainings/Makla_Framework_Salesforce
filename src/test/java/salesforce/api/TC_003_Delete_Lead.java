package salesforce.api;

import com.framework.restassured.api.base.RESTAssuredBase;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import io.restassured.response.Response;

import java.util.HashMap;
import java.util.Map;

public class TC_003_Delete_Lead extends RESTAssuredBase {
    @BeforeTest
    public void setValues() {

        testcaseName = "Delete a Lead with Rest Assured";
        testDescription = "delete a Lead and Verify response Code";
        authors = "Hari R";
        category = "REST";

    }

    //String id = "00QNS00000NbEV02AN";

    @Test
    public void deleteLead() {

        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + getAuthToken());

        Response response = deleteWithHeader(headers, id);
        System.out.println(response.getStatusCode());
        verifyResponseCode(response, 204);

    }
}
