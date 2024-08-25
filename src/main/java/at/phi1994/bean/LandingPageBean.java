package at.phi1994.bean;

import at.phi1994.model.Country;
import at.phi1994.model.Measurement;
import at.phi1994.service.CountryService;
import at.phi1994.service.MeasurementService;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.primefaces.model.charts.ChartData;
import org.primefaces.model.charts.line.LineChartDataSet;
import org.primefaces.model.charts.line.LineChartModel;
import org.primefaces.model.charts.line.LineChartOptions;
import org.primefaces.model.charts.optionconfig.title.Title;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Named("home")
@RequestScoped
public class LandingPageBean implements Serializable {

    private static final long serialVersionUID = 1582493768989126914L;

    // Data starts at 1990 all the time
    private static final List<String> LABEL_YEARS = List.of("1990","1991","1992","1993","1994","1995","1996","1997","1998","1999","2000","2001","2002","2003","2004","2005","2006","2007","2008","2009","2010","2011","2012","2013","2014","2015","2016","2017","2018","2019","2020","2021","2022","2023");

    @Inject
    private CountryService countryService;

    @Inject
    private MeasurementService measurementService;

    private Country country;

    private LineChartModel emissionChart;

    @PostConstruct
    public void init() {
        country = countryService.findByCode("AUT");
        createEmissionChart();
    }


    public void createEmissionChart() {
        emissionChart = new LineChartModel();

        Title title = new Title();
        title.setDisplay(true);
        title.setText("CO₂ Ausstoß in Kilotonnen");

        LineChartOptions options = new LineChartOptions();
        options.setTitle(title);
        emissionChart.setOptions(options);

        if (country == null) {
            return;
        }

        List<Object> co2InKilotons = new ArrayList<>();

        for (int year = Integer.parseInt(LABEL_YEARS.get(0)); year < Integer.parseInt(LABEL_YEARS.get(LABEL_YEARS.size() - 1)); year++) {
            Measurement found = null;
            for (Measurement m : measurementService.findByCountry(country)) {
                if (year == m.getYear()) {
                    found = m;

                    break;
                }
            }
            if (found != null) {
                co2InKilotons.add(found.getKiloTonsCO2());
            } else {
                co2InKilotons.add(null);
            }
        }

        LineChartDataSet dataSet = new LineChartDataSet();
        dataSet.setData(co2InKilotons);
        dataSet.setFill(true);
        dataSet.setLabel("kt CO₂");
        dataSet.setBorderColor("rgb(128, 72, 72)");
        dataSet.setTension(0.3);

        ChartData data = new ChartData();
        data.addChartDataSet(dataSet);
        data.setLabels(LABEL_YEARS);

        emissionChart.setData(data);
    }

    public List<Country> getCountries() {
        return countryService.findAll();
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public LineChartModel getEmissionChart() {
        return emissionChart;
    }

    public void setEmissionChart(LineChartModel emissionChart) {
        this.emissionChart = emissionChart;
    }
}
