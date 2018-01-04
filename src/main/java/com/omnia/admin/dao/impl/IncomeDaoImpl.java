package com.omnia.admin.dao.impl;

import com.omnia.admin.dao.IncomeDao;
import com.omnia.admin.model.Income;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@AllArgsConstructor
public class IncomeDaoImpl implements IncomeDao {
    private static final String SAVE_INCOME = "INSERT INTO adverts_income " +
            "(adv_id, date, sum_total, sum_com, sum_bank, fin_account_id, currency_id) VALUES (?, ?, ?, ?, ?, ?, ?);";
    private final JdbcTemplate jdbcTemplate;

    @Override
    public void save(Income income) {
        jdbcTemplate.update(SAVE_INCOME, income.getAdvertiserId(), income.getDate(), income.getTotal(), income.getCommission(),
                income.getBank(), income.getAccountId(), income.getCurrencyId());
    }
}
