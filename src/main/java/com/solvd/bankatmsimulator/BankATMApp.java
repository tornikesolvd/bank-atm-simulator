package com.solvd.bankatmsimulator;

import com.solvd.bankatmsimulator.domain.*;
import com.solvd.bankatmsimulator.persistence.ConnectionPool;
import com.solvd.bankatmsimulator.service.*;
import com.solvd.bankatmsimulator.service.impl.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Main application class for Bank ATM Simulator
 * 
 * This application simulates a banking system with accounts and transactions.
 * It ensures money conservation and atomic transaction processing.
 */
public class BankATMApp {
    
    private static final Logger log = LoggerFactory.getLogger(BankATMApp.class);

    private static IAccountService accountService;
    private static ITransactionService transactionService;
    private static IDepositService depositService;
    private static IWithdrawalService withdrawalService;
    private static IATMService atmService;
    
    public static void main(String[] args) {

        System.out.println("Bank ATM Simulator ");

        try {
            testDatabaseConnection();

            accountService = new AccountServiceImpl();
            transactionService = new TransactionServiceImpl();
            depositService = new DepositServiceImpl();
            withdrawalService = new WithdrawalServiceImpl();
            atmService = new ATMServiceImpl();

            runAllScenarios();
            
            log.info("\n Bank ATM Simulator Completed! ");
            
        } catch (Exception e) {
            log.error("Application error: {}", e.getMessage(), e);
            System.exit(1);
        } finally {
            ConnectionPool.close();
            log.info("Connection pool closed.");
        }
    }
    

    private static void runAllScenarios() {
        try {
            // Generate unique account numbers using timestamp to avoid duplicates
            long timestamp = System.currentTimeMillis();

            log.info("Multiple Account Creation");

            Account account1 = createAccount("ACC-" + timestamp + "-001", new BigDecimal("500.00"), "USD");
            Account account2 = createAccount("ACC-" + timestamp + "-002", new BigDecimal("1000.00"), "USD");
            Account account3 = createAccount("ACC-" + timestamp + "-003", new BigDecimal("750.00"), "EUR");
            Account account4 = createAccount("ACC-" + timestamp + "-004", new BigDecimal("2000.00"), "USD");

            log.info("ATM Creation");

            ATM atm = createATM("Downtown Branch", "ATM-" + timestamp);

            log.info("Deposit with Banknotes");

            List<DepositBanknote> depositBanknotes = new ArrayList<>();
            depositBanknotes.add(createDepositBanknote(new BigDecimal("100.00"), 2)); // 2x $100
            depositBanknotes.add(createDepositBanknote(new BigDecimal("50.00"), 1));  // 1x $50
            depositBanknotes.add(createDepositBanknote(new BigDecimal("20.00"), 5));  // 5x $20
            processDeposit(account2.getId(), atm.getId(), new BigDecimal("350.00"), "USD", depositBanknotes);

            log.info("Withdrawal with Banknotes");

            List<WithdrawalBanknote> withdrawalBanknotes = new ArrayList<>();
            withdrawalBanknotes.add(createWithdrawalBanknote(new BigDecimal("50.00"), 2)); // 2x $50
            withdrawalBanknotes.add(createWithdrawalBanknote(new BigDecimal("20.00"), 2));   // 2x $20
            processWithdrawal(account2.getId(), atm.getId(), new BigDecimal("140.00"), "USD", withdrawalBanknotes);

            log.info("Multiple Deposits to the Account");

            processDeposit(account3.getId(), atm.getId(), new BigDecimal("100.00"), "EUR", null);
            processDeposit(account3.getId(), atm.getId(), new BigDecimal("50.00"), "EUR", null);
            processDeposit(account3.getId(), atm.getId(), new BigDecimal("25.00"), "EUR", null);
            displayAccountBalance(account3);


            // Create -> Deposit -> Withdraw -> Check
            log.info("Create -> Deposit -> Withdraw -> Check");

            Account account5 = createAccount("ACC-" + timestamp + "-005", new BigDecimal("1000.00"), "USD");
            BigDecimal initialBalance = account5.getBalance();
            log.info("Initial Balance: ${}", initialBalance);
            
            processDeposit(account5.getId(), atm.getId(), new BigDecimal("500.00"), "USD", null);
            Account afterDeposit = accountService.getById(account5.getId());
            log.info("Balance after deposit: ${}", afterDeposit.getBalance());
            
            processWithdrawal(account5.getId(), atm.getId(), new BigDecimal("300.00"), "USD", null);
            Account afterWithdrawal = accountService.getById(account5.getId());
            log.info("Final Balance: ${}", afterWithdrawal.getBalance());
            log.info("Expected Balance: ${} (${} + ${} - ${})", 
                    initialBalance.add(new BigDecimal("500.00")).subtract(new BigDecimal("300.00")),
                    initialBalance, new BigDecimal("500.00"), new BigDecimal("300.00"));

            //Transfer between accounts
            log.info("Transfer Between Accounts");
            processTransfer(account1.getId(), account2.getId(), new BigDecimal("50.00"), "USD");
            
            // Large Deposit with Multiple Banknote Types
            log.info("SCENARIO 9: Large Deposit with Multiple Banknote Types");

            List<DepositBanknote> largeDepositBanknotes = new ArrayList<>();
            largeDepositBanknotes.add(createDepositBanknote(new BigDecimal("100.00"), 5)); // 5x $100 = $500
            largeDepositBanknotes.add(createDepositBanknote(new BigDecimal("50.00"), 4));  // 4x $50 = $200
            largeDepositBanknotes.add(createDepositBanknote(new BigDecimal("20.00"), 10)); // 10x $20 = $200
            largeDepositBanknotes.add(createDepositBanknote(new BigDecimal("10.00"), 10)); // 10x $10 = $100
            processDeposit(account1.getId(), atm.getId(), new BigDecimal("1000.00"), "USD", largeDepositBanknotes);

            
            // Log All Accounts Summary
            log.info("Final Accounts Summary");

            displayAllAccountsSummary();
            
        } catch (Exception e) {
            log.error("Error in scenarios: {}", e.getMessage(), e);
        }
    }
    
