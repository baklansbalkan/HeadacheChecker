package org.baklansbalkan.HeadacheChecker.dto;

import org.baklansbalkan.HeadacheChecker.models.RoleEnum;

public class RoleDTO {
    private Integer id;
    private RoleEnum name;

    public RoleDTO(int id, RoleEnum name) {
        this.name = name;
    }

    public RoleDTO() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public RoleEnum getName() {
        return name;
    }

    public void setName(RoleEnum name) {
        this.name = name;
    }
}

