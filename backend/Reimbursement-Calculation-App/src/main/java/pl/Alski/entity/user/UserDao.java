package pl.Alski.entity.user;

import java.util.List;

public interface UserDao {
    public List<User> getUsers();

    void saveUser(User user);

    User getUserById(int id);
}
