package oac.com.oac.app.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import org.oacasia.R;
import oac.com.oac.app.activities.QuizActivity;
import oac.com.oac.app.load_toast.LoadToast;
import oac.com.oac.app.manager.SharedPrefManager;
import oac.com.oac.app.modal.FeedParams;
import oac.com.oac.app.modal.QuizVO;
import oac.com.oac.app.modal.ResponseError;
import oac.com.oac.app.networking.RequestManager;
import oac.com.oac.app.utils.APIMethods;
import oac.com.oac.app.utils.Util;

/**
 * Created by Sudhir Singh on 11,July,2017
 * ESS,
 * B-65,Sector 63,Noida.
 */
public class QuizFragment extends BaseFragment implements View.OnClickListener {
    private static int QUIZ_REQUEST = 200;
    private TextView txtQuizContent;
    private TextView txtPrizeText;
    private Button btnStartQuiz;

    private LoadToast loadToast;
    private QuizVO quizVo;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.quiz_fragment, container, false);

        txtQuizContent = (TextView) mView.findViewById(R.id.txtQuizContent);
        txtPrizeText = (TextView) mView.findViewById(R.id.txtPrizeText);
        btnStartQuiz = (Button) mView.findViewById(R.id.btnStartQuiz);
        ImageView imgQuizsponsor = (ImageView) mView.findViewById(R.id.imgQuizsponsor);

        Picasso.with(getActivity()).load(RequestManager.LIVE_SERVER+SharedPrefManager.getInstance().
                getSharedDataString(FeedParams.QUIZ_LOGO))
                .placeholder(R.drawable.app_logo).error(R.drawable.app_logo).into(imgQuizsponsor);

        btnStartQuiz.setVisibility(View.GONE);

        Display display = getActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int height = size.y;
        loadToast = new LoadToast(getActivity());
        loadToast.setText("Please wait...");
        loadToast.setTranslationY(height / 2);
        loadToast.setTextColor(Color.BLACK).setBackgroundColor(Color.WHITE)
                .setProgressColor(getResources().getColor(R.color.colorPrimary));

        getQuizDetail();

        btnStartQuiz.setOnClickListener(this);

        return mView;
    }

    private void getQuizDetail() {
        if (Util.isNetworkOnline(getActivity())) {
            loadToast.show();

            HashMap<String, String> params = new HashMap<>();
            String userId = SharedPrefManager.getInstance().getSharedDataString(FeedParams.USER_ID);
            String token = SharedPrefManager.getInstance().getSharedDataString(FeedParams.TOKEN);

            params.put(FeedParams.USER_ID, userId);
            params.put(FeedParams.TOKEN, token);

            placeRequest(APIMethods.QUIZ_INTRO, QuizVO.class, params, true, null);
        } else {
            showToast(getResources().getString(R.string.no_internet));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btnStartQuiz:

                if (Util.isNetworkOnline(getActivity())) {

                    Intent activityQuizIntent = new Intent(getActivity(), QuizActivity.class);
                    activityQuizIntent.putExtra("QUIZ_DETAIL", quizVo);
                    startActivityForResult(activityQuizIntent, QUIZ_REQUEST);

                } else {
                    showToast(getResources().getString(R.string.no_internet));
                }
                break;

        }
    }

    @Override
    public void onAPIResponse(Object response, String apiMethod) {
        super.onAPIResponse(response, apiMethod);

        if (apiMethod.equalsIgnoreCase(APIMethods.QUIZ_INTRO)) {
            loadToast.success();
            quizVo = (QuizVO) response;
            btnStartQuiz.setVisibility(View.VISIBLE);

            if (quizVo.getQuizDetail().getStatus() == 0) {
                btnStartQuiz.setVisibility(View.GONE);
            } else if (quizVo.getQuizDetail().getStartNo() >= quizVo.getQuizDetail().getQuestionCount()) {
                btnStartQuiz.setVisibility(View.GONE);
            } else if (quizVo.getQuizDetail().getStartNo() > 1) {
                btnStartQuiz.setVisibility(View.VISIBLE);
                btnStartQuiz.setText("Continue Quiz");
            } else {
                btnStartQuiz.setText("Start Quiz Now");
                btnStartQuiz.setVisibility(View.VISIBLE);
            }

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                txtQuizContent.setText(Html.fromHtml(quizVo.getQuizDetail().getQuestionText(), Html.FROM_HTML_MODE_LEGACY));
                txtPrizeText.setText(Html.fromHtml(quizVo.getQuizDetail().getPrize_text(), Html.FROM_HTML_MODE_LEGACY));
            } else {
                txtQuizContent.setText(Html.fromHtml(quizVo.getQuizDetail().getQuestionText()));
                txtPrizeText.setText(Html.fromHtml(quizVo.getQuizDetail().getPrize_text()));
            }

        }
    }

    @Override
    public void onErrorResponse(VolleyError error, String apiMethod) {

        super.onErrorResponse(error, apiMethod);
        loadToast.error();
        ResponseError responseError = (ResponseError) error;
        showToast(responseError.getErrorMessage());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == QUIZ_REQUEST) {
            getQuizDetail();
        }
    }
}
