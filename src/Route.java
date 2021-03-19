
public class Route {
	
	private Country start;
	private String finish;
	
	public Route(Country start, String finish) {
		this.start = start;
		this.finish = finish;
	}

	public Country getStart() {
		return start;
	}

	public String getFinish() {
		return finish;
	}

	@Override
	public String toString() {
		return "Route [start=" + start.getCca3() + ", finish=" + finish + "]";
	}
	
	
	
}
