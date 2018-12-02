package oac.com.oac.app.modal;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Sudhir Singh on 03,July,2017
 * ESS,
 * B-65,Sector 63,Noida.
 */
public class CategoriesDetail {

    @SerializedName("category_id")
    @Expose
    private String categoryId;
    @SerializedName("category_name")
    @Expose
    private String categoryName;
    @SerializedName("category_image")
    @Expose
    private String categoryImage;
    @SerializedName("chapter_list")
    @Expose
    private ArrayList<ChaptersVo> chaptersList;

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryImage() {
        return categoryImage;
    }

    public void setCategoryImage(String categoryImage) {
        this.categoryImage = categoryImage;
    }

    public ArrayList<ChaptersVo> getChaptersList() {
        return chaptersList;
    }

    public void setChaptersList(ArrayList<ChaptersVo> chaptersList) {
        this.chaptersList = chaptersList;
    }
}
