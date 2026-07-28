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
        bestPath.clear(); // reset path
        bestPathDistance = Double.POSITIVE_INFINITY; // reset path distance

        Set<Vertex> visited = new HashSet<>();
        visited.add(start);

        List<Edge> currRoute = new ArrayList<>();
        findPath(graph, start, visited, currRoute, 0.0); // recursive helper call
    }

    public void findPath(Graph graph, Vertex current, Set<Vertex> visited,
                         List<Edge> currRoute, double currDistance) {
        if(visited.size() == graph.getVertexCount()) { // if all vertices are visited
            if(currDistance < bestPathDistance) {
                bestPathDistance = currDistance;
                bestPath = new ArrayList<>(currRoute); // clone current route
            }
        }
        else {
            for(Edge e : graph.getNeighbors(current)) { // check unvisited neighbors
                Vertex next = e.getDestination();

                if(!visited.contains(next)) { // if neighbor is unvisited
                    double totalDistance = currDistance + e.getWeight();

                    visited.add(next);
                    currRoute.add(e);

                    findPath(graph, next, visited, currRoute, totalDistance); // recursive method call

                    currRoute.remove(currRoute.size() - 1); // backtracking
                    visited.remove(next);
                }
            }
        }
    }

    public void findShortestCycle(Graph graph, Vertex start) {
        bestCycle.clear(); // reset cycle
        bestCycleDistance = Double.POSITIVE_INFINITY; // reset cycle distance

        Set<Vertex> visited = new HashSet<>();
        visited.add(start);

        List<Edge> currRoute = new ArrayList<>();
        findCycle(graph, start, start, visited, currRoute, 0.0); // recursive helper call
    }

    public void findCycle(Graph graph, Vertex start, Vertex current, Set<Vertex> visited,
                          List<Edge> currRoute, double currDistance) {
        if(visited.size() == graph.getVertexCount()) { // if all vertices are visited
            for(Edge e : graph.getNeighbors(current)) { // check unvisited neighbors
                if(e.getDestination().equals(start)) { // if edge ends with start
                    double totalDistance = currDistance + e.getWeight();

                    if(totalDistance < bestCycleDistance) {
                        bestCycleDistance = totalDistance;
                        bestCycle = new ArrayList<>(currRoute); // clone current route
                        bestCycle.add(e); // add edge to complete cycle
                    }
                }
            }
        }
        else {
            for(Edge e : graph.getNeighbors(current)) { // check unvisited neighbors
                Vertex next = e.getDestination();

                if(!visited.contains(next)) { // if neighbor is unvisited
                    double totalDistance = currDistance + e.getWeight();

                    visited.add(next);
                    currRoute.add(e);

                    findCycle(graph, start, next, visited, currRoute, totalDistance); // recursive method call

                    currRoute.remove(currRoute.size() - 1); // backtracking
                    visited.remove(next);
                }
            }
        }
    }
}
