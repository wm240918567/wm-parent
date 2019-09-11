package org.wmframework.crud;

import org.wmframework.dto.PageBaseDto;

import java.util.List;

/**
 * 查询接口，提供基础的条件、分页、全部查询功能
 *
 * @author: 王锰
 * @date: 2019/9/11
 */
public interface Retrieve<E> {

    <D extends PageBaseDto> List<E> conditionsQuery(D dto);

}
