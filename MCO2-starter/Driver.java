import java.util.Scanner;

/**
 * The Driver class contains the main method to run the hotel management system.
 */
public class Driver {

    /**
     * The main method that runs the hotel management system.
     *
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {
        SystemController controller = new SystemController();
        Scanner scanner = new Scanner(System.in);
        boolean exit = false;

        // Main loop to display options and handle user input
        while (!exit) {
            clearScreen();
            controller.displayOptions();
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            // Handle user choice
            switch (choice) {
                case 1:
                    createHotel(controller, scanner);
                    pressEnterToContinue();
                    break;
                case 2:
                    controller.viewHotel(scanner);
                    pressEnterToContinue();
                    break;
                case 3:
                    controller.manageHotel(scanner);
                    pressEnterToContinue();
                    break;
                case 4:
                    controller.simulateBooking(scanner); 
                    pressEnterToContinue();
                    break;
                case 5:
                    exit = true;
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
                    break;
            }
        }

        System.out.println("Exiting...");
        scanner.close();
    }

    /**
     * Prompts the user to enter a hotel name and creates the hotel.
     *
     * @param controller the system controller
     * @param scanner the scanner for user input
     */
    private static void createHotel(SystemController controller, Scanner scanner) {
        System.out.print("Enter hotel name: ");
        String name = scanner.nextLine();
        controller.createHotel(name);
    }

    /**
     * Pauses the execution and waits for the user to press the Enter key to continue.
     */
    private static void pressEnterToContinue() { 
        Scanner scanner = new Scanner(System.in);
        System.out.println("Press Enter key to continue...");
        try {
            System.in.read();
            scanner.nextLine();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Clears the console screen.
     */
    public static void clearScreen() {
        System.out.print("\033[H\033[2J");  
        System.out.flush();  
    }
}

