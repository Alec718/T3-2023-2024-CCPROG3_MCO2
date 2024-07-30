import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * The SystemController class is responsible for managing hotels, rooms, and reservations.
 */
public class SystemController {
    private List<Hotel> hotels;

    /**
     * Constructs a new SystemController with an empty list of hotels.
     */
    public SystemController() {
        this.hotels = new ArrayList<>();
    }

    /**
     * Creates a new hotel with the specified name if the name is unique.
     *
     * @param name the name of the new hotel
     * @return the newly created Hotel object, or null if the name is not unique
     */
    public Hotel createHotel(String name) {
        // Check if the hotel name is already in use
        for (Hotel existingHotel : hotels) {
            if (existingHotel.getName().equalsIgnoreCase(name)) {
                System.out.println("Hotel with name '" + name + "' already exists. Please choose a different name.");
                return null; // Return null if the hotel name is not unique
            }
        }

        // Create a new hotel and add it to the list
        Hotel newHotel = new Hotel(name);
        hotels.add(newHotel);
        System.out.println("Hotel created successfully.");
        return newHotel; // Return the newly created hotel
    }

    /**
     * Removes the specified hotel from the list of hotels.
     *
     * @param hotel the hotel to remove
     */
    public void removeHotel(Hotel hotel) {
        if (hotel != null && hotels.contains(hotel)) {
            hotels.remove(hotel);
            System.out.println("Hotel '" + hotel.getName() + "' has been removed.");
            pressEnterToContinue();
        } else {
            System.out.println("Hotel not found.");
            pressEnterToContinue();
        }
    }

    /**
     * Gets high-level information about the specified hotel.
     *
     * @param hotel the hotel to get information about
     * @return a string containing high-level information about the hotel
     */
    public String getHotelInfo(Hotel hotel) {
        int totalRooms = hotel.getTotalRooms();
        double estimatedEarnings = hotel.getMonthlyEarnings();

        return "Hotel name: " + hotel.getName() + "\n" +
               "Total rooms: " + totalRooms + "\n" +
               "Estimated earnings for the month: PHP " + estimatedEarnings;
    }

    /**
     * Gets information about the specified room.
     *
     * @param room the room to get information about
     * @return a string containing information about the room
     */
    public String getRoomInfo(Room room) {
        return room.getRoomInfo();
    }

    /**
     * Gets information about the specified reservation.
     *
     * @param reservation the reservation to get information about
     * @return a string containing information about the reservation
     */
    public String getReservationInfo(Reservation reservation) {
        return reservation.getDetails();
    }

    /**
     * Allows the user to view detailed information about a selected hotel.
     *
     * @param scanner the Scanner object for user input
     */
    public void viewHotel(Scanner scanner) {
        Hotel hotel = selectHotel(scanner);
        if (hotel != null) {    
            // Prompt user for more detailed information
            System.out.println("\nChoose the information level:");
            System.out.println("1. High-level information");
            System.out.println("2. Low-level information");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline
    
            switch (choice) {
                case 1:
                    System.out.println("\nHigh-level Information:");
                    System.out.println(hotel.getHighLevelInfo()); // Assuming getHighLevelInfo() returns formatted string
                    break;
                case 2:
                    viewLowLevelInfo(hotel, scanner);
                    break;
                default:
                    System.out.println("Invalid option.");
                    break;
            }
        }
    }

    /**
     * Allows the user to view low-level information about a selected hotel.
     *
     * @param hotel the hotel to view information about
     * @param scanner the Scanner object for user input
     */
    private void viewLowLevelInfo(Hotel hotel, Scanner scanner) {
        System.out.println("\nSelect low-level information:");
        System.out.println("1. Total number of available and booked rooms for a selected date");
        System.out.println("2. Information about a selected room");
        System.out.println("3. Information about a selected reservation");
        System.out.print("Enter your choice: ");
        int choice = scanner.nextInt();
        scanner.nextLine(); // Consume newline
    
        switch (choice) {
            case 1:
                viewRoomsAvailability(hotel, scanner);
                pressEnterToContinue();
                break;
            case 2:
                viewRoomDetails(hotel, scanner);
                pressEnterToContinue();
                break;
            case 3:
                viewReservationDetails(hotel, scanner);
                pressEnterToContinue();
                break;
            default:
                System.out.println("Invalid option.");
                break;
        }
    }

