package com.learn.base.objectmapper;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

/**
 * ObjectMapper 是Jackson库的主要类
 */
public class ObjectMapperLearn {

    public static void main(String[] args) {
        ObjectMapper mapper = new ObjectMapper();
        // 不强制匹配实体类和json串的属性
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        String jsonString = "{\"name\":\"Mahesh\", \"age\":21,\"address\":\"北京\"}";

        //map json to student
        try {
            Student student = mapper.readValue(jsonString, Student.class);
            System.out.println(mapper.writeValueAsString(student));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
