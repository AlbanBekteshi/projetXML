
public class Route {
	
	private Country start;
	private Country finish;
	
	public Route(Country start, Country finish) {
		this.start = start;
		this.finish = finish;
	}

	public Country getStart() {
		return start;
	}

	public Country getFinish() {
		return finish;
	}

	@Override
	public String toString() {
		return "Route [start=" + start.getCca3() + ", finish=" + finish.getCca3() + "]";
	}
	
	
	
}