    /**
     * Allows the user to view the availability of rooms in a selected hotel on a specified date.
     *
     * @param hotel the hotel to check room availability for
     * @param scanner the Scanner object for user input
     */
    private void viewRoomsAvailability(Hotel hotel, Scanner scanner) {
        System.out.print("Enter date to check (yyyy-MM-dd): ");
        String dateStr = scanner.nextLine();
        Date selectedDate = parseDate(dateStr);
    
        int totalAvailableRooms = hotel.getTotalAvailableRooms(selectedDate);
        int totalBookedRooms = hotel.getTotalBookedRooms(selectedDate);
    
        List<String> availableRoomNames = hotel.getAvailableRoomNames(selectedDate);
    
        System.out.println("Rooms Availability on " + dateStr + ":");
        System.out.println("Total rooms: " + hotel.getTotalRooms());
        System.out.println("Available rooms: " + totalAvailableRooms);
        System.out.println("Booked rooms: " + totalBookedRooms);
    
        System.out.println("Available rooms names:");
        for (String roomName : availableRoomNames) {
            System.out.println(roomName);
        }
    }

    /**
     * Parses a date from a string in the format "yyyy-MM-dd".
     *
     * @param dateStr the string to parse
     * @return the parsed Date object, or null if parsing fails
     */
    private Date parseDate(String dateStr) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            return sdf.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Allows the user to view detailed information about a selected room in a hotel.
     *
     * @param hotel the hotel containing the room
     * @param scanner the Scanner object for user input
     */
    private void viewRoomDetails(Hotel hotel, Scanner scanner) {
        System.out.print("Enter room name: ");
        String roomName = scanner.nextLine();
        Room room = hotel.getRoomInfo(roomName);
        if (room != null) {
            System.out.println("\nRoom Information:");
            System.out.println("Room Name: " + room.getName(roomName));
            System.out.println("Base Price: PHP " + room.getBasePrice());

            // Prompt user to enter the year and month for availability
            System.out.print("Checking Room Availability... Choose a year (e.g., 2024): ");
            int year = scanner.nextInt();
            System.out.print("Checking Days in Month Availability... Choose a month (1-12): ");
            int month = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            // Get availability for the month
            List<String> availability = room.getAvailabilityForMonth(year, month);
            System.out.println("Room Availability for " + month + "/" + year + ":");
            for (String dayAvailability : availability) {
                System.out.println(dayAvailability);
            }
        } else {
            System.out.println("Room not found.");
        }
    }

    /**
     * Allows the user to view detailed information about a selected reservation in a hotel.
     *
     * @param hotel the hotel containing the reservation
     * @param scanner the Scanner object for user input
     */
    private void viewReservationDetails(Hotel hotel, Scanner scanner) {
        System.out.print("Enter guest name to find reservation: ");
        String guestName = scanner.nextLine();
        Reservation reservation = findReservationByGuestName(hotel, guestName);
        if (reservation != null) {
            System.out.println("\nReservation Details:");
            System.out.println(reservation.getDetails());
        } else {
            System.out.println("Reservation not found.");
        }
    }

