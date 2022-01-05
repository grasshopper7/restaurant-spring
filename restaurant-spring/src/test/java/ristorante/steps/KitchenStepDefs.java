package ristorante.steps;

import static org.assertj.core.api.Assertions.assertThat;

import org.springframework.beans.factory.annotation.Autowired;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import ristorante.entity.Order;
import ristorante.entity.Order.OrderStatus;
import ristorante.pages.KitchenPageObject;
import ristorante.pages.ScenarioData;

public class KitchenStepDefs {

	@Autowired
	private KitchenPageObject kitchenPO;

	@Autowired
	private ScenarioData data;
	
	
	@When("User promotes order to {orderStatus} status in kitchen")
	public void userPromotesOrderToNewStatus(OrderStatus status) {
	    
		data.setOrderNo(kitchenPO.getOrderNum(data.getTableNo()));
		if(status == OrderStatus.PREPARING)
			kitchenPO.promoteOrderToPreparing(data.getTableNo());
		if(status == OrderStatus.READY)
			kitchenPO.promoteOrderToReady(data.getTableNo());
		data.getInitialOrder().setStatus(status);
	}
	
	@SuppressWarnings("deprecation")
	@Then("Order should be available in {orderStatus} status in kitchen")
	public void orderShouldBeAvailableInStatusInKitchenPage(OrderStatus status) {

		Order expectedOrder = data.getInitialOrder();
		//System.out.println("kitchen expectedOrder - "+expectedOrder);
		Order actualOrder = kitchenPO.getOrderDetails(data.getOrderNo(), status.toString().toLowerCase());	
		//System.out.println("kitchen actualOrder - "+actualOrder);
		assertThat(actualOrder).isEqualToIgnoringGivenFields(expectedOrder, "id", "orderLines").extracting("orderLines")
			.asList().usingElementComparatorIgnoringFields("id").containsOnlyElementsOf(expectedOrder.getOrderLines());

	}
	
	@Then("Order should not be available in kitchen")
	public void orderShouldNotBeAvailableInInKitchenPage() {
	    
		//System.out.println("kitchen not order - " + kitchenPO.orderForTableExists(data.getTableNo().substring(5)));
		assertThat(kitchenPO.orderForTableExists(data.getTableNo().substring(5))).isEqualTo(false);	
	}
}
