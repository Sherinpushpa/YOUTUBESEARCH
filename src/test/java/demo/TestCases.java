package demo;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.time.Duration;
import java.util.logging.Level;

import demo.utils.ExcelDataProvider;
// import io.github.bonigarcia.wdm.WebDriverManager;
import demo.wrappers.Wrappers;

public class TestCases extends ExcelDataProvider { // Lets us read the data
        ChromeDriver driver;
        Wrappers wrapper;

        /*
         * TODO: Write your tests here with testng @Test annotation.
         * Follow `testCase01` `testCase02`... format or what is provided in
         * instructions
         */

        /*
         * Do not change the provided methods unless necessary, they will help in
         * automation and assessment
         */
        @BeforeTest
        public void startBrowser() {
                System.setProperty("java.util.logging.config.file", "logging.properties");

                // NOT NEEDED FOR SELENIUM MANAGER
                // WebDriverManager.chromedriver().timeout(30).setup();

                ChromeOptions options = new ChromeOptions();
                LoggingPreferences logs = new LoggingPreferences();

                logs.enable(LogType.BROWSER, Level.ALL);
                logs.enable(LogType.DRIVER, Level.ALL);
                options.setCapability("goog:loggingPrefs", logs);
                options.addArguments("--remote-allow-origins=*");

                System.setProperty(ChromeDriverService.CHROME_DRIVER_LOG_PROPERTY, "build/chromedriver.log");

                driver = new ChromeDriver(options);

                driver.manage().window().maximize();

                wrapper = new Wrappers(driver);
        }

        @Test
        public void testCase01() throws InterruptedException {
                System.out.println("Start Test Case: testCase01");
                wrapper.openUrl("https://www.youtube.com/");
                wrapper.urlCheck();
                JavascriptExecutor js = (JavascriptExecutor) driver;
                js.executeScript("window.scrollBy(0, 1000);");
                Thread.sleep(2000);
                wrapper.clickElement(By.xpath("//a[text()='About']"));
                wrapper.getText(By.id("content"));
                System.out.println("End Test Case: testCase01");
        }

        @Test
        public void testCase02() throws InterruptedException {
                System.out.println("Start Test Case: testCase02");
                driver.navigate().back();
                Thread.sleep(1000);
                // JavascriptExecutor js = (JavascriptExecutor) driver;
                // js.executeScript("window.scrollBy(0, 1000);");
                // Thread.sleep(1000);
                wrapper.clickElement(By.xpath("//a[@title='Movies']"));
                Thread.sleep(1000);
                // WebElement topSelling = driver.findElement(By.xpath("//span[text()='Top
                // selling']"));
                // js.executeScript("arguments[0].scrollIntoView(true);", topSelling);
                wrapper.scrollIntoElement(By.xpath("//span[text()='Top selling']"));
                Thread.sleep(1000);
                wrapper.rightArrow(By.xpath("//button[@aria-label='Next']"));
                Thread.sleep(1000);
                wrapper.lastMovieDetails(By.xpath(
                                "//span[text()='Top selling']/following::div[@id='items']/ytd-grid-movie-renderer[last()]"));
                Thread.sleep(5000);
                System.out.println("End Test Case: testCase02");
        }

        @Test
        public void testCase03() throws InterruptedException {
                System.out.println("Start Test Case: testCase03");
                //Bollywood hitlist track
                wrapper.clickElement(By.xpath("//a[@title='Music']"));
                Thread.sleep(1000);
                wrapper.scrollIntoElement(By.xpath("//a[contains(@title,'Biggest Hits')]"));
                Thread.sleep(1000);
                wrapper.rightArrow(By.xpath(
                                "//a[contains(@title,'Biggest Hits')]/ancestor::div[contains(@class,'grid-subheader')]/following-sibling::div[@id='contents']//button[@aria-label='Next']"));
                Thread.sleep(1000);
                wrapper.getText(By.xpath("//h3[text()[normalize-space()='Bollywood Dance Hitlist']]"));
                Thread.sleep(1000);
                wrapper.tracksCount(By.xpath(
                                "//h3[text()[normalize-space()='Bollywood Dance Hitlist']]/following-sibling::p[text()[normalize-space()='50 tracks']]"));
                System.out.println("End Test Case: testCase03");
        }

        @Test
        public void testCase04() throws InterruptedException {
                System.out.println("Start Test Case: testCase04"); 
                //search for news content
                wrapper.scrollIntoElement(By.xpath("//a[@title='News']"));
                Thread.sleep(1000);
                wrapper.clickElement(By.xpath("//a[@title='News']"));
                Thread.sleep(2000);
                wrapper.scrollIntoElement(By.xpath("(//button[@aria-label='Show more'])[1]"));
                Thread.sleep(1000);
                wrapper.latestNews(By.xpath("//span[text()='Latest news posts']/ancestor::div[@id='rich-shelf-header-container']/following-sibling::div//div[@class='style-scope ytd-rich-item-renderer']"));
                System.out.println("End Test Case: testCase04");
        }

        @Test(dataProvider = "excelData", dataProviderClass = ExcelDataProvider.class)
        public void testCase05(String searchItem) throws InterruptedException {
                System.out.println("Start Test Case: testCase05");
                //passing the datas from excel
                wrapper.enterText(By.xpath("//input[@id='search']"), searchItem + Keys.ENTER);
                Thread.sleep(1000);
                wrapper.totalViews(By.xpath("//div[@id='title-wrapper']/following-sibling::ytd-video-meta-block"), searchItem);
                System.out.println("End Test Case: testCase05");
        }

        @AfterTest
        public void endTest() {
                driver.close();
                driver.quit();

        }
}