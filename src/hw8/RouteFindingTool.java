package hw8;
import hw5.*;
import java.util.*;

/**
 * RouteFindingTool represents the view and controller. 
 * It allows the user to interact with the console. 
 * @author Rohan Guliani
 *
 */
public class RouteFindingTool {
    
    /**
     * Get all building names
     * 
     * @requires model to be non-null
     * @param model: the model of the CampusRouteFindingTool
     */
    public static void getAllBuildings(RouteFindingModel model) {
        Graph.checkNull(model);
        String buildingsNames = "Buildings:" + "\n";
        Map<String,String> allNames = model.getAllBuildings();
        Set<String> abbrNames = model.getAbbreviatedBuildings();
        TreeSet<String> buildingsKeys = new TreeSet<String>(abbrNames);
        for (String abbrevName : buildingsKeys) {
            String fullName = allNames.get(abbrevName);
            buildingsNames += "\t" + abbrevName + ": " + fullName + "\n";
        }
        
        System.out.println(buildingsNames);
    }
    
    /**
     * Get the shortest path from one building to another.
     * 
     * @param model: the model of the CampusRouteFindingTool
     * @param start: the abbreviated name of the start building
     * @param end: the abbreviated name of the end building
     * @requires model, start, end to be non-null
     */
	public static void getShortestWalkingRoute(RouteFindingModel model, String start, String end) {
		Graph.checkNull(model);
		Graph.checkNull(start);
		Graph.checkNull(end);

		if (!model.getAbbreviatedBuildings().contains(start) && 
			!model.getAbbreviatedBuildings().contains(end)) {

			System.out.println("Unknown building: " + start);
			System.out.println("Unknown building: " + end + "\n");

		}

		else if (!model.getAbbreviatedBuildings().contains(start)) {
			System.out.println("Unknown building: " + start + "\n");
		}

		else if (!model.getAbbreviatedBuildings().contains(end)) {
			System.out.println("Unknown building: " + end + "\n");
			
		} else {

			String path = "";
			String startFullName = model.getLongName(start);
			String endFullName = model.getLongName(end);

			path += "Path from " + startFullName + " to " + endFullName + ":\n";

			List<Edge<Point, Double>> shortestPath = model.findShortestPath(start, end);

			double currX = model.getLocation(start).getX();
			double currY = model.getLocation(start).getY();
			double currDist = 0.0;

			shortestPath = shortestPath.subList(1, shortestPath.size());

			for (Edge<Point, Double> edge : shortestPath) {
				double destX = edge.getChild().getData().getX();
				double destY = edge.getChild().getData().getY();
				double distance = edge.getLabel();
				// get the angle for next direction
				double theta = Math.atan2(destY - currY, destX - currX);
				String direction = findDirection(theta);

				path += String.format("\tWalk %.0f feet %s to (%.0f, %.0f)\n", (distance - currDist), direction, destX,
						destY);
				currX = destX;
				currY = destY;
				currDist = distance;
			}
			path += String.format("Total distance: %.0f feet\n", currDist);

			System.out.println(path);
		}
    }
    
    /**
     * Determine the direction based on the angle.
     * 
     * Possible output is a String representing the direction.
     * 
     * @param theta: the angle
     * @return the direction based on theta
     */
	private static String findDirection(double theta) {
		if (Math.abs(theta) < 0.0001 
			|| Math.abs(theta - Math.PI / 8) < 0.0001
			|| Math.abs(theta - Math.PI / -8) < 0.0001 
			|| (theta > 0 && theta < Math.PI / 8)
			|| (theta > Math.PI / -8 && theta < 0)) {
			return "E";
		} else if (theta > Math.PI / 8 && theta < 3 * Math.PI / 8) {
			return "SE";
		} else if (theta > -3 * Math.PI / 8 && theta < Math.PI / -8) {
			return "NE";
		} else if (Math.abs(theta - 3 * Math.PI / 8) < 0.0001 
				|| Math.abs(theta - 5 * Math.PI / 8) < 0.0001
				|| (theta > 3 * Math.PI / 8 && theta < 5 * Math.PI / 8)) {
			return "S";
		} else if (Math.abs(theta - -3 * Math.PI / 8) < 0.0001 
				|| Math.abs(theta - -5 * Math.PI / 8) < 0.0001
				|| (theta > -5 * Math.PI / 8 && theta < -3 * Math.PI / 8)) {
			return "N";
		} else if (theta > 5 * Math.PI / 8 && theta < 7 * Math.PI / 8) {
			return "SW";
		} else if (theta > -7 * Math.PI / 8 && theta < -5 * Math.PI / 8) {
			return "NW";
		} else {
			return "W";
		}
	}
    
    /**
     * Main method allows user to find shortest walking route 
     * between two buildings and get all the buildings' names 
     * on campus.
     * 
     * @param args
     */
    public static void main(String[] args) {
        try {
            RouteFindingModel model = new RouteFindingModel
                                      ("src/hw8/data/campus_buildings.dat", 
                                       "src/hw8/data/campus_paths.dat");
            
            String menu = ("Menu:\n" + 
                           "\t" + "r to find a route\n" +
                           "\t" + "b to see a list of all buildings\n" +
                           "\t" + "q to quit\n");
            
            System.out.println(menu);
            Scanner scan = new Scanner(System.in);
            System.out.print("Enter an option ('m' to see the menu): ");
            
            while (true) {
                String input = scan.nextLine();
                
                if (input.length() == 0 || input.startsWith("#")) {
                    System.out.println(input);
                    continue;
                }
                
                if (input.equals("m")) {
                    System.out.println(menu);
                } else if (input.equals("b")) {
                    getAllBuildings(model);
                } else if (input.equals("r")) {
                    System.out.print("Abbreviated name of starting building: ");
                    String start = scan.nextLine();
                    System.out.print("Abbreviated name of ending building: ");
                    String end = scan.nextLine();
                    getShortestWalkingRoute(model, start, end);
                } else if (input.equals("q")) {
                    scan.close();
                    return;
                } else {
                    System.out.println("Unknown option\n");
                }
                System.out.print("Enter an option ('m' to see the menu): ");
            }
        } catch (Exception e) {
            System.err.println(e.toString());
            e.printStackTrace(System.err);
        }
    }

}
