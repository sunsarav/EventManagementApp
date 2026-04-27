package se.lexicon.dao;


import se.lexicon.model.Event;
import se.lexicon.model.Invitation;
import se.lexicon.model.Participant;
import se.lexicon.model.Status;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InvitationDAOImpl implements InvitationDAO {
    private final Connection connection;
    private final ParticipantDAO participantDAO;
    private final EventDAO eventDAO;

    // Uses other DAOs as helpers to get full details for participants and events.
    public InvitationDAOImpl(Connection connection, ParticipantDAO participantDAO, EventDAO eventDAO) {
        this.connection = connection;
        this.participantDAO = participantDAO;
        this.eventDAO = eventDAO;
    }

    @Override
    public Invitation save(Invitation invitation) {
        String sql = "INSERT INTO invitations (participant_id, event_id, status) VALUES (?, ?, ?)";

        try (PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, invitation.getParticipant().getId());
            ps.setInt(2,invitation.getEvent().getId());
            ps.setString(3, invitation.getStatus().name());

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    invitation.setId(rs.getInt(1));
                }
            }
        } catch (SQLException e) {
            System.err.println("❌ This participant is already invited to this event. " + e.getMessage());
            throw new RuntimeException("Error saving invitation", e);
        }
        return invitation;
    }

    @Override
    public List<Invitation> findByEventId(int eventId) {
        List<Invitation> invitations = new ArrayList<>();
        String sql = "SELECT * FROM invitations WHERE event_id = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, eventId);

                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        invitations.add(mapRowToInvitation(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("❌ Error searching invitations", e);
        }
        return invitations;
    }

    @Override
    public void updateStatus(int invitationId, Status status) {
        String sql = "UPDATE invitations SET status = ? WHERE id = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)){
            ps.setString(1, status.name());
            ps.setInt(2, invitationId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("❌ Error updating status", e);
        }
    }


    @Override
    public List<Invitation> findByParticipantId(int participantId) {
        List<Invitation> invitations = new ArrayList<>();
        String sql = "SELECT * FROM invitations WHERE participant_id = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, participantId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    // Using helper method to "build" the full invitation object
                    invitations.add(mapRowToInvitation(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("❌ Error finding invitations for participant: " +e.getMessage());
            throw new RuntimeException("Error searching invitations", e);
        }
        // will be empty if no invitations found
        return invitations;
    }
    // Helper Method - For Composition
    private Invitation mapRowToInvitation(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        int participantId = rs.getInt("participant_id");
        int eventId = rs.getInt("event_id");
        Status status = Status.valueOf(rs.getString("status"));

    // Using other DAOs to get the actual objects
        Participant participant = participantDAO.findById(participantId);
        Event event = eventDAO.findById(eventId);

    // Safety Check: objects exists before creating the invitation
        if (participant == null || event == null) {
            return null;
        }
        return new Invitation(id, event, participant, status);
    }
}

