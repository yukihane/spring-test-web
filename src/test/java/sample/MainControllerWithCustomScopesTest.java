package sample;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.CustomScopeConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.context.WebApplicationContext;
import sample.web.Foo;
import sample.web.MyPrototypeBean;
import sample.web.MyRequestBean;
import sample.web.MySessionBean;

import java.util.HashMap;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Mariusz Smykula
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = MainControllerWithCustomScopesTest.TestConfig.class)
public class MainControllerWithCustomScopesTest {

    @Autowired
    MyPrototypeBean myPrototypeBean;

    @Autowired
    MySessionBean mySessionBean;

    @Autowired
    MyRequestBean myRequestBean;

    @Autowired
    Foo foo;

    @Test
    public void shouldAutowireBeans() throws Exception {

        assertThat(myPrototypeBean).isNotNull();
        assertThat(mySessionBean).isNotNull();
        assertThat(myRequestBean).isNotNull();
        assertThat(foo).isNotNull();

    }

    @Configuration
    @ComponentScan("sample.web")
    public static class TestConfig {
        @Bean
        public static CustomScopeConfigurer customScopeConfigurer(){
            CustomScopeConfigurer scopeConfigurer = new CustomScopeConfigurer();

            HashMap<String, Object> scopes = new HashMap<String, Object>();
            scopes.put(WebApplicationContext.SCOPE_REQUEST, new CustomScope());
            scopes.put(WebApplicationContext.SCOPE_SESSION, new CustomScope());
            scopeConfigurer.setScopes(scopes);

            return scopeConfigurer;
        }
    }


}
