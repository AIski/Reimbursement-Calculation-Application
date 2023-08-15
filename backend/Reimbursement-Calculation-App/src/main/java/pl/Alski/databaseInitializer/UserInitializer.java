package pl.Alski.databaseInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class UserInitializer {
    private final Logger logger = LoggerFactory.getLogger(UserInitializer.class);
    private final String createUserTableSQL = "CREATE TABLE IF NOT EXISTS `USER` (" +
            "ID INT PRIMARY KEY," +
            " FIRST_NAME VARCHAR(50) NOT NULL," +
            " LAST_NAME VARCHAR(50) NOT NULL)";
    private final String insertUsersQuery = "INSERT INTO `USER` " +
            "(ID, FIRST_NAME, LAST_NAME) " +
            "VALUES " +
            "(1, 'Adam', 'Glowacki'), " +
            "(2, 'Milosz', 'Nowak'), " +
            "(3, 'Kazimierz', 'Ryckiewicz'), " +
            "(4, 'Ewa', 'Majchrzak')," +
            "(5, 'Milena', 'Kolbielak')";

    public void initialize() {
        logger.info("--------------------------");
        logger.info("UserInitializer starting its job.");
        try {
            Connection connection = DriverManager.getConnection(
                    "jdbc:h2:mem:myDB;DB_CLOSE_DELAY=-1", "username", "password");
            Statement statement = connection.createStatement();
            logger.info("Creating USER table.");
            statement.execute(createUserTableSQL);
            logger.info("Pre-populating USER table.");
            statement.executeUpdate(insertUsersQuery);
            logger.info("USER table is ready.");
            logger.info("UserInitializer finished its job.");
            logger.info("--------------------------");
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
