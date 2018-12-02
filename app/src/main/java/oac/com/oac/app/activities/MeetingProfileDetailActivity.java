package oac.com.oac.app.activities;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.PhoneNumberUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Type;
import java.util.ArrayList;

import cn.trinea.android.view.autoscrollviewpager.AutoScrollViewPager;
import org.oacasia.R;
import oac.com.oac.app.adapter.ImagePagerAdapter;
import oac.com.oac.app.fragment.MeFragment;
import oac.com.oac.app.manager.SharedPrefManager;
import oac.com.oac.app.modal.FeedParams;
import oac.com.oac.app.modal.MeetingRequestDetail;
import oac.com.oac.app.utils.CircleTransform;

import static oac.com.oac.app.utils.Constants.BANNER_SLIDE_TIME;

/**
 * Created by Sudhir Singh on 12,July,2018
 * ESS,
 * B-65,Sector 63,Noida.
 */
public class MeetingProfileDetailActivity extends AppBaseActivity {



    private MeetingRequestDetail networking_profile_detail;

    private AutoScrollViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.networking_profiele_detail);
        networking_profile_detail = (MeetingRequestDetail) getIntent().getExtras().get("NETWORKING_PROFILE_DETAIL");

        LinearLayout lnrProfileBack = (LinearLayout) findViewById(R.id.lnrProfileBack);
        ImageView imgProfImage = (ImageView) findViewById(R.id.imgProfImage);
         ImageView imgSendMeeting = (ImageView) findViewById(R.id.imgSendMeeting);
        TextView txtProfName = (TextView) findViewById(R.id.txtProfName);
        TextView txtProfDesign = (TextView) findViewById(R.id.txtProfDesign);
        TextView txtProfCompName = (TextView) findViewById(R.id.txtProfCompName);

//        LinearLayout lnrMeeting = (LinearLayout) findViewById(R.id.lnrMeeting);
        LinearLayout imgAdaChat = (LinearLayout) findViewById(R.id.imgAdaChat);
        LinearLayout lnrAdaMail = (LinearLayout) findViewById(R.id.lnrAdaMail);
        LinearLayout lnrAdaCall = (LinearLayout) findViewById(R.id.lnrAdaCall);

        viewPager = (AutoScrollViewPager) findViewById(R.id.sponsor_pager);

        String str = SharedPrefManager.getInstance().getSharedDataString(FeedParams.SPONSOR_BANNER);//you need to retrieve this string from shared preferences.
        Type type = new TypeToken<ArrayList<String>>() {
        }.getType();
        ArrayList<String> sponsorsList = new Gson().fromJson(str, type);
        viewPager.setAdapter(new ImagePagerAdapter(MeetingProfileDetailActivity.this, sponsorsList).setInfiniteLoop(true));
        viewPager.setInterval(BANNER_SLIDE_TIME);
        viewPager.startAutoScroll();
        viewPager.setCurrentItem(Integer.MAX_VALUE / 2 - Integer.MAX_VALUE / 2 % sponsorsList.size());

        txtProfName.setText(networking_profile_detail.getToUserName());
        txtProfCompName.setText(networking_profile_detail.getCompany());
        txtProfDesign.setText(networking_profile_detail.getDesignation());

        Picasso.with(MeetingProfileDetailActivity.this).load(networking_profile_detail.getImage()).placeholder(R.drawable.app_logo)
                .error(R.drawable.app_logo).transform(new CircleTransform())
                .resize(100, 100).into(imgProfImage);

        if (networking_profile_detail.getEmail() != null && networking_profile_detail.getEmail().length() > 0) {
            lnrAdaMail.setVisibility(View.VISIBLE);
        } else {
            lnrAdaMail.setVisibility(View.GONE);
        }

        if (networking_profile_detail.getPhone() != null && networking_profile_detail.getPhone().length() > 0) {
            lnrAdaCall.setVisibility(View.VISIBLE);
            imgAdaChat.setVisibility(View.VISIBLE);
        } else {
            lnrAdaCall.setVisibility(View.GONE);
            imgAdaChat.setVisibility(View.GONE);
        }

