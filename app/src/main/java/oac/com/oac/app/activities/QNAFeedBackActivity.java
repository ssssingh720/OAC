package oac.com.oac.app.activities;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import org.oacasia.R;
import oac.com.oac.app.load_toast.LoadToast;
import oac.com.oac.app.manager.SharedPrefManager;
import oac.com.oac.app.modal.BaseVO;
import oac.com.oac.app.modal.CurrentAgendaVo;
import oac.com.oac.app.modal.FeedParams;
import oac.com.oac.app.modal.ResponseError;
import oac.com.oac.app.networking.RequestManager;
import oac.com.oac.app.utils.APIMethods;
import oac.com.oac.app.utils.Util;

/**
 * Created by Sudhir Singh on 10,July,2018
 * ESS,
 * B-65,Sector 63,Noida.
 */
public class QNAFeedBackActivity extends AppBaseActivity implements View.OnClickListener {

    private Context context;

    private LoadToast loadToast;
    private EditText edtQuestion;
    private TextView txtAgendaTitle;
    private Spinner spnQuestTopic;

    private String topicList[] = {"Choose Topic", "Topic 1", "Topic 2", "Topic 3"};
    private String topicSelected = "Choose Topic";

    private String agendaId = "";

    boolean isFeedBackSubmitted=false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.quest_ans_fragment);

        context = QNAFeedBackActivity.this;

        LinearLayout lnrSessionBack = (LinearLayout) findViewById(R.id.lnrSessionBack);
        RelativeLayout relTopBack = (RelativeLayout) findViewById(R.id.relTopBack);
        edtQuestion = (EditText) findViewById(R.id.edtQuestion);
        txtAgendaTitle = (TextView) findViewById(R.id.txtAgendaTitle);
        spnQuestTopic = (Spinner) findViewById(R.id.spnQuestTopic);
        Button btnSubmit = (Button) findViewById(R.id.btnSubmit);
        ImageView imgQNASponsor = (ImageView) findViewById(R.id.imgQNASponsor);

        agendaId=getIntent().getExtras().getString(FeedParams.TYPE_ID);

        Picasso.with(context).load(RequestManager.LIVE_SERVER + SharedPrefManager.getInstance().
                getSharedDataString(FeedParams.QNA_LOGO))
                .placeholder(R.drawable.app_logo).error(R.drawable.app_logo).into(imgQNASponsor);

        relTopBack.setVisibility(View.VISIBLE);
        btnSubmit.setOnClickListener(this);

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int height = size.y;
        loadToast = new LoadToast(context);
        loadToast.setText("Please wait...");
        loadToast.setTranslationY(height / 2);
        loadToast.setTextColor(Color.BLACK).setBackgroundColor(Color.WHITE)
                .setProgressColor(getResources().getColor(R.color.colorPrimary));

        HashMap<String, String> params = new HashMap<>();
        String userId = SharedPrefManager.getInstance().getSharedDataString(FeedParams.USER_ID);
        String token = SharedPrefManager.getInstance().getSharedDataString(FeedParams.TOKEN);

        params.put(FeedParams.USER_ID, userId);
        params.put(FeedParams.TOKEN, token);
        params.put(FeedParams.AGENDA_ID, agendaId);

        placeRequest(APIMethods.CURRENT_AGENDA, CurrentAgendaVo.class, params, true, null);

        lnrSessionBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btnSubmit:

                if (Util.isNetworkOnline(context)) {

                    String question = edtQuestion.getText().toString();
//                    if (topicSelected.equalsIgnoreCase("choose Topic")) {
//                        showToast("Select Topic");
//                    } else
                    if (question.trim().length() <= 0) {
                        showToast("Enter your question");
                    } else {

                        loadToast.show();

                        HashMap<String, String> params = new HashMap<>();
                        String userId = SharedPrefManager.getInstance().getSharedDataString(FeedParams.USER_ID);
                        String token = SharedPrefManager.getInstance().getSharedDataString(FeedParams.TOKEN);

                        params.put(FeedParams.USER_ID, userId);
                        params.put(FeedParams.TOKEN, token);
                        params.put(FeedParams.QUESTION, question);
                        params.put(FeedParams.AGENDA_ID, agendaId);

                        placeRequest(APIMethods.QUES_ANS, BaseVO.class, params, true, null);
                    }
                } else {
                    showToast(getResources().getString(R.string.no_internet));
                }

                break;

        }
    }

    @Override
    public void onAPIResponse(Object response, String apiMethod) {
        super.onAPIResponse(response, apiMethod);

        if (apiMethod.equalsIgnoreCase(APIMethods.QUES_ANS)) {
            loadToast.success();
            BaseVO baseVO = (BaseVO) response;
            showToast("Thank You.\n " + baseVO.getMessage());
            edtQuestion.setText("");

            isFeedBackSubmitted=true;
            onBackPressed();

        }else if(apiMethod.equalsIgnoreCase(APIMethods.CURRENT_AGENDA)){
            CurrentAgendaVo currentAgendaVo = (CurrentAgendaVo) response;
            txtAgendaTitle.setText(currentAgendaVo.getCurrentAgenda().getTitle());
            agendaId=currentAgendaVo.getCurrentAgenda().getId();
        }
    }

    @Override
    public void onErrorResponse(VolleyError error, String apiMethod) {

        super.onErrorResponse(error, apiMethod);
        if (apiMethod.equalsIgnoreCase(APIMethods.QUES_ANS)) {
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