    private static void testDatabaseConnection() {
        try (Connection connection = ConnectionPool.getDataSource().getConnection()) {
            DatabaseMetaData metaData = connection.getMetaData();
            log.info("Database connection successful!");
            log.info("Database: {}", metaData.getDatabaseProductName());
            log.info("Version: {}", metaData.getDatabaseProductVersion());
            log.info("Driver: {}", metaData.getDriverName());
            log.info("URL: {}", metaData.getURL());
        } catch (SQLException e) {
            log.error("Failed to connect to database: {}", e.getMessage(), e);
            log.error("Please ensure that:");
            log.error("1. MySQL server is running");
            log.error("2. Credentials in config.properties are correct");
            throw new RuntimeException("Database connection failed", e);
        }
    }
    
    /**
     * Creates a new account
     */
    private static Account createAccount(String accountNumber, BigDecimal initialBalance, String currency) {
        try {
            Account account = new Account(null, accountNumber);
            account.setBalance(initialBalance);
            account.setCurrency(currency);
            Account created = accountService.register(account);
            log.info("✓ Account created: {} | Balance: {} {} | ID: {}", 
                    accountNumber, initialBalance, currency, created.getId());
            return created;
        } catch (Exception e) {
            log.error("✗ Failed to create account {}: {}", accountNumber, e.getMessage());
            throw new RuntimeException("Account creation failed", e);
        }
    }

    /**
     Creates ATM
     */
    private static ATM createATM(String location, String name) {
        try {
            ATM atm = new ATM();
            atm.setLocation(location);
            atm.setName(name);
            atm.setActive(true);
            ATM created = atmService.register(atm);
            log.info(" ATM created: {} at {} | ID: {}", name, location, created.getId());
            return created;
        } catch (Exception e) {
            log.error(" Failed to create ATM: {}", e.getMessage());
            throw new RuntimeException("ATM creation failed", e);
        }
    }

