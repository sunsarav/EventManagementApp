package se.lexicon.dao;

import se.lexicon.model.Event;

import java.util.List;

public interface EventDAO {
    Event save(Event event);
    Event findById(Integer id);
    List<Event> findAll();
}
