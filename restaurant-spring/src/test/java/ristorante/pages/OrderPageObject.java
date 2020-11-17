package ristorante.pages;

import static io.cucumber.spring.CucumberTestContext.SCOPE_CUCUMBER_GLUE;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import ristorante.entity.Dish;
import ristorante.entity.Order;
import ristorante.entity.Order.OrderStatus;
import ristorante.entity.OrderLine;
import ristorante.entity.Tables;

@Component
@Scope(SCOPE_CUCUMBER_GLUE)
public class OrderPageObject extends HomeFramePageObject<OrderPageObject> implements SwitchScreen {

	@FindBy(id = "createOrder")
	private WebElement createOrder;

	@FindBy(id = "modifyOrder")
	private WebElement modifyOrder;

	@FindBy(id = "cancelOrder")
	private WebElement cancelOrder;

	@FindBy(id = "status")
	private WebElement orderStatus;

	@FindBy(id = "ordernum")
	private WebElement orderNumber;

	@FindBy(id = "tablenum")
	private WebElement tableNumber;
	
	@FindBy(id = "message")
	private WebElement message;
	
	@FindBy(id = "refresh")
	private WebElement refresh;

	@FindBy(xpath = "//input[@class='dishqty'][@value!='']/..")
	private List<WebElement> selectedDishes;

	private String alertMessage;

	private DishMenuPageObject dishPO;
	

	public OrderPageObject(@Qualifier("mainDriver") WebDriver driver) {
		PageFactory.initElements(driver, this);
	}

	public String getAlertMessage() {
		return alertMessage;
	}

