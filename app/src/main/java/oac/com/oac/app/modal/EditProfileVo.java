package oac.com.oac.app.modal;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Sudhir Singh on 15,July,2017
 * ESS,
 * B-65,Sector 63,Noida.
 */
public class EditProfileVo extends BaseVO {


    @SerializedName("data")
    @Expose
    private UserProfile data;


    public UserProfile getData() {
        return data;
    }

    public void setData(UserProfile data) {
        this.data = data;
    }
}
