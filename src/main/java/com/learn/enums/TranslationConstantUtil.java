package com.learn.enums;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @ClassName TranslationConstantUtil
 * @Description  翻译机制   利用  枚举 维护系统中常量数据 前端不进行维护，在代码中进行code值翻译
 * 缺点:    1、不能动态维护常量数据
 *          2、常量改动，需重新进行发版
 * @Author wangxh
 * @Date 2019/1/18 15:18
 * @Version 1.0
 */
public class TranslationConstantUtil {
    /**
     *  将数据中的 value 值翻译为对应的中文表达
     * @param sourceData  原始数据信息
     * @param key  map中的key
     * @param newKey  添加数据key值
     * @param enumName 常量类型
     * @return
     */
    public static List<Map> translationType(List<Map> sourceData, final String key, final String newKey, String enumName){
        try {
            BaseEnum[] enums =  TypeMappingEnum.getValues(enumName);
            sourceData.stream().map(item -> {
                for (BaseEnum baseEnum : enums) {
                    if (baseEnum.getCode().equals(item.get(key).toString())){
                        item.put(newKey, baseEnum.getMsg());
                        break;
                    }
                }
                return item;
            }).collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sourceData;
    }

    /**
     * 根据常量类型enumName，获取常量所有 code，msg值
     * @param enumName
     * @return
     */
    public static List<Map<String,Object>> getEnums(String enumName){
        try {
            BaseEnum[] enums =  TypeMappingEnum.getValues(enumName);
            return  Arrays.stream(enums).map(item -> {
                Map<String, Object> data = new HashMap<>();
                data.put("value", item.getCode());
                data.put("name", item.getMsg());
                return  data;
            }).collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
