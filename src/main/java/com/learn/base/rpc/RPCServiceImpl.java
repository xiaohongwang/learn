package com.learn.base.rpc;

public class RPCServiceImpl implements RPCService {
    @Override
    public String userLogin(String userName, String password) {
        return userName +"  logged in successfully!";
    }
}
