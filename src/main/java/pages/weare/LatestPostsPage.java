package pages.weare;

import org.openqa.selenium.WebDriver;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.telerikacademy.testframework.utils.Utils.getUIMappingByKey;

public class LatestPostsPage extends BaseWearePage {

    public LatestPostsPage(WebDriver driver) {
        super(driver, "weare.latestPostsPage");
    }


    public void assertPostIsCreated(String text) {
        actions.waitForElementPresent("weare.latestPostsPage.postMessage", text);
    }

    public void clickLikeButton(Integer postId) {
        actions.waitForElementClickable("weare.latestPostsPage.likeButton", postId.toString());
        actions.clickElement("weare.latestPostsPage.likeButton", postId.toString());
    }

    public void assertPostIsLiked(Integer postId) {
        actions.waitForElementVisible("weare.latestPostsPage.likesCount", postId.toString(), 1);

        if (!actions.isElementVisible("weare.latestPostsPage.likesCount", postId.toString(), 1)) {
            throw new AssertionError("Incorrect amount of likes.");
        }
    }

    public void explorePost(Integer postId) {
        actions.waitForElementVisible("weare.latestPostPage.exploreThisPostButton", postId.toString());
        actions.clickElement("weare.latestPostPage.exploreThisPostButton", postId.toString());
    }


    public Integer extractPostId() {

        actions.waitForElementClickable("weare.latestPostPage.exploreThisPostButtonFirst");
        String attributeName = "href";
        String hrefValue = actions.getElementAttribute(
                getUIMappingByKey("weare.latestPostPage.exploreThisPostButtonFirst"), attributeName);

        Pattern pattern = Pattern.compile("/posts/(\\d+)$");
        Matcher matcher = pattern.matcher(hrefValue);

        if (matcher.find()) {
            return Integer.parseInt(matcher.group(1));
        } else {
            return null;
        }
    }
}
