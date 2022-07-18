package model;

/**A class that creates the Customer object with setters and getters for attributes.
 */
public class Customer {
    private int id;
    private String name;
    private String addy;
    private String postal;
    private String phone;
    private String division;
    private String country;
    private int divisionId;

    /**A constructor for the customer object.
     * @param phone customer phone number
     * @param addy customer address
     * @param country customer country
     * @param division customer division
     * @param divisionId division ID
     * @param id customer ID
     * @param name customer name
     * @param postal customer postal code
     */
    public Customer(int id, String name, String addy, String postal, String phone, String division, String country, int divisionId) {
        this.id = id;
        this.name = name;
        this.addy = addy;
        this.postal = postal;
        this.phone = phone;
        this.division = division;
        this.country = country;
        this.divisionId = divisionId;



    }
    /**Gets customer id
     * @return int returns customer id
     */
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    /**Gets customer name
     * @return String returns customer name
     */
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    /**Gets country name
     * @return String returns country name
     */
    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
    /**Gets customer address
     * @return String returns customer address
     */
    public String getAddy() {
        return addy;
    }

    public void setAddy(String addy) {
        this.addy = addy;
    }
    /**Gets customer postal code
     * @return String returns postal code
     */
    public String getPostal() {
        return postal;
    }

    public void setPostal(String postal) {
        this.postal = postal;
    }
    /**Gets customer phone number
     * @return String returns customer phone number
     */
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
    /**Gets customer division
     * @return String returns customer division
     */
    public String getDivision() {
        return division;
    }

    public void setDivision(String name) {
        this.division = division;
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
}
