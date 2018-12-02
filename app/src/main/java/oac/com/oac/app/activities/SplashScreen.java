package oac.com.oac.app.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ImageView;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import org.oacasia.R;
import oac.com.oac.app.manager.SharedPrefManager;
import oac.com.oac.app.modal.AppImagesVo;
import oac.com.oac.app.modal.FeedParams;
import oac.com.oac.app.modal.ResponseError;
import oac.com.oac.app.networking.RequestManager;
import oac.com.oac.app.utils.APIMethods;
import oac.com.oac.app.utils.Util;

public class SplashScreen extends AppBaseActivity {

    private ImageView imgSplash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        imgSplash = (ImageView) findViewById(R.id.imgSplash);

        if (SharedPrefManager.getInstance().getSharedDataString(FeedParams.OAA_LOGO).length() > 0) {
            Picasso.with(SplashScreen.this).load(RequestManager.LIVE_SERVER + SharedPrefManager.getInstance().getSharedDataString(FeedParams.SPLASH_LOGO))
                    .into(imgSplash);
            new SplashTask().execute();
        } else {
            fetchAppImages();
        }
    }

    private void fetchAppImages() {
        if (Util.isNetworkOnline(SplashScreen.this)) {

            HashMap<String, String> params = new HashMap<>();

            placeRequest(APIMethods.APP_IMAGE_API, AppImagesVo.class, params, true, null);
        } else {
            showToast(getResources().getString(R.string.no_internet));
        }
    }

    @Override
    public void onAPIResponse(Object response, String apiMethod) {
        super.onAPIResponse(response, apiMethod);

        if (apiMethod.equalsIgnoreCase(APIMethods.APP_IMAGE_API)) {

            AppImagesVo appImagesVo = (AppImagesVo) response;

            SharedPrefManager.getInstance().setSharedData(FeedParams.OAA_LOGO, appImagesVo.getAboutUs().getOaaLogo());
            SharedPrefManager.getInstance().setSharedData(FeedParams.OAC_LOGO, appImagesVo.getAboutUs().getOacLogo());
            SharedPrefManager.getInstance().setSharedData(FeedParams.SPLASH_LOGO, appImagesVo.getAboutUs().getSplashLogo());
            SharedPrefManager.getInstance().setSharedData(FeedParams.QUIZ_LOGO, appImagesVo.getAboutUs().getQuizLogo());
            SharedPrefManager.getInstance().setSharedData(FeedParams.QNA_LOGO, appImagesVo.getAboutUs().getQnaLogo());

            String dataStr = new Gson().toJson(appImagesVo.getAboutUs().getSponsor_banner());
            SharedPrefManager.getInstance().setSharedData(FeedParams.SPONSOR_BANNER, dataStr);

            Picasso.with(SplashScreen.this).load(RequestManager.LIVE_SERVER + SharedPrefManager.getInstance()
                    .getSharedDataString(FeedParams.SPLASH_LOGO))
                    .into(imgSplash);

            new SplashTask().execute();
        }
    }

    @Override
    public void onErrorResponse(VolleyError error, String apiMethod) {
        super.onErrorResponse(error, apiMethod);

        if (apiMethod.equalsIgnoreCase(APIMethods.LOGIN_API)) {

            ResponseError responseError = (ResponseError) error;
            showToast(responseError.getErrorMessage());
        }
    }

    private class SplashTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {

            try {
                Thread.sleep(3000);
            } catch (Exception e) {

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
//            SharedPrefManager.getInstance().setSharedData(FeedParams.USER_ID, userProfileVo.getUserId());
            if (SharedPrefManager.getInstance().getSharedDataString(FeedParams.USER_ID).length() <= 0) {
                Intent mainIntent = new Intent(SplashScreen.this, SignInActivity.class);
                startActivity(mainIntent);
            } else {
                Intent mainIntent = new Intent(SplashScreen.this, LandingActivity.class);
                startActivity(mainIntent);
            }

            overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
            finish();
        }
    }
}
