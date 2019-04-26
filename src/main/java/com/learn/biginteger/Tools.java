package com.learn.biginteger;

import java.math.BigInteger;

/**
 * @Description: 使用BigInteger.setBit 和 BigInteger.testBit进行权限控制
 * @Author: wangxh
 * @Date: 2019-04-26 14:47
 */
public class Tools {

    /**
     * 把具体的权限设置为一个正整数值，如果一个用户有多个权限的话，比如1，2权限，那么我们设置值的时候就是num.setBit(1)，num.setBit(2)，
     * 然后把返回的num值保存在session中，要验证是否有权限的话，只要从session中取得保存的num，然后执行下num.test(权限值)，如果返回true就是有权限的
     *
     * session中存储权限的空间很小，一个整数就代表了所有的权限，验证的时候计算速度也很快
     */


    /**
     * 利用BigInteger对权限进行2的权的和计算
     * @param rights int型权限编码数组
     * @return 2的权的和
     * 6= 2^2 + 2^1 其实计算的值是2的权的和
     */
    public static BigInteger sumRights(int[] rights){
        BigInteger num = BigInteger.valueOf(0);
        for(int i=0; i<rights.length; i++){
            num = num.setBit(rights[i]);
        }
        return num;
    }

    /**
     * 测试是否具有指定编码的权限
     * @param sum
     * @param targetRights
     * @return
     */
    public static boolean testRights(BigInteger sum,int targetRights){
        return sum.testBit(targetRights);
    }

    public static void main(String[] args) {
        int[] rights = new int[]{5};
        BigInteger bigInteger = sumRights(rights);
        System.out.println(bigInteger);
        System.out.println(testRights(bigInteger, 4));


        //testBit方法说明：
        //1.该方法的index是从后往前计算的。比如3  setBit = 8，就是二进制的：1000.那么index为0,1,2，就是0；index为3的时候都是1
        //2.如果位置值为0,1,2，那么返回false；如果是3，那么返回true

        BigInteger  bi = new BigInteger("8");

        String str1 = "Test Bit on " + bi + " at index 0 returns " + bi.testBit(0);
        String str2 = "Test Bit on " + bi + " at index 1 returns " + bi.testBit(1);
        String str3 = "Test Bit on " + bi + " at index 2 returns " + bi.testBit(2);
        String str4 = "Test Bit on " + bi + " at index 3 returns " + bi.testBit(3);

        System.out.println( str1 );
        System.out.println( str2 );
        System.out.println( str3 );
        System.out.println( str4 );

    }
}
