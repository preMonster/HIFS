package com.hifs.hifs.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Illegal {
	
	@GeneratedValue
	@Id
	private Integer id;
	
	private String name;
	
	private String punish;

    public Illegal() {
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

	public String getPunish() {
		return punish;
	}

	public void setPunish(String punish) {
		this.punish = punish;
	}
}
