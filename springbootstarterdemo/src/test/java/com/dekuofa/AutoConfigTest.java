package com.dekuofa;

import com.dekuofa.autoconfigure.DemoAutoConfiguration;
import com.dekuofa.service.DemoService;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.FilteredClassLoader;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

/**
 * @author dekuofa <br>
 * @date 2018-12-20 <br>
 */

public class AutoConfigTest {

    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
            .withConfiguration(AutoConfigurations.of(DemoAutoConfiguration.class));

    @Test
    public void testDefaultConfig() {
        this.contextRunner.withUserConfiguration(DemoProperties.class)
                .run((context) -> {
                    // 容器中只存在一个 DemoService 类对应的 bean
                    assertThat(context).hasSingleBean(DemoService.class);
                    // isSameAs 与 == 等价，表示同一个对象
                    assertThat(context.getBean(DemoProperties.class)).isSameAs(
                            context.getBean(DemoService.class).getProperties());

                    DemoProperties properties = context.getBean(DemoProperties.class);
                    assertEquals(DemoProperties.DEFAULT_HOST, properties.getHost());
                    assertEquals(DemoProperties.DEFAULT_PORT, properties.getPort());
                });
    }

    @Test
    public void testCustomizeEnvironment() {
        this.contextRunner
                .withPropertyValues("demo.host=customize")
                .withPropertyValues("demo.port=9000")
                .run((context) -> {
            assertThat(context).hasSingleBean(DemoService.class);
            assertThat(context.getBean(DemoProperties.class).getPort()).isEqualTo(9000);
            assertThat(context.getBean(DemoProperties.class).getHost()).isEqualTo("customize");
        });
    }

    @Test
    public void testIfNotPresent() {
        // 当 DemoService 未被使用时，自动配置应当被禁用
        this.contextRunner.withClassLoader(new FilteredClassLoader(DemoService.class))
                .run((context) -> assertThat(context).doesNotHaveBean("demoService"));
    }
}
