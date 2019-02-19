package com.learn.designpatterns.simple;

import com.learn.designpatterns.simple.Fruit;

/**
 * @ClassName Pear
 * @Description 水果 梨
 * @Author wangxh
 * @Date 2019/1/24 15:04
 * @Version 1.0
 */
public class Pear implements Fruit {
    @Override
    public void whatIm() {
        System.out.println("Pear");
    }
}
