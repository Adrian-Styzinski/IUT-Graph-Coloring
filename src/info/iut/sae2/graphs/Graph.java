/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package info.iut.sae2.graphs;

import info.iut.sae2.properties.ColorProperty;
import info.iut.sae2.properties.LayoutProperty;
import info.iut.sae2.properties.SizeProperty;
import info.iut.sae2.algorithm.Algorithm;
import info.iut.sae2.algorithm.FiveColorationPlanarAlgorithm;
import info.iut.sae2.algorithm.SixColorationPlanarAlgorithm;
import info.iut.sae2.algorithm.WelshAndPowell;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author sfoussatferr and astyzinski
 */
public class Graph implements IGraph {

    /**
     * List of all nodes currently in this graph
     */
    private final ArrayList<INode> nodes;
    
    /**
     * List of all edges currently in this graph
     */
    private final ArrayList<IEdge> edges;
    /**
     * Layout property storing the position of each node
     */
    private final LayoutProperty layout;
    
    /**
     * Color property storing the color of each node and each edge
     */
    private final ColorProperty color;
    
    /** 
     * Size property storing the size of each node and the width of each edge
     */
    private final SizeProperty sizes;
    
    /**
     * Maps each node to its outgoing edges
     */
    private final HashMap<INode, ArrayList<IEdge>> outEdges;

    /**
     * Maps each node to its incoming edges
     */
    private final HashMap<INode, ArrayList<IEdge>> inEdges;

    /**
     * Maps each source node to a map of target node -> edge.
     */
    private final HashMap<INode, HashMap<INode, IEdge>> edgeLookup;

    /**
     * Default constructor which makes an empty graph with no nodes nor edges,
     * and initialises default layout, color size properties, outEdges, inEdges and edgeLookup
     */
    public Graph() {
        nodes = new ArrayList<>();
        edges = new ArrayList<>();
        layout = new LayoutProperty();
        color = new ColorProperty();
        sizes = new SizeProperty();
        outEdges = new HashMap<>();
        inEdges = new HashMap<>();
        edgeLookup = new HashMap<>();
    }

    /**
     * Creates and returns a new empty Graph
     *
     * @return a new empty Graph instance
     */
    @Override
    public IGraph createGraph()
    {
        return new Graph();
    }

    /**
     * Creates and returns a deep copy of this graph.
     * All nodes and edges are duplicated, and their layout, color and size properties are copied to the new graph.
     *
     * @return a new Graph deep copy of this graph
     */
    @Override
    public IGraph copy() {
        IGraph copy = new Graph();
        Map<INode, INode> nodeMap = new HashMap<>();
        
        for (INode node : nodes) {
            INode nCopy = copy.addNode();
            nodeMap.put(node, nCopy);
            copy.setNodePosition(nCopy, getNodePosition(node));
            copy.setNodeColor(nCopy, getNodeColor(node));
            copy.setNodeSize(nCopy, getNodeSize(node));
        }
        
        for (IEdge e : edges) {
            IEdge eCopy = copy.addEdge(nodeMap.get(source(e)), nodeMap.get(target(e)));
            copy.setEdgeColor(eCopy, getEdgeColor(e));
            copy.setEdgeWidth(eCopy, getEdgeWidth(e));
            copy.setEdgePosition(eCopy, getEdgePosition(e));
        }
        return copy;
    }

    /**
     * Creates a new empty node, adds it to the graph with default properties, and returns it.
     *
     * @return the newly added node
     */
    @Override
    public INode addNode() {
        Node node = new Node();
        nodes.add(node);
        outEdges.put(node, new ArrayList<>());
        inEdges.put(node, new ArrayList<>());
        edgeLookup.put(node, new HashMap<>());
        layout.setNodeValue(node, new Coord(LayoutProperty.DEFAULT_NODE_POS));
        color.setNodeValue(node, new Color(ColorProperty.DEFAULT_NODE_COL));
        sizes.setNodeValue(node, new Size(SizeProperty.DEFAULT_NODE_SIZE));
        return node;
    }

