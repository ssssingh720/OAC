package oac.com.oac.app.fragment;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.android.volley.VolleyError;

import java.util.HashMap;

import org.oacasia.R;
import oac.com.oac.app.adapter.MeetingRequestAdapter;
import oac.com.oac.app.load_toast.LoadToast;
import oac.com.oac.app.manager.SharedPrefManager;
import oac.com.oac.app.modal.FeedParams;
import oac.com.oac.app.modal.MeetingRequestDetailVo;
import oac.com.oac.app.modal.ResponseError;
import oac.com.oac.app.utils.APIMethods;
import oac.com.oac.app.utils.Util;

/**
 * Created by Sudhir Singh on 09,July,2018
 * ESS,
 * B-65,Sector 63,Noida.
 */
public class MeetingSentFragment extends BaseFragment {

    private RecyclerView rclRequestCame;
    private Context context;

    private LoadToast loadToast;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.notification_activity, container, false);

        context=getActivity();

        rclRequestCame=(RecyclerView)mView.findViewById(R.id.rclNotification);
        RelativeLayout relTopBack=(RelativeLayout)mView.findViewById(R.id.relTopBack);

        rclRequestCame.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context);
        rclRequestCame.setLayoutManager(mLayoutManager);

        relTopBack.setVisibility(View.GONE);

        Display display =getActivity(). getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int height = size.y;
        loadToast = new LoadToast(context);
        loadToast.setText("Please wait...");
        loadToast.setTranslationY(height / 2);
        loadToast.setTextColor(Color.BLACK).setBackgroundColor(Color.WHITE)
                .setProgressColor(getResources().getColor(R.color.colorPrimary));

        if (Util.isNetworkOnline(context)) {
            loadToast.show();

            HashMap<String, String> params = new HashMap<>();
            String userId = SharedPrefManager.getInstance().getSharedDataString(FeedParams.USER_ID);
            String token = SharedPrefManager.getInstance().getSharedDataString(FeedParams.TOKEN);

            params.put(FeedParams.USER_ID, userId);
            params.put(FeedParams.TOKEN, token);

            placeRequest(APIMethods.MEETING_REQUEST_SEND, MeetingRequestDetailVo.class, params, true, null);
        } else {
            showToast(getResources().getString(R.string.no_internet));
        }

        return mView;
    }

    @Override
    public void onAPIResponse(Object response, String apiMethod) {
        super.onAPIResponse(response, apiMethod);

        if (apiMethod.equalsIgnoreCase(APIMethods.MEETING_REQUEST_SEND)) {
            loadToast.success();
            MeetingRequestDetailVo meetingRequestDetailVo = (MeetingRequestDetailVo) response;
            RecyclerView.Adapter chapterVideoAdapter = new MeetingRequestAdapter(context, meetingRequestDetailVo.getMeetingRequestDetails());
            rclRequestCame.setAdapter(chapterVideoAdapter);
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
