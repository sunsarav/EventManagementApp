package se.lexicon.dao;

import se.lexicon.model.Event;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EventDAOImpl implements EventDAO {
    private final Connection connection;

    public EventDAOImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Event save(Event event) {
        String sql = "INSERT INTO events (title, description, start_time, end_time," +
                "location, capacity) " + "VALUES (?, ?, ?, ?, ?, ?)";

        try (
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {
            ps.setString(1, event.getTitle());
            ps.setString(2, event.getDescription());
            // Converting LocalDateTime to SQL Timestamp
            ps.setTimestamp(3, Timestamp.valueOf(event.getStartDateTime()));
            ps.setTimestamp(4,Timestamp.valueOf(event.getEndDateTime()));
            ps.setString(5, event.getLocation());
            ps.setInt(6, event.getCapacity());

            ps.executeUpdate();

            // Read auto-generated ID
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    event.setId(rs.getInt(1));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return event;
    }

    @Override
    public Event findById(Integer id) {
        String sql = "SELECT * FROM events WHERE id = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRowToEvent(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("❌ Error finding event by ID: " + e.getMessage());
            throw new RuntimeException("Error finding event", e);
        }
        return null;
    }

    @Override
    public List<Event> findAll() {
        List<Event> events = new ArrayList<>();
        String sql = "SELECT * FROM events";

        try (
             PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()
        ) {
            while (rs.next()) {
                events.add(mapRowToEvent(rs));
            }
        } catch (SQLException e) {
            System.err.println("❌ Error fetching events: " + e.getMessage());
            throw new RuntimeException("Error fetching events", e);
        }
        return events;
    }

    @Override
    public Event findByName(String name) {
        String sql = "SELECT * FROM events WHERE title = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, name);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRowToEvent(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("❌ Error finding event by name: " + e.getMessage());
            throw new RuntimeException("Error finding event by name", e);
        }
        return null;
    }

    // Helper method to keep code clean
    private Event mapRowToEvent(ResultSet rs) throws SQLException {
        return new Event(
                rs.getInt("id"),
                rs.getString("title"),
                rs.getString("description"),
                rs.getTimestamp("start_time").toLocalDateTime(),
                rs.getTimestamp("end_time").toLocalDateTime(),
                rs.getString("location"),
                rs.getInt("capacity")
        );
    }
}
