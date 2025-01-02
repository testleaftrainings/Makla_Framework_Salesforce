package com.framework.restassured.api.base;

import java.io.File;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.http.Header;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;

import io.restassured.specification.RequestSpecification;

public class RESTAssuredBase extends PreAndTest {

  public String token;
    public static String id;
    static Properties prop = new Properties();

    static {
        try {
            prop.load(new FileInputStream("src/test/resources/config_api.properties"));
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Failed to load the properties file.");
        }
    }

    public static RequestSpecification setLogs() {
        return RestAssured.given().log().all().contentType("application/json");
    }

    protected String getAuthToken() {
        return oAuthToken; // Access shared token
    }

    public static Response get(String URL) {
        return setLogs().get(URL);
    }

    public static Response get() {
        return setLogs().get();
    }

    public static Response getWithHeader(Map<String, String> headers, String URL) {

        return setLogs().when().headers(headers).get(URL);
    }

    public static Response getWithParameter(String paramKey, String paramValue, String URL) {

        return setLogs().when().queryParam(paramKey, paramValue).get(URL);
    }

    public static Response getWithHeader(Header header, String URL) {

        return setLogs().when().header(header).get(URL);
    }

    public static Response post() {

        return setLogs().post();
    }

    public static Response post(String URL, ContentType type) {

        return setLogs().contentType(type).post(URL);
    }

    public static Response postWithBodyAsFile(File bodyFile) {

        return setLogs().body(bodyFile).post();
    }

    public static Response postWithBodyAsFileAndUrl(File bodyFile, String URL) {

        return setLogs().body(bodyFile).post(URL);
    }


    public static Response postWithHeaderAndFileBody(Map<String, String> headers, File file, String URL) {

        return setLogs().when().headers(headers).body(file).post(URL);
    }

    public static Response postWithHeaderAndFileBody(Map<String, String> headers, File file) {

        return setLogs().when().headers(headers).body(file).post();
    }

    public static Response deleteWithHeader(Map<String, String> headers, String resource) {

        return setLogs().when().headers(headers).delete(resource);
    }


    public static Response patchtWithHeaderAndFileBody(Map<String, String> headers, File bodyFile, String resource) {

        return setLogs().headers(headers).body(bodyFile).patch(resource);
    }


    public static Response postWithJsonAsBody(String jsonObject, String URL) {

        return setLogs().body(jsonObject).post(URL);
    }

    public static Response postWithHeaderAndJsonBody(Map<String, String> headers, String jsonObject, String URL) {

        return setLogs().when().headers(headers).body(jsonObject).post(URL);
    }

    public static Response postWithHeaderParam(Map<String, String> headers, String URL) {

        return setLogs().when().headers(headers).post(URL);
    }

    public static Response postWithHeaderAndPathParam(Map<String, String> headers, Map<String, String> pathParms,
                                                      String URL) {

        return setLogs().when().headers(headers).pathParams(pathParms).post(URL);
    }

    public static Response postWithHeaderAndPathParam(Map<String, String> pathParms, String URL) {

        return setLogs().when().pathParams(pathParms).post(URL);
    }

    public static Response putWithFileBodyAndURL(File file, String url) {
        return setLogs().when().body(file).put(url);

    }

    public static Response postWithAuthHeaders(String id, File file, String url) {
        return setLogs().header("authorization", id).when().body(file).put(url);

    }

    public static Response delete(String URL) {
        return setLogs().when().delete(URL);
    }

    public static Response deleteWithHeaderAndPathParamWithoutRequestBody(Map<String, String> headers, String URL)
            throws Exception {
        return setLogs().when().headers(headers).delete(URL);
    }

    public static Response putWithBodyParam(File file, String URL) {

        return RestAssured.given().contentType(getContentType()).request().body(file).when().put(URL);
    }

    public static ValidatableResponse enableResponseLog(Response response) {
        return response.then().log().all();
    }

    private static ContentType getContentType() {
        return ContentType.JSON;
    }

    public static void verifyContentType(Response response, String type) {
        if (response.getContentType().toLowerCase().contains(type.toLowerCase())) {
            reportStatus("The Content type " + type + " matches the expected content type", "PASS");
        } else {
            reportStatus("The Content type " + type + " does not match the expected content type "
                    + response.getContentType(), "FAIL");
        }
    }

    public static void verifyResponseCode(Response response, int code) {
        if (response.statusCode() == code) {
            reportStatus("The status code " + code + " matches the expected code", "pass");
        } else {
            reportStatus("The status code " + code + " does not match the expected code" + response.statusCode(),
                    "FAIL");
        }
    }

    public static void verifyResponseTime(Response response, long time) {
        if (response.time() <= time) {
            reportStatus("The time taken " + response.time() + " with in the expected time", "PASS");
        } else {
            reportStatus("The time taken " + response.time() + " is greater than expected SLA time " + time, "FAIL");
        }
    }

    public static String extractResponseContent(Response response, String key) {

        JsonPath jsonPath = response.jsonPath();
        String actValue = jsonPath.get(key);
        return actValue;

    }

    public static void verifyContentWithKey(Response response, String key, String expVal) {
        if (response.getContentType().contains("json")) {
            JsonPath jsonPath = response.jsonPath();
            String actValue = jsonPath.get(key);
            if (actValue.equalsIgnoreCase(expVal)) {
                reportStatus("The JSON response has value " + expVal + " as expected. ", "PASS");
            } else {
                reportStatus("The JSON response :" + actValue + " does not have the value " + expVal + " as expected. ",
                        "FAIL");
            }
        }
    }

    public static void verifyContentsWithKey(Response response, String key, String expVal) {
        if (response.getContentType().contains("json")) {
            JsonPath jsonPath = response.jsonPath();
            List<String> actValue = jsonPath.getList(key);
            if (actValue.get(0).equalsIgnoreCase(expVal)) {
                reportStatus("The JSON response has value " + expVal + " as expected. ", "PASS");
            } else {
                reportStatus("The JSON response :" + actValue + " does not have the value " + expVal + " as expected. ",
                        "FAIL");
            }
        }
    }

}
