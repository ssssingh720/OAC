package oac.com.oac.app.activities;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.HashMap;

import org.oacasia.R;
import oac.com.oac.app.load_toast.LoadToast;
import oac.com.oac.app.manager.SharedPrefManager;
import oac.com.oac.app.modal.AboutUsVo;
import oac.com.oac.app.modal.FeedParams;
import oac.com.oac.app.modal.ResponseError;
import oac.com.oac.app.modal.UserProfileVo;
import oac.com.oac.app.utils.APIMethods;
import oac.com.oac.app.utils.Util;

/**
 * Created by Sudhir Singh on 03,July,2017
 * ESS,
 * B-65,Sector 63,Noida.
 */
public class SignUpActivity extends AppBaseActivity implements View.OnClickListener {

    private EditText edtSignUpFName;
    private EditText edtSignUpEmail;
    private EditText edtSignUpPhone;
    private EditText edtSignUpCmpName;
    private EditText edtSignUpDesign;
    private EditText edtSignUpPwd;
    private EditText edtSignUpCnfPwd;
    private RadioGroup rdgProfileType;
    private RadioButton rdPublic;
    private RadioButton rdPrivate;
    private CheckBox chkTermsCondition;
//    private RecyclerView rclSponsorRegister;

    private LoadToast loadToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_activity);

        edtSignUpFName = (EditText) findViewById(R.id.edtSignUpFName);
        edtSignUpEmail = (EditText) findViewById(R.id.edtSignUpEmail);
        edtSignUpPhone = (EditText) findViewById(R.id.edtSignUpPhone);
        edtSignUpCmpName = (EditText) findViewById(R.id.edtSignUpCmpName);
        edtSignUpDesign = (EditText) findViewById(R.id.edtSignUpDesign);
        edtSignUpPwd = (EditText) findViewById(R.id.edtSignUpPwd);
        edtSignUpCnfPwd = (EditText) findViewById(R.id.edtSignUpCnfPwd);
        rdgProfileType = (RadioGroup) findViewById(R.id.rdgProfileType);
        rdPublic = (RadioButton) findViewById(R.id.rdPublic);
        rdPrivate = (RadioButton) findViewById(R.id.rdPrivate);
        Button btnRegister = (Button) findViewById(R.id.btnRegister);
        TextView txtLogin = (TextView) findViewById(R.id.txtLogin);
        final TextView txtNetworking = (TextView) findViewById(R.id.txtNetworking);
        chkTermsCondition = (CheckBox) findViewById(R.id.chkTermsCondition);
        TextView txtTermsCondition = (TextView) findViewById(R.id.txtTermsCondition);

//        rclSponsorRegister = (RecyclerView) findViewById(R.id.rclRegisterSponsor);

//        edtSignUpFName.setText("Test");
//        edtSignUpEmail.setText("test@gmail.com");
//        edtSignUpPhone.setText("8576543456");
//        edtSignUpCmpName.setText("ABC COMP");
//        edtSignUpDesign.setText("DESIGN ABC");
//        edtSignUpPwd.setText("12345");
//        edtSignUpCnfPwd.setText("12345");\

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int height = size.y;
        loadToast = new LoadToast(SignUpActivity.this);
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

        txtTermsCondition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chkTermsCondition.setChecked(true);
                if (Util.isNetworkOnline(SignUpActivity.this)) {
                    loadToast.show();

                    HashMap<String, String> params = new HashMap<>();
//                    String userId = SharedPrefManager.getInstance().getSharedDataString(FeedParams.USER_ID);
//                    String token = SharedPrefManager.getInstance().getSharedDataString(FeedParams.TOKEN);

                    params.put(FeedParams.USER_ID, "");
                    params.put(FeedParams.TOKEN, "");
                    params.put(FeedParams.CODE, "terms-condition");

                    placeRequest(APIMethods.TERMS_CONDITION, AboutUsVo.class, params, true, null);
                } else {
                    showToast(getResources().getString(R.string.no_internet));
                }
            }
        });


//        HashMap<String, String> params = new HashMap<>();
//        params.put(FeedParams.USER_ID, "7");
//        params.put(FeedParams.TOKEN, "a7f0ed54ac7db5d14877f083ae29753b");

//        placeRequest(APIMethods.SPONSOR_LIST, SponsorsVo.class, null, true, null);

        btnRegister.setOnClickListener(this);
        txtLogin.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btnRegister:
