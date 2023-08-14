package pl.Alski.DAO;

import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.Alski.entity.user.User;

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
        try {
            Connection connection = DriverManager.getConnection(
                    "jdbc:h2:mem:myDB;DB_CLOSE_DELAY=-1", "username", "password");
            String query = "SELECT ID, FIRST_NAME, LAST_NAME FROM `USER`";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String firstName = resultSet.getString("FIRST_NAME");
                String lastName = resultSet.getString("LAST_NAME");
                users.add(new User(id, firstName, lastName));
            }
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    @Override
    public User getUserById(int id) {
        logger.info("Getting user by id " + id);
        return null;
    }

    @Override
    public void saveUser(User user) {
        logger.info("Saving user " + user.getFirstName());
    }

    @Override
    public void deleteById(int id) {
        logger.info("Deleting user with id" + id);
    }
}
