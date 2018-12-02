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
import oac.com.oac.app.modal.SpeakerDetail;
import oac.com.oac.app.networking.RequestManager;
import oac.com.oac.app.utils.APIMethods;
import oac.com.oac.app.utils.Util;

public class OACSpeakerFeedbackActivity extends AppBaseActivity {

    private Button btnSubmitfeedBack;
    private LoadToast loadToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oacspeaker_feedback);

        final SpeakerDetail speakersVo = (SpeakerDetail) getIntent().getExtras().get("SPEAKER_DETAIL");

        TextView txtRateText = (TextView) findViewById(R.id.txtRateText);
        ImageView imgOACSpeakerfeedback = (ImageView) findViewById(R.id.imgOACSpeakerfeedback);
        TextView txtSpeakerName = (TextView) findViewById(R.id.txtSpeakerName);
        final RatingBar rateTopic = (RatingBar) findViewById(R.id.rateTopic);
        final RatingBar rateContent = (RatingBar) findViewById(R.id.rateContent);
        final RatingBar ratePresentation = (RatingBar) findViewById(R.id.ratePresentation);
        final EditText edtFeebackComment = (EditText) findViewById(R.id.edtFeebackComment);
        btnSubmitfeedBack = (Button) findViewById(R.id.btnSubmitfeedBack);
        LinearLayout lnrOACFeedbackBack = (LinearLayout) findViewById(R.id.lnrOACFeedbackBack);

        Picasso.with(OACSpeakerFeedbackActivity.this).load(RequestManager.LIVE_SERVER + SharedPrefManager.getInstance().getSharedDataString(FeedParams.OAC_LOGO))
                .placeholder(R.drawable.app_logo).error(R.drawable.app_logo).into(imgOACSpeakerfeedback);


        txtRateText.setText(getResources().getString(R.string.rate_feedback) + " OAC Speakers");
        txtSpeakerName.setText(speakersVo.getName());

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int height = size.y;
        loadToast = new LoadToast(OACSpeakerFeedbackActivity.this);
        loadToast.setText("Submitting Feedback...");
        loadToast.setTranslationY(height / 2);
        loadToast.setTextColor(Color.BLACK).setBackgroundColor(Color.WHITE)
                .setProgressColor(getResources().getColor(R.color.colorPrimary));

        btnSubmitfeedBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Util.isNetworkOnline(OACSpeakerFeedbackActivity.this)) {
                    if (rateTopic.getRating() <= 0) {
                        showToast("Please Give Topic Rating");
                    } else if (rateContent.getRating() <= 0) {
                        showToast("Please Give Content Rating");
                    } else if (ratePresentation.getRating() <= 0) {
                        showToast("Please Give Presentation Rating");
                    } else if (edtFeebackComment.getText().toString().trim().length() <= 0) {
                        edtFeebackComment.setError("Enter Comments");
                    } else {
                        btnSubmitfeedBack.setEnabled(false);
                        loadToast.show();
                        HashMap<String, String> params = new HashMap<>();

                        String userId = SharedPrefManager.getInstance().getSharedDataString(FeedParams.USER_ID);
                        String token = SharedPrefManager.getInstance().getSharedDataString(FeedParams.TOKEN);
//                  user_id, token, speaker_id, rating_topic [1,2,3,4,5], rating_content [1,2,3,4,5], rating_presentation [1,2,3,4,5], comment
                        params.put(FeedParams.USER_ID, userId);
                        params.put(FeedParams.TOKEN, token);
                        params.put("speaker_id", speakersVo.getSpeaker_id());
                        params.put("rating_topic", "" + rateTopic.getRating());
                        params.put("rating_content", "" + rateContent.getRating());
                        params.put("rating_presentation", "" + ratePresentation.getRating());
                        params.put(FeedParams.COMMENT, edtFeebackComment.getText().toString().trim());

                        placeRequest(APIMethods.SUBMIT_FEEDBACK, BaseVO.class, params, true, null);
                    }
                } else {
                    showToast(getResources().getString(R.string.no_internet));
                }
            }
        });


        lnrOACFeedbackBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OACSpeakerFeedbackActivity.this.finish();
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
}
