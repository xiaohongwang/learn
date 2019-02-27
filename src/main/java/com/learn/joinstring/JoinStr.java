package com.learn.joinstring;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;
import java.util.stream.Collectors;

/**
 * @ClassName JoinStr
 * @Description 字符串拼接
 * @Author wangxh
 * @Date 2019/1/16 17:23
 * @Version 1.0
 */
public class JoinStr {

    private List<String> list = new ArrayList<>();

    public void joinStr1(){
        String name = "xiaohong";
        String fullInfo = "name:" + name;
    }

    @Test
    public void joinStr2(){
        String name = "name:";
        String fullInfo = name.concat("xiaohong");
    }

    public void joinStr3(){
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("name:").append("xiaohong");
    }
    @Test
    public void joinStr4(){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("name:").append("xiaohong");
        String data  = null;
        stringBuilder.append(data);
        System.out.println(stringBuilder.toString());
    }
    @Test
    public void joinStr5(){
        StringUtils.join("name",":", "xiaohong");
        list.add("1");
        list.add("2");
        System.out.println(StringUtils.join(list));//[1, 2]
    }

    //jdk8 中提供StringJoiner 进行字符串拼接

    @Test
    public void joinStr6(){

        StringJoiner stringJoiner = new StringJoiner(",","[","]");

        stringJoiner.add("2").add("3");
        System.out.println(stringJoiner.toString());

        for (int i = 0; i < 5; i++){
            list.add(String.valueOf(i));
        }

        String result = list.stream().collect(Collectors.joining(" && ","[","]"));

        System.out.println(result);
    }


}
