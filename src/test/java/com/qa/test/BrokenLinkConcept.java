package com.qa.test;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class BrokenLinkConcept {

	WebDriver d;
	ChromeOptions opt;
	String appURL = "https://www.zlti.com/";
	String url = "";
	HttpURLConnection huc = null;
	int resCode = 200;

	@BeforeTest
	public void launch_Browser() {
		System.setProperty("webdriver.chrome.driver",
				"C:\\Users\\Virendra\\Desktop\\Learn Testing\\browser driver\\chromedriver.exe");
		opt = new ChromeOptions();
		opt.setAcceptInsecureCerts(true);
		opt.addArguments("--remote-allow-origins=*");
		d = new ChromeDriver(opt);
		d.manage().window().maximize();

		d.get(appURL);
	}

	@Test
	public void findBrokenLink() {
		List<WebElement> link = d.findElements(By.tagName("a"));
		Iterator<WebElement> itr = link.iterator();
		while (itr.hasNext()) {
			url = itr.next().getAttribute("href");
			System.out.println(url);

			if (url == null || url.isEmpty()) {
				System.out.println("URL is either not configured for anchor tag or it is empty");
				continue;
			}

			if (!url.startsWith(appURL)) {
				System.out.println("URL belongs to another domain, skipping it.");
				continue;
			}

			try {
				huc = (HttpURLConnection) (new URL(url).openConnection());

				huc.setRequestMethod("HEAD");

				huc.connect();

				resCode = huc.getResponseCode();

				if (resCode >= 400) {
					System.out.println(url + " is a broken link");
				} else {
					System.out.println(url + " is a valid link");
				}
			} catch (MalformedURLException e) {

				e.printStackTrace();
			} catch (IOException e) {

				e.printStackTrace();
			}
		}
	}

}
