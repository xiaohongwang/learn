package com.learn.springmvcannotations;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName LogginController
 * @Description 系统登陆
 * @Author wangxh
 * @Date 2019/1/14 10:11
 * @Version 1.0
 */
@RestController
public class LoginController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String login(){
//       logger.info("login === request的头数据 {} ，用户数据信息{}", request.getHeader("num"), user);
        logger.info("login === request的头数据 {} ，用户数据信息{}", request.getHeader("num"), getUser());
       return "login success";
    }

    @RequestMapping("/loginout")
//    public String loginOut(Model model){
    public String loginOut(@ModelAttribute("data") Integer data){
        logger.info("loginout === {}",data);
        return "loginout success";
    }
}
