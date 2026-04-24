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

            Participant test = new Participant("Shanmu","sarav07@gmail.com",
                    "Individual",null);
            participantDAO.save(test);

            System.out.println("Participant has been saved successfully:" + test.toString());
            System.out.println("Current list size: " + participantDAO.findAll().size());

        } catch (SQLException e) {
            System.err.println("Database error!");
            e.printStackTrace();
        }
    }
    }

