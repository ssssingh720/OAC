package oac.com.oac.app.activities;

import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.text.InputType;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.android.volley.VolleyError;

import org.oacasia.R;

import java.util.HashMap;

//import org.oacasia.R;
import oac.com.oac.app.load_toast.LoadToast;
import oac.com.oac.app.manager.SharedPrefManager;
import oac.com.oac.app.modal.BaseVO;
import oac.com.oac.app.modal.FeedParams;
import oac.com.oac.app.modal.ResponseError;
import oac.com.oac.app.utils.APIMethods;
import oac.com.oac.app.utils.Util;

/**
 * Created by Sudhir Singh on 11,July,2017
 * ESS,
 * B-65,Sector 63,Noida.
 */
public class ChangePasswordActivity extends AppBaseActivity implements View.OnClickListener {

    private EditText edtOldPwd;
    private EditText edtNewPwd;
    private EditText edtCnfNewPwd;

    private CheckBox chkShowPwd;

    private LoadToast loadToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.change_pwd_activity);

        LinearLayout lnrChangePwdBack=(LinearLayout)findViewById(R.id.lnrChangePwdBack);
        edtOldPwd = (EditText) findViewById(R.id.edtOldPwd);
        edtNewPwd = (EditText) findViewById(R.id.edtNewPwd);
        edtCnfNewPwd = (EditText) findViewById(R.id.edtCnfNewPwd);
        chkShowPwd = (CheckBox) findViewById(R.id.chkShowPwd);
        Button btnChangePwd = (Button) findViewById(R.id.btnChangePwd);

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int height = size.y;
        loadToast = new LoadToast(ChangePasswordActivity.this);
        loadToast.setText("Updating Password...");
        loadToast.setTranslationY(height / 2);
        loadToast.setTextColor(Color.BLACK).setBackgroundColor(Color.WHITE)
                .setProgressColor(getResources().getColor(R.color.colorPrimary));

        btnChangePwd.setOnClickListener(this);
        lnrChangePwdBack.setOnClickListener(this);

        chkShowPwd
                .setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView,
                                                 boolean isChecked) {
                        if (isChecked) {
                            edtOldPwd
                                    .setInputType(InputType.TYPE_CLASS_TEXT
                                            | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                            edtNewPwd
                                    .setInputType(InputType.TYPE_CLASS_TEXT
                                            | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                            edtCnfNewPwd
                                    .setInputType(InputType.TYPE_CLASS_TEXT
                                            | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                        } else {
                            edtOldPwd.setInputType(InputType.TYPE_CLASS_TEXT
                                    | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                            edtNewPwd.setInputType(InputType.TYPE_CLASS_TEXT
                                    | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                            edtCnfNewPwd.setInputType(InputType.TYPE_CLASS_TEXT
                                    | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        }
                    }
                });
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btnChangePwd:

                if (edtOldPwd.getText().toString().length() <= 0) {
                    edtOldPwd.setError("Provide old password.");
                } else if (edtNewPwd.getText().toString().length() <= 0) {
                    edtNewPwd.setError("Provide new password.");
                } else if (edtOldPwd.getText().toString()
                        .equals(edtNewPwd.getText().toString())) {
                    edtNewPwd.setError("Old and New Password can't be same");
                    edtCnfNewPwd.setText("");
                } else if (edtCnfNewPwd.getText().toString().length() <= 0) {
                    edtCnfNewPwd.setError("Provide New Password again.");
                } else if (!edtNewPwd.getText().toString()
                        .equalsIgnoreCase(edtCnfNewPwd.getText().toString())) {
                    edtCnfNewPwd.setError("New and Old Password must be same.");
                    edtCnfNewPwd.setText("");
                } else if (!Util.isNetworkOnline(ChangePasswordActivity.this)) {
                    showToast(getResources().getString(R.string.no_internet));
                } else {
                    loadToast.show();
                    HashMap<String, String> params = new HashMap<>();

                    String userId = SharedPrefManager.getInstance().getSharedDataString(FeedParams.USER_ID);
                    String token = SharedPrefManager.getInstance().getSharedDataString(FeedParams.TOKEN);
//                    user_id, token, old_password, new_password, confirm_password
                    params.put(FeedParams.USER_ID, userId);
                    params.put(FeedParams.TOKEN, token);
                    params.put("old_password", edtOldPwd.getText().toString().trim());
                    params.put("new_password", edtNewPwd.getText().toString().trim());
                    params.put("confirm_password", edtCnfNewPwd.getText().toString().trim());

                    placeRequest(APIMethods.CHANGE_PASSWORD, BaseVO.class, params, true, null);
                }

                break;

            case R.id.lnrChangePwdBack:

                onBackPressed();

                break;
        }
    }

    @Override
    public void onAPIResponse(Object response, String apiMethod) {
        super.onAPIResponse(response, apiMethod);

        if (apiMethod.equalsIgnoreCase(APIMethods.CHANGE_PASSWORD)) {
            BaseVO baseVO = (BaseVO) response;
            loadToast.success();
            showToast(baseVO.getMessage());
        }
    }

    @Override
    public void onErrorResponse(VolleyError error, String apiMethod) {
        super.onErrorResponse(error, apiMethod);

        if (apiMethod.equalsIgnoreCase(APIMethods.CHANGE_PASSWORD)) {

            loadToast.error();
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
