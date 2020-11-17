package ristorante.steps;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;

import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.IntBinaryOperator;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import ristorante.entity.Dish;
import ristorante.entity.Order;
import ristorante.entity.Order.OrderStatus;
import ristorante.entity.OrderLine;
import ristorante.entity.Tables;
import ristorante.pages.OrderPageObject;
import ristorante.pages.ScenarioData;

public class OrderStepDefs {

	@Autowired
	private OrderPageObject orderPO;

	@Autowired
	private ScenarioData data;


	private BiConsumer<Optional<OrderLine>, Dish> checkDish = (l, d) -> {
		if (!l.isPresent())
			fail("Dish " + d.getName() + " is not an existing ordered dish.");
	};

	private BiConsumer<List<OrderLine>, Optional<OrderLine>> removeLine = (l, o) -> l.remove(o.get());

	private BiFunction<Dish, Integer, OrderLine> line = (d, q) -> new OrderLine(0, d, q);

	private BiConsumer<List<OrderLine>, OrderLine> addLine = (l, o) -> l.add(o);


	@When("User creates new order by selecting dishes")
	public void userCreatesNewOrderBySelectingDishes(List<OrderLine> lines) {

		for (int i = 0; i < lines.size(); i++) {
			// Complex ways of adding quantities for first 2 dishes
			orderPO.selectDish(lines.get(i).getDish().getName());
			int qty = lines.get(i).getQty();
			if (i == 0) {
				orderPO.addQuantityToDish(3);
				orderPO.subtractQuantityFromDish(3);
			} else if (i == 1) {
				orderPO.addQuantityToDish(5);
				orderPO.subtractQuantityFromDish(3);
				orderPO.clearDishQuantity();
			}
			orderPO.addQuantityToDish(qty);
		}
		orderPO.saveOrder();

		data.getInitialOrder().setOrderLines(lines);
		data.getInitialOrder().setStatus(OrderStatus.ORDERED);
	}

	@When("User creates new order without selecting dish")
	public void userCreatesNewOrderWithoutSelectingDish() {

		orderPO.saveOrder();
	}

	@SuppressWarnings("unchecked")
	private void orderOperations(List<OrderLine> lines, BiConsumer<Optional<OrderLine>, Dish> checkDish,
			BiConsumer<OrderPageObject, Integer> operation,
			BiConsumer<List<OrderLine>, Optional<OrderLine>> removeLine, Object[] addLineArgs) {

		List<OrderLine> existingLines = data.getInitialOrder().getOrderLines();
		for (int i = 0; i < lines.size(); i++) {
			Dish dish = lines.get(i).getDish();
			Optional<OrderLine> existLine = existingLines.stream()
					.filter(l -> l.getDish().getName().equalsIgnoreCase(dish.getName())).findAny();

			checkDish.accept(existLine, dish);

			orderPO.selectDish(dish.getName());
			operation.accept(orderPO, lines.get(i).getQty());

			removeLine.accept(existingLines, existLine);

			if (addLineArgs.length == 3) {
				IntBinaryOperator quantity = (IntBinaryOperator) addLineArgs[0];
				BiFunction<Dish, Integer, OrderLine> line = (BiFunction<Dish, Integer, OrderLine>) addLineArgs[1];
				BiConsumer<List<OrderLine>, OrderLine> addLine = (BiConsumer<List<OrderLine>, OrderLine>) addLineArgs[2];

				int existqty = existLine.isPresent() ? existLine.get().getQty() : 0;
				addLine.accept(existingLines, line.apply(dish, quantity.applyAsInt(existqty, lines.get(i).getQty())));
			}
		}
		orderPO.modifyOrder();
	}

	@When("User adds following quantitiy to existing dishes")
	public void userAddsQuantityToExistingDish(List<OrderLine> lines) {

		BiConsumer<OrderPageObject, Integer> operation = (m, n) -> m.addQuantityToDish(n);
		IntBinaryOperator quantity = (i, j) -> i + j;
		orderOperations(lines, checkDish, operation, removeLine, new Object[] { quantity, line, addLine });
	}

