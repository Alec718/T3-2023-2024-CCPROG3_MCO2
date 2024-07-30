import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Scanner;

import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;


public class HotelGUI {
    private SystemController controller;
    private JTextArea textArea;
    private Reservation reservation;

    public HotelGUI() {
        this.controller = new SystemController();
        createAndShowGUI();
    }

    private void createAndShowGUI() {
        // Create the main frame
        JFrame frame = new JFrame("Hotel Management System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 400);
        frame.setLayout(new BorderLayout());

        // Create the text area for displaying messages
        textArea = new JTextArea();
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        frame.add(scrollPane, BorderLayout.CENTER);

        // Create a panel for buttons
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(5, 1));

        // Create buttons
        JButton createHotelButton = new JButton("Create Hotel");
        JButton viewHotelButton = new JButton("View Hotel");
        JButton manageHotelButton = new JButton("Manage Hotel");
        JButton simulateBookingButton = new JButton("Simulate Booking");
        JButton exitButton = new JButton("Exit");

        // Add action listeners to buttons
        createHotelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                createHotel();
            }
        });

        viewHotelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                viewHotel();
            }
        });

        manageHotelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                manageHotel();
            }
        });

        simulateBookingButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                simulateBooking();
            }
        });

        exitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                exit();
            }
        });

        // Add buttons to the panel
        panel.add(createHotelButton);
        panel.add(viewHotelButton);
        panel.add(manageHotelButton);
        panel.add(simulateBookingButton);
        panel.add(exitButton);

        frame.add(panel, BorderLayout.CENTER);
        frame.setVisible(true);
    }

    private void createHotel() {
        String name = JOptionPane.showInputDialog("Enter hotel name:");
        if (name != null && !name.trim().isEmpty()) {
            controller.createHotel(name);
            updateTextArea("Hotel '" + name + "' created.");
            JOptionPane.showMessageDialog(null, "Hotel '" + name + "' created successfully.");
        }
    }

    private void viewHotel() {
        List<String> hotelNames = controller.getHotelNames();
        if (hotelNames.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No hotels available. Please create a hotel first.");
            return;
        }
    
        String selectedHotelName = (String) JOptionPane.showInputDialog(
                null,
                "Select a hotel:",
                "View Hotel",
                JOptionPane.QUESTION_MESSAGE,
                null,
                hotelNames.toArray(),
                hotelNames.get(0)
        );
    
        if (selectedHotelName != null) {
            Hotel hotel = controller.getHotelByName(selectedHotelName);
            if (hotel != null) {
                JDialog dialog = new JDialog();
                dialog.setTitle("View Hotel Information");
                dialog.setSize(300, 200);
                dialog.setLayout(new GridLayout(3, 1));
    
                JLabel label = new JLabel("Choose the information level:");
                dialog.add(label);
    
                JButton highLevelButton = new JButton("High-level information");
                highLevelButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        String highLevelInfo = hotel.getHighLevelInfo(); // Assuming getHighLevelInfo() returns formatted string
                        JOptionPane.showMessageDialog(dialog, highLevelInfo, "High-level Information", JOptionPane.INFORMATION_MESSAGE);
                        updateTextArea("Viewed high-level information for hotel '" + hotel.getName() + "'.");
                    }
                });
    
                JButton lowLevelButton = new JButton("Low-level information");
                lowLevelButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        viewLowLevelInfo(hotel);
                        dialog.dispose();
                    }
                });
    
                dialog.add(highLevelButton);
                dialog.add(lowLevelButton);
    
                dialog.setVisible(true);
            }
        }
    }
    
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

private void viewRoomsAvailability(Hotel hotel, Scanner scanner) {
    // Prompt user for the date to check room availability
    String dateStr = JOptionPane.showInputDialog("Enter date to check (yyyy-MM-dd):");
    Date selectedDate = parseDate(dateStr);

    // Get room availability
    int totalAvailableRooms = hotel.getTotalAvailableRooms(selectedDate);
    int totalBookedRooms = hotel.getTotalBookedRooms(selectedDate);
    List<String> availableRoomNames = hotel.getAvailableRoomNames(selectedDate);

    // Display the information
    String message = "Rooms Availability on " + dateStr + ":\n" +
                     "Total rooms: " + hotel.getTotalRooms() + "\n" +
                     "Available rooms: " + totalAvailableRooms + "\n" +
                     "Booked rooms: " + totalBookedRooms + "\n" +
                     "Available rooms names:\n" + String.join("\n", availableRoomNames);

    JOptionPane.showMessageDialog(null, message);
}

