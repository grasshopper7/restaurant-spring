package ristorante.steps;

import static org.assertj.core.api.Assertions.assertThat;

import org.springframework.beans.factory.annotation.Autowired;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import ristorante.pages.AltBrowserPageObject;
import ristorante.pages.AltBrowserPageObject.BrowserType;
import ristorante.pages.ScenarioData;

public class AltBrowserStepDefs {

	@Autowired
	private AltBrowserPageObject altBrowPO;

	@Autowired
	private ScenarioData data;
	

	@Given("User navigates to home page in {browser} control browser")
	public void userNavigatesToHomePageInControlBrowser(BrowserType type) {

		altBrowPO.navigateToHomePage(type);
	}

	@When("User selects order in Ordered status in {browser} control browser")
	public void userSelectsOrderInStatusControlBrowser(BrowserType type) {

		altBrowPO.navigateToSelectedTable(type, data.getTableNo());
	}

	@Given("User selects control order in {browser} control browser")
	public void userSelectsControlOrderInControlBrowser(BrowserType type) {

		altBrowPO.navigateToSelectedTable(type, "Table1");
	}

	@Then("Order status in table list should be {word} in {browser} control browser")
	public void orderStatusInTableListShouldBeUpdated(String status, BrowserType type) {

		String tableStatus = altBrowPO.retrieveTableStatus(type, data.getTableNo());
		assertThat(tableStatus).isEqualToIgnoringCase(status.toString());
	}

	@Then("Order status text in table list should be highlighted in {browser} control browser")
	public void orderStatusInTableListShouldBeHighlightedControlBrowser(BrowserType type) {

		String tableTextClass = altBrowPO.retrieveTableStatusTextClass(type, data.getTableNo());
		assertThat(tableTextClass).isEqualToIgnoringCase("hightablestatus");
	}

	@Then("Order status text in table list should not be highlighted in {browser} control browser")
	public void orderStatusInTableListShouldNotBeHighlightedControlBrowser(BrowserType type) {

		String tableTextClass = altBrowPO.retrieveTableStatusTextClass(type, data.getTableNo());
		assertThat(tableTextClass).isEqualToIgnoringCase("tablestatus");
	}

	@Then("Modification message should be displayed in order details in {browser} control browser")
	public void modificationMessageShouldBeDisplayedInOrderDetailsControlBrowser(BrowserType type) {

		String modifMsg = altBrowPO.retrieveModificationOrderMessage(type);
		assertThat(modifMsg).isEqualToIgnoringCase("The order for this table has been updated. Press OK to view.");
	}

	@Then("Modification message should not be displayed in order details in {browser} control browser")
	public void modificationMessageShouldNotBeDisplayedInOrderDetailsControlBrowser(BrowserType type) {

		String modifMsg = altBrowPO.retrieveModificationOrderMessage(type);
		assertThat(modifMsg).isEmpty();
	}

	@Then("Order modify and cancel buttons should be disabled in {browser} control browser")
	public void orderModifyAndCancelButtonsShouldBeDisabledControlBrowser(BrowserType type) {

		boolean[] enabledStatus = { false, false };
		assertThat(altBrowPO.retrieveButtonEnableStatus(type)).containsExactly(enabledStatus);
	}

	@Then("Order modify and cancel buttons should not be disabled in {browser} control browser")
	public void orderModifyAndCancelButtonsShouldNotBeDisabledControlBrowser(BrowserType type) {

		boolean[] enabledStatus = { true, true };
		assertThat(altBrowPO.retrieveButtonEnableStatus(type)).containsExactly(enabledStatus);
	}
	
	@Then("alt page")
	public void altPage() {
		System.out.println("ALT BROWSER STEP DEFINITION");
	}


}
