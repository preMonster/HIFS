/**
 * 
 */
/**
 * @author asus
 *
 */
package com.hifs.hifs.service.record;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hifs.hifs.entity.Monitor;
import com.hifs.hifs.entity.Record;

public interface IRecordService  extends JpaRepository<Record, Integer> {

}