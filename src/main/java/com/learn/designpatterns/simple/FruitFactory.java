package com.learn.designpatterns.simple;

/**
 * @ClassName FruitFactory
 * @Description 水果工厂类
 * @Author wangxh
 * @Date 2019/1/24 15:05
 * @Version 1.0
 */
public class FruitFactory {
    public static Fruit createFruit(String type){
        switch (type){
            case "apple": return new Apple();
            case "pear": return new Pear();
            default: return null;
        }
    }
}
