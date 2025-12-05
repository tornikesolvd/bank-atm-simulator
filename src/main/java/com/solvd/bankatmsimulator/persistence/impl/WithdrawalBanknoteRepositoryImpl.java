package com.solvd.bankatmsimulator.persistence.impl;

import com.solvd.bankatmsimulator.domain.entity.WithdrawalBanknote;
import com.solvd.bankatmsimulator.persistence.IWithdrawalBanknoteRepository;
import com.solvd.bankatmsimulator.persistence.ConnectionPool;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class WithdrawalBanknoteRepositoryImpl implements IWithdrawalBanknoteRepository {

    private final DataSource dataSource;

    public WithdrawalBanknoteRepositoryImpl() {
        this.dataSource = ConnectionPool.getDataSource();
    }

    @Override
    public WithdrawalBanknote create(WithdrawalBanknote banknote) {
        String sql = "INSERT INTO withdrawal_banknotes (withdrawal_id, denomination, quantity) VALUES (?, ?, ?)";
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setLong(1, banknote.getWithdrawalId());
            ps.setBigDecimal(2, banknote.getDenomination());
            ps.setInt(3, banknote.getQuantity());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    banknote.setId(rs.getLong(1));
                }
            }
            connection.commit();
            return banknote;
        } catch (SQLException e) {
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException rollbackEx) {
                    throw new RuntimeException("Failed to rollback transaction", rollbackEx);
                }
            }
            throw new RuntimeException("Failed to create withdrawal banknote", e);
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
    public Optional<WithdrawalBanknote> findById(Long id) {
        String sql = "SELECT id, withdrawal_id, denomination, quantity FROM withdrawal_banknotes WHERE id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToWithdrawalBanknote(rs));
                }
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find withdrawal banknote by id", e);
        }
    }

    @Override
    public List<WithdrawalBanknote> findAll() {
        String sql = "SELECT id, withdrawal_id, denomination, quantity FROM withdrawal_banknotes";
        List<WithdrawalBanknote> banknotes = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                banknotes.add(mapResultSetToWithdrawalBanknote(rs));
            }
            return banknotes;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find all withdrawal banknotes", e);
        }
    }

    @Override
    public WithdrawalBanknote update(WithdrawalBanknote banknote) {
        String sql = "UPDATE withdrawal_banknotes SET withdrawal_id = ?, denomination = ?, quantity = ? WHERE id = ?";
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setLong(1, banknote.getWithdrawalId());
            ps.setBigDecimal(2, banknote.getDenomination());
            ps.setInt(3, banknote.getQuantity());
            ps.setLong(4, banknote.getId());
            ps.executeUpdate();
            connection.commit();
            return banknote;
        } catch (SQLException e) {
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException rollbackEx) {
                    throw new RuntimeException("Failed to rollback transaction", rollbackEx);
                }
            }
            throw new RuntimeException("Failed to update withdrawal banknote", e);
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
        String sql = "DELETE FROM withdrawal_banknotes WHERE id = ?";
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
            throw new RuntimeException("Failed to delete withdrawal banknote", e);
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
    public List<WithdrawalBanknote> findByWithdrawalId(Long withdrawalId) {
        String sql = "SELECT id, withdrawal_id, denomination, quantity FROM withdrawal_banknotes WHERE withdrawal_id = ?";
        List<WithdrawalBanknote> banknotes = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, withdrawalId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    banknotes.add(mapResultSetToWithdrawalBanknote(rs));
                }
            }
            return banknotes;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find withdrawal banknotes by withdrawal id", e);
        }
    }

    private WithdrawalBanknote mapResultSetToWithdrawalBanknote(ResultSet rs) throws SQLException {
        WithdrawalBanknote banknote = new WithdrawalBanknote();
        banknote.setId(rs.getLong("id"));
        banknote.setWithdrawalId(rs.getLong("withdrawal_id"));
        banknote.setDenomination(rs.getBigDecimal("denomination"));
        banknote.setQuantity(rs.getInt("quantity"));
        return banknote;
    }
}

