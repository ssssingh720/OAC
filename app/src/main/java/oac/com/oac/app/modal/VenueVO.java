package oac.com.oac.app.modal;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Sudhir Singh on 11,July,2017
 * ESS,
 * B-65,Sector 63,Noida.
 */
public class VenueVO extends BaseVO implements Serializable {

    @SerializedName("data")
    @Expose
    private Venue venueDetail;


    public Venue getVenueDetail() {
        return venueDetail;
    }

    public void setVenueDetail(Venue venueDetail) {
        this.venueDetail = venueDetail;
    }
}
