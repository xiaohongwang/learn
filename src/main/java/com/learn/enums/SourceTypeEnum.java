package com.learn.enums;

/**
 * @ClassName SourceTypeEnum
 * @Description 设备类型枚举
 * @Author wangxh
 * @Date 2019/1/18 15:37
 * @Version 1.0
 */
public enum  SourceTypeEnum implements BaseEnum{
    CAMERA("1000","摄像"),
    ENTRANCE_GUARD("2000","门禁");
    public String code;
    public String msg;

    SourceTypeEnum(String code, String msg){
        this.code = code;
        this.msg = msg;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getMsg() {
        return msg;
    }
}
