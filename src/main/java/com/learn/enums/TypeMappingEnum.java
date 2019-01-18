package com.learn.enums;

/**
 * @ClassName TypeMappingEnum
 * @Description 类型匹配枚举
 * @Author wangxh
 * @Date 2019/1/18 15:43
 * @Version 1.0
 */
public enum  TypeMappingEnum {
    ACCOUNT_RECORD_TYPE("sourceype", SourceTypeEnum.values());
    public String enumName;
    public BaseEnum[] enums;

    TypeMappingEnum (String enumName,  BaseEnum[] enums) {
        this.enumName = enumName;
        this.enums = enums;
    }

    public static BaseEnum[] getValues(final String enumName){
        TypeMappingEnum[] temp = TypeMappingEnum.values();
        for (TypeMappingEnum e : temp) {
            if (enumName.equals(e.enumName)) {
                return e.enums;
            }
        }
        return null;
    }
}
