package com.kuaizhan.utils;

import org.springframework.stereotype.Component;
import redis.clients.jedis.BinaryClient;
import redis.clients.jedis.JedisCluster;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * redis操作工具类
 * Created by Mr.Jadyn on 2016/12/27.
 */
@Component("redisUtil")
public final class RedisUtil {

    @Resource
    private JedisCluster jedisCluster;


    /**
     * 检查数据是否存在
     *
     * @param key key
     * @return
     */
    public boolean exists(String key) {
        return jedisCluster.exists(key);

    }

    public boolean exists(String key, String field) {
        return jedisCluster.hexists(key, field);
    }

    /**
     * 比较相等
     *
     * @param key
     * @param str
     * @return
     */
    public boolean equal(String key, String str) {
        if (exists(key)) {
            return get(key).equals(str);
        }
        return false;
    }

    /**
     * 获取数据的缓存剩余时间
     *
     * @param key key
     * @return
     */
    public long getTtl(String key) {
        return jedisCluster.ttl(key);
    }

    /**
     * 删除数据
     *
     * @param key key
     */
    public void delete(String key) {
        jedisCluster.del(key);
    }


    /**
     * 设置值以及设置缓存时间
     *
     * @param key    key
     * @param value  值
     * @param expire 剩余时间
     */
    public void setEx(String key, int expire, String value) {
        jedisCluster.setex(key, expire, value);
    }

    /**
     * 向Key对应的值增加num
     *
     * @param key key
     * @param num 增量
     */
    public void incrBy(String key, long num) {
        jedisCluster.incrBy(key, num);
    }

    /**
     * 设置缓存时间
     *
     * @param key    key
     * @param second 时间(秒)
     */
    public void setTimeout(String key, int second) {
        jedisCluster.expire(key, second);
    }

    /**
     * 在指定时间缓存失效
     *
     * @param key      key
     * @param unixTime unix时间
     */
    public void setExpireAt(String key, long unixTime) {
        jedisCluster.expireAt(key, unixTime);
    }

    /**
     * 移除缓存失效时间
     *
     * @param key key
     */
    public void persist(String key) {
        jedisCluster.persist(key);
    }

    /**
     * 将一个或多个成员元素加入到集合中
     *
     * @param key
     */
    public void sAdd(String key, String... values) {
        jedisCluster.sadd(key, values);
    }

    /**
     * 移除集合中的一个或多个成员元素
     *
     * @param key
     * @param values
     */
    public void sRem(String key, String... values) {
        jedisCluster.srem(key, values);
    }

    /**
     * 返回指定key的set集合
     *
     * @param key key
     * @return
     */
    public Set<String> sMembers(String key) {
        return jedisCluster.smembers(key);
    }

    /**
     * 获取元素数量
     *
     * @param key key
     * @return
     */
    public long size(String key) {
        return jedisCluster.scard(key);
    }

    /**
     * 命令判断成员元素是否是集合的成员
     *
     * @param key   key
     * @param value 值
     * @return
     */
    public boolean contains(String key, String value) {
        return jedisCluster.sismember(key, value);
    }

    /**
     * 返回列表的长度
     * 如果列表 key 不存在，则 key 被解释为一个空列表，返回0 如果 key 不是列表类型，返回一个错误。
     *
     * @param key key
     * @return
     */
    public long listSize(String key) {
        return jedisCluster.llen(key);
    }

    /**
     * 返回列表中指定区间内的元素，区间以偏移量 START 和 END 指定
     * 0 -1则返回全部
     *
     * @param key   key
     * @param start 起始
     * @param end   结束
     * @return
     */
    public List<String> listRange(String key, int start, int end) {
        return jedisCluster.lrange(key, start, end);
    }

    /**
     * 将一个或多个值插入到列表头部
     *
     * @param key   key
     * @param value 值
     */
    public void leftPush(String key, String... value) {
        jedisCluster.lpush(key, value);
    }

    /**
     * 命令用于将一个或多个值插入到列表的尾部(最右边)
     *
     * @param key   key
     * @param value 值
     */
    public void rightPush(String key, String... value) {
        jedisCluster.rpush(key, value);
    }

