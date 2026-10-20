/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package info.iut.sae2.algorithm;

import info.iut.sae2.graphs.IGraph;
import info.iut.sae2.graphs.INode;
import info.iut.sae2.graphs.Color;
import info.iut.sae2.properties.ColorProperty;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @author sfoussatferr and astyzinski
 * Implementation of the Welsh & Powell greedy graph-coloring algorithm.
 * Nodes are sorted in descending order of degree, then colored 'greedily':
 * each pass assigns the current color to every uncolored node that has no
 * already-colored neighbor with that color.
 *
 * This algorithm however does not guarantee an optimal (minimum) number of colors
 */
public class WelshAndPowell implements Algorithm<ColorProperty> {

    /**
     * Maps each graph to it's colorProperty using this algorithm 
     */
    private static final HashMap<IGraph, ColorProperty> colorations = new HashMap<>();
    
    /**
     * Applies the Welsh and Powell coloring algorithm to the given graph.
     * Each node is assigned a color both in the returned ColorProperty
     * and directly on the graph via IGraph setNodeColor
     *
     * @param g the graph to color; must not be null
     * @param parameters unused parameter map (may be null)
     * @return a ColorProperty mapping each node to its assigned color
     */
    @Override
        public ColorProperty apply(IGraph g, Map<String, Object> parameters) {
        if (colorations.containsKey(g)) {
            return colorations.get(g);
        }

        ColorProperty resultColor = new ColorProperty();
        ArrayList<INode> nodes = new ArrayList<>(g.getNodes());
        nodes.sort((a, b) -> Integer.compare(g.getNeighbors(b).size(), g.getNeighbors(a).size()));

        Map<INode, Integer> colorIds = new HashMap<>();
        initialiseMap(colorIds, nodes);

        int currentColorID = 0;
        int nodesColored = 0;
        
        //Algo
        while (nodesColored < nodes.size()) {
            for (INode n : nodes) {
                if (colorIds.get(n) == -1) {
                    boolean canColor = true;
                    for (INode neighbor : g.getNeighbors(n)) {
                        if (colorIds.get(neighbor) == currentColorID) {
                            canColor = false;
                        }
                    }
                    if (canColor) {
                        colorIds.put(n, currentColorID);
                        resultColor.setNodeValue(n, Color.generateColor(currentColorID + 1));
                        nodesColored++;
                    }
                }
            }
            currentColorID++;
        }

        colorations.put(g, resultColor);
        return resultColor;
    }
        
    /**
     * Initialises every node in the map to -1 (uncolored)
     *
     * @param map the colorId map to initialise
     * @param nodes the list of nodes to register
     */
    private void initialiseMap(Map<INode, Integer> map, ArrayList<INode> nodes) {
        for (INode n : nodes) {
            map.put(n, -1);
        }
    }

    
}