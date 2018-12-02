package oac.com.oac.app.modal;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Sudhir Singh on 07,July,2017
 * ESS,
 * B-65,Sector 63,Noida.
 */
public class Networking implements Serializable {

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("company")
    @Expose
    private String company;
    @SerializedName("designation")
    @Expose
    private String designation;

    @SerializedName("image")
    @Expose
    private String image;

    @SerializedName("meeting_sent")
    @Expose
    private boolean meeting_sent;

    @SerializedName("meeting_recieve")
    @Expose
    private boolean meeting_recieve;

    @SerializedName("isSelected")
    @Expose
    private boolean isSelected = false;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isMeeting_sent() {
        return meeting_sent;
    }

    public void setMeeting_sent(boolean meeting_sent) {
        this.meeting_sent = meeting_sent;
    }

    public boolean isMeeting_recieve() {
        return meeting_recieve;
    }

    public void setMeeting_recieve(boolean meeting_recieve) {
        this.meeting_recieve = meeting_recieve;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

}
