package se.lexicon.model;

public class Participant {
    private int id;
    private String name;
    private String email;
    private String participantType; // 'Individual' or 'Organization'
    private String representativeName; // Can be null

    public Participant(int id, String name, String email, String participantName, String representativeName) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.participantType = participantType;
        this.representativeName = representativeName;
    }
    // New participants where DB handles the ID
    public Participant(String name, String email, String participantType, String representativeName) {
        this.name = name;
        this.email = email;
        this.participantType = participantType;
        this.representativeName = representativeName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getParticipantType() {
        return participantType;
    }

    public String getRepresentativeName() {
        return representativeName;
    }

    @Override
    public String toString() {
        return "Participant{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", participantName='" + participantType + '\'' +
                ", representativeName='" + representativeName + '\'' +
                '}';
    }
}

