package com.kuaizhan.dao.redis;

/**
 * Created by Mr.Jadyn on 2017/1/19.
 */
public interface RedisBaseDao<T> {
    /**
     * 判断数据是否存在
     *
     * @return
     */
    boolean exist(String key);

    boolean exist(String key, String field);

    /**
     * 获取剩余时间
     *
     * @param key
     * @return
     */
    long getTtl(String key);

    /**
     * 与redis里面的值做比较
     *
     * @param key
     * @param str
     * @return
     */
    boolean equal(String key, String str);

    /**
     * 刷新数据
     *
     * @return
     */
    void setData(String key, String value, int expire);

    void setData(String key, String field, String value, int expire);

    /**
     * 获取数据
     *
     * @param key
     * @return
     */
    String getData(String key);

    String getData(String key, String field);

    /**
     * 删除数据
     *
     * @param key
     */
    void deleteData(String key);


}
