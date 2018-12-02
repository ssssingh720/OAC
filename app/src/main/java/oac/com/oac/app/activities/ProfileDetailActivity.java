package oac.com.oac.app.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Type;
import java.util.ArrayList;

import cn.trinea.android.view.autoscrollviewpager.AutoScrollViewPager;
import org.oacasia.R;
import oac.com.oac.app.adapter.ImagePagerAdapter;
import oac.com.oac.app.manager.SharedPrefManager;
import oac.com.oac.app.modal.FeedParams;
import oac.com.oac.app.modal.SpeakerDetail;

import static oac.com.oac.app.utils.Constants.BANNER_SLIDE_TIME;

/**
 * Created by Sudhir Singh on 15,July,2017
 * ESS,
 * B-65,Sector 63,Noida.
 */
public class ProfileDetailActivity extends AppBaseActivity {

    private ImageView btnTwitter;
    private ImageView btnFacebook;
    private ImageView btnLinkedIn;

    private AutoScrollViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.detail_view);

        final SpeakerDetail speakerDetail = (SpeakerDetail) getIntent().getExtras().get("PROFILE_DETAIL");

        TextView txtProfTopic = (TextView) findViewById(R.id.txtProfTopic);
        ImageView imgProfImage = (ImageView) findViewById(R.id.imgProfImage);
        TextView txtProfName = (TextView) findViewById(R.id.txtProfName);
        TextView txtProfDetail = (TextView) findViewById(R.id.txtProfDetail);
        TextView txtProfCompName = (TextView) findViewById(R.id.txtProfCompName);
        LinearLayout lnrSocialLogo = (LinearLayout) findViewById(R.id.lnrSocialLogo);
        LinearLayout lnrProfileBack = (LinearLayout) findViewById(R.id.lnrProfileBack);

        btnTwitter = (ImageView) findViewById(R.id.btnTwitter);
        btnFacebook = (ImageView) findViewById(R.id.btnFacebook);
        btnLinkedIn = (ImageView) findViewById(R.id.btnLinkdin);

        viewPager = (AutoScrollViewPager) findViewById(R.id.sponsor_pager);

        String str = SharedPrefManager.getInstance().getSharedDataString(FeedParams.SPONSOR_BANNER);//you need to retrieve this string from shared preferences.
        Type type = new TypeToken<ArrayList<String>>() {
        }.getType();
        ArrayList<String> sponsorsList = new Gson().fromJson(str, type);
        viewPager.setAdapter(new ImagePagerAdapter(ProfileDetailActivity.this, sponsorsList).setInfiniteLoop(true));
        viewPager.setInterval(BANNER_SLIDE_TIME);
        viewPager.startAutoScroll();
        viewPager.setCurrentItem(Integer.MAX_VALUE / 2 - Integer.MAX_VALUE / 2 % sponsorsList.size());


        if (speakerDetail.getTopic() != null && speakerDetail.getTopic().length() > 0) {
            txtProfTopic.setText(speakerDetail.getTopic());
        } else {
            txtProfTopic.setVisibility(View.GONE);
        }

        txtProfName.setText(speakerDetail.getName());
        txtProfCompName.setText(speakerDetail.getDesignation() + "," + speakerDetail.getCompanyName());

        Picasso.with(ProfileDetailActivity.this).load(speakerDetail.getImage()).placeholder(R.drawable.app_logo)
                .error(R.drawable.app_logo).into(imgProfImage);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            txtProfDetail.setText(Html.fromHtml(speakerDetail.getProfile(), Html.FROM_HTML_MODE_LEGACY));
        } else {
            txtProfDetail.setText(Html.fromHtml(speakerDetail.getProfile()));
        }

        if (speakerDetail.getTwitter() != null && speakerDetail.getTwitter().length() > 0) {
            lnrSocialLogo.setVisibility(View.VISIBLE);
            btnTwitter.setVisibility(View.VISIBLE);
        } else {
            btnTwitter.setVisibility(View.GONE);
        }

        if (speakerDetail.getFacebook() != null && speakerDetail.getFacebook().length() > 0) {
            lnrSocialLogo.setVisibility(View.VISIBLE);
            btnFacebook.setVisibility(View.VISIBLE);
        } else {
            btnFacebook.setVisibility(View.GONE);
        }

        if (speakerDetail.getLinkedin() != null && speakerDetail.getLinkedin().length() > 0) {
            lnrSocialLogo.setVisibility(View.VISIBLE);
            btnLinkedIn.setVisibility(View.VISIBLE);
        } else {
            btnLinkedIn.setVisibility(View.GONE);
        }

        lnrProfileBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        btnTwitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openWebPage(speakerDetail.getTwitter());
            }
        });
        btnFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openWebPage(speakerDetail.getFacebook());
            }
        });
        btnLinkedIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openWebPage(speakerDetail.getLinkedin());
            }
        });
    }

    public void openWebPage(String url) {

//        WebView webView = (WebView) findViewById(R.id.webView1);
//        WebSettings settings = webView.getSettings();
//        settings.setJavaScriptEnabled(true);
//        settings.setSupportZoom(true);
//        settings.setBuiltInZoomControls(true);
//        webView.loadUrl(url);

        Uri webpage = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();

        finish();
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
