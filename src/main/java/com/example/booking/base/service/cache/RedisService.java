package com.example.booking.base.service.cache;


public interface RedisService {

    // save string 
    void set(String key, String value);

    void set(String key, String value, long expireTime);

    Boolean setIfAbsent(String key, String value);

    Boolean setIfAbsent(String key, String value, long expireTime);

    // save object
    void set(String key, String hashKey, Object value);

    void set(String key, String hashKey, Object value, long expireTime);

    Boolean setIfAbsent(String key, String hashKey, Object value);

    Boolean setIfAbsent(String key, String hashKey, Object value, long expireTime);

    String get(String key);

    Object get(String key, String hashKey);

    Boolean deleteKey(String key);

    Long deleteHashKey(String key, Object... hashKeys);

    boolean deleteKeyPattern(String keyPattern);

    Long decrement(String key);

    Long increment(String key);

    Long increment(String key, String hashKey);

    Long getTokenExpireTime();


}
