package model;
/**A class that creates the User object
 */
public class User {
    private int id;
    private String username;
    private String password;

    public User(int id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }
    /**Gets user ID
     * @return int returns user ID
     */
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    /**Gets username
     * @return String returns username
     */
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    /**Gets password
     * @return String returns password
     */
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
