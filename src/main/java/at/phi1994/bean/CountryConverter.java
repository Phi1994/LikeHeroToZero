package at.phi1994.bean;

import at.phi1994.service.CountryService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.FacesConverter;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import at.phi1994.model.Country;

@Named
@ApplicationScoped
@FacesConverter(value = "countryConverter", managed = true)
public class CountryConverter implements Converter<Country> {

	@Inject
	private CountryService countryService;

	@Override
	public Country getAsObject(FacesContext ctx, UIComponent component, String value) {
		if (value == null) {
			return null;
		}

		return  countryService.findById(Integer.parseInt(value.trim()));
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Country value) {
		if (value == null) {
			return "";
		}
		return String.valueOf(value.getId());
	}

}
