/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package info.iut.sae2.algorithms.tests;

import info.iut.sae2.graphs.Graph;
import info.iut.sae2.graphs.INode;
import info.iut.sae2.graphs.Color;
import info.iut.sae2.algorithm.FiveColorationPlanarAlgorithm;
import info.iut.sae2.properties.ColorProperty;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * @author sfoussatferr and astyzinski
 *
 * Test for the recursive 5-Coloration Planar graph algorithm
 */
public class FiveColorationPlanarAlgorithmTest {

    @Test
    public void testManualReportTraceFiveColoration() {
        Graph graph = new Graph();

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

        ColorProperty colors = new FiveColorationPlanarAlgorithm().apply(graph, null);

        Color colorA = colors.getNodeValue(a);
        Color colorB = colors.getNodeValue(b);
        Color colorC = colors.getNodeValue(c);
        Color colorD = colors.getNodeValue(d);
        Color colorE = colors.getNodeValue(e);
        Color colorF = colors.getNodeValue(f);

        assertEquals("D and B must share the same Red channel value", colorD.getR(), colorB.getR());
        assertEquals("D and B must share the same Green channel value", colorD.getG(), colorB.getG());
        assertEquals("D and B must share the same Blue channel value", colorD.getB(), colorB.getB());

        assertFalse("A must be unique compared to D", colorA.getR() == colorD.getR() && colorA.getG() == colorD.getG() && colorA.getB() == colorD.getB());
        assertFalse("F must be unique compared to E", colorF.getR() == colorE.getR() && colorF.getG() == colorE.getG() && colorF.getB() == colorE.getB());
    }
}
