package com.learn.utils;

import java.util.concurrent.CountDownLatch;

/**
 * @ClassName BuildIdUtil
 * @Description 生成分布式系统唯一ID  https://www.cnblogs.com/haoxinyue/p/5208136.html (snowflake)  https://blog.csdn.net/hl_java/article/details/78462283
 * @Author wangxh
 * @Date 2019/1/16 16:20
 * @Version 1.0
 */
public class BuildIdUtil {
    private static StringBuilder builder = new StringBuilder();

    private static long currentTime = 0L;
    private static long beforeTime = 0L;
    private static Integer serialNum = -1;
    /**
     * 当string长度大于或等于指定的长度，则取前指定长度位；
     * 若string后的长度小于指定长度则在string后的字符串前补字符串0，直至指定位数
     * @param data 源数据
     * @param length 数据长度
     * @return
     */
    public static String completeDigits(String data, Integer length){
       if (data == null){
           data = "";
       }
        StringBuilder stringBuilder = new StringBuilder(length);
       int appendLength = length - data.length();
       if (appendLength < 0){
           return data.substring(0, length);
       }

       while (stringBuilder.length() < appendLength){
           stringBuilder.append("0");
       }
       stringBuilder.append(data);
       return stringBuilder.toString();
    }

    /**
     * 系统中ES数据的ID规则可参考：“AABBBBBBBBBBBBBCCCDDDDD”格式，其中A表示数据来源分类、B表示13位毫秒级时间戳、C表示Spark分块或Java线程序号（从0开始计数）、D表示序号（从0开始计数）。
     * AA：00=批量导入、01=人工录入、10=AP-MAC、11=MAC、12=IMSI、13=Telephone、14=Car、15=Face、18=门禁……。
     */
    public synchronized static String buildId(String sourceType, Integer threadId){
        builder.setLength(0);
        builder.append(completeDigits(sourceType, 2));
        builder.append(currentTime = System.currentTimeMillis());
        builder.append(completeDigits( String.valueOf(threadId), 3));
        serialNum = beforeTime == currentTime ? serialNum : 0;
        System.out.println(beforeTime == currentTime);
        beforeTime = currentTime;
        builder.append(completeDigits(String.valueOf(serialNum ++), 5));
        System.out.println(threadId +  " serialNum :" + serialNum);
        System.out.println(builder.toString());
        return builder.toString();
    }


    public static void main(String[] args) throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(100);

        for (int i = 0; i < 100; i++){
            BuildTask buildTask = new BuildTask("15", i, latch);
            new Thread(buildTask).start();
        }
        latch.await();

//        CountDownLatch latch = new CountDownLatch(300);
//        for (int i = 0; i < 300; i++){
//            String data = String.valueOf(i);
//            Task task = new Task(data, 5, latch);
//            new Thread(task).start();
//        }
//        latch.await();
    }


    static class BuildTask implements Runnable{
        private CountDownLatch latch;
        private String sourceType;
        private Integer threadId;

        public BuildTask(String sourceType, Integer threadId, CountDownLatch latch){
            this.sourceType = sourceType;
            this.threadId = threadId;
            this.latch = latch;
        }

        @Override
        public void run() {
            buildId(sourceType,threadId);
            latch.countDown();
        }
    }


    static class Task implements Runnable{

        private CountDownLatch latch;
        private String data;
        private Integer length;

        public Task(String data, Integer length, CountDownLatch latch){
            this.data = data;
            this.length = length;
            this.latch = latch;
        }
        @Override
        public void run() {
            System.out.println(System.currentTimeMillis());

//            System.out.println(Thread.currentThread().getId());
            completeDigits(data, length);
            latch.countDown();
        }
    }
}
