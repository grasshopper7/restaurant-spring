package ristorante.pages;

import static io.cucumber.spring.CucumberTestContext.SCOPE_CUCUMBER_GLUE;

import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Predicate;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(SCOPE_CUCUMBER_GLUE)
public class HomePageObject extends BasePageObject<HomePageObject> {

	@FindBy(id = "kitchen")
	@CacheLookup
	private WebElement kitchenLink;

	@FindBy(id = "server")
	@CacheLookup
	private WebElement serverLink;

	public HomePageObject(@Qualifier("mainDriver") WebDriver driver) {
		PageFactory.initElements(driver, this);
	}

	@Override
	protected void load() {
		driver.get("http://localhost:8080/");
	}

	@Override
	protected void isLoaded() throws Error {
		try {
			waitDriver.until(ExpectedConditions.and(ExpectedConditions.visibilityOf(kitchenLink),
					ExpectedConditions.visibilityOf(serverLink)));
			frameSwitcher.setHomeWindowHandle(driver.getWindowHandle());
		} catch (Exception e) {
			throw new Error(e);
		}
		frameSwitcher.setCurrentPage(this);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void switchScreen(BasePageObject<?> finalPage) {
		if (checkSameScreen(finalPage))
			return;

		if (HomeFramePageObject.class.isAssignableFrom(finalPage.getClass())) {

			waitDriver.until(
					ExpectedConditions.frameToBeAvailableAndSwitchToIt(((HomeFramePageObject) finalPage).getFrameId()));
		} else if (finalPage.getClass().equals(KitchenPageObject.class)) {

			if (frameSwitcher.getKitchenWindowHandle() == null) {

				Predicate<String> pred = h -> !h.equalsIgnoreCase(frameSwitcher.getServerWindowHandle());
				Consumer<String> cons = h -> frameSwitcher.setKitchenWindowHandle(h);
				storeWindowHandles(kitchenLink, pred, cons);
			}
			driver.switchTo().window(frameSwitcher.getKitchenWindowHandle());
		} else if (finalPage.getClass().equals(ServerPageObject.class)) {

			if (frameSwitcher.getServerWindowHandle() == null) {

				Predicate<String> pred = h -> !h.equalsIgnoreCase(frameSwitcher.getKitchenWindowHandle());
				Consumer<String> cons = h -> frameSwitcher.setServerWindowHandle(h);
				storeWindowHandles(serverLink, pred, cons);
			}
			driver.switchTo().window(frameSwitcher.getServerWindowHandle());
		}
		finalPage.get();
	}

	private void storeWindowHandles(WebElement link, Predicate<String> checkHandle, Consumer<String> saveHandle) {

		int windHandles = driver.getWindowHandles().size();
		link.click();
		waitDriver.until(ExpectedConditions.numberOfWindowsToBe(windHandles + 1));
		Set<String> handles = driver.getWindowHandles();
		handles.forEach(h -> {
			if (!h.equalsIgnoreCase(frameSwitcher.getHomeWindowHandle()) && checkHandle.test(h))
				saveHandle.accept(h);
		});
	}

}
