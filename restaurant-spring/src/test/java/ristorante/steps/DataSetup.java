package ristorante.steps;

import java.util.ArrayList;

import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;

import io.cucumber.core.api.Scenario;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import ristorante.Config;
import ristorante.entity.Order;
import ristorante.entity.Tables;
import ristorante.pages.PageFrameSwitcher;
import ristorante.pages.ScenarioData;

//@DirtiesContext
@ContextConfiguration(classes = { Config.class })
public class DataSetup {

	@Autowired
	private ScenarioData data;
	
	@Autowired
	private PageFrameSwitcher frameSwitcher;
	
	@Autowired
	@Qualifier("mainDriver")
	public WebDriver driver;


	@Before
	public void setup(Scenario scenario) {

		String tableno = scenario.getSourceTagNames().stream().filter(t -> t.startsWith("@Table")).findFirst()
				.orElse("");

		Order order = new Order(0);
		order.setOrderLines(new ArrayList<>());
		data.setInitialOrder(order);

		if (!tableno.isEmpty()) {
			data.setTableNo(tableno.substring(1));
			order.setTable(new Tables(Integer.parseInt(tableno.substring(6))));
		}
	}

	@After
	public void teardown() {
		
		closeOtherTabs(frameSwitcher.getKitchenWindowHandle());
		closeOtherTabs(frameSwitcher.getServerWindowHandle());
		driver.switchTo().window(frameSwitcher.getHomeWindowHandle());
	}

	private boolean closeOtherTabs(String handle) {
		if(handle == null || handle.isEmpty())
			return true;
		try {
			driver.switchTo().window(handle).close();
		} catch (Exception e) {
			return false;
		}
		return true;
	}
}
