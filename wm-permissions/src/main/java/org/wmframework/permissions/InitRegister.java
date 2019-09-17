package org.wmframework.permissions;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.MethodMetadata;
import org.springframework.core.type.classreading.AnnotationMetadataReadingVisitor;
import org.springframework.core.type.classreading.MethodMetadataReadingVisitor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 1，获取到当前system能提供的全部接口
 * 2，筛选出需要配置权限的接口，发送给路由
 */
@Slf4j
@Component
public class InitRegister implements ApplicationRunner {

    private static final String BASE_PACKAGE = "com.wm.demo";

    private static final String PERMISSIONS = "org.wmframework.permissions.Permissions";
    private static final String REQUEST_MAPPING = "org.springframework.web.bind.annotation.RequestMapping";
    private static final String GET_MAPPING = "org.springframework.web.bind.annotation.GetMapping";
    private static final String POST_MAPPING = "org.springframework.web.bind.annotation.PostMapping";
    private static final String PUT_MAPPING = "org.springframework.web.bind.annotation.PutMapping";
    private static final String DELETE_MAPPING = "org.springframework.web.bind.annotation.DeleteMapping";

    private static final String SEND_URL = "http://127.0.0.1:8201/register";

    @Value(value = "${spring.application.name:test系统}")
    private String serviceName;

    @Autowired
    private ClassScanner classScanner;

    @Override
    public void run(ApplicationArguments args) {
        Map<String, InterfaceDefinition> registerMap = new ConcurrentHashMap<>();
        Set<BeanDefinition> beanDefinitionSet = classScanner.scanSpecifiedAnnotationAtSpecifiedPackage(Controller.class, BASE_PACKAGE);
        for (BeanDefinition beanDefinition : beanDefinitionSet) {
            String classRequestMappingValue = getClassRequestMappingValue(beanDefinition);
            Set<MethodMetadata> permissionMethodSet = getMethodMetadataSetAtSpecialAnnotation(beanDefinition, PERMISSIONS);
            for (MethodMetadata permissionMethodMetadata : permissionMethodSet) {
                MethodMetadataReadingVisitor methodMetadataReadingVisitor = (MethodMetadataReadingVisitor) permissionMethodMetadata;
                AnnotationAttributes permissions = methodMetadataReadingVisitor.getAnnotationAttributes(PERMISSIONS);
                assert permissions != null;
                String name = getInterfaceName(methodMetadataReadingVisitor, permissions);
                boolean dynamic = isDynamic(permissions);
                Role role = getRole(permissions);
                String uri = getInterfaceUri(classRequestMappingValue, methodMetadataReadingVisitor);
                boolean checkRepeat = registerMap.containsKey(uri);
                if (checkRepeat)
                    continue;
                registerMap.put(uri, InterfaceDefinition.builder().name(name).role(role).dynamic(dynamic).build());
            }
        }
        PermissionRegister register = PermissionRegister.builder().serviceName(serviceName).permissionsMap(registerMap).build();
        String registerStr = JSONObject.toJSONString(register);
        log.info(registerStr);
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        HttpEntity<String> entity = new HttpEntity<>(registerStr, headers);
        restTemplate.postForEntity(SEND_URL, entity, String.class);
    }

    private Role getRole(AnnotationAttributes permissions) {
        return (Role) permissions.get("role");
    }

    /**
     * @param classRequestMappingValue     类@requestMapping值
     * @param methodMetadataReadingVisitor 方法元数据
     * @return 获取完整请求URI
     */
    private String getInterfaceUri(String classRequestMappingValue, MethodMetadataReadingVisitor methodMetadataReadingVisitor) {
        String path = getPath(methodMetadataReadingVisitor);
        return null == path ? "" : classRequestMappingValue + path;
    }

    private boolean isDynamic(AnnotationAttributes permission) {
        return (boolean) (Boolean) permission.get("dynamic");
    }

    /**
     * @param methodMetadataReadingVisitor 方法元数据
     * @param permission                   注解信息
     * @return 获取接口名
     */
    private String getInterfaceName(MethodMetadataReadingVisitor methodMetadataReadingVisitor, AnnotationAttributes permission) {
        return "".equals(permission.get("name")) ? methodMetadataReadingVisitor.getMethodName() : (String) permission.get("name");
    }

    /**
     * @param methodMetadataReadingVisitor 方法元数据
     * @return value/path值
     */
    private String getPath(MethodMetadataReadingVisitor methodMetadataReadingVisitor) {
        AnnotationAttributes requestMapping = methodMetadataReadingVisitor.getAnnotationAttributes(REQUEST_MAPPING);
        AnnotationAttributes getMapping = methodMetadataReadingVisitor.getAnnotationAttributes(GET_MAPPING);
        AnnotationAttributes postMapping = methodMetadataReadingVisitor.getAnnotationAttributes(POST_MAPPING);
        AnnotationAttributes putMapping = methodMetadataReadingVisitor.getAnnotationAttributes(PUT_MAPPING);
        AnnotationAttributes deleteMapping = methodMetadataReadingVisitor.getAnnotationAttributes(DELETE_MAPPING);
        String path = null;
        if (null != getMapping)
            path = ((String[]) getMapping.get("value"))[0];
        if (null != postMapping)
            path = ((String[]) postMapping.get("value"))[0];
        if (null != putMapping)
            path = ((String[]) putMapping.get("value"))[0];
        if (null != deleteMapping)
            path = ((String[]) deleteMapping.get("value"))[0];
        if (null != requestMapping && null == path)
            path = ((String[]) requestMapping.get("value"))[0];
        return path;
    }

    /**
     * @param beanDefinition 类定义
     * @return 该类请求前缀
     */
    private String getClassRequestMappingValue(BeanDefinition beanDefinition) {
        AnnotatedBeanDefinition annotatedBeanDefinition = (AnnotatedBeanDefinition) beanDefinition;
        AnnotationMetadataReadingVisitor metadata = (AnnotationMetadataReadingVisitor) annotatedBeanDefinition.getMetadata();
        AnnotationAttributes classRequestMappingAnnonation = metadata.getAnnotationAttributes(REQUEST_MAPPING);
        return null == classRequestMappingAnnonation ? "" : ((String[]) classRequestMappingAnnonation.get("value"))[0];
    }

    /**
     * @param beanDefinition 通过扫描器获得的beanDefinition对象
     * @return 有指定注解的方法元数据Set集合
     */
    private Set<MethodMetadata> getMethodMetadataSetAtSpecialAnnotation(BeanDefinition beanDefinition, String annotation) {
        AnnotatedBeanDefinition annotatedBeanDefinition = (AnnotatedBeanDefinition) beanDefinition;
        AnnotationMetadataReadingVisitor metadata = (AnnotationMetadataReadingVisitor) annotatedBeanDefinition.getMetadata();
        return metadata.getAnnotatedMethods(annotation);
    }


}
