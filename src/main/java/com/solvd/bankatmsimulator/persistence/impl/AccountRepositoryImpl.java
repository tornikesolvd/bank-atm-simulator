package com.solvd.bankatmsimulator.persistence.impl;

import com.solvd.bankatmsimulator.domain.Account;
import com.solvd.bankatmsimulator.persistence.ConnectionPool;
import com.solvd.bankatmsimulator.persistence.IAccountRepository;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AccountRepositoryImpl implements IAccountRepository {

    private final DataSource dataSource;

    public AccountRepositoryImpl() {
        this.dataSource = ConnectionPool.getDataSource();
    }

    @Override
    public Account create(Account account) {
        String sql = "INSERT INTO accounts (account_number, balance, currency, created_at, updated_at) VALUES (?, ?, ?, ?, ?)";
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            LocalDateTime now = LocalDateTime.now();
            ps.setString(1, account.getAccountNumber());
            ps.setBigDecimal(2, account.getBalance());
            ps.setString(3, account.getCurrency());
            ps.setTimestamp(4, Timestamp.valueOf(now));
            ps.setTimestamp(5, Timestamp.valueOf(now));
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    account.setId(rs.getLong(1));
                    account.setCreatedAt(now);
                    account.setUpdatedAt(now);
                }
            }
            connection.commit();
            return account;
        } catch (SQLException e) {
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException rollbackEx) {
                    throw new RuntimeException("Failed to rollback transaction", rollbackEx);
                }
            }
            String errorMsg = String.format("Failed to create account. SQL Error: %s (SQL State: %s, Error Code: %d)",
                    e.getMessage(), e.getSQLState(), e.getErrorCode());
            throw new RuntimeException(errorMsg, e);
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    throw new RuntimeException("Failed to close connection", e);
                }
            }
        }
    }

    @Override
    public Optional<Account> findById(Long id) {
        String sql = "SELECT id, account_number, balance, currency, created_at, updated_at FROM accounts WHERE id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToAccount(rs));
                }
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find account by id", e);
        }
    }

    @Override
    public List<Account> findAll() {
        String sql = "SELECT id, account_number, balance, currency, created_at, updated_at FROM accounts";
        List<Account> accounts = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                accounts.add(mapResultSetToAccount(rs));
            }
            return accounts;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find all accounts", e);
        }
    }

    @Override
    public Account update(Account account) {
        String sql = "UPDATE accounts SET account_number = ?, balance = ?, currency = ?, updated_at = ? WHERE id = ?";
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql);
            LocalDateTime now = LocalDateTime.now();
            ps.setString(1, account.getAccountNumber());
            ps.setBigDecimal(2, account.getBalance());
            ps.setString(3, account.getCurrency());
            ps.setTimestamp(4, Timestamp.valueOf(now));
            ps.setLong(5, account.getId());
            ps.executeUpdate();
            account.setUpdatedAt(now);
            connection.commit();
            return account;
        } catch (SQLException e) {
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException rollbackEx) {
                    throw new RuntimeException("Failed to rollback transaction", rollbackEx);
                }
            }
            throw new RuntimeException("Failed to update account", e);
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    throw new RuntimeException("Failed to close connection", e);
                }
            }
        }
    }

    @Override
    public void delete(Long id) {
        String sql = "DELETE FROM accounts WHERE id = ?";
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setLong(1, id);
            ps.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException rollbackEx) {
                    throw new RuntimeException("Failed to rollback transaction", rollbackEx);
                }
            }
            throw new RuntimeException("Failed to delete account", e);
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    throw new RuntimeException("Failed to close connection", e);
                }
            }
        }
    }

    @Override
    public Optional<Account> findByAccountNumber(String accountNumber) {
        String sql = "SELECT id, account_number, balance, currency, created_at, updated_at FROM accounts WHERE account_number = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, accountNumber);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToAccount(rs));
                }
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find account by account number", e);
        }
    }

    @Override
    public List<Account> findByCurrency(String currency) {
        String sql = "SELECT id, account_number, balance, currency, created_at, updated_at FROM accounts WHERE currency = ?";
        List<Account> accounts = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, currency);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    accounts.add(mapResultSetToAccount(rs));
                }
            }
            return accounts;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find accounts by currency", e);
        }
    }

    @Override
    public List<Account> findByPersonId(Long personId) {
        String sql = "SELECT a.id, a.account_number, a.balance, a.currency, a.created_at, a.updated_at " +
                "FROM accounts a INNER JOIN persons p ON a.id = p.account_id WHERE p.id = ?";
        List<Account> accounts = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, personId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    accounts.add(mapResultSetToAccount(rs));
                }
            }
            return accounts;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find accounts by person id", e);
        }
    }

    private Account mapResultSetToAccount(ResultSet rs) throws SQLException {
        Account account = new Account(rs.getLong("id"), rs.getString("account_number"));
        account.setBalance(rs.getBigDecimal("balance"));
        account.setCurrency(rs.getString("currency"));
        Timestamp createdAt = rs.getTimestamp("created_at");
        if (createdAt != null) {
            account.setCreatedAt(createdAt.toLocalDateTime());
        }
        Timestamp updatedAt = rs.getTimestamp("updated_at");
        if (updatedAt != null) {
            account.setUpdatedAt(updatedAt.toLocalDateTime());
        }
        return account;
    }
}

