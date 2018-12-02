package oac.com.oac.app.activities;

import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.android.volley.VolleyError;

import java.util.HashMap;

import org.oacasia.R;
import oac.com.oac.app.load_toast.LoadToast;
import oac.com.oac.app.manager.SharedPrefManager;
import oac.com.oac.app.modal.BaseVO;
import oac.com.oac.app.modal.EditProfileVo;
import oac.com.oac.app.modal.FeedParams;
import oac.com.oac.app.modal.ResponseError;
import oac.com.oac.app.utils.APIMethods;
import oac.com.oac.app.utils.Util;

/**
 * Created by Sudhir Singh on 11,July,2017
 * ESS,
 * B-65,Sector 63,Noida.
 */
public class EditProfileActivity extends AppBaseActivity implements View.OnClickListener {

    private EditText edtSignUpFName;
    private EditText edtSignUpPhone;
    private EditText edtSignUpCmpName;
    private EditText edtSignUpDesign;
    private RadioGroup rdgProfileType;
    private RadioButton rdPublic;
    private RadioButton rdPrivate;

    private LoadToast loadToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.edit_profile_activity);

        LinearLayout lnrBack = (LinearLayout) findViewById(R.id.lnrBack);
        edtSignUpFName = (EditText) findViewById(R.id.edtSignUpFName);
        edtSignUpPhone = (EditText) findViewById(R.id.edtSignUpPhone);
        edtSignUpCmpName = (EditText) findViewById(R.id.edtSignUpCmpName);
        edtSignUpDesign = (EditText) findViewById(R.id.edtSignUpDesign);
        rdgProfileType = (RadioGroup) findViewById(R.id.rdgProfileType);
        rdPublic = (RadioButton) findViewById(R.id.rdPublic);
        rdPrivate = (RadioButton) findViewById(R.id.rdPrivate);
        Button btnRegister = (Button) findViewById(R.id.btnRegister);
        final TextView txtNetworking = (TextView) findViewById(R.id.txtNetworking);
