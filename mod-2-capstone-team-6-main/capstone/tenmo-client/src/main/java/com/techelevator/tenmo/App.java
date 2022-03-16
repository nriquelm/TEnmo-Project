package com.techelevator.tenmo;

import com.techelevator.tenmo.model.*;
import com.techelevator.tenmo.services.*;

import java.math.BigDecimal;

public class App {

    private static final String API_BASE_URL = "http://localhost:8080/";

    private AccountService accountService = new AccountService();
    private final ConsoleService consoleService = new ConsoleService();
    private TransferService transferService = new TransferService();
    private UserService userService = new UserService();
    private final AuthenticationService authenticationService = new AuthenticationService(API_BASE_URL);

    private AuthenticatedUser currentUser;


    public static void main(String[] args) {
        App app = new App();
        app.run();
    }

    private void run() {
        consoleService.printGreeting();
        loginMenu();
        if (currentUser != null) {
            mainMenu();
        }
    }
    private void loginMenu() {
        int menuSelection = -1;
        while (menuSelection != 0 && currentUser == null) {
            consoleService.printLoginMenu();
            menuSelection = consoleService.promptForMenuSelection("Please choose an option: ");
            if (menuSelection == 1) {
                handleRegister();
            } else if (menuSelection == 2) {
                handleLogin();
            } else if (menuSelection != 0) {
                System.out.println("Invalid Selection");
                consoleService.pause();
            }
        }
    }

    private void handleRegister() {
        System.out.println("Please register a new user account");
        UserCredentials credentials = consoleService.promptForCredentials();
        if (authenticationService.register(credentials)) {
            System.out.println("Registration successful. You can now login.");
        } else {
            consoleService.printErrorMessage();
        }
    }

    private void handleLogin() {
        UserCredentials credentials = consoleService.promptForCredentials();
        currentUser = authenticationService.login(credentials);

        if (currentUser == null) {
            consoleService.printErrorMessage();
        }
        if(currentUser.getToken() != null){
            accountService.setAuthToken(currentUser.getToken());
            transferService.setAuthToken(currentUser.getToken());
            userService.setAuthToken(currentUser.getToken());
        } else {
            consoleService.printErrorMessage();
        }
    }

    private void mainMenu() {
        int menuSelection = -1;
        while (menuSelection != 0) {
            consoleService.printMainMenu();
            menuSelection = consoleService.promptForMenuSelection("Please choose an option: ");
            if (menuSelection == 1) {
                viewCurrentBalance();
            } else if (menuSelection == 2) {
                viewTransferHistory();
            } else if (menuSelection == 3) {
                viewPendingRequests();
            } else if (menuSelection == 4) {
                sendBucks();
            } else if (menuSelection == 5) {
                requestBucks();
            } else if (menuSelection == 0) {
                continue;
            } else {
                System.out.println("Invalid Selection");
            }
            consoleService.pause();
        }
    }

	private void viewCurrentBalance() {
        Account account = accountService.getAccount(currentUser.getUser().getId().intValue());
        System.out.println("Current balance is: $" + account.getBalance());
	}

