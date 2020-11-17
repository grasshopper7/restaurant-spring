package ristorante.pages;

import static io.cucumber.spring.CucumberTestContext.SCOPE_CUCUMBER_GLUE;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(SCOPE_CUCUMBER_GLUE)
public class PageFrameSwitcher {
	
	private BasePageObject<?> currentPage;
	
	private String homeWindowHandle;

	private String kitchenWindowHandle;
	
	private String serverWindowHandle;


	public BasePageObject<?> getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(BasePageObject<?> currentPage) {
		this.currentPage = currentPage;
	}

	public String getHomeWindowHandle() {
		return homeWindowHandle;
	}

	public void setHomeWindowHandle(String homeWindowHandle) {
		this.homeWindowHandle = homeWindowHandle;
	}

	public String getKitchenWindowHandle() {
		return kitchenWindowHandle;
	}

	public void setKitchenWindowHandle(String kitchenWindowHandle) {
		this.kitchenWindowHandle = kitchenWindowHandle;
	}
	
	public String getServerWindowHandle() {
		return serverWindowHandle;
	}

	public void setServerWindowHandle(String serverWindowHandle) {
		this.serverWindowHandle = serverWindowHandle;
	}
	
	public void switchScreen(BasePageObject<?> finalPage) {
		currentPage.switchScreen(finalPage);
	}
}
