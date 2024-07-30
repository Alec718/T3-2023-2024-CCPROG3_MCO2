import java.util.Date;

/**
 * Represents a reservation in a hotel.
 */
public class Reservation {
    private static int nextId = 1;

    private int id;
    private String guestName;
    private Date checkInDate;
    private Date checkOutDate;
    private Room room;

    /**
     * Constructs a new reservation with the specified details.
     *
     * @param guestName the name of the guest
     * @param checkInDate the check-in date
     * @param checkOutDate the check-out date
     * @param room the room for the reservation
     */
    public Reservation(String guestName, Date checkInDate, Date checkOutDate, Room room) {
        this.id = nextId++;
        this.guestName = guestName;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.room = room;
    }

    /**
     * Constructs a new reservation with the specified details but without specifying a room.
     *
     * @param guestName the name of the guest
     * @param checkInDate the check-in date
     * @param checkOutDate the check-out date
     */
    public Reservation(String guestName, Date checkinDate, Date checkOutDate) {
        this(guestName, checkinDate, checkOutDate, null);
    }

    /**
     * Checks if the reservation overlaps with the specified check-in and check-out dates.
     *
     * @param checkInDate the check-in date to check
     * @param checkOutDate the check-out date to check
     * @return true if the reservation overlaps with the specified dates, false otherwise
     */
    public boolean overlaps(Date checkInDate, Date checkOutDate) {
        return (this.checkInDate.before(checkOutDate) && this.checkOutDate.after(checkInDate));
    }

    /**
     * Gets the ID of the reservation.
     *
     * @return the ID of the reservation
     */
    public int getId() {
        return id;
    }

    /**
     * Gets the name of the guest.
     *
     * @return the name of the guest
     */
    public String getGuestName() {
        return guestName;
    }

    /**
     * Gets the check-in date of the reservation.
     *
     * @return the check-in date
     */
    public Date getCheckInDate() {
        return checkInDate;
    }

    /**
     * Gets the check-out date of the reservation.
     *
     * @return the check-out date
     */
    public Date getCheckOutDate() {
        return checkOutDate;
    }

    /**
     * Gets the room of the reservation.
     *
     * @return the room
     */
    public Room getRoom() {
        return room;
    }

    /**
     * Sets the room for the reservation.
     *
     * @param room the room to set
     */
    public void setRoom(Room room) {
        this.room = room;
    }

    /**
     * Calculates the total price for the reservation based on the room's base price and duration of stay.
     *
     * @return the total price for the reservation
     */
    public double getTotalPrice() {
        // Simplified calculation of total price based on room's base price and duration
        long duration = checkOutDate.getTime() - checkInDate.getTime();
        int nights = (int) (duration / (1000 * 60 * 60 * 24));
        return room.getBasePrice() * nights;
    }

    /**
     * Gets the details of the reservation in a string format.
     *
     * @return the details of the reservation
     */
    public String getDetails() {
        return "Reservation ID: " + id + "\nGuest Name: " + guestName + "\nCheck-in Date: " + checkInDate +
                "\nCheck-out Date: " + checkOutDate + "\nTotal Price: PHP " + getTotalPrice();
    }

    /**
     * Checks if the reservation conflicts with the specified date.
     *
     * @param date the date to check
     * @return true if the reservation conflicts with the specified date, false otherwise
     */
    public boolean conflictsWith(Date date) {
        return !date.before(checkInDate) && !date.after(checkOutDate);
    }
}
