package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcAccountDao implements AccountDao{

    private JdbcTemplate jdbcTemplate;

    public static List<Account> accounts = new ArrayList<>();

    public JdbcAccountDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Account getAccountByUserId(int userId){
        Account account = null;
        String sql = "SELECT account_id, user_id, balance FROM account WHERE user_id = ?";

        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId);

        if(results.next()) {
            account = mapRowToAccount(results);
        }
        return account;
    }

    @Override
    public void updateBalance(Account account, int id){

        String sql = "UPDATE account SET balance = ? WHERE account_id = ?";

        jdbcTemplate.update(sql, account.getBalance(), account.getAccountId());
    }

    private Account mapRowToAccount(SqlRowSet rs){
            return new Account(rs.getInt("account_id"), rs.getInt("user_id"), rs.getBigDecimal("balance"));
    }
}
