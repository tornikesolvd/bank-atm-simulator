package com.solvd.bankatmsimulator.persistence.impl;

import com.solvd.bankatmsimulator.domain.entity.Withdrawal;
import com.solvd.bankatmsimulator.persistence.IWithdrawalRepository;
import com.solvd.bankatmsimulator.persistence.ConnectionPool;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class WithdrawalRepositoryImpl implements IWithdrawalRepository {

    private final DataSource dataSource;

    public WithdrawalRepositoryImpl() {
        this.dataSource = ConnectionPool.getDataSource();
    }

    @Override
    public Withdrawal create(Withdrawal withdrawal) {
        String sql = "INSERT INTO withdrawals (account_id, transaction_id, atm_id, currency, total_amount, processed_at) VALUES (?, ?, ?, ?, ?, ?)";
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setLong(1, withdrawal.getAccountId());
            ps.setLong(2, withdrawal.getTransactionId());
            ps.setLong(3, withdrawal.getAtmId());
            ps.setString(4, withdrawal.getCurrency());
            ps.setBigDecimal(5, withdrawal.getTotalAmount());
            LocalDateTime processedAt = withdrawal.getProcessedAt() != null ? withdrawal.getProcessedAt() : LocalDateTime.now();
            ps.setTimestamp(6, Timestamp.valueOf(processedAt));
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    withdrawal.setId(rs.getLong(1));
                    withdrawal.setProcessedAt(processedAt);
                }
            }
            connection.commit();
            return withdrawal;
        } catch (SQLException e) {
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException rollbackEx) {
                    throw new RuntimeException("Failed to rollback transaction", rollbackEx);
                }
            }
            throw new RuntimeException("Failed to create withdrawal", e);
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
    public Optional<Withdrawal> findById(Long id) {
        String sql = "SELECT id, account_id, transaction_id, atm_id, currency, total_amount, processed_at FROM withdrawals WHERE id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToWithdrawal(rs));
                }
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find withdrawal by id", e);
        }
    }

    @Override
    public List<Withdrawal> findAll() {
        String sql = "SELECT id, account_id, transaction_id, atm_id, currency, total_amount, processed_at FROM withdrawals";
        List<Withdrawal> withdrawals = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                withdrawals.add(mapResultSetToWithdrawal(rs));
            }
            return withdrawals;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find all withdrawals", e);
        }
    }

    @Override
    public Withdrawal update(Withdrawal withdrawal) {
        String sql = "UPDATE withdrawals SET account_id = ?, transaction_id = ?, atm_id = ?, currency = ?, total_amount = ?, processed_at = ? WHERE id = ?";
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setLong(1, withdrawal.getAccountId());
            ps.setLong(2, withdrawal.getTransactionId());
            ps.setLong(3, withdrawal.getAtmId());
            ps.setString(4, withdrawal.getCurrency());
            ps.setBigDecimal(5, withdrawal.getTotalAmount());
            ps.setTimestamp(6, Timestamp.valueOf(withdrawal.getProcessedAt()));
            ps.setLong(7, withdrawal.getId());
            ps.executeUpdate();
            connection.commit();
            return withdrawal;
        } catch (SQLException e) {
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException rollbackEx) {
                    throw new RuntimeException("Failed to rollback transaction", rollbackEx);
                }
            }
            throw new RuntimeException("Failed to update withdrawal", e);
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
        String sql = "DELETE FROM withdrawals WHERE id = ?";
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
            throw new RuntimeException("Failed to delete withdrawal", e);
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
    public List<Withdrawal> findByAccountId(Long accountId) {
        String sql = "SELECT id, account_id, transaction_id, atm_id, currency, total_amount, processed_at FROM withdrawals WHERE account_id = ?";
        List<Withdrawal> withdrawals = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, accountId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    withdrawals.add(mapResultSetToWithdrawal(rs));
                }
            }
            return withdrawals;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find withdrawals by account id", e);
        }
    }

    @Override
    public List<Withdrawal> findByTransactionId(Long transactionId) {
        String sql = "SELECT id, account_id, transaction_id, atm_id, currency, total_amount, processed_at FROM withdrawals WHERE transaction_id = ?";
        List<Withdrawal> withdrawals = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, transactionId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    withdrawals.add(mapResultSetToWithdrawal(rs));
                }
            }
            return withdrawals;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find withdrawals by transaction id", e);
        }
    }

    @Override
    public List<Withdrawal> findByAtmId(Long atmId) {
        String sql = "SELECT id, account_id, transaction_id, atm_id, currency, total_amount, processed_at FROM withdrawals WHERE atm_id = ?";
        List<Withdrawal> withdrawals = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, atmId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    withdrawals.add(mapResultSetToWithdrawal(rs));
                }
            }
            return withdrawals;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find withdrawals by ATM id", e);
        }
    }

    private Withdrawal mapResultSetToWithdrawal(ResultSet rs) throws SQLException {
        Withdrawal withdrawal = new Withdrawal();
        withdrawal.setId(rs.getLong("id"));
        withdrawal.setAccountId(rs.getLong("account_id"));
        withdrawal.setTransactionId(rs.getLong("transaction_id"));
        withdrawal.setAtmId(rs.getLong("atm_id"));
        withdrawal.setCurrency(rs.getString("currency"));
        withdrawal.setTotalAmount(rs.getBigDecimal("total_amount"));
        Timestamp processedAt = rs.getTimestamp("processed_at");
        if (processedAt != null) {
            withdrawal.setProcessedAt(processedAt.toLocalDateTime());
        }
        return withdrawal;
    }
}

