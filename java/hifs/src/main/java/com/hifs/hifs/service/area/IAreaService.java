/**
 * 
 */
/**
 * @author wangchao
 *
 */
package com.hifs.hifs.service.area;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hifs.hifs.entity.Area;

public interface IAreaService extends JpaRepository<Area, Integer> {

    public List<Area> findByIsMonitor(String isMonitor);
}