	private void handleAlert(boolean ... acceptOrDismiss) {
		
		Alert alert = waitDriver.until(ExpectedConditions.alertIsPresent());
		alertMessage = alert.getText();
		// Just for displaying alert message.
		try {
			Thread.sleep(250);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		if(acceptOrDismiss.length==1 && acceptOrDismiss[0]==false)
			alert.dismiss();
		else
			alert.accept();
	}
	
	public void saveOrder() {

		waitDriver.until(ExpectedConditions.visibilityOf(createOrder)).click();
		handleAlert();
		waitDriver.until(ExpectedConditions.invisibilityOfElementLocated(By.id("loadmsg")));
	}
	
	public void cancelOrder() {

		waitDriver.until(ExpectedConditions.visibilityOf(cancelOrder)).click();
		handleAlert();		
		handleAlert();		
		waitDriver.until(ExpectedConditions.invisibilityOfElementLocated(By.id("loadmsg")));
	}
	
	public void cancelOrderCancellation() {

		waitDriver.until(ExpectedConditions.visibilityOf(cancelOrder)).click();
		handleAlert(false);
		waitDriver.until(ExpectedConditions.invisibilityOfElementLocated(By.id("loadmsg")));
	}

	public void modifyOrder() {

		waitDriver.until(ExpectedConditions.visibilityOf(modifyOrder)).click();
		handleAlert();
		waitDriver.until(ExpectedConditions.invisibilityOfElementLocated(By.id("loadmsg")));
	}

	public String getOrderStatus() {
		return orderStatus.getText();
	}

	public String getTableNumber() {
		return tableNumber.getText();
	}

	public String getOrderNumber() {
		return orderNumber.getText();
	}

	public Order getOrderDetails() {

		Order order = orderNumberDisplay() ? new Order(Integer.parseUnsignedInt(orderNumber.getText())) :
			new Order(0);
		//Order order = new Order(Integer.parseUnsignedInt(orderNumber.getText()));
		if(orderStatusDisplay())
			order.setStatus(OrderStatus.valueOf(orderStatus.getText().toUpperCase()));
		 
		waitDriver.until(ExpectedConditions.presenceOfElementLocated(By.id("tablenum")));
		order.setTable(tableNumber.getText().equals("") ? new Tables() : new Tables(tableNumber.getText()));

		List<OrderLine> lines = new ArrayList<>();

		selectedDishes.forEach(d -> {
			String dishName = d.findElement(By.xpath("./span[@class='dishName']")).getText();
			int dishQty = Integer
					.parseUnsignedInt(d.findElement(By.xpath("./input[@class='dishqty']")).getAttribute("value"));
			lines.add(new OrderLine(0, Dish.getDishFromName(dishName), dishQty));
		});
		order.setOrderLines(lines);
		return order;
	}
	
	public String getOrderMessage() {
		return message.getText();
	}
	
	public void refreshOrder() {
		waitDriver.until(ExpectedConditions.visibilityOf(refresh)).click();
		waitDriver.until(ExpectedConditions.invisibilityOfElementLocated(By.id("loadmsg")));
	}

	public boolean orderStatusDisplay() {
		try {
			orderStatus.isDisplayed();
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	public boolean orderNumberDisplay() {
		try {
			orderNumber.isDisplayed();
		} catch (Exception e) {
			return false;
		}
		return true;
	}
	
	public boolean isModifyButtonEnabled() {
		return modifyOrder.isEnabled();
	}
	
	public boolean isCancelButtonEnabled() {
		return cancelOrder.isEnabled();
	}
	
	public boolean[] retrieveButtonEnabledStatus() {		
		boolean[] enabled = {isModifyButtonEnabled(), isCancelButtonEnabled()};
		return enabled;
	}

	public void selectDish(String dish) {
		this.dishPO = new DishMenuPageObject(dish);
	}

	public String getDishType() {
		return this.dishPO.getDishType();
	}

	public String getDishPrice() {
		return this.dishPO.getDishPrice();
	}

	public void addQuantityToDish(int qty) {
		this.dishPO.addQuantityToDish(qty);
	}

	public void subtractQuantityFromDish(int qty) {
		this.dishPO.subtractQuantityFromDish(qty);
	}
	
	public void subtractQuantityFromDishPreparingState(int qty) {
		
		this.dishPO.subtractQuantityFromDish(1);
		handleAlert();
		waitDriver.until(ExpectedConditions.visibilityOf(tableNumber));
	}

	public void clearDishQuantity() {
		this.dishPO.clearDishQuantity();
	}
	
	public void clearDishQuantityPreparingState() {
		
		this.dishPO.clearDishQuantity();
		handleAlert();
		waitDriver.until(ExpectedConditions.visibilityOf(tableNumber));
	}

	public int getDishQuantity() {
		return this.dishPO.getDishQuantity();
	}

	@Override
	protected void load() {
	}

	@Override
	protected void isLoaded() throws Error {
		try {
			waitDriver.until(ExpectedConditions.invisibilityOfElementLocated(By.id("loadmsg")));
		} catch (Exception e) {
			throw new Error();
		}
		frameSwitcher.setCurrentPage(this);
	}

	@Override
	protected String getFrameId() {
		return "menuDetails";
	}
	

	private class DishMenuPageObject {

		private String dishName;

		private String dishContainer;

		public DishMenuPageObject(String dishName) {
			this.dishName = dishName;
			this.dishContainer = "//span[@class='dishName'][.='" + this.dishName + "']/..";
		}

		public String getDishType() {
			return waitDriver.until(ExpectedConditions
					.visibilityOfElementLocated(By.xpath(dishContainer + "/preceding-sibling::div[@class='dishtype']")))
					.getText();
		}

		public String getDishPrice() {
			return waitDriver
					.until(ExpectedConditions
							.visibilityOfElementLocated(By.xpath(dishContainer + "/span[@class='dishPrice']")))
					.getText();
		}

		public void addQuantityToDish(int qty) {
			WebElement add = waitDriver.until(
					ExpectedConditions.visibilityOfElementLocated(By.xpath(dishContainer + "/input[@value='+']")));
			modifyCount(add, qty);
		}

		public void subtractQuantityFromDish(int qty) {
			WebElement sub = waitDriver.until(
					ExpectedConditions.visibilityOfElementLocated(By.xpath(dishContainer + "/input[@value='-']")));
			modifyCount(sub, qty);
		}

		public void clearDishQuantity() {
			WebElement clear = waitDriver.until(
					ExpectedConditions.visibilityOfElementLocated(By.xpath(dishContainer + "/input[@value='X']")));
			clear.click();
			// Slow things down for display
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		public int getDishQuantity() {
			WebElement qty = waitDriver.until(ExpectedConditions
					.visibilityOfElementLocated(By.xpath(dishContainer + "/input[@class='dishqty']")));
			return Integer.parseUnsignedInt(qty.getAttribute("value"));
		}

		private void modifyCount(WebElement button, int qty) {
			int count = 0;
			while (count < qty) {
				button.click();
				// Slow things down for display
				try {
					Thread.sleep(200);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				count++;
			}
		}
	}
}
