package model;
/**A class that creates the Country object with setters and getters for attributes.
 */
public class Country {
    private int countryId;
    private String countryName;
    /**Country Constructor
     * @param countryId country ID
     * @param countryName country name
     */
    public Country(int countryId, String countryName) {
        this.countryId = countryId;
        this.countryName = countryName;
    }
    /**Gets country name
     * @return String returns country name
     */
    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }
    /**Gets Country ID
     * @return int returns country ID
     */
    public int getCountryId() {
        return countryId;
    }

    public void setCountryId(int countryId) {
        this.countryId = countryId;
    }
}
