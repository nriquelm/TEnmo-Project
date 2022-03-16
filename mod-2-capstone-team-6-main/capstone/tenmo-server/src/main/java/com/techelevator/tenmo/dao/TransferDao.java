package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;

import java.math.BigDecimal;
import java.util.List;

public interface TransferDao {

    public void makeTransfer(Transfer transfer);

    public List<Transfer> getTransfers(int userId);

    public void updateTransfer(Transfer transfer, int id);

}