//        edtSignUpFName.setText(SharedPrefManager.getInstance().getSharedDataString(FeedParams.NAME));
//        edtSignUpPhone.setText(SharedPrefManager.getInstance().getSharedDataString(FeedParams.PHONE));
//        edtSignUpCmpName.setText(SharedPrefManager.getInstance().getSharedDataString(FeedParams.COMAPANY));
//        edtSignUpDesign.setText(SharedPrefManager.getInstance().getSharedDataString(FeedParams.DESIGNATION));

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int height = size.y;
        loadToast = new LoadToast(EditProfileActivity.this);
        loadToast.setText("Please Wait...");
        loadToast.setTranslationY(height / 2);
        loadToast.setTextColor(Color.BLACK).setBackgroundColor(Color.WHITE)
                .setProgressColor(getResources().getColor(R.color.colorPrimary));

        rdPublic.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    txtNetworking.setText(getResources().getString(R.string.public_networking_text));
                } else {
                    txtNetworking.setText(getResources().getString(R.string.private_networking_text));
                }
            }
        });

        rdPrivate.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    txtNetworking.setText(getResources().getString(R.string.private_networking_text));
                } else {
                    txtNetworking.setText(getResources().getString(R.string.public_networking_text));
                }
            }
        });


        btnRegister.setOnClickListener(this);
        lnrBack.setOnClickListener(this);
        if (Util.isNetworkOnline(EditProfileActivity.this)) {
        loadToast.show();
        String userId = SharedPrefManager.getInstance().getSharedDataString(FeedParams.USER_ID);
        String token = SharedPrefManager.getInstance().getSharedDataString(FeedParams.TOKEN);
        HashMap<String, String> params = new HashMap<>();
        params.put(FeedParams.USER_ID, userId);
        params.put(FeedParams.TOKEN, token);
        placeRequest(APIMethods.EDIT_PROFILE, EditProfileVo.class, params, true, null);
        } else {
            showToast(R.string.no_internet);
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btnRegister:
/////name, email, phone, password, company, designation, networking
                if (Util.isNetworkOnline(EditProfileActivity.this)) {
                    if (isValidData()) {
                        loadToast.show();
                        HashMap<String, String> params = new HashMap<>();

                        String userId = SharedPrefManager.getInstance().getSharedDataString(FeedParams.USER_ID);
                        String token = SharedPrefManager.getInstance().getSharedDataString(FeedParams.TOKEN);

                        params.put(FeedParams.USER_ID, userId);
                        params.put(FeedParams.TOKEN, token);
                        params.put(FeedParams.NAME, edtSignUpFName.getText().toString().trim());
                        params.put(FeedParams.PHONE, edtSignUpPhone.getText().toString().trim());
                        params.put(FeedParams.COMAPANY, edtSignUpCmpName.getText().toString().trim());
                        params.put(FeedParams.DESIGNATION, edtSignUpDesign.getText().toString().trim());
                        int selectedId = rdgProfileType.getCheckedRadioButtonId();
                        if (selectedId == rdPublic.getId()) {
                            params.put(FeedParams.NETWORKING, "1");
                        } else if (selectedId == rdPrivate.getId()) {
                            params.put(FeedParams.NETWORKING, "0");
                        }

                        placeRequest(APIMethods.UPDATE_PROFILE, BaseVO.class, params, true, null);
                    }
                } else {
                    showToast(R.string.no_internet);
                }

                break;

            case R.id.lnrBack:
                onBackPressed();
                break;
        }
    }

    private boolean isValidData() {

        if (edtSignUpFName.getText().toString().trim().length() <= 0) {
            edtSignUpFName.setError("Provide First Name.");
            return false;
        } else if (edtSignUpPhone.getText().toString().trim().length() < 10) {
            edtSignUpPhone.setError("Provide Valid Phone Number.");
            return false;
        } else if (edtSignUpCmpName.getText().toString().trim().length() <= 0) {
            edtSignUpCmpName.setError("Provide Company Name.");
            return false;
        } else if (edtSignUpDesign.getText().toString().trim().length() <= 0) {
            edtSignUpDesign.setError("Provide Designation.");
            return false;
        }

        return true;

    }

    @Override
    public void onAPIResponse(Object response, String apiMethod) {
        super.onAPIResponse(response, apiMethod);

        loadToast.success();
        if (apiMethod.equalsIgnoreCase(APIMethods.UPDATE_PROFILE)) {

            BaseVO baseVO = (BaseVO) response;

            showToast(baseVO.getMessage());
            SharedPrefManager.getInstance().setSharedData(FeedParams.NAME, edtSignUpFName.getText().toString().trim());


        } else if (apiMethod.equalsIgnoreCase(APIMethods.EDIT_PROFILE)) {

            EditProfileVo editProfileVo = (EditProfileVo) response;

            edtSignUpFName.setText(editProfileVo.getData().getName());
            edtSignUpPhone.setText(editProfileVo.getData().getPhone());
            edtSignUpCmpName.setText(editProfileVo.getData().getCompany());
            edtSignUpDesign.setText(editProfileVo.getData().getDesignation());
            if(editProfileVo.getData().getNetworking().equalsIgnoreCase("0")){
                rdPrivate.setChecked(true);
                rdPublic.setChecked(false);
            }else{
                rdPrivate.setChecked(false);
                rdPublic.setChecked(true);
            }
        }
    }

    @Override
    public void onErrorResponse(VolleyError error, String apiMethod) {
        super.onErrorResponse(error, apiMethod);
        loadToast.error();
        if (apiMethod.equalsIgnoreCase(APIMethods.UPDATE_PROFILE)) {

            ResponseError responseError = (ResponseError) error;
            showToast(responseError.getErrorMessage());

        } else if (apiMethod.equalsIgnoreCase(APIMethods.EDIT_PROFILE)) {

            ResponseError responseError = (ResponseError) error;
            showToast(responseError.getErrorMessage());

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        finish();
    }
}
