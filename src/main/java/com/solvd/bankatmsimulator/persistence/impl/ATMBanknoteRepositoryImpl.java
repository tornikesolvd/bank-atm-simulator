package com.solvd.bankatmsimulator.persistence.impl;

import com.solvd.bankatmsimulator.domain.entity.ATMBanknote;
import com.solvd.bankatmsimulator.persistence.IATMBanknoteRepository;
import com.solvd.bankatmsimulator.persistence.ConnectionPool;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ATMBanknoteRepositoryImpl implements IATMBanknoteRepository {

    private final DataSource dataSource;

    public ATMBanknoteRepositoryImpl() {
        this.dataSource = ConnectionPool.getDataSource();
    }

    @Override
    public ATMBanknote create(ATMBanknote banknote) {
        String sql = "INSERT INTO atm_banknotes (atm_id, currency, denomination, quantity) VALUES (?, ?, ?, ?)";
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setLong(1, banknote.getAtmId());
            ps.setString(2, banknote.getCurrency());
            ps.setBigDecimal(3, banknote.getDenomination());
            ps.setInt(4, banknote.getQuantity());
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
            throw new RuntimeException("Failed to create ATM banknote", e);
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
    public Optional<ATMBanknote> findById(Long id) {
        String sql = "SELECT id, atm_id, currency, denomination, quantity FROM atm_banknotes WHERE id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToATMBanknote(rs));
                }
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find ATM banknote by id", e);
        }
    }

    @Override
    public List<ATMBanknote> findAll() {
        String sql = "SELECT id, atm_id, currency, denomination, quantity FROM atm_banknotes";
        List<ATMBanknote> banknotes = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                banknotes.add(mapResultSetToATMBanknote(rs));
            }
            return banknotes;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find all ATM banknotes", e);
        }
    }

    @Override
    public ATMBanknote update(ATMBanknote banknote) {
        String sql = "UPDATE atm_banknotes SET atm_id = ?, currency = ?, denomination = ?, quantity = ? WHERE id = ?";
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setLong(1, banknote.getAtmId());
            ps.setString(2, banknote.getCurrency());
            ps.setBigDecimal(3, banknote.getDenomination());
            ps.setInt(4, banknote.getQuantity());
            ps.setLong(5, banknote.getId());
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
            throw new RuntimeException("Failed to update ATM banknote", e);
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
        String sql = "DELETE FROM atm_banknotes WHERE id = ?";
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
            throw new RuntimeException("Failed to delete ATM banknote", e);
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
    public List<ATMBanknote> findByAtmId(Long atmId) {
        String sql = "SELECT id, atm_id, currency, denomination, quantity FROM atm_banknotes WHERE atm_id = ?";
        List<ATMBanknote> banknotes = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, atmId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    banknotes.add(mapResultSetToATMBanknote(rs));
                }
            }
            return banknotes;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find ATM banknotes by ATM id", e);
        }
    }

    @Override
    public List<ATMBanknote> findByAtmIdAndCurrency(Long atmId, String currency) {
        String sql = "SELECT id, atm_id, currency, denomination, quantity FROM atm_banknotes WHERE atm_id = ? AND currency = ?";
        List<ATMBanknote> banknotes = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, atmId);
            ps.setString(2, currency);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    banknotes.add(mapResultSetToATMBanknote(rs));
                }
            }
            return banknotes;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find ATM banknotes by ATM id and currency", e);
        }
    }

    private ATMBanknote mapResultSetToATMBanknote(ResultSet rs) throws SQLException {
        ATMBanknote banknote = new ATMBanknote();
        banknote.setId(rs.getLong("id"));
        banknote.setAtmId(rs.getLong("atm_id"));
        banknote.setCurrency(rs.getString("currency"));
        banknote.setDenomination(rs.getBigDecimal("denomination"));
        banknote.setQuantity(rs.getInt("quantity"));
        return banknote;
    }
}

