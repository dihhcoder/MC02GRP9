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

    public void findShortestPath(Graph g, Vertex start) {
        bestPath.clear(); // reset path
        bestPathDistance = Double.POSITIVE_INFINITY; // reset path distance

        Set<Vertex> visited = new HashSet<>();
        visited.add(start);

        List<Edge> currentRoute = new ArrayList<>();
        findPath(g, start, visited, currentRoute, 0.0); // recursive helper call
    }

    public void findPath(Graph g, Vertex current, Set<Vertex> visited,
                         List<Edge> currentRoute, double currentDistance) {
        if(visited.size() == g.getVertexCount()) { // if all vertices are visited
            if(currentDistance < bestPathDistance) {
                bestPathDistance = currentDistance;
                bestPath = new ArrayList<>(currentRoute); // clone current route
            }
        }
        else {
            for(Edge e : g.getNeighbors(current)) { // check unvisited neighbors
                Vertex next = e.getDestination();

                if(!visited.contains(next)) { // if neighbor is unvisited
                    double totalDistance = currentDistance + e.getWeight();

                    visited.add(next);
                    currentRoute.add(e);

                    findPath(g, next, visited, currentRoute, totalDistance); // recursive method call

                    currentRoute.remove(currentRoute.size() - 1); // backtracking
                    visited.remove(next);
                }
            }
        }
    }

    public void findShortestCycle(Graph g, Vertex start) {
        bestCycle.clear(); // reset cycle
        bestCycleDistance = Double.POSITIVE_INFINITY; // reset cycle distance

        Set<Vertex> visited = new HashSet<>();
        visited.add(start);

        List<Edge> currentRoute = new ArrayList<>();
        findCycle(g, start, start, visited, currentRoute, 0.0); // recursive helper call
    }

    public void findCycle(Graph g, Vertex start, Vertex current, Set<Vertex> visited,
                          List<Edge> currentRoute, double currentDistance) {
        if(visited.size() == g.getVertexCount()) { // if all vertices are visited
            for(Edge e : g.getNeighbors(current)) { // check unvisited neighbors
                if(e.getDestination().equals(start)) { // if edge ends with start
                    double totalDistance = currentDistance + e.getWeight();

                    if(totalDistance < bestCycleDistance) {
                        bestCycleDistance = totalDistance;
                        bestCycle = new ArrayList<>(currentRoute); // clone current route
                        bestCycle.add(e); // add edge to complete cycle
                    }
                }
            }
        }
        else {
            for(Edge e : g.getNeighbors(current)) { // check unvisited neighbors
                Vertex next = e.getDestination();

                if(!visited.contains(next)) { // if neighbor is unvisited
                    double totalDistance = currentDistance + e.getWeight();

                    visited.add(next);
                    currentRoute.add(e);

                    findCycle(g, start, next, visited, currentRoute, totalDistance); // recursive method call

                    currentRoute.remove(currentRoute.size() - 1); // backtracking
                    visited.remove(next);
                }
            }
        }
    }
}
