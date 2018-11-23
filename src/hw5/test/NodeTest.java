package hw5.test;

import static org.junit.Assert.*;

import org.junit.*;
import hw5.*;

public class NodeTest {
    
    private static final Node<String> Node1 = new Node<>("Node1");
    private static final Node<String> Node2 = new Node<>("Node2");
    
    
    @Test
    public void testToString(){
        assertEquals(Node1.toString(), "Node1");
    }
    
    @Test
    public void testEquality(){
        assertNotEquals(Node1, Node2);
        assertEquals(Node1, Node1);
    }
    
    @Test
    public void testSort(){
        assertTrue(Node1.compareTo(Node2) < 0);
    }   
    
    @Test(expected=IllegalArgumentException.class)
    public void illegalArgument(){
        new Node<>(null);
    }
    
}