    /**
     * 移除并返回列表的第一个元素
     *
     * @param key key
     */
    public void leftPop(String key) {
        jedisCluster.lpop(key);
    }

    /**
     * 在指定index插入值
     *
     * @param key   key
     * @param index 位置
     * @param value 值
     */
    public void insertByIndex(String key, int index, String value) {
        jedisCluster.lset(key, index, value);
    }

    /**
     * 获取制定位置的值
     *
     * @param key   key
     * @param index 位置
     * @return
     */
    public String getByIndex(String key, int index) {
        return jedisCluster.lindex(key, index);
    }

    /**
     * 在列表的元素前或者后插入元素。 当指定元素不存在于列表中时，不执行任何操作。 当列表不存在时，被视为空列表，不执行任何操作
     *
     * @param key      key
     * @param position LIST_POSITION.BEFORE 或者 LIST_POSITION.AFTER
     * @param pivot    指定元素的值
     * @param value    新值
     */
    public void insertByPosition(String key, BinaryClient.LIST_POSITION position, String pivot, String value) {
        jedisCluster.linsert(key, position, pivot, value);
    }

    /**
     * 限制push的数量
     *
     * @param key   key
     * @param limit 大小
     * @param value 值
     */
    public void leftPushWithLimit(String key, int limit, String... value) {
        jedisCluster.lpush(key, value);
        jedisCluster.ltrim(key, 0, limit);
    }

    /**
     * 为哈希表中的字段赋值
     * 如果哈希表不存在，一个新的哈希表被创建并进行 HSET 操作
     * 如果字段已经存在于哈希表中，旧值将被覆盖
     *
     * @param key   key
     * @param field 字段
     * @param value 值
     */
    public void setHash(String key, String field, String value) {
        jedisCluster.hset(key, field, value);
    }

    /**
     * 返回哈希表中指定字段的值
     *
     * @param key   key
     * @param field 字段
     * @return
     */
    public String getHash(String key, String field) {
        return jedisCluster.hget(key, field);
    }

    /**
     * 同时将多个 field-value (字段-值)对设置到哈希表中
     * 此命令会覆盖哈希表中已存在的字段
     * 如果哈希表不存在，会创建一个空哈希表，并执行 HMSET 操作
     *
     * @param key key
     * @param map hashmap
     */
    public void setHashMap(String key, Map<String, String> map) {
        jedisCluster.hmset(key, map);
    }

    /**
     * 命令用于返回哈希表中，一个或多个给定字段的值
     * 如果指定的字段不存在于哈希表，那么返回一个 null 值
     *
     * @param key   key
     * @param field 字段
     * @return
     */
    public List<String> getHashList(String key, String... field) {
        return jedisCluster.hmget(key, field);
    }

    /**
     * 获取哈希表中字段的数量
     *
     * @param key key
     * @return
     */
    public long getHashLength(String key) {
        return jedisCluster.hlen(key);
    }

    /**
     * 用于查看哈希表的指定字段是否存在
     *
     * @param key   key
     * @param field 字段
     * @return
     */
    public boolean hashExists(String key, String field) {
        return jedisCluster.hexists(key, field);
    }

    /**
     * 命令用于为哈希表中的字段值加上指定增量值。
     * 增量也可以为负数，相当于对指定字段进行减法操作。
     * 如果哈希表的 key 不存在，一个新的哈希表被创建并执行 HINCRBY 命令。
     * 如果指定的字段不存在，那么在执行命令前，字段的值被初始化为 0 。
     * 对一个储存字符串值的字段执行 HINCRBY 命令将造成一个错误。
     * 本操作的值被限制在 64 位(bit)有符号数字表示之内。
     *
     * @param key   key
     * @param field 字段
     * @param num   增量
     */
    public void hashIncrBy(String key, String field, long num) {
        jedisCluster.hincrBy(key, field, num);
    }

    /**
     * 返回哈希表中，所有的字段和值
     *
     * @param key key
     * @return
     */
    public Map<String, String> getHashMap(String key) {
        return jedisCluster.hgetAll(key);
    }

