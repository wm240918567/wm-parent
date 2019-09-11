package org.wmframework.crud;

import java.util.List;

/**
 * 新建接口
 *
 * @author: 王锰
 * @date: 2019/9/11
 */
public interface Create<E> {

    /**
     * 新建操作入参必定为数据库操作对象
     *
     * @param po 数据库操作对象
     * @return true:成功  false:失败
     */
    boolean add(E po);

    /**
     * 批量添加
     *
     * @param poList 对象列表
     * @return true:成功  false:失败
     */
    boolean batchAdd(List<E> poList);

}
