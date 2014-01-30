package sample;

import sample.web.BarRequest;
import sample.web.FooSession;
import sample.web.NormalBean;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.Scope;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.context.support.SimpleThreadScope;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.*;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Mariusz Smykula
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = MainControllerTest.TestConfig.class)
@TestExecutionListeners({MainControllerTest.WebContextTestExecutionListener.class,
        DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class})
public class MainControllerTest {

    @Autowired
    NormalBean normalBean;

    @Autowired
    FooSession fooSession;

    @Autowired
    BarRequest barRequest;

    @Test
    public void testName() throws Exception {

        assertThat(normalBean).isNotNull();
        assertThat(fooSession).isNotNull();
        assertThat(barRequest).isNotNull();
        assertThat(barRequest.random()).isNotNull();

    }

    public static class WebContextTestExecutionListener extends AbstractTestExecutionListener {
        @Override
        public void prepareTestInstance(TestContext testContext) throws Exception {

            if (testContext.getApplicationContext() instanceof GenericApplicationContext) {
                GenericApplicationContext context = (GenericApplicationContext) testContext.getApplicationContext();
                ConfigurableListableBeanFactory beanFactory = context.getBeanFactory();
                Scope requestScope = new SimpleThreadScope();
                beanFactory.registerScope("request", requestScope);
                Scope sessionScope = new SimpleThreadScope();
                beanFactory.registerScope("session", sessionScope);
            }
        }
    }

    @Configuration
    @ComponentScan("sample.web")
    public static class TestConfig {

    }


}