package oac.com.oac.app.modal;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Sudhir Singh on 14,June,2018
 * ESS,
 * B-65,Sector 63,Noida.
 */
public class AppImage implements Serializable{

    @SerializedName("oaa_logo")
    @Expose
    private String oaaLogo;
    @SerializedName("oac_logo")
    @Expose
    private String oacLogo;
    @SerializedName("splash_logo")
    @Expose
    private String splashLogo;
    @SerializedName("quiz_logo")
    @Expose
    private String quizLogo;
    @SerializedName("qna_logo")
    @Expose
    private String qnaLogo;

    @SerializedName("sponsor_banner")
    @Expose
    private ArrayList<String> sponsor_banner;

    public ArrayList<String> getSponsor_banner() {
        return sponsor_banner;
    }

    public void setSponsor_banner(ArrayList<String> sponsor_banner) {
        this.sponsor_banner = sponsor_banner;
    }

    public String getOaaLogo() {
        return oaaLogo;
    }

    public void setOaaLogo(String oaaLogo) {
        this.oaaLogo = oaaLogo;
    }

    public String getOacLogo() {
        return oacLogo;
    }

    public void setOacLogo(String oacLogo) {
        this.oacLogo = oacLogo;
    }

    public String getSplashLogo() {
        return splashLogo;
    }

    public void setSplashLogo(String splashLogo) {
        this.splashLogo = splashLogo;
    }

    public String getQuizLogo() {
        return quizLogo;
    }

    public void setQuizLogo(String quizLogo) {
        this.quizLogo = quizLogo;
    }

    public String getQnaLogo() {
        return qnaLogo;
    }

    public void setQnaLogo(String qnaLogo) {
        this.qnaLogo = qnaLogo;
    }
}
