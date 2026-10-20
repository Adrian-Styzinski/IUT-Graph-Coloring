/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package info.iut.sae2.graphs.tests;

import info.iut.sae2.graphs.*;
import org.junit.Test;
import java.util.ArrayList;
import static org.junit.Assert.*;

/**
 * @author sfoussatferr and astyzinski
 *
 * Test for the Graph class
 */
public class GraphTest {

    private static final double DELTA = 0.0000001;
    
    // Helper method to see if two colors match perfectly
    private boolean sameColor(Color a, Color b) {
        return a.getR() == b.getR() && 
               a.getG() == b.getG() && 
               a.getB() == b.getB() && 
               a.getA() == b.getA();
    }

    // Creating and Copying Graphs

    @Test
    public void testGraphInitialization() {
        Graph g = new Graph();
        
        // Make sure a brand new graph starts empty
        assertEquals(0, g.numberOfNodes());
        assertEquals(0, g.numberOfEdges());
        
        // Make sure properties get initialized
        assertNotNull(g.getLayout());
        assertNotNull(g.getColor());
        assertNotNull(g.getSizes());
    }

    @Test
    public void testGraphDeepCopyMechanisms() {
        Graph g = new Graph();
        INode a = g.addNode();
        INode b = g.addNode();
        IEdge e = g.addEdge(a, b);
        
        Color red = new Color(255, 0, 0, 255);
        g.setNodeColor(a, red);

        // Make a deep copy of the graph
        IGraph copy = g.copy();
        assertNotNull(copy);
        assertEquals(g.numberOfNodes(), copy.numberOfNodes());
        assertEquals(g.numberOfEdges(), copy.numberOfEdges());

        // Verify the copy's items match the original colors
        for (INode cn : copy.getNodes()) {
            if (copy.getNodes().indexOf(cn) == 0) {
                assertEquals(255, copy.getNodeColor(cn).getR());
            }
        }

        // Verify the two graphs are isolated (modifying the copy doesn't change the original)
        copy.addNode();
        assertEquals(2, g.numberOfNodes());
        assertEquals(3, copy.numberOfNodes());
    }

    // Node Operations

    @Test
    public void testNodeOperations() {
        Graph g = new Graph();
        
        // Add nodes manually
        INode a = g.addNode();
        INode b = g.addNode();
        
        assertNotNull(a);
        assertNotNull(b);
        assertNotEquals(a, b); // Make sure they are two different nodes
        
        // Make sure they automatically get default positions, colors, and sizes properties
        assertNotNull(g.getNodePosition(a));
        assertNotNull(g.getNodeColor(a));
        assertNotNull(g.getNodeSize(a));
        assertEquals(0, g.getInOutEdges(a).size());
        
        // Verify node count matches the actual list size
        assertEquals(2, g.numberOfNodes());
        assertEquals(2, g.getNodes().size());
        assertTrue(g.getNodes().contains(a));
        assertTrue(g.getNodes().contains(b));

        // Make sure getNodes() gives us a copy list, not the real list
        ArrayList<INode> nodesCopy = g.getNodes();
        nodesCopy.clear();
        assertEquals(2, g.numberOfNodes());
    }

    @Test
    public void testAddNodeWithArgument() {
        Graph g = new Graph();
        
        // Passing nothing (null) should be ignored and do nothing
        assertNull(g.addNode(null));
        assertEquals(0, g.numberOfNodes());

        // Put a specific node inside the graph
        INode n = new Node();
        INode added = g.addNode(n);
        
        assertEquals(n, added);
        assertEquals(1, g.numberOfNodes());
        assertTrue(g.getNodes().contains(n));
        
        // Make sure this newly inserted node also gets default styles automatically
        assertNotNull(g.getNodePosition(n));
        assertNotNull(g.getNodeColor(n));
        assertNotNull(g.getNodeSize(n));
    }

    @Test
    public void testNodeRemovalAndDeletions() {
        Graph g = new Graph();
        INode a = g.addNode();
        INode b = g.addNode();
        INode c = g.addNode();
        
        g.addEdge(a, b);
        g.addEdge(c, a);

        // Trying to delete a null or an outside node should be ignored
        g.delNode(null);
        g.delNode(new Node());
        assertEquals(3, g.numberOfNodes());

        // Remove node "a" and check if the graph automatically cleans up its connected lines
        g.delNode(a);
        assertEquals(2, g.numberOfNodes());
        assertEquals(0, g.numberOfEdges()); // Both lines connected to "a" should be automatically deleted
        assertFalse(g.getNodes().contains(a));
    }

