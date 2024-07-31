import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Scanner;
import java.util.Calendar;
import java.util.Date;


/**
 * The HotelGUI class provides a graphical user interface for managing a hotel system.
 * It allows users to create hotels, view hotel information, manage hotels, and simulate bookings.
 */
public class HotelGUI {
    private SystemController controller; //controller to manage hotel operations
    private JTextArea textArea; // component for displaying messages and updates to the user
    private Reservation reservation; //object to hold information about a specific reservation

    /**
     * Constructs a HotelGUI instance and initializes the graphical user interface.
     */
    public HotelGUI() {
        this.controller = new SystemController(); // Initiliaze the SystemController to handle hotel-related operations
        createAndShowGUI(); // Set up and display the GUI components
    }

    /**
     * Creates and displays the main GUI frame and its components.
     */
    private void createAndShowGUI() {
        // Create the main frame
        JFrame frame = new JFrame("Hotel Management System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Close the application when the frame is closed
        frame.setSize(500, 400); // Set the size of the frame
        frame.setLayout(new BorderLayout()); // Set layout manager for the frame

        // Create the text area for displaying messages
        textArea = new JTextArea();
        textArea.setEditable(false); // Make the text area non-editable
        JScrollPane scrollPane = new JScrollPane(textArea); // Add a scroll pane to the text area
        frame.add(scrollPane, BorderLayout.CENTER); // Add the scroll pane to the center of the frame

        // Create a panel for buttons
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(5, 1)); // Set a grid layout with 5 rows and 1 column for the panel

        // Create buttons
        JButton createHotelButton = new JButton("Create Hotel");
        JButton viewHotelButton = new JButton("View Hotel");
        JButton manageHotelButton = new JButton("Manage Hotel");
        JButton simulateBookingButton = new JButton("Simulate Booking");
        JButton exitButton = new JButton("Exit");

        // Add action listeners to buttons
        createHotelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                createHotel(); // Call method to create a hotel
            }
        });

        viewHotelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                viewHotel(); // Call method to create a hotel
            }
        });

        manageHotelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                manageHotel(); // Call method to manage hotel operations
            }
        });

        simulateBookingButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Step 1: Select a hotel
                List<String> hotelNames = controller.getHotelNames(); // Retrieve the list of hotel names
                if (hotelNames.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "No hotels available. Please create a hotel first.");
                    return; // Exit if no hotels are available
                }

                // Show a dialog to select a hotel
                String selectedHotelName = (String) JOptionPane.showInputDialog(
                        null,
                        "Select a hotel to book:",
                        "Simulate Booking",
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        hotelNames.toArray(),
                        hotelNames.get(0)
                );
        
                if (selectedHotelName != null) {
                    Hotel hotel = controller.getHotelByName(selectedHotelName);
        
                    // Step 2: Enter guest name, check-in date, and check-out date
                    String guestName = JOptionPane.showInputDialog("Enter guest name:");
                    if (guestName == null || guestName.trim().isEmpty()) {
                        return; // Exit if no guest name provided
                    }
        
                    String checkInDateStr = JOptionPane.showInputDialog("Enter check-in date (yyyy-mm-dd):");
                    Date checkInDate = parseDate(checkInDateStr); // Parse check-in date
                    if (checkInDate == null) {
                        JOptionPane.showMessageDialog(null, "Invalid check-in date.");
                        return; // Exit if invalid check-in date
                    }
        
                    String checkOutDateStr = JOptionPane.showInputDialog("Enter check-out date (yyyy-mm-dd):");
                    Date checkOutDate = parseDate(checkOutDateStr); // Parse check-out date
                    if (checkOutDate == null) {
                        JOptionPane.showMessageDialog(null, "Invalid check-out date.");
                        return; // Exit if invalid check-out date
                    }
        
                    // Step 3: Display available rooms
                    List<Room> availableRooms = controller.getAvailableRooms(hotel, checkInDate, checkOutDate); // Retrieve available rooms
                    if (availableRooms.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "No rooms available for the selected dates.");
                        return; // Exit if no rooms available
                    }
        
                    // Show a dialog to select a room
                    String[] roomNames = availableRooms.stream().map(Room::getName).toArray(String[]::new);
                    String selectedRoomName = (String) JOptionPane.showInputDialog(
                            null,
                            "Select a room:",
                            "Available Rooms",
                            JOptionPane.QUESTION_MESSAGE,
                            null,
                            roomNames,
                            roomNames[0]
                    );
        
                    if (selectedRoomName != null) {
                        Room selectedRoom = hotel.getRoomInfo(selectedRoomName);
                        if (selectedRoom != null) {
                            // Prompt user for any discount codes
                            String discountCode = JOptionPane.showInputDialog("Enter discount code (leave blank if N/A):");
        
                            // Create the booking
                            Reservation reservation = new Reservation(guestName, checkInDate, checkOutDate, selectedRoom, hotel);
                            controller.addReservation(hotel, selectedRoom, reservation); // Add the reservation to the controller
        
                            // Apply discount codes if applicable
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
                                        JOptionPane.showMessageDialog(null, "Invalid discount code.");
                                }
                            }
        
                            reservation.setDiscountCode(discountCode); // Set discount code in reservation
                            reservation.setFinalPrice(finalPrice); // Set final price in reservation
        
                            // Show confirmation
                            JOptionPane.showMessageDialog(null, "Booking confirmed for room '" + selectedRoomName + "'.\n" + reservation.getDetails());
                        }
                    }
                }
            }
        });
        

        
        //Exits the whole program when clicked
        exitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                exit();
            }
        });

        // Add buttons to the panel
        panel.add(createHotelButton); // Add the 'Create Hotel' button to panel
        panel.add(viewHotelButton); // Add the 'View Hotel' button to the panel
        panel.add(manageHotelButton); // Add the 'Manage Hotel' button to the panel
        panel.add(simulateBookingButton); // Add the 'Simulate Booking' button to the panel
        panel.add(exitButton); // Add the 'Exit' button to the panel

        frame.add(panel, BorderLayout.CENTER); // Add the panel containing buttons to the center of the frame
        frame.setVisible(true); // Make the frame visible to the user
    }

    /**
     * Handles creating a new hotel by prompting the user for hotel details.
     */
    private void createHotel() {
        String name = JOptionPane.showInputDialog("Enter hotel name:"); // Prompt the user to enter a hotel name
        if (name != null && !name.trim().isEmpty()) { // Check if the entered name is not null and not empty
            Hotel createdHotel = controller.createHotel(name); // Create a new hotel using the provided name
            if (createdHotel != null) { // Check if the hotel was successfully created
                updateTextArea("Hotel '" + name + "' created."); // Update the text area with a success message
                JOptionPane.showMessageDialog(null, "Hotel '" + name + "' created successfully."); // Display a confirmation dialog to the user
            }
        }
    }
    
    /**
     * Handles viewing hotel information by allowing the user to select a hotel and view its details.
     */
    private void viewHotel() {
        List<String> hotelNames = controller.getHotelNames(); // Retrieve the list of hotel names from the controller
        if (hotelNames.isEmpty()) { // Check if there are no hotels available
            JOptionPane.showMessageDialog(null, "No hotels available. Please create a hotel first."); // Display a message if no hotels are available
            return; // Exit the method if no hotels are found
        }
    
        String selectedHotelName = (String) JOptionPane.showInputDialog( // Prompt the user to select a hotel from the list
                null,
                "Select a hotel:",
                "View Hotel",
                JOptionPane.QUESTION_MESSAGE,
                null,
                hotelNames.toArray(),
                hotelNames.get(0)
        );
    
        if (selectedHotelName != null) { // Check if a hotel was selected
            Hotel hotel = controller.getHotelByName(selectedHotelName); // Retrieve the selected hotel object
            if (hotel != null) { // Check if the hotel object is valid
    
                // Create a dialog to display hotel information
                JDialog dialog = new JDialog();
                dialog.setTitle("View Hotel Information");
                dialog.setSize(300, 200);
                dialog.setLayout(new GridLayout(3, 1));
    
                JLabel label = new JLabel("Choose the information level:"); // Create and add a label to the dialog
                dialog.add(label);
    
                JButton highLevelButton = new JButton("High-level information"); // Create and add a button to display high-level information
                highLevelButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        // Get and display high-level information about the hotel
                        String highLevelInfo = hotel.getHighLevelInfo(); // Assuming getHighLevelInfo() returns formatted string
                        JOptionPane.showMessageDialog(dialog, highLevelInfo, "High-level Information", JOptionPane.INFORMATION_MESSAGE);
                        updateTextArea("Viewed high-level information for hotel '" + hotel.getName() + "'."); // Update the text area with a message about viewing high-level information
                    }
                });
    
                JButton lowLevelButton = new JButton("Low-level information"); // Create and add a button to display low-level information
                lowLevelButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        // Prompt the user to enter a guest name to check for reservations
                        String guestName = JOptionPane.showInputDialog(null, "Enter the guest name to check for reservation:");
                        if (guestName != null && !guestName.trim().isEmpty()) { // Check if a valid name is entered
                            Reservation matchingReservation = controller.findReservationByGuestName(hotel, guestName); // Find reservation by guest name
                            if (matchingReservation != null) {
                                // Show details of the matching reservation
                                String reservationDetails = matchingReservation.getDetails(); // Assuming getDetails() returns formatted string of reservation details
                                JOptionPane.showMessageDialog(dialog, reservationDetails, "Reservation Details", JOptionPane.INFORMATION_MESSAGE);
                            } else {
                                JOptionPane.showMessageDialog(dialog, "No reservation exists under the name: " + guestName); // Show message if no reservation exists
                            }
                        } else {
                            JOptionPane.showMessageDialog(dialog, "Please enter a valid name."); // Show message if no valid name is entered
                        }
    
                        viewLowLevelInfo(hotel); // Call method to view low-level information
                        dialog.dispose();
                    }
                });
    
                // Add the buttons to the dialog
                dialog.add(highLevelButton);
                dialog.add(lowLevelButton);
    
                // Display the dialog
                dialog.setVisible(true);
            }
        }
    }
    
    
    
    
    
    
    /**
     * Creates and displays a dialog to view low-level information about a selected hotel.
     * 
     * @param hotel The hotel whose low-level information is to be viewed.
     */
    private void viewLowLevelInfo(Hotel hotel) {
    
    
    // Create a new dialog to display low-level options
    JDialog dialog = new JDialog((JFrame) null, "Low-Level Information", true);
    dialog.setSize(300, 200);
    dialog.setLayout(new GridLayout(4, 1));

    // Create buttons for each option
    JButton viewRoomsButton = new JButton("View Rooms Availability");
    JButton viewRoomDetailsButton = new JButton("View Room Details");
    JButton viewReservationDetailsButton = new JButton("View Reservation Details");
    JButton cancelButton = new JButton("Cancel");

    // Add action listeners to buttons
    viewRoomsButton.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            viewRoomsAvailability(hotel, new Scanner(System.in)); // Adjust Scanner use as needed
        }
    });

    viewRoomDetailsButton.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            viewRoomDetails(hotel, new Scanner(System.in)); // Adjust Scanner use as needed
        }
    });

    viewReservationDetailsButton.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            viewReservationDetails(hotel, new Scanner(System.in)); // Adjust Scanner use as needed
        }
    });

    cancelButton.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            dialog.dispose(); // Close the dialog
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
     * Parses a date string into a Date object.
     * 
     * @param dateStr The date string to be parsed.
     * @return The parsed Date object, or null if parsing fails.
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
     * Displays room availability for a selected date.
     * 
     * @param hotel The hotel whose rooms are to be checked.
     * @param scanner The scanner used for user input.
     */
    private void viewRoomsAvailability(Hotel hotel, Scanner scanner) {
    // Prompt user for the date to check room availability
    String dateStr = JOptionPane.showInputDialog("Enter date to check (yyyy-MM-dd):");
    Date selectedDate = parseDate(dateStr); // Parse the entered date string into a Date object

    // Retrieve the number of available and booked rooms for the selected date
    int totalAvailableRooms = hotel.getTotalAvailableRooms(selectedDate);
    int totalBookedRooms = hotel.getTotalBookedRooms(selectedDate);
    // Get a list of available room names for the selected date
    List<String> availableRoomNames = hotel.getAvailableRoomNames(selectedDate);

    // Construct a message string with room availability information
    String message = "Rooms Availability on " + dateStr + ":\n" +
                     "Total rooms: " + hotel.getTotalRooms() + "\n" +
                     "Available rooms: " + totalAvailableRooms + "\n" +
                     "Booked rooms: " + totalBookedRooms + "\n" +
                     "Available rooms names:\n" + String.join("\n", availableRoomNames);
    
    // Display the constructed message in a dialog box
    JOptionPane.showMessageDialog(null, message);
}

    /**
     * Displays details of a specific room in the selected hotel.
     * 
     * @param hotel The hotel where the room is located.
     * @param scanner The scanner used for user input.
     */
    private void viewRoomDetails(Hotel hotel, Scanner scanner) {
        // Prompt the user to enter the name of the room they want to view
        String roomName = JOptionPane.showInputDialog("Enter room name:");
        // Retrieve room information based on the entered room name
        Room room = hotel.getRoomInfo(roomName);
    
        // Check if the room exists
        if (room != null) {
            // Construct a message with basic room information
            String message = "Room Information:\n" +
                             "Room Name: " + room.getName() + "\n" + // Display the name of the room
                             "Room Type: " + room.getRoomType() + "\n" + // Display the type of the room
                             "Base Price: PHP " + room.getBasePrice(); // Display the base price of the room
    
            // Prompt the user to enter a year to check room availability
            int year = Integer.parseInt(JOptionPane.showInputDialog("Choose a year (e.g., 2024):"));
            // Prompt the user to enter a month to check room availability
            int month = Integer.parseInt(JOptionPane.showInputDialog("Choose a month (1-12):"));
    
            // Retrieve the availability of the room for the specified year and month
            List<String> availability = room.getAvailabilityForMonth(year, month);
            // Append the room's availability information to the message
            message += "\nRoom Availability for " + month + "/" + year + ":\n" +
                       String.join("\n", availability); // List availability details
    
            // Display the constructed message in a dialog box
            JOptionPane.showMessageDialog(null, message);
        } else {
            // Show an error message if the room is not found
            JOptionPane.showMessageDialog(null, "Room not found.");
        }
    }
    

    /**
     * Displays details of reservations for a specific room.
     * 
     * @param hotel The hotel where the room is located.
     * @param scanner The scanner used for user input.
     */
    private void viewReservationDetails(Hotel hotel, Scanner scanner) {
    // Prompt the user to enter the guest's name to find the reservation
    String guestName = JOptionPane.showInputDialog("Enter guest name to find reservation:");
    // Use the controller to find the reservation by the guest's name
    controller.findReservationByGuestName(hotel, guestName);

    // Check if the reservation was found
    if (controller != null) {
        // Display the reservation details in the dialog box
        JOptionPane.showMessageDialog(null, "Reservation Details:\n" + reservation.getDetails());
    } else {
        JOptionPane.showMessageDialog(null, "Reservation not found.");
    }
}


    
    

