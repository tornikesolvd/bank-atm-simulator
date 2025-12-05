package com.solvd.bankatmsimulator.persistence.impl;

import com.solvd.bankatmsimulator.domain.entity.PaymentCard;
import com.solvd.bankatmsimulator.persistence.IPaymentCardRepository;
import com.solvd.bankatmsimulator.persistence.ConnectionPool;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PaymentCardRepositoryImpl implements IPaymentCardRepository {

    private final DataSource dataSource;

    public PaymentCardRepositoryImpl() {
        this.dataSource = ConnectionPool.getDataSource();
    }

    @Override
    public PaymentCard create(PaymentCard card) {
        String sql = "INSERT INTO payment_cards (card_number, card_type, status, pin_hash, expiry_date, created_at, updated_at) VALUES (?, ?, ?, ?, ?, ?, ?)";
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            LocalDateTime now = LocalDateTime.now();
            ps.setString(1, card.getCardNumber());
            ps.setString(2, card.getCardType().name());
            ps.setString(3, card.getStatus().name());
            ps.setString(4, card.getPinHash());
            ps.setDate(5, Date.valueOf(card.getExpiryDate()));
            ps.setTimestamp(6, Timestamp.valueOf(now));
            ps.setTimestamp(7, Timestamp.valueOf(now));
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    card.setId(rs.getLong(1));
                    card.setCreatedAt(now);
                    card.setUpdatedAt(now);
                }
            }
            connection.commit();
            return card;
        } catch (SQLException e) {
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException rollbackEx) {
                    throw new RuntimeException("Failed to rollback transaction", rollbackEx);
                }
            }
            throw new RuntimeException("Failed to create payment card", e);
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
    public Optional<PaymentCard> findById(Long id) {
        String sql = "SELECT id, card_number, card_type, status, pin_hash, expiry_date, created_at, updated_at FROM payment_cards WHERE id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToPaymentCard(rs));
                }
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find payment card by id", e);
        }
    }

    @Override
    public List<PaymentCard> findAll() {
        String sql = "SELECT id, card_number, card_type, status, pin_hash, expiry_date, created_at, updated_at FROM payment_cards";
        List<PaymentCard> cards = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                cards.add(mapResultSetToPaymentCard(rs));
            }
            return cards;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find all payment cards", e);
        }
    }

    @Override
    public PaymentCard update(PaymentCard card) {
        String sql = "UPDATE payment_cards SET card_number = ?, card_type = ?, status = ?, pin_hash = ?, expiry_date = ?, updated_at = ? WHERE id = ?";
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql);
            LocalDateTime now = LocalDateTime.now();
            ps.setString(1, card.getCardNumber());
            ps.setString(2, card.getCardType().name());
            ps.setString(3, card.getStatus().name());
            ps.setString(4, card.getPinHash());
            ps.setDate(5, Date.valueOf(card.getExpiryDate()));
            ps.setTimestamp(6, Timestamp.valueOf(now));
            ps.setLong(7, card.getId());
            ps.executeUpdate();
            card.setUpdatedAt(now);
            connection.commit();
            return card;
        } catch (SQLException e) {
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException rollbackEx) {
                    throw new RuntimeException("Failed to rollback transaction", rollbackEx);
                }
            }
            throw new RuntimeException("Failed to update payment card", e);
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
        String sql = "DELETE FROM payment_cards WHERE id = ?";
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
            throw new RuntimeException("Failed to delete payment card", e);
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
    public Optional<PaymentCard> findByCardNumber(String cardNumber) {
        String sql = "SELECT id, card_number, card_type, status, pin_hash, expiry_date, created_at, updated_at FROM payment_cards WHERE card_number = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, cardNumber);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToPaymentCard(rs));
                }
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find payment card by card number", e);
        }
    }

    @Override
    public List<PaymentCard> findByAccountId(Long accountId) {
        String sql = "SELECT pc.id, pc.card_number, pc.card_type, pc.status, pc.pin_hash, pc.expiry_date, pc.created_at, pc.updated_at " +
                     "FROM payment_cards pc INNER JOIN account_cards ac ON pc.id = ac.card_id WHERE ac.account_id = ?";
        List<PaymentCard> cards = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, accountId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    cards.add(mapResultSetToPaymentCard(rs));
                }
            }
            return cards;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find payment cards by account id", e);
        }
    }

    @Override
    public List<PaymentCard> findByStatus(PaymentCard.CardStatus status) {
        String sql = "SELECT id, card_number, card_type, status, pin_hash, expiry_date, created_at, updated_at FROM payment_cards WHERE status = ?";
        List<PaymentCard> cards = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, status.name());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    cards.add(mapResultSetToPaymentCard(rs));
                }
            }
            return cards;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find payment cards by status", e);
        }
    }

    @Override
    public List<PaymentCard> findExpiredCards() {
        String sql = "SELECT id, card_number, card_type, status, pin_hash, expiry_date, created_at, updated_at FROM payment_cards WHERE expiry_date < ?";
        List<PaymentCard> cards = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(LocalDate.now()));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    cards.add(mapResultSetToPaymentCard(rs));
                }
            }
            return cards;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find expired payment cards", e);
        }
    }

    private PaymentCard mapResultSetToPaymentCard(ResultSet rs) throws SQLException {
        PaymentCard card = new PaymentCard();
        card.setId(rs.getLong("id"));
        card.setCardNumber(rs.getString("card_number"));
        card.setCardType(PaymentCard.CardType.valueOf(rs.getString("card_type")));
        card.setStatus(PaymentCard.CardStatus.valueOf(rs.getString("status")));
        card.setPinHash(rs.getString("pin_hash"));
        Date expiryDate = rs.getDate("expiry_date");
        if (expiryDate != null) {
            card.setExpiryDate(expiryDate.toLocalDate());
        }
        Timestamp createdAt = rs.getTimestamp("created_at");
        if (createdAt != null) {
            card.setCreatedAt(createdAt.toLocalDateTime());
        }
        Timestamp updatedAt = rs.getTimestamp("updated_at");
        if (updatedAt != null) {
            card.setUpdatedAt(updatedAt.toLocalDateTime());
        }
        return card;
    }
}

