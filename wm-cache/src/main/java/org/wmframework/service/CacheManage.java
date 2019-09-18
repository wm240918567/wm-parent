package org.wmframework.service;

import java.util.concurrent.TimeUnit;

/**
 * 缓存服务组件
 *
 * @author: 王锰
 * @date: 2019/9/18
 */
public interface CacheManage {

    /**
     * 存储缓存
     *
     * @param key      缓存key
     * @param value    缓存值
     * @param timeout  超时时间
     * @param timeUnit 时间单位
     */
    void set(String key, String value, long timeout, TimeUnit timeUnit);

    /**
     * 获取缓存
     *
     * @param key 缓存key
     * @return 缓存值
     */
    String get(String key);

    /**
     * 删除某个缓存key
     *
     * @param key key值
     * @return true:成功  false:失败
     */
    boolean delete(String key);

    /**
     * 更新某个缓存值,并设置超时时间
     *
     * @param key 缓存key
     */
    void update(String key,String value,long timeout,TimeUnit timeUnit);

}
