package oac.com.oac.app.activities;

import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import org.oacasia.R;
import oac.com.oac.app.load_toast.LoadToast;
import oac.com.oac.app.manager.SharedPrefManager;
import oac.com.oac.app.modal.BaseVO;
import oac.com.oac.app.modal.FeedParams;
import oac.com.oac.app.modal.ResponseError;
import oac.com.oac.app.networking.RequestManager;
import oac.com.oac.app.utils.APIMethods;
import oac.com.oac.app.utils.Util;

public class OACFeedbackActivity extends AppBaseActivity {

    private Button btnSubmitfeedBack;
    private LoadToast loadToast;

    private  boolean isFeedBackSubmitted=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oacfeedback);

        TextView txtRateText = (TextView) findViewById(R.id.txtRateText);
        final RatingBar rateFeedback = (RatingBar) findViewById(R.id.rateFeedback);
        final EditText edtFeebackComment = (EditText) findViewById(R.id.edtFeebackComment);
        btnSubmitfeedBack = (Button) findViewById(R.id.btnSubmitfeedBack);
        LinearLayout lnrOACFeedbackBack = (LinearLayout) findViewById(R.id.lnrOACFeedbackBack);
        ImageView imgFeedbackLogo = (ImageView) findViewById(R.id.imgFeedbackLogo);

        final String rate_text = getIntent().getExtras().getString("RATE_TEXT");

        txtRateText.setText(getResources().getString(R.string.rate_feedback) + " " + rate_text);

//        if (rate_text.equalsIgnoreCase(getResources().getString(R.string.oaa_expo))) {
//            Picasso.with(OACFeedbackActivity.this).load(RequestManager.LIVE_SERVER +
//                    SharedPrefManager.getInstance().getSharedDataString(FeedParams.OAA_LOGO))
//                    .placeholder(R.drawable.app_logo).error(R.drawable.app_logo).into(imgFeedbackLogo);
////            imgFeedbackLogo.setImageResource(R.drawable.jury_logo);
//        } else {
//            Picasso.with(OACFeedbackActivity.this).load(RequestManager.LIVE_SERVER +
//                    SharedPrefManager.getInstance().getSharedDataString(FeedParams.OAC_LOGO))
//                    .placeholder(R.drawable.app_logo).error(R.drawable.app_logo).into(imgFeedbackLogo);
////            imgFeedbackLogo.setImageResource(R.drawable.logo);
//        }



        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int height = size.y;
        loadToast = new LoadToast(OACFeedbackActivity.this);
        loadToast.setText("Submitting Feedback...");
        loadToast.setTranslationY(height / 2);
        loadToast.setTextColor(Color.BLACK).setBackgroundColor(Color.WHITE)
                .setProgressColor(getResources().getColor(R.color.colorPrimary));


        lnrOACFeedbackBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btnSubmitfeedBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Util.isNetworkOnline(OACFeedbackActivity.this)) {
                    if (rateFeedback.getRating() <= 0) {
                        showToast("Please Give Rating");
                    } else if (edtFeebackComment.getText().toString().trim().length() <= 0) {
                        edtFeebackComment.setError("Enter Comments");
                    } else {
                        btnSubmitfeedBack.setEnabled(false);
                        loadToast.show();
                        HashMap<String, String> params = new HashMap<>();

                        String userId = SharedPrefManager.getInstance().getSharedDataString(FeedParams.USER_ID);
                        String token = SharedPrefManager.getInstance().getSharedDataString(FeedParams.TOKEN);
//                    user_id, token, section ['OAC 2017', 'OOH Expo', 'OAA 2017'], rating [1,2,3,4,5], comment
                        params.put(FeedParams.USER_ID, userId);
                        params.put(FeedParams.TOKEN, token);
                        params.put(FeedParams.SECTION, rate_text);
                        params.put(FeedParams.RATING, "" + rateFeedback.getRating());
                        params.put(FeedParams.COMMENT, edtFeebackComment.getText().toString().trim());

                        placeRequest(APIMethods.SUBMIT_FEEDBACK, BaseVO.class, params, true, null);
                    }
                } else {
                    showToast(getResources().getString(R.string.no_internet));
                }
            }
        });
    }

    @Override
    public void onAPIResponse(Object response, String apiMethod) {
        super.onAPIResponse(response, apiMethod);
        if (apiMethod.equalsIgnoreCase(APIMethods.SUBMIT_FEEDBACK)) {
            btnSubmitfeedBack.setEnabled(true);
            BaseVO baseVO = (BaseVO) response;
            loadToast.success();
            showToast(baseVO.getMessage());
            isFeedBackSubmitted=true;
            onBackPressed();
        }
    }

    @Override
    public void onErrorResponse(VolleyError error, String apiMethod) {
        super.onErrorResponse(error, apiMethod);

        if (apiMethod.equalsIgnoreCase(APIMethods.SUBMIT_FEEDBACK)) {
            btnSubmitfeedBack.setEnabled(true);
            loadToast.error();
            ResponseError responseError = (ResponseError) error;
            showToast(responseError.getErrorMessage());

        }
    }

    @Override
    public void onBackPressed() {
        if(isFeedBackSubmitted) {
            setResult(RESULT_OK);
        }else{
            setResult(RESULT_CANCELED);
        }
        finish();

    }
}
