package internal;

import java.io.File;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import javax.xml.parsers.*;

import maps.Coordinate;
import maps.Line;
import maps.Stop;
import maps.Street;
import vehicles.Vehicle;

/**
 * Class that parses an input map schema.
 * Also this class contains all information about all elements of the map.
 * 
 * @author Mikhail Abramov (xabram00)
 * @author Serhii Salatskyi (xsalat00)
 *
 */
public class Parser {
    private static List<Street> streets;
    private static List<Stop> stops;
    private static List<Line> lines;
    private static List<Vehicle> vehicles;

    private static String backgroundPath;
    private static int width;
    private static int height;

    private Parser(){}

    /**
     * General method that parses all instances (streets, stops, and lines).
     * @param XML An input schema of the map.
     * @return An error message, if error exists, else null.
     */ 
    public static String parse(File XML) {
        streets = new ArrayList<>();
        stops = new ArrayList<>();
        lines = new ArrayList<>();
        vehicles = new ArrayList<>();

        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(XML);

            doc.getDocumentElement().normalize();

            Element mainNode = (Element) doc.getDocumentElement();
            
            backgroundPath = mainNode.getAttribute("background");
            if (!backgroundPath.equals("")){
                width = Integer.parseInt(mainNode.getAttribute("width"));
                height = Integer.parseInt(mainNode.getAttribute("height"));
            }
    
            parseStreets(doc.getElementsByTagName("street"));
            parseStops(doc.getElementsByTagName("stop"));
            parseLines(doc.getElementsByTagName("line"));
        } catch (Exception err) {
            return err.getMessage();
        }

        return null;
    }

    /**
     * Method that parses all streets.
     * @param list List of streets.
     */ 
    private static void parseStreets(NodeList list) throws Exception {
        int listLength = list.getLength();
        for (int index = 0; index < listLength; index++) {
            Element streetElement = (Element) list.item(index);
            NodeList listOfCoors = streetElement.getElementsByTagName("coordinate_street");
            int countOfCoors = listOfCoors.getLength();

            String id = streetElement.getAttribute("id");
            Coordinate[] coordinates = new Coordinate[countOfCoors]; 

            for (int coorIndex = 0; coorIndex < countOfCoors; coorIndex++) {
                Element coordinate = (Element) listOfCoors.item(coorIndex);
                int X = Integer.parseInt(coordinate.getAttribute("X"));
                int Y = Integer.parseInt(coordinate.getAttribute("Y"));

                coordinates[coorIndex] = (Coordinate.create(X, Y));
                if (coordinates[coorIndex] == null)
                    throw new Exception("Coordinate must not have negative axis");
            }

            streets.add(new Street(id, coordinates));
        }
    }

    /**
     * Method that parses all stops.
     * @param list List of stops.
     */ 
    private static void parseStops(NodeList list) throws Exception {
        int listLength = list.getLength();
        for (int index = 0; index < listLength; index++) {
            Element stopElement = (Element) list.item(index);

            Element coordinateElement = (Element) stopElement.getElementsByTagName("coordinate_stop").item(0);
            Element streetElement = (Element) stopElement.getElementsByTagName("street_stop").item(0);

            String id = stopElement.getAttribute("id");
            String streetId = streetElement.getTextContent();

            int X = Integer.parseInt(coordinateElement.getAttribute("X"));
            int Y = Integer.parseInt(coordinateElement.getAttribute("Y"));
            Coordinate coordinate = Coordinate.create(X, Y);

            Stop stop = new Stop(id, coordinate);
            for (Street street : streets) {
                if (street.getId().equals(streetId)) {
                    if (!street.addStop(stop)){
                        throw new Exception("Stop " + stop + " isn't on street " + streetId);
                    }
                }
            }

            stops.add(stop);
        }
    }

    /**
     * Method that parses all lines.
     * @param list List of lines.
     */ 
    private static void parseLines(NodeList list) throws Exception {
        int listLength = list.getLength();
        for (int index = 0; index < listLength; index++) {
            Element lineElement = (Element) list.item(index);

            String id = lineElement.getAttribute("id");
            String color = lineElement.getAttribute("color");
            String type = lineElement.getAttribute("type");
            Line line = new Line(id, color, type);

            NodeList listOfStops = lineElement.getChildNodes();
            int stopsLength = listOfStops.getLength();
            for (int stopIndex = 0; stopIndex < stopsLength; stopIndex++) {
                Node stopNode = listOfStops.item(stopIndex);
                String nodeName = stopNode.getNodeName();

                switch (nodeName){
                    case "stop_line":
                        String stopId = stopNode.getTextContent();

                        for (Stop stop : stops) {
                            if (stop.getId().equals(stopId)) {
                                if (!line.addStop(stop)){
                                    throw new Exception("Stop " + stopId + 
                                                        " wasn't added to the route:" + 
                                                        "stop isn'n in a street, " +
                                                        "which must follow last street");
                                }
                                break;
                            }
                        }
                        break;

                    case "street_line":
                        String streetId = stopNode.getTextContent();

                        for (Street street : streets) {
                            if (street.getId().equals(streetId)) {
                                if (!line.addStreet(street)){
                                    throw new Exception("Street " + streetId + 
                                                        " wasn't added to the route:" + 
                                                        "route is empty or " +
                                                        "street doesn't follow last street"); 
                                }
                                break;
                            }
                        }
                        break;
                }
            }

            NodeList listOfTimes = lineElement.getElementsByTagName("time");
            int timesLength = listOfTimes.getLength();
            for (int timeIndex = 0; timeIndex < timesLength; timeIndex++) {
                Element timeElement = (Element) listOfTimes.item(timeIndex);

                LocalTime from = LocalTime.parse(timeElement.getAttribute("from"),
                                                 DateTimeFormatter.ofPattern("HH:mm"));
                LocalTime to = LocalTime.parse(timeElement.getAttribute("to"), 
                                               DateTimeFormatter.ofPattern("HH:mm"));

                Vehicle vehicle = line.createVehicle(from, to);
                vehicles.add(vehicle);
            }

            lines.add(line);
        }
    }

    /**
     * Returns the list of streets.
     * @return The list of streets.
    */
    public static List<Street> getStreets() {
        return streets;
    }

    /**
     * Returns the list of stops.
     * @return The list of stops.
    */
    public static List<Stop> getStops() {
        return stops;
    }

    /**
     * Returns the list of lines.
     * @return The list of lines.
    */
    public static List<Line> getLines() {
        return lines;
    }

    /**
     * Returns the list of vehicles.
     * @return The list of vehicles.
    */
    public static List<Vehicle> getVehicles() {
        return vehicles;
    }

    /**
     * Returns the path to the background.
     * @return The path to the background.
    */
    public static String getBackgroundPath() {
        return backgroundPath;
    }
    
    /**
     * Returns the width of the background.
     * @return The width of the background.
    */
    public static int getWidth() {
        return width;
    }

    /**
     * Returns the height of the background.
     * @return The height of the background.
    */
    public static int getHeight() {
        return height;
    }
}
