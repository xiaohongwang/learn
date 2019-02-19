package com.learn.springannotations;

import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @ClassName PostConstruct
 * @Description @PostConstruct
 * @Author wangxh
 * @Date 2019/2/19 16:14
 * @Version 1.0
 */
@Component
public class PostConstructController {

    public static Integer count ;

    public PostConstructController(){
        System.out.println("==创建PostConstructController对象===");
    }

    @PostConstruct
    public void init(){
        count = 12;
        System.out.println("==调用PostConstructController的init方法==");
    }


//      ===创建LoginController对象==
//      ==创建PostConstructController对象===
//      ==调用PostConstructController的init方法==
//      ==调用LoginController的init方法== 12
}
