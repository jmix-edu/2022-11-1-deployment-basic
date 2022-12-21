package com.company.tm.security;

import com.company.tm.entity.Project;
import io.jmix.security.model.EntityAttributePolicyAction;
import io.jmix.security.model.EntityPolicyAction;
import io.jmix.security.role.annotation.EntityAttributePolicy;
import io.jmix.security.role.annotation.EntityPolicy;
import io.jmix.security.role.annotation.ResourceRole;
import io.jmix.securityui.role.annotation.MenuPolicy;
import io.jmix.securityui.role.annotation.ScreenPolicy;

@ResourceRole(name = "ProjectManagement", code = "project-management")
public interface ProjectManagementRole {
    @EntityAttributePolicy(entityClass = Project.class, attributes = {"description", "name", "version", "createdBy", "createdDate", "lastModifiedDate", "lastModifiedBy", "deletedBy", "deletedDate", "id"}, action = EntityAttributePolicyAction.MODIFY)
    @EntityPolicy(entityClass = Project.class, actions = EntityPolicyAction.ALL)
    void project();

    @MenuPolicy(menuIds = "tm_Project.browse")
    @ScreenPolicy(screenIds = {"tm_Project.browse", "tm_Project.edit"})
    void screens();
}