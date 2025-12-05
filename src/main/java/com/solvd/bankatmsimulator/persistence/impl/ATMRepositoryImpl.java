package com.solvd.bankatmsimulator.persistence.impl;

import com.solvd.bankatmsimulator.domain.entity.ATM;
import com.solvd.bankatmsimulator.persistence.IATMRepository;
import com.solvd.bankatmsimulator.persistence.ConnectionPool;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ATMRepositoryImpl implements IATMRepository {

    private final DataSource dataSource;

    public ATMRepositoryImpl() {
        this.dataSource = ConnectionPool.getDataSource();
    }

    @Override
    public ATM create(ATM atm) {
        String sql = "INSERT INTO atms (location, name, is_active) VALUES (?, ?, ?)";
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, atm.getLocation());
            ps.setString(2, atm.getName());
            ps.setBoolean(3, atm.isActive());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    atm.setId(rs.getLong(1));
                }
            }
            connection.commit();
            return atm;
        } catch (SQLException e) {
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException rollbackEx) {
                    throw new RuntimeException("Failed to rollback transaction", rollbackEx);
                }
            }
            throw new RuntimeException("Failed to create ATM", e);
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
    public Optional<ATM> findById(Long id) {
        String sql = "SELECT id, location, name, is_active FROM atms WHERE id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToATM(rs));
                }
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find ATM by id", e);
        }
    }

    @Override
    public List<ATM> findAll() {
        String sql = "SELECT id, location, name, is_active FROM atms";
        List<ATM> atms = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                atms.add(mapResultSetToATM(rs));
            }
            return atms;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find all ATMs", e);
        }
    }

    @Override
    public ATM update(ATM atm) {
        String sql = "UPDATE atms SET location = ?, name = ?, is_active = ? WHERE id = ?";
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, atm.getLocation());
            ps.setString(2, atm.getName());
            ps.setBoolean(3, atm.isActive());
            ps.setLong(4, atm.getId());
            ps.executeUpdate();
            connection.commit();
            return atm;
        } catch (SQLException e) {
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException rollbackEx) {
                    throw new RuntimeException("Failed to rollback transaction", rollbackEx);
                }
            }
            throw new RuntimeException("Failed to update ATM", e);
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
        String sql = "DELETE FROM atms WHERE id = ?";
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
            throw new RuntimeException("Failed to delete ATM", e);
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
    public List<ATM> findActiveATMs() {
        String sql = "SELECT id, location, name, is_active FROM atms WHERE is_active = true";
        List<ATM> atms = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                atms.add(mapResultSetToATM(rs));
            }
            return atms;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find active ATMs", e);
        }
    }

    @Override
    public List<ATM> findByLocation(String location) {
        String sql = "SELECT id, location, name, is_active FROM atms WHERE location = ?";
        List<ATM> atms = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, location);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    atms.add(mapResultSetToATM(rs));
                }
            }
            return atms;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find ATMs by location", e);
        }
    }

    private ATM mapResultSetToATM(ResultSet rs) throws SQLException {
        ATM atm = new ATM();
        atm.setId(rs.getLong("id"));
        atm.setLocation(rs.getString("location"));
        atm.setName(rs.getString("name"));
        atm.setActive(rs.getBoolean("is_active"));
        return atm;
    }
}

