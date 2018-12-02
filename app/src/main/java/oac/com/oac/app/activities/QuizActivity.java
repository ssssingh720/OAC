package oac.com.oac.app.activities;

import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.Display;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.squareup.picasso.Picasso;

import org.oacasia.R;

import java.util.HashMap;

import oac.com.oac.app.adapter.LoginSponsorAdapter;
import oac.com.oac.app.load_toast.LoadToast;
import oac.com.oac.app.manager.SharedPrefManager;
import oac.com.oac.app.modal.BaseVO;
import oac.com.oac.app.modal.FeedParams;
import oac.com.oac.app.modal.QuizVO;
import oac.com.oac.app.modal.ResponseError;
import oac.com.oac.app.modal.SponsorsVo;
import oac.com.oac.app.networking.RequestManager;
import oac.com.oac.app.utils.APIMethods;
import oac.com.oac.app.utils.MyBounceInterpolator;
import oac.com.oac.app.utils.Util;

public class QuizActivity extends AppBaseActivity implements View.OnClickListener {

    private Button btnSubmitOption;
    private LinearLayout lnrQuestionView;
    private TextView txtQuestionText;
    private RecyclerView rclSponsorRegister;
    private ImageView imgOptionA;
    private ImageView imgOptionB;
    private ImageView imgOptionC;
    private ImageView imgOptionD;
    private String optionSelected = "";

    private Animation myAnim;

    private int total_question = 0;
    private int current_question = 1;
    private QuizVO quizVO;

    private LoadToast loadToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_quiz);

        txtQuestionText = (TextView) findViewById(R.id.txtQuestionText);
        lnrQuestionView = (LinearLayout) findViewById(R.id.lnrQuestionView);
        rclSponsorRegister = (RecyclerView) findViewById(R.id.rclRegisterSponsor);
        imgOptionA = (ImageView) findViewById(R.id.imgOptionA);
        imgOptionB = (ImageView) findViewById(R.id.imgOptionB);
        imgOptionC = (ImageView) findViewById(R.id.imgOptionC);
        imgOptionD = (ImageView) findViewById(R.id.imgOptionD);

        ImageView quizLogo = (ImageView) findViewById(R.id.quizLogo);

        Picasso.with(QuizActivity.this).load(RequestManager.LIVE_SERVER + SharedPrefManager.getInstance().getSharedDataString(FeedParams.QUIZ_LOGO))
                .placeholder(R.drawable.app_logo).error(R.drawable.app_logo).into(quizLogo);

        myAnim = AnimationUtils.loadAnimation(this, R.anim.bounce);
        // Use bounce interpolator with amplitude 0.2 and frequency 20
        MyBounceInterpolator interpolator = new MyBounceInterpolator(0.2, 20);
        myAnim.setInterpolator(interpolator);

        imgOptionA.startAnimation(myAnim);
        imgOptionB.startAnimation(myAnim);
        imgOptionC.startAnimation(myAnim);
        imgOptionD.startAnimation(myAnim);

//        final RadioGroup rdgProfileType = (RadioGroup) findViewById(R.id.rdgProfileType);
//        final RadioButton option_a = (RadioButton) findViewById(R.id.option_a);
//        final RadioButton option_b = (RadioButton) findViewById(R.id.option_b);
//        final RadioButton option_c = (RadioButton) findViewById(R.id.option_c);
//        final RadioButton option_d = (RadioButton) findViewById(R.id.option_d);

        TextView txtPrizeText = (TextView) findViewById(R.id.txtPrizeText);
        btnSubmitOption = (Button) findViewById(R.id.btnSubmitOption);
        LinearLayout lnrQuizBack = (LinearLayout) findViewById(R.id.lnrQuizBack);

        quizVO = (QuizVO) getIntent().getExtras().getSerializable("QUIZ_DETAIL");

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            txtPrizeText.setText(Html.fromHtml(quizVO.getQuizDetail().getPrize_text(), Html.FROM_HTML_MODE_LEGACY));
        } else {
            txtPrizeText.setText(Html.fromHtml(quizVO.getQuizDetail().getPrize_text()));
        }

        total_question = quizVO.getQuizDetail().getQuestionCount();
        current_question = quizVO.getQuizDetail().getStartNo();
        txtQuestionText.setText(quizVO.getQuizDetail().getPreText() + " " + current_question);

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int height = size.y;
        loadToast = new LoadToast(QuizActivity.this);
        loadToast.setText("Please Wait...");
        loadToast.setTranslationY(height / 2);
        loadToast.setTextColor(Color.BLACK).setBackgroundColor(Color.WHITE)
                .setProgressColor(getResources().getColor(R.color.colorPrimary));

        btnSubmitOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Util.isNetworkOnline(QuizActivity.this)) {

                    HashMap<String, String> params = new HashMap<>();

                    if (optionSelected.length() > 0) {

//                        Util.makeViewGone(imgOptionA);
//                        Util.makeViewGone(imgOptionB);
//                        Util.makeViewGone(imgOptionC);
//                        Util.makeViewGone(imgOptionD);

                        imgOptionA.setVisibility(View.INVISIBLE);
                        imgOptionB.setVisibility(View.INVISIBLE);
                        imgOptionC.setVisibility(View.INVISIBLE);
                        imgOptionD.setVisibility(View.INVISIBLE);


                        btnSubmitOption.setEnabled(false);
                        loadToast.show();

                        String userId = SharedPrefManager.getInstance().getSharedDataString(FeedParams.USER_ID);
                        String token = SharedPrefManager.getInstance().getSharedDataString(FeedParams.TOKEN);

                        params.put(FeedParams.USER_ID, userId);
                        params.put(FeedParams.TOKEN, token);
                        params.put(FeedParams.OPTION, optionSelected);
                        params.put(FeedParams.QUIZ_ID, quizVO.getQuizDetail().getQuizId());
                        params.put(FeedParams.QUESTION_NO, "" + current_question);
                        placeRequest(APIMethods.QUIZ_RESPONSE, BaseVO.class, params, true, null);
                    } else {
                        showToast("Select an option");
                    }
                } else {
                    showToast(getResources().getString(R.string.no_internet));
                }
            }
        });

        lnrQuizBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        imgOptionA.setOnClickListener(this);
        imgOptionB.setOnClickListener(this);
        imgOptionC.setOnClickListener(this);
        imgOptionD.setOnClickListener(this);

