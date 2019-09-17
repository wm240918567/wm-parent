package org.wmframework.util;

import org.springframework.util.Assert;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * bean工具类
 *
 * @author 王锰
 * @date 11:02 2019/6/19
 */
public class BeanUtils {

    /**
     * dto-》po
     * 约定：
     * 1，dto和po中对应的属性名，类型，必须一致
     * 2，getter/setter方法自动生成驼峰结构
     * 当检测到传入的属性和po中属性匹配时，反射获取属性值
     * null值也会被赋值
     *
     * @param dto 传入对象
     * @param po  赋值后的对象
     * @param <T> 入参对象类型
     * @param <D> 赋值后对象类型
     * @return 赋值后的对象
     */
    public static <T, D> D convert(T dto, D po) {
        checkParamter(dto, po);
        List<Field> dtoFieldList = recursive(new ArrayList<>(), dto.getClass());
        List<Field> poFieldList = recursive(new ArrayList<>(), po.getClass());

        dtoFieldList.stream().filter(fd -> !Modifier.isStatic(fd.getModifiers())).peek(p -> p.setAccessible(true)).forEach(fd -> {
            poFieldList.stream().filter(fc -> !Modifier.isStatic(fc.getModifiers())).peek(p -> p.setAccessible(true)).forEach(fc -> {
                if (fd.getName().equals(fc.getName()) && fd.getType().equals(fc.getType())) {
                    try {
                        fc.set(po, fd.get(dto));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        });
        return po;
    }

    /**
     * dto-》po
     * 不包括null值的复制
     * 约定：
     * 1，dto和po中对应的属性名，类型，必须一致
     * 2，getter/setter方法自动生成驼峰结构
     * 当检测到传入的属性和po中属性匹配时，反射获取属性值
     * 忽略NULL值
     *
     * @param dto 传入对象
     * @param po  赋值后的对象
     * @param <T> 入参对象类型
     * @param <D> 赋值后对象类型
     * @return 赋值后的对象
     */
    public static <T, D> D convertIgnoreNull(T dto, D po) {
        checkParamter(dto, po);
        List<Field> dtoFieldList = recursive(new ArrayList<>(), dto.getClass());
        List<Field> poFieldList = recursive(new ArrayList<>(), po.getClass());

        dtoFieldList.stream().filter(fd -> !Modifier.isStatic(fd.getModifiers())).peek(p -> p.setAccessible(true)).forEach(fd -> {
            poFieldList.stream().filter(fc -> !Modifier.isStatic(fc.getModifiers())).peek(p -> p.setAccessible(true)).forEach(fc -> {
                try {
                    if (null != fd.get(dto)) {
                        if (fd.getName().equals(fc.getName()) && fd.getType().equals(fc.getType())) {
                            try {
                                fc.set(po, fd.get(dto));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            });
        });
        return po;
    }

    /**
     * 校验入参
     *
     * @param dto dto对象
     * @param po  po对象
     * @param <T> 入参泛型
     * @param <D> po泛型
     */
    private static <T, D> void checkParamter(T dto, D po) {
        Assert.notNull(dto, "dto is null");
        Assert.notNull(po, "po is null");
    }

    /**
     * 递归获取字段，包含父类
     *
     * @param list  fieldjlist
     * @param clazz 类型
     * @return fieldjlist
     */
    public static List<Field> recursive(List<Field> list, Class<?> clazz) {
        if (clazz != null) {
            list.addAll(Arrays.asList(clazz.getDeclaredFields()));
            return recursive(list, clazz.getSuperclass());
        } else {
            return list;
        }
    }

}
