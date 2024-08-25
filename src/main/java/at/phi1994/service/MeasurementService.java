package at.phi1994.service;

import at.phi1994.application.EntityManagerProvider;
import at.phi1994.model.Country;
import at.phi1994.model.Measurement;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;

import java.util.List;

@ApplicationScoped
public class MeasurementService {

    private final EntityManager em = EntityManagerProvider.getInstance().getEntityManager();

    public void save(Measurement measurement) {
        em.getTransaction().begin();
        em.persist(measurement);
        em.getTransaction().commit();
    }

    public List<Measurement> findByCountry(Country country) {
        return em.createQuery("SELECT m FROM Measurement m WHERE m.country = :country", Measurement.class)
                .setParameter("country", country)
                .getResultList();
    }

    public void deleteById(Integer id) {
        em.getTransaction().begin();
        em.createQuery("DELETE FROM Measurement m WHERE m.id = :id")
                .setParameter("id", id)
                .executeUpdate();
        em.getTransaction().commit();
    }

}
