package ristorante.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.LoadableComponent;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;


public abstract class BasePageObject<T extends BasePageObject<T>> extends LoadableComponent<T> {

	@Autowired
	@Qualifier("mainDriver")
	protected WebDriver driver;

	@Autowired
	protected WebDriverWait waitDriver;
	
	@Autowired
	protected PageFrameSwitcher frameSwitcher;
	
	
	protected abstract void switchScreen(BasePageObject<?> finalPage);
	
	protected boolean checkSameScreen(BasePageObject<?> finalPage) {
		
		if(frameSwitcher.getCurrentPage().getClass().equals(finalPage.getClass()))
			return true;
		return false;
	}

}
