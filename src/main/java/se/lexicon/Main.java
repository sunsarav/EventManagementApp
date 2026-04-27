package se.lexicon;

import se.lexicon.dao.EventDAO;
import se.lexicon.dao.EventDAOImpl;
import se.lexicon.dao.ParticipantDAO;
import se.lexicon.dao.ParticipantDAOImpl;
import se.lexicon.db.MySqlConnection;
import se.lexicon.model.Participant;
import se.lexicon.ui.CommunityCenterApp;

import java.sql.Connection;
import java.sql.SQLException;

public class Main {
    static void main() {
        try {
            // Connection Logic
            Connection connection = MySqlConnection.getMysqlDataSource().getConnection();

            // Initialize DAO
            ParticipantDAO participantDAO = new ParticipantDAOImpl(connection);
            EventDAO eventDAO = new EventDAOImpl(connection);

            // Start the UI
            CommunityCenterApp app = new CommunityCenterApp(participantDAO, eventDAO);
            app.start();

        } catch (SQLException e) {
            System.err.println("❌ Database connnection failed!");
            e.printStackTrace();
        }
    }
    }

