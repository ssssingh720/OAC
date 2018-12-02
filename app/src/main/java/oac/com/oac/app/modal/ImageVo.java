package oac.com.oac.app.modal;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Sudhir Singh on 13,July,2017
 * ESS,
 * B-65,Sector 63,Noida.
 */
public class ImageVo extends BaseVO {

    @SerializedName("file_path")
    @Expose
    private String filePath;


    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }


}
