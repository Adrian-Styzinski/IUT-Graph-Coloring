/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package info.iut.sae2.algorithms.tests;

import info.iut.sae2.graphs.Graph;
import info.iut.sae2.graphs.INode;
import info.iut.sae2.graphs.Color;
import info.iut.sae2.algorithm.WelshAndPowell;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * @author sfoussatferr and astyzinski
 *
 * Test for the Welsh & Powell greedy coloring algorithm
 */
public class WelshAndPowellTest {

    @Test
    public void testManualReportTraceWelshPowell() {
        Graph graph = new Graph();

        // Initialize graph
        INode a = graph.addNode();
        INode b = graph.addNode();
        INode c = graph.addNode();
        INode d = graph.addNode();
        INode e = graph.addNode();

        graph.addEdge(a, b);
        graph.addEdge(a, d);
        graph.addEdge(a, e);
        graph.addEdge(b, c);
        graph.addEdge(c, d);
        graph.addEdge(d, e);

        // Get colors
        Color colorA = new WelshAndPowell().apply(graph, null).getNodeValue(a);
        Color colorB = new WelshAndPowell().apply(graph, null).getNodeValue(b);
        Color colorC = new WelshAndPowell().apply(graph, null).getNodeValue(c);
        Color colorD = new WelshAndPowell().apply(graph, null).getNodeValue(d);
        Color colorE = new WelshAndPowell().apply(graph, null).getNodeValue(e);

        // 1: Nodes A and C (Color 0)
        assertEquals("A and C must share the same Red channel value", colorA.getR(), colorC.getR());
        assertEquals("A and C must share the same Green channel value", colorA.getG(), colorC.getG());
        assertEquals("A and C must share the same Blue channel value", colorA.getB(), colorC.getB());

        // 2: Nodes D and B (Color 1)
        assertEquals("D and B must share the same Red channel value", colorD.getR(), colorB.getR());
        assertEquals("D and B must share the same Green channel value", colorD.getG(), colorB.getG());
        assertEquals("D and B must share the same Blue channel value", colorD.getB(), colorB.getB());

        // 3: Node E (Color 2)
        assertFalse("E must have a different color than A", 
            colorE.getR() == colorA.getR() && colorE.getG() == colorA.getG() && colorE.getB() == colorA.getB());
        assertFalse("E must have a different color than D", 
            colorE.getR() == colorD.getR() && colorE.getG() == colorD.getG() && colorE.getB() == colorD.getB());
    }
}
