package se.lexicon;

import se.lexicon.dao.*;
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
            InvitationDAO invitationDAO = new InvitationDAOImpl(connection, participantDAO, eventDAO);

            // Start the UI
            CommunityCenterApp app = new CommunityCenterApp(participantDAO, eventDAO);
            app.start();

        } catch (SQLException e) {
            System.err.println("❌ Database connection failed!");
            e.printStackTrace();
        }
    }
    }

