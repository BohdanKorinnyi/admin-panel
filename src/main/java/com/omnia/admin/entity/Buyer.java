package com.omnia.admin.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "buyers")
public class Buyer {
    @Id
    private Long id;
    private String name;
    private String type;
    private String comment;
    private String init;

    @OneToMany(mappedBy = "buyer", fetch = FetchType.LAZY)
    private List<Wallet> wallets;
}
