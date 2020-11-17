package ristorante;

import static io.cucumber.spring.CucumberTestContext.SCOPE_CUCUMBER_GLUE;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.opera.OperaDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;

import io.github.bonigarcia.wdm.WebDriverManager;
import ristorante.entity.Order;

@Configuration
@ComponentScan({ "ristorante.pages" })
@EnableAspectJAutoProxy
public class Config {

	@Bean(destroyMethod = "quit")
	@Qualifier("mainDriver")
	public WebDriver getMainDriver() {
		WebDriverManager.chromedriver().setup();
		WebDriver driver = new ChromeDriver();
		driver.manage().window().maximize();
		driver.get("http://localhost:8080/");
		return driver;
	}

	@Bean
	public WebDriverWait getWaitDriver() {
		return new WebDriverWait(getMainDriver(), 10, 250);
	}

	@Bean
	@Scope(SCOPE_CUCUMBER_GLUE)
	public Order getOrder() {
		return new Order();
	}

	@Bean(destroyMethod = "quit")
	@Qualifier("similarDriver")
	@Lazy
	public WebDriver getSimilarDriver() {
		WebDriverManager.chromedriver().setup();
		WebDriver driver = new ChromeDriver();
		driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
		driver.manage().window().maximize();
		driver.get("http://localhost:8080/");
		return driver;
	}

	@Bean(destroyMethod = "quit")
	@Qualifier("differentDriver")
	@Lazy
	public WebDriver getDifferentDriver() {

		WebDriverManager.operadriver().setup();
		WebDriver driver = new OperaDriver();
		driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
		driver.manage().window().maximize();
		driver.get("http://localhost:8080/");
		return driver;
	}
}
