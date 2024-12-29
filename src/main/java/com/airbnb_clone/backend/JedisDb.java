package com.airbnb_clone.backend;

import lombok.Getter;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@Getter
public class JedisDb {
    private static JedisDb jedisInstance;
    private final Jedis jedisPool;

    // jedis 를 요즘 잘 안 씀. lettuce 를 많이 씀. 성능이 좀 더 좋음.
    // redisson 도 있음. 용도에 맞게 사용하면 됨.
    // singleton 보다는 bean 으로 등록하는 게 좋음.

    // Private constructor to prevent instantiation
    private JedisDb() {
        // Initialize the Redis connection
        String host = "127.0.0.1";
        int port = 6379;
        int timeout = 3000;
        int db = 0;

        JedisPoolConfig config = new JedisPoolConfig();
        JedisPool pool = new JedisPool(config, host, port, timeout, null, db);
        jedisPool = pool.getResource();
    }

    // Static method to provide the single instance
    public static JedisDb getInstance() {
        if (jedisInstance == null) {
            synchronized (JedisDb.class) {
                if (jedisInstance == null) {
                   jedisInstance = new JedisDb(); // Initialize the connection
                }
            }
        }
        return jedisInstance;
    }
}

/* when using
// Get the Singleton Redis client instance
Jedis jedisPool = JedisDb.getInstance().getJedisPool();

// Perform Redis operations
jedisPool.set("key", "value");
String value = jedisPool.get("key");
*/
