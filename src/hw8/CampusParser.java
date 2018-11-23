package hw8;

import java.util.*;
import java.io.*;
import hw5.*;
import hw6.MarvelParser.MalformedDataException;

/**
 * Parser used to load campus buildings and paths from well-formed files.
 * 
 * @author Rohan Guliani
 *
 */
public class CampusParser {

	/**
	 * Static method that takes in the name of the well-formed
	 * file for the buildings, and empty Maps and fills the maps
	 * with the appropriate data (names and locations)
	 * @param buildings: file name for the well-formed buildings file
	 * @param buildingNames: The empty Map for building names, initially empty
	 *                       Keys of the maps are abbr. names and the values are
	 *                       the corresponding long names
	 * @param buildingLocs: Empty map for building locations, initially empty
	 *                      Keys of the map are abbr. names and the values are
	 *                      the corresponding locations
	 * @requires buildings to be non-null and a valid file in the system and
	 *           buildingNames, buildingLocs to be non null
	 * @throws MalformedDataException if the buildings file is malformed
	 */
    public static void parseBuildings(String buildings,
                                      Map<String, String> buildingNames, 
                                      Map<String, Point> buildingLocs)
                                      throws MalformedDataException {

        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(buildings));

            String inputLine;
            while ((inputLine = reader.readLine()) != null) {

                // Ignore comment lines.
                if (inputLine.startsWith("#")) {
                    continue;
                }

                // Parse the data, stripping out quotation marks and throwing
                // an exception for malformed lines.
                inputLine = inputLine.replace("\"", "");
                String[] tokens = inputLine.split("\t");
                if (tokens.length != 4) {
                    throw new MalformedDataException("bad format");
                }

                String abbrName = tokens[0];
                String fullName = tokens[1];
                double x = Double.parseDouble(tokens[2]);
                double y = Double.parseDouble(tokens[3]);
                Point p = new Point(x, y);

                buildingNames.put(abbrName, fullName);
                buildingLocs.put(abbrName, p);
            }
        } catch (IOException e) {
            System.err.println(e.toString());
            e.printStackTrace(System.err);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    System.err.println(e.toString());
                    e.printStackTrace(System.err);
                }
            }
        }
    }
    
    /**
     * Static method that takes in an empty campus graph and a path
     * to the well-formed paths file and fills the Graph with all
     * possible paths
     * @param campusGraph: The empty graph
     * @param paths: The name of the path to the well-formed paths file
     * @requires paths to be non-null and a valid file in the system and
     *           campusGraph to be non null
     * @throws MalformedDataException: if the file is malformed. 
     */
    public static void createCampusGraph (Graph<Point, Double> campusGraph, 
                                          String paths) 
                                          throws MalformedDataException {
        
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(paths));
            
            String inputLine;
            Point currLoc = null;
            while((inputLine = reader.readLine()) != null){
                
                // Ignore comment lines.
                if (inputLine.startsWith("#")) {
                    continue;
                }
                inputLine = inputLine.replace("\"", "");
                inputLine = inputLine.replace("\t", "");
                String [] split1 = inputLine.split(": ");
                String [] split2 = split1[0].split(",");
                
                double x = Double.parseDouble(split2[0]);
                double y = Double.parseDouble(split2[1]);
                Point p = new Point(x,y);
                
                // if the line was non-indented
                if(split1.length == 1){
                    campusGraph.addNode(new Node<Point>(p));
                    currLoc = p;
                }
                // if the line was indented
                else if(split1.length == 2){
                    
                    if(currLoc == null){
                        throw new MalformedDataException("bad format");
                    }
                    
                    campusGraph.addNode(new Node<Point>(p));
                    double distance = Double.parseDouble(split1[1]);
                    campusGraph.addEdge(new Node<Point>(currLoc), new Node<Point>(p), distance);
                }
                else{
                    throw new MalformedDataException("bad format");
                }
            }
        } catch (IOException e) {
            System.err.println(e.toString());
            e.printStackTrace(System.err);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    System.err.println(e.toString());
                    e.printStackTrace(System.err);
                }
            }
        }
    }

}
