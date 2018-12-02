package oac.com.oac.app.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.View;
import android.widget.LinearLayout;

import com.android.volley.VolleyError;

import java.util.HashMap;

import org.oacasia.R;
import oac.com.oac.app.adapter.NotificationAdapter;
import oac.com.oac.app.load_toast.LoadToast;
import oac.com.oac.app.manager.SharedPrefManager;
import oac.com.oac.app.modal.FeedParams;
import oac.com.oac.app.modal.NotificationVo;
import oac.com.oac.app.modal.ResponseError;
import oac.com.oac.app.utils.APIMethods;
import oac.com.oac.app.utils.Util;

/**
 * Created by Sudhir Singh on 09,July,2018
 * ESS,
 * B-65,Sector 63,Noida.
 */
public class NotificationActivity extends AppBaseActivity {

    public static int NOTIF_STATUS=4000;

    private RecyclerView rclNotification;
    private Context context;

    private LoadToast loadToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.notification_activity);

        context = NotificationActivity.this;

        rclNotification = (RecyclerView) findViewById(R.id.rclNotification);
        LinearLayout lnrNotificationBack = (LinearLayout) findViewById(R.id.lnrNotificationBack);

        rclNotification.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context);
        rclNotification.setLayoutManager(mLayoutManager);

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int height = size.y;
        loadToast = new LoadToast(context);
        loadToast.setText("Please wait...");
        loadToast.setTranslationY(height / 2);
        loadToast.setTextColor(Color.BLACK).setBackgroundColor(Color.WHITE)
                .setProgressColor(getResources().getColor(R.color.colorPrimary));

        fetchNotification();

        lnrNotificationBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    private void fetchNotification(){
        if (Util.isNetworkOnline(context)) {
            loadToast.show();

            HashMap<String, String> params = new HashMap<>();
            String userId = SharedPrefManager.getInstance().getSharedDataString(FeedParams.USER_ID);
            String token = SharedPrefManager.getInstance().getSharedDataString(FeedParams.TOKEN);

            params.put(FeedParams.USER_ID, userId);
            params.put(FeedParams.TOKEN, token);

            placeRequest(APIMethods.NOTIFICATIONS, NotificationVo.class, params, true, null);
        } else {
            showToast(getResources().getString(R.string.no_internet));
        }
    }

    @Override
    public void onAPIResponse(Object response, String apiMethod) {
        super.onAPIResponse(response, apiMethod);

        if (apiMethod.equalsIgnoreCase(APIMethods.NOTIFICATIONS)) {
            loadToast.success();
            NotificationVo notificationVo = (NotificationVo) response;
            RecyclerView.Adapter chapterVideoAdapter = new NotificationAdapter(NotificationActivity.this, notificationVo.getData());
            rclNotification.setAdapter(chapterVideoAdapter);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==NOTIF_STATUS && resultCode==RESULT_OK){

            fetchNotification();

        }

    }
}
