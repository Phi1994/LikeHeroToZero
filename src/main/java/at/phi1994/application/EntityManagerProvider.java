package at.phi1994.application;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

/**
 * Apparently it is not possible to Inject the EntityManager into a @FacesConverter.
 * Thus, this little helper class exists.
 */
public class EntityManagerProvider {

    private static EntityManagerProvider instance;

    private final EntityManagerFactory emf;

    private EntityManagerProvider() {
        emf = Persistence.createEntityManagerFactory("default");
    }

    public static synchronized EntityManagerProvider getInstance() {
        if (instance == null) {
            instance = new EntityManagerProvider();
        }
        return instance;
    }

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void shutdown() {
        emf.close();
    }

}
