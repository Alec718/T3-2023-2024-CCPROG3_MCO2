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

    /*public SystemController(Hotel hotel, Scanner scanner){
        this.hotel = hotel;
        this.scanner = scanner;
    }*/

    /**
     * Creates a new hotel with the specified name if the name is unique.
     *
     * @param name the name of the new hotel
     * @return the newly created Hotel object, or null if the name is not unique
     */
    public Hotel createHotel(String name) {
        // Check if the hotel name is already in use
        for (Hotel existingHotel : hotels) {
            // Compare existing hotel names (case insensitive) with the new name
            if (existingHotel.getName().equalsIgnoreCase(name)) {
                // Print a message if the hotel name already exists
                System.out.println("Hotel with name '" + name + "' already exists. Please choose a different name.");
                return null; // Return null if the hotel name is not unique
            }
        }

        // Create a new hotel and add it to the list
        Hotel newHotel = new Hotel(name); // Adding an instantiation of a hotel with the chosen name
        hotels.add(newHotel); // Adding hotel to the list of hotels
        System.out.println("Hotel created successfully."); //Hotel created successfully
        return newHotel; // Return the newly created hotel
    }

    /**
     * Removes the specified hotel from the list of hotels.
     *
     * @param hotel the hotel to remove
     */
    public void removeHotel(Hotel hotel) {
        // Check if the hotel is not null and exists in the list of hotels
        if (hotel != null && hotels.contains(hotel)) {
            // Remove the hotel from the list
            hotels.remove(hotel);
            // Print a confirmation message indicating the hotel has been removed
            System.out.println("Hotel '" + hotel.getName() + "' has been removed.");
        } else {
            // Print a message indicating that the hotel was not found
            System.out.println("Hotel not found.");
        }
    }

    /**
     * Gets high-level information about the specified hotel.
     *
     * @param hotel the hotel to get information about
     * @return a string containing high-level information about the hotel
     */
    public String getHotelInfo(Hotel hotel) {
        // Retrieve the total number of rooms in the hotel
        int totalRooms = hotel.getTotalRooms();
        // Retrieve the estimated monthly earnings of the hotel
        double estimatedEarnings = hotel.getMonthlyEarnings();

        // Construct and return a string with the hotel information
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
        return room.getRoomInfo(); // Returns the Room Info
    }

    /**
     * Gets information about the specified reservation.
     *
     * @param reservation the reservation to get information about
     * @return a string containing information about the reservation
     */
    public String getReservationInfo(Reservation reservation) {
        return reservation.getDetails(); // Returns the complete reservation details
    }

    /**
     * Allows the user to view detailed information about a selected hotel.
     *
     * @param scanner the Scanner object for user input
     */
    public void viewHotel(Scanner scanner) {
        // Prompt the user to select a hotel
        Hotel hotel = selectHotel(scanner); // Assume selectHotel() handles hotel selection
        if (hotel != null) {    
            // Prompt user for more detailed information
            System.out.println("\nChoose the information level:");
            System.out.println("1. High-level information"); // Option for high-level info
            System.out.println("2. Low-level information"); // Option for low-level info
            System.out.print("Enter your choice: "); // Prompt for user input
            int choice = scanner.nextInt(); // Read user's choice
            scanner.nextLine(); // Consume newline character from input buffer

            // Process user's choice
            switch (choice) {
                case 1:
                    // Display high-level information about the selected hotel
                    System.out.println("\nHigh-level Information:");
                    System.out.println(hotel.getHighLevelInfo()); // Assuming getHighLevelInfo() returns formatted string
                    break;
                case 2:
                    // Call method to display low-level information
                    viewLowLevelInfo(hotel, scanner); // Assuming viewLowLevelInfo() handles low-level info display
                    break;
                default:
                    // Handle invalid option
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
        // Display options for low-level information
        System.out.println("\nSelect low-level information:");
        System.out.println("1. Total number of available and booked rooms for a selected date"); // Option for room availability
        System.out.println("2. Information about a selected room"); // Option for room details
        System.out.println("3. Information about a selected reservation"); // Option for reservation details
        System.out.print("Enter your choice: "); // Prompt for user input

        int choice = scanner.nextInt(); // Read user's choice
        scanner.nextLine(); // Consume newline character from input buffer

        // Process user's choice for low-level information
        switch (choice) {
            case 1:
                // Call method to view availability of rooms
                viewRoomsAvailability(hotel, scanner); // Assuming viewRoomsAvailability() handles room availability display
                break;
            case 2:
                // Call method to view details of a selected room
                viewRoomDetails(hotel, scanner); // Assuming viewRoomDetails() handles room detail display
                break;
            case 3:
                // Call method to view details of a selected reservation
                viewReservationDetails(hotel, scanner); // Assuming viewReservationDetails() handles reservation detail display
                break;
            default:
                // Handle invalid option
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
        // Prompt user for the date to check room availability
        System.out.print("Enter date to check (yyyy-MM-dd): ");
        String dateStr = scanner.nextLine(); // Read the date as a string

        // Parse the input date string into a Date object
        Date selectedDate = parseDate(dateStr); // Assuming parseDate() is defined elsewhere

        // Get the total number of available and booked rooms for the selected date
        int totalAvailableRooms = hotel.getTotalAvailableRooms(selectedDate); // Fetch available rooms
        int totalBookedRooms = hotel.getTotalBookedRooms(selectedDate); // Fetch booked rooms

        // Get the names of available rooms for the selected date
        List<String> availableRoomNames = hotel.getAvailableRoomNames(selectedDate); // Fetch names of available rooms

        // Display the room availability information
        System.out.println("Rooms Availability on " + dateStr + ":");
        System.out.println("Total rooms: " + hotel.getTotalRooms()); // Display total number of rooms in the hotel
        System.out.println("Available rooms: " + totalAvailableRooms); // Display total available rooms
        System.out.println("Booked rooms: " + totalBookedRooms); // Display total booked rooms

        // Display the names of available rooms
        System.out.println("Available rooms names:");
        for (String roomName : availableRoomNames) {
            System.out.println(roomName); // Print each available room name
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
            // Create a SimpleDateFormat object to define the date format
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            
            // Parse the date string into a Date object and return it
            return sdf.parse(dateStr);
        } catch (ParseException e) {
            // Print the stack trace if parsing fails, indicating an error
            e.printStackTrace();
            
            // Return null to indicate that the parsing was unsuccessful
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
        // Prompt the user to enter the room name to view details
        System.out.print("Enter room name: ");
        String roomName = scanner.nextLine();
        
        // Retrieve the Room object using the provided room name
        Room room = hotel.getRoomInfo(roomName);
        
        // Check if the room exists
        if (room != null) {
            // Display room information
            System.out.println("\nRoom Information:");
            System.out.println("Room Name: " + room.getName(roomName)); // Get and print the room name
            System.out.println("Room Type: " + room.getRoomType()); // Get and print the room type
            System.out.println("Base Price: PHP " + room.getBasePrice()); // Get and print the base price
            
            // Prompt user to enter the year for checking room availability
            System.out.print("Checking Room Availability... Choose a year (e.g., 2024): ");
            int year = scanner.nextInt();
            
            // Prompt user to enter the month for checking room availability
            System.out.print("Checking Days in Month Availability... Choose a month (1-12): ");
            int month = scanner.nextInt();
            scanner.nextLine(); // Consume newline
            
            // Get availability for the specified month
            List<String> availability = room.getAvailabilityForMonth(year, month);
            
            // Display room availability for the specified month
            System.out.println("Room Availability for " + month + "/" + year + ":");
            for (String dayAvailability : availability) {
                System.out.println(dayAvailability); // Print each day's availability
            }
        } else {
            // If the room is not found, inform the user
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
        // Prompt the user to enter the guest name to find the reservation
        System.out.print("Enter guest name to find reservation: ");
        String guestName = scanner.nextLine();
        
        // Search for the reservation by guest name
        Reservation reservation = findReservationByGuestName(hotel, guestName);
        
        // Check if the reservation was found
        if (reservation != null) {
            // If found, display the reservation details
            System.out.println("\nReservation Details:");
            System.out.println(reservation.getDetails()); // Print the details of the reservation
        } else {
            // If not found, inform the user
            System.out.println("Reservation not found.");
        }
    }


    /**
     * Allows the user to manage a selected hotel.
     *
     * @param scanner the Scanner object for user input
     */
    public void manageHotel(Scanner scanner) {
        // Uncomment the line below for testing the maximum room limit feature
        // testMaxRoomLimit(); 

        // Select a hotel using user input
        Hotel hotel = selectHotel(scanner);
        
        // Check if a hotel was successfully selected
        if (hotel != null) {
            boolean exit = false; // Flag to control the exit from management loop
            
            // Continue managing the hotel until the user chooses to exit
            while (!exit) {
                clearScreen(); // Clear the console screen for a fresh view
                displayHotelManagementOptions(); // Display management options to the user
                
                int choice = scanner.nextInt(); // Read user choice
                scanner.nextLine(); // Consume newline character
                
                switch (choice) {
                    case 1:
                        // Change hotel name
                        System.out.print("Enter new hotel name: ");
                        String newName = scanner.nextLine(); // Get the new hotel name from user
                        changeHotelName(hotel, newName); // Update the hotel name
                        pressEnterToContinue(); // Wait for user to press Enter
                        break;
                    case 2:
                        // Update hotel base price
                        System.out.print("Enter new base price: ");
                        double newPrice = scanner.nextDouble(); // Get the new base price
                        updateHotelBasePrice(hotel, newPrice); // Update the hotel's base price
                        pressEnterToContinue(); // Wait for user to press Enter
                        break;
                    case 3:
                        // Add a room to the hotel
                        System.out.print("Enter room name: ");
                        String roomName = scanner.nextLine(); // Get the new room name
                        double roomBasePrice = hotel.getBasePrice(); // Get the current base price of the hotel
                        System.out.println("Select room type:");
                        System.out.println("1. Standard");
                        System.out.println("2. Deluxe");
                        System.out.println("3. Executive");
                        System.out.print("Enter your choice: ");
                        int roomTypeChoice = scanner.nextInt(); // Get user's choice for room type
                        scanner.nextLine(); // Consume newline
                        
                        String roomType; // Variable to hold the selected room type
                        switch (roomTypeChoice) {
                            case 1:
                                roomType = "Standard"; // Set room type to Standard
                                break;
                            case 2:
                                roomType = "Deluxe"; // Set room type to Deluxe
                                break;
                            case 3:
                                roomType = "Executive"; // Set room type to Executive
                                break;
                            default:
                                System.out.println("Invalid choice."); // Handle invalid choice
                                return; // Exit if invalid choice is provided
                        }
                    
                        addRoomToHotel(hotel, roomName, roomBasePrice, roomType); // Add the new room to the hotel
                        break;
                    case 4:
                        // Remove a room from the hotel
                        System.out.print("Enter room name to remove: ");
                        String roomNameToRemove = scanner.nextLine(); // Get the room name to remove
                        Room roomToRemove = hotel.getRoomInfo(roomNameToRemove); // Find the room in the hotel
                        if (roomToRemove != null) {
                            removeRoomFromHotel(hotel, roomToRemove); // Remove the room if found
                        } else {
                            System.out.println("Room not found."); // Inform if the room is not found
                            pressEnterToContinue(); // Wait for user to press Enter
                        }
                        break;
                    case 5:
                        // Remove reservation by guest name
                        System.out.print("Enter guest name to remove reservation: ");
                        String guestNameToRemove = scanner.nextLine(); // Get the guest name to remove reservation
                        Reservation reservationToRemove = findReservationByGuestName(hotel, guestNameToRemove); // Find the reservation
                        if (reservationToRemove != null) {
                            Room roomOfReservationToRemove = reservationToRemove.getRoom(); // Get room associated with the reservation
                            removeReservationFromRoom(hotel, roomOfReservationToRemove, reservationToRemove); // Remove the reservation from the room
                            pressEnterToContinue(); // Wait for user to press Enter
                        } else {
                            System.out.println("Reservation not found."); // Inform if reservation is not found
                            pressEnterToContinue(); // Wait for user to press Enter
                        }
                        break;
                    case 6:
                        // Remove the entire hotel
                        removeHotel(hotel); // Remove the hotel
                        exit = true; // Set exit flag to true to break loop
                        break;
                    case 7:
                        // Add Date Price Modifier
                        addDatePriceModifier(scanner, hotel); // Prompt user to add a date price modifier
                        pressEnterToContinue(); // Wait for user to press Enter
                        break;
                    case 8:
                        // Remove Date Price Modifier
                        removeDatePriceModifier(scanner, hotel); // Prompt user to remove a date price modifier
                        pressEnterToContinue(); // Wait for user to press Enter
                        break;
                    case 9:
                        // Exit from hotel management
                        exit = true; // Set exit flag to true to break loop
                        break;
                    default:
                        System.out.println("Invalid option. Please choose again."); // Handle invalid option
                        break;
                }
            }
        }
    }


    /**
     * Adds a date price modifier to the specified hotel based on user input.
     *
     * @param scanner the Scanner object for user input
     * @param hotel   the Hotel object to which the date price modifier will be added
     */
    private void addDatePriceModifier(Scanner scanner, Hotel hotel) {
        // Prompt the user for the start date
        System.out.print("Enter start date (yyyy-mm-dd): ");
        String startDateStr = scanner.nextLine(); // Read the start date string
        Date startDate = parseDate(startDateStr); // Parse the start date

        // Prompt the user for the end date
        System.out.print("Enter end date (yyyy-mm-dd): ");
        String endDateStr = scanner.nextLine(); // Read the end date string
        Date endDate = parseDate(endDateStr); // Parse the end date

        // Prompt the user for the price modifier
        System.out.print("Enter price modifier (0.50 to 1.50): ");
        double modifier = scanner.nextDouble(); // Read the price modifier
        scanner.nextLine(); // Consume the newline character left by nextDouble()

        // Create a new DatePriceModifier object with the specified dates and modifier
        DatePriceModifier datePriceModifier = new DatePriceModifier(startDate, endDate, modifier);
        
        // Add the date price modifier to the hotel
        hotel.addDatePriceModifier(datePriceModifier);
        
        // Inform the user that the date price modifier was set successfully
        System.out.println("Date price modifier set successfully.");
    }


    /**
     * Removes a date price modifier from the specified hotel based on user input.
     *
     * @param scanner the Scanner object for user input
     * @param hotel   the Hotel object from which the date price modifier will be removed
     */
    private void removeDatePriceModifier(Scanner scanner, Hotel hotel) {
        // Prompt the user for the start date of the modifier to remove
        System.out.print("Enter start date of modifier to remove (yyyy-mm-dd): ");
        String startDateStr = scanner.nextLine(); // Read the start date string
        Date startDate = parseDate(startDateStr); // Parse the start date

        // Iterate through the list of date price modifiers for the hotel
        for (DatePriceModifier modifier : hotel.getDatePriceModifiers()) {
            // Check if the current modifier's start date matches the user-provided date
            if (modifier.getStartDate().equals(startDate)) {
                // Remove the matching date price modifier from the hotel
                hotel.removeDatePriceModifier(modifier);
                // Inform the user that the date price modifier was removed successfully
                System.out.println("Date price modifier removed successfully.");
                return; // Exit the method after successful removal
            }
        }

        // Inform the user if no date price modifier was found for the given start date
        System.out.println("No date price modifier found for the given start date.");
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
        System.out.println("7. Add Date Price Modifier"); // Option to add a date price modifier
        System.out.println("8. Remove Date Price Modifier"); //Option to remove a date price modifier
        System.out.println("9. Back to Main Menu"); //Option to go back to the main menu
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
        } else if (newPrice < 100.0) {
            System.out.println("New base price must be >= 100.0.");
        } else {
            // Update the base price since conditions are met
            hotel.updateBasePrice(newPrice); // Call the method in Hotel class to update base price
            System.out.println("Base price updated for hotel '" + hotel.getName() + "'."); // Inform user of successful update
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
     * @param hotel the hotel to which the room is to be added
     * @param roomName  the name of the room to be added
     * @param basePrice the base price of the room to be added
     */
    public void addRoomToHotel(Hotel hotel, String roomName, double basePrice, String roomType) {
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
        hotel.addRoom(roomName, basePrice, roomType); // Call the method in Hotel class to add room
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
        // Select a hotel using user input
        Hotel hotel = selectHotel(scanner);
        
        // Check if a hotel was successfully selected
        if (hotel != null) {
            // Prompt user for guest name
            System.out.print("Enter guest name: ");
            String guestName = scanner.nextLine();
            
            // Prompt user for check-in date
            System.out.print("Enter check-in date (yyyy-mm-dd): ");
            String checkInStr = scanner.nextLine();
            
            // Prompt user for check-out date
            System.out.print("Enter check-out date (yyyy-mm-dd): ");
            String checkOutStr = scanner.nextLine();

            // Convert check-in and check-out date strings to Date objects
            Date checkInDate = parseDate(checkInStr);
            Date checkOutDate = parseDate(checkOutStr);

            // Display available room names for user to choose from
            List<String> availableRoomNames = hotel.getAvailableRoomNames(checkInDate, checkOutDate);
            
            // Check if there are available rooms
            if (availableRoomNames.isEmpty()) {
                System.out.println("No rooms available for the selected dates.");
                return; // Exit method if no rooms are available
            }

            // Print the list of available rooms
            System.out.println("Available rooms:");
            for (int i = 0; i < availableRoomNames.size(); i++) {
                System.out.println((i + 1) + ". " + availableRoomNames.get(i)); // Display room options
            }

            // Prompt user to choose a room number
            System.out.print("Choose a room number: ");
            int roomNumber = scanner.nextInt(); // Read user input for room number
            scanner.nextLine(); // Consume newline after reading integer input

            // Check if the selected room number is valid
            if (roomNumber > 0 && roomNumber <= availableRoomNames.size()) {
                String selectedRoomName = availableRoomNames.get(roomNumber - 1); // Get the selected room name
                Room selectedRoom = hotel.getRoomInfo(selectedRoomName); // Retrieve the Room object
                
                // Check if the selected room exists in the hotel
                if (selectedRoom != null) {
                    // Prompt user for any discount codes to apply
                    System.out.print("Any discount codes that you want to apply? (leave blank if N/A): ");
                    String discountCode = scanner.nextLine();

                    // Simulate booking in the hotel
                    Reservation reservation = hotel.simulateBooking(guestName, checkInDate, checkOutDate, selectedRoom, hotel);
                    
                    // Check if the booking simulation was successful
                    if (reservation != null) {
                        reservation.setDiscountCode(discountCode); // Set discount code if provided
                        selectedRoom.addReservation(reservation); // Add the reservation to the selected room
                        System.out.println("Booking simulated successfully for " + guestName + ".\n");
                        
                        // Display reservation details
                        System.out.println("Here are your reservation details:");
                        System.out.println(reservation.getDetails());

                    } else {
                        System.out.println("Failed to simulate booking."); // Inform user if booking simulation fails
                    }
                } else {
                    System.out.println("Selected room not found in the hotel."); // Inform user if the room is not found
                }
            } else {
                System.out.println("Invalid room number."); // Inform user if the room number is invalid
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
    }*/
}
