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
 */
public class GraphLoaderTest {
    
    private static final double DELTA = 0.00000001;
    private static final String ROOT = "dataset/";

    // Dataset paths
    // File names are approximations not exact node counts.
    // Actual counts are verified by counting lines in each csv file
    private static final String SIMPLE_N = ROOT + "simple_nodes.csv";
    private static final String SIMPLE_E = ROOT + "simple_edges.csv";

    private static final String CJ_N = ROOT + "testCJ_nodes.csv";
    private static final String CJ_E = ROOT + "testCJ_edges.csv";

    private static final String EB_N = ROOT + "testEB_nodes.csv";
    private static final String EB_E = ROOT + "testEB_edges.csv";

    private static final String EBB_N = ROOT + "testEBB_nodes.csv";
    private static final String EBB_E = ROOT + "testEBB_edges.csv";

    private static final String GRID_10_N = ROOT + "planar_grid_10_nodes.csv";
    private static final String GRID_10_E = ROOT + "planar_grid_10_edges.csv";

    private static final String GRID_100_N = ROOT + "planar_grid_100_nodes.csv";
    private static final String GRID_100_E = ROOT + "planar_grid_100_edges.csv";

    private static final String P100_N = ROOT + "planar_100_nodes.csv";
    private static final String P100_E = ROOT + "planar_100_edges.csv";

    private static final String P500_N = ROOT + "planar_500_nodes.csv";
    private static final String P500_E = ROOT + "planar_500_edges.csv";

    private static final String P1000_N = ROOT + "planar_1000_nodes.csv";
    private static final String P1000_E = ROOT + "planar_1000_edges.csv";

    private static final String P2000_N = ROOT + "planar_2000_nodes.csv";
    private static final String P2000_E = ROOT + "planar_2000_edges.csv";

    // Null & Non-Existent Error Handling

    @Test
    public void testInvalidAndMissingInputs() {
        // Null inputs
        assertNull(GraphLoader.loadFromFile(null, SIMPLE_E));
        
        IGraph nullEdge = GraphLoader.loadFromFile(SIMPLE_N, null);
        assertNotNull(nullEdge);
        assertEquals(12, nullEdge.numberOfNodes());
        assertEquals(0,  nullEdge.numberOfEdges());

        // Non-existent files
        IGraph missingNode = GraphLoader.loadFromFile("qyugefyqgyfugqufigyv.csv", SIMPLE_E);
        assertNotNull(missingNode);
        assertEquals(0, missingNode.numberOfNodes());

        IGraph missingEdge = GraphLoader.loadFromFile(SIMPLE_N, "isyubfnbui.csv");
        assertNotNull(missingEdge);
        assertEquals(12, missingEdge.numberOfNodes());
        assertEquals(0,  missingEdge.numberOfEdges());
        
        IGraph bothMissing = GraphLoader.loadFromFile("eqfuiqufuig.csv", "qfoiqbuzuibf.csv");
        assertNotNull(bothMissing);
        assertEquals(0, bothMissing.numberOfNodes());
    }

    // Small/Test Dataset Loading

    @Test
    public void testSimpleGraph_comprehensive() {
        IGraph g = GraphLoader.loadFromFile(SIMPLE_N, SIMPLE_E);
        assertNotNull(g);
        assertEquals(12, g.numberOfNodes());
        assertEquals(16, g.numberOfEdges());

        // Validate node coordinate exist
        for (INode n : g.getNodes()) {
            assertNotNull(g.getNodePosition(n));
        }

        // First line coordinate check: "0;13.0 7.5"
        Coord firstPos = g.getNodePosition(g.getNodes().get(0));
        assertEquals(13.0, firstPos.getX(), DELTA);
        assertEquals(7.5,  firstPos.getY(), DELTA);

        // Last line coordinate check: "11;4.5 5.0"
        Coord lastPos = g.getNodePosition(g.getNodes().get(11));
        assertEquals(4.5, lastPos.getX(), 1e-9);
        assertEquals(5.0, lastPos.getY(), 1e-9);
    }

