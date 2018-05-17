package com.hifs.hifs.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Area {
	
	@GeneratedValue
	@Id
	private Integer id;
	
	private String name;
	
	private String isMonitor;
	
	private String areaDescribe;

    public Area() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIsMonitor() {
		return isMonitor;
	}

	public void setIsMonitor(String isMonitor) {
		this.isMonitor = isMonitor;
	}

	public String getAreaDescribe() {
		return areaDescribe;
	}

	public void setAreaDescribe(String areaDescribe) {
		this.areaDescribe = areaDescribe;
	}
}
