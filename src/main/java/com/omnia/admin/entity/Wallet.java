package com.omnia.admin.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Setter
@Getter
@Table(name = "buyer_wallet")
public class Wallet {
    @Id
    private Long id;
    @ManyToOne
    @JoinColumn(name = "buyer_id")
    private Buyer buyer;
    private String number;
    private String description;
}
