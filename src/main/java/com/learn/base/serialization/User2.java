package com.learn.base.serialization;

import java.io.*;
import java.util.StringJoiner;

public class User2 implements Serializable {
    private static final long serialVersionUID = -6071085924061402257L;
    //    stream classdesc serialVersionUID = -3900642229436836058
//    serialVersionUID = -6782963171485699053
    private String name;
    private Integer age;
    private String address;
    private Integer count;

    public User2(String name, Integer age){
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    @Override
    public String toString() {
        StringJoiner joiner = new StringJoiner(";", "{","}");
        joiner.add(name).add(String.valueOf(age)).add(address);
        return joiner.toString();
//        return name + " : " + age;
    }

    public String getAddress() {
        return address;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}
