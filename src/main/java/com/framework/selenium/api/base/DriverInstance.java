package com.framework.selenium.api.base;

import java.net.MalformedURLException;
import java.time.Duration;

import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.github.sukgu.Shadow;

public class DriverInstance  extends AbstractTestNGCucumberTests{

	private static final ThreadLocal<RemoteWebDriver> remoteWebdriver = new ThreadLocal<RemoteWebDriver>();

	private static final ThreadLocal<WebDriverWait> wait = new ThreadLocal<WebDriverWait>();

	private static final ThreadLocal<Shadow> dom = new ThreadLocal<Shadow>() ;

	public void setDom() {
		dom.set(new Shadow(getDriver()));
	}

	public void setWait() {
		wait.set(new WebDriverWait(getDriver(), Duration.ofSeconds(30)));
	}

	public WebDriverWait getWait() {
		return wait.get();
	}

	public void setDriver(String browser, boolean headless) {
		switch (browser) {
		case "chrome":
			ChromeOptions options = new ChromeOptions();
			options.addArguments("--start-maximized");
			options.addArguments("--disable-notifications");
			options.addArguments("--incognito");
			remoteWebdriver.set(new ChromeDriver(options));
			break;
		case "firefox":
			remoteWebdriver.set(new FirefoxDriver());
			break;
		case "ie":
			remoteWebdriver.set(new InternetExplorerDriver());
		case "edge":
			remoteWebdriver.set(new EdgeDriver());
		default:
			break;
		}
	}

	public static  RemoteWebDriver getDriver() {
		return remoteWebdriver.get();
	}


	public Shadow getShadowElement() {
		return dom.get();


	}

	
	
	
}
