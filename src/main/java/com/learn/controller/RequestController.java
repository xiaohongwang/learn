package com.learn.controller;

import org.codehaus.jackson.map.Serializers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @ClassName RequestController
 * @Description 验证  request 线程安全问题
 * @Author wangxh
 * @Date 2019/1/15 14:36
 * @Version 1.0
 */
@RestController
public class RequestController extends BaseController{

    private static final Logger logger = LoggerFactory.getLogger(RequestController.class);

    @RequestMapping(value = "/first", method = RequestMethod.POST)
    public String testA(@RequestBody String jsonData,HttpServletRequest request){
        logger.info(request.hashCode() + " === " + jsonData + "  header数据" + " === " + request.getHeader("num") );
        return "first";
    }

    @RequestMapping(value = "/second", method = RequestMethod.POST)
    public String testB(@RequestBody String jsonData){
        logger.info(request.hashCode() + " == " + jsonData + "  header数据" + " == " + request.getHeader("num"));
        return "second";
    }

}
