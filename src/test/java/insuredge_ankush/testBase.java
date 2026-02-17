package insuredge_ankush;

import java.time.Duration;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

public class testBase {

    protected WebDriver driver;
    private WebDriverWait wait;

    // ---------- WAIT ----------
    protected WebDriverWait getWait() {
        if (wait == null) {
            wait = new WebDriverWait(driver, Duration.ofSeconds(90));
        }
        return wait;
    }

    // ---------- DRIVER SETUP (CI friendly) ----------
    protected void initDriver() {
        ChromeOptions options = new ChromeOptions();

        // Jenkins runs as SYSTEM -> headless is more stable
        // If you want UI mode locally, comment next line.
//        options.addArguments("--headless=new");

        options.addArguments("--window-size=1920,1080");
        options.addArguments("--disable-gpu");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--remote-allow-origins=*");

        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(90));
    }

    protected void quitDriver() {
        try {
            if (driver != null) driver.quit();
        } catch (Exception ignored) { }
    }

    // ---------- UTIL: PAGE READY ----------
    protected void waitForPageReady() {
        try {
            getWait().until(d ->
                    ((JavascriptExecutor) d).executeScript("return document.readyState").equals("complete"));
        } catch (Exception ignored) { }
    }

    // ---------- UTIL: SCROLL / SAFE CLICK ----------
    protected void jsScroll(WebElement element) {
        if (element == null) return;
        try {
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("arguments[0].scrollIntoView({block:'center', inline:'nearest'});", element);
            js.executeScript("window.scrollBy(0, -80);"); // sticky header offset
        } catch (Exception e) {
            try { new Actions(driver).scrollToElement(element).perform(); } catch (Exception ignored) { }
        }
    }

    protected void safeClick(By locator) {
        WebElement el = getWait().until(ExpectedConditions.presenceOfElementLocated(locator));
        getWait().until(ExpectedConditions.visibilityOf(el));
        jsScroll(el);

        // Try normal click first
        try {
            getWait().until(ExpectedConditions.elementToBeClickable(el)).click();
            return;
        } catch (Exception ignored) { }

        // Retry with JS click (CI overlays/responsive issues)
        try {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", el);
            return;
        } catch (Exception ignored) { }

        // Last fallback: Actions click
        new Actions(driver).moveToElement(el).click().perform();
    }

    // ---------- PAGE TITLE SANITY ----------
    protected boolean correctPageTitleIs(String expectedTitle) {
        try {
            WebElement h1 = getWait().until(
                    ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(@class,'pagetitle')]//h1"))
            );
            String title = h1.getText().trim();
            if (!title.equalsIgnoreCase(expectedTitle)) {
                System.out.println("Fail: Page title mismatch. Found: " + title);
                return false;
            }
            return true;
        } catch (Exception e) {
            System.out.println("Navigation is Incorrect: " + e.getMessage());
            return false;
        }
    }

    protected boolean correctPageSanity_pph1() { return correctPageTitleIs("Pending Policy Holders"); }
    protected boolean correctPageSanity_rph1() { return correctPageTitleIs("Rejected Policy Holders"); }
    protected boolean correctPageSanity_aph1() { return correctPageTitleIs("Approved Policy Holders"); } // fixed

    // ---------- APP FLOW ----------
    protected void launchAndNavigation() {
        driver.get("https://qeaskillhub.cognizant.com/LoginPage");
        waitForPageReady();
    }

    protected void login() {
        try {
            WebElement username = getWait().until(ExpectedConditions.visibilityOfElementLocated(By.id("txtUsername")));
            username.clear();
            username.sendKeys("admin_user");

            WebElement password = getWait().until(ExpectedConditions.visibilityOfElementLocated(By.id("txtPassword")));
            password.clear();
            password.sendKeys("testadmin");

            safeClick(By.id("BtnLogin"));

            // Wait until login page is gone OR dashboard is present
            getWait().until(ExpectedConditions.or(
                    ExpectedConditions.urlContains("Dashboard"),
                    ExpectedConditions.invisibilityOfElementLocated(By.id("BtnLogin"))
            ));
            waitForPageReady();
        } catch (Exception e) {
            Assert.fail("Login failed: " + e.getMessage(), e);
        }
    }

    // ---------- NAVIGATION (ROBUST) ----------
    // Important: CLICK THE <a> NOT THE <span> and don't use li[5]
    private final By POLICY_HOLDER_MENU = By.cssSelector(
            "body > aside:nth-child(2) > ul:nth-child(1) > li:nth-child(5) > a:nth-child(1) > span:nth-child(2)"
    );

    private final By PENDING_PH = By.xpath("//ul[@id='policyHolder-nav']//a[contains(normalize-space(.),'Pending')]");
    private final By APPROVED_PH = By.xpath("//ul[@id='policyHolder-nav']//a[contains(normalize-space(.),'Approved')]");
    private final By REJECTED_PH = By.xpath("//ul[@id='policyHolder-nav']//a[contains(normalize-space(.),'Rejected')]");

    private void openPolicyHolderMenu() {
        // Click menu (if it is collapsed / not opened)
        safeClick(POLICY_HOLDER_MENU);

        // Wait for submenu to appear
        getWait().until(ExpectedConditions.visibilityOfElementLocated(By.id("policyHolder-nav")));
    }

    protected void navigation_pendingPH() {
        try {
            openPolicyHolderMenu();
            safeClick(PENDING_PH);
            waitForPageReady();
        } catch (Exception e) {
            Assert.fail("Navigation to Pending Policy Holders failed: " + e.getMessage(), e);
        }
    }

    protected void navigation_approvedPH() {
        try {
            openPolicyHolderMenu();
            safeClick(APPROVED_PH);
            waitForPageReady();
        } catch (Exception e) {
            Assert.fail("Navigation to Approved Policy Holders failed: " + e.getMessage(), e);
        }
    }

    protected void navigation_rejectedPH() {
        try {
            openPolicyHolderMenu();
            safeClick(REJECTED_PH);
            waitForPageReady();
        } catch (Exception e) {
            Assert.fail("Navigation to Rejected Policy Holders failed: " + e.getMessage(), e);
        }
    }
}
