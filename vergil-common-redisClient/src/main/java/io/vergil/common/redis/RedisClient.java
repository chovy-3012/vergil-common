package io.vergil.common.redis;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

import io.vergil.common.lang.StringUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.ScanParams;
import redis.clients.jedis.ScanResult;

/**
 * 访问redis客户端
 * 
 * @author zhaowei
 * @date 2015年11月26日上午11:14:16
 */
public class RedisClient {

	private JedisPool pool;

	public JedisPool getPool() {
		return pool;
	}

	public void setPool(JedisPool pool) {
		this.pool = pool;
	}

	public RedisClient(JedisPool pool) {
		this.pool = pool;
	}

	public RedisClient(String host, int port, GenericObjectPoolConfig poolConfig) {
		if (poolConfig == null) {
			pool = new JedisPool(host, port);
		} else {
			pool = new JedisPool(poolConfig, host, port);
		}
	}

	public boolean isExists(String key) {
		if (StringUtils.isEmpty(key)) {
			return false;
		}
		Jedis redis = null;
		try {
			redis = pool.getResource();
			return redis.exists(key);
		} finally {
			pool.returnResource(redis);
		}
	}

	public void expire(String key, int seconds) {
		Jedis redis = null;
		try {
			redis = pool.getResource();
			redis.expire(key, seconds);
		} finally {
			pool.returnResource(redis);
		}
	}

	public void set(String key, String value) {
		if (StringUtils.isEmpty(key) || StringUtils.isEmpty(value)) {
			return;
		}

		Jedis redis = null;
		try {
			redis = pool.getResource();
			redis.set(key, value);
		} finally {
			pool.returnResource(redis);
		}
	}

	public String get(String key) {
		if (StringUtils.isEmpty(key)) {
			return null;
		}

		Jedis redis = null;
		try {
			redis = pool.getResource();
			return redis.get(key);
		} finally {
			pool.returnResource(redis);
		}
	}

	/**
	 * 设置带过期时间的键值，过期时间为秒
	 * 
	 * @param key
	 * @param value
	 * @param expire
	 *            过期时间单位秒
	 */
	public void setWithExpire(String key, String value, long expireTime) {
		if (StringUtils.isEmpty(key)) {
			return;
		}
		Jedis redis = null;
		try {
			redis = pool.getResource();
			if (redis.get(key) != null) {
				redis.set(key, value, "XX", "EX", expireTime);
			} else {
				redis.set(key, value, "NX", "EX", expireTime);
			}
		} finally {
			pool.returnResource(redis);
		}
	}

	/**
	 * 左进右出表——左边插入
	 * 
	 * @param queueName
	 * @param data
	 */
	public void push(String queueName, String data) {
		if (StringUtils.isEmpty(queueName) || StringUtils.isEmpty(data)) {
			return;
		}
		Jedis redis = null;
		try {
			redis = pool.getResource();
			redis.lpush(queueName, data);
		} finally {
			pool.returnResource(redis);
		}
	}

	/**
	 * 左进右出表——右边取出
	 * 
	 * @param queueName
	 * @return
	 */
	public String pop(String queueName) {
		if (StringUtils.isEmpty(queueName)) {
			return null;
		}
		Jedis redis = null;
		try {
			redis = pool.getResource();
			String value = redis.rpop(queueName);
			return value;
		} finally {
			pool.returnResource(redis);
		}
	}

	public void lpush(String key, String... values) {
		if (StringUtils.isEmpty(key)) {
			return;
		}
		Jedis redis = null;
		try {
			redis = pool.getResource();
			redis.lpush(key, values);
		} finally {
			pool.returnResource(redis);
		}
	}

	public void rpush(String key, String... values) {
		if (StringUtils.isEmpty(key)) {
			return;
		}
		Jedis redis = null;
		try {
			redis = pool.getResource();
			redis.rpush(key, values);
		} finally {
			pool.returnResource(redis);
		}
	}

	public String hget(String key, String field) {
		if (StringUtils.isEmpty(key) || StringUtils.isEmpty(field)) {
			return null;
		}
		Jedis redis = null;
		try {
			redis = pool.getResource();
			return redis.hget(key, field);
		} finally {
			pool.returnResource(redis);
		}
	}

	/**
	 * 获取list数据类型中的元素
	 * 
	 * @param key
	 * @param start
	 * @param end
	 * @return
	 */
	public List<String> lrange(String key, int start, int end) {
		if (StringUtils.isEmpty(key)) {
			return null;
		}

		Jedis redis = null;
		try {
			redis = pool.getResource();
			return redis.lrange(key, start, end);
		} finally {
			pool.returnResource(redis);
		}
	}

	/**
	 * 模糊查询获得键列表
	 * 
	 * @param match
	 * @return
	 */
	public List<String> getKeys(String match) {
		if (StringUtils.isEmpty(match)) {
			return null;
		}

		Jedis redis = null;
		try {
			redis = pool.getResource();
			ScanResult<String> result = redis.scan("0", new ScanParams().match(match));
			List<String> keys = result.getResult();
			return keys;
		} finally {
			pool.returnResource(redis);
		}

	}

