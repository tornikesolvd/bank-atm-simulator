package com.solvd.bankatmsimulator.persistence.impl;

import com.solvd.bankatmsimulator.domain.DepositBanknote;
import com.solvd.bankatmsimulator.persistence.ConnectionPool;
import com.solvd.bankatmsimulator.persistence.IDepositBanknoteRepository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DepositBanknoteRepositoryImpl implements IDepositBanknoteRepository {

    private final DataSource dataSource;

    public DepositBanknoteRepositoryImpl() {
        this.dataSource = ConnectionPool.getDataSource();
    }

    @Override
    public DepositBanknote create(DepositBanknote banknote) {
        String sql = "INSERT INTO deposit_banknotes (deposit_id, denomination, quantity) VALUES (?, ?, ?)";
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setLong(1, banknote.getDepositId());
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
            throw new RuntimeException("Failed to create deposit banknote", e);
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
    public Optional<DepositBanknote> findById(Long id) {
        String sql = "SELECT id, deposit_id, denomination, quantity FROM deposit_banknotes WHERE id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToDepositBanknote(rs));
                }
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find deposit banknote by id", e);
        }
    }

    @Override
    public List<DepositBanknote> findAll() {
        String sql = "SELECT id, deposit_id, denomination, quantity FROM deposit_banknotes";
        List<DepositBanknote> banknotes = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                banknotes.add(mapResultSetToDepositBanknote(rs));
            }
            return banknotes;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find all deposit banknotes", e);
        }
    }

    @Override
    public DepositBanknote update(DepositBanknote banknote) {
        String sql = "UPDATE deposit_banknotes SET deposit_id = ?, denomination = ?, quantity = ? WHERE id = ?";
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setLong(1, banknote.getDepositId());
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
            throw new RuntimeException("Failed to update deposit banknote", e);
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
        String sql = "DELETE FROM deposit_banknotes WHERE id = ?";
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
            throw new RuntimeException("Failed to delete deposit banknote", e);
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
    public List<DepositBanknote> findByDepositId(Long depositId) {
        String sql = "SELECT id, deposit_id, denomination, quantity FROM deposit_banknotes WHERE deposit_id = ?";
        List<DepositBanknote> banknotes = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, depositId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    banknotes.add(mapResultSetToDepositBanknote(rs));
                }
            }
            return banknotes;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find deposit banknotes by deposit id", e);
        }
    }

    private DepositBanknote mapResultSetToDepositBanknote(ResultSet rs) throws SQLException {
        DepositBanknote banknote = new DepositBanknote();
        banknote.setId(rs.getLong("id"));
        banknote.setDepositId(rs.getLong("deposit_id"));
        banknote.setDenomination(rs.getBigDecimal("denomination"));
        banknote.setQuantity(rs.getInt("quantity"));
        return banknote;
    }
}

