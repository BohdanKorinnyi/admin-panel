package com.omnia.admin.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@Table(name = "staff")
public class Staff {
    private Long id;

    private String firstName;
    private String lastName;
    private LocalDate firstDay;
    private LocalDate lastDay;
    private StaffType staffType;
    private Group group;
    private Buyer buyer;
}
