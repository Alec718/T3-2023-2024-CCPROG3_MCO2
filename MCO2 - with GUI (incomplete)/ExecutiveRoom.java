/**
 * Represents an Executive Room in the hotel.
 * This room type is a subclass of Room with additional pricing rules.
 */
public class ExecutiveRoom extends Room {
    /**
     * Constructs a new Executive Room with the specified name and base price.
     *
     * @param name the name of the room
     * @param basePrice the base price of the room
     */
    public ExecutiveRoom(String name, double basePrice) {
        super(name, basePrice); // Call the constructor of the superclass (Room) to set the name and base price
    }

    /**
     * Gets the base price for the Executive Room.
     * This price includes a 35% increase over the base price.
     *
     * @return the adjusted base price for the Executive Room
     */
    public double getBasePrice() {
        // Calculate and return the base price with a 35% increase
        return getBasePriceWithoutAdjustment() + (getBasePriceWithoutAdjustment() * 0.35); // 35% more expensive
    }

    /**
     * Gets the information about the Executive Room.
     *
     * @return a string containing the room information including its type
     */
    public String getRoomInfo() {
        // Call the superclass method to get base room info and append the room type
        return super.getRoomInfo() + "\nRoom Type: " + getRoomType(); // Include the specific room type in the info
    }

    /**
     * Gets the type of the room.
     *
     * @return the string representation of the room type
     */
    public String getRoomType() {
        return "Executive"; // Return the room type as "Executive"
    }
}