    /**
     Process deposit
     */
    private static void processDeposit(Long accountId, Long atmId, BigDecimal amount, String currency, 
                                       List<DepositBanknote> banknotes) {
        try {
            Account account = accountService.getById(accountId);
            BigDecimal balanceBefore = account.getBalance();

            Transaction transaction = new Transaction();
            transaction.setToAccountId(accountId);
            transaction.setAmount(amount);
            transaction.setCurrency(currency);
            transaction.setTransactionType(Transaction.TransactionType.DEPOSIT);
            transaction.setStatus(Transaction.TransactionStatus.PENDING);
            transaction.setProcessedAt(LocalDateTime.now());
            Transaction createdTransaction = transactionService.register(transaction);
            
            // Create deposit record
            Deposit deposit = new Deposit();
            deposit.setTransactionId(createdTransaction.getId());
            deposit.setAtmId(atmId);
            deposit.setCurrency(currency);
            deposit.setTotalAmount(amount);
            deposit.setProcessedAt(LocalDateTime.now());
            
            // Add banknotes if provided
            if (banknotes != null && !banknotes.isEmpty()) {
                for (DepositBanknote banknote : banknotes) {
                    banknote.setDepositId(null); // Will be set after deposit creation
                    deposit.addBanknote(banknote);
                }
            }
            
            Deposit createdDeposit = depositService.register(deposit);
            
            // Update account balance
            account.setBalance(balanceBefore.add(amount));
            accountService.update(account);
            
            // Update transaction status
            createdTransaction.setStatus(Transaction.TransactionStatus.COMPLETED);
            transactionService.update(createdTransaction);
            
            Account accountAfter = accountService.getById(accountId);
            log.info("✓ Deposit successful: ${} {} | Balance: ${} -> ${} | Transaction ID: {}", 
                    amount, currency, balanceBefore, accountAfter.getBalance(), createdTransaction.getId());
            if (banknotes != null && !banknotes.isEmpty()) {
                log.info("  Banknotes: {}", banknotes);
            }
            
        } catch (Exception e) {
            log.error("✗ Deposit failed: {}", e.getMessage());
            throw new RuntimeException("Deposit processing failed", e);
        }
    }

    /**
     Process Withdrawal
     */

    private static void processWithdrawal(Long accountId, Long atmId, BigDecimal amount, String currency,
                                         List<WithdrawalBanknote> banknotes) {
        try {
            // Get account
            Account account = accountService.getById(accountId);
            BigDecimal balanceBefore = account.getBalance();
            
            // Check sufficient balance
            if (balanceBefore.compareTo(amount) < 0) {
                throw new RuntimeException("Insufficient balance. Available: " + balanceBefore + ", Requested: " + amount);
            }
            
            // Check minimum withdrawal amount
            if (amount.compareTo(Withdrawal.MIN_AMOUNT) < 0) {
                throw new RuntimeException("Withdrawal amount is below minimum limit of " + Withdrawal.MIN_AMOUNT);
            }
            
            // Create transaction
            Transaction transaction = new Transaction();
            transaction.setFromAccountId(accountId);
            transaction.setAmount(amount);
            transaction.setCurrency(currency);
            transaction.setTransactionType(Transaction.TransactionType.WITHDRAWAL);
            transaction.setStatus(Transaction.TransactionStatus.PENDING);
            transaction.setProcessedAt(LocalDateTime.now());
            Transaction createdTransaction = transactionService.register(transaction);
            
            // Create withdrawal record
            Withdrawal withdrawal = new Withdrawal();
            withdrawal.setAccountId(accountId);
            withdrawal.setTransactionId(createdTransaction.getId());
            withdrawal.setAtmId(atmId);
            withdrawal.setCurrency(currency);
            withdrawal.setTotalAmount(amount);
            withdrawal.setProcessedAt(LocalDateTime.now());
            

            if (banknotes != null && !banknotes.isEmpty()) {
                for (WithdrawalBanknote banknote : banknotes) {
                    banknote.setWithdrawalId(null); // Will be set after withdrawal creation
                    withdrawal.addBanknote(banknote);
                }
            }
            
            Withdrawal createdWithdrawal = withdrawalService.register(withdrawal);
            
            // Update account balance
            account.setBalance(balanceBefore.subtract(amount));
            accountService.update(account);
            
            // Update transaction status
            createdTransaction.setStatus(Transaction.TransactionStatus.COMPLETED);
            transactionService.update(createdTransaction);
            
            Account accountAfter = accountService.getById(accountId);
            log.info("✓ Withdrawal successful: ${} {} | Balance: ${} -> ${} | Transaction ID: {}", 
                    amount, currency, balanceBefore, accountAfter.getBalance(), createdTransaction.getId());
            if (banknotes != null && !banknotes.isEmpty()) {
                log.info("  Banknotes: {}", banknotes);
            }
            
        } catch (Exception e) {
            log.error("✗ Withdrawal failed: {}", e.getMessage());
            throw new RuntimeException("Withdrawal processing failed", e);
        }
    }
    