	private void viewTransferHistory() {
        Transfer[] transfers = transferService.getTransfers(currentUser.getUser().getId().intValue());
        int currentUserAcctId = accountService.getAccount(currentUser.getUser().getId().intValue()).getAccountId();

        System.out.println();
        System.out.println("-----------------------------------------------------");
        System.out.println("                   Transfer History                  ");
        System.out.println("-----------------------------------------------------");
        for (Transfer transfer : transfers) {
            if(transfer.getAccountFrom() == currentUserAcctId) {
                System.out.println("Transfer ID: " + transfer.getTransferId() + " || To: " + userService.getUsernameByAcctId(transfer.getAccountTo()).getUsername() + " || Amount: $" + transfer.getAmount());
            } else if (transfer.getAccountTo() == currentUserAcctId){
                System.out.println("Transfer ID: " + transfer.getTransferId() + " || From: " + userService.getUsernameByAcctId(transfer.getAccountFrom()).getUsername() + " || Amount: $" + transfer.getAmount());
            }
        }

        System.out.println();
        int transferIdSelection = consoleService.promptForInt("Please enter transfer ID to view details (0 to cancel): ");
        System.out.println();

        for (Transfer transfer : transfers) {
            if (transferIdSelection == transfer.getTransferId()) {
                System.out.println("-----------------------------------------------------");
                System.out.println("                   Transfer Details                  ");
                System.out.println("-----------------------------------------------------");
                System.out.println("Transfer ID: " + transfer.getTransferId());
                System.out.println("From: " + userService.getUsernameByAcctId(transfer.getAccountFrom()).getUsername());
                System.out.println("To: " + userService.getUsernameByAcctId(transfer.getAccountTo()).getUsername());
                System.out.println("Type: " + transfer.getTransferTypeDesc());
                System.out.println("Status: " + transfer.getTransferStatusDesc());
                System.out.println("Amount: $" + transfer.getAmount());
            }
        }
	}

	private void viewPendingRequests() {
        Transfer[] transfers = transferService.getTransfers(currentUser.getUser().getId().intValue());
        int currentUserAcctId = accountService.getAccount(currentUser.getUser().getId().intValue()).getAccountId();
        Account currentUserAcct = accountService.getAccount(currentUser.getUser().getId().intValue());
        BigDecimal userBalance = currentUserAcct.getBalance();


        System.out.println();
        System.out.println("-----------------------------------------------------");
        System.out.println("                  Pending Transfers                  ");
        System.out.println("-----------------------------------------------------");

        for (Transfer transfer : transfers) {

            if(transfer.getTransferStatusId() == 1 && currentUserAcctId == transfer.getAccountFrom()){
                System.out.println("Transfer ID: " + transfer.getTransferId() + " || To: " + userService.getUsernameByAcctId(transfer.getAccountTo()).getUsername() + " || Amount: $" + transfer.getAmount());
            }
        }

        System.out.println();
        int userInput = consoleService.promptForInt("Please enter transfer ID to Approve/Reject (0 to cancel): ");

        Transfer transferToApproveReject = null;

        for (Transfer transfer : transfers) {
            if(userInput == transfer.getTransferId()) {
                transferToApproveReject = transfer;
            }
        }

        int menuSelection = -1;
        while (menuSelection != 0) {
            consoleService.printApproveRejectMenu();
            menuSelection = consoleService.promptForMenuSelection("Please choose an option: ");
            if (menuSelection == 1) {
                //handle approve

                boolean isValid = false;
                while(!isValid) {

                    if (userBalance.compareTo(transferToApproveReject.getAmount()) == -1) {
                        System.out.println("Sorry, you do not have enough money in your account to complete this transaction.");
                        break;
                    } else {
                        isValid = true;

                        User requestingUser = userService.getUsernameByAcctId(transferToApproveReject.getAccountTo());
                        int requestingUserId = requestingUser.getId().intValue();

                        Account requestingUserAcct = accountService.getAccount(requestingUserId);
                        BigDecimal receivingUserUpdatedBal = requestingUserAcct.getBalance().add(transferToApproveReject.getAmount());
                        requestingUserAcct.setBalance(receivingUserUpdatedBal);
                        accountService.updateBalance(requestingUserAcct);

                        BigDecimal currentUserUpdatedBalance = userBalance.subtract(transferToApproveReject.getAmount());
                        currentUserAcct.setBalance(currentUserUpdatedBalance);
                        accountService.updateBalance(currentUserAcct);

                        transferToApproveReject.setTransferStatusId(2);
                        transferService.updateTransfer(transferToApproveReject);

                        System.out.println("Request was successfully approved and your balance was updated.");
                        menuSelection = 0;
                    }
                }

            } else if (menuSelection == 2) {

                transferToApproveReject.setTransferStatusId(3);
                transferService.updateTransfer(transferToApproveReject);

                System.out.println("Request was successfully rejected.");
                menuSelection = 0;

            } else if (menuSelection != 0) {
                System.out.println("Invalid Selection");
                consoleService.pause();
            }
        }

	}

