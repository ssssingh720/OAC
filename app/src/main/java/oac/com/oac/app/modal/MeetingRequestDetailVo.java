package oac.com.oac.app.modal;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Sudhir Singh on 09,July,2018
 * ESS,
 * B-65,Sector 63,Noida.
 */
public class MeetingRequestDetailVo extends BaseVO {

    @SerializedName("data")
    @Expose
    private List<MeetingRequestDetail> meetingRequestDetails = null;


    public List<MeetingRequestDetail> getMeetingRequestDetails() {
        return meetingRequestDetails;
    }

    public void setMeetingRequestDetails(List<MeetingRequestDetail> meetingRequestDetails) {
        this.meetingRequestDetails = meetingRequestDetails;
    }
}
