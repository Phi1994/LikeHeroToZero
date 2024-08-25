package at.phi1994.bean;

import at.phi1994.model.Country;
import at.phi1994.model.Measurement;
import at.phi1994.service.CountryService;
import at.phi1994.service.MeasurementService;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

@Named("dashboard")
@RequestScoped
public class DashboardBean implements Serializable {

    private static final long serialVersionUID = -6860847121544398465L;

    @Inject
    private CountryService countryService;

    @Inject
    private MeasurementService measurementService;

    private Country country;

    private Measurement measurement;


    @PostConstruct
    public void init() {
        country = countryService.findByCode("AUT");
        measurement = new Measurement();
    }


    public List<Country> getCountries() {
        return countryService.findAll();
    }

    public List<Measurement> getMeasurements() {
        if (country == null) {
            return Collections.emptyList();
        }
        return measurementService.findByCountry(country);
    }

    public void deleteMeasurement(Integer id) {
        measurementService.deleteById(id);
    }

    public void saveMeasurement() {
        measurement.setCountry(country);
        measurementService.save(measurement);
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public Measurement getMeasurement() {

        return measurement;
    }

    public void setMeasurement(Measurement measurement) {
        this.measurement = measurement;
    }
}
