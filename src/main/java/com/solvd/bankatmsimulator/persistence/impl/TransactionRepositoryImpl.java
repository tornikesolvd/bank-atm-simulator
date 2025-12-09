package com.solvd.bankatmsimulator.persistence.impl;

import com.solvd.bankatmsimulator.domain.Transaction;
import com.solvd.bankatmsimulator.persistence.ITransactionRepository;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TransactionRepositoryImpl implements ITransactionRepository {

    private final DataSource dataSource;

    public TransactionRepositoryImpl() {
        this.dataSource = ConnectionPool.getDataSource();
    }

    @Override
    public Transaction create(Transaction transaction) {
        String sql = "INSERT INTO transactions (from_account_id, to_account_id, amount, currency, transaction_type, status, processed_at) VALUES (?, ?, ?, ?, ?, ?, ?)";
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setObject(1, transaction.getFromAccountId(), Types.BIGINT);
            ps.setObject(2, transaction.getToAccountId(), Types.BIGINT);
            ps.setBigDecimal(3, transaction.getAmount());
            ps.setString(4, transaction.getCurrency());
            ps.setString(5, transaction.getTransactionType().name());
            ps.setString(6, transaction.getStatus().name());
            LocalDateTime processedAt = transaction.getProcessedAt() != null ? transaction.getProcessedAt() : LocalDateTime.now();
            ps.setTimestamp(7, Timestamp.valueOf(processedAt));
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    transaction.setId(rs.getLong(1));
                    transaction.setProcessedAt(processedAt);
                }
            }
            connection.commit();
            return transaction;
        } catch (SQLException e) {
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException rollbackEx) {
                    throw new RuntimeException("Failed to rollback transaction", rollbackEx);
                }
            }
            throw new RuntimeException("Failed to create transaction", e);
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
    public Optional<Transaction> findById(Long id) {
        String sql = "SELECT id, from_account_id, to_account_id, amount, currency, transaction_type, status, processed_at FROM transactions WHERE id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToTransaction(rs));
                }
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find transaction by id", e);
        }
    }

    @Override
    public List<Transaction> findAll() {
        String sql = "SELECT id, from_account_id, to_account_id, amount, currency, transaction_type, status, processed_at FROM transactions";
        List<Transaction> transactions = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                transactions.add(mapResultSetToTransaction(rs));
            }
            return transactions;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find all transactions", e);
        }
    }

    @Override
    public Transaction update(Transaction transaction) {
        String sql = "UPDATE transactions SET from_account_id = ?, to_account_id = ?, amount = ?, currency = ?, transaction_type = ?, status = ?, processed_at = ? WHERE id = ?";
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setObject(1, transaction.getFromAccountId(), Types.BIGINT);
            ps.setObject(2, transaction.getToAccountId(), Types.BIGINT);
            ps.setBigDecimal(3, transaction.getAmount());
            ps.setString(4, transaction.getCurrency());
            ps.setString(5, transaction.getTransactionType().name());
            ps.setString(6, transaction.getStatus().name());
            ps.setTimestamp(7, Timestamp.valueOf(transaction.getProcessedAt()));
            ps.setLong(8, transaction.getId());
            ps.executeUpdate();
            connection.commit();
            return transaction;
        } catch (SQLException e) {
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException rollbackEx) {
                    throw new RuntimeException("Failed to rollback transaction", rollbackEx);
                }
            }
            throw new RuntimeException("Failed to update transaction", e);
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
        String sql = "DELETE FROM transactions WHERE id = ?";
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
            throw new RuntimeException("Failed to delete transaction", e);
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
    public List<Transaction> findByAccountId(Long accountId) {
        String sql = "SELECT id, from_account_id, to_account_id, amount, currency, transaction_type, status, processed_at " +
                "FROM transactions WHERE from_account_id = ? OR to_account_id = ?";
        List<Transaction> transactions = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, accountId);
            ps.setLong(2, accountId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    transactions.add(mapResultSetToTransaction(rs));
                }
            }
            return transactions;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find transactions by account id", e);
        }
    }

    @Override
    public List<Transaction> findByFromAccountId(Long fromAccountId) {
        String sql = "SELECT id, from_account_id, to_account_id, amount, currency, transaction_type, status, processed_at FROM transactions WHERE from_account_id = ?";
        return findTransactionsByField(sql, fromAccountId);
    }

    @Override
    public List<Transaction> findByToAccountId(Long toAccountId) {
        String sql = "SELECT id, from_account_id, to_account_id, amount, currency, transaction_type, status, processed_at FROM transactions WHERE to_account_id = ?";
        return findTransactionsByField(sql, toAccountId);
    }

    @Override
    public List<Transaction> findByType(Transaction.TransactionType type) {
        String sql = "SELECT id, from_account_id, to_account_id, amount, currency, transaction_type, status, processed_at FROM transactions WHERE transaction_type = ?";
        List<Transaction> transactions = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, type.name());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    transactions.add(mapResultSetToTransaction(rs));
                }
            }
            return transactions;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find transactions by type", e);
        }
    }

    @Override
    public List<Transaction> findByStatus(Transaction.TransactionStatus status) {
        String sql = "SELECT id, from_account_id, to_account_id, amount, currency, transaction_type, status, processed_at FROM transactions WHERE status = ?";
        List<Transaction> transactions = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, status.name());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    transactions.add(mapResultSetToTransaction(rs));
                }
            }
            return transactions;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find transactions by status", e);
        }
    }

    private List<Transaction> findTransactionsByField(String sql, Long accountId) {
        List<Transaction> transactions = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, accountId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    transactions.add(mapResultSetToTransaction(rs));
                }
            }
            return transactions;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find transactions", e);
        }
    }

    private Transaction mapResultSetToTransaction(ResultSet rs) throws SQLException {
        Transaction transaction = new Transaction();
        transaction.setId(rs.getLong("id"));
        Long fromAccountId = rs.getLong("from_account_id");
        if (!rs.wasNull()) {
            transaction.setFromAccountId(fromAccountId);
        }
        Long toAccountId = rs.getLong("to_account_id");
        if (!rs.wasNull()) {
            transaction.setToAccountId(toAccountId);
        }
        transaction.setAmount(rs.getBigDecimal("amount"));
        transaction.setCurrency(rs.getString("currency"));
        transaction.setTransactionType(Transaction.TransactionType.valueOf(rs.getString("transaction_type")));
        transaction.setStatus(Transaction.TransactionStatus.valueOf(rs.getString("status")));
        Timestamp processedAt = rs.getTimestamp("processed_at");
        if (processedAt != null) {
            transaction.setProcessedAt(processedAt.toLocalDateTime());
        }
        return transaction;
    }
}

