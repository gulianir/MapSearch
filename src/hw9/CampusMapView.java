package hw9;

import java.util.*;
import java.util.List;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import hw8.*;
import hw8.Point;
import hw5.*;

public class CampusMapView extends JComponent{
    
    private static final long serialVersionUID = 1L;
    
    private int actualWidth;
    private int actualHeight;
    private double xFactor;
    private double yFactor;
    private RouteFindingModel model;
    private List<Edge<Point,Double>> route;  
    private BufferedImage img;
    
    /**
     * Creates a GUI view of the campus map.
     * 
     * @param m the model of route finding tool
     * @requires m != null
     */
    public CampusMapView(RouteFindingModel m) {
        Graph.checkNull(m);        
        model = m;
        
        actualWidth = 1024;
        actualHeight = 660;
        
        this.setPreferredSize(new Dimension(actualWidth, actualHeight));
        try {
            img = ImageIO.read(new File("src/hw8/data/campus_map.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        // for resizing
        xFactor = (double)actualWidth / img.getWidth();
        yFactor = (double)actualHeight / img.getHeight();
    }

    /**
     * Gets the shortest walking route from one building to another building on campus.
     * 
     * @param start starting building's full name of the walking route
     * @param end ending building's full name of the walking route
     * @return total distance of the shortest route
     */
    public void getShortestRoute(String start, String end) {
        String start_abbr = this.model.getAbbreviatedName(start);
        String end_abbr = this.model.getAbbreviatedName(end);
        route = model.findShortestPath(start_abbr, end_abbr);
        repaint();
    }
    
    /**
     * Resets the view, removing a path if there is one.
     */
    public void reset() {
        route = null;
        repaint();
    }
    
    /**
     * Overrides paintComponent to draw a route if two buildings are selected. 
     * 
     * @param g the original graphics
     * @modifies the user view
     * @effect display the route between two buildings if two buildings are chosen
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        Graphics2D graphics = (Graphics2D) g;
        
        actualWidth = getWidth();
        actualHeight = getHeight();
        
        //for resizing
        xFactor = (double)actualWidth / img.getWidth();
        yFactor = (double)actualHeight / img.getHeight();
        
        graphics.drawImage(img, 0, 0, actualWidth, actualHeight, 
                           0, 0, img.getWidth(), img.getHeight(), null);
        
        if (route != null) {
            List<Point> points = new ArrayList<Point>(getPoints());
            int startX = (int) Math.round(points.get(0).getX() * xFactor);
            int startyY = (int) Math.round(points.get(0).getY() * yFactor);
            int currX = startX;
            int currY = startyY;
            graphics.setColor(Color.green);
            graphics.setStroke(new BasicStroke(4));
            for (Point point : points) {
                int nextX = (int)Math.round(point.getX()*xFactor);
                int nextY = (int)Math.round(point.getY()*yFactor);
                graphics.drawLine(currX, currY, nextX, nextY);
                currX = nextX;
                currY = nextY;
            }
            graphics.setColor(Color.blue);
            graphics.fillOval(startX-2, startyY-2, 7, 7);
            graphics.setColor(Color.red);
            graphics.fillOval(currX-2, currY-2, 7, 7);
        }
    }
    
    /**
     * Helper method to parse the list of edges for a list of destination
     * points
     * @return a list of destination points
     */
    private List<Point> getPoints(){
        List<Point> points = new ArrayList<>();
        for(Edge<Point,Double> e: route){
            points.add(e.getParent().getData());
        }
        return points;
    }

}
