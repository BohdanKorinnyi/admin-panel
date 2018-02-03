package com.omnia.admin.dao.impl;

import com.omnia.admin.dao.WalletDao;
import com.omnia.admin.model.Wallet;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@AllArgsConstructor
public class WalletDaoImpl implements WalletDao {
    private static final String SELECT_WALLET = "SELECT * FROM buyer_wallet";
    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Wallet> findAll() {
        return jdbcTemplate.query(SELECT_WALLET, BeanPropertyRowMapper.newInstance(Wallet.class));
    }
}
