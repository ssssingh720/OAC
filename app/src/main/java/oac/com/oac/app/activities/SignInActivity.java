package oac.com.oac.app.activities;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;
import org.oacasia.R;

import java.util.HashMap;

import oac.com.oac.app.adapter.LoginSponsorAdapter;
import oac.com.oac.app.load_toast.LoadToast;
import oac.com.oac.app.manager.SharedPrefManager;
import oac.com.oac.app.modal.AppImagesVo;
import oac.com.oac.app.modal.BaseVO;
import oac.com.oac.app.modal.FeedParams;
import oac.com.oac.app.modal.ResponseError;
import oac.com.oac.app.modal.SponsorsVo;
import oac.com.oac.app.modal.UserProfileVo;
import oac.com.oac.app.utils.APIMethods;
import oac.com.oac.app.utils.Util;

/**
 * Created by Sudhir Singh on 04,July,2017
 * ESS,
 * B-65,Sector 63,Noida.
 */
public class SignInActivity extends AppBaseActivity implements View.OnClickListener {

    private EditText edtSignInEmail;
    private EditText edtSignInPwd;
    private RecyclerView rclSponsorRegister;

    private Dialog forgotPasswordPopUp;

    private LoadToast loadToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.sign_in_activity);

        edtSignInEmail = (EditText) findViewById(R.id.edtSignInEmail);
        edtSignInPwd = (EditText) findViewById(R.id.edtSignInPwd);
        Button btnLogin = (Button) findViewById(R.id.btnLogin);
        TextView txtForgotPwd = (TextView) findViewById(R.id.txtForgotPwd);
        rclSponsorRegister = (RecyclerView) findViewById(R.id.rclRegisterSponsor);

        btnLogin.setOnClickListener(this);
        txtForgotPwd.setOnClickListener(this);

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int height = size.y;
        loadToast = new LoadToast(SignInActivity.this);
        loadToast.setText("Please Wait...");
        loadToast.setTranslationY(height / 2);
        loadToast.setTextColor(Color.BLACK).setBackgroundColor(Color.WHITE)
                .setProgressColor(getResources().getColor(R.color.colorPrimary));

//        HashMap<String, String> params = new HashMap<>();
//        params.put(FeedParams.USER_ID, "7");
//        params.put(FeedParams.TOKEN, "a7f0ed54ac7db5d14877f083ae29753b");

        //  placeRequest(APIMethods.SPONSOR_LIST, SponsorsVo.class, null, true, null);

