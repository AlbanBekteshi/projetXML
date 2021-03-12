
public class Country {
	
	private String name;
	private String capital;
	private String cca3;
	private String currency;
	private String languages;
	private String latlng;
	private int population;
	private String region;
	private String subregion;
	
	public Country(String name, String capital, String cca3, String currency, String languages, String latlng, int population, String region, String subregion) {
		this.name = name;
		this.capital = capital;
		this.cca3 = cca3;
		this.currency = currency;
		this.languages = languages;
		this.latlng = latlng;
		this.population = population;
		this.region = region;
		this.subregion = subregion;
	}

	public String getName() {
		return name;
	}

	public String getCapital() {
		return capital;
	}

	public String getCca3() {
		return cca3;
	}

	public String getCurrency() {
		return currency;
	}

	public String getLanguages() {
		return languages;
	}

	public String getLatlng() {
		return latlng;
	}

	public int getPopulation() {
		return population;
	}

	public String getRegion() {
		return region;
	}

	public String getSubregion() {
		return subregion;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cca3 == null) ? 0 : cca3.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Country other = (Country) obj;
		if (cca3 == null) {
			if (other.cca3 != null)
				return false;
		} else if (!cca3.equals(other.cca3))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Country [name=" + name + ", capital=" + capital + ", cca3=" + cca3 + ", currency=" + currency
				+ ", languages=" + languages + ", latlng=" + latlng + ", population=" + population + ", region="
				+ region + ", subregion=" + subregion + "]";
	}
	
	
}
