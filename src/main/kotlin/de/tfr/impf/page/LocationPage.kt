package de.tfr.impf.page

import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement
import org.openqa.selenium.support.ui.ExpectedConditions
import org.openqa.selenium.support.ui.FluentWait

import org.openqa.selenium.support.ui.Wait




class LocationPage(driver: WebDriver) : AbstractPage(driver) {

    fun title(): WebElement? = findAnyBy("//h1")

    override fun isDisplayed() = title()?.text == "Wurde Ihr Anspruch auf eine Corona-Schutzimpfung bereits geprüft?"

    /**
     * Already approved -> No
     */
    fun askForClaim() {
        findAnyBy(claimSelection("Nein"))?.click()
    }

    /**
     * Fo you have a verification code -> Yes
     */
    fun confirmClaim() {
        findAnyBy(claimSelection("Ja"))?.click()
    }

    private fun claimSelection(text: String) =
        "//input[@type='radio' and @name='vaccination-approval-checked']//following-sibling::span[contains(text(),'$text')]/.."

    fun submitInput() {
        findAnyBy("//button[@type='submit']")?.click()
    }

    /**
     * Are you an approved person -> yes
     */
    fun checkCorrectPerson() {
        findAnyBy("//input[@type='radio' and @formcontrolname='isValid']//following-sibling::span[contains(text(),'Ja')]/..")?.click()
    }

    fun enterAge(age: Int) {
        var input = findBy("//input[@formcontrolname='age']")
        input.click()
        sendKeys(input, "" + age)
        //input.sendKeys("" + age)
    }

    /**
     * Locates the warning on top or on bottom
     * @return free seats are not available
     */
    fun isFull(): Boolean {
        return findAll("//div[contains(@class, 'alert-danger') and contains(text(), 'keine')]").isNotEmpty()
    }

    fun codeField(index: Int) = findAnyBy("//input[@type='text' and @data-index='$index']")

    private fun fillCodeField(index: Int, code: String) {
        codeField(index)?.click()
        sendKeys(codeField(index), code)
        //(index)?.sendKeys(code)
    }

    fun enterCodeSegment0(code: String) = fillCodeField(0, code)
    fun enterCodeSegment1(code: String) = fillCodeField(1, code)
    fun enterCodeSegment2(code: String) = fillCodeField(2, code)

    /**
     * Termin suchen
     */
    fun searchForFreeDate() = submitInput()

    /**
     * Impftermine > Termin suchen
     */
    fun searchForVaccinateDate() = findAnyBy("//button[contains(text(),'Termine suchen')]")?.click()

    fun hasNoVaccinateDateAvailable(): Boolean =
        (findAnyBy("//span[@class='its-slot-pair-search-no-results']")?.isDisplayed) ?: false

    fun hasVacError(): Boolean =
        findAll("//span[contains(@class, 'text-pre-wrap') and contains(text(), 'Fehler')]").isNotEmpty()


    fun sendKeys(element: WebElement?, keys: String) {
        val wait: Wait<*> = FluentWait<Any?>(driver)
            .ignoring(NoSuchElementException::class.java)


        for (i in 0 until keys.length) {
            element?.sendKeys(Character.toString(keys[i]))
            wait.until {ExpectedConditions.attributeContains(element, "value", keys.substring(0, i))}
        }
    }
}

