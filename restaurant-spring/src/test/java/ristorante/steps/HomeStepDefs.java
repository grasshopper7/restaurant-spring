package ristorante.steps;

import org.springframework.beans.factory.annotation.Autowired;

import io.cucumber.java.en.Given;
import ristorante.pages.HomePageObject;


public class HomeStepDefs {

	@Autowired
	private HomePageObject homePO;
			
	@Given("User navigates to home page")
	public void userNavigatesToHomePage() {		
		
		homePO.get();
	}
}
