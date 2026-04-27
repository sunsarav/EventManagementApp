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
        String sql = "INSERT INTO events (title, description, participant, representative) " +
                "VALUES (?, ?, ?, ?)";

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
