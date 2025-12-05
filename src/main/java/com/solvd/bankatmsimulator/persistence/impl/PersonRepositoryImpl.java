package com.solvd.bankatmsimulator.persistence.impl;

import com.solvd.bankatmsimulator.domain.entity.Person;
import com.solvd.bankatmsimulator.persistence.IPersonRepository;
import com.solvd.bankatmsimulator.persistence.ConnectionPool;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PersonRepositoryImpl implements IPersonRepository {

    private final DataSource dataSource;

    public PersonRepositoryImpl() {
        this.dataSource = ConnectionPool.getDataSource();
    }

    @Override
    public Person create(Person person) {
        String sql = "INSERT INTO persons (full_name, email, phone_number) VALUES (?, ?, ?)";
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, person.getFullName());
            ps.setString(2, person.getEmail());
            ps.setString(3, person.getPhoneNumber());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    person.setId(rs.getLong(1));
                }
            }
            connection.commit();
            return person;
        } catch (SQLException e) {
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException rollbackEx) {
                    throw new RuntimeException("Failed to rollback transaction", rollbackEx);
                }
            }
            throw new RuntimeException("Failed to create person", e);
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
    public Optional<Person> findById(Long id) {
        String sql = "SELECT id, full_name, email, phone_number FROM persons WHERE id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToPerson(rs));
                }
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find person by id", e);
        }
    }

    @Override
    public List<Person> findAll() {
        String sql = "SELECT id, full_name, email, phone_number FROM persons";
        List<Person> persons = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                persons.add(mapResultSetToPerson(rs));
            }
            return persons;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find all persons", e);
        }
    }

    @Override
    public Person update(Person person) {
        String sql = "UPDATE persons SET full_name = ?, email = ?, phone_number = ? WHERE id = ?";
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, person.getFullName());
            ps.setString(2, person.getEmail());
            ps.setString(3, person.getPhoneNumber());
            ps.setLong(4, person.getId());
            ps.executeUpdate();
            connection.commit();
            return person;
        } catch (SQLException e) {
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException rollbackEx) {
                    throw new RuntimeException("Failed to rollback transaction", rollbackEx);
                }
            }
            throw new RuntimeException("Failed to update person", e);
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
        String sql = "DELETE FROM persons WHERE id = ?";
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
            throw new RuntimeException("Failed to delete person", e);
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
    public Optional<Person> findByEmail(String email) {
        String sql = "SELECT id, full_name, email, phone_number FROM persons WHERE email = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToPerson(rs));
                }
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find person by email", e);
        }
    }

    @Override
    public Optional<Person> findByPhoneNumber(String phoneNumber) {
        String sql = "SELECT id, full_name, email, phone_number FROM persons WHERE phone_number = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, phoneNumber);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToPerson(rs));
                }
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find person by phone number", e);
        }
    }

    private Person mapResultSetToPerson(ResultSet rs) throws SQLException {
        Person person = new Person();
        person.setId(rs.getLong("id"));
        person.setFullName(rs.getString("full_name"));
        person.setEmail(rs.getString("email"));
        person.setPhoneNumber(rs.getString("phone_number"));
        return person;
    }
}

