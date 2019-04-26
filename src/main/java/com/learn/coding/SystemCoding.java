package com.learn.coding;

/**
 * @Description: 进制编码 32进制  ==》 二进制
 *               二进制 ==》 32进制
 * @Author: wangxh
 * @Date: 2019-04-19 15:48
 */
public class SystemCoding {

    final static char[] digits = {
            '0', '1', '2', '3', '4', '5', '6', '7',
            '8','9', 'A', 'B', 'C', 'D', 'E', 'F',
            'G', 'H','J', 'K', 'L','M', 'N', 'P',
            'R', 'S', 'T', 'U', 'W', 'X', 'Y','Z' };

    /**
       * 将其它进制的数字（字符串形式）转换为十进制的数字。
       *
       * @param s
       *            其它进制的数字（字符串形式）
       * @param system
       *            指定的进制，常见的2/8/16。
       * @return 转换后的数字。
       */
    public static int stringToNumeric(String s, int system) {
        char[] buf = new char[s.length()];
        s.getChars(0, s.length(), buf, 0);
        long num = 0;
        for (int i = 0; i < buf.length; i++) {
            for (int j = 0; j < digits.length; j++) {
                if (digits[j] == buf[i]) {
                 num += j * Math.pow(system, buf.length - i - 1);
                 break;
                }
            }
        }
        return (int) num;
    }


    public static void main(String[] args) {
        System.out.println(stringToNumeric("Z",32));

    }

    public static void convert32To2(String driver){

        //三十二进制转二进制  不包含 ILOU 字符

    }

}
