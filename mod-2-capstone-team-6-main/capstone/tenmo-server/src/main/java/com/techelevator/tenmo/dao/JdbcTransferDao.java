package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcTransferDao implements TransferDao{

    private JdbcTemplate jdbcTemplate;
    public JdbcTransferDao(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void makeTransfer(Transfer transfer){

        String sql = "INSERT INTO transfer (transfer_type_id, transfer_status_id, account_from, account_to, amount) " +
                "VALUES (?, ?, ?, ?, ?)";

       try{
            jdbcTemplate.update(sql, transfer.getTransferTypeId(), transfer.getTransferStatusId(), transfer.getAccountFrom(), transfer.getAccountTo(), transfer.getAmount());

        }catch (DataAccessException dae){

        }

    }


    @Override
    public List<Transfer> getTransfers(int userId) {
        List<Transfer> transferList = new ArrayList<>();

        String sql = "SELECT t.transfer_id, t.transfer_type_id, tt.transfer_type_desc, t.transfer_status_id, ts.transfer_status_desc, t.account_from, a.user_id AS user_from, t.account_to, ax.user_id AS user_to, t.amount FROM transfer as t " +
            "JOIN account as a ON t.account_from = a.account_id " +
            "JOIN account as ax ON t.account_to = ax.account_id " +
            "INNER JOIN transfer_status AS ts ON t.transfer_status_id = ts.transfer_status_id " +
            "INNER JOIN transfer_type AS tt ON t.transfer_type_id = tt.transfer_type_id " +
            "WHERE a.user_id = ? OR ax.user_id = ?;";

        try{

            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId, userId);

            while(results.next()){

                Transfer transfer = mapRowToTransfer(results);

                transferList.add(transfer);

            }

        } catch (DataAccessException dae){}


        return transferList;

    }

    @Override
    public void updateTransfer(Transfer transfer, int id){

        String sql = "UPDATE transfer SET transfer_status_id = ? WHERE transfer_id = ?;";

        jdbcTemplate.update(sql, transfer.getTransferStatusId(), transfer.getTransferId());

    }


    private Transfer mapRowToTransfer(SqlRowSet rowSet){

        Transfer transfer = new Transfer();

        transfer.setAccountFrom(rowSet.getInt("account_from"));
        transfer.setAccountTo(rowSet.getInt("account_to"));
        transfer.setAmount(rowSet.getBigDecimal("amount"));
        transfer.setTransferId(rowSet.getInt("transfer_id"));
        transfer.setTransferStatusId(rowSet.getInt("transfer_status_id"));
        transfer.setTransferTypeId(rowSet.getInt("transfer_type_id"));
        transfer.setTransferTypeDesc(rowSet.getString("transfer_type_desc"));
        transfer.setTransferStatusDesc(rowSet.getString("transfer_status_desc"));

        return transfer;
    }
}
