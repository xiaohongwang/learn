package com.learn.base.serialization;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class SerializableDemo1 {
    public static void main(String[] args) {
//        User user = new User();
//        user.setName("xiaohong");
//        user.setAge(23);
//        User.count = 1;
//        user.setAddress("北京");
//        System.out.println(user);

//        User1 user1 = new User1();
//        user1.setName("xiaohong");
//        user1.setAge(24);
//
//        ArrayList list = new ArrayList();
//        list.add(1);
//        list.add(2);


//        User2 user = new User2("小红",20);
//        try(FileOutputStream fos = new FileOutputStream("/Users/wangxiaohong/tmp/User.txt");
//            ObjectOutputStream oos = new ObjectOutputStream(fos)){
//            oos.writeObject(user);
//            System.out.println(user);
//        }catch (Exception e){
//            e.printStackTrace();
//        }

        User.count = 2;
        /**
         * Java 7中的新特性try-with-resources。
         * 这其实是Java中的一个语法糖，背后原理其实是编译器帮我们做了关闭IO流的工作
         */
        try(ObjectInputStream ois = new ObjectInputStream(new
                FileInputStream("/Users/wangxiaohong/tmp/User.txt"))){
            User2 list1 = (User2)ois.readObject();
            System.out.println(list1);
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
