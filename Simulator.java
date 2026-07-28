import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Scanner;

public class Simulator {
    private List<Route> mapRoutes;
    private List<Route> officeRoutes;
    private Solver solver;
    private boolean isOver;

    public Simulator() {
        this.mapRoutes = new ArrayList<>();
        this.officeRoutes = new ArrayList<>();
        this.solver = new Solver();
        this.isOver = false;
    }

    public void runSimulation(Scanner sc) {
        mapRoutes = Input.readMapFileLocation(sc);
        officeRoutes = Input.readOfficeFileLocation(sc);

        while(!isOver) {
            List<String> localDestinations = new ArrayList<>();
            Deque<String> pendingDestinations = new ArrayDeque<>();

            List<String> offices = getUniquePostOffices();
            Vertex startOffice = selectStartingOffice(sc, offices);

            processDeliveries(sc, pendingDestinations, localDestinations, startOffice);

            checkIfOver(sc);
        }
    }

    public void processDeliveries(Scanner sc, Deque<String> pendingDestinations,
                                  List<String> localDestinations, Vertex startOffice) {
        boolean isFirstOffice = true;

        do {
            int mailCount = readMailCount(sc, isFirstOffice);
            isFirstOffice = false;

            List<String> destinations = Input.readDestinations(sc, mapRoutes, mailCount, pendingDestinations);

            for(String s : destinations)
                pendingDestinations.add(s);

            String originCity = getOriginCity(startOffice);
            filterDestinations(pendingDestinations, localDestinations, originCity);
            System.out.println();

            Graph routeGraph = createRouteGraph(localDestinations, originCity, startOffice);
            solver.findShortestCycle(routeGraph, startOffice);

            List<Edge> deliveryRoute = solver.getBestCycle();
            double deliveryDistance = solver.getBestCycleDistance();

            for(Edge e : deliveryRoute) // temp display for route
                System.out.printf("%s -> %s %.1f km%n",
                        e.getSource().name(),
                        e.getDestination().name(),
                        e.getWeight());

            System.out.println();
            System.out.printf("Total Distance Covered: %.1f km%n", deliveryDistance); // end of display

            List<String> nextOffices = findNextPostOffices(pendingDestinations);

            Graph postOfficeGraph = createPostOfficeGraph(nextOffices, originCity);
            Vertex currOffice = new Vertex(originCity + " Post Office");
            solver.findShortestPath(postOfficeGraph, currOffice);

            startOffice = updateStartingOffice(startOffice);
            System.out.println();

            localDestinations.clear();

        } while(!pendingDestinations.isEmpty());
    }

    public List<String> getUniquePostOffices() {
        List<String> offices = new ArrayList<>();
        System.out.println("List of Post Offices:");

        for(Route r : officeRoutes) {
            String place1 = r.getPlace1();
            String place2 = r.getPlace2();

            if(!offices.contains(place1))
                offices.add(place1);

            if(!offices.contains(place2))
                offices.add(place2);
        }

        for(int i = 0; i < offices.size(); i++)
            System.out.println((i + 1) + " - " + offices.get(i));

        return offices;
    }

    public Vertex selectStartingOffice(Scanner sc, List<String> offices) {
        System.out.print("Select the number of the post office to start: ");
        int choice = Input.readIntInput(sc, 1, offices.size());

        Vertex start = new Vertex(offices.get(choice - 1));

        return start;
    }

    public int readMailCount(Scanner sc, boolean isFirst) {
        System.out.print("Enter the amount of mails: ");
        int mailCount;

        if(isFirst)
            mailCount = Input.readIntInput(sc, 1, -1);
        else mailCount = Input.readIntInput(sc, 0, -1);

        return mailCount;
    }

    public String getOriginCity(Vertex start) {
        String origin = null;

        for(Route r : mapRoutes) {
            if(origin == null && r.getPlace1().equals(start.name()))
                origin = r.getOrigin();
        }

        return origin;
    }

    public void filterDestinations(Deque<String> unfiltered, List<String> filtered, String origin) {
        List<String> tempDestinations = new ArrayList<>();

        while(!unfiltered.isEmpty()) {
            boolean hasRoute = false;
            String s = unfiltered.poll();

            for(Route r : mapRoutes) {
                if(!hasRoute && r.getOrigin().equals(origin)) {
                    if(s.equals(r.getPlace1()) || s.equals(r.getPlace2()))
                        hasRoute = true;
                }
            }

            if(hasRoute)
                filtered.add(s);
            else tempDestinations.add(s);
        }

        for(String s : tempDestinations)
            unfiltered.add(s);
    }

    public Graph createRouteGraph(List<String> filtered, String origin, Vertex start) {
        Graph graph = new Graph();

        for(Route r : mapRoutes) {
            if(r.getOrigin().equals(origin)) {
                boolean isPlace1Valid = r.getPlace1().equals(start.name());
                boolean isPlace2Valid = r.getPlace2().equals(start.name());

                for(String s : filtered) {
                    if(s.equals(r.getPlace1()))
                        isPlace1Valid = true;

                    if(s.equals(r.getPlace2()))
                        isPlace2Valid = true;
                }

                if(isPlace1Valid && isPlace2Valid) {
                    Vertex place1 = new Vertex(r.getPlace1());
                    Vertex place2 = new Vertex(r.getPlace2());
                    graph.addEdge(place1, place2, r.getDistance());
                }
            }
        }

        return graph;
    }

    public List<String> findNextPostOffices(Deque<String> unfiltered) {
        List<String> nextOffices = new ArrayList<>();

        for(String s : unfiltered) {
            for(Route r : mapRoutes) {
                if(s.equals(r.getPlace2())) {
                    String toOffice = r.getOrigin() + " Post Office";

                    if(r.getPlace1().equals(toOffice)) {
                        if(!nextOffices.contains(r.getPlace1()))
                            nextOffices.add(r.getPlace1());
                    }
                }
            }
        }

        return nextOffices;
    }

    public Graph createPostOfficeGraph(List<String> nextOffices, String origin) {
        Graph graph = new Graph();

        for(Route r : officeRoutes) {
            boolean isPlace1Valid = r.getPlace1().equals(origin + " Post Office") ||
                    nextOffices.contains(r.getPlace1());
            boolean isPlace2Valid = r.getPlace2().equals(origin + " Post Office") ||
                    nextOffices.contains(r.getPlace2());

            if(isPlace1Valid && isPlace2Valid) {
                Vertex place1 = new Vertex(r.getPlace1());
                Vertex place2 = new Vertex(r.getPlace2());
                graph.addEdge(place1, place2, r.getDistance());
            }
        }

        return graph;
    }

    public Vertex updateStartingOffice(Vertex start) {
        if(!solver.getBestPath().isEmpty()) {
            String next = solver.getBestPath().get(0).getDestination().name();
            System.out.println("Next Post Office: " + next);
            start = new Vertex(next);
        }
        else System.out.println("No more post offices to explore...");

        return start;
    }

    public void checkIfOver(Scanner sc) {
        System.out.print("Would you like to simulate again? (1 for Yes, 2 for No): ");
        int choice2 = Input.readIntInput(sc, 1, 2);

        if(choice2 == 2)
            isOver = true;
    }
}
