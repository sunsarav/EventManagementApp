package se.lexicon;

import se.lexicon.dao.*;
import se.lexicon.db.MySqlConnection;

import java.sql.Connection;
import java.sql.SQLException;

public class Main {
    static void main() {
        try {
            // Connection Logic
            Connection connection = MySqlConnection.getMysqlDataSource().getConnection();
            connection.setAutoCommit(true);

            // Initialize DAO
            ParticipantDAO participantDAO = new ParticipantDAOImpl(connection);
            EventDAO eventDAO = new EventDAOImpl(connection);
            InvitationDAO invitationDAO = new InvitationDAOImpl(connection, participantDAO, eventDAO);

            // Start the UI
            CommunityCenterApp app = new CommunityCenterApp(connection, participantDAO, eventDAO, invitationDAO);
            app.start();

        } catch (SQLException e) {
            System.err.println("❌ Database connection failed!");
            e.printStackTrace();
        }
    }
    }

