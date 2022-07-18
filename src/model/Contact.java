package model;
/**A class that creates the Contact object
 */

public class Contact {
    private String name;
    private String email;
    private int id;
    /**Contact Constructor
     * @param email contact email.
     * @param name contact name
     * @param id contact id
     */
    public Contact(String name, String email, int id) {

        this.name = name;
        this.email = email;
        this.id = id;
    }
    /**Gets Contact Name
     * @return String returns contact name.
     */
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    /**Gets contact email
     * @return String returns contact email.
     */
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    /**Gets contact ID
     * @return int returns contact ID
     */
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
