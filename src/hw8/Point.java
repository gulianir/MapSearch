package hw8;

import hw5.*;

/**
 * This class represents an immutable (x,y) coordinate pair
 * in two dimensional space. 
 * @specfield x-coordinate (double)
 * @specfield y-coordinate (double)
 * @author Rohan Guliani
 *
 */
public class Point implements Comparable<Point>{
    
    /*
     * Rep Invariant: 
     *  x != null and
     *  y != null.
     *  
     *  Abstraction Function: 
     *  AF(this) = 
     *      p.x-coordinate = this.getX()
     *      p.y-coordinate = this.getY()
     */
      
    private final Double x;
    private final Double y;
    
    /**
     * Construct a new point with the given coordinates
     * @param x: the x coordinate
     * @param y: the y coordinate
     */
    public Point(double x, double y){
        this.x = x;
        this.y = y;
        checkRep();
    }
    
    /**
     * Return the x coordinate
     * @return The x coordinate
     */
    public double getX(){
        return x;
    }
    
    /**
     * Return the y coordinate
     * @return The y coordinate
     */
    public double getY(){
        return y;
    }
    
    @Override
    public boolean equals(Object obj){
        Graph.checkNull(obj);
        
        if(!(obj instanceof Point)){
            return false;
        }
        Point p = (Point) obj;
        return (p.x.equals(x) && p.y.equals(y));
    }
    
    @Override
    public int hashCode() {
        return x.hashCode() + y.hashCode();
    }
    
    /**
     * Compares two points. First compares by 
     * x-coordinate, then by y-coordinate
     * (so a point with an x less than 
     * another point will always be 
     * "less" than another point. 
     */
    public int compareTo(Point other) {
        if (y.equals(other.y)) {
            return y.compareTo(other.y);
        } else {
            return x.compareTo(other.x);
        }
    }
    
    /**
     * Check if the rep invariant is true, throw RunTime exception otherwise
     */
    private void checkRep(){
        if(x == null || y == null){
            throw new RuntimeException("None of the coordinates can be null");
        }
    }

}
