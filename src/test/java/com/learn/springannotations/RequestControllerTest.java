package com.learn.springannotations;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.concurrent.CountDownLatch;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RequestControllerTest {

    private static final Logger logger = LoggerFactory.getLogger(RequestControllerTest.class);

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mvc;

    private MockHttpSession session;
    @Before
    public void setupMockMvc(){
        //初始化MockMvc对象
        mvc = MockMvcBuilders.webAppContextSetup(wac).build();
//        对于会判断用户登陆的接口测试，可在此进行用户注入
    }


    @Test
    public void testA(){
        try {
            for (int i = 0; i < 100; i++){
                new  Thread(){
                    @Override
                    public void run() {
                        try {
                            mvc.perform(MockMvcRequestBuilders.post("/first"))
                                    .andExpect(MockMvcResultMatchers.status().isOk())
                                    .andDo(MockMvcResultHandlers.print());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }.start();

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 线程协作  保证主线在所有请求后在执行
     * 采用testA 方法中方式进行测试，导致在某些线程请求未结束时，主线程已结束
     * Closing org.springframework.web.context.support.GenericWebApplicationContext@7161d8d1: startup date [Wed Jan 16 10:09:27 CST 2019]; root of context hierarchy
     * 过早关闭  Spring WebApplicationContext
     */
    @Test
    public void testB(){
        try {
            CountDownLatch countDownLatch = new CountDownLatch(100);
            for (int i = 0; i < 100; i++){
                Task task = new Task(i,"/second" ,countDownLatch, mvc);
                new Thread(task).start();
            }
            //       在此阻塞，知道计数减为0
            countDownLatch.await();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Test
    public void testC(){
        try {
            CountDownLatch countDownLatch = new CountDownLatch(100);
            for (int i = 0; i < 100; i++){
                Task task = new Task(i,"/first" ,countDownLatch, mvc);
                new Thread(task).start();
            }
            //       在此阻塞，知道计数减为0
            countDownLatch.await();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}