    /**
     * Allows the user to manage a selected hotel.
     *
     * @param scanner the Scanner object for user input
     */
    public void manageHotel(Scanner scanner) {
        testMaxRoomLimit(); // for testing of maxRoomLimit. Remove // if using and put // if not in use.
        
        // Select a hotel using user input
        Hotel hotel = selectHotel(scanner);
        
        if (hotel != null) {
            boolean exit = false;
            
            // Continue managing the hotel until the user chooses to exit
            while (!exit) {
                clearScreen(); // Clear the console screen
                displayHotelManagementOptions(); // Display management options
                
                int choice = scanner.nextInt(); // Read user choice
                scanner.nextLine(); // Consume newline character
                
                switch (choice) {
                    case 1:
                        // Change hotel name
                        System.out.print("Enter new hotel name: ");
                        String newName = scanner.nextLine();
                        changeHotelName(hotel, newName);
                        pressEnterToContinue();
                        break;
                    case 2:
                        // Update hotel base price
                        System.out.print("Enter new base price: ");
                        double newPrice = scanner.nextDouble();
                        updateHotelBasePrice(hotel, newPrice);
                        pressEnterToContinue();
                        break;
                    case 3:
                        // Add a room to the hotel
                        System.out.print("Enter room name: ");
                        String roomName = scanner.nextLine();
                        double roomBasePrice = hotel.getBasePrice();
                        addRoomToHotel(hotel, roomName, roomBasePrice);
                        break;
                    case 4:
                        // Remove a room from the hotel
                        System.out.print("Enter room name to remove: ");
                        String roomNameToRemove = scanner.nextLine();
                        Room roomToRemove = hotel.getRoomInfo(roomNameToRemove);
                        if (roomToRemove != null) {
                            removeRoomFromHotel(hotel, roomToRemove);
                        } else {
                            System.out.println("Room not found.");
                            pressEnterToContinue();
                        }
                        break;
                    case 5:
                        // Remove reservation by guest name
                        System.out.print("Enter guest name to remove reservation: ");
                        String guestNameToRemove = scanner.nextLine();
                        Reservation reservationToRemove = findReservationByGuestName(hotel, guestNameToRemove);
                        if (reservationToRemove != null) {
                            Room roomOfReservationToRemove = reservationToRemove.getRoom();
                            removeReservationFromRoom(hotel, roomOfReservationToRemove, reservationToRemove);
                            pressEnterToContinue();
                        } else {
                            System.out.println("Reservation not found.");
                            pressEnterToContinue();
                        }
                        break;
                    case 6:
                        // Remove the entire hotel
                        removeHotel(hotel);
                        exit = true;
                        break;
                    case 7:
                        // Exit from hotel management
                        exit = true;
                        break;
                    default:
                        System.out.println("Invalid option. Please choose again.");
                        break;
                }
            }
        }
    }


    /**
     * Displays the menu of hotel management options.
     * These options include changing hotel name, updating base price,
     * adding/removing rooms, removing reservations from rooms, removing the hotel, 
    * and going back to the main menu.
    */
    private void displayHotelManagementOptions() {
        System.out.println("\nHotel Management Options:");
        System.out.println("1. Change Hotel Name"); // Option to change the name of the selected hotel
        System.out.println("2. Update Hotel Base Price"); // Option to update the base price of the selected hotel
        System.out.println("3. Add Room"); // Option to add a room to the selected hotel
        System.out.println("4. Remove Room"); // Option to remove a room from the selected hotel
        System.out.println("5. Remove Reservation from Room"); // Option to remove a reservation from a room in the selected hotel
        System.out.println("6. Remove Hotel"); // Option to completely remove the selected hotel
        System.out.println("7. Back to Main Menu"); // Option to go back to the main menu
        System.out.print("Enter your choice: "); // Prompting the user to enter their choice
        }


    /**
     * Finds a reservation in the given hotel based on the guest's name.
     *
     * @param hotel     the hotel in which to search for the reservation
     * @param guestName the name of the guest whose reservation is to be found
     * @return the Reservation object if found, or null if no reservation matches the guest name
     */
    public Reservation findReservationByGuestName(Hotel hotel, String guestName) {
    // Iterate through each room in the hotel
        for (Room room : hotel.getRooms()) {
            // Iterate through each reservation in the current room
            for (Reservation reservation : room.getReservations()) {
                // Check if the guest name matches ignoring case
                if (reservation.getGuestName().equalsIgnoreCase(guestName)) {
                    return reservation; // Return the reservation if found
                }
            }
        }
        return null; // Return null if no reservation found for the guest name
    }


