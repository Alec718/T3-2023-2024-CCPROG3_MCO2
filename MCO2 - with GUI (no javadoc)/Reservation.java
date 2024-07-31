import java.util.Date;
import java.util.Calendar;
import java.util.List;


/**
 * Represents a reservation in a hotel.
 */
public class Reservation {
    private static int nextId = 1; // Static variable to generate unique reservation IDs

    private int id; // Unique identifier for the reservation
    private String guestName; // Name of the guest making the reservation
    private Date checkInDate; // Check-in date for the reservation
    private Date checkOutDate; // Check-out date for the reservation
    private Room room; // Room associated with the reservation
    private String discountCode; // Discount code applied to the reservation
    private Hotel hotel; // Hotel where the reservation is made

    private double finalPrice; // To store the final price

    /**
     * Constructs a new reservation with the specified details.
     *
     * @param guestName the name of the guest
     * @param checkInDate the check-in date
     * @param checkOutDate the check-out date
     * @param room the room for the reservation
     */
    public Reservation(String guestName, Date checkInDate, Date checkOutDate, Room room) {
        this.id = nextId++; // Assign a unique ID and increment the counter
        this.guestName = guestName; // Set the guest name
        this.checkInDate = checkInDate; // Set the check-in date
        this.checkOutDate = checkOutDate; // Set the check-out date
        this.room = room; // Set the room for the reservation
    }

    /**
     * Constructs a new reservation with the specified details and hotel information.
     *
     * @param guestName the name of the guest
     * @param checkInDate the check-in date
     * @param checkOutDate the check-out date
     * @param room the room for the reservation
     * @param hotel the hotel where the reservation is made
     */
    public Reservation(String guestName, Date checkInDate, Date checkOutDate, Room room, Hotel hotel) {
        this.id = nextId++; // Assign a unique ID and increment the counter
        this.guestName = guestName; // Set the guest name
        this.checkInDate = checkInDate; // Set the check-in date
        this.checkOutDate = checkOutDate; // Set the check-out date
        this.room = room; // Set the room for the reservation
        this.hotel = hotel; // Initialize hotel information
    }

    /**
     * Constructs a new reservation with the specified details but without specifying a room.
     *
     * @param guestName the name of the guest
     * @param checkInDate the check-in date
     * @param checkOutDate the check-out date
     */
    public Reservation(String guestName, Date checkInDate, Date checkOutDate) {
        this(guestName, checkInDate, checkOutDate, null); // Call the other constructor with null room
    }

    /**
     * Checks if the reservation overlaps with the specified check-in and check-out dates.
     *
     * @param checkInDate the check-in date to check
     * @param checkOutDate the check-out date to check
     * @return true if the reservation overlaps with the specified dates, false otherwise
     */
    public boolean overlaps(Date checkInDate, Date checkOutDate) {
        // Check if the reservation period overlaps with the given dates
        return (this.checkInDate.before(checkOutDate) && this.checkOutDate.after(checkInDate));
    }

    /**
     * Gets the ID of the reservation.
     *
     * @return the ID of the reservation
     */
    public int getId() {
        return id; // Return the unique ID of the reservation
    }

    /**
     * Gets the name of the guest.
     *
     * @return the name of the guest
     */
    public String getGuestName() {
        return guestName; // Return the guest's name
    }

    /**
     * Gets the check-in date of the reservation.
     *
     * @return the check-in date
     */
    public Date getCheckInDate() {
        return checkInDate; // Return the check-in date
    }

    /**
     * Gets the check-out date of the reservation.
     *
     * @return the check-out date
     */
    public Date getCheckOutDate() {
        return checkOutDate; // Return the check-out date
    }

    /**
     * Gets the room of the reservation.
     *
     * @return the room
     */
    public Room getRoom() {
        return room; // Return the room associated with the reservation
    }

    /**
     * Sets the room for the reservation.
     *
     * @param room the room to set
     */
    public void setRoom(Room room) {
        this.room = room; // Update the room for this reservation
    }

    /**
     * Calculates the total price for the reservation based on the room's base price and duration of stay.
     *
     * @return the total price for the reservation
     */
    public double getTotalPrice() {
        double totalPrice = 0.0; // Initialize total price
        long duration = checkOutDate.getTime() - checkInDate.getTime(); // Calculate the duration in milliseconds
        int nights = (int) (duration / (1000 * 60 * 60 * 24)); // Convert duration to number of nights

        Calendar calendar = Calendar.getInstance(); // Create a calendar instance
        calendar.setTime(checkInDate); // Set the calendar to the check-in date

        // Iterate over each night of the stay
        for (int i = 0; i < nights; i++) {
            Date currentDate = calendar.getTime(); // Get the current date
            double modifier = hotel.getModifierForDate(currentDate); // Get price modifier for the current date
            totalPrice += room.getBasePrice() * modifier; // Add to total price
            calendar.add(Calendar.DATE, 1); // Move to the next date
        }

        return totalPrice; // Return the total price before discount
    }

