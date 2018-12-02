package oac.com.oac.app.modal;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Sudhir Singh on 11,July,2017
 * ESS,
 * B-65,Sector 63,Noida.
 */
public class Quiz implements Serializable {
    @SerializedName("quiz_id")
    @Expose
    private String quizId;
    @SerializedName("question_text")
    @Expose
    private String questionText;
    @SerializedName("question_count")
    @Expose
    private Integer questionCount;

    @SerializedName("pre_text")
    @Expose
    private String preText;

    @SerializedName("prize_text")
    @Expose
    private String prize_text;

    @SerializedName("duration")
    @Expose
    private Integer duration;
    @SerializedName("start_no")
    @Expose
    private Integer startNo;
    @SerializedName("status")
    @Expose
    private Integer status;

    public String getPrize_text() {
        return prize_text;
    }

    public void setPrize_text(String prize_text) {
        this.prize_text = prize_text;
    }

    public String getQuizId() {
        return quizId;
    }

    public void setQuizId(String quizId) {
        this.quizId = quizId;
    }

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public Integer getQuestionCount() {
        return questionCount;
    }

    public void setQuestionCount(Integer questionCount) {
        this.questionCount = questionCount;
    }

    public String getPreText() {
        return preText;
    }

    public void setPreText(String preText) {
        this.preText = preText;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public Integer getStartNo() {
        return startNo;
    }

    public void setStartNo(Integer startNo) {
        this.startNo = startNo;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }


}