/**
 * Displays a dialog with options for managing a selected hotel, including changing the hotel name,
 * updating the base price, adding/removing rooms, removing reservations, removing the hotel, and managing date-based price modifiers.
 */
private void manageHotel() {
    // Get the list of hotel names from the controller
    List<String> hotelNames = controller.getHotelNames();
    // Check if there are no hotels available
    if (hotelNames.isEmpty()) {
        JOptionPane.showMessageDialog(null, "No hotels available. Please create a hotel first.");
        return; // Exit if no hotels are available
    }

    // Show a dialog to select a hotel to manage
    String selectedHotelName = (String) JOptionPane.showInputDialog(
            null,
            "Select a hotel to manage:",
            "Manage Hotel",
            JOptionPane.QUESTION_MESSAGE,
            null,
            hotelNames.toArray(),
            hotelNames.get(0)
    );

    // Proceed if a hotel is selected
    if (selectedHotelName != null) {
        // Retrieve the selected hotel
        Hotel hotel = controller.getHotelByName(selectedHotelName);
        if (hotel != null) {
            // Create a dialog for managing hotel options
            JDialog dialog = new JDialog((JFrame) null, "Hotel Management Options", true);
            dialog.setSize(700, 400);
            dialog.setLayout(new GridLayout(3, 1));

            // Create a panel for buttons
            JPanel buttonPanel = new JPanel();
            buttonPanel.setLayout(new GridLayout(0, 3, 10, 10));

            // Create and add buttons to the panel
            JButton changeNameButton = new JButton("Change Hotel Name");
            JButton updatePriceButton = new JButton("Update Base Price");
            JButton addRoomButton = new JButton("Add Room");
            JButton removeRoomButton = new JButton("Remove Room");
            JButton removeReservationButton = new JButton("Remove Reservation");
            JButton removeHotelButton = new JButton("Remove Hotel");
            JButton addDateModifierButton = new JButton("Add Date Price Modifier");
            JButton removeDateModifierButton = new JButton("Remove Date Price Modifier");
            JButton exitButton = new JButton("Exit");

            // Add action listener for changing the hotel name
            changeNameButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    // Prompt user for a new hotel name
                    String newName = JOptionPane.showInputDialog("Enter new hotel name:");
                    if (newName != null && !newName.trim().isEmpty()) {
                        // Attempt to change the hotel name
                        boolean isChanged = controller.changeHotelName(hotel, newName);
                        if (isChanged) {
                            updateTextArea("Hotel name changed to '" + newName + "'.");
                            JOptionPane.showMessageDialog(null, "Hotel name changed to '" + newName + "' successfully.");
                            dialog.dispose(); // Close the dialog after successful name change
                        }
                    }
                }
            });

            // Add action listener for updating the base price of the hotel
            updatePriceButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    // Prompt user for a new base price
                    String newPriceStr = JOptionPane.showInputDialog("Enter new base price:");
                    if (newPriceStr != null) {
                        try {
                            double newPrice = Double.parseDouble(newPriceStr);
                            // Update the hotel's base price
                            controller.updateHotelBasePrice(hotel, newPrice);
                            updateTextArea("Hotel base price updated to '" + newPrice + "'.");
                            dialog.dispose(); // Close the dialog after successful price update
                        } catch (NumberFormatException ex) {
                            JOptionPane.showMessageDialog(null, "Invalid price. Please enter a valid number.");
                        }
                    }
                }
            });

            // Add action listener for adding a new room to the hotel
            addRoomButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    // Prompt user for room name and type
                    String roomName = JOptionPane.showInputDialog("Enter room name:");
                    if (roomName != null && !roomName.trim().isEmpty()) {
                        double roomBasePrice = hotel.getBasePrice();
                        String[] roomTypes = {"Standard", "Deluxe", "Executive"};
                        String roomType = (String) JOptionPane.showInputDialog(null, "Select room type:",
                                "Room Type", JOptionPane.QUESTION_MESSAGE, null, roomTypes, roomTypes[0]);
                        if (roomType != null) {
                            // Add the new room to the hotel
                            boolean roomAdded = controller.addRoomToHotel(hotel, roomName, roomBasePrice, roomType);
                            if (roomAdded) {
                                updateTextArea("Room '" + roomName + "' added to " + hotel.getName());
                                JOptionPane.showMessageDialog(null, "Room '" + roomName + "' added to " + hotel.getName(), "Success", JOptionPane.INFORMATION_MESSAGE);
                            }
                        }
                    }
                }
            });

            // Add action listener for removing a room from the hotel
            removeRoomButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    // Get the list of rooms from the hotel
                    List<Room> rooms = hotel.getRooms();
                    if (rooms.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "No rooms available in the hotel.");
                        return;
                    }

                    // Create a JComboBox with room names for selection
                    JComboBox<Room> roomComboBox = new JComboBox<>(rooms.toArray(new Room[0]));
                    roomComboBox.setRenderer(new DefaultListCellRenderer() {
                        @Override
                        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                            Room room = (Room) value;
                            return super.getListCellRendererComponent(list, room.getName(), index, isSelected, cellHasFocus);
                        }
                    });

                    // Show a dialog with the JComboBox for room selection
                    int option = JOptionPane.showConfirmDialog(null, roomComboBox, "Select Room to Remove", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

                    if (option == JOptionPane.OK_OPTION) {
                        Room selectedRoom = (Room) roomComboBox.getSelectedItem();
                        if (selectedRoom != null) {
                            // Remove the selected room from the hotel
                            controller.removeRoomFromHotel(hotel, selectedRoom);
                            updateTextArea("Room '" + selectedRoom.getName() + "' removed from " + hotel.getName());
                            JOptionPane.showMessageDialog(null, "Room '" + selectedRoom.getName() + "' removed from " + hotel.getName(), "Success", JOptionPane.INFORMATION_MESSAGE);
                        }
                    }
                }
            });

            // Add action listener for removing a reservation from the hotel
            removeReservationButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    // Prompt user for guest name to find reservation
                    String guestName = JOptionPane.showInputDialog("Enter guest name to remove reservation:");
                    if (guestName != null && !guestName.trim().isEmpty()) {
                        // Find and remove the reservation for the guest
                        Reservation reservationToRemove = controller.findReservationByGuestName(hotel, guestName);
                        if (reservationToRemove != null) {
                            Room roomOfReservationToRemove = reservationToRemove.getRoom();
                            controller.removeReservationFromRoom(hotel, roomOfReservationToRemove, reservationToRemove);
                            updateTextArea("Reservation for guest '" + guestName + "' removed.");
                            dialog.dispose(); // Close the dialog after successful removal
                        } else {
                            JOptionPane.showMessageDialog(null, "Reservation not found.");
                        }
                    }
                }
            });

            // Add action listener for removing the entire hotel
            removeHotelButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    // Confirm removal of the hotel
                    int confirm = JOptionPane.showConfirmDialog(null, "Are you sure you want to remove this hotel?",
                            "Confirm Removal", JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) {
                        // Remove the hotel from the system
                        controller.removeHotel(hotel);
                        updateTextArea("Hotel '" + hotel.getName() + "' removed.");
                        dialog.dispose(); // Close the dialog after successful removal
                    }
                }
            });

            // Add action listener for adding a date-based price modifier
            addDateModifierButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    // Prompt user for date range and price modifier
                    String startDateStr = JOptionPane.showInputDialog("Enter start date (yyyy-MM-dd):");
                    String endDateStr = JOptionPane.showInputDialog("Enter end date (yyyy-MM-dd):");
                    String modifierStr = JOptionPane.showInputDialog("Enter price modifier (0.50 to 1.50):");

                    if (startDateStr != null && endDateStr != null && modifierStr != null) {
                        try {
                            Date startDate = parseDate(startDateStr);
                            Date endDate = parseDate(endDateStr);
                            double modifier = Double.parseDouble(modifierStr);

                            // Validate the modifier value
                            if (modifier >= 0.5 && modifier <= 1.5) {
                                DatePriceModifier datePriceModifier = new DatePriceModifier(startDate, endDate, modifier);
                                hotel.addDatePriceModifier(datePriceModifier);
                                updateTextArea("Date price modifier added.");
                                JOptionPane.showMessageDialog(null, "Date price modifier added successfully.");
                                dialog.dispose(); // Close the dialog after successful addition
                            } else {
                                JOptionPane.showMessageDialog(null, "Price modifier must be between 0.50 and 1.50.", "Invalid Modifier", JOptionPane.ERROR_MESSAGE);
                            }
                        } catch (NumberFormatException ex) {
                            JOptionPane.showMessageDialog(null, "Invalid input. Please enter valid data.");
                        }
                    }
                }
            });

            // Add action listener for removing a date-based price modifier
            removeDateModifierButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    // Call the SystemController method to handle the removal
                    controller.removeDatePriceModifier(hotel);
                    // Close the current dialog
                    dialog.dispose();
                }
            });

            // Add action listener for exiting the management dialog
            exitButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    dialog.dispose();
                }
            });

            // Add buttons to the button panel
            buttonPanel.add(changeNameButton);
            buttonPanel.add(updatePriceButton);
            buttonPanel.add(addRoomButton);
            buttonPanel.add(removeRoomButton);
            buttonPanel.add(removeReservationButton);
            buttonPanel.add(removeHotelButton);
            buttonPanel.add(addDateModifierButton);
            buttonPanel.add(removeDateModifierButton);
            buttonPanel.add(exitButton);

            // Add the button panel to the dialog and display it
            dialog.add(buttonPanel);
            dialog.setVisible(true);
        }
    }
}


    /**
     * Simulates booking a room by prompting the user for booking details.
     */
    private void simulateBooking() {
        String guestName = JOptionPane.showInputDialog("Enter guest name:");
        String checkInDate = JOptionPane.showInputDialog("Enter check-in date (YYYY-MM-DD):");
        String checkOutDate = JOptionPane.showInputDialog("Enter check-out date (YYYY-MM-DD):");
        String roomName = JOptionPane.showInputDialog("Enter room name:");

        if (guestName != null && checkInDate != null && checkOutDate != null && roomName != null) {
            // Convert dates and handle booking
          //  controller.simulateBooking(guestName, checkInDate, checkOutDate, roomName);
            updateTextArea("Simulated booking for guest '" + guestName + "' in room '" + roomName + "'.");
        }
    }

    /**
     * Exits the application.
     */
    private void exit() {
        JOptionPane.showMessageDialog(null, "Exiting...");
        System.exit(0);
    }

    /**
     * Updates the text area with new messages.
     * 
     * @param message The message to be displayed in the text area.
     */
    private void updateTextArea(String message) {
        textArea.append(message + "\n");
    }

    /**
     * The main method to start the application.
     * 
     * @param args Command-line arguments (not used).
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new HotelGUI());
    }
}
    
