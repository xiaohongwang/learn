package com.learn.startprocess;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * @Description:  java启动exe  shell脚本
 * @Author: wangxh
 * @Date: 2019-03-05 15:39
 */
public class StartProcess {
    public static void main(String[] args) {
        String folder = "/Users/wangxiaohong/aa.sh".substring(0,"/Users/wangxiaohong/aa.sh".lastIndexOf(java.io.File.separator) + 1);
        String cmd[] = {"/Users/wangxiaohong/aa.sh"};
        try {
            final Process process = Runtime.getRuntime().exec(cmd, null, new File(folder));
            InputStream is = process.getInputStream();
            byte[] data = new byte[1024*2];
            int i = 0;
            is.read(data,0,1024*2);
            System.out.println(new String(data));
            is.close();
            is = null;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