    /**
     * Adds the given node to this graph and returns it.
     * If any property (position, color, size) is not yet set for the node,
     * the default value is applied.
     *
     * @param node the node to add to the graph
     * @return the added node or null if n was null
     */
    @Override
    public INode addNode(INode node) {
        if (node == null) {
            return null;
        }
        nodes.add(node);
        outEdges.put(node, new ArrayList<>());
        inEdges.put(node, new ArrayList<>());
        edgeLookup.put(node, new HashMap<>());
        if (layout.getNodeValue(node) == null) {
            layout.setNodeValue(node, new Coord(LayoutProperty.DEFAULT_NODE_POS));
        }
        if (color.getNodeValue(node) == null) {
                color.setNodeValue(node, new Color(ColorProperty.DEFAULT_NODE_COL));
        }
        if (sizes.getNodeValue(node) == null) {
            sizes.setNodeValue(node, new Size(SizeProperty.DEFAULT_NODE_SIZE));
        }
        return node;
    }

    /**
     * Removes the given node and all its edges from the graph and property maps
     * Does nothing but logs an error if the node is null or not present in the graph
     *
     * @param node the node to remove
     */
    @Override
    public void delNode(INode node) {
        if (node == null || !nodes.contains(node)){
            System.err.println("Node couldn't be deleted doesn't exist");
            return;
        }
        for (IEdge edge : new ArrayList<>(getInOutEdges(node))){
            delEdge(edge);
        }
        outEdges.remove(node);
        inEdges.remove(node);
        edgeLookup.remove(node);
        nodes.remove(node);
        layout.delNode(node);
        color.delNode(node);
        sizes.delNode(node);
    }

    /**
     * Creates a new edge with default properties from src to tgt, adds it to the graph and then returns it.
     *
     * @param src source node of the new edge
     * @param tgt target node of the new edge
     * @return the newly added edge
     */
    @Override
    public IEdge addEdge(INode src, INode tgt) {
        Edge edge = new Edge(src, tgt);
        edges.add(edge);
        outEdges.get(src).add(edge);
        inEdges.get(tgt).add(edge);
        edgeLookup.get(src).put(tgt, edge);
        layout.setEdgeValue(edge, new ArrayList<>());
        color.setEdgeValue(edge, new Color(ColorProperty.DEFAULT_EDGE_COL));
        sizes.setEdgeValue(edge, SizeProperty.DEFAULT_EDGE_WIDTH);
        return edge;
    }

    /**
     * Adds the given edge to this graph and returns it.
     * If any property (position, color, width) is not yet set for the edge,
     * the default value is applied.
     * Returns null and logs an error if e is null
     *
     * @param edge the edge to add to the graph
     * @return the added edge, or null if e was null
     */
    @Override
    public IEdge addEdge(IEdge edge) {
        if (edge == null){
            System.err.println("Can't add null edge");
            return null;
        }
        edges.add(edge);
        outEdges.get(edge.source()).add(edge);
        inEdges.get(edge.target()).add(edge);
        edgeLookup.get(edge.source()).put(edge.target(), edge);
        if (layout.getEdgeValue(edge) == null) {
            layout.setEdgeValue(edge, new ArrayList<>());
        }
        if (color.getEdgeValue(edge) == null) {
            color.setEdgeValue(edge, new Color(ColorProperty.DEFAULT_EDGE_COL));
        }
        if (sizes.getEdgeValue(edge) == null) {
            sizes.setEdgeValue(edge, SizeProperty.DEFAULT_EDGE_WIDTH);
        }
        return edge;
    }

    /**
     * Removes the given edge from this graph and from all internal property maps.
     * Does nothing but logs an error if the edge is null or not present in the graph.
     *
     * @param edge the edge to remove
     */
    @Override
    public void delEdge(IEdge edge) {
        if (edge == null || !edges.contains(edge)) {
            System.err.println("Edge couldn't be find or probably doesn't exist");
            return;
        }
        edges.remove(edge);
        outEdges.get(edge.source()).remove(edge);
        inEdges.get(edge.target()).remove(edge);
        edgeLookup.get(edge.source()).remove(edge.target());
        layout.delEdge(edge); color.delEdge(edge); sizes.delEdge(edge);
    }

