package oac.com.oac.app.modal;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Sudhir Singh on 07,July,2017
 * ESS,
 * B-65,Sector 63,Noida.
 */
public class SponsorsVo extends  BaseVO  {

    @SerializedName("data")
    @Expose
    private List<Sponsors> sponsorsList = null;
    @SerializedName("next_page")
    @Expose
    private Object nextPage;

    public List<Sponsors> getSponsorsList() {
        return sponsorsList;
    }

    public void setSponsorsList(List<Sponsors> sponsorsList) {
        this.sponsorsList = sponsorsList;
    }

    public Object getNextPage() {
        return nextPage;
    }

    public void setNextPage(Object nextPage) {
        this.nextPage = nextPage;
    }

}
