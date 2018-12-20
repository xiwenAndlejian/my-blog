package com.dekuofa.service;

import com.dekuofa.DemoProperties;
import lombok.Getter;
import lombok.Setter;

/**
 * @author dekuofa <br>
 * @date 2018-12-19 <br>
 */
@Getter
@Setter
public class DemoService {

    private DemoProperties properties;

    public DemoService(DemoProperties properties) {
        this.properties = properties;
    }

    public String doSomething() {
        return "doSomething with properties:" + properties.toString();
    }

}
