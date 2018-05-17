package com.hifs.hifs.service.roleAssign;

import com.hifs.hifs.entity.RoleAssign;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IRoleAssignService extends JpaRepository<RoleAssign, Integer> {

    public List<RoleAssign> findByRoleId(Integer roleId);

    public List<RoleAssign> findByModuleId(Integer moduleId);

}