/////name, email, phone, password, company, designation, networking
                if (Util.isNetworkOnline(SignUpActivity.this)) {
                    if (isValidData()) {
                        loadToast.show();
                        HashMap<String, String> params = new HashMap<>();
                        params.put(FeedParams.NAME, edtSignUpFName.getText().toString().trim());
                        params.put(FeedParams.EMAIL, edtSignUpEmail.getText().toString().trim());
                        params.put(FeedParams.PHONE, edtSignUpPhone.getText().toString().trim());
                        params.put(FeedParams.PASSWORD, edtSignUpPwd.getText().toString().trim());
                        params.put(FeedParams.COMAPANY, edtSignUpCmpName.getText().toString().trim());
                        params.put(FeedParams.DESIGNATION, edtSignUpDesign.getText().toString().trim());
                        SharedPrefManager.getInstance().setSharedData(FeedParams.NOTIFICATION_STATUS, true);

                        int selectedId = rdgProfileType.getCheckedRadioButtonId();
                        if (selectedId == rdPublic.getId()) {
                            params.put(FeedParams.NETWORKING, "1");
                        } else if (selectedId == rdPrivate.getId()) {
                            params.put(FeedParams.NETWORKING, "0");
                        }

                        String gcmToken = FirebaseInstanceId.getInstance().getToken();
                        if (gcmToken != null && gcmToken.length() > 0) {
                            params.put(FeedParams.DEVICE_TYPE, FeedParams.DEVICE_TYPE_NAME);
                            params.put(FeedParams.DEVICE_ID, gcmToken);
                        }

                        placeRequest(APIMethods.REGISTER_API, UserProfileVo.class, params, true, null);
                    }
                } else {
                    showToast(R.string.no_internet);
                }

                break;

            case R.id.txtLogin:

                Intent signIntent = new Intent(SignUpActivity.this, SignInActivity.class);
                startActivity(signIntent);

                break;
        }
    }

    private boolean isValidData() {

        if (edtSignUpFName.getText().toString().trim().length() <= 0) {
            edtSignUpFName.setError("Provide First Name.");
            return false;
        } else if (edtSignUpEmail.getText().toString().trim().length() <= 0) {
            edtSignUpEmail.setError("Provide Email.");
            return false;
        } else if (!Util.isValidEmail(edtSignUpEmail.getText().toString())) {
            edtSignUpEmail.setError("Provide Valid Email.");
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
        } else if (edtSignUpPwd.getText().toString().trim().length() <= 0) {
            edtSignUpPwd.setError("Provide Password.");
            return false;
        } else if (!edtSignUpPwd.getText().toString().equals(edtSignUpCnfPwd.getText().toString())) {
            edtSignUpCnfPwd.setError("Password Not Match.");
            return false;
        } else if (!chkTermsCondition.isChecked()) {
            showToast("Accept " + getResources().getString(R.string.terms_condition));
            return false;
        }

        return true;

    }

    @Override
    public void onAPIResponse(Object response, String apiMethod) {
        super.onAPIResponse(response, apiMethod);

        if (apiMethod.equalsIgnoreCase(APIMethods.REGISTER_API)) {
            loadToast.success();

            UserProfileVo userProfileVo = (UserProfileVo) response;
            SharedPrefManager.getInstance().setSharedData(FeedParams.USER_ID, userProfileVo.getUserId());
            SharedPrefManager.getInstance().setSharedData(FeedParams.NAME, userProfileVo.getName());
            SharedPrefManager.getInstance().setSharedData(FeedParams.EMAIL, userProfileVo.getUserEmail());
            SharedPrefManager.getInstance().setSharedData(FeedParams.IMAGE, userProfileVo.getImage());
            SharedPrefManager.getInstance().setSharedData(FeedParams.TOKEN, userProfileVo.getToken());

            Intent landingIntent = new Intent(SignUpActivity.this, LandingActivity.class);
            landingIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//            landingIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            landingIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(landingIntent);

            SignUpActivity.this.finish();

        } else if (apiMethod.equalsIgnoreCase(APIMethods.TERMS_CONDITION)) {
            loadToast.success();
            AboutUsVo aboutUsVo = (AboutUsVo) response;
            Intent termsIntent = new Intent(SignUpActivity.this, TermsConditionActivity.class);
            termsIntent.putExtra("TERMS", aboutUsVo);
            startActivity(termsIntent);

        } else if (apiMethod.equalsIgnoreCase(APIMethods.SPONSOR_LIST)) {
//            SponsorsVo sponsorsVo = (SponsorsVo) response;
//
//            rclSponsorRegister.setHasFixedSize(true);
//            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
//            rclSponsorRegister.setLayoutManager(mLayoutManager);
//
//            LoginSponsorAdapter chapterVideoAdapter = new LoginSponsorAdapter(SignUpActivity.this, sponsorsVo.getSponsorsList());
//            rclSponsorRegister.setAdapter(chapterVideoAdapter);
        }
    }

    @Override
    public void onErrorResponse(VolleyError error, String apiMethod) {
        super.onErrorResponse(error, apiMethod);

        if (apiMethod.equalsIgnoreCase(APIMethods.REGISTER_API)) {

            loadToast.error();
            ResponseError responseError = (ResponseError) error;
            showToast(responseError.getErrorMessage());

        } else if (apiMethod.equalsIgnoreCase(APIMethods.TERMS_CONDITION)) {
            loadToast.error();
            ResponseError responseError = (ResponseError) error;
            showToast(responseError.getErrorMessage());
        }

    }
}


