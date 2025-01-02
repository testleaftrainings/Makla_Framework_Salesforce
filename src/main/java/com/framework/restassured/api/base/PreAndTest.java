package com.framework.restassured.api.base;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.DataProvider;

import com.framework.utils.DataLibrary;
import com.framework.utils.Reporter;

import io.restassured.RestAssured;
import io.restassured.response.Response;

public class PreAndTest extends Reporter {
    public static String oAuthToken;
    static Properties prop = new Properties();

    public PreAndTest() {
        oAuthToken = generateOauth();
    }

    static {
        try {
            prop.load(new FileInputStream("./src/test/resources/config_api.properties"));
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Failed to load the properties file.");
        }
    }


    @BeforeMethod
    public void beforeMethod() {
        setNode();
        RestAssured.baseURI = "https://" + prop.getProperty("instance") + "." + prop.getProperty("server") + "/"
                + prop.getProperty("resources");

    }


    @DataProvider(name = "fetchData")
    public Object[][] getData() {
        if (dataFileType.equalsIgnoreCase("Excel"))
            return DataLibrary.getSheet(dataFileName);
        else if (dataFileType.equalsIgnoreCase("JSON")) {
            Object[][] data = new Object[1][1];
            data[0][0] = new File("./data/" + dataFileName + "." + dataFileType);
            System.out.println(data[0][0]);
            return data;
        } else {
            return null;
        }

    }

    public String generateOauth() {
        String server = prop.getProperty("server");
        String resources = prop.getProperty("oAuthResources");
        String endpoint = "https://login." + server + resources;
        Map<String, String> formParams = new HashMap<>();
        formParams.put("grant_type", "password");
        formParams.put("client_id", prop.getProperty("client_id"));
        formParams.put("client_secret", prop.getProperty("client_secret"));
        formParams.put("username", prop.getProperty("username"));
        formParams.put("password", prop.getProperty("password") + prop.getProperty("secritykey"));
        Response response = RestAssured.given().contentType("application/x-www-form-urlencoded").formParams(formParams)
                .when().post(endpoint).then().extract().response();
        // response.prettyPrint();

        try {
            return response.jsonPath().getString("access_token");
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Failed to parse access token from response.");
            return null; // Or handle it according to your logic
        }
    }

}
