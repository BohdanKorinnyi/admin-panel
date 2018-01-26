package com.omnia.admin.dao.impl;

import com.omnia.admin.dao.PaymentDao;
import com.omnia.admin.model.Payment;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@AllArgsConstructor
public class PaymentDaoImpl implements PaymentDao {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Payment> getPayrollsByBuyerAndYear(int buyerId, int year) {
        return jdbcTemplate.query("SELECT " +
                        "  monthname(buyer_payments.date) AS 'month', " +
                        "  sum(buyer_payments.sum)        AS 'sum', " +
                        "  buyer_payments.date, " +
                        "  currency.code " +
                        "FROM buyer_payments " +
                        "  LEFT JOIN currency ON buyer_payments.currency_id = currency.id " +
                        "  WHERE buyer_payments.buyer_id = ? AND year(buyer_payments.date) = ? " +
                        "GROUP BY buyer_payments.date, monthname(buyer_payments.date);", BeanPropertyRowMapper.newInstance(Payment.class),
                buyerId, year);
    }
}
