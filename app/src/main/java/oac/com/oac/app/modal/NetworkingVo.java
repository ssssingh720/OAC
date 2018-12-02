package oac.com.oac.app.modal;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Sudhir Singh on 07,July,2017
 * ESS,
 * B-65,Sector 63,Noida.
 */
public class NetworkingVo extends  BaseVO implements Serializable {

    @SerializedName("data")
    @Expose
    private List<Networking> networkingList = null;

    public List<Networking> getNetworkingList() {
        return networkingList;
    }

    public void setNetworkingList(List<Networking> networkingList) {
        this.networkingList = networkingList;
    }
}