	@When("User adds following new dishes")
	public void userAddsNewDish(List<OrderLine> lines) {

		BiConsumer<Optional<OrderLine>, Dish> checkDish = (l, d) -> {
			if (l.isPresent())
				fail("Dish " + d.getName() + " is an existing ordered dish.");
		};
		BiConsumer<OrderPageObject, Integer> operation = (m, n) -> m.addQuantityToDish(n);
		BiConsumer<List<OrderLine>, Optional<OrderLine>> removeLine = (l, o) -> {
		};
		IntBinaryOperator quantity = (i, j) -> j;

		orderOperations(lines, checkDish, operation, removeLine, new Object[] { quantity, line, addLine });
	}

	@When("User subtracts following quantity from existing dishes")
	public void userSubtractsQuantityFromExistingDish(List<OrderLine> lines) {

		OrderStatus status = data.getInitialOrder().getStatus();
		if (status == OrderStatus.ORDERED) {
			BiConsumer<OrderPageObject, Integer> operation = (m, n) -> m.subtractQuantityFromDish(n);
			IntBinaryOperator quantity = (i, j) -> i - j;
			orderOperations(lines, checkDish, operation, removeLine, new Object[] { quantity, line, addLine });
		} else if (status == OrderStatus.PREPARING) {
			lines.stream().forEach(l -> {
				orderPO.selectDish(l.getDish().getName());
				orderPO.subtractQuantityFromDishPreparingState(l.getQty());
			});
		}
	}

	@When("User removes following existing dishes")
	public void userRemovesExistingDish(List<Dish> dishes) {

		List<OrderLine> lines = dishes.stream().map(d -> new OrderLine(0, d, 0)).collect(Collectors.toList());

		OrderStatus status = data.getInitialOrder().getStatus();
		if (status == OrderStatus.ORDERED) {
			BiConsumer<OrderPageObject, Integer> operation = (m, n) -> m.clearDishQuantity();
			orderOperations(lines, checkDish, operation, removeLine, new Object[0]);
		} else if (status == OrderStatus.PREPARING) {
			dishes.stream().forEach(d -> {
				orderPO.selectDish(d.getName());
				orderPO.clearDishQuantityPreparingState();
			});
		}
	}

	@When("User subtracts quantity till zero from following existing dishes")
	public void userSubtractsQuantityTillZero(List<Dish> dishes) {

		List<OrderLine> lines = data.getInitialOrder().getOrderLines().stream()
				.filter(l -> dishes.contains(l.getDish())).collect(Collectors.toList());

		BiConsumer<OrderPageObject, Integer> operation = (m, n) -> m.subtractQuantityFromDish(n);
		orderOperations(lines, checkDish, operation, removeLine, new Object[0]);
	}

	@When("User cancels the order")
	public void userCancelsTheOrder() {

		orderPO.cancelOrder();

		OrderStatus initialState = data.getInitialOrder().getStatus();
		if (initialState == OrderStatus.ORDERED)
			data.getInitialOrder().setStatus(OrderStatus.CANCELLED);
		if (initialState == OrderStatus.PREPARING)
			data.getInitialOrder().setStatus(OrderStatus.BILLED);

		data.getInitialOrder().setTable(new Tables(0));
	}

	@When("User cancels the order cancellation")
	public void userCancelsTheOrderCancellation() {

		//frameSwitcher.switchScreen(orderPO);
		orderPO.cancelOrderCancellation();
	}

	@SuppressWarnings("deprecation")
	@Then("Created/Updated/Promoted order details should be displayed")
	public void orderDetailsShouldBeUpdatedDisplayed() {

		Order expectedOrder = data.getInitialOrder();
		Order actualOrder = orderPO.getOrderDetails();
		//System.out.println("expectedOrder - " + expectedOrder);
		//System.out.println("actualOrder - " + actualOrder);
		assertThat(actualOrder).isEqualToIgnoringGivenFields(expectedOrder, "id", "orderLines").extracting("orderLines")
				.asList().usingElementComparatorIgnoringFields("id")
				.containsOnlyElementsOf(expectedOrder.getOrderLines());

		data.setOrderNo(String.valueOf(actualOrder.getId()));
	}
	
