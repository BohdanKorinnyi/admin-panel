package com.omnia.admin.dao.impl;

import com.omnia.admin.dao.PaymentDao;
import com.omnia.admin.model.Payment;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.util.List;

import static org.springframework.jdbc.core.BeanPropertyRowMapper.newInstance;

@Repository
@AllArgsConstructor
public class PaymentDaoImpl implements PaymentDao {
    private static final String SQL_PARAMETER_BUYER_ID = "buyerId";
    private static final String SQL_PARAMETER_YEAR = "year";
    private static final String SELECT_PAYMENT_BY_BUYER_AND_YEAR = "SELECT " +
            "  monthname(buyer_payments.date) AS 'month', " +
            "  sum(buyer_payments.sum)        AS 'sum', " +
            "  buyer_payments.date, " +
            "  currency.code " +
            "FROM buyer_payments " +
            "  LEFT JOIN currency ON buyer_payments.currency_id = currency.id " +
            "  WHERE buyer_payments.buyer_id = :buyerId AND year(buyer_payments.date) = :year " +
            "GROUP BY buyer_payments.date, monthname(buyer_payments.date);";

    private static final String SELECT_PAYMENT_BY_BUYER = "SELECT buyer_payments.*, currency.code " +
            "FROM buyer_payments " +
            "  LEFT JOIN currency ON buyer_payments.currency_id = currency.id " +
            "WHERE IF(ISNULL(:buyerIds), TRUE, buyer_payments.buyer_id IN (:buyerId));";

    private static final String INSERT_PAYMENT = "INSERT INTO buyer_payments (buyer_id, staff_id, date, date_payroll, sum, currency_id, type_id, wallet_id) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?);";

    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public List<Payment> getByBuyerAndYear(int buyerId, int year) {
        MapSqlParameterSource source = new MapSqlParameterSource();
        source.addValue(SQL_PARAMETER_BUYER_ID, buyerId);
        source.addValue(SQL_PARAMETER_YEAR, year);
        return namedParameterJdbcTemplate.query(SELECT_PAYMENT_BY_BUYER_AND_YEAR, source, newInstance(Payment.class));
    }

    @Override
    public List<Payment> getByBuyerIds(List<Integer> buyerIds) {
        MapSqlParameterSource source = new MapSqlParameterSource();
        source.addValue(SQL_PARAMETER_BUYER_ID, CollectionUtils.isEmpty(buyerIds) ? null : buyerIds);
        return namedParameterJdbcTemplate.query(SELECT_PAYMENT_BY_BUYER, source, newInstance(Payment.class));
    }

    @Override
    public void save(List<Payment> payments) {
        jdbcTemplate.batchUpdate(INSERT_PAYMENT, new BatchPreparedStatementSetter() {
            @Override
            @SneakyThrows
            public void setValues(PreparedStatement preparedStatement, int i) {
                preparedStatement.setInt(1, payments.get(i).getBuyerId());
                preparedStatement.setInt(2, payments.get(i).getStaffId());
                preparedStatement.setString(3, payments.get(i).getDate());
                preparedStatement.setString(4, payments.get(i).getDatePayroll());
                preparedStatement.setFloat(5, payments.get(i).getSum());
                preparedStatement.setInt(6, payments.get(i).getCurrencyId());
                preparedStatement.setInt(7, payments.get(i).getTypeId());
                preparedStatement.setInt(8, payments.get(i).getWalletId());
            }

            @Override
            public int getBatchSize() {
                return payments.size();
            }
        });
    }
}
