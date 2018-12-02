package oac.com.oac.app.modal;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Madhumita on 05-11-2015.
 */
public class UserProfileVo extends BaseVO implements Serializable {

    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("user_email")
    @Expose
    private String userEmail;
    @SerializedName("token")
    @Expose
    private String token;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("qrcode")
    @Expose
    private String qrcode;
    @SerializedName("qrcode_no")
    @Expose
    private String qrcode_no;

    public String getQrcode() {
        return qrcode;
    }

    public void setQrcode(String qrcode) {
        this.qrcode = qrcode;
    }

    public String getQrcode_no() {
        return qrcode_no;
    }

    public void setQrcode_no(String qrcode_no) {
        this.qrcode_no = qrcode_no;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