private void viewRoomDetails(Hotel hotel, Scanner scanner) {
    // Prompt for room name
    String roomName = JOptionPane.showInputDialog("Enter room name:");
    Room room = hotel.getRoomInfo(roomName);

    if (room != null) {
        String message = "Room Information:\n" +
                         "Room Name: " + room.getName() + "\n" +
                         "Room Type: " + room.getRoomType() + "\n" +
                         "Base Price: PHP " + room.getBasePrice();

        // Ask for year and month
        int year = Integer.parseInt(JOptionPane.showInputDialog("Choose a year (e.g., 2024):"));
        int month = Integer.parseInt(JOptionPane.showInputDialog("Choose a month (1-12):"));
        
        List<String> availability = room.getAvailabilityForMonth(year, month);
        message += "\nRoom Availability for " + month + "/" + year + ":\n" +
                   String.join("\n", availability);

        JOptionPane.showMessageDialog(null, message);
    } else {
        JOptionPane.showMessageDialog(null, "Room not found.");
    }
}

private void viewReservationDetails(Hotel hotel, Scanner scanner) {
    String guestName = JOptionPane.showInputDialog("Enter guest name to find reservation:");
    controller.findReservationByGuestName(hotel, guestName);

    if (controller != null) {
        JOptionPane.showMessageDialog(null, "Reservation Details:\n" + reservation.getDetails());
    } else {
        JOptionPane.showMessageDialog(null, "Reservation not found.");
    }
}


    
    

