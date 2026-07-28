import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Solver {
    private List<Edge> bestPath;
    private List<Edge> bestCycle;
    private double bestPathDistance;
    private double bestCycleDistance;

    public Solver() {
        this.bestPath = new ArrayList<>();
        this.bestCycle = new ArrayList<>();
        this.bestPathDistance = Double.POSITIVE_INFINITY;
        this.bestCycleDistance = Double.POSITIVE_INFINITY;
    }

    public List<Edge> getBestPath() {
        return this.bestPath;
    }

    public List<Edge> getBestCycle() {
        return this.bestCycle;
    }

    public double getBestPathDistance() {
        return this.bestPathDistance;
    }

    public double getBestCycleDistance() {
        return this.bestCycleDistance;
    }

    public void findShortestPath(Graph graph, Vertex start) {
        bestPath.clear();
        bestPathDistance = Double.POSITIVE_INFINITY;

        Set<Vertex> visited = new HashSet<>();
        visited.add(start);

        List<Edge> currRoute = new ArrayList<>();
        findPath(graph, start, visited, currRoute, 0.0);
    }

    public void findPath(Graph graph, Vertex current, Set<Vertex> visited,
                         List<Edge> currRoute, double currDistance) {
        if(visited.size() == graph.getVertexCount()) {
            if(currDistance < bestPathDistance) {
                bestPathDistance = currDistance;
                bestPath = new ArrayList<>(currRoute);
            }
        }
        else {
            for(Edge e : graph.getNeighbors(current)) {
                Vertex next = e.getDestination();

                if(!visited.contains(next)) {
                    double totalDistance = currDistance + e.getWeight();

                    visited.add(next);
                    currRoute.add(e);

                    findPath(graph, next, visited, currRoute, totalDistance);

                    currRoute.remove(currRoute.size() - 1);
                    visited.remove(next);
                }
            }
        }
    }

    public void findShortestCycle(Graph graph, Vertex start) {
        bestCycle.clear();
        bestCycleDistance = Double.POSITIVE_INFINITY;

        Set<Vertex> visited = new HashSet<>();
        visited.add(start);

        List<Edge> currRoute = new ArrayList<>();
        findCycle(graph, start, start, visited, currRoute, 0.0);
    }

    public void findCycle(Graph graph, Vertex start, Vertex current, Set<Vertex> visited,
                          List<Edge> currRoute, double currDistance) {
        if(visited.size() == graph.getVertexCount()) {
            for(Edge e : graph.getNeighbors(current)) {
                if(e.getDestination().equals(start)) {
                    double totalDistance = currDistance + e.getWeight();

                    if(totalDistance < bestCycleDistance) {
                        bestCycleDistance = totalDistance;
                        bestCycle = new ArrayList<>(currRoute);
                        bestCycle.add(e);
                    }
                }
            }
        }
        else {
            for(Edge e : graph.getNeighbors(current)) {
                Vertex next = e.getDestination();

                if(!visited.contains(next)) {
                    double totalDistance = currDistance + e.getWeight();

                    visited.add(next);
                    currRoute.add(e);

                    findCycle(graph, start, next, visited, currRoute, totalDistance);

                    currRoute.remove(currRoute.size() - 1);
                    visited.remove(next);
                }
            }
        }
    }
}