    @Test
    public void testValidationGraphs() {
        // testCJ
        IGraph cj = GraphLoader.loadFromFile(CJ_N, CJ_E);
        assertEquals(4, cj.numberOfNodes());
        assertEquals(4, cj.numberOfEdges());
        assertEquals(2, cj.getOutEdges(cj.getNodes().get(0)).size());
        
        for (INode n : cj.getNodes()) {
            assertNotNull(cj.getNodePosition(n));
        }
        Coord cjPos = cj.getNodePosition(cj.getNodes().get(0));
        assertEquals(1.0, cjPos.getX(), DELTA);
        assertEquals(1.0, cjPos.getY(), DELTA);

        // testEB
        IGraph eb = GraphLoader.loadFromFile(EB_N, EB_E);
        assertEquals(5, eb.numberOfNodes());
        assertEquals(5, eb.numberOfEdges());
        assertEquals(3, eb.getOutEdges(eb.getNodes().get(0)).size());
        
        for (INode n : eb.getNodes()) {
            assertNotNull(eb.getNodePosition(n));
        }

        // testEBB
        IGraph ebb = GraphLoader.loadFromFile(EBB_N, EBB_E);
        assertEquals(5, ebb.numberOfNodes());
        assertEquals(6, ebb.numberOfEdges());
        
        for (INode n : ebb.getNodes()) {
            assertNotNull(ebb.getNodePosition(n));
        }
    }

    // Large & Planar Dataset

    @Test
    public void testLargeAndPlanarDatasets() {
        // Planar Grid 10
        IGraph grid10 = GraphLoader.loadFromFile(GRID_10_N, GRID_10_E);
        assertNotNull(grid10);
        assertEquals(100, grid10.numberOfNodes());
        assertTrue(grid10.numberOfEdges() > 0);
        
        for (INode n : grid10.getNodes()) {
            assertNotNull(grid10.getNodePosition(n));
        }
        
        // Planar Grid 100
        IGraph grid100 = GraphLoader.loadFromFile(GRID_100_N, GRID_100_E);
        assertNotNull(grid100);
        assertEquals(10000, grid100.numberOfNodes());
        assertTrue(grid100.numberOfEdges() > 0);

        // Planar 100 (123 lines)
        IGraph p100 = GraphLoader.loadFromFile(P100_N, P100_E);
        assertNotNull(p100);
        assertEquals(123, p100.numberOfNodes());
        assertTrue(p100.numberOfEdges() > 0);

        // Planar 500 (543 lines)
        IGraph p500 = GraphLoader.loadFromFile(P500_N, P500_E);
        assertNotNull(p500);
        assertEquals(543, p500.numberOfNodes());
        assertTrue(p500.numberOfEdges() > 0);

        // Planar 1000 (1023 lines)
        IGraph p1000 = GraphLoader.loadFromFile(P1000_N, P1000_E);
        assertNotNull(p1000);
        assertEquals(1023, p1000.numberOfNodes());
        assertTrue(p1000.numberOfEdges() > 0);

        // Planar 2000 (2043 lines)
        IGraph p2000 = GraphLoader.loadFromFile(P2000_N, P2000_E);
        assertNotNull(p2000);
        assertEquals(2043, p2000.numberOfNodes());
        assertTrue(p2000.numberOfEdges() > 0);
    }

    // Graph Structure and more

    @Test
    public void testGraphStructureAndIsolation() {
        IGraph g1 = GraphLoader.loadFromFile(SIMPLE_N, SIMPLE_E);
        IGraph g2 = GraphLoader.loadFromFile(SIMPLE_N, SIMPLE_E);

        // Make sure the count methods match the actual size of the lists
        assertEquals(g1.numberOfNodes(), g1.getNodes().size());
        assertEquals(g1.numberOfEdges(), g1.getEdges().size());

        // All edges are connected to valid existing nodes
        ArrayList<INode> nodes = g1.getNodes();
        for (IEdge e : g1.getEdges()) {
            assertTrue(nodes.contains(g1.source(e)));
            assertTrue(nodes.contains(g1.target(e)));
        }

        // Separate loadings returns different instances (or graphs) and are isolated (modifying g1 does not impact g2)
        assertNotEquals(g1, g2);
        g1.addNode();
        assertEquals(12, g2.numberOfNodes()); // g2 remains unaffected
    }
}
