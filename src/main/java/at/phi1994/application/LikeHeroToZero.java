package at.phi1994.application;

import at.phi1994.model.*;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.StringTokenizer;

@WebListener
@ApplicationScoped
public class LikeHeroToZero implements ServletContextListener {

    private final EntityManager em = EntityManagerProvider.getInstance().getEntityManager();

    /**
     * Initialize the application
     */
    @Override
    public void contextInitialized(ServletContextEvent contextEvent) {
        loadInitialData(contextEvent.getServletContext());
    }

    @Override
    public void contextDestroyed(ServletContextEvent contextEvent) {
        EntityManagerProvider.getInstance().shutdown();
    }

    private void loadInitialData(ServletContext context) {

        try(BufferedReader in = new BufferedReader(new InputStreamReader(context.getResourceAsStream("data/initial-data.csv")))) {
            // Headers: "Country Name","Country Code","Indicator Name","Indicator Code","1960","1961","1962","1963","1964","1965","1966","1967","1968","1969","1970","1971","1972","1973","1974","1975","1976","1977","1978","1979","1980","1981","1982","1983","1984","1985","1986","1987","1988","1989","1990","1991","1992","1993","1994","1995","1996","1997","1998","1999","2000","2001","2002","2003","2004","2005","2006","2007","2008","2009","2010","2011","2012","2013","2014","2015","2016","2017","2018","2019","2020","2021","2022","2023",
            String line;
            StringTokenizer tok;
            boolean isFirstLine = true;

            em.getTransaction().begin();

            // create the initial admin users
            User user = new User();
            user.setUsername("admin");
            user.setPassword("admin");
            em.persist(user);

            while ((line = in.readLine()) != null) {

                // skip the first line since it contains no data but just headers
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }

                tok = new StringTokenizer(line, ",");

                // get the country
                String countryName = tok.nextToken();
                String countryCode = tok.nextToken();
                Country country = new Country(normalize(countryCode), normalize(countryName));

                // skip "Indicator Name" and "Indicator Code" for now
                tok.nextToken();
                tok.nextToken();

                // get the CO2 in kilo tons for the years 1960 - 2023
                int year = 1960;
                while (year < 2023) {
                    String value = normalize(tok.nextToken());

                    if (value != null) {
                        BigDecimal ktCo2 = new BigDecimal(value).setScale(2, RoundingMode.HALF_UP);
                        Measurement measurement = new Measurement(country, ktCo2, year);
                        country.getMeasurements().add(measurement);
                    }

                    year++;
                }

                em.persist(country);
            }

            // delete countries without measurements
            em.createQuery("DELETE FROM Country c WHERE c.measurements.size = 0").executeUpdate();

            em.getTransaction().commit();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private String normalize(String str) {
        if (str == null) {
            return null;
        }

        str = str.substring(1, str.length() - 1).trim();
        return !str.isEmpty() ? str : null;
    }
}
