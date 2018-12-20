package com.dekuofa;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * @author dekuofa <br>
 * @date 2018-12-19 <br>
 */
@Data
@Primary
@Configuration
@ConfigurationProperties(prefix = "demo")
public class DemoProperties {

    public static final String DEFAULT_HOST = "localhost";
    public static final int    DEFAULT_PORT = 8000;

    private String host;
    private int    port;

    public DemoProperties() {
        this.host = DEFAULT_HOST;
        this.port = DEFAULT_PORT;
    }
}
