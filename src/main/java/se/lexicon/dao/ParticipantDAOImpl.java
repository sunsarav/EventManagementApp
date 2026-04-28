package se.lexicon.dao;

import se.lexicon.model.Participant;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ParticipantDAOImpl implements ParticipantDAO {

    private final Connection connection;

    public ParticipantDAOImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Participant save(Participant participant) {
        String sql = "INSERT INTO participants (name, email, participant_type, representative_name) " +
                "VALUES (?,?,?,?)";

        try (
            // Statement.RETURN_GENERATED_KEYS is used to retrieve auto-generated ID values from INSERT statements
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {
            ps.setString(1, participant.getParticipantType());
            ps.setString(2, participant.getEmail());
            ps.setString(3, participant.getParticipantType());
            ps.setString(4, participant.getRepresentativeName());

            ps.executeUpdate();

            // Read auto-generated ID
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    participant.setId(rs.getInt(1));
                }
            }
    } catch (SQLException e) {
            System.err.println("❌ Error saving participant: " + e.getMessage());
            throw new RuntimeException("Error saving participant", e);
        }
        return participant;
    }

    @Override
    public Participant findById(Integer id) {
        String sql = "SELECT * FROM participants WHERE id = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1,id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Participant(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getString("email"),
                            rs.getString("participant_type"),
                            rs.getString("representative_name")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("❌ Error finding participant by ID: " + e.getMessage());
            throw new RuntimeException("Error finding participant", e);
        }
        return null; // If no participant was found with that ID
    }

    @Override
    public List<Participant> findAll() {

        List<Participant> participants = new ArrayList<>();
        String sql = "SELECT * FROM participants";

        try (
                PreparedStatement ps = connection.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()
        ) {
            while (rs.next()) {
                participants.add(new Participant(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("participant_type"),
                        rs.getString("representative_name")
                ));
            }

        } catch (SQLException e) {
            System.err.println("❌ Error fetching participants: " + e.getMessage());
            throw new RuntimeException("Error fetching participants", e);
        }
        return participants;
    }

    @Override
    public Participant findByName(String name) {
        String sql = "SELECT * FROM participants WHERE name = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, name);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    // Return a new participant object if found
                    return new Participant(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getString("email"),
                            rs.getString("participant_type"),
                            rs.getString("representative_name")
                    );
                }
            }
        } catch (SQLException e) {
            System.out.println("❌ Error finding participant by name: " + e.getMessage());
            throw new RuntimeException("Error finding participant by name", e);
        }
        // Returns null if no participant was found with that name
        return null;
    }
}
