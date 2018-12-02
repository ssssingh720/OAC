package oac.com.oac.app.modal;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Sudhir Singh on 03,July,2017
 * ESS,
 * B-65,Sector 63,Noida.
 */
public class Categories extends BaseVO implements Serializable {

    @SerializedName("0")
    @Expose
    private List<CategoriesDetail> _categories = null;

    public List<CategoriesDetail> get_categories() {
        return _categories;
    }

    public void set_categories(List<CategoriesDetail> _categories) {
        this._categories = _categories;
    }
}
