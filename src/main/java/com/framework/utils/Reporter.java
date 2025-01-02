package com.framework.utils;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.remote.UnreachableBrowserException;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeSuite;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.MediaEntityModelProvider;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.configuration.ChartLocation;
import com.aventstack.extentreports.reporter.configuration.Theme;
import com.framework.selenium.api.base.DriverInstance;

public abstract class Reporter extends DriverInstance {

	private static ExtentReports extent;
	private static final ThreadLocal<ExtentTest> parentTest = new ThreadLocal<ExtentTest>();
	private static final ThreadLocal<ExtentTest> test = new ThreadLocal<ExtentTest>();
	private static final ThreadLocal<String> testName = new ThreadLocal<String>();
	public static ExtentTest child;
	public static String incidentNumber;
	private String fileName = "result.html";
	private String pattern = "dd-MMM-yyyy HH-mm-ss";
	public  String testcaseName, testDescription, authors, category, dataFileName, dataFileType, excelFileName;
	public static String folderName = "";
	public static final ThreadLocal<String> screenshotDir = new ThreadLocal<>();
    public static final ConcurrentHashMap<Long, String> threadToVideoMap = new ConcurrentHashMap<>();

	@BeforeSuite(alwaysRun = true)
	public synchronized void startReport() {
		String date = new SimpleDateFormat(pattern).format(new Date());
		folderName = "reports/" + date;

		File folder = new File("./" + folderName);
		if (!folder.exists()) {
			folder.mkdir();
		}
		ExtentHtmlReporter htmlReporter = new ExtentHtmlReporter("./" + folderName + "/" + fileName);
		htmlReporter.config().setTestViewChartLocation(ChartLocation.BOTTOM);
		htmlReporter.config().setChartVisibilityOnOpen(!true);
		htmlReporter.config().setTheme(Theme.STANDARD);
		htmlReporter.config().setDocumentTitle("ServiceNow");
		htmlReporter.config().setEncoding("utf-8");
		htmlReporter.config().setReportName("ServiceNow");
		htmlReporter.setAppendExisting(true);
		extent = new ExtentReports();
		extent.attachReporter(htmlReporter);
	}

	@BeforeClass(alwaysRun = true)
	public synchronized void startTestCase() {
		ExtentTest parent = extent.createTest(testcaseName, testDescription);
		parent.assignCategory(category);
		parent.assignAuthor(authors);
		parentTest.set(parent);
		testName.set(testcaseName);
	}

	public synchronized void setNode() {
		child = parentTest.get().createNode(getTestName());
		test.set(child);
	}

	public long takeSnap() {
		// TODO Auto-generated method stub
		return 0;
	}

	public  void reportStep(String desc, String status, boolean bSnap) {
		synchronized (test) {

			// Start reporting the step and snapshot
			MediaEntityModelProvider img = null;
			if (bSnap && !(status.equalsIgnoreCase("INFO") || status.equalsIgnoreCase("skipped")
					)) {
				long snapNumber = 100000L;
				snapNumber = takeSnap();
				try {
					img = MediaEntityBuilder
							.createScreenCaptureFromPath("./../../" + folderName + "/images/" + snapNumber + ".jpg")
							.build();
				} catch (IOException e) {
				}
			}
			if (status.equalsIgnoreCase("pass")) {
				test.get().pass(desc, img);
				captureScreenshot(desc);
			} else if (status.equalsIgnoreCase("fail")) { // additional steps to manage alert pop-up
				test.get().fail(desc, img);
				captureScreenshot(desc);
				throw new RuntimeException("See the reporter for details.");

			} else if (status.equalsIgnoreCase("warning")) {
				test.get().warning(desc, img);
				captureScreenshot(desc);
			} else if (status.equalsIgnoreCase("skipped")) {
				test.get().skip("The test is skipped due to dependency failure");
				captureScreenshot(desc);
			} else if (status.equalsIgnoreCase("INFO")) {
				test.get().info(desc);
				captureScreenshot(desc);
			}

			
		}
	}

	public  void reportStep(String desc, String status) {
		reportStep(desc, status, true);
	}
	
	
	//To log Rest Steps
		public static void reportStatus(String desc, String status) {
			
			MediaEntityModelProvider img = null;
			if(status.equalsIgnoreCase("PASS")) {
				test.get().pass(desc);		
			}else if(status.equalsIgnoreCase("FAIL")) {
				test.get().fail(desc);
				throw new RuntimeException();
			}else if(status.equalsIgnoreCase("WARNING")) {
				test.get().warning(desc);		
			}else {
				test.get().info(desc);
			}	
		}

	@AfterSuite(alwaysRun = true)
	public synchronized void endResult() {
		try {
			if (getDriver() != null) {
				getDriver().quit();
			}
		} catch (UnreachableBrowserException e) {
		}
		extent.flush();
	}

	
	public String getTestName() {
		return testName.get();
	}

	public Status getTestStatus() {
		return parentTest.get().getModel().getStatus();
	}
	 public void captureScreenshot(String stepName) {
	        String threadDir = screenshotDir.get();
	        try {
	            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss_SSS").format(new Date());
	            String screenshotPath = threadDir + File.separator + stepName + "_" + timestamp + ".png";
	            File screenshot = ((TakesScreenshot) getDriver()).getScreenshotAs(OutputType.FILE);
	            FileUtils.copyFile(screenshot, new File(screenshotPath));
	            System.out.println("Screenshot saved: " + screenshotPath);
	        } catch (IOException e) {
	           System.out.println("See the video for details.");
	        }
	    }
	 public void setUp() {
	        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
	        //String threadDir = "./videoreport/Test_" + Thread.currentThread().getId() + "_" + timestamp;
	        String threadDir = "./videoreport/"+testcaseName + "_" + timestamp;
	        screenshotDir.set(threadDir);

	        File dir = new File(threadDir);
	        if (!dir.exists() && dir.mkdirs()) {
	            System.out.println("Screenshot directory created: " + threadDir);
	        }

	        // Map thread to video output
	        threadToVideoMap.put(Thread.currentThread().getId(), threadDir + File.separator + "output_video.mp4");
	    }
	
}