package ristorante.pages;

import org.springframework.beans.factory.annotation.Autowired;

public abstract class HomeFramePageObject<T extends HomeFramePageObject<T>> extends BasePageObject<T> {

	@Autowired
	protected HomePageObject homePO;


	@Override
	protected void switchScreen(BasePageObject<?> finalPage) {
		
		if (checkSameScreen(finalPage))
			return;
		
		driver.switchTo().defaultContent();
		homePO.get();
		
		if (!finalPage.getClass().equals(HomePageObject.class))
			homePO.switchScreen(finalPage);		
	}
	
	protected abstract String getFrameId();
}
