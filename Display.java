import java.util.Deque;
import java.util.List;

public class Display {

    public static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    public static void showStorage(Deque<String> pendingDestinations, List<String> localDestination) {
        System.out.println("========== MAIL STORAGE ==========");

        if(pendingDestinations.isEmpty() && localDestination.isEmpty()) {
            System.out.println("[ Mail Storage Empty ]");
        }
        else {
            for(String dest : localDestination) {
                System.out.println("+----------------------+");
                System.out.printf("| %-20s |\n", shorten(dest));
            }

            for(String dest : pendingDestinations) {
                System.out.println("+----------------------+");
                System.out.printf("| %-20s |\n", shorten(dest));
            }
            System.out.println("+----------------------+");
        }
        System.out.println();
    }

    public static void showRoute(Edge edge) {
        System.out.println("========== DELIVERY ROUTE ==========");System.out.printf("%s ---> %s (%.1f km)\n",
                shorten(edge.getSource().name()),
                shorten(edge.getDestination().name()),
                edge.getWeight());
        System.out.println();
    }

    public static void showNoLocalMailMessage(String cityName) {
        System.out.println("========== NOTICE ==========");
        System.out.println("No local mails to deliver for " + cityName + "!");
        System.out.println("Proceeding to next post office destination...");
        System.out.println("============================\n");
    }

    public static void showTotalDistance(double distance, String cityName) {
        if (Double.isInfinite(distance) || distance == 0.0) {
            System.out.printf("Total Distance Covered in %s: 0.0 km%n", cityName);
        }
        else {
            System.out.printf("Total Distance Covered in %s: %.1f km%n", cityName, distance);
        }
    }

    public static void animateDelivery(String destination) {

        int length = 20;

        for(int i = 0; i <= length; i++) {
            StringBuilder line = new StringBuilder();

            for(int j = 0; j < i; j++) {
                line.append("-");
            }

            line.append("[]");

            for(int j = i; j < length; j++) {
                line.append("-");
            }

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

        System.out.println("     Delivered");
    }

    private static String shorten(String text) {
        int maxLength = 20;
        if(text.length() <= maxLength)
            return text;

        return text.substring(0, maxLength - 3) + "...";
    }

    public static void introMessage() {
        System.out.println("+---------------------------------------+"); // temporary intro display
        System.out.println("|   Welcome to Arrow Mail Simulator 2   |");
        System.out.println("+---------------------------------------+");
        System.out.println("Please select an option:");
        System.out.println("1. Start Simulation");
        System.out.println("2. Exit");
    }

    public static void exitMessage() {
        System.out.println();
        System.out.println("+----------------------------------------+");
        System.out.println("| Work hard, Mailman! See you next time! |");
        System.out.println("+----------------------------------------+");
    }
}
