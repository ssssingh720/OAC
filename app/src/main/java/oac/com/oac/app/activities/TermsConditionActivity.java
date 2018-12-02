package oac.com.oac.app.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.oacasia.R;
import oac.com.oac.app.modal.AboutUsVo;
import oac.com.oac.app.utils.Util;

public class TermsConditionActivity extends AppCompatActivity {
    private WebView webview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_terms_condition);

        AboutUsVo aboutUsVo = (AboutUsVo) getIntent().getExtras().get("TERMS");

        webview = (WebView) findViewById(R.id.webview);
        TextView txtAboutUs = (TextView) findViewById(R.id.txtAboutUs);
        TextView txtTitle = (TextView) findViewById(R.id.txtTitle);
        ImageView imgAboutUs = (ImageView) findViewById(R.id.imgAboutUs);
        LinearLayout lnrTermsBack = (LinearLayout) findViewById(R.id.lnrTermsBack);

        txtTitle.setText(aboutUsVo.getAboutUs().getHeading());

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            txtAboutUs.setText(Html.fromHtml(aboutUsVo.getAboutUs().getContent(), Html.FROM_HTML_MODE_LEGACY));
        } else {
            txtAboutUs.setText(Html.fromHtml(aboutUsVo.getAboutUs().getContent()));
        }

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

        if (Util.isNetworkOnline(TermsConditionActivity.this)) {
            webview.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        } else {
            webview.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        }
        webview.loadData(aboutUsVo.getAboutUs().getContent(), "text/html; charset=UTF-8", null);

        if (aboutUsVo.getAboutUs().getImage() != null && aboutUsVo.getAboutUs().getImage().toString().trim().length() > 0)
            Picasso.with(TermsConditionActivity.this).load(aboutUsVo.getAboutUs().getImage()).placeholder(R.drawable.app_logo)
                    .error(R.drawable.app_logo).into(imgAboutUs);

        lnrTermsBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TermsConditionActivity.this.finish();
            }
        });
    }
}
