package com.framework.testng.listeners;

import java.util.List;

import org.testng.ITestListener;
import org.testng.ITestResult;

import com.framework.restassured.api.base.RESTAssuredBase;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class TestListener implements ITestListener{
	
	@Override
	public void onTestFailure(ITestResult result) {
		//test method name
		String testName = result.getName();
		int count =0;
				
		RestAssured.baseURI = "https://api-training.atlassian.net/rest/api/2/";
		RestAssured.authentication = RestAssured.preemptive().basic("hari.radhakrishnan@testleaf.com", "ATATT3xFfGF0xHQIsINmRYbMy3dPjeIzfgyqb5SvQrgxAW1KzwPIe85mBCwvzBKeleqVkE6JUcKi-PmmUBIy32aGUz3Q5e9JoKcjYtDYtTw4G4eekrEJLY-qMi_3f3AxHsmNgjpQ0W9Ag9LAhovIcJ-XEvDYUxNv1oTl5qD7U-rZvJdVsu6DQf0=6F595243");
		
		//hit get all issues
		Response response = RESTAssuredBase.getWithParameter("jql", "project=TM", "search");
		
		JsonPath jsonPath = response.jsonPath();
		
		List<String> allSummary = jsonPath.getList("issues.fields.summary");
		
		for (String eachSummary : allSummary) {
			if(eachSummary.equals(testName)) {
				count++;
			}
		}
		
		if(count==0) {
			RESTAssuredBase.postWithJsonAsBody("{\"fields\":{\"project\":{\"key\":\"TM\"},\"summary\":\""+testName+"\",\"description\":\""+testName+"\",\"issuetype\":{\"name\":\"Bug\"}}}", "issue");
		}else {
			System.out.println("Bug is already available");
		}
		
	
	}

}
