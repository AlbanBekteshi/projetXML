import org.xml.sax.Attributes;
import org.xml.sax.*;
import org.xml.sax.helpers.DefaultHandler;

public class SAXHandler extends DefaultHandler {

    private Graph graph;
    private Country country;
    private Route route;
    private boolean border;

    @Override
    public void startDocument() throws SAXException {
        graph = new Graph();
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if(qName.equalsIgnoreCase("country")) {
            country = new Country(attributes.getValue("name"), attributes.getValue("cca3"), Integer.parseInt(attributes.getValue("population")));
            graph.ajouterSommet(country, country.getCca3());
        }
        if(qName.equalsIgnoreCase("border")) {
        	border = true;
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        if(border) {
        	//méthode pour ajouter un border
        	route = new Route(country, new String(ch, start, length));
        	graph.ajouterArc(route);
        	border = false;
        }
    }


    public Graph getGraph() {
        return graph;
    }


}