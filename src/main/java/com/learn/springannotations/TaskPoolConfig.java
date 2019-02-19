package com.learn.springannotations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * @ClassName TaskPoolConfig
 * @Description 线程池配置
 * @Author wangxh
 * @Date 2019/1/16 11:34
 * @Version 1.0
 */
@Configuration
@EnableAsync  //开启异步任务的支持
public class TaskPoolConfig {
    @Bean
    public TaskExecutor taskExecutor(){
        ThreadPoolTaskExecutor poolTaskExecutor = new ThreadPoolTaskExecutor();
        poolTaskExecutor.setCorePoolSize(5);
        poolTaskExecutor.setMaxPoolSize(20);
        return poolTaskExecutor;
    }
}