    /**
     * Allows the user to select a hotel from the list of available hotels.
     *
     * @param scanner the Scanner object for user input
     * @return the selected Hotel object, or null if no hotels are available or an invalid selection is made
     */
    public Hotel selectHotel(Scanner scanner) {
        // Check if there are any hotels available
        if (hotels.isEmpty()) {
            System.out.println("No hotels available.");
            return null; // Return null if there are no hotels
        }

        // Display a list of available hotels for selection
        System.out.println("Select a hotel:");
        for (int i = 0; i < hotels.size(); i++) {
            System.out.println((i + 1) + ". " + hotels.get(i).getName()); // Display hotel index and name
        }
        System.out.print("Enter hotel number: ");
        int hotelNumber = scanner.nextInt(); // Read user input for hotel number
        scanner.nextLine(); // Consume newline after reading integer input

        // Validate the user's input
        if (hotelNumber > 0 && hotelNumber <= hotels.size()) {
            return hotels.get(hotelNumber - 1); // Return the selected hotel object
        } else {
            System.out.println("Invalid hotel number."); // Inform user of invalid input
            return null; // Return null for an invalid hotel selection
        }
    }


    /**
     * Changes the name of the specified hotel, ensuring uniqueness among existing hotels.
     *
     * @param hotel   the hotel whose name is to be changed
     * @param newName the new name to be assigned to the hotel
     */
    public void changeHotelName(Hotel hotel, String newName) {
        // Check if the new name is already in use by another hotel
        for (Hotel existingHotel : hotels) {
            if (existingHotel != hotel && existingHotel.getName().equalsIgnoreCase(newName)) {
                System.out.println("Hotel with name '" + newName + "' already exists. Please choose a different name.");
                return; // Exit the method if the new name is not unique
            }
        }

        // Change the hotel name since it's unique
        hotel.changeName(newName); // Call the method in Hotel class to change the name
        System.out.println("Hotel name changed to '" + newName + "'."); // Inform user of successful name change
    }


    /**
     * Updates the base price of the specified hotel, subject to certain conditions.
     *
     * @param hotel    the hotel whose base price is to be updated
     * @param newPrice the new base price to be assigned to the hotel
     */
    public void updateHotelBasePrice(Hotel hotel, double newPrice) {
        // Check if there are any active reservations in the hotel
        if (hotelHasActiveReservations(hotel)) {
            System.out.println("Cannot update base price. There are active reservations in the hotel.");
            pressEnterToContinue(); // Prompt user to continue after displaying message
        } else if (newPrice < 100.0) {
            System.out.println("New base price must be >= 100.0.");
            pressEnterToContinue(); // Prompt user to continue after displaying message
        } else {
            // Update the base price since conditions are met
            hotel.updateBasePrice(newPrice); // Call the method in Hotel class to update base price
            System.out.println("Base price updated for hotel '" + hotel.getName() + "'."); // Inform user of successful update
            pressEnterToContinue(); // Prompt user to continue after displaying message
        }
    }


    /**
     * Checks if the specified hotel has any active reservations.
     *
     * @param hotel the hotel to check for active reservations
     * @return true if there are active reservations, false otherwise
     */
    private boolean hotelHasActiveReservations(Hotel hotel) {
        // Iterate through each room in the hotel
        for (Room room : hotel.getRooms()) {
            // Iterate through each reservation in the current room
            for (Reservation reservation : room.getReservations()) {
                Date today = new Date(); // Get the current date
                // Check if the reservation's check-out date is after today's date
                if (reservation.getCheckOutDate().after(today)) {
                    return true; // Found an active reservation
                }
            }
        }
        return false; // No active reservations found
    }


