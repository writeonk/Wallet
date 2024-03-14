package pages;

import com.aventstack.extentreports.Status;
import common.TestBase;
import library.WebDriver;
import org.testng.annotations.Test;

public class TcDashboard extends TestBase {

    @Test
    public void tc01VerifyDashboard() {

        test = extent.createTest("Verify Dashboard");
        webdriver.waitForElementVisible(Dashboard.btnCreateNewWallet);
        WebDriver.clickOnButton(Dashboard.btnCreateNewWallet);
        test.log(Status.PASS, "Dashboard : Click on Create New Wallet");
        logger.info("Dashboard : Click on Create New Wallet");
    }
}
