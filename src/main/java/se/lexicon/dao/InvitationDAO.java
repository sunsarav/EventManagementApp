package se.lexicon.dao;

import se.lexicon.model.Invitation;
import se.lexicon.model.Status;

import java.util.List;

public interface InvitationDAO {
    Invitation save(Invitation invitation);
    List<Invitation> findByEventId(int eventId);
    List<Invitation> findByParticipantId(int participantId);
    void updateStatus(int invitationId, Status status);
}
