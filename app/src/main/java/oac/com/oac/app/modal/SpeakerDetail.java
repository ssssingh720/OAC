package oac.com.oac.app.modal;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Sudhir Singh on 07,July,2017
 * ESS,
 * B-65,Sector 63,Noida.
 */
public class SpeakerDetail implements Serializable {

    @SerializedName("speaker_id")
    @Expose
    private String speaker_id;
    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("designation")
    @Expose
    private String designation;

    @SerializedName("company_name")
    @Expose
    private String companyName;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("profile")
    @Expose
    private String profile;

    @SerializedName("topic")
    @Expose
    private String topic;

    @SerializedName("linkedin")
    @Expose
    private String linkedin;

    @SerializedName("twitter")
    @Expose
    private String twitter;

    @SerializedName("facebook")
    @Expose
    private String facebook;

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getLinkedin() {
        return linkedin;
    }

    public void setLinkedin(String linkedin) {
        this.linkedin = linkedin;
    }

    public String getTwitter() {
        return twitter;
    }

    public void setTwitter(String twitter) {
        this.twitter = twitter;
    }

    public String getFacebook() {
        return facebook;
    }

    public void setFacebook(String facebook) {
        this.facebook = facebook;
    }

    public String getSpeaker_id() {
        return speaker_id;
    }

    public void setSpeaker_id(String speaker_id) {
        this.speaker_id = speaker_id;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }


}
