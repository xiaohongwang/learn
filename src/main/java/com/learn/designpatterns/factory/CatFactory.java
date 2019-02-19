package com.learn.designpatterns.factory;

/**
 * @ClassName CatFactory
 * @Description TODO
 * @Author wangxh
 * @Date 2019/1/24 15:16
 * @Version 1.0
 */
public class CatFactory implements AnimalFactory {
    @Override
    public Animal createAnimal() {
        return new Cat();
    }
}
