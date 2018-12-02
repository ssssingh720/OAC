package oac.com.oac.app.modal;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Sudhir Singh on 07,July,2017
 * ESS,
 * B-65,Sector 63,Noida.
 */
public class AboutUsVo extends  BaseVO implements Serializable{

    @SerializedName("data")
    @Expose
    private AboutUs aboutUs;


    public AboutUs getAboutUs() {
        return aboutUs;
    }

    public void setAboutUs(AboutUs aboutUs) {
        this.aboutUs = aboutUs;
    }
}