    /**
     * 将一个或多个成员元素及其分数值加入到有序集当中。
     * 如果某个成员已经是有序集的成员，那么更新这个成员的分数值，并通过重新插入这个成员元素，来保证该成员在正确的位置上。
     * 分数值可以是整数值或双精度浮点数。
     * 如果有序集合 key 不存在，则创建一个空的有序集并执行 ZADD 操作。
     * 当 key 存在但不是有序集类型时，返回一个错误
     *
     * @param key   key
     * @param score 分数
     * @param value 值
     */
    public void zAdd(String key, double score, String value) {
        jedisCluster.zadd(key, score, value);

    }

    /**
     * Redis Zrange 返回有序集中，指定区间内的成员。
     * 其中成员的位置按分数值递增(从小到大)来排序。
     * 具有相同分数值的成员按字典序(lexicographical order )来排列
     *
     * @param key   key
     * @param start 起始
     * @param end   结束
     * @return
     */
    public Set<String> zRange(String key, long start, long end) {
        return jedisCluster.zrange(key, start, end);
    }

    /**
     * 指定区间内的成员。
     * 其中成员的位置按分数值递减(从大到小)来排列。
     * 具有相同分数值的成员按字典序的逆序(reverse lexicographical order)排列
     *
     * @param key   key
     * @param start 起始
     * @param end   结束
     * @return
     */
    public Set<String> zRevRange(String key, long start, long end) {
        return jedisCluster.zrange(key, start, end);
    }

    /**
     * 获取大小
     *
     * @param key key
     * @return
     */
    public long zSize(String key) {
        return jedisCluster.zcard(key);
    }

    /**
     * 计算有序集合中指定分数区间的成员数量
     *
     * @param key   key
     * @param start 开始
     * @param end   结束
     * @return
     */
    public long zCount(String key, double start, double end) {
        return jedisCluster.zcount(key, start, end);
    }

    /**
     * Redis Zrangebyscore 返回有序集合中指定分数区间的成员列表。有序集成员按分数值递增(从小到大)次序排列。
     * 具有相同分数值的成员按字典序来排列(该属性是有序集提供的，不需要额外的计算)。
     * 默认情况下，区间的取值使用闭区间 (小于等于或大于等于)，你也可以通过给参数前增加 ( 符号来使用可选的开区间 (小于或大于)
     *
     * @param key   key
     * @param start 开始
     * @param end   结束
     * @return
     */
    public Set<String> zRangeByScore(String key, double start, double end) {
        return jedisCluster.zrangeByScore(key, start, end);
    }

    /**
     * Redis Zrevrangebyscore 返回有序集中指定分数区间内的所有的成员。有序集成员按分数值递减(从大到小)的次序排列。
     * 具有相同分数值的成员按字典序的逆序(reverse lexicographical order )排列。
     *
     * @param key   key
     * @param start 开始
     * @param end   结束
     * @return
     */
    public Set<String> zRevRangeByScore(String key, double start, double end) {
        return jedisCluster.zrevrangeByScore(key, start, end);
    }

    /**
     * Redis Incr 命令将 key 中储存的数字值增一
     * 如果 key 不存在，那么 key 的值会先被初始化为 0 ，然后再执行 INCR 操作
     * 如果值包含错误的类型，或字符串类型的值不能表示为数字，那么返回一个错误
     *
     * @param key
     */
    public void incrOne(String key) {
        jedisCluster.incr(key);
    }

    /**
     * 命令在指定的 key 不存在时，为 key 设置指定的值
     *
     * @param key   key
     * @param value 值
     */
    public void setnx(String key, String value) {
        jedisCluster.setnx(key, value);
    }

    /**
     * 设置String数据类型
     *
     * @param key   key
     * @param value 值
     * @return
     */
    public void set(String key, String value) {
        jedisCluster.set(key, value);
    }

    /**
     * 获取String数据类型
     *
     * @param key key
     * @return
     */
    public String get(String key) {
        return jedisCluster.get(key);
    }

    /**
     * 根据索引查询列表
     *
     * @param key   key
     * @param index 索引
     */
    public String getListByIndex(String key, long index) {
        return jedisCluster.lindex(key, index);
    }

    /**
     * 根据索引向列表添加数据
     *
     * @param key   key
     * @param index 索引
     * @param value 值
     */
    public void setListByIndex(String key, long index, String value) {
        jedisCluster.lset(key, index, value);
    }


}
