package oac.com.oac.app.activities;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import org.oacasia.R;
import oac.com.oac.app.load_toast.LoadToast;
import oac.com.oac.app.manager.SharedPrefManager;
import oac.com.oac.app.modal.FeedParams;
import oac.com.oac.app.modal.ResponseError;
import oac.com.oac.app.modal.SpeakersVo;
import oac.com.oac.app.networking.RequestManager;
import oac.com.oac.app.utils.APIMethods;
import oac.com.oac.app.utils.Util;

public class SelectSpeakerActivity extends AppBaseActivity implements View.OnClickListener {

    private LinearLayout lnrSpeaker;

    private LoadToast loadToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_speaker);

        LinearLayout lnrSelectSpeakerBack = (LinearLayout) findViewById(R.id.lnrSelectSpeakerBack);
        ImageView imgTopRightLogo = (ImageView) findViewById(R.id.imgTopRightLogo);
        lnrSpeaker = (LinearLayout) findViewById(R.id.lnrSpeaker);

        Picasso.with(SelectSpeakerActivity.this).load(RequestManager.LIVE_SERVER + SharedPrefManager.getInstance().getSharedDataString(FeedParams.OAC_LOGO))
                .placeholder(R.drawable.app_logo).error(R.drawable.app_logo).into(imgTopRightLogo);

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int height = size.y;
        loadToast = new LoadToast(SelectSpeakerActivity.this);
        loadToast.setText("Please wait...");
        loadToast.setTranslationY(height / 2);
        loadToast.setTextColor(Color.BLACK).setBackgroundColor(Color.WHITE)
                .setProgressColor(getResources().getColor(R.color.colorPrimary));

        if (Util.isNetworkOnline(SelectSpeakerActivity.this)) {
            loadToast.show();

            HashMap<String, String> params = new HashMap<>();
            String userId = SharedPrefManager.getInstance().getSharedDataString(FeedParams.USER_ID);
            String token = SharedPrefManager.getInstance().getSharedDataString(FeedParams.TOKEN);

            params.put(FeedParams.USER_ID, userId);
            params.put(FeedParams.TOKEN, token);

            placeRequest(APIMethods.SPEAKER_LIST, SpeakersVo.class, params, true, null);
        } else {
            showToast(getResources().getString(R.string.no_internet));
        }

        lnrSelectSpeakerBack.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.lnrSelectSpeakerBack:
                onBackPressed();
                break;


        }
    }

    @Override
    public void onAPIResponse(Object response, String apiMethod) {
        super.onAPIResponse(response, apiMethod);

        if (apiMethod.equalsIgnoreCase(APIMethods.SPEAKER_LIST)) {
            loadToast.success();
            SpeakersVo speakersVo = (SpeakersVo) response;
            showSpeakers(speakersVo);
        }
    }

    @Override
    public void onErrorResponse(VolleyError error, String apiMethod) {

        super.onErrorResponse(error, apiMethod);
        loadToast.error();
        ResponseError responseError = (ResponseError) error;
        showToast(responseError.getErrorMessage());
    }

    private void showSpeakers(final SpeakersVo speakersVo) {
        lnrSpeaker.removeAllViews();
        for (int counter = 0; counter < speakersVo.getSpeakersList().size(); counter++) {
            View itemView = LayoutInflater.from(SelectSpeakerActivity.this)
                    .inflate(R.layout.help_exp_child, null, false);

            TextView txtExpChild = (TextView) itemView.findViewById(R.id.txtExpChild);
            txtExpChild.setText(speakersVo.getSpeakersList().get(counter).getName());
            txtExpChild.setTag(counter);
            txtExpChild.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int positionClicked = (Integer) v.getTag();
                    Intent speakerfeedbackIntent = new Intent(SelectSpeakerActivity.this, OACSpeakerFeedbackActivity.class);
                    speakerfeedbackIntent.putExtra("SPEAKER_DETAIL", speakersVo.getSpeakersList().get(positionClicked));
                    startActivity(speakerfeedbackIntent);
                    SelectSpeakerActivity.this.finish();
                }
            });


            lnrSpeaker.addView(itemView);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        SelectSpeakerActivity.this.finish();
    }
}