//        placeRequest(APIMethods.SPONSOR_LIST, SponsorsVo.class, null, true, null);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.imgOptionA:
                imgOptionA.setImageResource(R.drawable.option_selected);
                imgOptionB.setImageResource(R.drawable.option_b);
                imgOptionC.setImageResource(R.drawable.option_c);
                imgOptionD.setImageResource(R.drawable.option_d);
                optionSelected = "A";
                break;

            case R.id.imgOptionB:
                imgOptionA.setImageResource(R.drawable.option_a);
                imgOptionB.setImageResource(R.drawable.option_selected);
                imgOptionC.setImageResource(R.drawable.option_c);
                imgOptionD.setImageResource(R.drawable.option_d);
                optionSelected = "B";
                break;

            case R.id.imgOptionC:
                imgOptionA.setImageResource(R.drawable.option_a);
                imgOptionB.setImageResource(R.drawable.option_b);
                imgOptionC.setImageResource(R.drawable.option_selected);
                imgOptionD.setImageResource(R.drawable.option_d);
                optionSelected = "C";
                break;

            case R.id.imgOptionD:
                imgOptionA.setImageResource(R.drawable.option_a);
                imgOptionB.setImageResource(R.drawable.option_b);
                imgOptionC.setImageResource(R.drawable.option_c);
                imgOptionD.setImageResource(R.drawable.option_selected);
                optionSelected = "D";
                break;
        }
    }

    @Override
    public void onAPIResponse(Object response, String apiMethod) {
        super.onAPIResponse(response, apiMethod);

        if (apiMethod.equalsIgnoreCase(APIMethods.QUIZ_RESPONSE)) {

            Util.makeViewVisible(imgOptionA);
            Util.makeViewVisible(imgOptionB);
            Util.makeViewVisible(imgOptionC);
            Util.makeViewVisible(imgOptionD);

            imgOptionA.startAnimation(myAnim);
            imgOptionB.startAnimation(myAnim);
            imgOptionC.startAnimation(myAnim);
            imgOptionD.startAnimation(myAnim);


            loadToast.success();
            BaseVO baseVO = (BaseVO) response;
            showToast(baseVO.getMessage());

            btnSubmitOption.setEnabled(true);

            optionSelected = "";
            imgOptionA.setImageResource(R.drawable.option_a);
            imgOptionB.setImageResource(R.drawable.option_b);
            imgOptionC.setImageResource(R.drawable.option_c);
            imgOptionD.setImageResource(R.drawable.option_d);

            current_question++;
            if (current_question > total_question) {
                txtQuestionText.setText(getResources().getString(R.string.quiz_thanks_text));
                lnrQuestionView.setVisibility(View.GONE);
            } else {
                txtQuestionText.setText(quizVO.getQuizDetail().getPreText() + " " + current_question);
            }
        } else if (apiMethod.equalsIgnoreCase(APIMethods.SPONSOR_LIST)) {
            SponsorsVo sponsorsVo = (SponsorsVo) response;

            rclSponsorRegister.setHasFixedSize(true);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
            rclSponsorRegister.setLayoutManager(mLayoutManager);

            LoginSponsorAdapter chapterVideoAdapter = new LoginSponsorAdapter(QuizActivity.this, sponsorsVo.getSponsorsList());
            rclSponsorRegister.setAdapter(chapterVideoAdapter);
        }
    }

    @Override
    public void onErrorResponse(VolleyError error, String apiMethod) {

        super.onErrorResponse(error, apiMethod);
        if (apiMethod.equalsIgnoreCase(APIMethods.QUIZ_RESPONSE)) {
            loadToast.error();


            Util.makeViewVisible(imgOptionA);
            Util.makeViewVisible(imgOptionB);
            Util.makeViewVisible(imgOptionC);
            Util.makeViewVisible(imgOptionD);


            btnSubmitOption.setEnabled(true);
            ResponseError responseError = (ResponseError) error;
            showToast(responseError.getErrorMessage());
        }

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();

        QuizActivity.this.finish();
    }
}
