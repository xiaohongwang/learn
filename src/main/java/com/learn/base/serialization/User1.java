package com.learn.base.serialization;

import java.io.*;

public class User1 implements Externalizable {

    private String name;
    private Integer age;
    //类继承Externalizable，实现序列化，需要有无参构造函数  在反序列化时，会调用类得无参构造函数创建一个新的对象，在进行参数填充
    public User1(){}

    public User1(String name, Integer age){
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
        return "name: " + name + " age: " + age;
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeObject(name);
        out.writeInt(age);
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        name = (String)in.readObject();
        age = in.readInt();
    }


}
