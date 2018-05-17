package com.hifs.hifs.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Role {

    @GeneratedValue
    @Id
    private Integer id;

    private String name;

    private String hifs_describe;

    public Role() {
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

    public String getHifs_describe() {
        return hifs_describe;
    }

    public void setHifs_describe(String hifs_describe) {
        this.hifs_describe = hifs_describe;
    }
}
