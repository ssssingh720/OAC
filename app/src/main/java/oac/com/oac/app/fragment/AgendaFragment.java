package oac.com.oac.app.fragment;

import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import org.oacasia.R;
import oac.com.oac.app.load_toast.LoadToast;
import oac.com.oac.app.manager.SharedPrefManager;
import oac.com.oac.app.modal.AboutUsVo;
import oac.com.oac.app.modal.FeedParams;
import oac.com.oac.app.modal.ResponseError;
import oac.com.oac.app.utils.APIMethods;
import oac.com.oac.app.utils.Util;

/**
 * Created by Sudhir Singh on 15,July,2017
 * ESS,
 * B-65,Sector 63,Noida.
 */
public class AgendaFragment extends BaseFragment {

    //    private TextView txtAboutUs;
    private WebView webview;
    private ImageView imgAboutUs;
    private LoadToast loadToast;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.about_us_frag, container, false);

        webview = (WebView) mView.findViewById(R.id.webview);
//        txtAboutUs = (TextView) mView.findViewById(R.id.txtAboutUs);
        imgAboutUs = (ImageView) mView.findViewById(R.id.imgAboutUs);
        TextView txtTitle = (TextView) mView.findViewById(R.id.txtTitle);

        txtTitle.setText("Agenda");

        Display display = getActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int height = size.y;
        loadToast = new LoadToast(getActivity());
        loadToast.setText("Please Wait...");
        loadToast.setTranslationY(height / 2);
        loadToast.setTextColor(Color.BLACK).setBackgroundColor(Color.WHITE)
                .setProgressColor(getResources().getColor(R.color.colorPrimary));

        if (Util.isNetworkOnline(getActivity())) {
            loadToast.show();

            HashMap<String, String> params = new HashMap<>();
            String userId = SharedPrefManager.getInstance().getSharedDataString(FeedParams.USER_ID);
            String token = SharedPrefManager.getInstance().getSharedDataString(FeedParams.TOKEN);

            params.put(FeedParams.USER_ID, userId);
            params.put(FeedParams.TOKEN, token);
            params.put(FeedParams.CODE, "agenda");

            placeRequest(APIMethods.ABOUT_US, AboutUsVo.class, params, true, null);
        } else {
            showToast(getResources().getString(R.string.no_internet));
        }

        return mView;
    }

    @Override
    public void onAPIResponse(Object response, String apiMethod) {
        super.onAPIResponse(response, apiMethod);

        if (apiMethod.equalsIgnoreCase(APIMethods.ABOUT_US)) {
            loadToast.success();
            AboutUsVo aboutUsVo = (AboutUsVo) response;

//            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
//                txtAboutUs.setText(Html.fromHtml(aboutUsVo.getAboutUs().getContent(), Html.FROM_HTML_MODE_LEGACY));
//            } else {
//                txtAboutUs.setText(Html.fromHtml(aboutUsVo.getAboutUs().getContent().trim()));
//            }

            final String mimeType = "text/html";
            final String encoding = "utf-8";
            webview.getSettings().setDefaultTextEncodingName(encoding);
            webview.getSettings().setLoadWithOverviewMode(true);
            webview.getSettings().setUseWideViewPort(true);
            webview.getSettings().setSupportZoom(false);
            webview.getSettings().setUseWideViewPort(false);
            webview.getSettings().setJavaScriptEnabled(true);
            webview.getSettings().setDomStorageEnabled(true);
            webview.setFocusableInTouchMode(false);
            webview.setFocusable(false);
            webview.getSettings().setLoadsImagesAutomatically(true);

            if (Util.isNetworkOnline(getActivity())) {
                webview.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
            } else {
                webview.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
            }
            webview.loadData(aboutUsVo.getAboutUs().getContent(), "text/html; charset=UTF-8", null);

            if (aboutUsVo.getAboutUs().getImage() != null && aboutUsVo.getAboutUs().getImage().trim().length() > 0)
                Picasso.with(getActivity()).load(aboutUsVo.getAboutUs().getImage()).placeholder(R.drawable.app_logo)
                        .error(R.drawable.app_logo).into(imgAboutUs);

        }
    }

    @Override
    public void onErrorResponse(VolleyError error, String apiMethod) {

        super.onErrorResponse(error, apiMethod);
        loadToast.error();
        ResponseError responseError = (ResponseError) error;
        showToast(responseError.getErrorMessage());
    }

}
