package com.omnia.admin.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@Table(name = "staff_type")
public class StaffType {
    private Long id;
    private String name;
}
