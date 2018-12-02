package oac.com.oac.app.activities;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.volley.VolleyError;

import java.util.HashMap;

import org.oacasia.R;
import oac.com.oac.app.load_toast.LoadToast;
import oac.com.oac.app.manager.SharedPrefManager;
import oac.com.oac.app.modal.BaseVO;
import oac.com.oac.app.modal.CurrentAgendaVo;
import oac.com.oac.app.modal.FeedParams;
import oac.com.oac.app.modal.ResponseError;
import oac.com.oac.app.utils.APIMethods;
import oac.com.oac.app.utils.Util;

/**
 * Created by Sudhir Singh on 10,July,2018
 * ESS,
 * B-65,Sector 63,Noida.
 */
public class FeedbackSessionActivity extends AppBaseActivity {


    private Context context;

    private String agendaId;
    private Button btnSubmitfeedBack;
    private    TextView txtSessionFebackTitle;

    private LoadToast loadToast;

    private boolean isFeedBackSubmitted=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.feedback_session_activity);

        context = FeedbackSessionActivity.this;

        agendaId = getIntent().getExtras().getString(FeedParams.TYPE_ID);

         txtSessionFebackTitle = (TextView) findViewById(R.id.txtSessionFebackTitle);
        final RatingBar rtbUse_Business = (RatingBar) findViewById(R.id.rtbUse_Business);
        final TextView txtUse_Business = (TextView) findViewById(R.id.txtUse_Business);

        final RatingBar rtbQual_Present = (RatingBar) findViewById(R.id.rtbQual_Present);
        final TextView txtQual_Present = (TextView) findViewById(R.id.txtQual_Present);

        final RatingBar rtbOverallExp = (RatingBar) findViewById(R.id.rtbOverallExp);
        final TextView txtOverallExp = (TextView) findViewById(R.id.txtOverallExp);

        final EditText edtFeebackComment = (EditText) findViewById(R.id.edtFeebackComment);
        btnSubmitfeedBack = (Button) findViewById(R.id.btnSubmitfeedBack);
        LinearLayout lnrOACFeedbackBack = (LinearLayout) findViewById(R.id.lnrOACFeedbackBack);

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int height = size.y;
        loadToast = new LoadToast(context);
        loadToast.setText("Submitting Feedback...");
        loadToast.setTranslationY(height / 2);
        loadToast.setTextColor(Color.BLACK).setBackgroundColor(Color.WHITE)
                .setProgressColor(getResources().getColor(R.color.colorPrimary));

        btnSubmitfeedBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Util.isNetworkOnline(context)) {
                    if (rtbUse_Business.getRating() <= 0) {
                        showToast("Please Give usefulness to my Business Rating");
                    } else if (rtbQual_Present.getRating() <= 0) {
                        showToast("Please Give Quality of Presentation Rating");
                    } else if (rtbOverallExp.getRating() <= 0) {
                        showToast("Please Give Overall Experiences Rating");
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
                        params.put(FeedParams.AGENDA_ID, agendaId);
                        params.put("rating_topic", "" + rtbUse_Business.getRating());
                        params.put("rating_content", "" + rtbQual_Present.getRating());
                        params.put("rating_presentation", "" + rtbOverallExp.getRating());
                        params.put(FeedParams.COMMENT, edtFeebackComment.getText().toString().trim());

                        placeRequest(APIMethods.SUBMIT_FEEDBACK, BaseVO.class, params, true, null);
                    }
                } else {
                    showToast(getResources().getString(R.string.no_internet));
                }
            }
        });

        rtbUse_Business.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                changeRatingText(rating,txtUse_Business);
            }
        });

        rtbQual_Present.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                changeRatingText(rating,txtQual_Present);
            }
        });

        rtbOverallExp.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                changeRatingText(rating,txtOverallExp);
            }
        });


        HashMap<String, String> params = new HashMap<>();
        String userId = SharedPrefManager.getInstance().getSharedDataString(FeedParams.USER_ID);
        String token = SharedPrefManager.getInstance().getSharedDataString(FeedParams.TOKEN);

        params.put(FeedParams.USER_ID, userId);
        params.put(FeedParams.TOKEN, token);
        params.put(FeedParams.AGENDA_ID, agendaId);

        placeRequest(APIMethods.CURRENT_AGENDA, CurrentAgendaVo.class, params, true, null);

        lnrOACFeedbackBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }


    private void changeRatingText(float rating, TextView textView) {

        if (rating <= 1) {
            textView.setText("Poor");
        } else if (rating > 1 && rating <= 2) {
            textView.setText("Fair");
        } else if (rating > 2 && rating <= 3) {
            textView.setText("Average");
        }  else if (rating > 3 && rating <= 4) {
            textView.setText("Good");
        }else {
            textView.setText("Excellent");
        }

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
        }else if(apiMethod.equalsIgnoreCase(APIMethods.CURRENT_AGENDA)){
            CurrentAgendaVo currentAgendaVo = (CurrentAgendaVo) response;
            txtSessionFebackTitle.setText(currentAgendaVo.getCurrentAgenda().getTitle());
            agendaId=currentAgendaVo.getCurrentAgenda().getId();
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
