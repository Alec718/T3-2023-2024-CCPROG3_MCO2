import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Represents a hotel with rooms and reservations.
 */
public class Hotel {
    private String name; // Name of the hotel
    private List<Room> rooms; // List of rooms in the hotel
    private List<Reservation> reservations; // List of reservations made in the hotel
    private double totalEarnings; // Total earnings from reservations
    private List<DatePriceModifier> datePriceModifiers; // List of date-based price modifiers

    /**
     * Constructs a Hotel with the given name.
     *
     * @param name the name of the hotel
     */
    public Hotel(String name) {
        this.name = name;
        this.rooms = new ArrayList<>(); // Initialize the list of rooms
        this.reservations = new ArrayList<>(); // Initialize the list of reservations
        this.datePriceModifiers = new ArrayList<>(); // Initialize the list of date price modifiers
    }

    /**
     * Adds a date price modifier to the hotel.
     *
     * @param modifier the date price modifier to add
     */
    public void addDatePriceModifier(DatePriceModifier modifier) {
        datePriceModifiers.add(modifier); // Add the modifier to the list
    }

    /**
     * Removes a date price modifier from the hotel.
     *
     * @param modifier the date price modifier to remove
     */
    public void removeDatePriceModifier(DatePriceModifier modifier) {
        datePriceModifiers.remove(modifier); // Remove the modifier from the list
    }

    /**
     * Gets the list of date price modifiers.
     *
     * @return the list of date price modifiers
     */
    public List<DatePriceModifier> getDatePriceModifiers() {
        return datePriceModifiers; // Return the list of modifiers
    }

    /**
     * Gets the date price modifier applicable for a given date.
     *
     * @param date the date to check
     * @return the applicable date price modifier, or null if none found
     */
    public DatePriceModifier getDatePriceModifier(Date date) {
        for (DatePriceModifier modifier : datePriceModifiers) {
            if (modifier.isDateInRange(date)) { // Check if the modifier applies to the given date
                return modifier; // Return the found modifier
            }
        }
        return null; // Return null if no modifier is found for the given date
    }

    /**
     * Gets the price modifier for a specific date.
     *
     * @param date the date to check
     * @return the price modifier for the date, defaults to 1.0 if none found
     */
    public double getModifierForDate(Date date) {
        for (DatePriceModifier modifier : datePriceModifiers) {
            if (modifier.isDateInRange(date)) { // Check if the modifier applies to the given date
                return modifier.getModifier(); // Return the modifier value
            }
        }
        return 1.0; // Default modifier is 1.0 (100%)
    }

    /**
     * Adds a room to the hotel.
     *
     * @param name the name of the room
     * @param basePrice the base price of the room
     * @param roomType the type of the room (Standard, Deluxe, Executive)
     */
    public void addRoom(String name, double basePrice, String roomType) {
        Room room; // Variable to hold the new room

        // Create the room based on the specified room type
        switch (roomType) {
            case "Standard":
                room = new StandardRoom(name, basePrice); // Create a StandardRoom
                break;
            case "Deluxe":
                room = new DeluxeRoom(name, basePrice); // Create a DeluxeRoom
                break;
            case "Executive":
                room = new ExecutiveRoom(name, basePrice); // Create an ExecutiveRoom
                break;
            default:
                System.out.println("Invalid room type."); // Error message for invalid type
                return; // Exit if invalid type is provided
        }
        rooms.add(room); // Add the created room to the hotel's room list
    }

    /**
     * Removes a room from the hotel.
     *
     * @param room the room to remove
     */
    public void removeRoom(Room room) {
        // Check if the room is available for removal (no active reservations)
        if (room.isAvailable(null)) {
            rooms.remove(room); // Remove the room from the list

        } else {
            System.out.println("Cannot remove room. It has active reservations."); // Error message
        }
    }

