/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package info.iut.sae2.algorithms.tests;

import info.iut.sae2.graphs.Graph;
import info.iut.sae2.graphs.INode;
import info.iut.sae2.graphs.Color;
import info.iut.sae2.algorithm.SixColorationPlanarAlgorithm;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * @author sfoussatferr and astyzinski
 *
 * Test for the 6-Coloration Planar graph algorithm
 */
public class SixColorationPlanarAlgorithmTest {

    @Test
    public void testManualReportTraceSixColoration() {
        Graph graph = new Graph();

        // Graph W5
        INode a = graph.addNode(); 
        INode b = graph.addNode(); 
        INode c = graph.addNode(); 
        INode d = graph.addNode(); 
        INode e = graph.addNode(); 
        INode f = graph.addNode(); 

        graph.addEdge(b, c);
        graph.addEdge(c, d);
        graph.addEdge(d, e);
        graph.addEdge(e, f);
        graph.addEdge(f, b);

        graph.addEdge(a, b);
        graph.addEdge(a, c);
        graph.addEdge(a, d);
        graph.addEdge(a, e);
        graph.addEdge(a, f);

        // Get colors
        Color colorA = new SixColorationPlanarAlgorithm().apply(graph, null).getNodeValue(a);
        Color colorB = new SixColorationPlanarAlgorithm().apply(graph, null).getNodeValue(b);
        Color colorC = new SixColorationPlanarAlgorithm().apply(graph, null).getNodeValue(c);
        Color colorD = new SixColorationPlanarAlgorithm().apply(graph, null).getNodeValue(d);
        Color colorE = new SixColorationPlanarAlgorithm().apply(graph, null).getNodeValue(e);
        Color colorF = new SixColorationPlanarAlgorithm().apply(graph, null).getNodeValue(f);

        // Nodes F and D (Color 1)
        assertEquals("F and D must share the same Red channel value", colorF.getR(), colorD.getR());
        assertEquals("F and D must share the same Green channel value", colorF.getG(), colorD.getG());
        assertEquals("F and D must share the same Blue channel value", colorF.getB(), colorD.getB());

        // Nodes E and C (Color 2)
        assertEquals("E and C must share the same Red channel value", colorE.getR(), colorC.getR());
        assertEquals("E and C must share the same Green channel value", colorE.getG(), colorC.getG());
        assertEquals("E and C must share the same Blue channel value", colorE.getB(), colorC.getB());

        // node A (Color 0) and Node B (Color 3)
        assertFalse("B must have a distinct color from A", colorB.getR() == colorA.getR() && colorB.getG() == colorA.getG() && colorB.getB() == colorA.getB());
        assertFalse("B must have a distinct color from F", colorB.getR() == colorF.getR() && colorB.getG() == colorF.getG() && colorB.getB() == colorF.getB());
        assertFalse("B must have a distinct color from C", colorB.getR() == colorC.getR() && colorB.getG() == colorC.getG() && colorB.getB() == colorC.getB());
    }
}
