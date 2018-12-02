package oac.com.oac.app.modal;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Sudhir Singh on 24,July,2018
 * ESS,
 * B-65,Sector 63,Noida.
 */
public class AwardNominee implements Serializable {

    @SerializedName("code")
    @Expose
    private String code;
    @SerializedName("html")
    @Expose
    private String html;

    @SerializedName("name")
    @Expose
    private String name;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getHtml() {
        return html;
    }

    public void setHtml(String html) {
        this.html = html;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
