package ristorante.steps;

import static org.assertj.core.api.Assertions.assertThat;

import org.springframework.beans.factory.annotation.Autowired;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import ristorante.entity.Order;
import ristorante.pages.OrderPageObject;
import ristorante.pages.ScenarioData;
import ristorante.pages.TableListPageObject;

public class TableStepDefs {

	@Autowired
	private TableListPageObject tablePO;

	@Autowired
	private OrderPageObject orderPO;

	@Autowired
	private ScenarioData data;
	

	@When("User selects (vacant )table")
	public void userSelectsTable() {

		tablePO.selectTableOrder(data.getTableNo());

		// Store the initial order details
		Order initialOrder = orderPO.getOrderDetails();
		data.setInitialOrder(initialOrder);
	}

	@When("User selects control order")
	public void userSelectsControlTable() {

		tablePO.selectTableOrder("Table1");
	}

	@When("User selects order in Ordered/Preparing/Ready/Served status")
	public void userSelectsOrderInStatus() {
		userSelectsTable();
	}

	@Then("Order status in table list should be {word}")
	public void orderStatusInTableListShouldBeUpdated(String status) {

		// System.out.println("Table order status -
		// "+tablePO.getTableStatus(data.getTableNo()));
		assertThat(tablePO.getTableStatus(data.getTableNo())).isEqualToIgnoringCase(status.toString().toUpperCase());
	}

	@Then("Order status text in table list should be highlighted")
	public void orderStatusInTableListShouldBeHighlighted() {

		// System.out.println("class--"+tablePO.getTableStatusClass(data.getTableNo()));
		assertThat(tablePO.getTableStatusClass(data.getTableNo())).isEqualToIgnoringCase("hightablestatus");
	}

	@Then("Order status text in table list should not be highlighted")
	public void orderStatusInTableListShouldNotBeHighlighted() {

		// System.out.println("class--"+tablePO.getTableStatusClass(data.getTableNo()));
		assertThat(tablePO.getTableStatusClass(data.getTableNo())).isEqualToIgnoringCase("tablestatus");
	}

	@Then("Table should be vacant in table list")
	public void tableShouldBeVacantInTableList() {

		assertThat(tablePO.getTableStatus(data.getTableNo())).isEqualToIgnoringCase("VACANT");
	}
}
