import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Graph {
    private Map<Vertex, List<Edge>> adjacencyList;

    public Graph() {
        this.adjacencyList = new HashMap<>();
    }

    public void addVertex(Vertex v) {
        adjacencyList.putIfAbsent(v, new ArrayList<>());
    }

    public void addEdge(Vertex source, Vertex destination, double weight) {
        addVertex(source);
        addVertex(destination);
        adjacencyList.get(source).add(new Edge(source, destination, weight));
        adjacencyList.get(destination).add(new Edge(destination, source, weight));
    }

    public List<Edge> getNeighbors(Vertex v) {
        return adjacencyList.getOrDefault(v, Collections.emptyList());
    }

    public int getVertexCount() {
        return adjacencyList.size();
    }
}
