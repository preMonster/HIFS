package com.hifs.hifs.service.module;

import com.hifs.hifs.entity.Module;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IModuleService  extends JpaRepository<Module, Integer> {

}
