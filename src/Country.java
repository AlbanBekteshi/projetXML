import java.awt.List;
import java.util.ArrayList;
import java.util.Map;

public class Country {
	
	private String name;
	private String cca3;
	private int population;
	
	public Country(String name, String cca3, int population) {
		this.name = name;
		this.cca3 = cca3;
		this.population = population;
	}
	
	public String getName() {
		return name;
	}

	public String getCca3() {
		return cca3;
	}

	public int getPopulation() {
		return population;
	}


	@Override
	public String toString() {
		return "Country [name=" + name + ", cca3=" + cca3 + ", population=" + population + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cca3 == null) ? 0 : cca3.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + population;
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
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (population != other.population)
			return false;
		return true;
	}
	
	
}
