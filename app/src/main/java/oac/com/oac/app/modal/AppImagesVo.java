package oac.com.oac.app.modal;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Sudhir Singh on 14,June,2018
 * ESS,
 * B-65,Sector 63,Noida.
 */
public class AppImagesVo extends BaseVO implements Serializable {

    @SerializedName("data")
    @Expose
    private AppImage appImage;


    public AppImage getAboutUs() {
        return appImage;
    }

    public void setAboutUs(AppImage appImage) {
        this.appImage = appImage;
    }
}

