package insuredge_ankush;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeSuite;

public class testBase {

protected WebDriver driver;
    protected WebDriverWait wait;
    
    protected WebDriverWait getWait() {
        return new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    protected void jsScroll(WebDriver driver, WebElement element) {
        if (element == null) return;
        JavascriptExecutor js = (JavascriptExecutor) driver;
        try {
            js.executeScript("arguments[0].scrollIntoView({block:'center', inline:'nearest'});", element);
            js.executeScript("window.scrollBy(0, -80);"); // offset for sticky header
        } catch (Exception ignore) {
            try { new Actions(driver).scrollToElement(element).perform(); } catch (Exception ignored) {}
        }
    }

    protected void jsClick(WebDriver driver, WebElement element) {
        if (element == null) return;
        JavascriptExecutor js = (JavascriptExecutor) driver;
        jsScroll(driver, element);
        try {
            js.executeScript("arguments[0].click();", element);
            return;
        } catch (Exception ignore) { }
        try {
            element.click();
        } catch (Exception e) {
            new Actions(driver).moveToElement(element).click().perform();
        }
    }

    protected boolean correctPageSanity_rph1() {
        try {
            String title = getWait().until(
                ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='pagetitle']/h1"))
            ).getText().trim();
            if (!title.equalsIgnoreCase("Rejected Policy Holders")) {
                System.out.println("Fail: Page title mismatch. Found: " + title);
                return false;
            }
            return true;
        } catch (Exception e) {
            System.out.println("Navigation is Incorrect: " + e.getMessage());
            return false;
        }
    }
    protected boolean correctPageSanity_pph1() {
        try {
            String title = getWait().until(
                ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='pagetitle']/h1"))
            ).getText().trim();
            if (!title.equalsIgnoreCase("Pending Policy Holders")) {
                System.out.println("Fail: Page title mismatch. Found: " + title);
                return false;
            }
            return true;
        } catch (Exception e) {
            System.out.println("Navigation is Incorrect: " + e.getMessage());
            return false;
        }
    }
    protected boolean correctPageSanity_aph1() {
        try {
            String title = getWait().until(
                ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='pagetitle']/h1"))
            ).getText().trim();
            if (!title.equalsIgnoreCase("approve Policy Holders")) {
                System.out.println("Fail: Page title mismatch. Found: " + title);
                return false;
            }
            return true;
        } catch (Exception e) {
            System.out.println("Navigation is Incorrect: " + e.getMessage());
            return false;
        }
    }

    protected void launchAndNavigation() {
        driver.get("https://qeaskillhub.cognizant.com/LoginPage");
        driver.manage().window().maximize();
    }

    protected void login() {
        try {
            WebElement username = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("txtUsername")));
            username.sendKeys("admin_user");
            WebElement password = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("txtPassword")));
            password.sendKeys("testadmin");
            WebElement loginBtn = wait.until(ExpectedConditions.elementToBeClickable(By.id("BtnLogin")));
            loginBtn.click();
        } catch (Exception e) {
            System.out.println("login failed: " + e.getMessage());
        }
    }

    protected void navigation_rejectedPH() {
        try {
            WebElement sidebar = wait.until(
                ExpectedConditions.elementToBeClickable(By.xpath("//*[@id='sidebar-nav']/li[5]/a")));
            sidebar.click();
            WebElement rejectedPH = wait.until(
                ExpectedConditions.elementToBeClickable(By.xpath("//*[@id='policyHolder-nav']/li[4]")));
            rejectedPH.click();
        } catch (Exception e) {
            System.out.println("navigation failed: " + e.getMessage());
        }
    }
    protected void navigation_approvedPH() {
        try {
            WebElement sidebar = wait.until(
                ExpectedConditions.elementToBeClickable(By.xpath("//*[@id='sidebar-nav']/li[5]/a")));
            sidebar.click();
            WebElement approvedPH = wait.until(
                ExpectedConditions.elementToBeClickable(By.xpath("//*[@id='policyHolder-nav']/li[2]")));
            approvedPH.click();
        } catch (Exception e) {
            System.out.println("navigation failed: " + e.getMessage());
        }
    }

    protected void navigation_pendingPH() {
    	try {
    		WebElement sidebar = wait.until(
                    ExpectedConditions.elementToBeClickable(By.xpath("//*[@id='sidebar-nav']/li[5]/a")));
            sidebar.click();
            WebElement pendingPH = wait.until(
                    ExpectedConditions.elementToBeClickable(By.xpath("//*[@id='policyHolder-nav']/li[3]")));
            pendingPH.click();
    	}catch(Exception e){
    		System.out.println("navigation failed: " + e.getMessage());
    	}
    }
}
