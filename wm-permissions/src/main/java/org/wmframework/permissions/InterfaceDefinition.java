package org.wmframework.permissions;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class InterfaceDefinition {

    /**
     * 接口名称
     */
    private String name;

    /**
     * true:支持动态更新  false:不支持动态更新
     */
    private boolean dynamic;

    /**
     * 角色
     */
    private Role role;

}