	/**
	 * 模糊查询获得键列表
	 * 
	 * @param match
	 * @return
	 */
	public Set<String> getKeySet(String match) {
		if (StringUtils.isEmpty(match)) {
			return null;
		}
		Jedis redis = null;
		try {
			redis = pool.getResource();
			return redis.keys(match);
		} finally {
			pool.returnResource(redis);
		}
	}

	/**
	 * 获取list数据类型中的第一个元素
	 * 
	 * @param keys
	 * @return
	 */
	public List<String> getFirstElementFromList(List<String> keys) {
		List<String> values = new ArrayList<String>();
		Jedis redis = null;
		try {
			redis = pool.getResource();
			for (String key : keys) {
				String value = redis.lindex(key, -1);
				values.add(value);
			}

			return values;

		} finally {
			pool.returnResource(redis);
		}
	}

	public List<String> mget(String match) {
		if (StringUtils.isEmpty(match)) {
			return null;
		}

		Jedis redis = null;
		try {
			redis = pool.getResource();
			List<String> keys = getKeys(match);

			if (keys != null && keys.size() > 0) {
				String[] keysArr = new String[keys.size()];
				keysArr = keys.toArray(keysArr);
				List<String> values = redis.mget(keysArr);
				return values;
			}
			return null;
		} finally {
			pool.returnResource(redis);
		}
	}

	/**
	 * 删除key对应的记录
	 * 
	 * @title: del
	 * @param key
	 * @return
	 * 
	 * @Date: 2015年4月3日上午11:26:35
	 */
	public Long del(String key) {
		if (StringUtils.isEmpty(key)) {
			return null;
		}
		Long re = null;
		Jedis redis = null;
		try {
			redis = pool.getResource();
			re = redis.del(key);
		} finally {
			pool.returnResource(redis);
		}
		return re;
	}

	/**
	 * 添加Map
	 * 
	 * @title: hmset
	 * @param key
	 * @param value
	 * @return
	 * 
	 * @Date: 2015年4月14日下午3:02:13
	 */
	public String hmset(String key, Map<String, String> value) {

		if (StringUtils.isEmpty(key)) {
			return null;
		}

		Jedis redis = null;
		try {
			redis = pool.getResource();
			if (redis.exists(key)) {
				redis.del(key);
			}
			return redis.hmset(key, value);
		} finally {
			pool.returnResource(redis);
		}
	}

	/**
	 * 获取Map
	 * 
	 * @title: hmget
	 * @param key
	 * @return
	 * 
	 * @Date: 2015年4月14日下午3:02:32
	 */
	public Map<String, String> hmget(String key) {

		if (StringUtils.isEmpty(key)) {
			return null;
		}

		Jedis redis = null;
		try {
			redis = pool.getResource();
			return redis.hgetAll(key);
		} finally {
			pool.returnResource(redis);
		}
	}

	/**
	 * 
	 * 根据key获取所有field的value值
	 */
	public List<String> hvals(String key) {
		if (StringUtils.isEmpty(key)) {
			return null;
		}
		Jedis redis = null;
		try {
			redis = pool.getResource();
			return redis.hvals(key);
		} finally {
			pool.returnResource(redis);
		}
	}

	public Long hset(String key, String field, String value) {
		if (StringUtils.isEmpty(key) || StringUtils.isEmpty(field)) {
			return null;
		}
		Jedis redis = null;
		try {
			redis = pool.getResource();
			return redis.hset(key, field, value);
		} finally {
			pool.returnResource(redis);
		}
	}

	/**
	 * 删除map中的记录
	 * 
	 * @title: hdel
	 * @param key
	 * @param field
	 * @return
	 * @Date: 2015年4月14日下午3:03:30
	 */
	public Long hdel(String key, String field) {

		if (StringUtils.isEmpty(key) || StringUtils.isEmpty(field)) {
			return null;
		}
		Jedis redis = null;
		try {
			redis = pool.getResource();
			return redis.hdel(key, field);
		} finally {
			pool.returnResource(redis);
		}
	}

	// ---------------------------------------set------------------------------------------------
	/**
	 * set添加
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public Long hsetAdd(String key, String value) {
		if (StringUtils.isEmpty(key) || StringUtils.isEmpty(value)) {
			return null;
		}
		Jedis redis = null;
		try {
			redis = pool.getResource();
			return redis.sadd(key, value);
		} finally {
			pool.returnResource(redis);
		}
	}

	/**
	 * set删除
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public Long hsetRemove(String key, String value) {
		if (StringUtils.isEmpty(key) || StringUtils.isEmpty(value)) {
			return null;
		}
		Jedis redis = null;
		try {
			redis = pool.getResource();
			return redis.srem(key, value);
		} finally {
			pool.returnResource(redis);
		}
	}

	/**
	 * set是否存在值
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public boolean hsetExists(String key, String value) {
		if (StringUtils.isEmpty(key) || StringUtils.isEmpty(value)) {
			return false;
		}
		Jedis redis = null;
		try {
			redis = pool.getResource();
			return redis.sismember(key, value);
		} finally {
			pool.returnResource(redis);
		}
	}

	/**
	 * set获取所有
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public Set<String> hsetGetAll(String key) {
		if (StringUtils.isEmpty(key)) {
			return null;
		}
		Jedis redis = null;
		try {
			redis = pool.getResource();
			return redis.smembers(key);
		} finally {
			pool.returnResource(redis);
		}
	}
}
