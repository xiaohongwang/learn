package com.learn.base.rpc;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.ipc.RPC;

import java.io.IOException;
import java.net.InetSocketAddress;

public class RpcLearn {
    public static void main(String[] args) throws IOException {
        RPCService serverceImpl=
                RPC.getProxy(RPCService.class,100,new InetSocketAddress("hadoop01",10000),new Configuration());
//<span style="white-space:pre">        </span>//100是指前面设置的版本号，InetSocketAddress中的是hdfs主机名和10000是通信端口
                String result = serverceImpl.userLogin("aaa", "aaa");  //设置用户用户名和密码
        System.out.println(result);
    }
}