   /**
     * Adds a room to the specified hotel, subject to certain conditions.
     *
     * @param hotel     the hotel to which the room is to be added
     * @param roomName  the name of the room to be added
     * @param basePrice the base price of the room to be added
     */
    public void addRoomToHotel(Hotel hotel, String roomName, double basePrice) {
        // Check if the hotel already has the maximum number of rooms (50)
        if (hotel.getRooms().size() >= 50) {
            System.out.println("Cannot add room. Hotel '" + hotel.getName() + "' already has the maximum number of rooms (50).");
            pressEnterToContinue(); // Prompt user to continue after displaying message
            return; // Exit method if hotel has reached maximum rooms
        }

        // Validate room name uniqueness within the hotel
        for (Room room : hotel.getRooms()) {
            if (room.getName(roomName).equalsIgnoreCase(roomName)) {
                System.out.println("Room with name '" + roomName + "' already exists in hotel '" + hotel.getName() + "'.");
                pressEnterToContinue(); // Prompt user to continue after displaying message
                return; // Exit if room name is not unique
            }
        }

        // Add room to the hotel
        hotel.addRoom(roomName); // Call the method in Hotel class to add room
        System.out.println("Room '" + roomName + "' added to hotel '" + hotel.getName() + "'."); // Inform user of successful addition
        pressEnterToContinue(); // Prompt user to continue after displaying message
    }


    /**
     * Removes a room from the specified hotel, subject to certain conditions.
     *
     * @param hotel the hotel from which the room is to be removed
     * @param room  the room to be removed from the hotel
     */
    public void removeRoomFromHotel(Hotel hotel, Room room) {
        // Check if the room has any active reservations
        if (roomHasActiveReservations(room)) {
            System.out.println("Cannot remove room '" + room.getName() + "' because it has active reservations.");
            pressEnterToContinue(); // Prompt user to continue after displaying message
        } else {
            // Remove the room from the hotel
            hotel.removeRoom(room); // Call the method in Hotel class to remove room
            System.out.println("Room '" + room.getName() + "' removed from hotel '" + hotel.getName() + "'."); // Inform user of successful removal
            pressEnterToContinue(); // Prompt user to continue after displaying message
        }
    }


    /**
     * Checks if the specified room has any active reservations.
     *
     * @param room the room to check for active reservations
     * @return true if there are active reservations, false otherwise
     */
    private boolean roomHasActiveReservations(Room room) {
        Date today = new Date(); // Get the current date

        // Iterate through each reservation in the room
        for (Reservation reservation : room.getReservations()) {
            // Check if the reservation's check-out date is after today's date
            if (reservation.getCheckOutDate().after(today)) {
                return true; // Found an active reservation
            }
        }
        return false; // No active reservations found
    }

    /**
     * Removes a reservation from a specific room in the hotel.
     *
     * @param hotel the hotel from which the reservation is to be removed
     * @param room the room from which the reservation is to be removed
     * @param reservation the reservation to be removed
     */
    public void removeReservationFromRoom(Hotel hotel, Room room, Reservation reservation) {
        // Check if both room and reservation are not null
        if (room != null && reservation != null) {
            room.removeReservation(reservation); // Call the method in Room class to remove reservation from the room
            System.out.println("Reservation for " + reservation.getGuestName() + " removed from room " + room.getName());
        } else {
            System.out.println("Failed to remove reservation."); // Inform user if failed to remove reservation
        }
    }