	@Then("Modification message should be displayed in order details")
	public void modificationMessageShouldBeDisplayedInOrderDetails() {
		
		//System.out.println("message--"+orderPO.getOrderMessage());
		assertThat(orderPO.getOrderMessage()).isEqualToIgnoringCase("The order for this table has been updated. Press OK to view.");
	}
	
	@Then("Modification message should not be displayed in order details")
	public void modificationMessageShouldNotBeDisplayedInOrderDetails() {
		
		//System.out.println("message--"+orderPO.getOrderMessage());
		assertThat(orderPO.getOrderMessage()).isEmpty();
	}
	
	@Then("Order modify and cancel buttons should be disabled")
	public void orderModifyAndCancelButtonsShouldBeDisabled() {

		boolean[] enabledStatus = {false, false};
		assertThat(orderPO.retrieveButtonEnabledStatus()).containsExactly(enabledStatus);
	}
	
	@Then("Order modify and cancel buttons should not be disabled")
	public void orderModifyAndCancelButtonsShouldNotBeDisabled() {

		boolean[] enabledStatus = {true, true};
		assertThat(orderPO.retrieveButtonEnabledStatus()).containsExactly(enabledStatus);
	}
	
	@Then("User refreshes order")
	public void userRefreshesOrder() {
		
		orderPO.refreshOrder();
	}

	@SuppressWarnings("deprecation")
	@Then("Order (details )should not be created/updated")
	public void orderShouldNotBeCreated() {

		Order expectedOrder = data.getInitialOrder();
		Order actualOrder = orderPO.getOrderDetails();
		//System.out.println("expectedOrder - " + expectedOrder);
		//System.out.println("actualOrder - " + actualOrder);
		assertThat(actualOrder).isEqualToIgnoringGivenFields(expectedOrder, "id", "orderLines").extracting("orderLines")
				.asList().usingElementComparatorIgnoringFields("id")
				.containsOnlyElementsOf(expectedOrder.getOrderLines());

		if (orderPO.orderNumberDisplay())
			data.setOrderNo(orderPO.getOrderNumber());
	}

	@Then("Alert is displayed with order creation message")
	public void alertIsDisplayedWithOrderCreationMessage() {

		assertThat(orderPO.getAlertMessage()).isEqualTo("Order is created.");
	}

	@Then("Alert is displayed with order updation message")
	public void alertIsDisplayedWithOrderUpdationMessage() {

		assertThat(orderPO.getAlertMessage()).isEqualTo("Order is updated.");
	}

	@Then("Alert is displayed with order creation warning")
	public void alertIsDisplayedWithIllegalOperationWarning() {

		assertThat(orderPO.getAlertMessage()).isEqualTo("Order needs atleast one dish.");
	}

	@Then("Alert is displayed with status changed warning")
	public void alertIsDisplayedWithStatusChangedWarning() {

		OrderStatus status = data.getInitialOrder().getStatus();

		if (status == OrderStatus.PREPARING) {
			assertThat(orderPO.getAlertMessage())
					.isEqualTo("Order status has already changed from ORDERED TO PREPARING. Kindly modify again.");
		} else if (status == OrderStatus.READY) {
			assertThat(orderPO.getAlertMessage())
					.isEqualTo("Order status has already changed from PREPARING TO READY. Order cannot be modified.");
		}
	}

	@Then("Alert is displayed with illegal dish quantity removal warning")
	public void alertIsDisplayedWithIllegalDishQuantityRemovalWarning() {

		assertThat(orderPO.getAlertMessage())
				.isEqualTo("Your order is getting prepared. Number of already ordered dishes cannot be reduced.");
	}

	@Then("Alert is displayed with illegal dish removal warning")
	public void alertIsDisplayedWithIllegalDishRemovalWarning() {

		assertThat(orderPO.getAlertMessage())
				.isEqualTo("Your order is getting prepared. Already ordered dishes cannot be removed.");
	}
}
