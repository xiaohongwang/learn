package com.learn.designpatterns.factory;

/**
 * @ClassName Cat
 * @Description TODO
 * @Author wangxh
 * @Date 2019/1/24 15:14
 * @Version 1.0
 */
public class Cat implements Animal {
    @Override
    public void whatIm() {
        System.out.println("cat");
    }
}
