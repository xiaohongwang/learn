package com.learn.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Properties;

/**
 * @ClassName PropertiesUtil
 * @Description 读取属性文件
 * @Author wangxh
 * @Date 2019/1/18 11:18
 * @Version 1.0
 */
public class PropertiesUtil {
    private static Logger LOGGER = LoggerFactory.getLogger(PropertiesUtil.class);
    private static String configPath = "/conf/config.properties";
//    static {
//        String savePath = PropertiesUtil.class.getResource(configPath).getPath();
//        //以下方法读取属性文件会缓存问题
////		InputStream in = PropertiesUtils.class
////				.getResourceAsStream("/config.properties");
//        InputStream in = null;
//        try {
//            in = new BufferedInputStream(new FileInputStream(savePath));
//            prop.load(new InputStreamReader(in, "utf-8"));
//        } catch (Exception e) {
//            prop = null;
//            LOGGER.error("error");
//        }finally{
//            try {
//                in.close();
//            } catch (IOException e) {
//                LOGGER.error("error");
//            }
//        }
//    }

    /**
     * Class.getResource(String path)
     * 1、path不以'/'开头时，默认是此类所在的包下取资源
     * 2、path以'/'开头时，则是从项目的ClassPath根下获取资源
     * Class.getClassLoader().getResource(String path)
     * 1、path不能以'/'开头，path是指类加载器的加载范围
     * Class.getResourceAsStream(String path)
     * 1、path不以'/'开头时，默认是指所在类的相对路径，从这个相对路径下取资源
     * 2、path以'/'开头时，则是从项目的ClassPath根下获取资源，就是要写相对于classpath根下的绝对路径
     * Class.getClassLoader.getResourceAsStream(String path)
     * 1、path不能以'/'开头，path是指类加载器的加载范围
     * @return
     */
    private static Properties getProperties(){
        Properties prop = new Properties();
        String savePath = PropertiesUtil.class.getResource("/conf/config.properties").getPath();
        System.out.println(savePath);
        //以下方法读取属性文件会缓存问题   ---  未验证成功
//       InputStream in = PropertiesUtil.class
//                .getResourceAsStream(configPath);
        InputStream in = null;
        try {
            in = new BufferedInputStream(new FileInputStream(savePath));
            prop.load(new InputStreamReader(in, "utf-8"));
        } catch (Exception e) {
            prop = null;
            LOGGER.error("error");
        }finally{
            try {
                in.close();
            } catch (IOException e) {
                LOGGER.error("error");
            }
        }
        return prop;
    }

    public static String findPropertiesKey(String key){
        Properties prop = getProperties();
        return prop.getProperty(key);
    }

    public static void main(String[] args) {
        getProperties();
    }
}
