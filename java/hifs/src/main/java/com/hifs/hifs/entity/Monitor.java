package com.hifs.hifs.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Monitor {
	
	@GeneratedValue
	@Id
	private Integer id;
	
	private String name;
	
	private String isRun;
	
	private String areaId;
	
	private String mtorDescribe;

    public Monitor() {
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

	public String getIsRun() {
		return isRun;
	}

	public void setIsRun(String isRun) {
		this.isRun = isRun;
	}

	public String getAreaId() {
		return areaId;
	}

	public void setAreaId(String areaId) {
		this.areaId = areaId;
	}

	public String getMtorDescribe() {
		return mtorDescribe;
	}

	public void setMtorDescribe(String mtorDescribe) {
		this.mtorDescribe = mtorDescribe;
	}

}
