package oac.com.oac.app.modal;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Sudhir Singh on 09,July,2018
 * ESS,
 * B-65,Sector 63,Noida.
 */
public class NotificationVo extends BaseVO implements Serializable {

    @SerializedName("data")
    @Expose
    private List<Notification> data = null;

    public List<Notification> getData() {
        return data;
    }

    public void setData(List<Notification> data) {
        this.data = data;
    }
}
