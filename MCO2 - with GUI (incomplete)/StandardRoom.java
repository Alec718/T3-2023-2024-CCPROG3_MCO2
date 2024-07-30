/**
 * The StandardRoom class represents a standard type of room in a hotel.
 * It extends the Room class to provide specific implementations for standard rooms.
 */
public class StandardRoom extends Room {

    /**
     * Constructs a new StandardRoom with the specified name and base price.
     *
     * @param name the name of the room
     * @param basePrice the base price of the room
     */
    public StandardRoom(String name, double basePrice) {
        // Call the constructor of the superclass (Room) with the provided name and base price
        super(name, basePrice);
    }

    /**
     * Returns the base price of the room without any adjustments.
     *
     * @return the base price of the room
     */
    public double getBasePrice() {
        // Return the base price of the room without any adjustment
        return getBasePriceWithoutAdjustment();
    }

    /**
     * Returns a string containing information about the room, including its type.
     *
     * @return a string with room information and type
     */
    public String getRoomInfo() {
        // Call the superclass method to get the base room information
        // Append the room type to the room information
        return super.getRoomInfo() + "\nRoom Type: " + getRoomType();
    }

    /**
     * Returns the type of the room.
     *
     * @return a string representing the room type
     */
    public String getRoomType() {
        // Return the type of the room as "Standard"
        return "Standard";
    }
}

