package com.example.booking.base.service.cache.impl;

import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import com.example.booking.base.service.cache.RedisService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RedisServiceImpl implements RedisService {

    private static final TimeUnit TIME_UNIT_CONFIG = TimeUnit.SECONDS;

    private final RedisTemplate<String, Object> objectRedisTemplate;
    private final RedisTemplate<String, String> stringRedisTemplate;

    @Override
    public void set(String key, String value) {
        try {
            stringRedisTemplate.opsForValue().set(key, value);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @Override
    public Boolean setIfAbsent(String key, String value) {
        try {
            return stringRedisTemplate.opsForValue().setIfAbsent(key, value);
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    @Override
    public Boolean setIfAbsent(String key, String value, long expireTime) {
        try {
            return stringRedisTemplate.opsForValue().setIfAbsent(key, value, expireTime, TIME_UNIT_CONFIG);
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    @Override
    public void set(String key, String hashKey, Object value) {
        try {
            objectRedisTemplate.boundHashOps(key).put(hashKey, value);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @Override
    public void set(String key, String value, long expireTime) {
        try {
            stringRedisTemplate.opsForValue().set(key, value, expireTime, TIME_UNIT_CONFIG);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @Override
    public Boolean setIfAbsent(String key, String hashKey, Object value) {
        try {
            return objectRedisTemplate.boundHashOps(key).putIfAbsent(hashKey, value);
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    @Override
    public void set(String key, String hashKey, Object value, long expireTime) {
        try {
            objectRedisTemplate.opsForHash().put(key, hashKey, value);
            objectRedisTemplate.expire(key, expireTime, TIME_UNIT_CONFIG);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @Override
    public Boolean setIfAbsent(String key, String hashKey, Object value, long expireTime) {
        try {
            Boolean putIfAbsent = objectRedisTemplate.boundHashOps(key).putIfAbsent(hashKey, value);
            objectRedisTemplate.expire(key, expireTime, TIME_UNIT_CONFIG);
            return putIfAbsent;
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    @Override
    public String get(String key) {
        try {
            return stringRedisTemplate.opsForValue().get(key);
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    @Override
    public Object get(String key, String hashKey) {
        try {
            if (hashKey == null) {
                return objectRedisTemplate.boundHashOps(key);
            } else {
                return objectRedisTemplate.boundHashOps(key).get(hashKey);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    @Override
    public Boolean deleteKey(String key) {
        try {
            Boolean deleteKeyObj = stringRedisTemplate.delete(key);
            return Objects.nonNull(deleteKeyObj) && deleteKeyObj;
        } catch (Exception e) {
            System.out.println(e);
        }
        return false;
    }

    @Override
    public Long deleteHashKey(String key, Object... hashKeys) {
        try {
            // delete key and data
            Long deleteKeyObj = objectRedisTemplate.opsForHash().delete(key, hashKeys);
            return deleteKeyObj;
        } catch (Exception e) {
            System.out.println(e);
        }
        return 0L;
    }

    @Override
    public boolean deleteKeyPattern(String keyPattern) {
        try {
            Set<String> keys = stringRedisTemplate.keys(keyPattern + "*");
            Set<String> hashKeys = objectRedisTemplate.keys(keyPattern + "*");
            Boolean deleteKeyObj = true;
            for (String key : keys) {
                deleteKeyObj = stringRedisTemplate.delete(key);
            }
            for (String key : hashKeys) {
                deleteKeyObj = objectRedisTemplate.delete(key);
            }

            return Objects.nonNull(deleteKeyObj) && deleteKeyObj;
        } catch (Exception e) {
            // can not find key in order to delete
            System.out.println(e);

        }
        return false;
    }

    protected boolean hasKeyGroup(String key, String hashKey) {
        // check key group (key - hash_key) identify exists in Redis
        return Objects.nonNull(objectRedisTemplate.opsForHash().get(key, hashKey));
    }

    @Override
    public Long decrement(String key) {
        return stringRedisTemplate.boundValueOps(key).increment(-1);
    }

    @Override
    public Long increment(String key) {
        return stringRedisTemplate.boundValueOps(key).increment(1);
    }
}
