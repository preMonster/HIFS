package com.hifs.hifs.service.role;

import com.hifs.hifs.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IRoleServer  extends JpaRepository<Role, Integer> {

    public List<Role> findByName(String name);

}
