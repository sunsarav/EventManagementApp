package se.lexicon;

import se.lexicon.dao.ParticipantDAO;
import se.lexicon.dao.ParticipantDAOImpl;
import se.lexicon.db.MySqlConnection;
import se.lexicon.model.Participant;

import java.sql.Connection;
import java.sql.SQLException;

public class Main {
    static void main() {
        try {
            // Connection Logic
            Connection connection = MySqlConnection.getMysqlDataSource().getConnection();

            // Initialize DAO
            ParticipantDAO participantDAO = new ParticipantDAOImpl(connection);

            Participant test = new Participant("Shanmu","krish@gmail.com",
                    "Individual",null);

            // 1. Test SAVE
            participantDAO.save(test);
            System.out.println("1. SAVE TEST: Saved successfully:" + test.getName() +
                    " with ID: " + test.getId());

            // 2. Test FIND BY ID
            Participant found = participantDAO.findById(test.getId());
            System.out.println("2. FIND BY ID TEST: Found Name is -> " +
                    (found != null ? found.getName() : "Not Found"));

            // 3. TEST FIND ALL
            System.out.println("3. FIND ALL TEST: Total rows in DB : " +
                    participantDAO.findAll().size());

        } catch (SQLException e) {
            System.err.println("Database error!");
            e.printStackTrace();
        }
    }
    }

