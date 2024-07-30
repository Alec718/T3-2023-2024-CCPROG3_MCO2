import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Represents a hotel with rooms and reservations.
 */
public class Hotel {
    private String name;
    private List<Room> rooms;
    private List<Reservation> reservations;
    private double totalEarnings;

    /**
     * Constructs a Hotel with the given name.
     *
     * @param name the name of the hotel
     */
    public Hotel(String name) {
        this.name = name;
        this.rooms = new ArrayList<>();
        this.reservations = new ArrayList<>();
        this.totalEarnings = 0.0;
    }

    /**
     * Adds a room to the hotel.
     *
     * @param name the name of the room
     */
    public void addRoom(String name) {
        double basePrice = getBasePrice();
        Room room = new Room(name, basePrice);
        rooms.add(room);
    }

    /**
     * Removes a room from the hotel.
     *
     * @param room the room to remove
     */
    public void removeRoom(Room room) {
        rooms.remove(room);
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
            if (room.isAvailable(checkInDate, checkOutDate)) {
                return room;
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
            for (Reservation reservation : room.getReservations()) {
                if (reservation.getCheckInDate().equals(checkInDate) && reservation.getCheckOutDate().equals(checkOutDate)) {
                    return reservation;
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
        this.name = newName;
    }

    /**
     * Gets high-level information about the hotel.
     *
     * @return a string containing high-level information about the hotel
     */
    public String getHighLevelInfo() {
        return "Hotel Name: " + name + "\nTotal Rooms: " + rooms.size() + "\nTotal Earnings: PHP " + totalEarnings;
    }

    /**
     * Gets low-level information about the hotel.
     *
     * @return a string containing low-level information about the hotel
     */
    public String getLowLevelInfo() {
        StringBuilder info = new StringBuilder();
        info.append("Hotel Name: ").append(name).append("\n");
        info.append("Total Rooms: ").append(rooms.size()).append("\n");

        // Calculate total available and booked rooms
        int totalAvailable = 0;
        int totalBooked = 0;
        Date today = new Date(); // Assuming today's date

        for (Room room : rooms) {
            totalAvailable += room.isAvailable(today) ? 1 : 0;
            totalBooked += room.isAvailable(today) ? 0 : 1;
        }

        info.append("Total Available Rooms: ").append(totalAvailable).append("\n");
        info.append("Total Booked Rooms: ").append(totalBooked).append("\n");

        // Display information about a selected room (first room for example)
        if (!rooms.isEmpty()) {
            Room firstRoom = rooms.get(0);
            info.append("Room Info: ").append(firstRoom.getRoomInfo()).append("\n");
        }

        // Display information about a selected reservation (first reservation for example)
        if (!reservations.isEmpty()) {
            Reservation firstReservation = reservations.get(0);
            info.append("Reservation Info: ").append(firstReservation.getDetails()).append("\n");
        }

        return info.toString();
    }

    /**
     * Updates the base price for all rooms in the hotel.
     *
     * @param newPrice the new base price
     */
    public void updateBasePrice(double newPrice) {
        for (Room room : rooms) {
            room.setBasePrice(newPrice);
        }
    }

    /**
     * Removes a reservation from the hotel.
     *
     * @param reservation the reservation to remove
     */
    public void removeReservation(Reservation reservation) {
        reservations.remove(reservation);
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
    public Reservation simulateBooking(String guestName, Date checkInDate, Date checkOutDate, Room selectedRoom) {
        if (selectedRoom != null && selectedRoom.isAvailable(checkInDate, checkOutDate)) {
            Reservation reservation = new Reservation(guestName, checkInDate, checkOutDate, selectedRoom);
            reservations.add(reservation);
            totalEarnings += reservation.getTotalPrice();
            return reservation;
        } else {
            System.out.println("Selected room is not available for the given dates. We apologize.");
            return null;
        }
    }

    /**
     * Gets the names of all available rooms on a specific date.
     *
     * @param date the date to check availability
     * @return a list of available room names
     */
    public List<String> getAvailableRoomNames(Date date) {
        List<String> availableRooms = new ArrayList<>();
        for (Room room : rooms) {
            if (room.isAvailable(date)) { // Assuming Room class has an isAvailable method
                availableRooms.add(room.getName()); // Assuming Room class has a getName method
            }
        }
        return availableRooms;
    }

    /**
     * Gets the total number of available rooms on a specific date.
     *
     * @param date the date to check availability
     * @return the total number of available rooms
     */
    public int getTotalAvailableRooms(Date date) {
        int count = 0;
        for (Room room : rooms) {
            if (room.isAvailable(date)) {
                count++;
            }
        }
        return count;
    }

    /**
     * Gets the names of all available rooms between the check-in and check-out dates.
     *
     * @param checkInDate  the check-in date
     * @param checkOutDate the check-out date
     * @return a list of available room names
     */
    public List<String> getAvailableRoomNames(Date checkInDate, Date checkOutDate) {
        List<String> availableRoomNames = new ArrayList<>();
        for (Room room : rooms) {
            if (room.isAvailable(checkInDate, checkOutDate)) {
                availableRoomNames.add(room.getName());
            }
        }
        return availableRoomNames;
    }

    /**
     * Gets the total number of booked rooms on a specific date.
     *
     * @param date the date to check
     * @return the total number of booked rooms
     */
    public int getTotalBookedRooms(Date date) {
        return rooms.size() - getTotalAvailableRooms(date);
    }

    /**
     * Gets the name of the hotel.
     *
     * @return the name of the hotel
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the total number of rooms in the hotel.
     *
     * @return the total number of rooms
     */
    public int getTotalRooms() {
        return rooms.size();
    }

    /**
     * Gets the monthly earnings of the hotel.
     *
     * @return the monthly earnings
     */
    public double getMonthlyEarnings() {
        // Simplified monthly earnings calculation
        return totalEarnings;
    }

    /**
     * Gets information about a room by its name.
     *
     * @param roomName the name of the room
     * @return the room, or null if not found
     */
    public Room getRoomInfo(String roomName) {
        for (Room room : rooms) {
            if (room.getName().equalsIgnoreCase(roomName)) {
                return room;
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
        return rooms;
    }

    /**
     * Gets the base price for rooms.
     *
     * @return the base price
     */
    public double getBasePrice() {
        return 1299.0;
    }
}

