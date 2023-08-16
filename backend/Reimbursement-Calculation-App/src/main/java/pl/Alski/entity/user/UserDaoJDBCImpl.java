package pl.Alski.entity.user;

import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
public class UserDaoJDBCImpl implements UserDao {
    private final Logger logger = LoggerFactory.getLogger(UserDaoJDBCImpl.class);
    private static final String DB_URL = "jdbc:h2:mem:myDB;DB_CLOSE_DELAY=-1";
    private static final String DB_USER = "username";
    private static final String DB_PASSWORD = "password";

    private final String selectFromUserQuery = "SELECT ID, FIRST_NAME, LAST_NAME FROM `USER`";
    private final String insertIntoUserQuery = "INSERT INTO `USER` (ID, FIRST_NAME, LAST_NAME, COMPANY_NAME, EMPLOYED_AT) " +
            "VALUES (?, ?, ?, ?, ?)";
    private final String getUserByIdQuery = "SELECT * FROM USER WHERE ID = ?";

    @Override
    public List<User> getUsers() {
        logger.info("Getting users...");
        List<User> users = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(selectFromUserQuery);
            while (resultSet.next()) {
               User user = getUserFromResultSet(resultSet);
               users.add(user);
            }
            logger.info("Successfully fetched users from db");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    @Override
    public void saveUser(User user) {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            try (PreparedStatement statement = connection.prepareStatement(insertIntoUserQuery)) {
                statement.setInt(1, user.getId());
                statement.setString(2, user.getFirstName());
                statement.setString(3, user.getLastName());
                statement.setString(4, user.getCompanyName());
                statement.setObject(5, user.getEmployedAt());
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public User getUserById(int id) {
        User user = null;
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(getUserByIdQuery)) {
            preparedStatement.setInt(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    user = getUserFromResultSet(resultSet);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    private User getUserFromResultSet(ResultSet resultSet) throws SQLException {
        int userId = resultSet.getInt("ID");
        String firstName = resultSet.getString("FIRST_NAME");
        String lastName = resultSet.getString("LAST_NAME");

        return new User(userId, firstName, lastName);
    }


}
