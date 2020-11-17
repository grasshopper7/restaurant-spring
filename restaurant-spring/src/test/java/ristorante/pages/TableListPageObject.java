package ristorante.pages;

import static io.cucumber.spring.CucumberTestContext.SCOPE_CUCUMBER_GLUE;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(SCOPE_CUCUMBER_GLUE)
public class TableListPageObject extends HomeFramePageObject<TableListPageObject> implements SwitchScreen {

	@FindBy(id = "refresh")
	private WebElement refreshTablesLink;

	@FindBy(xpath = "//span[@class='tablestatus'][.='VACANT']/preceding-sibling::span[@class='tablenum']")
	private WebElement availableVacantTableLink;
	

	public TableListPageObject(@Qualifier("mainDriver") WebDriver driver) {
		PageFactory.initElements(driver, this);
	}

	public String getAvailableVacantTable() {
		refreshTablesLink.click();
		waitDriver.until(ExpectedConditions.visibilityOfElementLocated(
				By.xpath("//span[@class='tablestatus'][.='VACANT']/preceding-sibling::span[@class='tablenum']")));
		return availableVacantTableLink.getText();
	}

	public void selectTableOrder(String tableNum) {

		waitDriver.until(ExpectedConditions.visibilityOfElementLocated
				(By.xpath("//span[@class='tablenum'][.='" + tableNum + "']"))).click();
	}

	public String getTableStatus(String tableNum) {
		
		WebElement status = waitDriver.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(
				"//span[@class='tablenum'][.='" + tableNum + "']/following-sibling::span")));
		return status.getText();
	}
	
	public String getTableStatusClass(String tableNum) {
		
		WebElement status = waitDriver.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(
				"//span[@class='tablenum'][.='" + tableNum + "']/following-sibling::span")));
		return status.getAttribute("class");
	}

	@Override
	protected void load() {
	}

	@Override
	protected void isLoaded() throws Error {
		try {
			waitDriver.until(ExpectedConditions.invisibilityOfElementLocated(By.id("loadmsg")));
		} catch (Exception e) {
			throw new Error(e);
		}
		frameSwitcher.setCurrentPage(this);
	}

	@Override
	protected String getFrameId() {
		return "tables";
	}
}
