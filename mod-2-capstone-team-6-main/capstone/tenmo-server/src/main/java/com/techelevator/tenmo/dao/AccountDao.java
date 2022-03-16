package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;

import java.math.BigDecimal;

public interface AccountDao {

    public Account getAccountByUserId(int id);

    public void updateBalance(Account account, int id);

}