	private void sendBucks() {
        User[] users = userService.getUsers();

        System.out.println();
        System.out.println("-----------------------------------------------------");
        System.out.println("                     TEnmo Users                     ");
        System.out.println("-----------------------------------------------------");

        for (User user : users) {
            System.out.println("User ID: " + user.getId() + " || Username: " + user.getUsername());
        }
        System.out.println();
        int userSelection = consoleService.promptForInt("Enter ID of user you are sending to (0 to cancel): ");
        BigDecimal amountToSend = consoleService.promptForBigDecimal("Enter amount: ");

        Account currentUserAcct = accountService.getAccount(currentUser.getUser().getId().intValue());
        BigDecimal userBalance = currentUserAcct.getBalance();

        boolean isValid = false;
        while(!isValid){

            if(userBalance.compareTo(amountToSend) == -1){
                System.out.println("Sorry, you do not have enough money in your account to complete this transaction.");
                break;
            }else if (currentUser.getUser().getId().intValue() == userSelection){
                System.out.println("You cannot send money to yourself.");
                break;
            }else if (amountToSend.compareTo(BigDecimal.ZERO) == -1 || amountToSend.compareTo(BigDecimal.ZERO) == 0){
                System.out.println("You must send an amount over 0 TEBucks!");
                break;
            }else{
                isValid = true;

                Account receivingUser = accountService.getAccount(userSelection);
                BigDecimal receivingUserUpdatedBal = receivingUser.getBalance().add(amountToSend);
                receivingUser.setBalance(receivingUserUpdatedBal);
                accountService.updateBalance(receivingUser);

                BigDecimal sendingUserUpdatedBal = userBalance.subtract(amountToSend);
                currentUserAcct.setBalance(sendingUserUpdatedBal);
                accountService.updateBalance(currentUserAcct);

                Transfer makeTransfer = new Transfer();
                makeTransfer.setTransferTypeId(2);
                makeTransfer.setTransferStatusId(2);
                makeTransfer.setAccountFrom(currentUserAcct.getAccountId());
                makeTransfer.setAccountTo(receivingUser.getAccountId());
                makeTransfer.setAmount(amountToSend);

                transferService.makeTransfer(makeTransfer);

                System.out.println();
                System.out.println("Transfer successful!");
            }
        }
	}

	private void requestBucks() {
        User[] users = userService.getUsers();
        Account currentUserAcct = accountService.getAccount(currentUser.getUser().getId().intValue());


        System.out.println();
        System.out.println("-----------------------------------------------------");
        System.out.println("                     TEnmo Users                     ");
        System.out.println("-----------------------------------------------------");

        for (User user : users) {
            System.out.println("User ID: " + user.getId() + " || Username: " + user.getUsername());
        }
        System.out.println();
        int userSelection = consoleService.promptForInt("Enter ID of user you are requesting TEBucks from (0 to cancel): ");
        BigDecimal amountToRequest = consoleService.promptForBigDecimal("Enter amount: ");
        Account requestedUser = accountService.getAccount(userSelection);

        boolean isValid = false;
        while(!isValid) {

            if (currentUser.getUser().getId().intValue() == userSelection) {
                System.out.println("You cannot request money from yourself.");
                break;
            } else if (amountToRequest.compareTo(BigDecimal.ZERO) == -1 || amountToRequest.compareTo(BigDecimal.ZERO) == 0) {
                System.out.println("You must request an amount over 0 TEBucks!");
                break;
            } else {
                isValid = true;


                Transfer requestTransfer = new Transfer();
                requestTransfer.setTransferTypeId(1);
                requestTransfer.setTransferStatusId(1);
                requestTransfer.setAccountFrom(requestedUser.getAccountId());
                requestTransfer.setAccountTo(currentUserAcct.getAccountId());
                requestTransfer.setAmount(amountToRequest);

                transferService.makeTransfer(requestTransfer);

                System.out.println();
                System.out.println("Request successful!");
            }
        }

		
	}

}
