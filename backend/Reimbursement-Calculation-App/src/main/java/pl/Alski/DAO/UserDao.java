package pl.Alski.DAO;

import pl.Alski.entity.user.User;

import java.util.List;

public interface UserDao {
    public List<User> getUsers();
    public User getUserById(int id);
    public void saveUser(User user);
    public void deleteById(int id);
}
