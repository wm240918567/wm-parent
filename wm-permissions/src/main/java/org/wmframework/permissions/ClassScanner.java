package org.wmframework.permissions;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.util.Set;

/**
 * 类扫描器
 *
 * @author: 王锰
 * @date: 2019/9/17
 */
@Component
public class ClassScanner {

    /**
     * @param clazz       指定注解
     * @param basePackage 指定包路径
     * @return 扫描指定包中的指定注释类的BeanDefinitionSet集合
     */
    public Set<BeanDefinition> scanSpecifiedAnnotationAtSpecifiedPackage(Class<? extends Annotation> clazz, String basePackage) {
        ClassPathScanningCandidateComponentProvider provider = new ClassPathScanningCandidateComponentProvider(false);
        provider.addIncludeFilter(new AnnotationTypeFilter(clazz));
        return provider.findCandidateComponents(basePackage);
    }

}
