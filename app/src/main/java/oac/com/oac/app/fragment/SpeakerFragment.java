package oac.com.oac.app.fragment;

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
import android.widget.ImageView;

import com.android.volley.VolleyError;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import org.oacasia.R;
import oac.com.oac.app.adapter.SpeakerAdapter;
import oac.com.oac.app.load_toast.LoadToast;
import oac.com.oac.app.manager.SharedPrefManager;
import oac.com.oac.app.modal.FeedParams;
import oac.com.oac.app.modal.ResponseError;
import oac.com.oac.app.modal.SpeakersVo;
import oac.com.oac.app.modal.UserProfileVo;
import oac.com.oac.app.networking.RequestManager;
import oac.com.oac.app.utils.APIMethods;
import oac.com.oac.app.utils.Util;

/**
 * Created by Sudhir Singh on 07,July,2017
 * ESS,
 * B-65,Sector 63,Noida.
 */
public class SpeakerFragment extends BaseFragment {

    private LoadToast loadToast;
    private  RecyclerView rclSpeakers;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.speaker_frag, container, false);

         rclSpeakers = (RecyclerView) mView.findViewById(R.id.rclSpeakers);
//        ImageView   imgTopRightLogo = (ImageView) mView.findViewById(R.id.imgTopRightLogo);
//
//        Picasso.with(getActivity()).load(RequestManager.LIVE_SERVER+SharedPrefManager.getInstance().
//                getSharedDataString(FeedParams.OAC_LOGO))
//                .placeholder(R.drawable.logo).error(R.drawable.logo).into(imgTopRightLogo);

        rclSpeakers.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        rclSpeakers.setLayoutManager(mLayoutManager);

        Display display = getActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int height = size.y;
        loadToast = new LoadToast(getActivity());
        loadToast.setText("Please wait...");
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

            placeRequest(APIMethods.SPEAKER_LIST, SpeakersVo.class, params, true, null);
        } else {
            showToast(getResources().getString(R.string.no_internet));
        }

        return mView;
    }

    @Override
    public void onAPIResponse(Object response, String apiMethod) {
        super.onAPIResponse(response, apiMethod);

        if (apiMethod.equalsIgnoreCase(APIMethods.SPEAKER_LIST)) {
            loadToast.success();
            SpeakersVo speakersVo = (SpeakersVo) response;
            RecyclerView.Adapter chapterVideoAdapter = new SpeakerAdapter(getActivity(), speakersVo.getSpeakersList());
            rclSpeakers.setAdapter(chapterVideoAdapter);
        }
    }

    @Override
    public void onErrorResponse(VolleyError error, String apiMethod) {

        super.onErrorResponse(error, apiMethod);
        loadToast.error();
        ResponseError responseError=(ResponseError)error;
        showToast(responseError.getErrorMessage());
    }
}
