/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package info.iut.sae2.graphs;

/**
 *
 * @author sfousat and astyzinski
 */
public class Edge implements IEdge {

    /** 
     * Auto incremented counter used to assign a unique identifier to each new edge
     */
    private static int nextId = 0;
    /** 
     * Unique identifier of the edge, assigned at it's construction
     */
    private final int id;
    /** 
     * Source node of this edge
     */
    private final INode src;
    /**
     * Target node of this edge
     */
    private final INode tgt;
    
    /**
     * Constructor for a new Edge between a source and a target 
     *
     * @param src the source node of this edge
     * @param tgt the target node of this edge
     * @throws IllegalArgumentException if either src or tgt is null
     */
    public Edge(INode src, INode tgt) {
        if (src == null || tgt == null) {
            throw new IllegalArgumentException("Erreur");
        }
        this.src = src;
        this.tgt = tgt;
        this.id = nextId++;
    }

    /**
     * Returns the unique identifier of this edge.
     *
     * @return the unique integer that is the identifier of this edge
     */
    public int getId() { return id; }

    /**
     * {@inheritDoc}
     */
    @Override
    public INode source() { return src; }

    /**
     * {@inheritDoc}
     */
    @Override
    public INode target() { return tgt; }

    /**
     * Returns a string representation of this edge, showing its identifier as well as it's
     * source and target node.
     *
     * @return a string in the form "Edge(id: src -> tgt)"
     */
    @Override
    public String toString() {
        return "Edge(" + id + ": " + src + " -> " + tgt + ")";
    }
}