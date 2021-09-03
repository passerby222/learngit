package com.miaoshaproject.service.impl;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.miaoshaproject.service.ICacheService;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.concurrent.TimeUnit;

/**
 * ClassName CacheServiceImpl
 * Description TODO
 *
 * @author 13299
 * Date 2020/8/13 15:07
 * @version 1.0
 **/
@Service
public class CacheServiceImpl implements ICacheService {
    private Cache<String, Object> commonCache = null;

    @PostConstruct
    public void init(){
        commonCache = CacheBuilder.newBuilder()
                //设置缓存容器的初始容量为10
                .initialCapacity(10)
                //设置缓存中最大可以存储100个key，超过100个之后会按照LRU的策略移除缓存项
                .maximumSize(100)
                //设置写入缓存后多少秒过期
                .expireAfterWrite(1, TimeUnit.MINUTES).build();
    }

    @Override
    public void setCommonCache(String key, Object value) {
        commonCache.put(key, value);
    }

    @Override
    public Object getFromCommonCache(String key) {
        return commonCache.getIfPresent(key);
    }
}
