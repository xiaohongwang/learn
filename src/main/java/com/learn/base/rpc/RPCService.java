package com.learn.base.rpc;

public interface RPCService {
    public static final long versionID = 100L;

    public String userLogin(String userName, String password);
}
