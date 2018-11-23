package hw5;

/**
 * Edge represents a single, immutable edge for a Graph ADT. 
 * 
 * @specfield parent: the node that "owns" this edge
 * @specfield child: the node that the parent is "pointing" to
 * @specfield name: The name (label) of the edge
 * @param <N>: The type of the node. Should implement Comparable. 
 * @param <E>: The type of the edge labels. Should implement Comparable. 
 * @author Rohan Guliani
 * @version 0.1
 *
 */
public final class Edge<N extends Comparable<N>, E extends Comparable<E>> implements Comparable<Edge<N,E>> {
    
    /* ***** REP INVARIANT/ABSTRACTION FUNCTION ***** */
    /*
     * Rep Invariant: 
     *      none of parent, child, and name are null.
     * 
     * Abstraction Function: AF(this) where this is an instantiation of edge =
     *      -Edge(parent, child, name) where parent is the parent node, child 
     *       is the child node, and name is the label for this edge. 
     */
    
    private final Node<N> parent;
    private final Node<N> child;
    private final E label;
    
    public static boolean TEST_FLAG = false;
    
    /**
     * Constructor that takes in a parent, child and name and creates
     * a new edge. 
     * @effects Creates a new Edge.
     * @requires parent, child, and name to all be non-null
     * @param parent: The parent node
     * @param child: The child node
     * @param label: The label of the edge. Should be of a type that 
     *               extends Object (no primitives) and be a type
     *               that implements Comparable<E> where E is the 
     *               same type of Label. 
     */
    public Edge(Node<N> parent, Node<N> child, E label){
        Graph.checkNull(parent);
        Graph.checkNull(child);
        Graph.checkNull(label);
        this.parent = parent;
        this.child = child;
        this.label = label;
        checkRep();

    }
    
    /**
     * Returns the parent of the edge
     * @return the parent of the edge
     */
    public Node<N> getParent(){
        return parent;
    }
    
    /**
     * Returns the child of the edge
     * @return the child of the edge
     */
    public Node<N> getChild(){
        return child;
    }
    
    /**
     * Returns the label of the edge
     * @return the label of the edge
     */
    public E getLabel(){
        return label;
    }
    
    @Override
    /**
     * Returns true if the given object equals this edge and false otherwise.
     * @return true if the given object equals this. 
     */
    public boolean equals(Object obj){
        if(!(obj instanceof Edge<?,?>)){
            return false;
        }
        Edge<?,?> e = (Edge<?,?>) obj;
        return (parent.equals(e.parent) && child.equals(e.child) && label.equals(e.label));
    }
    
    @Override
    /**
     * Generates a unique hashcode for this edge. Two hashcodes are guaranteed
     * to be the same if they are equal. 
     * @return a certain hashcode
     */
    public int hashCode(){
        return label.hashCode() + parent.hashCode() + 
               + child.hashCode();
    }
    
    /**
     * Compares two edges first by comparing children, then 
     * by edge label. 
     * @return A negative number if this comes before other as specified
     * in the description, 0 if they're equal, and a positive number
     * otherwise. 
     */
    public int compareTo(Edge<N,E> other) {
        int childCompare = child.compareTo(other.child);
        if (childCompare == 0) {
            return label.compareTo(other.label);
        }
        return childCompare;
    }
    
    
    @Override
    /**
     * Returns a String representation of the edge. 
     */
    public String toString(){
        return "Edge " + label + " from " + parent + " to " + child;
    }
    
    /**
     * checkRep method to ensure rep invariant is upheld
     */
    private void checkRep() {
        if (TEST_FLAG) {
            if (parent == null || child == null || label == null) {
                throw new RuntimeException("None of the fields can be null");
            }
        }
    }

}
