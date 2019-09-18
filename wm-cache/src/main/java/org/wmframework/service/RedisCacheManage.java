package org.wmframework.service;

import java.util.concurrent.TimeUnit;

public interface RedisCacheManage extends CacheManage {

    /**
     * 存入缓存并设置默认超时时间
     *
     * @param key   key值
     * @param value 缓存值
     */
    public void setDefaultExpire(String key, String value);

    /**
     * 存入缓存并设置随机超时时间
     * 防止缓存同时到期造成的缓存雪崩
     *
     * @param key   key值
     * @param value 缓存值
     */
    public void setRandomExpire(String key, String value);

    /**
     * 更改缓存的超时时间
     *
     * @param key      key值
     * @param timeout  超时时间
     * @param timeUnit 实践单位
     * @return true:更新成功 false:更新失败
     */
    boolean updateExpire(String key, long timeout, TimeUnit timeUnit);

}
