package com.learn.enums;

/**
 * @ClassName CycleSysCode
 * @Description switch enum
 * @Author wangxh
 * @Date 2019/1/18 15:11
 * @Version 1.0
 */
public class CycleSysCode {
    public static void main(String[] args) {
        SysCodeEnum code = SysCodeEnum.SUCCESS;
        switch (code){
            case FAIL:
                System.out.println("fail"); break;
            case ERROR:
                System.out.println("errot");break;
            case SUCCESS:
                System.out.println("success"); break;
                default:
                    System.out.println("end");
        }
    }
}
