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
        bestPath.clear();
        bestPathDistance = Double.POSITIVE_INFINITY;

        Set<Vertex> visited = new HashSet<>();
        visited.add(start);

        List<Edge> currentRoute = new ArrayList<>();
        findPath(g, start, visited, currentRoute, 0.0);
    }

    public void findPath(Graph g, Vertex current, Set<Vertex> visited,
                         List<Edge> currentRoute, double currentDistance) {
        if(visited.size() == g.getVertexCount()) {
            if(currentDistance < bestPathDistance) {
                bestPathDistance = currentDistance;
                bestPath = new ArrayList<>(currentRoute);//bestPath = (List<Edge>) currentRoute.clone();
            }
        }
        else {
            for(Edge e : g.getNeighbors(current)) {
                Vertex next = e.getDestination();

                if(!visited.contains(next)) {
                    double totalDistance = currentDistance + e.getWeight();

                    visited.add(next);
                    currentRoute.add(e);

                    findPath(g, next, visited, currentRoute, totalDistance);

                    currentRoute.remove(currentRoute.size() - 1);
                    visited.remove(next);
                }
            }
        }
    }

    public void findShortestCycle(Graph g, Vertex start) {
        bestCycle.clear();
        bestCycleDistance = Double.POSITIVE_INFINITY;

        Set<Vertex> visited = new HashSet<>();
        visited.add(start);

        List<Edge> currentRoute = new ArrayList<>();
        findCycle(g, start, start, visited, currentRoute, 0.0);
    }

    public void findCycle(Graph g, Vertex start, Vertex current, Set<Vertex> visited,
                          List<Edge> currentRoute, double currentDistance) {
        if(visited.size() == g.getVertexCount()) {

            for(Edge e : g.getNeighbors(current)) {
                if(e.getDestination().equals(start)) {
                    double totalDistance = currentDistance + e.getWeight();

                    if(totalDistance < bestCycleDistance) {
                        bestCycleDistance = totalDistance;
                        bestCycle = new ArrayList<>(currentRoute);//bestCycle = (List<Edge>) currentRoute.clone();
                        bestCycle.add(e);
                    }
                }
            }
        }
        else {
            for(Edge e : g.getNeighbors(current)) {
                Vertex next = e.getDestination();

                if(!visited.contains(next)) {
                    double totalDistance = currentDistance + e.getWeight();

                    visited.add(next);
                    currentRoute.add(e);

                    findCycle(g, start, next, visited, currentRoute, totalDistance);

                    currentRoute.remove(currentRoute.size() - 1);
                    visited.remove(next);
                }
            }
        }
    }
}
