package com.learn.designpatterns.factory;

/**
 * @ClassName Dog
 * @Description TODO
 * @Author wangxh
 * @Date 2019/1/24 15:14
 * @Version 1.0
 */
public class Dog implements Animal {
    @Override
    public void whatIm() {
        System.out.println("Dog");
    }
}
