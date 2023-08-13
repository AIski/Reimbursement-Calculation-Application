package pl.Alski.DAO;

import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.Alski.Main;
import pl.Alski.entity.user.User;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;

import static pl.Alski.EntityManagerFactoryHelper.getFactory;

@NoArgsConstructor
public class UserDaoJPAImpl implements UserDao {

    @PersistenceContext
    private final EntityManager entityManager = getFactory().createEntityManager();
    private final Logger logger = LoggerFactory.getLogger(UserDaoJPAImpl.class);

    @Override
    public List<User> getUsers() {
        logger.info("Getting users...");
        TypedQuery<User> theQuery = entityManager.createQuery("from USER", User.class);
        return theQuery.getResultList();
    }

    @Override
    public User getUserById(int id) {

        logger.info("Getting user by id " + id);
        return entityManager.find(User.class, id);
    }

    @Override
    public void saveUser(User user) {
        logger.info("Saving user " + user.getFirstName());
        User dbUser = entityManager.merge(user);
        user.setId(dbUser.getId());
    }

    @Override
    public void deleteById(int id) {
        logger.info("Deleting user with id" +id);
        Query theQuery = entityManager.createQuery("delete from USER where ID=:USER_ID");
        theQuery.setParameter("USER_ID", id);
        theQuery.executeUpdate();
    }
}
