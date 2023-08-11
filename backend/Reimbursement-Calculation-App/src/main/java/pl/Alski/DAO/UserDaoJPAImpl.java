package pl.Alski.DAO;

import pl.Alski.entity.user.User;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;

public class UserDaoJPAImpl implements UserDAO{

    private EntityManager entityManager;

    public UserDaoJPAImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<User> getUsers() {
        TypedQuery<User> theQuery = entityManager.createQuery("from USER", User.class);
        return theQuery.getResultList();
    }

    @Override
    public User getUserById(int id) {
        return entityManager.find(User.class, id);
    }

    @Override
    public void saveUser(User user) {
        User dbUser = entityManager.merge(user);
        user.setId(dbUser.getId());
    }

    @Override
    public void deleteById(int id) {
        Query theQuery = entityManager.createQuery("delete from USER where ID=:USER_ID");
        theQuery.setParameter("USER_ID", id);
        theQuery.executeUpdate();
    }
}
