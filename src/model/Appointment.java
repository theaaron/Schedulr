package model;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
/**A class that creates the Appointment object
 */
public class Appointment {
    private int id;
    private String title;
    private String desc;
    private String location;
    private String type;
    private LocalDate startDate;
    private LocalDateTime startTime;
    private LocalDate endDate;
    private LocalDateTime endTime;
    private int custId;
    private int userId;
    private int contactId;
    private String contact;
    /**A Method that creates the Appointment object
     * @param id Appointment ID
     * @param title Appointment Title
     * @param contact Appointment Contact
     * @param contactId Contact ID
     * @param type Appointment Type
     * @param userId User ID
     * @param location Appointment Location
     * @param desc Appointment Description
     * @param custId Customer ID
     * @param endDate Appointment End Date
     * @param endTime Appointment End Time
     * @param startDate Appointment Start Date
     * @param startTime Appointment Start Time
     */
    public Appointment(int id, String title, String desc, String location, String type, LocalDate startDate, LocalDateTime startTime, LocalDate endDate, LocalDateTime endTime, int custId, int userId, int contactId, String contact){
        this.id = id;
        this.title = title;
        this.desc = desc;
        this.location = location;
        this.type = type;
        this.startDate = startDate;
        this.startTime = startTime;
        this.endDate = endDate;
        this.endTime = endTime;
        this.custId = custId;
        this.userId = userId;
        this.contactId = contactId;
        this.contact = contact;
    }
    /**Gets Appointment ID
     * @return int returns appointment ID
     */
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    /**Gets Appointment Title
     * @return String returns appointment title
     */
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    /**Gets Appointment Description
     * @return String returns appointment description.
     */
    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
    /**Gets Appointment Location
     * @return String returns appointment location
     */
    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
    /**Gets Appointment Type
     * @return String returns appointment type
     */
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    /**Gets Appointment Start Date
     * @return LocalDate returns appointment start date
     */
    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }
    /**Gets Appointment Start Time
     * @return LocalDateTime returns appointment start time
     */
    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }
    /**Gets Appointment End Date
     * @return LocalDate returns appointment end date
     */
    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }
    /**Gets Appointment end Time
     * @return LocalDateTime returns appointment end time.
     */
    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }
    /**Gets Customer ID
     * @return int returns customer ID
     */
    public int getCustId() {
        return custId;
    }

    public void setCustId(int custId) {
        this.custId = custId;
    }
    /**Gets User ID
     * @return int returns user ID
     */
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
    /**Gets contact ID
     * @return int returns contact ID
     */
    public int getContactId() {
        return contactId;
    }

    public void setContactId(int contactId) {
        this.contactId = contactId;
    }
    /**Gets Contact name
     * @return String returns contact name.
     */
    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }
}
