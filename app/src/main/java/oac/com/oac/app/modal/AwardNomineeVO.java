package oac.com.oac.app.modal;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Sudhir Singh on 24,July,2018
 * ESS,
 * B-65,Sector 63,Noida.
 */
public class AwardNomineeVO extends BaseVO implements Serializable {

    @SerializedName("data")
    @Expose
    private List<AwardNominee> speakersList = null;
    @SerializedName("next_page")
    @Expose
    private Object nextPage;

    public List<AwardNominee> getSpeakersList() {
        return speakersList;
    }

    public void setSpeakersList(List<AwardNominee> speakersList) {
        this.speakersList = speakersList;
    }

    public Object getNextPage() {
        return nextPage;
    }

    public void setNextPage(Object nextPage) {
        this.nextPage = nextPage;
    }
}
