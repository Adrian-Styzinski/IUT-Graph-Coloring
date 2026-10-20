/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package info.iut.sae2.graphs.tests;

import info.iut.sae2.graphs.Edge;
import info.iut.sae2.graphs.Node;
import info.iut.sae2.graphs.INode;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * @author sfoussatferr and astyzinski
 *
 * Test for the Edge class
 */
public class EdgeTest {
    
    // Constructor & Nodes

    @Test
    public void edge_constructor_createsEdgeWithNodes() {
        INode a = new Node();
        INode b = new Node();
        Edge e = new Edge(a, b);
        
        assertNotNull(e);
        assertEquals(a, e.source());
        assertEquals(b, e.target());
        assertNotEquals(e.source(), e.target());
    }

    @Test(expected = IllegalArgumentException.class)
    public void edge_constructor_nullSrcThrows() {
        new Edge(null, new Node());
    }

    @Test(expected = IllegalArgumentException.class)
    public void edge_constructor_nullTgtThrows() {
        new Edge(new Node(), null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void edge_constructor_bothNullThrows() {
        new Edge(null, null);
    }

    @Test
    public void edge_constructor_selfLoopIsValid() {
        INode a = new Node();
        Edge e = new Edge(a, a);
        
        assertNotNull(e);
        assertEquals(e.source(), e.target());
    }

    // getId

    @Test
    public void edge_getId_incrementsSequentiallyAndIsStable() {
        INode a = new Node();
        INode b = new Node();
        Edge e1 = new Edge(a, b);
        Edge e2 = new Edge(a, b);

        // Verify "stability" (multiple calls return same ID)
        assertEquals(e1.getId(), e1.getId());

        // Verify auto-increment sequencing
        assertNotEquals(e1.getId(), e2.getId());
        assertTrue(e2.getId() > e1.getId());
    }

    @Test
    public void edge_getId_manyEdgesAllHaveUniqueIds() {
        INode a = new Node();
        INode b = new Node();
        int count = 50;
        int[] ids = new int[count];
        for (int i = 0; i < count; i++) {
            ids[i] = new Edge(a, b).getId();
        }
        for (int i = 0; i < count; i++) {
            for (int j = i + 1; j < count; j++) {
                assertNotEquals(ids[i], ids[j]);
            }
        }
    }

    // toString

    @Test
    public void edge_toString_formatsCorrectly() {
        INode a = new Node();
        INode b = new Node();
        Edge e1 = new Edge(a, b);
        Edge e2 = new Edge(a, b);

        // Verifies the pattern (keywords, IDs, arrows)
        assertEquals("Edge(" + e1.getId() + ": " + a + " -> " + b + ")", e1.toString());
        
        // Verifies distinct instances have distinct string outputs (unique IDs)
        assertNotEquals(e1.toString(), e2.toString());
    }
}