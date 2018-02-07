package com.omnia.admin.dao;

import com.omnia.admin.model.Wallet;

import java.util.List;

public interface WalletDao {
    List<Wallet> findAll();

    List<Wallet> findByStaffId(int staffId);
}