    // Edge Operations

    @Test
    public void testEdgeOperations() {
        Graph g = new Graph();
        INode a = g.addNode();
        INode b = g.addNode();
        
        IEdge edge = g.addEdge(a, b);
        assertNotNull(edge);
        assertEquals(a, g.source(edge));
        assertEquals(b, g.target(edge));

        // Make sure lines automatically get default colors and widths
        assertNotNull(g.getEdgeColor(edge));
        assertNotNull(g.getEdgeWidth(edge));

        // Verify the shortcut edge counters match the actual list size
        assertEquals(1, g.numberOfEdges());
        assertEquals(1, g.getEdges().size());
        assertTrue(g.getEdges().contains(edge));

        // Test adding duplicate lines and self loops
        assertNotNull(g.addEdge(a, b)); 
        assertNotNull(g.addEdge(a, a)); 
        assertEquals(3, g.numberOfEdges());

        // Make sure getEdges() gives us a copy list, not the real list
        ArrayList<IEdge> edgesCopy = g.getEdges();
        edgesCopy.clear();
        assertEquals(3, g.numberOfEdges());
    }

    @Test
    public void testAddEdgeWithArgument() {
        Graph g = new Graph();
        INode a = g.addNode();
        INode b = g.addNode();
        
        // Passing nothing (null) should do nothing
        assertNull(g.addEdge((IEdge) null));
        assertEquals(0, g.numberOfEdges());

        // Create a specific line object and put it directly in the graph
        IEdge e = new Edge(a, b);
        IEdge added = g.addEdge(e);
        
        assertEquals(e, added);
        assertEquals(1, g.numberOfEdges());
    }

    @Test
    public void testEdgeRemoval() {
        Graph g = new Graph();
        INode a = g.addNode();
        INode b = g.addNode();
        IEdge e1 = g.addEdge(a, b);

        // Deleting non-existent or null lines should be ignored
        g.delEdge(null);
        g.delEdge(new Edge(a, b));
        assertEquals(1, g.numberOfEdges());

        // Delete the real line and check that the nodes no longer track it
        g.delEdge(e1);
        assertEquals(0, g.numberOfEdges());
        assertEquals(0, g.getOutEdges(a).size());
        assertEquals(0, g.getInEdges(b).size());
        assertFalse(g.existEdge(a, b, true));
    }

    // Checking Neighbors and Connections

    @Test
    public void testNodeAdjacencyAndDegree() {
        Graph g = new Graph();
        INode a = g.addNode();
        INode b = g.addNode();
        INode c = g.addNode();
        
        // Add edges
        g.addEdge(a, b);
        g.addEdge(c, a);
        g.addEdge(a, c);

        // Check incoming vs outgoing edges
        assertEquals(1, g.inDegree(a));
        assertEquals(2, g.outDegree(a));
        assertEquals(2, g.degree(a));

        // Check directions (who follows who)
        assertTrue(g.getSuccesors(a).contains(b));
        assertEquals(0, g.getSuccesors(b).size());
        assertTrue(g.getPredecessors(b).contains(a));
        assertEquals(1, g.getPredecessors(a).size());

        // Check neighbor list
        ArrayList<INode> neighborsA = g.getNeighbors(a);
        assertEquals(2, neighborsA.size());
        assertTrue(neighborsA.contains(b));
        assertTrue(neighborsA.contains(c));

        // Test a self loop
        g.addEdge(b, b);
        assertEquals(2, g.getInOutEdges(b).size());
    }

    @Test
    public void testEdgeExistence() {
        Graph g = new Graph();
        INode a = g.addNode();
        INode b = g.addNode();
        IEdge e = g.addEdge(a, b);

        // Check oriented connection 
        assertTrue(g.existEdge(a, b, true));
        assertFalse(g.existEdge(b, a, true));
        assertEquals(e, g.getEdge(a, b, true));
        assertNull(g.getEdge(b, a, true));

        // Check non-oriented connection 
        assertTrue(g.existEdge(b, a, false));
        assertEquals(e, g.getEdge(b, a, false));
        
        // Search for a edge that doesn't exist
        assertFalse(g.existEdge(a, a, true));
    }

    // Positions, Sizes, Colors, and Screen Boundaries