    /**
     * Processes a transfer between accounts
     */
    private static void processTransfer(Long fromAccountId, Long toAccountId, BigDecimal amount, String currency) {
        try {
            Account fromAccount = accountService.getById(fromAccountId);
            Account toAccount = accountService.getById(toAccountId);
            
            BigDecimal fromBalanceBefore = fromAccount.getBalance();
            BigDecimal toBalanceBefore = toAccount.getBalance();
            
            // Check sufficient balance
            if (fromBalanceBefore.compareTo(amount) < 0) {
                throw new RuntimeException("Insufficient balance in source account");
            }
            
            // Create transaction
            Transaction transaction = new Transaction();
            transaction.setFromAccountId(fromAccountId);
            transaction.setToAccountId(toAccountId);
            transaction.setAmount(amount);
            transaction.setCurrency(currency);
            transaction.setTransactionType(Transaction.TransactionType.TRANSFER);
            transaction.setStatus(Transaction.TransactionStatus.PENDING);
            transaction.setProcessedAt(LocalDateTime.now());
            Transaction createdTransaction = transactionService.register(transaction);
            
            // Update from account balance
            fromAccount.setBalance(fromBalanceBefore.subtract(amount));
            accountService.update(fromAccount);
            
            // Update to account balance
            toAccount.setBalance(toBalanceBefore.add(amount));
            accountService.update(toAccount);
            
            // Update transaction status
            createdTransaction.setStatus(Transaction.TransactionStatus.COMPLETED);
            transactionService.update(createdTransaction);
            
            Account fromAccountAfter = accountService.getById(fromAccountId);
            Account toAccountAfter = accountService.getById(toAccountId);
            
            log.info("✓ Transfer successful: ${} {} from Account {} to Account {}", 
                    amount, currency, fromAccountId, toAccountId);
            log.info("  From Account Balance: ${} -> ${}", fromBalanceBefore, fromAccountAfter.getBalance());
            log.info("  To Account Balance: ${} -> ${}", toBalanceBefore, toAccountAfter.getBalance());
            log.info("  Transaction ID: {}", createdTransaction.getId());
            
        } catch (Exception e) {
            log.error("✗ Transfer failed: {}", e.getMessage());
            throw new RuntimeException("Transfer processing failed", e);
        }
    }
    
    /**
     * Creates a deposit banknote
     */
    private static DepositBanknote createDepositBanknote(BigDecimal denomination, Integer quantity) {
        DepositBanknote banknote = new DepositBanknote();
        banknote.setDenomination(denomination);
        banknote.setQuantity(quantity);
        return banknote;
    }
    
    /**
     * Creates a withdrawal banknote
     */
    private static WithdrawalBanknote createWithdrawalBanknote(BigDecimal denomination, Integer quantity) {
        WithdrawalBanknote banknote = new WithdrawalBanknote();
        banknote.setDenomination(denomination);
        banknote.setQuantity(quantity);
        return banknote;
    }
    
    /**
     * Displays account balance
     */
    private static void displayAccountBalance(Account account) {
        try {
            Account current = accountService.getById(account.getId());
            log.info("Account {} | Balance: {} {}", 
                    current.getAccountNumber(), current.getBalance(), current.getCurrency());
        } catch (Exception e) {
            log.error("Failed to retrieve account balance: {}", e.getMessage());
        }
    }
    
    /**
     * Info summary of all accounts
     */
    private static void displayAllAccountsSummary() {
        try {
            List<Account> accounts = accountService.getAll();
            log.info("\nTotal Accounts: {}", accounts.size());

            BigDecimal totalBalanceUSD = BigDecimal.ZERO;
            BigDecimal totalBalanceEUR = BigDecimal.ZERO;
            
            for (Account account : accounts) {
                log.info("Account: {} | Balance: {} {} | ID: {}", 
                        account.getAccountNumber(), account.getBalance(), account.getCurrency(), account.getId());
                
                if ("USD".equals(account.getCurrency())) {
                    totalBalanceUSD = totalBalanceUSD.add(account.getBalance());
                } else if ("EUR".equals(account.getCurrency())) {
                    totalBalanceEUR = totalBalanceEUR.add(account.getBalance());
                }
            }

            log.info("Total USD Balance: ${}", totalBalanceUSD);
            log.info("Total EUR Balance: €{}", totalBalanceEUR);
            
        } catch (Exception e) {
            log.warn("Could not retrieve all accounts: {}", e.getMessage());
        }
    }
}

