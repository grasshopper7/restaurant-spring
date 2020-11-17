package ristorante.pages;

import static io.cucumber.spring.CucumberTestContext.SCOPE_CUCUMBER_GLUE;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Aspect
@Scope(SCOPE_CUCUMBER_GLUE)
public class SwitchPageFrameAspect {

	@Autowired
	private PageFrameSwitcher frameSwitcher;

	@Before("target(ristorante.pages.SwitchScreen) && "
			+ "(execution(public * ristorante.pages.TableListPageObject.*(..)) || "
			+ "execution(public * ristorante.pages.OrderPageObject.*(..)) || "
			+ "execution(public * ristorante.pages.KitchenPageObject.*(..)) || "
			+ "execution(public * ristorante.pages.ServerPageObject.*(..)) || "
			+ "execution(public * ristorante.pages.SearchOrdersPageObject.*(..)))")
	public void managePageFrameAdvice(JoinPoint jp) {
		//System.out.println(jp.getSignature().getName());
		BasePageObject<?> pageObject = (BasePageObject<?>) jp.getTarget();
		frameSwitcher.switchScreen(pageObject);
	}
}