    @Test
    public void testNodeAndEdgePositions() {
        Graph g = new Graph();
        INode a = g.addNode();
        INode b = g.addNode();
        IEdge e = g.addEdge(a, b);

        // Set position for one node
        Coord pos = new Coord(15.0, 25.0);
        g.setNodePosition(a, pos);
        assertEquals(15.0, g.getNodePosition(a).getX(), DELTA);
        assertEquals(25.0, g.getNodePosition(a).getY(), DELTA);

        // Change positions for all nodes at once
        Coord allPos = new Coord(5.0, 5.0);
        g.setAllNodesPositions(allPos);
        assertEquals(5.0, g.getNodePosition(a).getX(), DELTA);
        assertEquals(5.0, g.getNodePosition(b).getX(), DELTA);

        // Set position bends for one Edge
        ArrayList<Coord> bends = new ArrayList<>();
        bends.add(new Coord(1, 2));
        g.setEdgePosition(e, bends);
        assertEquals(1, g.getEdgePosition(e).size());

        // Change position bends for all Edge at once
        ArrayList<Coord> allBends = new ArrayList<>();
        allBends.add(new Coord(3, 3));
        g.setAllEdgesPositions(allBends);
        assertEquals(1, g.getEdgePosition(e).size());
    }

    @Test
    public void testNodeAndEdgeSize() {
        Graph g = new Graph();
        INode a = g.addNode();
        INode b = g.addNode();
        IEdge e = g.addEdge(a, b);

        // Set size for one node
        Size s = new Size(42, 42);
        g.setNodeSize(a, s);
        assertEquals(42, g.getNodeSize(a).getW(), DELTA);

        // Change sizes for all nodes at once
        Size allS = new Size(10, 10);
        g.setAllNodesSizes(allS);
        assertEquals(10, g.getNodeSize(a).getW(), DELTA);
        assertEquals(10, g.getNodeSize(b).getW(), DELTA);

        // Set width for one edge
        g.setEdgeWidth(e, 5.0);
        assertEquals(5.0, g.getEdgeWidth(e), DELTA);

        // Change widths for all edge at once
        g.setAllEdgesWidths(3.0);
        assertEquals(3.0, g.getEdgeWidth(e), DELTA);
    }

    @Test
    public void testColorAssignmentsAndOverrides() {
        Graph g = new Graph();
        INode a = g.addNode();
        INode b = g.addNode();
        IEdge e = g.addEdge(a, b);
        
        Color red = new Color(255, 0, 0, 255);
        Color green = new Color(0, 255, 0, 255);

        // Set colors for individual elements
        g.setNodeColor(a, red);
        assertEquals(255, g.getNodeColor(a).getR());
        assertEquals(0, g.getNodeColor(a).getG());

        g.setEdgeColor(e, green);
        assertEquals(255, g.getEdgeColor(e).getG());

        // Change colors for all nodes and all lines
        Color col = new Color(10, 20, 30, 255);
        g.setAllNodesColors(col);
        g.setAllEdgesColors(col);

        for (INode n : g.getNodes()) {
            assertTrue(sameColor(col, g.getNodeColor(n)));
        }
        for (IEdge edge : g.getEdges()) {
            assertEquals(10, g.getEdgeColor(edge).getR());
        }
    }

    @Test
    public void testGraphBoundingBoxes() {
        Graph g = new Graph();
        
        // Empty graphs should use fallback to the screen origin (0,0)
        ArrayList<Coord> emptyBb = g.getBoundingBox();
        assertEquals(2, emptyBb.size());
        assertEquals(0, emptyBb.get(0).getX(), DELTA);
        assertEquals(0, emptyBb.get(1).getX(), DELTA);

        // Placing nodes far apart should automatically stretch the screen border box boundaries
        INode a = g.addNode();
        INode b = g.addNode();
        g.setNodePosition(a, new Coord(0, 0));
        g.setNodePosition(b, new Coord(100, 100));
        
        ArrayList<Coord> bb = g.getBoundingBox();
        assertTrue(bb.get(0).getX() <= 0);
        assertTrue(bb.get(1).getX() >= 100);
    }

    // Graph Coloring Algorithms (quick tests)

    @Test
    public void welshAndPowell_emptyGraph() {
        Color.populate(); // Load color first
        Graph g = new Graph();
        g.welshAndPowell();
    }

    @Test
    public void sixColors_emptyGraph() {
        Color.populate(); // Load color first
        Graph g = new Graph();
        g.sixColors();
    }

    @Test
    public void fiveColors_singleNode() {
        Color.populate(); // Load color first
        Graph g = new Graph();
        g.addNode();
        g.fiveColors();
    }
}