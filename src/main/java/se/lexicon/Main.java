package se.lexicon;

public class Main {
    static void main() {
        try {
            // Get the DataSource
            javax.sql.DataSource ds = se.lexicon.db.MySqlConnection.getMysqlDataSource();

            // Ask DataSource for a connection
            java.sql.Connection connection = ds.getConnection();

            if (connection != null) {
                System.out.println("Success: Connected using DataSource.");
            }
        } catch (java.sql.SQLException e) {
            System.out.println("Connection Failed!");
            e.printStackTrace();
        }
    }
    }