private void manageHotel() {
    List<String> hotelNames = controller.getHotelNames();
    if (hotelNames.isEmpty()) {
        JOptionPane.showMessageDialog(null, "No hotels available. Please create a hotel first.");
        return;
    }

    String selectedHotelName = (String) JOptionPane.showInputDialog(
            null,
            "Select a hotel to manage:",
            "Manage Hotel",
            JOptionPane.QUESTION_MESSAGE,
            null,
            hotelNames.toArray(),
            hotelNames.get(0)
    );

    if (selectedHotelName != null) {
        Hotel hotel = controller.getHotelByName(selectedHotelName);
        if (hotel != null) {
            JDialog dialog = new JDialog((JFrame) null, "Hotel Management Options", true);
            dialog.setSize(700, 400);
            dialog.setLayout(new GridLayout(3, 1));

            JPanel buttonPanel = new JPanel();
            buttonPanel.setLayout(new GridLayout(0, 3, 10, 10));

            JButton changeNameButton = new JButton("Change Hotel Name");
            JButton updatePriceButton = new JButton("Update Base Price");
            JButton addRoomButton = new JButton("Add Room");
            JButton removeRoomButton = new JButton("Remove Room");
            JButton removeReservationButton = new JButton("Remove Reservation");
            JButton removeHotelButton = new JButton("Remove Hotel");
            JButton addDateModifierButton = new JButton("Add Date Price Modifier");
            JButton removeDateModifierButton = new JButton("Remove Date Price Modifier");
            JButton exitButton = new JButton("Exit");

            changeNameButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    String newName = JOptionPane.showInputDialog("Enter new hotel name:");
                    if (newName != null && !newName.trim().isEmpty()) {
                        controller.changeHotelName(hotel, newName);
                        updateTextArea("Hotel name changed to '" + newName + "'.");
                        
                        JOptionPane.showMessageDialog(null, "Hotel name changed to '" + hotel.getName() + "'");
                        dialog.dispose();
                    }
                }
            });

            updatePriceButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    String newPriceStr = JOptionPane.showInputDialog("Enter new base price:");
                    if (newPriceStr != null) {
                        try {
                            double newPrice = Double.parseDouble(newPriceStr);
                            controller.updateHotelBasePrice(hotel, newPrice);
                            updateTextArea("Hotel base price updated to '" + newPrice + "'.");
                            dialog.dispose();
                        } catch (NumberFormatException ex) {
                            JOptionPane.showMessageDialog(null, "Invalid price. Please enter a valid number.");
                        }
                    }
                }
            });

            addRoomButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    String roomName = JOptionPane.showInputDialog("Enter room name:");
                    if (roomName != null && !roomName.trim().isEmpty()) {
                        double roomBasePrice = hotel.getBasePrice();
                        String[] roomTypes = {"Standard", "Deluxe", "Executive"};
                        String roomType = (String) JOptionPane.showInputDialog(null, "Select room type:",
                                "Room Type", JOptionPane.QUESTION_MESSAGE, null, roomTypes, roomTypes[0]);
                        if (roomType != null) {
                            controller.addRoomToHotel(hotel, roomName, roomBasePrice, roomType);
                            updateTextArea("Room '" + roomName + "' added to " + hotel.getName());
                            
                            // Show confirmation dialog
                            JOptionPane.showMessageDialog(null, "Room '" + roomName + "' added to " + hotel.getName());
                            
                            dialog.dispose();
                        }
                    }
                }
            });
            

            removeRoomButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    String roomName = JOptionPane.showInputDialog("Enter room name to remove:");
                    if (roomName != null && !roomName.trim().isEmpty()) {
                        Room roomToRemove = hotel.getRoomInfo(roomName);
                        if (roomToRemove != null) {
                            controller.removeRoomFromHotel(hotel, roomToRemove);
                            updateTextArea("Room '" + roomName + "' removed from hotel.");
                            JOptionPane.showMessageDialog(null, "Room '" + roomName + "' removed to " + hotel.getName());
                            dialog.dispose();
                        } else {
                            JOptionPane.showMessageDialog(null, "Room not found.");
                        }
                    }
                }
            });

            removeReservationButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    String guestName = JOptionPane.showInputDialog("Enter guest name to remove reservation:");
                    if (guestName != null && !guestName.trim().isEmpty()) {
                        Reservation reservationToRemove = controller.findReservationByGuestName(hotel, guestName);
                        if (reservationToRemove != null) {
                            Room roomOfReservationToRemove = reservationToRemove.getRoom();
                            controller.removeReservationFromRoom(hotel, roomOfReservationToRemove, reservationToRemove);
                            updateTextArea("Reservation for guest '" + guestName + "' removed.");
                            dialog.dispose();
                        } else {
                            JOptionPane.showMessageDialog(null, "Reservation not found.");
                        }
                    }
                }
            });

            removeHotelButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    int confirm = JOptionPane.showConfirmDialog(null, "Are you sure you want to remove this hotel?",
                            "Confirm Removal", JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) {
                        controller.removeHotel(hotel);
                        updateTextArea("Hotel '" + hotel.getName() + "' removed.");
                        dialog.dispose();
                    }
                }
            });

            addDateModifierButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    String startDateStr = JOptionPane.showInputDialog("Enter start date (yyyy-mm-dd):");
                    String endDateStr = JOptionPane.showInputDialog("Enter end date (yyyy-mm-dd):");
                    String modifierStr = JOptionPane.showInputDialog("Enter price modifier (0.50 to 1.50):");

                    if (startDateStr != null && endDateStr != null && modifierStr != null) {
                        try {
                            Date startDate = parseDate(startDateStr);
                            Date endDate = parseDate(endDateStr);
                            double modifier = Double.parseDouble(modifierStr);
                            DatePriceModifier datePriceModifier = new DatePriceModifier(startDate, endDate, modifier);
                            hotel.addDatePriceModifier(datePriceModifier);
                            updateTextArea("Date price modifier added.");
                            dialog.dispose();
                        } catch (NumberFormatException ex) {
                            JOptionPane.showMessageDialog(null, "Invalid input. Please enter valid data.");
                        }
                    }
                }
            });

            removeDateModifierButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    String startDateStr = JOptionPane.showInputDialog("Enter start date of modifier to remove (yyyy-mm-dd):");
                    if (startDateStr != null) {
                        Date startDate = parseDate(startDateStr);
                        for (DatePriceModifier modifier : hotel.getDatePriceModifiers()) {
                            if (modifier.getStartDate().equals(startDate)) {
                                hotel.removeDatePriceModifier(modifier);
                                updateTextArea("Date price modifier removed.");
                                dialog.dispose();
                                return;
                            }
                        }
                        JOptionPane.showMessageDialog(null, "No date price modifier found for the given start date.");
                    }
                }
            });

            exitButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    dialog.dispose();
                }
            });

            buttonPanel.add(changeNameButton);
            buttonPanel.add(updatePriceButton);
            buttonPanel.add(addRoomButton);
            buttonPanel.add(removeRoomButton);
            buttonPanel.add(removeReservationButton);
            buttonPanel.add(removeHotelButton);
            buttonPanel.add(addDateModifierButton);
            buttonPanel.add(removeDateModifierButton);
            buttonPanel.add(exitButton);

            dialog.add(buttonPanel);
            dialog.setVisible(true);
        }
    }
}

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

    private void exit() {
        JOptionPane.showMessageDialog(null, "Exiting...");
        System.exit(0);
    }

    private void updateTextArea(String message) {
        textArea.append(message + "\n");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new HotelGUI());
    }
}