    /**
     * Selects an available room for booking based on the check-in and check-out dates.
     *
     * @param checkInDate  the check-in date
     * @param checkOutDate the check-out date
     * @return the selected room, or null if no rooms are available
     */
    public Room selectRoomForBooking(Date checkInDate, Date checkOutDate) {
        for (Room room : rooms) {
            // Check if the room is available for the specified dates
            if (room.isAvailable(checkInDate, checkOutDate)) {
                return room; // Return the first available room found
            }
        }
        return null; // No available rooms for the specified dates
    }

    /**
     * Finds a reservation by check-in and check-out dates.
     *
     * @param checkInDate  the check-in date
     * @param checkOutDate the check-out date
     * @return the reservation, or null if not found
     */
    public Reservation findReservationByDates(Date checkInDate, Date checkOutDate) {
        for (Room room : rooms) {
            // Check each reservation in the room for matching dates
            for (Reservation reservation : room.getReservations()) {
                if (reservation.getCheckInDate().equals(checkInDate) && 
                    reservation.getCheckOutDate().equals(checkOutDate)) {
                    return reservation; // Return the found reservation
                }
            }
        }
        return null; // Reservation not found for the specified dates
    }

    /**
     * Changes the name of the hotel.
     *
     * @param newName the new name of the hotel
     */
    public void changeName(String newName) {
        this.name = newName; // Update the hotel's name
    }

    /**
     * Gets high-level information about the hotel.
     *
     * @return a string containing high-level information about the hotel
     */
    public String getHighLevelInfo() {
        // Compile and return high-level information about the hotel
        return "Hotel Name: " + name + 
               "\nTotal Rooms: " + rooms.size() + 
               "\nTotal Earnings: PHP " + totalEarnings;
    }

    /**
     * Gets low-level information about the hotel.
     *
     * @return a string containing low-level information about the hotel
     */
    public String getLowLevelInfo() {
        StringBuilder info = new StringBuilder(); // StringBuilder for efficient string concatenation
        info.append("Hotel Name: ").append(name).append("\n");
        info.append("Total Rooms: ").append(rooms.size()).append("\n");

        // Calculate total available and booked rooms
        int totalAvailable = 0;
        int totalBooked = 0;
        Date today = new Date(); // Assuming today's date

        for (Room room : rooms) {
            // Count available and booked rooms based on today's date
            totalAvailable += room.isAvailable(today) ? 1 : 0; // Increment available count
            totalBooked += room.isAvailable(today) ? 0 : 1; // Increment booked count
        }

        // Append availability information to the string
        info.append("Total Available Rooms: ").append(totalAvailable).append("\n");
        info.append("Total Booked Rooms: ").append(totalBooked).append("\n");

        // Display information about the first room (if available)
        if (!rooms.isEmpty()) {
            Room firstRoom = rooms.get(0); // Get the first room
            info.append("Room Info: ").append(firstRoom.getRoomInfo()).append("\n"); // Append room info
        }

        // Display information about the first reservation (if available)
        if (!reservations.isEmpty()) {
            Reservation firstReservation = reservations.get(0); // Get the first reservation
            info.append("Reservation Info: ").append(firstReservation.getDetails()).append("\n"); // Append reservation info
        }

        return info.toString(); // Return the low-level information as a string
    }

    /**
     * Updates the base price for all rooms in the hotel.
     *
     * @param newPrice the new base price
     */
    public void updateBasePrice(double newPrice) {
        for (Room room : rooms) {
            room.setBasePrice(newPrice); // Update each room's base price
        }
    }

    /**
     * Removes a reservation from the hotel.
     *
     * @param reservation the reservation to remove
     */
    public void removeReservation(Reservation reservation) {
        reservations.remove(reservation); // Remove the reservation from the list
    }

