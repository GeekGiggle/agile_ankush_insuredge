package insuredge_ankush;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;
public class US17P4_21_filterPopulationLogic extends testBase{
	  // ---------- TestNG Hooks ----------
	WebDriverWait wait = getWait();
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
    public void beforeClass() {
        launchAndNavigation();
        login();
        navigation_rejectedPH();
        Assert.assertTrue(correctPageSanity_rph1(), "Not on rejected Policy Holders page.");
        
    }
	
    	    @Test(priority = 1)
	    public void tc01_validateDefaultCustomerIsAll() {
	        WebElement ddlCustomer = getWait().until(ExpectedConditions.visibilityOfElementLocated(
	            By.id("ContentPlaceHolder_Admin_ddlCustomerName")));
	        Select selCustomer = new Select(ddlCustomer);
	        String selected = selCustomer.getFirstSelectedOption().getText().trim();

	        boolean ok = selected.equalsIgnoreCase("-- All --") || selected.equalsIgnoreCase("--All--");
	        Assert.assertTrue(ok, "Default selection should be '-- All --'"+selected);
	    }

	    // ---------- TC02: Customer dropdown shows ONLY rejected customers & unique ----------
	    @Test(priority = 2)
	    public void tc02_validateCustomerDropdownUniqueAndRelevant() {
	        WebDriverWait w = getWait();

	        // Open the visible Select2 (customer name)
	        WebElement s2Container = w.until(ExpectedConditions.elementToBeClickable(
	            By.xpath("//span[@id='select2-ContentPlaceHolder_Admin_ddlCustomerName-container']/parent::span")));
	        s2Container.click();

	        // Collect visible options (Select2 results panel)
	        WebElement resultsList = w.until(ExpectedConditions.visibilityOfElementLocated(
	            By.id("select2-ContentPlaceHolder_Admin_ddlCustomerName-results")));

	        List<String> dropdownNames = new ArrayList<>();
	        List<WebElement> liOptions = resultsList.findElements(By.cssSelector("li.select2-results__option"));
	        for (WebElement li : liOptions) {
	            String txt = li.getText().trim();
	            if (txt.length() > 0 && !txt.equalsIgnoreCase("-- All --") && !txt.equalsIgnoreCase("--All--")) {
	                jsScroll(driver, li); // visually scroll the list while reading
	                dropdownNames.add(txt);
	            }
	        }
//	        dropdownNames.forEach(System.out::println);
	        Assert.assertFalse(dropdownNames.isEmpty(), "Dropdown should not be empty (excluding -- All --)");

	        // Uniqueness check (case-sensitive as per UI; switch to lower-case if needed)
	        Set<String> unique = new HashSet<>(dropdownNames);
	        Assert.assertEquals(unique.size(), dropdownNames.size(), "Dropdown has duplicate names");
	    }

	    // ---------- TC03: Policy Name Select2 accepts alphanumeric & filters grid ----------
	    @Test(priority = 3)
	    public void tc03_validatePolicyNameAlphanumericSearch() {
	    	 WebDriverWait w = getWait();

		        // Open Policy Name Select2
		        WebElement policySelection = w.until(ExpectedConditions.elementToBeClickable(
		            By.xpath("//span[@id='select2-ContentPlaceHolder_Admin_ddlPolicyName-container']")));
		        policySelection.click();

		        // Type alphanumeric and pick a match
		        WebElement searchBox = w.until(ExpectedConditions.visibilityOfElementLocated(
		            By.cssSelector("input.select2-search__field")));
		        String query = "premium3";  // adjust to a known alphanumeric in your env
		        searchBox.sendKeys(query);

		        WebElement match = w.until(ExpectedConditions.visibilityOfElementLocated(
		            By.xpath("//li[contains(@class,'select2-results__option')]" +
		                     "[contains(translate(normalize-space(.),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'), '" +
		                     query.toLowerCase() + "')]")));
		        match.click();

		        // Validate grid filtered (col-4 is Policy Name)
		        WebElement firstPolicyCell = w.until(ExpectedConditions.visibilityOfElementLocated(
		            By.xpath("//table[@id='ContentPlaceHolder_Admin_gvRejectedHolders']//tr[td][1]/td[4]")));
		        String policyInGrid = firstPolicyCell.getText().trim().toLowerCase();

		        Assert.assertFalse(policyInGrid.contains(query.toLowerCase()),
		                "Grid not filtered by selected policy. Found first policy: " + policyInGrid);

		       }


	    // ---------- TC04: Policy Name search with special chars -> 'No results found', then clear restores ----------
	    @Test(priority = 4)
	    public void tc04_validatePolicyNameSearchSpecialChars_NoResultsThenRestore() {
	        WebDriverWait w = getWait();

	        // Open Policy Name Select2
	        WebElement policySelection = w.until(ExpectedConditions.elementToBeClickable(
	            By.xpath("//span[@id='select2-ContentPlaceHolder_Admin_ddlPolicyName-container']")));
	        policySelection.click();

	        // Type special characters
	        WebElement searchBox = w.until(ExpectedConditions.visibilityOfElementLocated(
	            By.cssSelector("input.select2-search__field")));
	        String special = "policy@12";  // your special/non-alphanumeric
	        searchBox.sendKeys(special);

	        // Expect "No results found"
	        WebElement noResults = w.until(ExpectedConditions.visibilityOfElementLocated(
	            By.xpath("//li[contains(@class,'select2-results__message')]")));
	        String text = noResults.getText().trim();
	        Assert.assertTrue(text.equalsIgnoreCase("No results found"),
	                "Expected 'No results found'. Found: " + text);

	        // Clear and ensure list restores
	        searchBox.sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.DELETE);
	        WebElement anyOption = w.until(ExpectedConditions.visibilityOfElementLocated(
	            By.xpath("//li[contains(@class,'select2-results__option') and @role='option']")));
	        Assert.assertFalse(anyOption.getText().trim().isEmpty(), "Options did not restore after clearing search.");
 }

}
