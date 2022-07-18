package model;
/**A class that creates the Appointment object
 */
public class Division {
    private int divisionId;
    private String divisionName;
    private int countryId;
    /**Constructor for the division object
     * @param divisionId division id
     * @param countryId country id
     * @param divisionName division name
     */
    public Division(int divisionId, String divisionName, int countryId) {
        this.divisionId = divisionId;
        this.divisionName = divisionName;
        this.countryId = countryId;
    }
    /**Gets division name
     * @return String returns division name
     */
    public String getDivisionName() {
        return divisionName;
    }

    public void setDivisionName(String divisionName) {
        this.divisionName = divisionName;
    }
    /**Gets division ID
     * @return int returns division ID
     */
    public int getDivisionId() {
        return divisionId;
    }

    public void setDivisionId(int divisionId) {
        this.divisionId = divisionId;
    }
    /**Gets country ID
     * @return int returns country ID
     */
    public int getCountryId() {
        return countryId;
    }

    public void setCountryId(int countryId) {
        this.countryId = countryId;
    }
}
