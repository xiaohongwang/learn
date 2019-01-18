package com.learn.joinstring;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

/**
 * @ClassName JoinStr
 * @Description 字符串拼接
 * @Author wangxh
 * @Date 2019/1/16 17:23
 * @Version 1.0
 */
public class JoinStr {
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

    public void joinStr5(){
        StringUtils.join("name",":", "xiaohong");
    }
}
