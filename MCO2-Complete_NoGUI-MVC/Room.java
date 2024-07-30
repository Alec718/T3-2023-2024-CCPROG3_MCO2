import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

    /**
    * Represents a room in a hotel, with a name, base price, and a list of reservations.
    */
    public abstract class Room {
        private String name; // The name of the room
        private double basePrice; // The base price of the room
        private List<Reservation> reservations; // List of reservations for the room

    /**
     * Constructs a Room with the given name and base price.
     *
     * @param name the name of the room
     * @param basePrice the base price of the room
     */
    public Room(String name, double basePrice) {
        this.name = name; // Initialize the room's name
        this.basePrice = basePrice; // Initialize the room's base price
        this.reservations = new ArrayList<>(); // Initialize an empty list of reservations
    }

    /**
     * Checks if the room is available on a specific date.
     *
     * @param date the date to check availability
     * @return true if the room is available, false otherwise
     */
    public boolean isAvailable(Date date) {
        // Loop through the list of reservations
        for (Reservation reservation : reservations) {
            // Check if the reservation conflicts with the given date
            if (reservation.conflictsWith(date)) { // Assuming Reservation class has a conflictsWith method
                return false; // Room is not available
            }
        }
        return true; // Room is available
    }

    /**
     * Checks if the room is available between the check-in and check-out dates.
     *
     * @param checkInDate the check-in date
     * @param checkOutDate the check-out date
     * @return true if the room is available, false otherwise
     */
    public boolean isAvailable(Date checkInDate, Date checkOutDate) {
        // Loop through the list of reservations
        for (Reservation reservation : reservations) {
            // Check if the reservation overlaps with the given check-in and check-out dates
            if (reservation.overlaps(checkInDate, checkOutDate)) {
                return false; // Room is not available
            }
        }
        return true; // Room is available
    }

    /**
     * Adds a reservation to the room.
     *
     * @param reservation the reservation to add
     */
    public void addReservation(Reservation reservation) {
        reservations.add(reservation); // Add the reservation to the list
    }

    /**
     * Removes a reservation from the room.
     *
     * @param reservation the reservation to remove
     */
    public void removeReservation(Reservation reservation) {
        reservations.remove(reservation); // Remove the specified reservation from the list
    }

    /**
     * Gets information about the room.
     *
     * @return a string containing the room name and base price
     */
    public String getRoomInfo() {
        // Return a formatted string with the room's name and base price
        return "Room Name: " + name + "\nBase Price: PHP " + basePrice;
    }

    /**
     * Gets the base price of the room without adjustments.
     *
     * @return the base price of the room
     */
    public double getBasePriceWithoutAdjustment() {
        return basePrice; // Return the base price without any adjustments
    }

    /**
     * Gets the base price of the room with adjustments
     *
     * @return the base price of the room with the adjusted price
     */
    public abstract double getBasePrice(); // Abstract method to be implemented by subclasses

    /**
     * Gets the room type of the room.
     *
     * @return the room type of the room
     */
    public abstract String getRoomType(); // Abstract method to be implemented by subclasses

    /**
     * Sets the base price of the room.
     *
     * @param basePrice the new base price
     */
    public void setBasePrice(double basePrice) {
        this.basePrice = basePrice; // Update the base price of the room
    }

    /**
     * Gets the list of reservations for the room.
     *
     * @return the list of reservations
     */
    public List<Reservation> getReservations() {
        return reservations; // Return the list of reservations
    }

    /**
     * Gets the name of the room.
     *
     * @return the name of the room
     */
    public String getName() {
        return name; // Return the name of the room
    }

    /**
     * Gets the name of the room (overloaded method).
     *
     * @param name the name to check (unused)
     * @return the name of the room
     */
    public String getName(String name) {
        return this.name; // Return the name of the room, ignoring the parameter
    }

    /**
     * Gets the availability of the room for a specific month.
     *
     * @param year the year
     * @param month the month (1-12)
     * @return a list of strings indicating the availability for each day of the month
     */
    public List<String> getAvailabilityForMonth(int year, int month) {
        List<String> availability = new ArrayList<>(); // List to hold availability information
        Calendar calendar = Calendar.getInstance(); // Create a Calendar instance
        calendar.set(year, month - 1, 1); // Set the calendar to the first day of the specified month
        int daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH); // Get the total days in the month

        // Loop through each day of the month
        for (int day = 1; day <= daysInMonth; day++) {
            calendar.set(year, month - 1, day); // Set the calendar to the current day
            Date date = calendar.getTime(); // Get the Date object for the current day
            // Add availability status to the list (Available or Booked)
            availability.add(day + ": " + (isAvailable(date) ? "Available" : "Booked"));
        }
        return availability; // Return the list of availability information
    }
}



