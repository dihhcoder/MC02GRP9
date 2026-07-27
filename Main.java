import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        boolean isRunning = true;

        while(isRunning) {
            System.out.println("+---------------------------------------+"); // temporary intro display
            System.out.println("|   Welcome to Arrow Mail Simulator 2   |");
            System.out.println("+---------------------------------------+");
            System.out.println("Please select an option:");
            System.out.println("1. Start Simulation");
            System.out.println("2. Exit");
            System.out.print("Enter your choice: ");
            int choice = Input.readIntInput(sc, 1, 2);

            switch(choice) {
                case 1:
                    Simulator s = new Simulator();
                    s.runSimulation(sc);
                    break;
                case 2:
                    isRunning = false;
                    System.out.println();
                    System.out.println("+----------------------------------------+");
                    System.out.println("| Work hard, Mailman! See you next time! |");
                    System.out.println("+----------------------------------------+"); // temporary exit message
                    break;
            }
        }

        sc.close();
    }
}
