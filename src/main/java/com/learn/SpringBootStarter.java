package com.learn;

import com.learn.utils.PropertiesUtil;
import org.junit.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by xiaohong on 2017/11/8.
 */
@SpringBootApplication
@RestController
public class SpringBootStarter {
    public static void main(String[] args){
        SpringApplication.run(SpringBootStarter.class,args);
    }

    @RequestMapping(value = "/hello")
    public String hello(){
        return PropertiesUtil.findPropertiesKey("user");
    }

}
