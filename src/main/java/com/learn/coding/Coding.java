package com.learn.coding;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Description: 因业务需要，需要对系统建立的机构进行编码，仿照地区编码规则
 * @Author: wangxh
 * @Date: 2019-04-17 10:38
 */
public class Coding {
//    编码规则一（pass 限定位数数字递增）
//    除一级机构外，其他机构编码位数为8位, 1～2 表示二级机构，3～4位表示三级机构，5～6位表示四级机构，7～8位表示五级机构）
//    一级机构： 0
//    二级机构：1～2位从11开始编码，新增二级机构每次增1, 3~8位固定为0
//    三级机构：1～2位所属二级机构编码的1～2位编码，3~4位从01开始编码，新增三级机构每次增1，5～8固定位为0
//    四级机构：1～4为所属三级机构编码的1～4位编码，5～6位从01开始编码，新增四级机构每次增1，7～8固定为0
//    五级机构：1～6为所属四级机构编码的1～位编码，7～8位从01开始编码，新增五级机构每次增1
//
//    编码规则二（添加分隔符，每一级从1开始，每次增加1）（pass 因系统中已根据地区编码规则进行查询权限处理，这种编码系统改动较大）
//    一级机构：1
//    二级机构： 1-1，1-2
//    三级机构：1-1-1，1-1-2，1-2-1，1-2-2
//    依次类推

//    编码规则三（本类中实现方式）
//    业务要求：
//       1.默认限定位数的数字递增
//       2.在限定位数数字达到全部为9的时候，用字母替换最后一位，数字归0再次递增
//       3.当字母长度为限定位数长度并且字母都为Z的时候，限定长度加1

//    编码规则四 在编码规则一基础上，使用两位表示一级机构，采用进制表示（两位32进制，可表示最大值：1024）


    private static long count = 0;

    /**
     *
     * @param len 限定长度
     * @param driver 当前编码
     */
        public static String sn(int len, String driver){
            String dr = "";
            if (isNumeric(driver)){
                AtomicInteger z = new AtomicInteger(Integer.valueOf(driver));
                z.getAndIncrement();
                if (z.toString().length() > len){
                    //超过限定位数 driver 99
                    System.out.println("1A");
                    return "1A";
                }else {
                    //未超限定位数
                    System.out.println(z.intValue());
                    return String.valueOf(z.intValue());
                }
            } else {
                //字母
                dr = driverCheck(driver,len);
                if(dr.equals(".N")){
                    //如超出限定长度并字母都为Z的时候，限定长度加1，dr重新开始，默认为空
                    System.out.println("a");
                    return "a";
                }else{
                    System.out.println(dr);
                    return dr;
                }
            }

        }

    /**
     * 判断字符串是否是数字
     * @param str
     * @return
     */
    public static boolean isNumeric(String str){
            Pattern pattern = Pattern.compile("[0-9]*");
            Matcher isnum = pattern.matcher(str);
            if (!isnum.matches()){
                return false;
            }
            return true;
        }


        /**
         * 字母有效检查
         * 1.检查字母是否都为Z
         * 2.检查字母长度
         * @param driver
         * @param len
         * @return
         */
        public static String driverCheck(String driver, int len){
            char[] charArray = driver.toCharArray();
            AtomicInteger z = new AtomicInteger(0);

            for (char c : charArray) {
                if(c == 'Z'){
                    z.getAndIncrement();
                }
            }

            if(z.intValue() == driver.length() && z.intValue() == len){
                //如所有字母都为Z，并且长度达到限定长度，返回.N
                return ".N";
            }else if(z.intValue() == driver.length() && z.intValue()<len){
                //如果所有字母都为Z，但长度未达到限定长度，则在调用字母递增方法之前加入@用以递增A
                return driver("@" + driver);
            }else{
                //以上两个条件都不满足，则直接递增
                return driver(driver);
            }

        }

        /**
         * 字母递增
         * @param driver
         * @return
         */
        public static String driver(String driver){
            if(driver != null && driver.length() > 0){
                char[] charArray = driver.toCharArray();
                AtomicInteger z = new AtomicInteger(0);
                for(int i = charArray.length - 1 ; i > -1; i--){
                    if(charArray[i] == 'Z'){
                        z.set(z.incrementAndGet());
                    }else{
                        if(z.intValue() > 0 || i == charArray.length - 1){
                            AtomicInteger atomic = new AtomicInteger(charArray[i]);
                            if (atomic.intValue() == 57){
                                charArray[i]='A';
                            } else {
                                charArray[i]=(char) atomic.incrementAndGet();
                            }
                            //非最后位增加,为Z位变A
                            if (i != charArray.length - 1) {
                                for (int j = charArray.length -1; j >= charArray.length - z.intValue(); j --){
                                    charArray[j] = 'A';
                                }
                            }
                            z.set(0);
                        }

                    }
                }

                return String.valueOf(charArray);
            }else{
                return "A";
            }
        }

        public static void main(String[] args) {
            String driver = "10";
            System.out.println(driver);
            count ++;
           for (int i = 0 ;i < 1024; i++) {
              driver =  sn(2,  driver);
              count ++;
           }
            System.out.println(count);
        }
}
