package se.lexicon.dao;

import se.lexicon.model.Participant;

import java.util.List;

public interface ParticipantDAO {
    Participant save(Participant participant);
    Participant findById(Integer id);
    List<Participant> findAll();
}