    /**
     * Returns the number of nodes in the graph
     *
     * @return the number of nodes
     */
    @Override
    public int numberOfNodes(){
        return nodes.size();
    }
    /**
     * Returns the number of edges in the graph
     *
     * @return the number of edges
     */
    @Override
    public int numberOfEdges(){
        return edges.size();
    }
    /**
     * Returns a copy of the list of all nodes in the graph
     *
     * @return an ArrayList containing all nodes of the graph
     */
    @Override
    public ArrayList<INode> getNodes() {
        return new ArrayList<>(nodes);
    }
    /**
     * Returns a copy of the list of all edges in the graph
     *
     * @return an ArrayList containing all edges of the graph
     */
    @Override
    public ArrayList<IEdge> getEdges() {
        return new ArrayList<>(edges);
    }
    /**
     * Returns the source node of the given edge
     *
     * @param edge the edge whose source is wanted
     * @return the source node of the edge
     */
    @Override
    public INode source(IEdge edge) {
        return edge.source();
    }
    /**
     * Returns the target node of the given edge
     *
     * @param edge the edge whose target is wanted
     * @return the target node of the edge
     */
    @Override
    public INode target(IEdge edge) {
        return edge.target();
    }

    /**
     * Returns the list of successor nodes (direct targets of outgoing edges) of the given node
     *
     * @param node the node whose successors are wanted
     * @return an ArrayList of successor nodes
     */
    @Override
    public ArrayList<INode> getSuccesors(INode node) {
        ArrayList<INode> successors = new ArrayList<>();
        for (IEdge edge : outEdges.get(node))
            successors.add(edge.target());
        return successors;
    }

    /**
     * Returns the list of predecessor nodes (direct sources of incoming edges) of the given node
     *
     * @param node the node whose predecessors are wanted
     * @return an ArrayList of predecessor nodes
     */
    @Override
    public ArrayList<INode> getPredecessors(INode node) {
        ArrayList<INode> predecessors = new ArrayList<>();
        for (IEdge edge : inEdges.get(node)){
            predecessors.add(edge.source());
        }
        return predecessors;
    }

    /**
     * Returns all distinct neighbors (successors and predecessors) of the given node
     * (edges are considered undirected)
     *
     * @param node the node whose neighbors are wanted
     * @return an ArrayList of neighboring nodes (no duplicates)
     */
    @Override
    public ArrayList<INode> getNeighbors(INode node) {
        ArrayList<INode> neighbors = new ArrayList<>();
        
        for (IEdge edge : outEdges.get(node)) {
            if (!neighbors.contains(edge.target())) {
                neighbors.add(edge.target());
            }
        }
        
        for (IEdge edge : inEdges.get(node)){
            if (!neighbors.contains(edge.source())) {
                neighbors.add(edge.source());
            }
        }
        return neighbors;
    }

    /**
     * Returns all edges connected to the given node (incoming and outgoing)
     *
     * @param node the node whose edges are wanted
     * @return an ArrayList of all edges connected to the node
     */
    @Override
    public ArrayList<IEdge> getInOutEdges(INode node) {
        ArrayList<IEdge> inOutEdges = new ArrayList<>(outEdges.get(node));
        for (IEdge edge : inEdges.get(node)) {
            if (!inOutEdges.contains(edge)) {
                inOutEdges.add(edge);
            }
        }
        return inOutEdges;
    }

    /**
     * Returns the list of incoming edges
     *
     * @param node the node whose incoming edges are wanted
     * @return an ArrayList of incoming edges
     */
    @Override
    public ArrayList<IEdge> getInEdges(INode node) {
        return new ArrayList<>(inEdges.get(node));
    }

