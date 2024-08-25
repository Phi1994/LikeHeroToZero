package at.phi1994.service;

import at.phi1994.application.EntityManagerProvider;
import at.phi1994.model.User;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;

import java.util.List;

@ApplicationScoped
public class UserService {

    private final EntityManager em = EntityManagerProvider.getInstance().getEntityManager();

    public User findByUsernameAndPassword(String username, String password) {
        List<User> result = em.createQuery("SELECT u FROM User u WHERE u.username = :username AND u.password = :password", User.class)
                .setParameter("username", username)
                .setParameter("password", password)
                .setMaxResults(1)
                .getResultList();

        if (result.isEmpty()) {
            return null;
        }

        return result.get(0);
    }

}
