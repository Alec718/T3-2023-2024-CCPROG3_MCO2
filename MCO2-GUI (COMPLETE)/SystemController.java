
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;





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
                // Show a popup message if the hotel name already exists
                JOptionPane.showMessageDialog(null, "Hotel with name '" + name + "' already exists. Please choose a different name.", "Invalid Hotel Name", JOptionPane.ERROR_MESSAGE);
                return null; // Return null if the hotel name is not unique
            }
        }

        // Create a new hotel and add it to the list
        Hotel newHotel = new Hotel(name); // Adding an instantiation of a hotel with the chosen name
        hotels.add(newHotel); // Adding hotel to the list of hotels
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
            // Show a confirmation message indicating the hotel has been removed
            JOptionPane.showMessageDialog(null, "Hotel '" + hotel.getName() + "' has been removed successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
        } else {
            // Show a message indicating that the hotel was not found
            JOptionPane.showMessageDialog(null, "Hotel not found.", "Error", JOptionPane.ERROR_MESSAGE);
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
        Hotel hotel = selectHotel(); // Assume selectHotel() handles hotel selection
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
                    viewLowLevelInfo(hotel); // Assuming viewLowLevelInfo() handles low-level info display
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
    private void viewLowLevelInfo(Hotel hotel) {
    // Create a dialog to display low-level information options
    JDialog dialog = new JDialog();
    dialog.setTitle("Select Low-Level Information");
    dialog.setSize(300, 200);
    dialog.setLayout(new GridLayout(4, 1)); // Layout to hold buttons

    // Create and add buttons for each option
    JButton viewRoomsButton = new JButton("Total number of available and booked rooms for a selected date");
    JButton viewRoomDetailsButton = new JButton("Information about a selected room");
    JButton viewReservationDetailsButton = new JButton("Information about a selected reservation");
    JButton cancelButton = new JButton("Cancel");

    // Add action listeners to buttons
    viewRoomsButton.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            viewRoomsAvailability(hotel);
            dialog.dispose();
        }
    });

    viewRoomDetailsButton.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            viewRoomDetails(hotel);
            dialog.dispose();
        }
    });

    viewReservationDetailsButton.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            viewReservationDetails(hotel);
            dialog.dispose();
        }
    });

    cancelButton.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            dialog.dispose();
        }
    });

    // Add buttons to the dialog
    dialog.add(viewRoomsButton);
    dialog.add(viewRoomDetailsButton);
    dialog.add(viewReservationDetailsButton);
    dialog.add(cancelButton);

    dialog.setVisible(true);
}



    /**
     * Allows the user to view the availability of rooms in a selected hotel on a specified date.
     *
     * @param hotel the hotel to check room availability for
     * @param scanner the Scanner object for user input
     */
    private void viewRoomsAvailability(Hotel hotel) {
        String dateStr = JOptionPane.showInputDialog("Enter date to check (yyyy-MM-dd):");
        if (dateStr != null && !dateStr.trim().isEmpty()) {
            Date selectedDate = parseDate(dateStr);
            if (selectedDate != null) {
                int totalAvailableRooms = hotel.getTotalAvailableRooms(selectedDate);
                int totalBookedRooms = hotel.getTotalBookedRooms(selectedDate);
                List<String> availableRoomNames = hotel.getAvailableRoomNames(selectedDate);
    
                StringBuilder message = new StringBuilder();
                message.append("Rooms Availability on ").append(dateStr).append(":\n");
                message.append("Total rooms: ").append(hotel.getTotalRooms()).append("\n");
                message.append("Available rooms: ").append(totalAvailableRooms).append("\n");
                message.append("Booked rooms: ").append(totalBookedRooms).append("\n");
                message.append("Available room names:\n");
                for (String roomName : availableRoomNames) {
                    message.append(roomName).append("\n");
                }
    
                JOptionPane.showMessageDialog(null, message.toString(), "Rooms Availability", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "Invalid date format. Please use yyyy-MM-dd.", "Error", JOptionPane.ERROR_MESSAGE);
            }
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
    private void viewRoomDetails(Hotel hotel) {
        String roomName = JOptionPane.showInputDialog("Enter room name:");
        if (roomName != null && !roomName.trim().isEmpty()) {
            Room room = hotel.getRoomInfo(roomName);
            if (room != null) {
                StringBuilder message = new StringBuilder();
                message.append("Room Information:\n");
                message.append("Room Name: ").append(room.getName(roomName)).append("\n");
                message.append("Room Type: ").append(room.getRoomType()).append("\n");
                message.append("Base Price: PHP ").append(room.getBasePrice()).append("\n");
    
                String yearStr = JOptionPane.showInputDialog("Checking Room Availability... Choose a year (e.g., 2024):");
                String monthStr = JOptionPane.showInputDialog("Checking Days in Month Availability... Choose a month (1-12):");
    
                try {
                    int year = Integer.parseInt(yearStr);
                    int month = Integer.parseInt(monthStr);
    
                    List<String> availability = room.getAvailabilityForMonth(year, month);
                    message.append("Room Availability for ").append(month).append("/").append(year).append(":\n");
                    for (String dayAvailability : availability) {
                        message.append(dayAvailability).append("\n");
                    }
    
                    JOptionPane.showMessageDialog(null, message.toString(), "Room Details", JOptionPane.INFORMATION_MESSAGE);
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(null, "Invalid year or month format.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(null, "Room not found.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    


    /**
     * Allows the user to view detailed information about a selected reservation in a hotel.
     *
     * @param hotel the hotel containing the reservation
     * @param scanner the Scanner object for user input
     */
    private void viewReservationDetails(Hotel hotel) {
        String guestName = JOptionPane.showInputDialog("Enter guest name to find reservation:");
        if (guestName != null && !guestName.trim().isEmpty()) {
            Reservation reservation = findReservationByGuestName(hotel, guestName);
            if (reservation != null) {
                JOptionPane.showMessageDialog(null, reservation.getDetails(), "Reservation Details", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "Reservation not found.", "Error", JOptionPane.ERROR_MESSAGE);
            }
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
        Hotel hotel = selectHotel();
        
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
                       // pressEnterToContinue(); // Wait for user to press Enter
                        break;
                    case 2:
                        // Update hotel base price
                        System.out.print("Enter new base price: ");
                        double newPrice = scanner.nextDouble(); // Get the new base price
                        updateHotelBasePrice(hotel, newPrice); // Update the hotel's base price
                      //  pressEnterToContinue(); // Wait for user to press Enter
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
                           // pressEnterToContinue(); // Wait for user to press Enter
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
                           // pressEnterToContinue(); // Wait for user to press Enter
                        } else {
                            System.out.println("Reservation not found."); // Inform if reservation is not found
                           // pressEnterToContinue(); // Wait for user to press Enter
                        }
                        break;
                    case 6:
                        // Remove the entire hotel
                        removeHotel(hotel); // Remove the hotel
                        exit = true; // Set exit flag to true to break loop
                        break;
                    case 7:
                        // Add Date Price Modifier
                        addDatePriceModifier(hotel); // Prompt user to add a date price modifier
                       // pressEnterToContinue(); // Wait for user to press Enter
                        break;
                    case 8:
                        // Remove Date Price Modifier
                        removeDatePriceModifier(hotel); // Prompt user to remove a date price modifier
                       // pressEnterToContinue(); // Wait for user to press Enter
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
    public void addDatePriceModifier(Hotel hotel) {
        // Prompt the user for the start date
        String startDateStr = JOptionPane.showInputDialog("Enter start date (yyyy-mm-dd):");
        Date startDate = parseDate(startDateStr); // Parse the start date
    
        // Prompt the user for the end date
        String endDateStr = JOptionPane.showInputDialog("Enter end date (yyyy-mm-dd):");
        Date endDate = parseDate(endDateStr); // Parse the end date
    
        // Prompt the user for the price modifier
        String modifierStr = JOptionPane.showInputDialog("Enter price modifier (0.50 to 1.50):");
        double modifier = Double.parseDouble(modifierStr); // Parse the price modifier
    
        // Check if the modifier is within the valid range
        if (modifier >= 0.5 && modifier <= 1.5) {
            // Create a new DatePriceModifier object with the specified dates and modifier
            DatePriceModifier datePriceModifier = new DatePriceModifier(startDate, endDate, modifier);
            
            // Add the date price modifier to the hotel
            hotel.addDatePriceModifier(datePriceModifier);
            
            // Inform the user that the date price modifier was set successfully
            JOptionPane.showMessageDialog(null, "Date price modifier set successfully.");
        } else {
            // Inform the user that the modifier is out of the valid range
            JOptionPane.showMessageDialog(null, "Price modifier must be between 0.50 and 1.50.", "Invalid Modifier", JOptionPane.ERROR_MESSAGE);
        }
    }
    

    /**
     * Removes a date price modifier from the specified hotel based on user input.
     *
     * @param scanner the Scanner object for user input
     * @param hotel   the Hotel object from which the date price modifier will be removed
     */
    public void removeDatePriceModifier(Hotel hotel) {
        // Prompt the user for the start date of the modifier to remove
        String startDateStr = JOptionPane.showInputDialog("Enter start date of modifier to remove (yyyy-mm-dd):");
        if (startDateStr == null) {
            return; // Exit if no date was entered
        }
        
        Date startDate = parseDate(startDateStr); // Parse the start date
    
        // Iterate through the list of date price modifiers for the hotel
        for (DatePriceModifier modifier : hotel.getDatePriceModifiers()) {
            // Check if the current modifier's start date matches the user-provided date
            if (modifier.getStartDate().equals(startDate)) {
                int confirm = JOptionPane.showConfirmDialog(null, "Are you sure you want to remove the date price modifier for " + startDateStr + "?",
                        "Confirm Removal", JOptionPane.YES_NO_OPTION);
    
                if (confirm == JOptionPane.YES_OPTION) {
                    // Remove the matching date price modifier from the hotel
                    hotel.removeDatePriceModifier(modifier);
                    // Inform the user that the date price modifier was removed successfully
                    JOptionPane.showMessageDialog(null, "Date price modifier removed successfully.");
                    return; // Exit the method after successful removal
                } else {
                    // Inform the user that the removal was canceled
                    JOptionPane.showMessageDialog(null, "Date price modifier removal canceled.");
                    return; // Exit the method as the user canceled
                }
            }
        }
    
        // Inform the user if no date price modifier was found for the given start date
        JOptionPane.showMessageDialog(null, "No date price modifier found for the given start date.");
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
    for (Reservation reservation : hotel.getReservations()) {
        if (reservation.getGuestName().equalsIgnoreCase(guestName)) {
            return reservation; // Return the matching reservation
        }
    }
    return null; // Return null if no matching reservation is found
    }


    /**
     * Allows the user to select a hotel from the list of available hotels.
     *
     * @param scanner the Scanner object for user input
     * @return the selected Hotel object, or null if no hotels are available or an invalid selection is made
     */
    public Hotel selectHotel() {
        // Check if there are any hotels available
        if (hotels.isEmpty()) {
            return null; // Return null if there are no hotels
        }
    
        // Display a list of available hotels for selection
        String[] hotelNames = new String[hotels.size()];
        for (int i = 0; i < hotels.size(); i++) {
            hotelNames[i] = (i + 1) + ". " + hotels.get(i).getName(); // Prepare hotel index and name
        }
    
        // Show a dialog with a list of hotels
        String selectedHotel = (String) JOptionPane.showInputDialog(
                null,
                "Select a hotel:",
                "Select Hotel",
                JOptionPane.QUESTION_MESSAGE,
                null,
                hotelNames,
                hotelNames[0]
        );
    
        if (selectedHotel != null) {
            int index = Integer.parseInt(selectedHotel.split("\\.")[0]) - 1; // Extract index from selectedHotel string
            if (index >= 0 && index < hotels.size()) {
                return hotels.get(index); // Return the selected hotel object
            }
        }
        return null; // Return null if selection is invalid
    }
    



    /**
     * Changes the name of the specified hotel, ensuring uniqueness among existing hotels.
     *
     * @param hotel   the hotel whose name is to be changed
     * @param newName the new name to be assigned to the hotel
     */
    public boolean changeHotelName(Hotel hotel, String newName) {
        // Check if the new hotel name is already in use
        for (Hotel existingHotel : hotels) {
            // Compare existing hotel names (case insensitive) with the new name
            if (existingHotel.getName().equalsIgnoreCase(newName)) {
                // Show a popup message if the hotel name already exists
                JOptionPane.showMessageDialog(null, "Hotel with name '" + newName + "' already exists. Please choose a different name.", "Invalid Hotel Name", JOptionPane.ERROR_MESSAGE);
                return false; // Indicate that the name change was not successful
            }
        }
        // Update the hotel name since it is unique
        hotel.changeName(newName);
        return true; // Indicate that the name change was successful
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
        JOptionPane.showMessageDialog(null, "Cannot update base price. There are active reservations in the hotel.", "Update Failed", JOptionPane.ERROR_MESSAGE);
    } else if (newPrice < 100.0) {
        JOptionPane.showMessageDialog(null, "New base price must be >= 100.0.", "Invalid Price", JOptionPane.WARNING_MESSAGE);
    } else {
        // Update the base price since conditions are met
        hotel.updateBasePrice(newPrice); // Call the method in Hotel class to update base price
        JOptionPane.showMessageDialog(null, "Base price updated successfully to " + newPrice + ".", "Update Successful", JOptionPane.INFORMATION_MESSAGE);
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
    public boolean addRoomToHotel(Hotel hotel, String roomName, double basePrice, String roomType) {
        // Check if the hotel already has the maximum number of rooms (50)
        if (hotel.getRooms().size() >= 50) {
            // Show an error message if the hotel has reached the maximum number of rooms
            JOptionPane.showMessageDialog(null, "Cannot add room. Hotel '" + hotel.getName() + "' already has the maximum number of rooms (50).", "Error", JOptionPane.ERROR_MESSAGE);
            return false; // Indicate that the room could not be added
        }
    
        // Validate room name uniqueness within the hotel
        for (Room room : hotel.getRooms()) {
            if (room.getName().equalsIgnoreCase(roomName)) {
                // Show an error message if room name is not unique
                JOptionPane.showMessageDialog(null, "Room with name '" + roomName + "' already exists in hotel '" + hotel.getName() + "'.", "Error", JOptionPane.ERROR_MESSAGE);
                return false; // Indicate that the room could not be added
            }
        }
    
        // Add room to the hotel
        hotel.addRoom(roomName, basePrice, roomType); // Call the method in Hotel class to add room
    
        return true; // Indicate that the room was added successfully
    }
    
    
    


    /**
     * Removes a room from the specified hotel, subject to certain conditions.
     *
     * @param hotel the hotel from which the room is to be removed
     * @param room  the room to be removed from the hotel
     */
    public boolean removeRoomFromHotel(Hotel hotel, Room room) {
        // Check if the room has any active reservations
        if (roomHasActiveReservations(room)) {
            // Show an error message if the room has active reservations
            JOptionPane.showMessageDialog(null, "Cannot remove room '" + room.getName() + "' because it has active reservations.", "Error", JOptionPane.ERROR_MESSAGE);
            return false; // Indicate failure
        } else {
            // Remove the room from the hotel
            hotel.removeRoom(room); // Call the method in Hotel class to remove the room
            
            // Show a success message if the room is removed
            JOptionPane.showMessageDialog(null, "Room '" + room.getName() + "' has been removed successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
            return true; // Indicate success
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
            // Display a popup message indicating the reservation has been removed
            JOptionPane.showMessageDialog(null, "Reservation for " + reservation.getGuestName() + " removed from room '" + room.getName() + "'", "Reservation Removed", JOptionPane.INFORMATION_MESSAGE);
        } else {
            // Display a popup message indicating the failure to remove the reservation
            JOptionPane.showMessageDialog(null, "Failed to remove reservation. Please ensure the room and reservation are valid.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    


    /**
     * Simulates a booking process for a guest in the selected hotel.
     *
     * @param scanner the Scanner object for user input
     */
    public void simulateBooking(Scanner scanner) {
        // Select a hotel using user input
        Hotel hotel = selectHotel();
        
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
    
                    // Create the booking
                    Reservation reservation = hotel.simulateBooking(guestName, checkInDate, checkOutDate, selectedRoom, hotel);
                    
                    // Handle discount codes
                    double finalPrice = reservation.getTotalPrice(); // Total price with modifiers and discounts
                    if (discountCode != null && !discountCode.trim().isEmpty()) {
                        switch (discountCode) {
                            case "I_WORK_HERE":
                                finalPrice *= 0.9; // Example: 10% discount
                                break;
                            case "STAY4_GET1":
                                long nightsStayed = (checkOutDate.getTime() - checkInDate.getTime()) / (1000 * 60 * 60 * 24);
                                if (nightsStayed >= 5) {
                                    finalPrice *= 0.8; // Example: 20% discount
                                }
                                break;
                            case "PAYDAY":
                                Calendar calendar = Calendar.getInstance();
                                calendar.setTime(checkInDate);
                                int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
                                if (dayOfMonth == 15 || dayOfMonth == 30) {
                                    finalPrice *= 0.85; // Example: 15% discount
                                }
                                break;
                            default:
                                System.out.println("Invalid discount code.");
                        }
                    }
    
                    // Update reservation with final price and discount code
                    reservation.setDiscountCode(discountCode); // Set the discount code for the reservation
                    reservation.setFinalPrice(finalPrice); // Set the final price for the reservation after applying the discount
                    selectedRoom.addReservation(reservation); // Add the reservation to the selected room
    
                    // Display reservation details
                    System.out.println("Booking simulated successfully for " + guestName + ".\n");
                    System.out.println("Here are your reservation details:");
                    System.out.println(reservation.getDetails());
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

    /**
    * Gets the names of all hotels in the list.
    *
    * @return a list of hotel names
    */
    public List<String> getHotelNames() {
        List<String> hotelNames = new ArrayList<>(); // Initialize a list to store hotel names

        for (Hotel hotel : hotels) { // Iterate through each hotel in the list
        hotelNames.add(hotel.getName()); // Add the name of the current hotel to the list
    }

    return hotelNames; // Return the list of hotel names
    }

    
    /**
    * Finds and returns a hotel by its name.
    *
    * @param name the name of the hotel to find
    * @return the hotel with the specified name, or null if not found
    */
    public Hotel getHotelByName(String name) {
        for (Hotel hotel : hotels) { // Iterate through each hotel in the list
            if (hotel.getName().equals(name)) { // Check if the hotel's name matches the specified name
                return hotel; // Return the matching hotel
        }
    }
    return null; // Return null if no hotel matches the name
    }

    /**
    * Retrieves a list of available rooms in the specified hotel for the given date range.
    *
    * @param hotel the hotel to check for available rooms
    * @param checkInDate the check-in date
    * @param checkOutDate the check-out date
    * @return a list of available rooms for the specified date range
    */
    public List<Room> getAvailableRooms(Hotel hotel, Date checkInDate, Date checkOutDate) {
        List<Room> availableRooms = new ArrayList<>(); // Initialize a list to store available rooms
        for (Room room : hotel.getRooms()) { // Iterate through each room in the hotel's room list
            if (isRoomAvailable(room, checkInDate, checkOutDate)) { // Check if the room is available for the date range
                availableRooms.add(room); // Add the available room to the list
            }
        }
        return availableRooms; // Return the list of available rooms
    }
    
    /**
 * Checks if a room is available for a given date range.
 *
 * @param room the room to check
 * @param checkInDate the check-in date
 * @param checkOutDate the check-out date
 * @return true if the room is available, false otherwise
 */
private boolean isRoomAvailable(Room room, Date checkInDate, Date checkOutDate) {
    for (Reservation reservation : room.getReservations()) { // Iterate through each reservation for the room
        // Check if the reservation dates overlap with the requested date range
        if (reservation.getCheckOutDate().after(checkInDate) && reservation.getCheckInDate().before(checkOutDate)) {
            return false; // Return false if there is an overlapping reservation
        }
    }
    return true; // Return true if no overlapping reservations are found
}

/**
 * Adds a reservation to both the specified room and hotel.
 *
 * @param hotel the hotel to which the reservation will be added
 * @param room the room to which the reservation will be added
 * @param reservation the reservation to be added
 */
public void addReservation(Hotel hotel, Room room, Reservation reservation) {
    room.addReservation(reservation); // Add the reservation to the room's list of reservations
    hotel.addReservation(reservation); // Add the reservation to the hotel's list of reservations
}

    
    
}
