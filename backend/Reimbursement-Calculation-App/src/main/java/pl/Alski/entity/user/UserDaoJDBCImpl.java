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

    @Override
    public List<User> getUsers() {
        logger.info("Getting users...");
        List<User> users = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(
                "jdbc:h2:mem:myDB;DB_CLOSE_DELAY=-1", "username", "password")) {
            String query = "SELECT ID, FIRST_NAME, LAST_NAME FROM `USER`";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String firstName = resultSet.getString("FIRST_NAME");
                String lastName = resultSet.getString("LAST_NAME");
                users.add(new User(id, firstName, lastName));
            }
            logger.info("Successfully fetched users from db");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    @Override
    public void saveUser(User user) {
        try (Connection connection = DriverManager.getConnection(
                "jdbc:h2:mem:myDB;DB_CLOSE_DELAY=-1", "username", "password")) {
            String query = "INSERT INTO `USER` (ID, FIRST_NAME, LAST_NAME, COMPANY_NAME, EMPLOYED_AT) " +
                    "VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                stmt.setInt(1, user.getId());
                stmt.setString(2, user.getFirstName());
                stmt.setString(3, user.getLastName());
                stmt.setString(4, user.getCompanyName());
                stmt.setObject(5, user.getEmployedAt());
                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    }


}
