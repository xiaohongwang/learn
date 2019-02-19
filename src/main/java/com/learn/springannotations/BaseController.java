package com.learn.springannotations;

import org.codehaus.jackson.map.Serializers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @ClassName BaseController
 * @Description 预定义controller层用对象
 * @Author wangxh
 * @Date 2019/1/14 10:08
 * @Version 1.0
 */
public class BaseController {

    private static final Logger logger = LoggerFactory.getLogger(Serializers.Base.class);

    @Resource
    protected HttpServletRequest request;

//    @ModelAttribute
//    private void setRequest(HttpServletRequest request){
//        this.request = request;
//    }

    private static final ThreadLocal<Integer> localUser = new ThreadLocal<>();
    @ModelAttribute
    private void setUser(){
        logger.info("获取用户信息");
        localUser.set(request.getSession() == null ? null : (Integer) request.getSession().getAttribute("user"));
//        user = request.getSession() == null ? null : (Integer) request.getSession().getAttribute("user");
    }
    protected Integer getUser(){
        return localUser.get();
    }

    protected Integer data;

    @ModelAttribute("data")
    public Integer setData(){
        data = 1;
        return data;
    }

}
