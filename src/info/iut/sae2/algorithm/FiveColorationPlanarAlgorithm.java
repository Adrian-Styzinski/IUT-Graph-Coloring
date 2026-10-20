/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package info.iut.sae2.algorithm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import info.iut.sae2.graphs.Color;
import info.iut.sae2.graphs.IGraph;
import info.iut.sae2.graphs.IEdge;
import info.iut.sae2.graphs.INode;
import info.iut.sae2.properties.ColorProperty;

/**
 * @author sfoussatferr and astyzinski
 * Implementation of a five-coloring algorithm for planar graphs.
 *
 * Uses the fact that every planar graph has at least one node of degree ≤ 5.
 * Works recursively in two cases:
 *
 * Degree ≤ 4: remove the node, color the rest recursively, assign the first
 * available color on return.
 *
 * Degree 5: find two non-adjacent neighbors u and v, merge v into u, remove
 * both the node and v, color the rest recursively, then assign colors on return.
 * Since u and v were merged they share a color, freeing a slot for the degree-5 node.
 *
 * This guarantees at most 5 colors for any planar graph.
 */
public class FiveColorationPlanarAlgorithm implements Algorithm<ColorProperty> {

    /**
     * Maps each graph to its ColorProperty using this algorithm
     */
    private static final HashMap<IGraph, ColorProperty> colorations = new HashMap<>();

    @Override
    public ColorProperty apply(IGraph g, Map<String, Object> parameters) {
        if (colorations.containsKey(g)) {
            return colorations.get(g);
        }
        IGraph copy = g.createGraph();
        Map<INode, INode> originalToCopy = new HashMap<>();
        Map<INode, INode> copyToOriginal = new HashMap<>();

        for (INode n : g.getNodes()) {
            INode nCopy = copy.addNode();
            originalToCopy.put(n, nCopy);
            copyToOriginal.put(nCopy, n);
            copy.setNodePosition(nCopy, g.getNodePosition(n));
            copy.setNodeColor(nCopy, g.getNodeColor(n));
            copy.setNodeSize(nCopy, g.getNodeSize(n));
        }
        for (IEdge e : g.getEdges()) {
            copy.addEdge(originalToCopy.get(g.source(e)), originalToCopy.get(g.target(e)));
        }

        ColorProperty resultColor = new ColorProperty();
        Map<INode, Integer> colorIds = new HashMap<>(g.numberOfNodes() * 2);

        // Run in a dedicated thread with a large stack to avoid StackOverflow on large graphs
        Thread thread = new Thread(null,
            () -> colorierRecursif(copy, resultColor, copyToOriginal, colorIds),
            "five-color-thread",
            64 * 1024 * 1024
        );
        try {
            thread.start();
            thread.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        colorations.put(g, resultColor);
        return resultColor;
    }

    private void colorierRecursif(IGraph copy, ColorProperty resultColor,
                                   Map<INode, INode> toOriginal, Map<INode, Integer> colorIds) {
        if (copy.numberOfNodes() <= 5) {
            int id = 0;
            for (INode copyNode : copy.getNodes()) {
                INode orig = toOriginal.get(copyNode);
                if (orig != null) assignColor(resultColor, colorIds, orig, id++);
            }
            return;
        }

        INode nCopy = findNodeDegreeFour(copy);

        if (nCopy != null) {
            List<INode> copyNeighbors = new ArrayList<>(copy.getNeighbors(nCopy));
            INode originalN = toOriginal.get(nCopy);

            copy.delNode(nCopy);
            colorierRecursif(copy, resultColor, toOriginal, colorIds);

            Set<Integer> usedColors = neighborColorSet(copyNeighbors, toOriginal, colorIds);
            assignColor(resultColor, colorIds, originalN, firstAvailable(usedColors));

        } else {
            nCopy = findNodeDegreeFive(copy);
            List<INode> copyNeighbors = new ArrayList<>(copy.getNeighbors(nCopy));

            INode[] pair = findNonAdjacentPair(copyNeighbors, copy);
            INode uCopy = pair[0];
            INode vCopy = pair[1];

            // Merge v into u before deletion
            for (INode vn : new ArrayList<>(copy.getNeighbors(vCopy))) {
                if (!vn.equals(uCopy) && !copy.existEdge(uCopy, vn, false)) {
                    copy.addEdge(uCopy, vn);
                }
            }

            INode originalN = toOriginal.get(nCopy);
            INode originalU = toOriginal.get(uCopy);
            INode originalV = toOriginal.get(vCopy);

            copy.delNode(nCopy);
            copy.delNode(vCopy);

            colorierRecursif(copy, resultColor, toOriginal, colorIds);

            Set<Integer> usedColors = neighborColorSet(copyNeighbors, toOriginal, colorIds);
            assignColor(resultColor, colorIds, originalN, firstAvailable(usedColors));

            if (originalU != null && originalV != null) {
                assignColor(resultColor, colorIds, originalV, colorIds.get(originalU));
            }
        }
    }

    /**
     * Assigns a color to the given original node in both the result and the colorIds map.
     */
    private void assignColor(ColorProperty resultColor, Map<INode, Integer> colorIds,
                              INode original, int colorId) {
        if (original == null) return;
        colorIds.put(original, colorId);
        resultColor.setNodeValue(original, Color.generateColor(colorId + 1));
    }

    /**
     * Returns the set of color ids already used by the given copy neighbors.
     * Using a Set gives O(1) lookup in firstAvailable instead of O(N).
     */
    private Set<Integer> neighborColorSet(List<INode> copyNeighbors, Map<INode, INode> toOriginal,
                                           Map<INode, Integer> colorIds) {
        Set<Integer> used = new HashSet<>(copyNeighbors.size() * 2);
        for (INode cn : copyNeighbors) {
            INode orig = toOriginal.get(cn);
            if (orig != null) {
                Integer id = colorIds.get(orig);
                if (id != null) used.add(id);
            }
        }
        return used;
    }

    /**
     * Returns the first color id not in the used set.
     * Bounded to 6 iterations since planar graphs need at most 5 colors.
     */
    private int firstAvailable(Set<Integer> used) {
        for (int i = 0; i < 6; i++) {
            if (!used.contains(i)) return i;
        }
        return 0;
    }

    /**
     * Finds a node of degree ≤ 4, or null if none exists.
     */
    private INode findNodeDegreeFour(IGraph g) {
        for (INode n : g.getNodes()) {
            if (g.getNeighbors(n).size() <= 4) return n;
        }
        return null;
    }

    /**
     * Finds a node of degree exactly 5, or null if none exists.
     */
    private INode findNodeDegreeFive(IGraph g) {
        for (INode n : g.getNodes()) {
            if (g.getNeighbors(n).size() == 5) return n;
        }
        return null;
    }

    /**
     * Finds a non-adjacent pair among the given neighbors.
     * At most degree 5, so the double loop runs at most 10 iterations -> O(1).
     */
    private INode[] findNonAdjacentPair(List<INode> neighbors, IGraph g) {
        for (int i = 0; i < neighbors.size(); i++) {
            for (int j = i + 1; j < neighbors.size(); j++) {
                INode u = neighbors.get(i);
                INode v = neighbors.get(j);
                if (!g.existEdge(u, v, false)) return new INode[]{u, v};
            }
        }
        return null;
    }
}