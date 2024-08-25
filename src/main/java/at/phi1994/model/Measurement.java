package at.phi1994.model;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
public class Measurement {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @ManyToOne(optional = false)
    private Country country;

    @Column(name = "kilotons_co2", nullable = false)
    private BigDecimal kiloTonsCO2;

    @Column(nullable = false, length = 4)
    private Integer year;

    public Measurement() {
    }

    public Measurement(Country country, BigDecimal kiloTonsCO2, Integer year) {
        this.country = country;
        this.kiloTonsCO2 = kiloTonsCO2;
        this.year = year;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public BigDecimal getKiloTonsCO2() {
        return kiloTonsCO2;
    }

    public void setKiloTonsCO2(BigDecimal kiloTonsCO2) {
        this.kiloTonsCO2 = kiloTonsCO2;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }
}
