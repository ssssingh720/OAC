package oac.com.oac.app.modal;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Sudhir Singh on 10,July,2018
 * ESS,
 * B-65,Sector 63,Noida.
 */
public class CurrentAgendaVo extends BaseVO {

    @SerializedName("data")
    @Expose
    private CurrentAgenda currentAgenda;

    public CurrentAgenda getCurrentAgenda() {
        return currentAgenda;
    }

    public void setCurrentAgenda(CurrentAgenda currentAgenda) {
        this.currentAgenda = currentAgenda;
    }
}
