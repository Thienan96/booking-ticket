// package com.example.booking.base.service.cache;

// import com.fasterxml.jackson.core.type.TypeReference;

// import io.lettuce.core.RedisCommandExecutionException;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.data.redis.core.HashOperations;
// import org.springframework.data.redis.core.RedisTemplate;

// import java.util.Objects;
// import java.util.Set;
// import java.util.concurrent.TimeUnit;

// public class RedisHandler {
//     @Autowired
//     private RedisTemplate<String, Object> redisTemplate;

//         protected void saveCacheData(String key, String hashKey, Object hashValue, long timeout, TimeUnit timeUnit) {
//         // save or update cache data (key, hash_key, data)
//         redisTemplate.opsForHash().put(key, hashKey, CommonCoreUtils.convertObjectToJson(hashValue));
//         if (!this.hasKeyGroup(key, hashKey) && timeout > 0) {
//             // set timeout for saving cache data (key, timeout, time type)
//             redisTemplate.expire(key, timeout, timeUnit);
//         }
//     }

//     protected <T> T getCacheData(String key, String hashKey, Class<T> objectType) {
//         // get user cache data by key
//         HashOperations<String, Object, Object> ops = redisTemplate.opsForHash();
//         if (Objects.nonNull(ops.get(key, hashKey))) {
//             // exist data with key and user_key
//             String jsonString = ops.get(key, hashKey).toString();
//             return CommonCoreUtils.readJsonStrAsClassType(jsonString, objectType);
//         }
//         // not found user data
//         return null;
//     }

//     protected <T> T getCacheData(String key, String hashKey, TypeReference typeReference) {
//         // get user cache data by key
//         HashOperations<String, Object, Object> ops = redisTemplate.opsForHash();
//         if (Objects.nonNull(ops.get(key, hashKey))) {
//             // exist data with key and user_key
//             String jsonString = ops.get(key, hashKey).toString();
//             return (T) CommonCoreUtils.readJsonStrAsTypeReference(jsonString, typeReference);
//         }
//         // not found user data
//         return null;
//     }

//     protected Set<String> getAllKey(String patternRedisKey) {
//         Objects.requireNonNull(patternRedisKey);
//         return redisTemplate.keys(patternRedisKey);
//     }

//     protected void setRedisExpire(String key, int timeout, TimeUnit timeUnit) {
//         if (this.hasKey(key)) {
//             // update expiration of redis timeout when key exists
//             redisTemplate.expire(key, timeout, timeUnit);
//         }
//     }

//     protected long getRedisExpire(String key, TimeUnit timeUnit) {
//         // timeUnit: type TimeUnit (MILLISECONDS, SECONDS, MINUTES, HOURS, DAYS)
//         long keyExpire = redisTemplate.getExpire(key, timeUnit);
//         return keyExpire < 0 ? 0 : keyExpire;
//     }

//     protected boolean hasKey(String key) {
//         // check key (key) exists in Redis
//         Boolean hasKeyObj = redisTemplate.hasKey(key);
//         return Objects.nonNull(hasKeyObj) && hasKeyObj;
//     }

//     protected boolean hasKeyGroup(String key, String hashKey) {
//         // check key group (key - hash_key) identify exists in Redis
//         return Objects.nonNull(redisTemplate.opsForHash().get(key, hashKey));
//     }

//     protected boolean renameKey(String oldKey, String newKey) {
//         try {
//             // rename old key to new key
//             redisTemplate.rename(oldKey, newKey);
//             return true;
//         } catch (RedisCommandExecutionException ex) {
//             // can not find old key in order to rename
//             log.error("Error rename key: " + ex.getMessage());
//         }
//         return false;
//     }

//     protected boolean deleteHashData(String key, String... hashKey) {
//         return redisTemplate.opsForHash().delete(key, hashKey) > 0;
//     }

//     protected boolean deleteKey(String key) {
//         try {
//             // delete key and data
//             Boolean deleteKeyObj = redisTemplate.delete(key);
//             return Objects.nonNull(deleteKeyObj) && deleteKeyObj;
//         } catch (RedisCommandExecutionException ex) {
//             // can not find key in order to delete
//             log.error("Error delete Key: " + ex.getMessage());
//         }
//         return false;
//     }

//     /**
//      * Delete all key matching with pattern
//      *
//      * @param keyPattern
//      * @return
//      */
//     protected boolean deleteKeyPattern(String keyPattern) {
//         try {
//             // delete key and data
//             Set<String> keysReward = redisTemplate.keys(keyPattern + "*");
//             Boolean deleteKeyObj = true;
//             for (String key : keysReward) {
//                 deleteKeyObj = redisTemplate.delete(key);
//             }

//             return Objects.nonNull(deleteKeyObj) && deleteKeyObj;
//         } catch (RedisCommandExecutionException ex) {
//             // can not find key in order to delete
//             log.error("Error delete Key: " + ex.getMessage());
//         }
//         return false;
//     }

// }