    /**
     * Simulates a booking process for a guest in the selected hotel.
     *
     * @param scanner the Scanner object for user input
     */
    public void simulateBooking(Scanner scanner) {
        Hotel hotel = selectHotel(scanner); // Select a hotel using user input
        if (hotel != null) {
            System.out.print("Enter guest name: ");
            String guestName = scanner.nextLine();
            System.out.print("Enter check-in date (yyyy-mm-dd): ");
            String checkInStr = scanner.nextLine();
            System.out.print("Enter check-out date (yyyy-mm-dd): ");
            String checkOutStr = scanner.nextLine();

            Date checkInDate = parseDate(checkInStr); // Convert check-in date string to Date object
            Date checkOutDate = parseDate(checkOutStr); // Convert check-out date string to Date object

            // Display available room names for user to choose from
            List<String> availableRoomNames = hotel.getAvailableRoomNames(checkInDate, checkOutDate);
            if (availableRoomNames.isEmpty()) {
                System.out.println("No rooms available for the selected dates.");
                return; // Exit method if no rooms are available
            }

            System.out.println("Available rooms:");
            for (int i = 0; i < availableRoomNames.size(); i++) {
                System.out.println((i + 1) + ". " + availableRoomNames.get(i));
            }

            System.out.print("Choose a room number: ");
            int roomNumber = scanner.nextInt();
            scanner.nextLine(); // Consume newline after reading integer input

            if (roomNumber > 0 && roomNumber <= availableRoomNames.size()) {
                String selectedRoomName = availableRoomNames.get(roomNumber - 1);
                Room selectedRoom = hotel.getRoomInfo(selectedRoomName);
                if (selectedRoom != null) {
                    // Simulate booking in the hotel
                    Reservation reservation = hotel.simulateBooking(guestName, checkInDate, checkOutDate, selectedRoom);
                    if (reservation != null) {
                        selectedRoom.addReservation(reservation); // Add reservation to the selected room
                        System.out.println("Booking simulated successfully for " + guestName);
                    } else {
                        System.out.println("Failed to simulate booking.");
                    }
                } else {
                    System.out.println("Selected room not found in the hotel.");
                }
            } else {
                System.out.println("Invalid room number.");
            }
        }
    }


    /**
     * Displays the main menu options for the hotel management system.
     */
    public void displayOptions() {
        System.out.println("\nSelect an option:");
        System.out.println("1. Create Hotel");   // Option to create a new hotel
        System.out.println("2. View Hotel");      // Option to view details of existing hotels
        System.out.println("3. Manage Hotel");    // Option to manage an existing hotel
        System.out.println("4. Simulate Booking"); // Option to simulate a booking in a hotel
        System.out.println("5. Exit");            // Option to exit the program
        System.out.print("Enter your choice: ");
    }


    /**
     * Retrieves the list of hotels managed by the system.
     *
     * @return the list of hotels
     */
    public List<Hotel> getHotels() {
        return hotels; // Return the list of hotels
    }


    /**
     * Clears the console screen.
     * This method uses ANSI escape codes to clear the screen.
     * Reference: https://stackoverflow.com/questions/2979383/how-to-clear-the-console-using-java.
     */
    private void clearScreen() {
        System.out.print("\033[H\033[2J"); // ANSI escape code to clear screen
        System.out.flush(); // Flush the output stream
    }



    /**
     * Waits for the user to press Enter to continue.
     * This method prompts the user to press the Enter key and waits for the input.
     * Reference: https://stackoverflow.com/questions/19870467/how-do-i-get-press-any-key-to-continue-to-work-in-my-java-code.
     */
    private void pressEnterToContinue() {
        Scanner scanner = new Scanner(System.in); // Create a new Scanner object to read input
        System.out.println("Press Enter key to continue..."); // Prompt message for user
        try {
            System.in.read(); // Wait for user to press Enter
            scanner.nextLine(); // Consume newline character after Enter key press
        } catch (Exception e) {
            e.printStackTrace(); // Print stack trace if an exception occurs
        }
    }


    /**
     * Tests the maximum room limit functionality of a hotel.
     * Creates a test hotel and attempts to add 50 rooms to it,
     * then tries to add an extra room which should fail due to the room limit.
     */
    public void testMaxRoomLimit() {
        // Create a new hotel for testing
        Hotel testHotel = createHotel("h1");

        if (testHotel != null) {
            // Add 50 rooms to the hotel
            for (int i = 1; i <= 50; i++) {
                String roomName = "Room" + i;
                double basePrice = 1299.0; // Default base price

                // Add room to the hotel
                addRoomToHotel(testHotel, roomName, basePrice);
            }

            // Attempt to add one more room (should fail)
            String extraRoomName = "Extra Room";
            double extraRoomPrice = 1299.0; // Default base price

            // Attempt to add the extra room
            addRoomToHotel(testHotel, extraRoomName, extraRoomPrice);
        }
    }
}
