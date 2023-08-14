package pl.Alski;

import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

@NoArgsConstructor
public class DatabaseInitializer {
    private final Logger logger = LoggerFactory.getLogger(DatabaseInitializer.class);
    private final String createTableSQL = "CREATE TABLE IF NOT EXISTS `USER` (" +
            "ID INT PRIMARY KEY," +
            " FIRST_NAME VARCHAR(50) NOT NULL," +
            " LAST_NAME VARCHAR(50) NOT NULL)";
    private final String insertQuery = "INSERT INTO `USER` " +
            "(ID, FIRST_NAME, LAST_NAME) " +
            "VALUES " +
            "(1, 'Adam', 'Glowacki'), " +
            "(2, 'Milosz', 'Nowak'), " +
            "(3, 'Kazimierz', 'Ryckiewicz'), " +
            "(4, 'Ewa', 'Majchrzak')";

    public void initialize() throws SQLException {
        logger.info("--------------------------");
        logger.info("DatabaseInitializer starting its job.");
        try {
            Connection connection = DriverManager.getConnection(
                    "jdbc:h2:mem:myDB;DB_CLOSE_DELAY=-1", "username", "password");
            Statement statement = connection.createStatement();
            logger.info("Creating USER table.");
            statement.execute(createTableSQL);
            logger.info("Done creating USER table.");
            logger.info("--------------------------");
            logger.info("Pre-populating USER table.");
            statement.executeUpdate(insertQuery);
            logger.info("Done Pre-populating USER table.");
            logger.info("DatabaseInitializer finished its job.");
            logger.info("--------------------------");
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
