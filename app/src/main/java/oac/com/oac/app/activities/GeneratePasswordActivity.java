package oac.com.oac.app.activities;

import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.android.volley.VolleyError;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;
import org.oacasia.R;

import java.util.HashMap;

import oac.com.oac.app.load_toast.LoadToast;
import oac.com.oac.app.modal.BaseVO;
import oac.com.oac.app.modal.FeedParams;
import oac.com.oac.app.utils.APIMethods;
import oac.com.oac.app.utils.Util;

/**
 * Created by Sudhir Singh on 21,July,2018
 * ESS,
 * B-65,Sector 63,Noida.
 */
public class GeneratePasswordActivity extends AppBaseActivity {

    private LoadToast loadToast;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.generate_password_activity);

        final EditText emailField = (EditText) findViewById(R.id.forgot_password_email);
        Button resetPasswordBtn = (Button) findViewById(R.id.reset_password_button);
        LinearLayout lnrGeneratePwdBack = (LinearLayout) findViewById(R.id.lnrGeneratePwdBack);

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int height = size.y;
        loadToast = new LoadToast(GeneratePasswordActivity.this);
        loadToast.setText("Please Wait...");
        loadToast.setTranslationY(height / 2);
        loadToast.setTextColor(Color.BLACK).setBackgroundColor(Color.WHITE)
                .setProgressColor(getResources().getColor(R.color.colorPrimary));

        resetPasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailField.getText().toString().trim();
                if (email.length() > 0 && Util.isValidEmail(email)) {
                    if (Util.isNetworkOnline(GeneratePasswordActivity.this)) {
                        recoverPassword(email);
                    } else {
                        showToast(getResources().getString(R.string.no_internet));
                    }
                } else {
                    showToast(getResources().getString(R.string.email_to_get_password));
                }
            }
        });

        lnrGeneratePwdBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    private void recoverPassword(String email) {
        loadToast.show();
        HashMap<String, String> params = new HashMap<String, String>();
        params.put(FeedParams.EMAIL, email);
        placeRequest(APIMethods.RESET_PASSWORD, BaseVO.class, params, true, null);
    }

    @Override
    public void onAPIResponse(Object response, String apiMethod) {
        super.onAPIResponse(response, apiMethod);

        if (apiMethod.equalsIgnoreCase(APIMethods.RESET_PASSWORD)) {
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
                showToast(getString(R.string.password_sent));
            }

            onBackPressed();
        }
    }


    @Override
    public void onErrorResponse(VolleyError error, String apiMethod) {
        super.onErrorResponse(error, apiMethod);

        loadToast.error();

        if (apiMethod.equalsIgnoreCase(APIMethods.RESET_PASSWORD)) {
            try {
                JSONObject errorObject = new JSONObject(new String(error.networkResponse.data));
                // JSONObject result = errorObject.getJSONObject("result");
                String message = errorObject.getString("message");
                showToast(message);

            } catch (JSONException e) {
                e.printStackTrace();
                showToast(getString(R.string.couldnt_generate_pwd));
            } catch (Exception e) {
                e.printStackTrace();
                showToast(getString(R.string.couldnt_generate_pwd));
            }
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        GeneratePasswordActivity.this.finish();
        overridePendingTransition(R.anim.back_slide_in, R.anim.back_slide_out);
    }
}
