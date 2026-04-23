package se.lexicon.model;

public class Invitation {
    private int eventId;
    private int participantId;
    private Status status;

    public Invitation(int eventId, int participantId, Status status) {
        this.eventId = eventId;
        this.participantId = participantId;
        this.status = status;
    }

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public int getParticipantId() {
        return participantId;
    }

    public void setParticipantId(int participantId) {
        this.participantId = participantId;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Invitation{" +
                "eventId=" + eventId +
                ", participantId=" + participantId +
                ", status=" + status +
                '}';
    }
}
