package org.wmframework.permissions;

import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
public class PermissionRegister {

    //服务名
    private String serviceName;

    //需要权限控制的map，key是请求URL value暂时
    private Map<String, InterfaceDefinition> permissionsMap;


}
