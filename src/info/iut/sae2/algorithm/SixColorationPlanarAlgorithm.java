/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package info.iut.sae2.algorithm;

import info.iut.sae2.graphs.Color;
import info.iut.sae2.graphs.IGraph;
import info.iut.sae2.graphs.INode;
import info.iut.sae2.properties.ColorProperty;

import java.util.ArrayList;
import java.util.Map;
import java.util.Stack;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;

public class SixColorationPlanarAlgorithm implements Algorithm<ColorProperty> {


    /**
     * Maps each graph to it's colorProperty using this algorithm 
     */
    private static final HashMap<IGraph, ColorProperty> colorations = new HashMap<>();
    
    /**
     * Applies a six-coloring algorithm to the given planar graph.
     * Each node is assigned a color both in the returned ColorProperty
     * and directly on the graph via IGraph setNodeColor
     *
     * @param g the planar graph to color; must not be null
     * @param parameters unused parameter map (may be null)
     * @return a ColorProperty mapping each node to its assigned color
     */
    @Override
    public ColorProperty apply(IGraph g, Map<String, Object> parameters) {
        if (colorations.containsKey(g)) {
            return colorations.get(g);
        }

        ColorProperty resultColor = new ColorProperty();
        IGraph copy = g.copy();
        Stack<INode> pileNode = new Stack<>();
        Map<INode, Integer> colorIds = new HashMap<>();
        Set<Integer> usedColors = new HashSet<>();

        Map<INode, INode> copyToOriginal = OriginaltoCopy(g, copy);

        while (copy.numberOfNodes() > 0) {
            INode nodeToRemove = null;
            for (INode n : copy.getNodes()) {
                if (copy.getNeighbors(n).size() <= 5) {
                    nodeToRemove = n;
                    break;
                }
            }
            if (nodeToRemove != null) {
                pileNode.push(copyToOriginal.get(nodeToRemove));
                copy.delNode(nodeToRemove);
            }
        }

        while (!pileNode.isEmpty()) {
            INode n = pileNode.pop();
            usedColors.clear();
            for (INode neighbor : g.getNeighbors(n)) {
                if (colorIds.containsKey(neighbor)) {
                    usedColors.add(colorIds.get(neighbor));
                }
            }
            int currentColorID = 0;
            while (usedColors.contains(currentColorID)) {
                currentColorID++;
            }
            colorIds.put(n, currentColorID);
            resultColor.setNodeValue(n, Color.generateColor(currentColorID + 1));
        }

        colorations.put(g, resultColor);
        return resultColor;
    }

    /**
     * Builds a map from each copied node to its corresponding original node.
     *
     * @param g the original graph
     * @param copy the copied graph
     * @return a map from copy nodes to original nodes
     */
    private HashMap<INode, INode> OriginaltoCopy(IGraph g, IGraph copy) {
        ArrayList<INode> originalNodes = g.getNodes();
        ArrayList<INode> copiedNodes = copy.getNodes();
        HashMap<INode, INode> copyToOriginal = new HashMap<>();
        for (int i = 0; i < originalNodes.size(); i++) {
            copyToOriginal.put(copiedNodes.get(i), originalNodes.get(i));
        }
        return copyToOriginal;
    }
}