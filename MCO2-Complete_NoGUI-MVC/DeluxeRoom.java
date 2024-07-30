/**
 * Represents a deluxe room in the hotel.
 */
public class DeluxeRoom extends Room {
    
    /**
     * Constructs a new DeluxeRoom with the specified name and base price.
     *
     * @param name the name of the room
     * @param basePrice the base price of the room
     */
    public DeluxeRoom(String name, double basePrice) {
        super(name, basePrice); // Call the constructor of the superclass (Room)
    }

    /**
     * Gets the base price of the deluxe room, including a 20% increase.
     *
     * @return the adjusted base price of the room
     */
    public double getBasePrice() {
        // Return the base price plus an additional 20%
        return getBasePriceWithoutAdjustment() + (getBasePriceWithoutAdjustment() * 0.20);
    }

    /**
     * Gets information about the deluxe room, including its type.
     *
     * @return a string containing room information
     */
    public String getRoomInfo() {
        // Call the superclass method to get room information and append the room type
        return super.getRoomInfo() + "\nRoom Type: " + getRoomType();
    }

    /**
     * Gets the type of the room.
     *
     * @return a string representing the room type
     */
    public String getRoomType() {
        return "Deluxe"; // Return the string "Deluxe" indicating the room type
    }
}
