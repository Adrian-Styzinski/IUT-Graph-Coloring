/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package info.iut.sae2.graphs;

/**
 *
 * @author sfousat and astyzinski
 */
public class Node implements INode {

    /** 
     * Auto incremented counter used to assign a unique identifier to each new node
     */
    private static int nextId = 0;
    
    /**
     * Unique identifier of the node, assigned at it's construction
     */
    private final int id;

    /**
     * Default constructor. Creates a new Node with a unique auto-incremented identifier.
     */
    public Node() {
        this.id = nextId++;
    }

    /**
     * Returns the unique identifier of this node
     *
     * @return the unique that is the integer identifier of this node
     */
    public int getId() {
        return id;
    }

    /**
     * Returns a string representation of this node
     *
     * @return a string in the form "Node(id)"
     */
    @Override
    public String toString() {
        return "Node(" + id + ")";
    }
}