    /**
     * Simulates booking a room for a guest.
     *
     * @param guestName    the name of the guest
     * @param checkInDate  the check-in date
     * @param checkOutDate the check-out date
     * @param selectedRoom the room selected for booking
     * @return the reservation, or null if the room is not available
     */
    public Reservation simulateBooking(String guestName, Date checkInDate, Date checkOutDate, Room selectedRoom, Hotel hotel) {
        // Check if the selected room is available for the given dates
        if (selectedRoom != null && selectedRoom.isAvailable(checkInDate, checkOutDate)) {
            // Create a new reservation and add it to the list
            Reservation reservation = new Reservation(guestName, checkInDate, checkOutDate, selectedRoom, hotel);
            reservations.add(reservation); // Add the reservation to the list
            totalEarnings += reservation.getTotalPrice(); // Update total earnings
            return reservation; // Return the created reservation
        } else {
            System.out.println("Selected room is not available for the given dates. We apologize."); // Error message
            return null; // Return null if the room is not available
        }
    }

    /**
     * Gets the names of all available rooms on a specific date.
     *
     * @param date the date to check availability
     * @return a list of available room names
     */
    public List<String> getAvailableRoomNames(Date date) {
        List<String> availableRooms = new ArrayList<>(); // List to store names of available rooms
        for (Room room : rooms) {
            if (room.isAvailable(date)) { // Check if the room is available on the given date
                availableRooms.add(room.getName()); // Add the room name to the list
            }
        }
        return availableRooms; // Return the list of available room names
    }

    /**
     * Gets the total number of available rooms on a specific date.
     *
     * @param date the date to check availability
     * @return the total number of available rooms
     */
    public int getTotalAvailableRooms(Date date) {
        int count = 0; // Initialize count of available rooms
        for (Room room : rooms) {
            if (room.isAvailable(date)) { // Check if the room is available on the given date
                count++; // Increment the count
            }
        }
        return count; // Return the total number of available rooms
    }

    /**
     * Gets the names of all available rooms between the check-in and check-out dates.
     *
     * @param checkInDate  the check-in date
     * @param checkOutDate the check-out date
     * @return a list of available room names
     */
    public List<String> getAvailableRoomNames(Date checkInDate, Date checkOutDate) {
        List<String> availableRoomNames = new ArrayList<>(); // List to store names of available rooms
        for (Room room : rooms) {
            if (room.isAvailable(checkInDate, checkOutDate)) { // Check if the room is available for the date range
                availableRoomNames.add(room.getName()); // Add the room name to the list
            }
        }
        return availableRoomNames; // Return the list of available room names
    }

    /**
     * Gets the total number of booked rooms on a specific date.
     *
     * @param date the date to check
     * @return the total number of booked rooms
     */
    public int getTotalBookedRooms(Date date) {
        return rooms.size() - getTotalAvailableRooms(date); // Total booked rooms is total rooms minus available rooms
    }

    /**
     * Gets the name of the hotel.
     *
     * @return the name of the hotel
     */
    public String getName() {
        return name; // Return the hotel's name
    }

    /**
     * Gets the total number of rooms in the hotel.
     *
     * @return the total number of rooms
     */
    public int getTotalRooms() {
        return rooms.size(); // Return the size of the rooms list
    }

    /**
     * Gets the monthly earnings of the hotel.
     *
     * @return the monthly earnings
     */
    public double getMonthlyEarnings() {
        // Simplified monthly earnings calculation
        return totalEarnings; // Return total earnings
    }

    /**
     * Gets information about a room by its name.
     *
     * @param roomName the name of the room
     * @return the room, or null if not found
     */
    public Room getRoomInfo(String roomName) {
        for (Room room : rooms) {
            if (room.getName().equalsIgnoreCase(roomName)) { // Check if room name matches (case insensitive)
                return room; // Return the found room
            }
        }
        return null; // Return null if room with given name is not found
    }

    /**
     * Gets a list of all rooms in the hotel.
     *
     * @return the list of rooms
     */
    public List<Room> getRooms() {
        return rooms; // Return the list of rooms
    }

    /**
     * Gets the base price for rooms.
     *
     * @return the base price
     */
    public double getBasePrice() {
        return 1299.0; // Return the base price for rooms
    }
}
