package hw5;

/**
 * Node represents a single, immutable node of data in the form of a String. 
 * 
 * @specfield name: The name of the node
 * @param <T> The type of the Node. Should implement Comparable. 
 * @author Rohan Guliani
 * @version 0.1
 */
public final class Node<T extends Comparable<T>> implements Comparable<Node<T>> {
    
    /* ***** REP INVARIANT/ABSTRACTION FUNCTION ***** */
    /*
     * Rep Invariant: 
     *      name is not null
     * 
     * Abstraction Function: AF(this) where this is an instantiation of node =
     *      -Node(data) where data is the data for this node.
     */
    
    private final T data;
    
    /**
     * Constructor that takes a String name
     * @effects Creates a new Node
     * @requires name != null
     * @param name: The desired name
     */
    public Node(T data){
        Graph.checkNull(data);
        this.data = data;
        checkRep();
    }
    
    /**
     * Returns the name of the node
     * @return The name of the node
     */
    public T getData(){
        return data;
    }
    
    
    @Override
    /**
     * Returns true if the given object is 
     * equal to this node. 
     */
    public boolean equals(Object obj){
        if(!(obj instanceof Node<?>)){
            return false;
        }
        Node<?> n = (Node<?>)obj;
        return n.data.equals(data);
    }
    
    @Override
    /**
     * Generates a unique hashcode for this
     * particular instance. Two equal
     * objects are guaranteed to 
     * have the same hashcode. 
     */
    public int hashCode(){
        return data.hashCode();
    }
    
    
    /**
     * Standard compareTo function. 
     * Returns a negative number if the
     * passed Node's data is 'less' than
     * this' node's data, 0 if they're 
     * the same, and a positive number if 
     * this' node's name is 'bigger'
     * the other's node's data. 
     */
    public int compareTo(Node<T> child){
        return data.compareTo(child.data);
    }
    
    @Override
    /**
     * Returns a String representation of the Node. 
     */
    public String toString(){
        return data.toString();
    }
    
    /**
     * Private checkRep() method to verify rep invariant
     */
    private void checkRep(){
        if(data==null){
            throw new RuntimeException("name cannot be null");
        }
    }

}
