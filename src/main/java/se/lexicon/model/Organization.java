package se.lexicon.model;

public class Organization extends Participant {
    private String representativeName;

    public Organization(int id, String name, String email, String participantName,
                        String representativeName, String representativeName1) {
        super(id, name, email, participantName, representativeName);
        this.representativeName = representativeName1;
    }

    public String getRepresentativeName() {
        return representativeName;
    }

    public void setRepresentativeName(String representativeName) {

        this.representativeName = representativeName;
    }

    @Override
    public String toString() {
        return super.toString() + "Organization{" +
                "representativeName='" + representativeName + '\'' +
                '}';
    }
}
