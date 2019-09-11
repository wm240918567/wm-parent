package org.wmframework.crud;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.wmframework.dto.PageBaseDto;

import java.util.ArrayList;
import java.util.List;

public abstract class UserService<E, M extends BaseMapper<E>> implements Create<E>, Retrieve<E> {

    @Autowired
    private M mapper;

    @Override
    public boolean add(E po) {
        int insert = mapper.insert(po);
        return insert == 1;
    }

    /**
     * 分页条件查询
     * 分页全部查询
     * 条件查询
     * 全部查询
     * 下拉列表查询
     *
     * @param dto 查询条件
     * @param <D> 泛型
     * @return 返回列表
     */
    @Override
    public <D extends PageBaseDto> List<E> conditionsQuery(D dto) {
        //分页查询
        page(dto);
        //条件查询
        LambdaQueryWrapper<E> lambdaQueryWrapper = new QueryWrapper<E>().lambda();
        getCoditions(lambdaQueryWrapper);
        List<E> es = mapper.selectList(lambdaQueryWrapper);
        if (null == es || es.size() == 0) {
            return new ArrayList<>();
        }
        return es;
    }

    /**
     * 判断是否需要分页，如果大于0则分页；如果-1则为查询全部满足条件内容
     *
     * @param dto 条件对象
     * @param <D> 泛型
     */
    private <D extends PageBaseDto> void page(D dto) {
        if (dto.getPageNum() > 0) {
            PageHelper.startPage(dto.getPageNum(), dto.getPageSize());
        }
    }


    /**
     * 子类实现
     * @param lambdaQueryWrapper 查询条件wrapper
     */
    protected abstract void getCoditions(LambdaQueryWrapper<E> lambdaQueryWrapper);

}
