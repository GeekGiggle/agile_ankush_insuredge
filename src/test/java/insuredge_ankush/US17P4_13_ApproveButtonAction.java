package insuredge_ankush;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

public class US17P4_13_ApproveButtonAction extends testBase {

	 // ---------- TestNG Hooks ----------
    @BeforeSuite
    public void beforeSuite() {
        driver = new ChromeDriver();
        wait = getWait();
    }

    @AfterSuite(alwaysRun = true)
    public void afterSuite() {
        if (driver != null) driver.quit();
    }

    @BeforeClass
    public void start() {
        launchAndNavigation();
        login();
        navigation_pendingPH();
        Assert.assertTrue(correctPageSanity_pph1(), "Not on Pending Policy Holders page.");
    }
    
	//validate approve click
@Test(priority = 1)
    public void TC01_validateApproveClick() {

        WebElement table = wait.until(ExpectedConditions
                .visibilityOfElementLocated(By.id("ContentPlaceHolder_Admin_gvPendingHolders")));

        List<WebElement> rows = table.findElements(By.xpath(".//tbody/tr[td]"));
        Assert.assertTrue(!rows.isEmpty(), "FAIL: No pending rows found.");

        WebElement firstRow = rows.get(0);

        WebElement approveBtn = firstRow.findElement(By.xpath(".//input[@value='Approve']"));
        jsClick(driver, approveBtn);

        WebElement msg = wait.until(ExpectedConditions
                .visibilityOfElementLocated(By.id("ContentPlaceHolder_Admin_lblMessage")));

        Assert.assertTrue(msg.isDisplayed(), "FAIL: Success message not displayed.");
    }

    
    // Validate record moves to Approved list
    @Test(priority = 2)
    public void TC02_validateRecordMovesToApproved() throws Exception {

        navigation_pendingPH();
        Assert.assertTrue(correctPageSanity_pph1(), "Not on pending page");

        WebElement pendingTable = wait.until(ExpectedConditions
                .visibilityOfElementLocated(By.id("ContentPlaceHolder_Admin_gvPendingHolders")));

        List<WebElement> rows = pendingTable.findElements(By.xpath(".//tbody/tr[td]"));
        Assert.assertTrue(!rows.isEmpty(), "FAIL: No pending rows found.");

        WebElement firstRow = rows.get(0);

        // Capture values before approval

        String customerName = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(".//td[1]"))).getText().trim();
        String policyName = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(".//td[4]"))).getText().trim();
        String subCategory = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(".//td[6]"))).getText().trim();

        WebElement approveBtn = wait.until(
                ExpectedConditions.elementToBeClickable(
                        firstRow.findElement(By.xpath(".//input[@value='Approve']"))
                )
        );
        jsClick(driver, approveBtn);

        Thread.sleep(1500);

        navigation_approvedPH();
