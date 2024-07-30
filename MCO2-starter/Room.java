import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Represents a room in a hotel, with a name, base price, and a list of reservations.
 */
public class Room {
    private String name;
    private double basePrice;
    private List<Reservation> reservations;

    /**
     * Constructs a Room with the given name and base price.
     *
     * @param name the name of the room
     * @param basePrice the base price of the room
     */
    public Room(String name, double basePrice) {
        this.name = name;
        this.basePrice = basePrice;
        this.reservations = new ArrayList<>();
    }

    /**
     * Checks if the room is available on a specific date.
     *
     * @param date the date to check availability
     * @return true if the room is available, false otherwise
     */
    public boolean isAvailable(Date date) {
        for (Reservation reservation : reservations) {
            if (reservation.conflictsWith(date)) { // Assuming Reservation class has conflictsWith method
                return false;
            }
        }
        return true;
    }

    /**
     * Checks if the room is available between the check-in and check-out dates.
     *
     * @param checkInDate the check-in date
     * @param checkOutDate the check-out date
     * @return true if the room is available, false otherwise
     */
    public boolean isAvailable(Date checkInDate, Date checkOutDate) {
        for (Reservation reservation : reservations) {
            if (reservation.overlaps(checkInDate, checkOutDate)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Adds a reservation to the room.
     *
     * @param reservation the reservation to add
     */
    public void addReservation(Reservation reservation) {
        reservations.add(reservation);
    }

    /**
     * Removes a reservation from the room.
     *
     * @param reservation the reservation to remove
     */
    public void removeReservation(Reservation reservation) {
        reservations.remove(reservation);
    }

    /**
     * Gets information about the room.
     *
     * @return a string containing the room name and base price
     */
    public String getRoomInfo() {
        return "Room Name: " + name + "\nBase Price: PHP " + basePrice;
    }

    /**
     * Gets the base price of the room.
     *
     * @return the base price of the room
     */
    public double getBasePrice() {
        return basePrice;
    }

    /**
     * Sets the base price of the room.
     *
     * @param basePrice the new base price
     */
    public void setBasePrice(double basePrice) {
        this.basePrice = basePrice;
    }

    /**
     * Gets the list of reservations for the room.
     *
     * @return the list of reservations
     */
    public List<Reservation> getReservations() {
        return reservations;
    }

    /**
     * Gets the name of the room.
     *
     * @return the name of the room
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the name of the room (overloaded method).
     *
     * @param name the name to check
     * @return the name of the room
     */
    public String getName(String name) {
        return this.name;
    }

    /**
     * Gets the availability of the room for a specific month.
     *
     * @param year the year
     * @param month the month (1-12)
     * @return a list of strings indicating the availability for each day of the month
     */
    public List<String> getAvailabilityForMonth(int year, int month) {
        List<String> availability = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, 1);
        int daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        for (int day = 1; day <= daysInMonth; day++) {
            calendar.set(year, month - 1, day);
            Date date = calendar.getTime();
            availability.add(day + ": " + (isAvailable(date) ? "Available" : "Booked"));
        }
        return availability;
    }
}