//        if (networking_profile_detail.isMeeting_sent()) {
        imgSendMeeting.setBackgroundResource(R.drawable.meetingcheck);
//        } else {
//            imgSendMeeting.setBackgroundResource(R.drawable.meeting);
//        }

        lnrProfileBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        imgAdaChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openWhatsApp(networking_profile_detail.getPhone());
            }
        });

        lnrAdaMail.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        sendEmail(networking_profile_detail.getPhone());
                    }
                }
        );

        lnrAdaCall.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (checkPermission(MeetingProfileDetailActivity.this)) {
                            String phoneNo = networking_profile_detail.getPhone();
                            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNo));
                            startActivity(intent);
                        } else {
                            Toast.makeText(MeetingProfileDetailActivity.this, "Permission required", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );

    }


    private void sendEmail(String emailTO) {

        try {
            Uri uri = Uri.parse("smsto:"+emailTO);
            // No permisison needed
            Intent smsIntent = new Intent(Intent.ACTION_SENDTO, uri);
            // Set the message to be sent
            smsIntent.putExtra("sms_body", "");
            startActivity(smsIntent);
        } catch (Exception e) {
            Toast.makeText(this,
                    "SMS faild, please try again later!",
                    Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }

//        Intent email = new Intent(Intent.ACTION_SEND);
//        email.putExtra(Intent.EXTRA_EMAIL, new String[]{emailTO});
//        email.putExtra(Intent.EXTRA_SUBJECT, "");
//        email.putExtra(Intent.EXTRA_TEXT, "");
//
//        //need this to prompts email client only
//        email.setType("message/rfc822");
//        try {
//            startActivity(Intent.createChooser(email, "Send mail..."));
//        } catch (android.content.ActivityNotFoundException ex) {
//            Toast.makeText(MeetingProfileDetailActivity.this, "There is no mailing app installed.", Toast.LENGTH_SHORT).show();
//        }
    }

    private void openWhatsApp(String smsNumber) {
//        String smsNumber = "917835846055";
        boolean isWhatsappInstalled = whatsappInstalledOrNot("com.whatsapp");
        if (isWhatsappInstalled) {

            Intent sendIntent = new Intent("android.intent.action.MAIN");
            sendIntent.setComponent(new ComponentName("com.whatsapp", "com.whatsapp.Conversation"));
            sendIntent.putExtra("jid", PhoneNumberUtils.stripSeparators(smsNumber) + "@s.whatsapp.net");//phone number without "+" prefix

            startActivity(sendIntent);
        } else {
            Uri uri = Uri.parse("market://details?id=com.whatsapp");
            Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
            Toast.makeText(MeetingProfileDetailActivity.this, "WhatsApp not Installed",
                    Toast.LENGTH_SHORT).show();
            startActivity(goToMarket);
        }
    }

    private boolean whatsappInstalledOrNot(String uri) {
        PackageManager pm = getPackageManager();
        boolean app_installed = false;
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            app_installed = true;
        } catch (PackageManager.NameNotFoundException e) {
            app_installed = false;
        }
        return app_installed;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private boolean checkPermission(final Context context) {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if (currentAPIVersion >= android.os.Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, Manifest.permission.CALL_PHONE)) {
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
                    alertBuilder.setCancelable(true);
                    alertBuilder.setTitle("Permission necessary");
                    alertBuilder.setMessage("External storage permission is necessary");
                    alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.CALL_PHONE}, MeFragment.MY_PERMISSIONS_REQUEST_MAKE_CALL);
                        }
                    });
                    AlertDialog alert = alertBuilder.create();
                    alert.show();
                } else {
                    ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.CALL_PHONE}, MeFragment.MY_PERMISSIONS_REQUEST_MAKE_CALL);
                }
                return false;
            } else {
                return true;
            }
        } else {
            return true;
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
