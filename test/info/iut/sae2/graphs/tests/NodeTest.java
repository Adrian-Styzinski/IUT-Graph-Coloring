/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package info.iut.sae2.graphs.tests;

import info.iut.sae2.graphs.Node;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * @author sfoussatferr and astyzinski
 *
 * Test for the Node class
 */
public class NodeTest {

    // Constructor

    @Test
    public void testNodeConstructor() {
        Node a = new Node();
        assertNotNull(a);

        Node b = new Node();
        assertNotSame(a, b);
    }

    // IDs

    @Test
    public void testNodeId() {
        Node a = new Node();
        Node b = new Node();

        assertEquals(a.getId(), a.getId());

        assertNotEquals(a.getId(), b.getId());

        assertTrue(b.getId() > a.getId());
    }

    @Test
    public void node_getId_manyEdgesAllHaveUniqueIds() {
        int count = 100;
        int[] ids = new int[count];

        for (int i = 0; i < count; i++) {
            ids[i] = new Node().getId();
        }

        for (int i = 0; i < count; i++) {
            for (int j = i + 1; j < count; j++) {
                assertNotEquals(ids[i], ids[j]);
            }
        }
    }

    // toString

    @Test
    public void testNodeTextFormatting() {
        Node a = new Node();
        Node b = new Node();

        String expectedFormat = "Node(" + a.getId() + ")";
        assertEquals(expectedFormat, a.toString());

        assertNotEquals(a.toString(), b.toString());
    }
}