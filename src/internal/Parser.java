package internal;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Element;
import javax.xml.parsers.*;

import maps.Coordinate;
import maps.Line;
import maps.Stop;
import maps.Street;

public class Parser {
    private static List<Street> streets = new ArrayList<>();
    private static List<Stop> stops = new ArrayList<>();
    private static List<Line> lines = new ArrayList<>();

    public static void parse(File XML){

        parseStreets(XML);
        parseStops(XML);
        parseLines(XML);
    }

    private static void parseStreets(File file){
        NodeList list = getListOfNodes(file, "street");

        for (int index = 0; index < list.getLength(); index++) {
            Element streetElement = (Element) list.item(index);;
            String id = streetElement.getAttribute("id");
            List<Coordinate> coors = new ArrayList<>();

            NodeList listOfCoors = streetElement.getElementsByTagName("coordinate_street");
            for (int j = 0; j < listOfCoors.getLength(); j++) {
                Element coor = (Element) listOfCoors.item(j);
                int X = Integer.parseInt(coor.getAttribute("X"));
                int Y = Integer.parseInt(coor.getAttribute("Y"));

                coors.add(Coordinate.create(X, Y));
            }

            Coordinate[] coordinates = new Coordinate[coors.size()];
            coors.toArray(coordinates);

            streets.add(Street.create(id, coordinates));
        }
    }

    private static void parseStops(File file){
        NodeList list = getListOfNodes(file, "stop");

        for (int index = 0; index < list.getLength(); index++) {
            Element stopElement = (Element) list.item(index);;
            Element coordinateElement = (Element) stopElement
                                          .getElementsByTagName("coordinate_stop")
                                          .item(0);
            Element streetElement = (Element) stopElement
                                             .getElementsByTagName("street_stop")
                                             .item(0);

            String id = stopElement.getAttribute("id");

            int X = Integer.parseInt(coordinateElement.getAttribute("X"));
            int Y = Integer.parseInt(coordinateElement.getAttribute("Y"));
            Coordinate coordinate = Coordinate.create(X, Y);

            String streetId = streetElement.getTextContent();

            Stop stop = new Stop(id, coordinate);
            for(Street street:streets){
                if (street.getId().equals(streetId)){
                    stop.setStreet(street);
                }
            }

            stops.add(stop);
        }
    }

    private static void parseLines(File file){
        NodeList list = getListOfNodes(file, "line");

        for (int index = 0; index < list.getLength(); index++) {
            Element lineElement = (Element) list.item(index);;
            
            String id = lineElement.getAttribute("id");
            Line line = new Line(id);

            NodeList listOfStops = lineElement.getElementsByTagName("stop_line");
            for (int j = 0; j < listOfStops.getLength(); j++) {
                Element stopElement = (Element) listOfStops.item(j);
                String stopId = stopElement.getTextContent();

                for(Stop stop: stops){
                    if (stop.getId().equals(stopId)){
                        line.addStop(stop);
                    }
                }
            }
            
            lines.add(line);
        }
    }

    private static NodeList getListOfNodes(File file, String node){
        try{
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(file);

            doc.getDocumentElement().normalize();

            return doc.getElementsByTagName(node);
        } 
        catch (Exception e){
            System.err.println("Got an error");
            return null;
        }
    }

    public static List<Street> getStreets(){
        return streets;
    }
}

