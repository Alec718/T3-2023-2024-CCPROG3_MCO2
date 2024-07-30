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
        SystemController controller = new SystemController(); // Create an instance of SystemController to manage hotel operations
        Scanner scanner = new Scanner(System.in); // Create a Scanner object for user input
        boolean exit = false; // Flag to control the exit of the main loop

        // Main loop to display options and handle user input
        while (!exit) {
            clearScreen(); // Clear the console screen for a fresh display
            controller.displayOptions(); // Display available options to the user
            int choice = scanner.nextInt(); // Read user choice
            scanner.nextLine(); // Consume the newline character left in the input buffer

            // Handle user choice
            switch (choice) {
                case 1:
                    createHotel(controller, scanner); // Call method to create a hotel
                    pressEnterToContinue(); // Pause for user confirmation
                    break;
                case 2:
                    controller.viewHotel(scanner); // Call method to view hotel details
                    pressEnterToContinue(); // Pause for user confirmation
                    break;
                case 3:
                    controller.manageHotel(scanner); // Call method to manage hotel operations
                    pressEnterToContinue(); // Pause for user confirmation
                    break;
                case 4:
                    controller.simulateBooking(scanner); // Call method to simulate a booking
                    pressEnterToContinue(); // Pause for user confirmation
                    break;
                case 5:
                    exit = true; // Set exit flag to true to terminate the loop
                    break;
                default:
                    System.out.println("Invalid option. Please try again."); // Handle invalid input
                    break;
            }
        }

        System.out.println("Exiting..."); // Message before exiting
        scanner.close(); // Close the Scanner resource
    }

    /**
     * Prompts the user to enter a hotel name and creates the hotel.
     *
     * @param controller the system controller
     * @param scanner the scanner for user input
     */
    private static void createHotel(SystemController controller, Scanner scanner) {
        System.out.print("Enter hotel name: "); // Prompt user for hotel name
        String name = scanner.nextLine(); // Read the hotel name from input
        controller.createHotel(name); // Call the controller to create the hotel
    }

    /**
     * Pauses the execution and waits for the user to press the Enter key to continue.
     * https://stackoverflow.com/questions/19870467/how-do-i-get-press-any-key-to-continue-to-work-in-my-java-code.
     */
    private static void pressEnterToContinue() { 
        Scanner scanner = new Scanner(System.in); // Create a new Scanner for waiting for Enter key
        System.out.println("Press Enter key to continue..."); // Prompt user to continue
        try {
            System.in.read(); // Wait for input
            scanner.nextLine(); // Consume the newline character
        } catch (Exception e) {
            e.printStackTrace(); // Print stack trace in case of an exception
        }
    }

    /**
     * Clears the console screen.
     * Reference: https://stackoverflow.com/questions/2979383/how-to-clear-the-console-using-java.
     */
    public static void clearScreen() {
        System.out.print("\033[H\033[2J");  // ANSI escape codes to clear the screen
        System.out.flush();  // Flush the output stream
    }
}
