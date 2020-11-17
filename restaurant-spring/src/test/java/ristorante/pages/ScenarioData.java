package ristorante.pages;

import static io.cucumber.spring.CucumberTestContext.SCOPE_CUCUMBER_GLUE;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import ristorante.entity.Order;

@Component
@Scope(SCOPE_CUCUMBER_GLUE)
public class ScenarioData {

	private String tableNo;

	private String orderNo;

	@Autowired
	private Order initialOrder;

	public String getTableNo() {
		return tableNo;
	}

	public void setTableNo(String tableNo) {
		this.tableNo = tableNo;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public Order getInitialOrder() {
		return initialOrder;
	}

	public void setInitialOrder(Order initialOrder) {
		this.initialOrder = initialOrder;
	}

	@Override
	public String toString() {
		return "ScenarioData [tableNo=" + tableNo + ", orderNo=" + orderNo + ", initialOrder=" + initialOrder + "]";
	}
}
