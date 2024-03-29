package ui.tests;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.remote.DesiredCapabilities;
import ui.helpers.Attach;

import java.util.Map;

import static com.codeborne.selenide.Selenide.closeWebDriver;

public class TestBase {

    public static String env = System.getProperty("env", "local");

    @BeforeAll
    static void configure() {
        Configuration.holdBrowserOpen = false;
        Configuration.baseUrl = "https://arena.tarkov.com/";
        Configuration.browser = System.getProperty("browserName", "chrome");
        Configuration.browserVersion = System.getProperty("browserVersion", "120.0");
        Configuration.browserSize = System.getProperty("browserSize", "1920x1080");
        if (env.equals("remote")) {
            Configuration.remote = System.getProperty("remoteUrl", "https://user1:1234@selenoid.autotests.cloud/wd/hub");
            DesiredCapabilities capabilities = new DesiredCapabilities();
            capabilities.setCapability("selenoid:options", Map.<String, Object>of(
                    "enableVNC", true,
                    "enableVideo", true
            ));
            Configuration.browserCapabilities = capabilities;
        }
    }

    @BeforeEach
    void addListener() {
        SelenideLogger.addListener("AllureSelenide", new AllureSelenide());
    }

    @AfterEach
    void addAttachments() {
        Attach.screenshotAs("Last screenshot");
        Attach.pageSource();
        Attach.browserConsoleLogs();
        if (env.equals("remote"))
            Attach.addVideo();

        closeWebDriver();
    }
}