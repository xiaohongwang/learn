package com.learn.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.concurrent.CountDownLatch;

import static org.junit.Assert.*;
@RunWith(SpringRunner.class)
@SpringBootTest
public class LoginControllerTest {

    @Autowired
    WebApplicationContext context;

    private MockMvc mvc;

    @Before
    public void setupMockMvc(){
        //初始化MockMvc对象
        mvc = MockMvcBuilders.webAppContextSetup(context).build();
//        对于会判断用户登陆的接口测试，可在此进行用户注入
    }


    @Test
    public void login() throws Exception {
//        mvc.perform(MockMvcRequestBuilders.post("/login")
//                .contentType(MediaType.APPLICATION_JSON_UTF8)
//                .content("1".getBytes()).header("num", 1).sessionAttr("user",1))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andDo(MockMvcResultHandlers.print());

        CountDownLatch countDownLatch = new CountDownLatch(100);
        for (int i = 0; i < 100; i++){
            Task task = new Task(i,"/login", countDownLatch, mvc);
            new Thread(task).start();
        }
//       在此阻塞，知道计数减为0
        countDownLatch.await();
    }


}