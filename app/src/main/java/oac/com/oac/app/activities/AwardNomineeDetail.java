package oac.com.oac.app.activities;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.oacasia.R;

import java.lang.reflect.Type;
import java.util.ArrayList;

import cn.trinea.android.view.autoscrollviewpager.AutoScrollViewPager;
import oac.com.oac.app.adapter.ImagePagerAdapter;
import oac.com.oac.app.manager.SharedPrefManager;
import oac.com.oac.app.modal.AwardNominee;
import oac.com.oac.app.modal.FeedParams;
import oac.com.oac.app.utils.Util;

import static oac.com.oac.app.utils.Constants.BANNER_SLIDE_TIME;

/**
 * Created by Sudhir Singh on 24,July,2018
 * ESS,
 * B-65,Sector 63,Noida.
 */
public class AwardNomineeDetail extends AppBaseActivity {

    private   AutoScrollViewPager   viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.award_nominee_detail);

        WebView  webview = (WebView) findViewById(R.id.webview);
        LinearLayout lnrOACFeedbackBack=(LinearLayout)findViewById(R.id.lnrOACFeedbackBack);

        AwardNominee awardNominee=(AwardNominee)getIntent().getExtras().get("NOMINEE_DETAIL");

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

        if (Util.isNetworkOnline(AwardNomineeDetail.this)) {
            webview.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        } else {
            webview.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        }
        webview.loadData(awardNominee.getHtml(), "text/html; charset=UTF-8", null);


           viewPager = (AutoScrollViewPager) findViewById(R.id.sponsor_pager);

        String str = SharedPrefManager.getInstance().getSharedDataString(FeedParams.SPONSOR_BANNER);//you need to retrieve this string from shared preferences.
        Type type = new TypeToken<ArrayList<String>>() {
        }.getType();
        ArrayList<String> sponsorsList = new ArrayList<>();
        sponsorsList.add("images/oaa-banner.jpg");
        viewPager.setAdapter(new ImagePagerAdapter(AwardNomineeDetail.this, sponsorsList).setInfiniteLoop(true));
        viewPager.setInterval(BANNER_SLIDE_TIME);
        viewPager.startAutoScroll();
        viewPager.setCurrentItem(Integer.MAX_VALUE / 2 - Integer.MAX_VALUE / 2 % sponsorsList.size());

        lnrOACFeedbackBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        // stop auto scroll when onPause
        viewPager.stopAutoScroll();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // start auto scroll when onResume
        viewPager.startAutoScroll();
    }
}