//        Assert.assertTrue(correctPageSanity_aph1(), "Not on Approved Policy Holders page");
        
        applyFilter(customerName, policyName,subCategory);

        WebElement searchBtn = wait.until(ExpectedConditions
                .elementToBeClickable(By.id("ContentPlaceHolder_Admin_btnSearch")));
        jsClick(driver, searchBtn);

        List<WebElement> approvedRows = wait.until(
                ExpectedConditions.visibilityOfAllElementsLocatedBy(
                        By.xpath("//table[@id='ContentPlaceHolder_Admin_gvApprovedHolders']//tbody/tr[td]")
                )
        );
        Assert.assertTrue(!approvedRows.isEmpty(), "FAIL: Approved record not found.");

        WebElement matchedRow = approvedRows.get(0);

        Assert.assertEquals(matchedRow.findElement(By.xpath(".//td[1]")).getText().trim(), customerName);
        Assert.assertEquals(matchedRow.findElement(By.xpath(".//td[4]")).getText().trim(), policyName);
        Assert.assertEquals(matchedRow.findElement(By.xpath(".//td[6]")).getText().trim(), subCategory);
    }

    // Utility method – Select2 filter
    private void applyFilter(String customerName, String policyName, String subCategory) {
    	 WebElement customerSel = wait.until(ExpectedConditions.elementToBeClickable(
                 By.xpath("//span[@id='select2-ContentPlaceHolder_Admin_ddlCustomerName-container']/parent::span")
             ));
             
             //customer name, policy name and sub category
             customerSel.click(); 
             WebElement s2Input = wait.until(ExpectedConditions.visibilityOfElementLocated(
                 By.xpath("//input[@class='select2-search__field' and @aria-controls='select2-ContentPlaceHolder_Admin_ddlCustomerName-results']")
             ));
             s2Input.sendKeys(customerName);
             s2Input.sendKeys(Keys.ENTER);
             
             WebElement policySel = wait.until(ExpectedConditions.elementToBeClickable(
                 By.xpath("//span[@id='select2-ContentPlaceHolder_Admin_ddlPolicyName-container']/parent::span")
             ));
             policySel.click();
             s2Input = wait.until(ExpectedConditions.visibilityOfElementLocated(
                 By.xpath("//input[@class='select2-search__field' and @aria-controls='select2-ContentPlaceHolder_Admin_ddlPolicyName-results']")
             ));
             s2Input.sendKeys(policyName);
             s2Input.sendKeys(Keys.ENTER);
             
             WebElement subCatSel = wait.until(ExpectedConditions.elementToBeClickable(
                     By.xpath("//span[@id='select2-ContentPlaceHolder_Admin_ddlSubCategory-container']/parent::span")
                 ));
                 subCatSel.click();
                 s2Input = wait.until(ExpectedConditions.visibilityOfElementLocated(
                     By.xpath("//input[@class='select2-search__field' and @aria-controls='select2-ContentPlaceHolder_Admin_ddlSubCategory-results']")
                 ));
                 s2Input.sendKeys(subCategory);
                 s2Input.sendKeys(Keys.ENTER);
    }


    // Validate approval success message formatting
    @Test(priority = 3)
    public void TC03_validateApprovalSuccessMessage() {

        navigation_pendingPH();
        Assert.assertTrue(correctPageSanity_pph1(), "Not on pending page");

        List<WebElement> rows = wait.until(
                ExpectedConditions.visibilityOfAllElementsLocatedBy(
                        By.xpath("//table[@id='ContentPlaceHolder_Admin_gvPendingHolders']//tbody/tr[td]")
                )
        );
        Assert.assertTrue(!rows.isEmpty(), "FAIL: No rows found in pending.");

        WebElement firstRow = rows.get(0);
        String cust = firstRow.findElement(By.xpath(".//td[1]")).getText().trim();


        WebElement approveBtn = wait.until(ExpectedConditions.elementToBeClickable(firstRow.findElement(By.xpath("./td//input[@value='Approve']"))));

        jsClick(driver, approveBtn);

        WebElement msg = wait.until(ExpectedConditions
                .visibilityOfElementLocated(By.id("ContentPlaceHolder_Admin_lblMessage")));

        String txt = msg.getText().trim().toLowerCase();
        String color = msg.getCssValue("color").toLowerCase();

        Assert.assertTrue(
                txt.contains("policy for") &&
                txt.contains(cust.toLowerCase()) &&
                txt.contains("has been approved"),
                "FAIL: Success message text incorrect."
        );

        Assert.assertTrue(
                color.contains("25, 135, 84") ||
                color.contains("0, 128, 0") ||
                color.contains("#198754") ||
                color.contains("green"),
                "FAIL: Message color incorrect."
        );

        WebElement filterRow = wait.until(ExpectedConditions
                .visibilityOfElementLocated(By.xpath("//*[@id='frmPendingPolicyHolder']/div[4]")));

        boolean isBelow = filterRow.findElements(
                By.xpath("//*[@id='frmPendingPolicyHolder']/div[4]/following-sibling::div/span"))
                .size() > 0;

        Assert.assertTrue(isBelow, "FAIL: Message not positioned correctly below filter.");
    }
}