    public void setFinalPrice(double finalPrice) {
        this.finalPrice = finalPrice; // Set the final price after applying discounts
    }

    public double getFinalPrice() {
        return finalPrice; // Get the final price
    }

    /**
     * Gets the details of the reservation in a string format.
     *
     * @return the details of the reservation
     */
    public String getDetails() {
        long durationOfStay = checkOutDate.getTime() - checkInDate.getTime(); // Calculate the duration in milliseconds
        int nights = (int) (durationOfStay / (1000 * 60 * 60 * 24)); // Convert duration to number of nights

        // Return formatted reservation details
        return "Reservation ID: " + id + "\nGuest Name: " + guestName + 
               "\nCheck-in Date: " + checkInDate + "\nCheck-out Date: " + checkOutDate + 
               "\nNumber of nights: " + nights + "\nRoom Name: " + room.getName() + 
               "\nRoom Type: " + room.getRoomType() + "\nDiscount code applied: " + getDiscountCode() + 
               "\nRoom Price: PHP " + room.getBasePrice() + "\nTotal Price: PHP " + getFinalPrice();
    }


    /**
     * Checks if the reservation conflicts with the specified date.
     *
     * @param date the date to check
     * @return true if the reservation conflicts with the specified date, false otherwise
     */
    public boolean conflictsWith(Date date) {
        // Check if the date falls within the reservation period
        return !date.before(checkInDate) && !date.after(checkOutDate);
    }

    /**
     * Sets the discount code for the reservation.
     *
     * @param discountCode the discount code to set
     */
    public void setDiscountCode(String discountCode) {
        this.discountCode = discountCode; // Update the discount code
    }

    /**
     * Gets the discount code for the reservation.
     *
     * @return the discount code, or "N/A" if none is set
     */
    public String getDiscountCode() {
        // Return the discount code or "N/A" if not set
        return discountCode != null ? discountCode : "N/A";
    }

    /**
     * Applies the discount based on the discount code to the given total price.
     *
     * @param totalPrice the initial total price
     * @return the discounted total price
     */
    public double applyDiscount(double totalPrice) {
        // Check if a discount code is set
        if (discountCode == null) {
            return totalPrice; // No discount applied
        }

        // Apply discount based on the provided discount code
        switch (discountCode) {
            case "I_WORK_HERE":
                totalPrice *= 0.90; // Apply a flat 10% discount
                System.out.println("Discount code applied!"); // Notify the user
                break;

            case "STAY4_GET1":
                long durationOfStay = checkOutDate.getTime() - checkInDate.getTime(); // Calculate stay duration
                int nights = (int) (durationOfStay / (1000 * 60 * 60 * 24)); // Convert duration to number of nights
                if (nights >= 5) {
                    totalPrice -= room.getBasePrice(); // Apply discount for 5 nights or more
                    System.out.println("Discount code applied!"); // Notify the user
                } else {
                    System.out.println("Your stay does not exceed or reach 5 nights."); // Notify the user
                }
                break;

            case "PAYDAY":
                Calendar checkIn = Calendar.getInstance(); // Create a calendar instance for check-in
                checkIn.setTime(checkInDate); // Set to check-in date
                Calendar checkOut = Calendar.getInstance(); // Create a calendar instance for check-out
                checkOut.setTime(checkOutDate); // Set to check-out date

                boolean itCoversPayDay = false; // Flag to check for payday
                // Iterate over each day of the stay period
                while (checkIn.before(checkOut)) {
                    int day = checkIn.get(Calendar.DAY_OF_MONTH); // Get the current day of the month
                    // Check if the current day is a payday (15th or 30th)
                    if (day == 15 || day == 30) {
                        itCoversPayDay = true; // Set flag to true if payday is covered
                        break; // Exit the loop if a payday is found
                    }
                    checkIn.add(Calendar.DAY_OF_MONTH, 1); // Move to the next day
                }

                // Apply a 7% discount on payday if covered
                if (itCoversPayDay) {
                    totalPrice *= 0.93; // Apply a 7% discount
                    System.out.println("Discount code applied!"); // Notify the user
                } else {
                    System.out.println("Your check-in date does not land on any payday."); // Notify the user
                }
                break;

            default:
                System.out.println("Invalid discount code."); // Notify if discount code is invalid
                break;
        }

        return totalPrice; // Return the final discounted price
    }
}