    /**
     * Returns the list of outgoing edges
     *
     * @param node the node whose outgoing edges are wanted
     * @return an ArrayList of outgoing edges
     */
    @Override
    public ArrayList<IEdge> getOutEdges(INode node) {
        return new ArrayList<>(outEdges.get(node));
    }

    /**
     * Returns the in-degree (number of incoming edges) of the given node
     *
     * @param node the node whose in-degree is wanted
     * @return the in-degree of the node
     */
    @Override
    public int inDegree(INode node) {
        return inEdges.get(node).size();
    }
    /**
     * Returns the out-degree (number of outgoing edges) of the given node
     *
     * @param node the node whose out-degree is wanted
     * @return the out-degree of the node
     */
    @Override
    public int outDegree(INode node) {
        return outEdges.get(node).size();
    }
    /**
     * Returns the degree (number of neighbors) of the given node
     * (edges are considering undirected)
     *
     * @param n the node whose degree is wanted
     * @return the degree of the node
     */
    @Override
    public int degree(INode n) {
        return getNeighbors(n).size();
    }

    /**
     * Returns true if an edge exists between src and tgt
     * If oriented is true, only the directed edge src to tgt is checked;
     * otherwise both directions are considered.
     *
     * @param src the source node of the edge to look for
     * @param tgt the target node of the edge to look for
     * @param oriented if true considers the graph as directed
     * @return true if such an edge exists, false otherwise
     */
    @Override
    public boolean existEdge(INode src, INode tgt, boolean oriented) {
        return getEdge(src, tgt, oriented) != null;
    }

    /**
     * Returns the edge between src and tgt if it exists, or null
     * If oriented is false, also considers the reverse direction tgt to src.
     *
     * @param src the source node of the edge to look for
     * @param tgt the target node of the edge to look for
     * @param oriented if true, considers the graph as directed
     * @return the matching edge, or null if none is found
     */
    @Override
    public IEdge getEdge(INode src, INode tgt, boolean oriented) {
        IEdge edge = edgeLookup.get(src).get(tgt);
        if (edge != null) {
            return edge;
        }
        if (!oriented) {
            return edgeLookup.get(tgt).get(src);
        }
        return null;
    }

    /**
     * Returns the LayoutProperty storing positions for all nodes and edges
     *
     * @return the layout property of this graph
     */
    @Override
    public LayoutProperty getLayout() {
        return layout;
    }
    
    /**
     * Returns the position of the given node. Use a falls back to the default position
     * and logs an error if the node has no registered position.
     *
     * @param node the node whose position is wanted
     * @return the coordinates of the node
     */
    @Override
    public Coord getNodePosition(INode node) {
        Coord coords = layout.getNodeValue(node);
        if (coords == null){
            coords = new Coord(LayoutProperty.DEFAULT_NODE_POS);
            System.err.println("Node position doesn't exist");
        }
        return coords;
    }
    
    /**
     * Returns the bend points (or intermediate points) of the given edge. Returns an empty list and logs an error
     * if the edge has no position.
     *
     * @param edge the edge whose bend points (or intermediate points) are wanted
     * @return the list of bend (or intermediate) coordinates of the edge
     */
    @Override
    public ArrayList<Coord> getEdgePosition(IEdge edge) {
        ArrayList<Coord> bends = layout.getEdgeValue(edge);
        if (bends == null){
            bends = new ArrayList<>();
            System.err.println("Edge position doesn't exist");
        }
        return bends;
    }
    
    /**
     * Sets the position of the given node
     *
     * @param node the node whose position is set
     * @param coords the new coordinates for the node
     */
    @Override
    public void setNodePosition(INode node, Coord coords) {
        layout.setNodeValue(node, coords);
    }
    
    /**
     * Sets the bend points (or intermediate points) of the given edge
     *
     * @param edge the edge whose bend points (or intermediate points) are set
     * @param bends the new list of bend (or intermediate) coordinates
     */
    @Override
    public void setEdgePosition(IEdge edge, ArrayList<Coord> bends) 
    {
        layout.setEdgeValue(edge, bends);
    }
    
