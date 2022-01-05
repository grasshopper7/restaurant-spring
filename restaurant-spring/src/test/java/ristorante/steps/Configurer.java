package ristorante.steps;

import java.lang.reflect.Type;
import java.util.Map;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import io.cucumber.java.DataTableType;
import io.cucumber.java.DefaultDataTableCellTransformer;
import io.cucumber.java.DefaultDataTableEntryTransformer;
import io.cucumber.java.DefaultParameterTransformer;
import io.cucumber.java.ParameterType;
import ristorante.entity.Dish;
import ristorante.entity.Order.OrderStatus;
import ristorante.entity.OrderLine;
import ristorante.pages.AltBrowserPageObject.BrowserType;

public class Configurer {

	private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

	@ParameterType(".*?")
	public OrderStatus orderStatus(String status) {
		return OrderStatus.valueOf(status.toUpperCase());
	}

	@ParameterType(".*?")
	public BrowserType browser(String browser) {
		return BrowserType.valueOf(browser.toUpperCase());
	}

	@DataTableType
	public OrderLine getOrderLine(Map<String, String> entry) {
		Dish dish = Dish.getDishFromName(entry.get("dish"));
		return new OrderLine(0, dish, Integer.parseInt((entry.get("qty"))));
	}

	@DataTableType
	public Dish getDish(String entry) {
		return Dish.getDishFromName(entry);
	}

	@DefaultParameterTransformer
	@DefaultDataTableEntryTransformer
	@DefaultDataTableCellTransformer
	public Object defaultTransformer(Object fromValue, Type toValueType) {
		JavaType javaType = objectMapper.constructType(toValueType);
		return objectMapper.convertValue(fromValue, javaType);
	}

	/*
	 * @Override public void configureTypeRegistry(TypeRegistry registry) {
	 * 
	 * JacksonTableTransformer jacksonTableTransformer = new
	 * JacksonTableTransformer();
	 * registry.setDefaultParameterTransformer(jacksonTableTransformer);
	 * registry.setDefaultDataTableEntryTransformer(jacksonTableTransformer);
	 * registry.setDefaultDataTableCellTransformer(jacksonTableTransformer);
	 * 
	 * registry.defineParameterType(new ParameterType<>("status", ".*?",
	 * OrderStatus.class, (String s) -> OrderStatus.valueOf(s.toUpperCase())));
	 * 
	 * registry.defineParameterType(new ParameterType<>("browser", ".*?",
	 * BrowserType.class, (String d) -> BrowserType.valueOf(d.toUpperCase())));
	 * 
	 * registry.defineDataTableType(new DataTableType(OrderLine.class, new
	 * TableEntryTransformer<OrderLine>() {
	 * 
	 * @Override public OrderLine transform(Map<String, String> entry) { return new
	 * OrderLine(0, Dish.getDishFromName(entry.get("dish")),
	 * Integer.parseInt(entry.get("qty"))); } }));
	 * 
	 * registry.defineDataTableType(new DataTableType(Dish.class, new
	 * TableCellTransformer<Dish>() {
	 * 
	 * @Override public Dish transform(String cell) throws Throwable { return
	 * Dish.getDishFromName(cell); } })); }
	 * 
	 * @Override public Locale locale() { return Locale.ENGLISH; }
	 * 
	 * private static final class JacksonTableTransformer implements
	 * ParameterByTypeTransformer, TableEntryByTypeTransformer,
	 * TableCellByTypeTransformer {
	 * 
	 * private final ObjectMapper objectMapper = new ObjectMapper();
	 * 
	 * @Override public Object transform(String s, Type type) { return
	 * objectMapper.convertValue(s, objectMapper.constructType(type)); }
	 * 
	 * @Override public <T> T transform(Map<String, String> entry, Class<T> type,
	 * TableCellByTypeTransformer cellTransformer) { return
	 * objectMapper.convertValue(entry, type); }
	 * 
	 * @Override public <T> T transform(String value, Class<T> cellType) { return
	 * objectMapper.convertValue(value, cellType); } }
	 */
}
