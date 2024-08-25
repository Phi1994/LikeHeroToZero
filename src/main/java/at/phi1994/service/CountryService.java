package at.phi1994.service;

import java.util.List;

import at.phi1994.application.EntityManagerProvider;
import jakarta.persistence.EntityManager;
import at.phi1994.model.Country;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class CountryService {

	private final EntityManager em = EntityManagerProvider.getInstance().getEntityManager();

	public Country findById(int id) {
		return em.find(Country.class, id);
	}

	public List<Country> findAll() {
		return em.createQuery("SELECT c FROM Country c ORDER BY c.name", Country.class)
				.getResultList();
	}

	public Country findByCode(String code) {
		return em.createQuery("SELECT c FROM Country c WHERE c.code = :code", Country.class)
				.setParameter("code", code)
				.getSingleResult();
	}

	public Country add(Country country) {
		if (country.getId() == null) {
			em.persist(country);
		} else {
			em.merge(country);
		}
		return country;
	}

}