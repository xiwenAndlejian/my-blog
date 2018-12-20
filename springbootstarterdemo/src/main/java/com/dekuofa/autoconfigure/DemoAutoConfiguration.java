package com.dekuofa.autoconfigure;

import com.dekuofa.DemoProperties;
import com.dekuofa.service.DemoService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author dekuofa <br>
 * @date 2018-12-19 <br>
 */
@Configuration
@EnableConfigurationProperties(DemoProperties.class)
@ConditionalOnClass(DemoService.class)
public class DemoAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(DemoService.class)
    public DemoService demoService(DemoProperties properties) {
        return new DemoService(properties);
    }
}
