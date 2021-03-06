package com.learn.algorithm;

/**
 * 递归 考虑因素
 *      -- 递归结束条件
 *      -- 进行方法调用的条件
 *      -- 内存问题
 *
 *
 *      n! 阶乘
 */
public class Factorial {

    /**
     * 当n整数值较小时，无须考虑n！是否会超出数据类型的范围
     */
    public long factorial(int n){
        if (n == 0) return 1l;
        return n * factorial(n - 1);
    }
    /**
     * -- 不使用递归
     * 当n整数值较小时，无须考虑n！是否会超出数据类型的范围
     */
    public long factorial1(int n){
        if (n == 0) return 1l;
        long sum  = 1l;
        for (int i = 1; i <= n; i++){
            sum = sum * i;
        }
        return sum;
    }

    /**
     * 当n整数值较大时，n！会超出数据类型的范围，使用  数组模拟大数   的乘法
     * 思路：主要在进位的考虑
     * 1 * 2 * 3 * 4 * 5 = 120 在数组中为021
     * 数组中 0 2 1 * 6 = 0 2 7
     */
    public  void getNFactorial(int n) {
        int num[] = new int[1000];
        int i, j;
        if (n == 1 || n == 0) {
            System.out.println(1);
        } else {
            int p, h;// p 存放当前结果的位数，h为进位；
            p = 1;
            h = 0;
            num[1] = 1;
            for (i = 2; i <= n; i++) {
                // 使得num[]的每位与i相乘
                for (j = 1; j <= p; j++) {
                    num[j] = num[j] * i + h;
                    h = num[j] / 10;
                    num[j] = num[j] % 10;
                }
                // 表示向新的位置进位
                while (h > 0) {
                    num[j] = h % 10;
                    h = h / 10;
                    j++;
                }
                p = j - 1;
            }
            for (i = p; i >= 1; i--) {
                System.out.print(num[i]);
            }
        }
    }

    public long factorial2(){
        return Integer.MAX_VALUE * 120;
    }


    public static void main(String[] args) {
        Factorial test = new Factorial();
//        System.out.println(test.factorial2());
//
        System.out.println(test.factorial1(50));
//        System.out.println( test.factorial1(50));
        test.getNFactorial(50);
    }


}
