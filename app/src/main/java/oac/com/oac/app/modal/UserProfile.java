package oac.com.oac.app.modal;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Sudhir Singh on 15,July,2017
 * ESS,
 * B-65,Sector 63,Noida.
 */
public class UserProfile {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("company")
    @Expose
    private String company;
    @SerializedName("designation")
    @Expose
    private String designation;
    @SerializedName("networking")
    @Expose
    private String networking;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getNetworking() {
        return networking;
    }

    public void setNetworking(String networking) {
        this.networking = networking;
    }
}
