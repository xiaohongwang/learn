package com.learn.designpatterns.abstractfactory;

/**
 * @ClassName XiaomiFactoryImpl
 * @Description
 * @Author wangxh
 * @Date 2019/1/24 15:28
 * @Version 1.0
 */
public class XiaomiFactoryImpl implements PhoneFactory {
    @Override
    public Cpu getCpu() {
        return new  Cpu.Cpu625();
    }

    @Override
    public Green getGreen() {
        return new Green.Green5();
    }
}
