package se.lexicon.model;

public class Individual extends Participant {
    public Individual(int id, String name, String email) {
        super(id, name, email);
    }

    @Override
    public String toString() {
        return "Individual: " + super.toString();
    }
}
