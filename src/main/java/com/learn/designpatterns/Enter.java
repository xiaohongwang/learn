package com.learn.designpatterns;

import com.learn.designpatterns.factory.AnimalFactory;
import com.learn.designpatterns.factory.CatFactory;
import com.learn.designpatterns.factory.DogFactory;
import com.learn.designpatterns.simple.FruitFactory;

/**
 * @ClassName Enter
 * @Description 入口
 * @Author wangxh
 * @Date 2019/1/24 15:08
 * @Version 1.0
 */
public class Enter {
    public static void main(String[] args) {
        FruitFactory.createFruit("apple").whatIm();
        FruitFactory.createFruit("pear").whatIm();

        DogFactory dogFactory = new  DogFactory();
        CatFactory catFactory = new CatFactory();
        dogFactory.createAnimal().whatIm();
        catFactory.createAnimal().whatIm();
    }
}