    /**
     * Sets the same position for all nodes in the graph
     *
     * @param coords the coordinates to assign to every node
     */
    @Override
    public void setAllNodesPositions(Coord coords) {
        for (INode node : nodes) {
            layout.setNodeValue(node, coords);
        }
    }
    
    /**
     * Sets the same bend points (or intermediate points) for all edges in this graph.
     *
     * @param bends the list of bend coordinates to assign to every edge
     */
    @Override
    public void setAllEdgesPositions(ArrayList<Coord> bends) {
        for (IEdge edge : edges)
            layout.setEdgeValue(edge, bends);
    }

    /**
     * Computes and returns the bounding box of the graph drawing as a list of two coordinates:
     * the top-left corner (minimum x, minimum y) at index 0 and the bottom-right corner
     * (maximum x, maximum y) at index 1. Returns a box at the origin if the graph has no nodes.
     *
     * @return an ArrayList of two Coord: [top-left corner, bottom-right corner]
     */
    @Override
    public ArrayList<Coord> getBoundingBox() {
        ArrayList<Coord> boundingBox = new ArrayList<>();
        if (nodes.isEmpty()) {
            System.err.println("Nodes is empty");
            boundingBox.add(new Coord(0, 0));
            boundingBox.add(new Coord(0, 0));
            return boundingBox;
        }
        double minX = Double.MAX_VALUE, minY = Double.MAX_VALUE;
        double maxX = -Double.MAX_VALUE, maxY = -Double.MAX_VALUE;
        for (INode node : nodes) {
            Coord nodeCoords = getNodePosition(node);
            double radius = getNodeSize(node).getW() / 2;
            if (nodeCoords.getX() - radius < minX) {
                minX = nodeCoords.getX() - radius;
            }
            if (nodeCoords.getY() - radius < minY) {
                minY = nodeCoords.getY() - radius;
            }
            if (nodeCoords.getX() + radius > maxX) {
                maxX = nodeCoords.getX() + radius;
            }
            if (nodeCoords.getY() + radius > maxY) {
                maxY = nodeCoords.getY() + radius;
            }
        }
        boundingBox.add(new Coord(minX, minY));
        boundingBox.add(new Coord(maxX, maxY));
        return boundingBox;
    }

    /**
     * Returns the SizeProperty storing sizes for all nodes and edge widths.
     *
     * @return the size property of the graph
     */
    @Override
    public SizeProperty getSizes() {
        return sizes;
    }

    /**
     * Returns the size of the given node. Uses a fall back to the default size and logs
     * an error if the node has no size.
     *
     * @param node the node whose size is wanted
     * @return the size of the node
     */
    @Override
    public Size getNodeSize(INode node) {
        Size size = sizes.getNodeValue(node);
        if (size == null) {
            System.err.println("node size is null");
            size = new Size(SizeProperty.DEFAULT_NODE_SIZE);
        }
        return size;
    }
    
    /**
     * Returns the width of the given edge. Uses a fall back to the default width and logs
     * an error if the edge has no width.
     *
     * @param edge the edge whose width is wanted
     * @return the width of the edge
     */
    @Override
    public Double getEdgeWidth(IEdge edge) {
        Double width = sizes.getEdgeValue(edge);
        if (width == null) {
            System.err.println("Edge width is null");
            width = SizeProperty.DEFAULT_EDGE_WIDTH;
        }
        return width;
    }
    
    /**
     * Sets the size of the given node
     *
     * @param node the node whose size is set
     * @param size the new size for the node
     */
    @Override
    public void setNodeSize(INode node, Size size) {
        sizes.setNodeValue(node, size);
    }
    /**
     * Sets the width of the given edge
     *
     * @param edge the edge whose width is set
     * @param width the new width for the edge
     */
    @Override
    public void setEdgeWidth(IEdge edge, Double width) {
        sizes.setEdgeValue(edge, width);
    }
    /**
     * Sets the same size for all nodes in the graph
     *
     * @param size the size to assign to every node
     */
    @Override
    public void setAllNodesSizes(Size size) {
        for (INode node : nodes){
            sizes.setNodeValue(node, size);
        }
    }
    
