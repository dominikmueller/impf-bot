package de.tfr.impf.selenium

import de.tfr.impf.config.Config
import org.openqa.selenium.WebDriver
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeOptions
import java.util.concurrent.TimeUnit

fun createDriver(): ChromeDriver {
    System.setProperty(Config.nameDriver, Config.pathDriver + Config.exeDriver)
    val chromeOptions = ChromeOptions()
    if (Config.hasUserAgent) {
        chromeOptions.addArguments("general.useragent.override", Config.userAgent)
    }
    // Mitigate Bot Detection
    chromeOptions.addArguments("--disable-blink-features=AutomationControlled")
    chromeOptions.setExperimentalOption("excludeSwitches", Array<String>(1){"enable-automation"})
    chromeOptions.setExperimentalOption("useAutomationExtension", false)

    val chromeDriver = ChromeDriver(chromeOptions)
    chromeDriver.setTimeOut(Config.searchElementTimeout())

    // Mitigate Bot Detection
    chromeDriver.executeScript("Object.defineProperty(navigator, 'webdriver', {get: () => undefined})")

    return chromeDriver
}

fun WebDriver.setTimeOut(milliseconds: Long) {
    this.manage()?.timeouts()?.implicitlyWait(milliseconds, TimeUnit.MILLISECONDS)
}
