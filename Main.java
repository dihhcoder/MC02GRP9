import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner keyboard = new Scanner(System.in);
        boolean isRunning = true;

        while(isRunning) {
            Display.introMessage();
            String prompt = "Enter your choice: ";
            int choice = Input.readIntInput(keyboard, prompt, 1, 2);

            switch(choice) {
                case 1:
                    Simulator simulator = new Simulator();
                    simulator.runSimulation(keyboard);
                    break;
                case 2:
                    isRunning = false;
                    System.out.println();
                    Display.exitMessage();
                    break;
            }
        }

        keyboard.close();
    }
}
