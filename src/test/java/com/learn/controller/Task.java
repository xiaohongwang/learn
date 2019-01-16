package com.learn.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.concurrent.CountDownLatch;

/**
 * @ClassName Task
 * @Description 访问请求
 * @Author wangxh
 * @Date 2019/1/16 11:01
 * @Version 1.0
 */
public class Task implements Runnable{

    private static final Logger logger = LoggerFactory.getLogger(Task.class);

    private CountDownLatch latch;
    private String url;
    private Integer num;
    private MockMvc mvc;

    public Task(Integer num , String url, CountDownLatch latch, MockMvc mvc){
        this.num = num;
        this.latch = latch;
        this.url = url;
        this.mvc = mvc;
    }

    @Override
    public void run() {
        String jsonData = "{\"num\":\"" + num +  "\"}";
        try {
            logger.info(jsonData);
            mvc.perform(MockMvcRequestBuilders.post(url)
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .content(jsonData.getBytes()).header("num", num).sessionAttr("user",num))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andDo(MockMvcResultHandlers.print());
        } catch (Exception e) {
            e.printStackTrace();
        }
        latch.countDown();
    }
}
