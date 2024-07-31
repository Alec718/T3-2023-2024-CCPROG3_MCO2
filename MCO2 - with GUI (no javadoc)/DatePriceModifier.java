import java.util.Date;

/**
 * Represents a price modifier for a specific date range.
 */
public class DatePriceModifier {
    
    // Start date for the price modifier
    private Date startDate; 
    
    // End date for the price modifier
    private Date endDate; 
    
    // The modifier to be applied for the date range
    private double modifier; 

    /**
     * Constructs a new DatePriceModifier with the specified start date, end date, and modifier.
     *
     * @param startDate the starting date for the modifier
     * @param endDate the ending date for the modifier
     * @param modifier the price modifier to apply within the date range
     */
    public DatePriceModifier(Date startDate, Date endDate, double modifier) {
        this.startDate = startDate; // Initialize start date
        this.endDate = endDate;     // Initialize end date
        this.modifier = modifier;    // Initialize price modifier
    }

    /**
     * Checks if a given date falls within the range of the start and end dates.
     *
     * @param date the date to check
     * @return true if the date is within the range, false otherwise
     */
    public boolean isDateInRange(Date date) {
        // Return true if the date is on or after the start date and on or before the end date
        return !date.before(startDate) && !date.after(endDate);
    }

    /**
     * Gets the price modifier for the date range.
     *
     * @return the price modifier
     */
    public double getModifier() {
        return modifier; // Return the price modifier
    }

    /**
     * Gets the start date of the modifier.
     *
     * @return the start date
     */
    public Date getStartDate() {
        return startDate; // Return the start date
    }

    /**
     * Sets the start date of the modifier.
     *
     * @param startDate the start date to set
     */
    public void setStartDate(Date startDate) {
        this.startDate = startDate; // Update the start date
    }

    /**
     * Gets the end date of the modifier.
     *
     * @return the end date
     */
    public Date getEndDate() {
        return endDate; // Return the end date
    }

    /**
     * Sets the end date of the modifier.
     *
     * @param endDate the end date to set
     */
    public void setEndDate(Date endDate) {
        this.endDate = endDate; // Update the end date
    }
}
