package com.techelevator.tenmo.model;

import java.math.BigDecimal;

public class Transfer {

    private int transferId;
    private int transferTypeId;
    private String transferTypeDesc;
    private int transferStatusId;
    private String transferStatusDesc;
    private int accountFrom;
    private int accountTo;
    private BigDecimal amount;
    private int userId;

    //CONSTRUCTORS
    public Transfer() {}

    public Transfer(int transferId, int transferTypeId, String transferTypeDesc, int transferStatusId, String transferStatusDesc, int accountFrom, int accountTo, BigDecimal amount) {
        this.transferId = transferId;
        this.transferTypeId = transferTypeId;
        this.transferTypeDesc = transferTypeDesc;
        this.transferStatusId = transferStatusId;
        this.transferStatusDesc = transferStatusDesc;
        this.accountFrom = accountFrom;
        this.accountTo = accountTo;
        this.amount = amount;
    }

    //GETTERS
    public int getTransferId() {
        return transferId;
    }

    public int getTransferTypeId() {
        return transferTypeId;
    }

    public String getTransferTypeDesc() {
        return transferTypeDesc;
    }

    public int getTransferStatusId() {
        return transferStatusId;
    }

    public String getTransferStatusDesc() {
        return transferStatusDesc;
    }

    public int getAccountFrom() {
        return accountFrom;
    }

    public int getAccountTo() {
        return accountTo;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public int getUserId() {
        return userId;
    }

    //SETTERS
    public void setTransferId(int transferId) {
        this.transferId = transferId;
    }

    public void setTransferTypeId(int transferTypeId) {
        this.transferTypeId = transferTypeId;
    }

    public void setTransferTypeDesc(String transferTypeDesc) {
        this.transferTypeDesc = transferTypeDesc;
    }

    public void setTransferStatusId(int transferStatusId) {
        this.transferStatusId = transferStatusId;
    }

    public void setTransferStatusDesc(String transferStatusDesc) {
        this.transferStatusDesc = transferStatusDesc;
    }

    public void setAccountFrom(int accountFrom) {
        this.accountFrom = accountFrom;
    }

    public void setAccountTo(int accountTo) {
        this.accountTo = accountTo;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
