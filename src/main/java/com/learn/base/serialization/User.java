package com.learn.base.serialization;

import java.io.Serializable;

public class User implements Serializable {
    private static final long serialVersionUID = 1L;
    private String name;
    private Integer age;
    private transient String address;
    public static int count;
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
        return "name: " + name + " age: " + age + " address: " + address + " count: " + count;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    private void writeObject(java.io.ObjectOutputStream s)
            throws java.io.IOException{
        s.writeObject(address);
    }
    private void readObject(java.io.ObjectInputStream s)
            throws java.io.IOException, ClassNotFoundException {
       address = (String)s.readObject();
    }
}
