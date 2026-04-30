package se.lexicon.model;

public class Invitation {
    private int id;                  // ID for database tracking
    private final Event event;             // Composition: Invitation "has an" Event
    private Participant participant; // Composition: Invitation "has a" Participant
    private Status status;

    // Constructor for creating NEW invitations
    public Invitation(Event event, Participant participant, Status status) {
        this.event = event;
        this.participant = participant;
        this.status = status;
    }
    // Constructor for Loading from Database (includes ID)
    public Invitation(int id, Event event, Participant participant, Status status) {
        this.id = id;
        this.event = event;
        this.participant = participant;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Event getEvent() {
        return event;
    }

    public Participant getParticipant() {
        return participant;
    }

    public void setParticipant(Participant participant) {
        this.participant = participant;
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
                "id=" + id +
                ", event=" + event +
                ", participant=" + participant +
                ", status=" + status +
                '}';
    }
}

