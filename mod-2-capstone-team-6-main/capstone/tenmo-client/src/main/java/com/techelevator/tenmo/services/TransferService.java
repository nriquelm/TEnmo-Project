package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.util.BasicLogger;
import org.springframework.http.*;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

public class TransferService {

    public static final String API_BASE_URL = "http://localhost:8080/transfer/";
    private RestTemplate restTemplate = new RestTemplate();

    private String authToken = null;
    public void setAuthToken(String authToken){
        this.authToken = authToken;
    }

    private HttpEntity<Void> makeAuthEntity(){
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authToken);
        return new HttpEntity<>(headers);
    }

    private HttpEntity<Transfer> makeTransferEntity(Transfer transfer){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(authToken);
        return new HttpEntity<>(transfer, headers);
    }

    public Transfer[] getTransfers(int userId){
        Transfer[] transfers = null;

        try{
            ResponseEntity<Transfer[]> response =
                    restTemplate.exchange(API_BASE_URL + userId, HttpMethod.GET, makeAuthEntity(), Transfer[].class);
            transfers = response.getBody();
        }catch (RestClientResponseException | ResourceAccessException rae){
            BasicLogger.log(rae.getMessage());
        }

        return transfers;
    }

    public void makeTransfer(Transfer transfer) {
        HttpEntity<Transfer> entity = makeTransferEntity(transfer);
        try {
            restTemplate.postForObject(API_BASE_URL, entity, Transfer.class);

        }catch (RestClientResponseException | ResourceAccessException rae){
            BasicLogger.log(rae.getMessage());
        }

    }

    public boolean updateTransfer(Transfer transfer) {
        boolean success = false;
        HttpEntity<Transfer> entity = makeTransferEntity(transfer);
        try {
            restTemplate.put(API_BASE_URL + transfer.getTransferId(), entity);
            success = true;
        }catch (RestClientResponseException | ResourceAccessException rae){
            BasicLogger.log(rae.getMessage());
        }
        return success;
    }


}
