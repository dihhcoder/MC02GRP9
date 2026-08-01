import java.util.Deque;
import java.util.List;

public class Display {

    public static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    public static void showStorage(Deque<String> unfiltered, List<String> filtered) {
        System.out.println("========== MAIL STORAGE ==========");

        if(unfiltered.isEmpty() && filtered.isEmpty())
            System.out.println("[ Mail Storage Empty ]");
        else {
            for(String s : filtered) {
                System.out.println("+----------------------+");
                System.out.printf("| %-20s |%n", shorten(s));
            }

            for(String s : unfiltered) {
                System.out.println("+----------------------+");
                System.out.printf("| %-20s |%n", shorten(s));
            }

            System.out.println("+----------------------+");
        }

        System.out.println();
    }

    public static void showRoute(Edge e, boolean isLast) {
        if(!isLast)
            System.out.println("========== DELIVERY ROUTE ==========");
        else System.out.println("========== RETURN ROUTE ==========");

        System.out.printf("%s ---> %s (%.1f km)%n%n", shorten(e.getSource().name()),
                shorten(e.getDestination().name()), e.getWeight());
    }

    public static void showNoLocalMailMessage(String city) {
        System.out.println("========== NOTICE ==========");
        System.out.println("No local mails to deliver for " + city + "!");
        System.out.println("Proceeding to next post office destination...");
        System.out.println("============================");
        System.out.println();
    }

    public static void showTotalDistance(String city, double distance) {
        if (Double.isInfinite(distance) || distance == 0.0)
            System.out.printf("Total Distance Covered in %s: 0.0 km%n%n", city);
        else System.out.printf("Total Distance Covered in %s: %.1f km%n%n", city, distance);
    }

    public static void animateDelivery(String destination, boolean isLast) {
        int length = 20;

        for(int i = 0; i <= length; i++) {
            StringBuilder line = new StringBuilder();

            for(int j = 0; j < i; j++)
                line.append("-");

            line.append("[]");

            for(int j = i; j < length; j++)
                line.append("-");

            line.append("> ");
            line.append(shorten(destination));
            System.out.print("\r" + line);

            try {
                Thread.sleep(150);
            }
            catch(InterruptedException e) {
                e.printStackTrace();
            }
        }

        if(!isLast)
            System.out.println("     Delivered");
        else System.out.println("     Returned");

        System.out.println();
    }

    private static String shorten(String s) {
        int maxLength = 20;

        if (s.length() > maxLength)
            s = s.substring(0, maxLength - 3) + "...";

        return s;
    }

    public static void introMessage() {
        System.out.println("+-------------------------------------------+");
        System.out.println("|     Welcome to Arrow Mail Simulator 2     |");
        System.out.println("+-------------------------------------------+");
        System.out.println("Please select an option:");
        System.out.println("1. Start Simulation");
        System.out.println("2. Exit");
    }

    public static void exitMessage() {
        System.out.println("+--------------------------------------------+");
        System.out.println("|   Work hard, Mailman! See you next time!   |");
        System.out.println("+--------------------------------------------+");
    }
}
