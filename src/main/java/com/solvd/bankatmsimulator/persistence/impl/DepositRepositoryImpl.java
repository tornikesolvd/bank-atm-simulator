package com.solvd.bankatmsimulator.persistence.impl;

import com.solvd.bankatmsimulator.domain.entity.Deposit;
import com.solvd.bankatmsimulator.persistence.IDepositRepository;
import com.solvd.bankatmsimulator.persistence.ConnectionPool;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DepositRepositoryImpl implements IDepositRepository {

    private final DataSource dataSource;

    public DepositRepositoryImpl() {
        this.dataSource = ConnectionPool.getDataSource();
    }

    @Override
    public Deposit create(Deposit deposit) {
        String sql = "INSERT INTO deposits (transaction_id, atm_id, currency, total_amount, processed_at) VALUES (?, ?, ?, ?, ?)";
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setLong(1, deposit.getTransactionId());
            ps.setLong(2, deposit.getAtmId());
            ps.setString(3, deposit.getCurrency());
            ps.setBigDecimal(4, deposit.getTotalAmount());
            LocalDateTime processedAt = deposit.getProcessedAt() != null ? deposit.getProcessedAt() : LocalDateTime.now();
            ps.setTimestamp(5, Timestamp.valueOf(processedAt));
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    deposit.setId(rs.getLong(1));
                    deposit.setProcessedAt(processedAt);
                }
            }
            connection.commit();
            return deposit;
        } catch (SQLException e) {
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException rollbackEx) {
                    throw new RuntimeException("Failed to rollback transaction", rollbackEx);
                }
            }
            throw new RuntimeException("Failed to create deposit", e);
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
    public Optional<Deposit> findById(Long id) {
        String sql = "SELECT id, transaction_id, atm_id, currency, total_amount, processed_at FROM deposits WHERE id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToDeposit(rs));
                }
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find deposit by id", e);
        }
    }

    @Override
    public List<Deposit> findAll() {
        String sql = "SELECT id, transaction_id, atm_id, currency, total_amount, processed_at FROM deposits";
        List<Deposit> deposits = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                deposits.add(mapResultSetToDeposit(rs));
            }
            return deposits;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find all deposits", e);
        }
    }

    @Override
    public Deposit update(Deposit deposit) {
        String sql = "UPDATE deposits SET transaction_id = ?, atm_id = ?, currency = ?, total_amount = ?, processed_at = ? WHERE id = ?";
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setLong(1, deposit.getTransactionId());
            ps.setLong(2, deposit.getAtmId());
            ps.setString(3, deposit.getCurrency());
            ps.setBigDecimal(4, deposit.getTotalAmount());
            ps.setTimestamp(5, Timestamp.valueOf(deposit.getProcessedAt()));
            ps.setLong(6, deposit.getId());
            ps.executeUpdate();
            connection.commit();
            return deposit;
        } catch (SQLException e) {
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException rollbackEx) {
                    throw new RuntimeException("Failed to rollback transaction", rollbackEx);
                }
            }
            throw new RuntimeException("Failed to update deposit", e);
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
        String sql = "DELETE FROM deposits WHERE id = ?";
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
            throw new RuntimeException("Failed to delete deposit", e);
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
    public List<Deposit> findByTransactionId(Long transactionId) {
        String sql = "SELECT id, transaction_id, atm_id, currency, total_amount, processed_at FROM deposits WHERE transaction_id = ?";
        List<Deposit> deposits = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, transactionId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    deposits.add(mapResultSetToDeposit(rs));
                }
            }
            return deposits;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find deposits by transaction id", e);
        }
    }

    @Override
    public List<Deposit> findByAtmId(Long atmId) {
        String sql = "SELECT id, transaction_id, atm_id, currency, total_amount, processed_at FROM deposits WHERE atm_id = ?";
        List<Deposit> deposits = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, atmId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    deposits.add(mapResultSetToDeposit(rs));
                }
            }
            return deposits;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find deposits by ATM id", e);
        }
    }

    private Deposit mapResultSetToDeposit(ResultSet rs) throws SQLException {
        Deposit deposit = new Deposit();
        deposit.setId(rs.getLong("id"));
        deposit.setTransactionId(rs.getLong("transaction_id"));
        deposit.setAtmId(rs.getLong("atm_id"));
        deposit.setCurrency(rs.getString("currency"));
        deposit.setTotalAmount(rs.getBigDecimal("total_amount"));
        Timestamp processedAt = rs.getTimestamp("processed_at");
        if (processedAt != null) {
            deposit.setProcessedAt(processedAt.toLocalDateTime());
        }
        return deposit;
    }
}

