package com.techelevator.tenmo.controller;


import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@PreAuthorize("isAuthenticated()")
@RestController
public class AccountController {

    private AccountDao accountDao;
    private UserDao userDao;
    private TransferDao transferDao;

    public AccountController(AccountDao accountDao, UserDao userDao, TransferDao transferDao){
        this.accountDao = accountDao;
        this.userDao = userDao;
        this.transferDao = transferDao;
    }

    @RequestMapping(value = "/balance/{id}", method = RequestMethod.GET)
    public Account getBalance(@PathVariable int id) throws UsernameNotFoundException {

        return accountDao.getAccountByUserId(id);

    }

    @RequestMapping(value = "/user", method = RequestMethod.GET)
    public List<User> list(){

        return userDao.findAll();

    }

    @RequestMapping(value = "/balance/{id}", method = RequestMethod.PUT)
    public void updateBalance(@RequestBody Account account, @PathVariable int id){

        accountDao.updateBalance(account, id);

    }

    @RequestMapping(value = "/transfer", method = RequestMethod.POST)
    public void makeTransfer(@RequestBody Transfer transfer){

        transferDao.makeTransfer(transfer);

    }

    @RequestMapping(value = "/transfer/{id}", method = RequestMethod.PUT)
    public void updateTransfer(@RequestBody Transfer transfer, @PathVariable int id) {

        transferDao.updateTransfer(transfer, id);

    }

    @RequestMapping(value = "/transfer/{userId}", method = RequestMethod.GET)
    public List<Transfer> list(@PathVariable int userId){

        return transferDao.getTransfers(userId);

    }

    @RequestMapping(value = "/user/{id}", method = RequestMethod.GET)
    public User getUsernameByAcctId(@PathVariable int id){

        return userDao.getUsernameByAcctId(id);

    }

}
