package oac.com.oac.app.fragment;

import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.VolleyError;

import java.util.HashMap;
import java.util.List;

import org.oacasia.R;
import oac.com.oac.app.adapter.NetworkingNewAdapter;
import oac.com.oac.app.load_toast.LoadToast;
import oac.com.oac.app.manager.SharedPrefManager;
import oac.com.oac.app.modal.FeedParams;
import oac.com.oac.app.modal.Networking;
import oac.com.oac.app.modal.NetworkingVo;
import oac.com.oac.app.modal.ResponseError;
import oac.com.oac.app.utils.APIMethods;
import oac.com.oac.app.utils.Util;

/**
 * Created by Sudhir Singh on 07,July,2017
 * ESS,
 * B-65,Sector 63,Noida.
 */
public class NetworkingFragment extends BaseFragment  implements SearchView.OnQueryTextListener {

    private LoadToast loadToast;
    private SearchView editsearch;
    private RecyclerView rclSpeakers;
    private List<Networking> videoDetailList;
    private NetworkingNewAdapter newtwokingAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.networking_fragment, container, false);

        TextView txtTitle = (TextView) mView.findViewById(R.id.txtTitle);
        rclSpeakers = (RecyclerView) mView.findViewById(R.id.rclSpeakers);
        editsearch = (SearchView)mView. findViewById(R.id.search);
        editsearch.setOnQueryTextListener(this);

        txtTitle.setText("Networking");

       rclSpeakers.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        rclSpeakers.setLayoutManager(mLayoutManager);
        setScrollListener();

//        ItemTouchHelperExtension.Callback mCallback = new FinanceItemTouchHelperCallback();
//        ItemTouchHelperExtension mItemTouchHelper = new ItemTouchHelperExtension(mCallback);
//        mItemTouchHelper.attachToRecyclerView(rclSpeakers);

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

            placeRequest(APIMethods.NETWORKING_LIST, NetworkingVo.class, params, true, null);
        } else {
            showToast(getResources().getString(R.string.no_internet));
        }

        return mView;
    }

    @Override
    public void onAPIResponse(Object response, String apiMethod) {
        super.onAPIResponse(response, apiMethod);

        if (apiMethod.equalsIgnoreCase(APIMethods.NETWORKING_LIST)) {
            loadToast.success();
            NetworkingVo networkingVo = (NetworkingVo) response;
            videoDetailList = networkingVo.getNetworkingList();
             newtwokingAdapter = new NetworkingNewAdapter(getActivity(), videoDetailList);
            rclSpeakers.setAdapter(newtwokingAdapter);
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
    public boolean onQueryTextSubmit(String query) {

        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        String text = newText;
        newtwokingAdapter.filter(text);
        return false;
    }

    private void setScrollListener() {
        rclSpeakers.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (videoDetailList != null) {
                    boolean isAnyChange = false;
                    for (int counter = 0; counter < videoDetailList.size(); counter++) {
                        if (videoDetailList.get(counter).isSelected()) {
                            isAnyChange = true;
                            videoDetailList.get(counter).setSelected(false);
                            break;
                        }
                    }
                    if (isAnyChange) {
                        recyclerView.getAdapter().notifyDataSetChanged();
                    }
                }

            }
        });
    }

}
