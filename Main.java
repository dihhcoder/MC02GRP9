import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        boolean isRunning = true;
        String prompt = "Enter your choice: ";

        while(isRunning) {
            Display.introMessage();
            int choice = Input.readIntInput(sc, 1, 2, prompt);

            switch(choice) {
                case 1:
                    Simulator s = new Simulator();
                    s.runSimulation(sc);
                    break;
                case 2:
                    isRunning = false;
                    Display.exitMessage();
                    break;
            }
        }

        sc.close();
    }
}
