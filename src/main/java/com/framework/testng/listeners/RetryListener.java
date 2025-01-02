package com.framework.testng.listeners;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import org.testng.IAnnotationTransformer;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;
import org.testng.annotations.ITestAnnotation;

public class RetryListener implements IRetryAnalyzer,IAnnotationTransformer{


	
	int retryCount = 1;
	public boolean retry(ITestResult result) {
		if(retryCount < 3) {
			retryCount++;
			return true;
		}
		
		return false;
	}

	
	
	public void transform(ITestAnnotation annotation, Class testClass, Constructor testConstructor, Method testMethod) {
		
		annotation.setRetryAnalyzer(RetryListener.class);	
	
	}
}
