    /**
     * Sets the same width for all edges in the graph
     *
     * @param w the width to assign to every edge
     */
    @Override
    public void setAllEdgesWidths(Double w) {
        for (IEdge e : edges) {
            sizes.setEdgeValue(e, w);
        }
    }

    /**
     * Returns the ColorProperty storing colors for all nodes and edges.
     *
     * @return the color property of the graph
     */
    @Override
    public ColorProperty getColor() {
        return color;
    }
    
    /**
     * Returns the color of the given node. Uses a fall back to the default node color
     * and logs an error if the node has no color
     *
     * @param n the node whose color is wanted
     * @return the color of the node
     */
    @Override
    public Color getNodeColor(INode n) {
        Color nodeColor = color.getNodeValue(n);
        if (nodeColor  == null) {
            System.err.println("Node doesn't exist");
            nodeColor = new Color(ColorProperty.DEFAULT_NODE_COL);
        }
        return nodeColor ;
    }
    /**
     * Returns the color of the given edge. Uses a Fall back to the default node color
     * and logs an error if the edge has no color
     *
     * @param e the edge whose color is wanted
     * @return the color of the edge
     */
    @Override
    public Color getEdgeColor(IEdge e) {
        Color edgeColor = color.getEdgeValue(e);
        if ( edgeColor  == null) {
            System.err.println("Edge doesn't exist");
            edgeColor = new Color(ColorProperty.DEFAULT_NODE_COL);
        }
        return edgeColor;
    }
    /**
     * Sets the color of the given node
     *
     * @param n the node whose color is set
     * @param c the new color for the node
     */
    @Override
    public void setNodeColor(INode n, Color c) {
        color.setNodeValue(n, c);
    }
    /**
     * Sets the color of the given edge
     *
     * @param e the edge whose color is set
     * @param c the new color for the edge
     */
    @Override
    public void setEdgeColor(IEdge e, Color c) {
        color.setEdgeValue(e, c);
    }
    /**
     * Sets the same color for all nodes in this graph
     *
     * @param c the color to assign to every node
     */
    @Override 
    public void setAllNodesColors(Color c) {
        for (INode n : nodes) {
            color.setNodeValue(n, c);
        }
    }
    /**
     * Sets the same color for all edges in this graph.
     *
     * @param c the color to assign to every edge
     */
    @Override
    public void setAllEdgesColors(Color c) {
        for (IEdge e : edges) {
            color.setEdgeValue(e, c);
        }
    }

    /**
     * Colors the nodes of the graph using the Welsh and Powell greedy coloring algorithm
     */
    @Override
    public void welshAndPowell() {
        Algorithm<ColorProperty> algo = new WelshAndPowell();
        ColorProperty result = algo.apply(this, null);
        
        for(INode node : nodes) {
            this.setNodeColor(node, result.getNodeValue(node));
        }
    }
    
    /**
     * Colors the nodes of the graph using a six-coloring algorithm for planar graphs
     */
    @Override
    public void sixColors() {
        Algorithm<ColorProperty> algo = new SixColorationPlanarAlgorithm();
        ColorProperty result = algo.apply(this, null);
        for(INode node : nodes) {
            this.setNodeColor(node, result.getNodeValue(node));
        }
    }
    
    /**
     * Colors the nodes of the graph using a five-coloring algorithm for planar graphs
     */
    @Override
    public void fiveColors() {
        Algorithm<ColorProperty> algo = new FiveColorationPlanarAlgorithm();
        ColorProperty result = algo.apply(this, null);
        for(INode node : nodes) {
            this.setNodeColor(node, result.getNodeValue(node));
        }
    }
}

