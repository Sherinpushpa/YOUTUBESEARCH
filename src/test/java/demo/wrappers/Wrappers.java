package demo.wrappers;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.asserts.SoftAssert;

import java.time.Duration;
import java.util.List;

public class Wrappers {
    /*
     * Write your selenium wrappers here
     */
    ChromeDriver driver;

    SoftAssert sa = new SoftAssert();

    public Wrappers(ChromeDriver driver) {
        this.driver = driver;
    }

    public void openUrl(String url) {
        driver.get(url);
    }

    public void urlCheck() {
        boolean status = driver.getCurrentUrl().contains("youtube");
        if (status) {
            System.out.println("The Url is Correct");
        } else {
            System.out.println("The Url is Incorrect");
        }
    }

    public void clickElement(By locator) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(2));
        wait.until(ExpectedConditions.visibilityOfElementLocated(locator)).click();
    }

    public void getText(By locator) {
        String text = driver.findElement(locator).getText();
        System.out.println("The Displayed Text is: " + text);

    }

    public void scrollIntoElement(By locator) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        WebElement scrollElement = driver.findElement(locator);
        js.executeScript("arguments[0].scrollIntoView(true);", scrollElement);
    }

    public void rightArrow(By locator) throws InterruptedException {
        WebElement rightArrowElement = driver.findElement(locator);
        while (true) {
            if (rightArrowElement.isDisplayed()) {
                rightArrowElement.click();
                Thread.sleep(500);
            } else {
                break;
            }
        }
    }

    public void lastMovieDetails(By locator) {
        WebElement lastMovie = driver.findElement(locator);

        String title = lastMovie.findElement(By.xpath(".//h3")).getText();
        System.out.println(title);
        boolean isMature = false;
        String matureLabel = lastMovie.findElement(By.xpath(".//p[contains(text(),'U')]")).getText();
        System.out.println(matureLabel);
        if (matureLabel.equals("A")) {
            isMature = true;
        }
        boolean hasCategory = false;
        WebElement categoryLabel = lastMovie.findElement(By.xpath(
                ".//span[contains(text(),'Comedy') or contains(text(),'Animation') or contains(text(),'Drama')]"));
        hasCategory = categoryLabel.isDisplayed();

        sa.assertFalse(isMature, "Last Movie '" + title + "' has not marked as 'A' for mature");
        sa.assertTrue(hasCategory, "Last Movie '" + title + "' does not belong to any of the categories");

        sa.assertAll();
    }

    public void tracksCount(By locator) {
        String trackCountText = driver.findElement(locator).getText();
        System.out.println(trackCountText);
        boolean flag = false;
        if (Integer.parseInt(trackCountText.split(" ")[0]) <= 50) {
            flag = true;
            System.out.println("The No: of tracks listed is less than or equals 50");
        }

        sa.assertTrue(flag, "The No: of tracks listed is not less than or equals 50");
        sa.assertAll();
    }

    public void latestNews(By locator) {
        List<WebElement> newsList = driver.findElements(locator);
        System.out.println(newsList.size());
        int likes = 0;
        for (int i = 0; i < 3 && i < newsList.size(); i++) {
            WebElement post = newsList.get(i);
            String title = post.findElement(By.xpath(".//a[@id='author-text']")).getText();
            System.out.println(title);

            String bodyElement = post.findElement(By.xpath(".//yt-formatted-string[@id='content-text']")).getText();
            System.out.println("The body Content is: " + bodyElement);

            String likesCount = post.findElement(By.xpath(".//div[@id='toolbar']//span[@id='vote-count-middle']"))
                    .getText().trim();
            System.out.println(likesCount);
            if (likesCount.isEmpty() || likesCount.equals("0")) {
                likes += 0;
            } else {
                int like = Integer.parseInt(likesCount);
                likes += like;
            }
        }
        System.out.println("The Total Likes: " + likes);
    }

    public void enterText(By locator, String searchItem) throws InterruptedException {
       WebElement element =  driver.findElement(locator);
       element.click();
       element.sendKeys(Keys.chord(Keys.CONTROL, "a", Keys.DELETE));
       Thread.sleep(1000);
       element.sendKeys(searchItem);
    }

    public void totalViews(By locator, String searchItem) throws InterruptedException {
        long totalViews = 0;
        JavascriptExecutor js = (JavascriptExecutor) driver;

        while (totalViews < 100000000) {
            List <WebElement> videoElements = driver.findElements(locator);
            Thread.sleep(2000);
            String viewsText = "";
            for (WebElement video : videoElements) {
                if (searchItem.equals("Games")) { 
                    Thread.sleep(1000);
                    viewsText = video.findElement(By.xpath(".//span[contains(text(),'views')]")).getText();
                } else { 
                    viewsText = video.findElement(By.xpath(".//span[contains(text(),'views') or contains(text(),'watching')]")).getText();
                }
                
                long views = parseViews(viewsText);
                totalViews += views;

                if (totalViews >= 100000000) {
                    break;
                }
            }
            js.executeScript("window.scrollBy(0, 1000);");
            Thread.sleep(2000);
        }
        System.out.println("The Total Views for the '" + searchItem + "' is: " + totalViews);
    }

    private long parseViews(String viewCount) {
        viewCount = viewCount.replace(" views", "").replace(" watching", "").trim();

        if (viewCount.endsWith("M")) {
            double number = Double.parseDouble(viewCount.replace("M", ""));
            return (long) (number * 1_000_000);
        } else if (viewCount.endsWith("K")) {
            double number = Double.parseDouble(viewCount.replace("K", ""));
            return (long) (number * 1_000);
        } else {
            return Long.parseLong(viewCount.replaceAll("[^\\d]", ""));
        }
    }

}
