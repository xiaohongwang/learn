package com.ehcache;

import net.sf.ehcache.CacheManager;

import java.util.List;

public class EhCacheMgr {

    public static void main(String[] args) {
        CacheManager cacheManager = CacheManager.create();

        List<String> keys = cacheManager.getCache("labelCache").getKeys();

        for (String key : keys){
            System.out.println(key);
        }
    }
}
