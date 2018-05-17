/**
 * 
 */
/**
 * @author asus
 *
 */
package com.hifs.hifs.service.monitor;
import org.springframework.data.jpa.repository.JpaRepository;

import com.hifs.hifs.entity.Monitor;

public interface IMonitorService  extends JpaRepository<Monitor, Integer> {

}