//        edtSignInEmail.setText("test5@gmail.com");
//        edtSignInPwd.setText("12345");


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btnLogin:

                if (Util.isNetworkOnline(SignInActivity.this)) {

                    if (isValidData()) {
                        loadToast.show();

                        String gcmToken = FirebaseInstanceId.getInstance().getToken();

                        HashMap<String, String> params = new HashMap<>();
                        params.put(FeedParams.EMAIL, edtSignInEmail.getText().toString().trim());
                        params.put(FeedParams.PASSWORD, edtSignInPwd.getText().toString().trim());
                        if (gcmToken != null && gcmToken.length() > 0) {
                            params.put(FeedParams.DEVICE_TYPE, FeedParams.DEVICE_TYPE_NAME);
                            params.put(FeedParams.DEVICE_ID, gcmToken);
                        }
                        placeRequest(APIMethods.LOGIN_API, UserProfileVo.class, params, true, null);
                    }
                } else {
                    showToast(R.string.no_internet);
                }

                break;

            case R.id.txtForgotPwd:
                Intent generatePwd = new Intent(SignInActivity.this, GeneratePasswordActivity.class);
                startActivity(generatePwd);
                overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
                break;

        }
    }

    private void fetchAppImages()    {
        if (Util.isNetworkOnline(SignInActivity.this)) {

            HashMap<String, String> params = new HashMap<>();

            placeRequest(APIMethods.APP_IMAGE_API, AppImagesVo.class, params, true, null);
        } else {
            showToast(getResources().getString(R.string.no_internet));
        }
    }

    @Override
    public void onAPIResponse(Object response, String apiMethod) {
        super.onAPIResponse(response, apiMethod);

        if (apiMethod.equalsIgnoreCase(APIMethods.LOGIN_API)) {
            loadToast.success();

            UserProfileVo userProfileVo = (UserProfileVo) response;
            SharedPrefManager.getInstance().setSharedData(FeedParams.USER_ID, userProfileVo.getUserId());
            SharedPrefManager.getInstance().setSharedData(FeedParams.NAME, userProfileVo.getName());
            SharedPrefManager.getInstance().setSharedData(FeedParams.EMAIL, userProfileVo.getUserEmail());
            SharedPrefManager.getInstance().setSharedData(FeedParams.IMAGE, userProfileVo.getImage());
            SharedPrefManager.getInstance().setSharedData(FeedParams.TOKEN, userProfileVo.getToken());
            SharedPrefManager.getInstance().setSharedData(FeedParams.QR_CODE, userProfileVo.getQrcode());
            SharedPrefManager.getInstance().setSharedData(FeedParams.QR_CODE_NO, userProfileVo.getQrcode_no());
            SharedPrefManager.getInstance().setSharedData(FeedParams.NOTIFICATION_STATUS, true);

            if (SharedPrefManager.getInstance().getSharedDataString(FeedParams.OAA_LOGO).length() <= 0) {
                fetchAppImages();
            } else {
                Intent landingIntent = new Intent(SignInActivity.this, LandingActivity.class);
                landingIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//            landingIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            landingIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(landingIntent);
                finish();
            }
        } else if (apiMethod.equalsIgnoreCase(APIMethods.APP_IMAGE_API)) {

            AppImagesVo appImagesVo = (AppImagesVo) response;

            SharedPrefManager.getInstance().setSharedData(FeedParams.OAA_LOGO, appImagesVo.getAboutUs().getOaaLogo());
            SharedPrefManager.getInstance().setSharedData(FeedParams.OAC_LOGO, appImagesVo.getAboutUs().getOacLogo());
            SharedPrefManager.getInstance().setSharedData(FeedParams.SPLASH_LOGO, appImagesVo.getAboutUs().getSplashLogo());
            SharedPrefManager.getInstance().setSharedData(FeedParams.QUIZ_LOGO, appImagesVo.getAboutUs().getQuizLogo());
            SharedPrefManager.getInstance().setSharedData(FeedParams.QNA_LOGO, appImagesVo.getAboutUs().getQnaLogo());

            String dataStr = new Gson().toJson(appImagesVo.getAboutUs().getSponsor_banner());
            SharedPrefManager.getInstance().setSharedData(FeedParams.SPONSOR_BANNER, dataStr);

            Intent landingIntent = new Intent(SignInActivity.this, LandingActivity.class);
            landingIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//            landingIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            landingIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(landingIntent);
            finish();
        } else if (apiMethod.equalsIgnoreCase(APIMethods.SPONSOR_LIST)) {
            SponsorsVo sponsorsVo = (SponsorsVo) response;

            rclSponsorRegister.setHasFixedSize(true);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
            rclSponsorRegister.setLayoutManager(mLayoutManager);

            LoginSponsorAdapter chapterVideoAdapter = new LoginSponsorAdapter(SignInActivity.this, sponsorsVo.getSponsorsList());
            rclSponsorRegister.setAdapter(chapterVideoAdapter);
        } else if (apiMethod.equalsIgnoreCase(APIMethods.RESET_PASSWORD)) {
            loadToast.success();
            try {
                BaseVO resultData = (BaseVO) response;
                Gson gson = new Gson();
                String json = gson.toJson(resultData);
                JSONObject responseObj = new JSONObject(new String(json));
                //JSONObject result = responseObj.getJSONObject("result");
                String message = responseObj.getString("message");
                showToast(message);
            } catch (Exception e) {
                e.printStackTrace();
                showToast("Password has been sent to your registered email");
            }
        }
    }


    @Override
    public void onErrorResponse(VolleyError error, String apiMethod) {
        super.onErrorResponse(error, apiMethod);

        loadToast.error();

        if (apiMethod.equalsIgnoreCase(APIMethods.LOGIN_API)) {

            ResponseError responseError = (ResponseError) error;
            showToast(responseError.getErrorMessage());

        } else if (apiMethod.equalsIgnoreCase(APIMethods.RESET_PASSWORD)) {
            try {
                JSONObject errorObject = new JSONObject(new String(error.networkResponse.data));
                // JSONObject result = errorObject.getJSONObject("result");
                String message = errorObject.getString("message");
                showToast(message);

            } catch (JSONException e) {
                e.printStackTrace();
                showToast("Couldn't recover your password. Please try later.");
            } catch (Exception e) {
                e.printStackTrace();
                showToast("Couldn't recover your password. Please try later.");
            }
        }

    }


    private boolean isValidData() {

        if (edtSignInEmail.getText().toString().trim().length() <= 0) {
            edtSignInEmail.setError("Provide Email.");
            return false;
        } else if (!Util.isValidEmail(edtSignInEmail.getText().toString())) {
            edtSignInEmail.setError("Provide Valid Email.");
            return false;
        } else if (edtSignInPwd.getText().toString().trim().length() <= 0) {
            edtSignInPwd.setError("Provide Password.");
            return false;
        }

        return true;

    }

}
