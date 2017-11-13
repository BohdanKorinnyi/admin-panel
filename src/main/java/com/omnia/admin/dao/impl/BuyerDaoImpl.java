package com.omnia.admin.dao.impl;

import com.omnia.admin.dao.BuyerDao;
import com.omnia.admin.model.Buyer;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@Log4j
@Repository
@AllArgsConstructor
public class BuyerDaoImpl implements BuyerDao {
    private static final String NO_BUYER_NAME = "No buyer name";
    private static final String SELECT_BUYER_NAME_BY_ID = "SELECT name FROM buyers WHERE id = ?;";
    private static final String SELECT_ALL_BUYERS_NAME = "SELECT name FROM buyers ORDER BY name ASC;";
    private static final String SELECT_ALL_BUYERS = "SELECT * FROM buyers ORDER BY name ASC;";
    private static final String INSERT_BUYER = "INSERT INTO buyers (name, type, comment, plan_rev, plan_profit, plan_rev_old, plan_profit_old) VALUES (?, ?, ?, ?, ?, ?, ?);";
    private static final String UPDATE_BUYER = "UPDATE buyers " +
            "SET name = ?, type = ?, comment = ?, plan_rev = ?, plan_profit = ?, plan_rev_old = ?, plan_profit_old = ? " +
            "WHERE id = ?;";

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<String> getBuyersName() {
        return jdbcTemplate.queryForList(SELECT_ALL_BUYERS_NAME, String.class);
    }

    @Override
    public List<Buyer> getBuyers() {
        return jdbcTemplate.query(SELECT_ALL_BUYERS, new BeanPropertyRowMapper<>(Buyer.class));
    }

    @Override
    public void updateBuyers(List<Buyer> buyers) {
        jdbcTemplate.batchUpdate(UPDATE_BUYER, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                preparedStatement.setString(1, buyers.get(i).getName());
                preparedStatement.setString(2, buyers.get(i).getType());
                preparedStatement.setString(3, buyers.get(i).getComment());
                preparedStatement.setInt(4, buyers.get(i).getPlanRev());
                preparedStatement.setInt(5, buyers.get(i).getPlanProfit());
                preparedStatement.setInt(6, buyers.get(i).getPlanRevOld());
                preparedStatement.setInt(7, buyers.get(i).getPlanProfitOld());
                preparedStatement.setLong(8, buyers.get(i).getId());
            }

            @Override
            public int getBatchSize() {
                return buyers.size();
            }
        });
    }

    @Override
    public String getBuyerById(int buyerId) {
        try {
            return jdbcTemplate.queryForObject(SELECT_BUYER_NAME_BY_ID, String.class, buyerId);
        } catch (EmptyResultDataAccessException e) {
            log.error("Not found buyer name by id=" + buyerId + " in database, sql=" + SELECT_BUYER_NAME_BY_ID);
        } catch (Exception e) {
            log.error("Error occurred during getting buyers", e);
        }
        return NO_BUYER_NAME;
    }

    @Override
    public void saveBuyers(List<Buyer> buyers) {
        jdbcTemplate.batchUpdate(INSERT_BUYER, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                preparedStatement.setString(1, buyers.get(i).getName());
                preparedStatement.setString(2, buyers.get(i).getType());
                preparedStatement.setString(3, buyers.get(i).getComment());
                preparedStatement.setInt(4, buyers.get(i).getPlanRev());
                preparedStatement.setInt(5, buyers.get(i).getPlanProfit());
                preparedStatement.setInt(6, buyers.get(i).getPlanRevOld());
                preparedStatement.setInt(7, buyers.get(i).getPlanProfitOld());
            }

            @Override
            public int getBatchSize() {
                return buyers.size();
            }
        });
    }
}
