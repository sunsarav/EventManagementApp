package se.lexicon.model;

public class Individual extends Participant {
    public Individual(int id, String name, String email, String participantName,
                      String representativeName) {
        super(id, name, email, participantName, representativeName);
    }

    @Override
    public String toString() {
        return "Individual: " + super.toString();
    }
}
