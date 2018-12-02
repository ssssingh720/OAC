package oac.com.oac.app.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.oacasia.R;
import oac.com.oac.app.activities.OACFeedbackActivity;
import oac.com.oac.app.activities.SelectSpeakerActivity;
import oac.com.oac.app.adapter.HelpExpandableAdapter;

/**
 * Created by Sudhir Singh on 09,July,2017
 * ESS,
 * B-65,Sector 63,Noida.
 */
public class FeedBackFragment extends BaseFragment {

//    private TextView txtAboutUs;
//    private ImageView imgAboutUs;
//    private LoadToast loadToast;
//
//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        mView = inflater.inflate(R.layout.about_us_frag, container, false);
//        mView = inflater.inflate(R.layout.help_fragment, container, false);

//        txtAboutUs = (TextView) mView.findViewById(R.id.txtAboutUs);
//        imgAboutUs = (ImageView) mView.findViewById(R.id.imgAboutUs);
//
//        TextView txtTitle = (TextView) mView.findViewById(R.id.txtTitle);
//        txtTitle.setText("Help");

//        Display display = getActivity().getWindowManager().getDefaultDisplay();
//        Point size = new Point();
//        display.getSize(size);
//        int height = size.y;
//        loadToast = new LoadToast(getActivity());
//        loadToast.setText("Please Wait...");
//        loadToast.setTranslationY(height / 2);
//        loadToast.setTextColor(Color.BLACK).setBackgroundColor(Color.WHITE)
//                .setProgressColor(getResources().getColor(R.color.colorPrimary));

//        if (Util.isNetworkOnline(getActivity())) {
//            loadToast.show();

//            HashMap<String, String> params = new HashMap<>();
//            String userId = SharedPrefManager.getInstance().getSharedDataString(FeedParams.USER_ID);
//            String token = SharedPrefManager.getInstance().getSharedDataString(FeedParams.TOKEN);
//
//            params.put(FeedParams.USER_ID, userId);
//            params.put(FeedParams.TOKEN, token);
//            params.put(FeedParams.CODE, "help");
//
//            placeRequest(APIMethods.ABOUT_US, AboutUsVo.class, params, true, null);
//        } else {
//            showToast(getResources().getString(R.string.no_internet));
//        }

//        return mView;
//    }

//    @Override
//    public void onAPIResponse(Object response, String apiMethod) {
//        super.onAPIResponse(response, apiMethod);
//
//        if (apiMethod.equalsIgnoreCase(APIMethods.ABOUT_US)) {
//            loadToast.success();
//            AboutUsVo aboutUsVo = (AboutUsVo) response;
//
//            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
//                txtAboutUs.setText(Html.fromHtml(aboutUsVo.getAboutUs().getContent(), Html.FROM_HTML_MODE_LEGACY));
//            } else {
//                txtAboutUs.setText(Html.fromHtml(aboutUsVo.getAboutUs().getContent()));
//            }
//
//            Picasso.with(getActivity()).load(aboutUsVo.getAboutUs().getImage()).placeholder(R.drawable.logo)
//                    .error(R.drawable.logo).into(imgAboutUs);
//
//        }
//    }
//
//    @Override
//    public void onErrorResponse(VolleyError error, String apiMethod) {
//
//        super.onErrorResponse(error, apiMethod);
//        loadToast.error();
//        ResponseError responseError = (ResponseError) error;
//        showToast(responseError.getErrorMessage());
//    }

    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.help_fragment, container, false);

        ExpandableListView expListView = (ExpandableListView) mView.findViewById(R.id.expHelp);
        expListView.setDividerHeight(10);
        expListView.setGroupIndicator(null);
        expListView.setClickable(true);

        prepareListData();

        HelpExpandableAdapter listAdapter = new HelpExpandableAdapter(getActivity(), listDataHeader, listDataChild);

        // setting list adapter
        expListView.setAdapter(listAdapter);

        // Listview Group click listener
        expListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v,
                                        int groupPosition, long id) {

                if (groupPosition == 1) {
                    Intent OACFeedbackIntent = new Intent(getActivity(), SelectSpeakerActivity.class);
//                    OACFeedbackIntent.putExtra("RATE_TEXT",listDataHeader.get(groupPosition));
                    startActivity(OACFeedbackIntent);

                } else {
                    Intent OACFeedbackIntent = new Intent(getActivity(), OACFeedbackActivity.class);
                    OACFeedbackIntent.putExtra("RATE_TEXT",listDataHeader.get(groupPosition));
                    startActivity(OACFeedbackIntent);
                }

//                Toast.makeText(getActivity(),
//                        "Group Clicked " + listDataHeader.get(groupPosition),
//                        Toast.LENGTH_SHORT).show();

                return false;

            }
        });

        // Listview Group expanded listener
        expListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {
//                Toast.makeText(getActivity(),
//                        listDataHeader.get(groupPosition) + " Expanded",
//                        Toast.LENGTH_SHORT).show();
            }
        });

        // Listview Group collasped listener
        expListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {
//                Toast.makeText(getActivity(),
//                        listDataHeader.get(groupPosition) + " Collapsed",
//                        Toast.LENGTH_SHORT).show();

            }
        });

        // Listview on child click listener
        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                // TODO Auto-generated method stub
                Toast.makeText(
                        getActivity(),
                        listDataHeader.get(groupPosition)
                                + " : "
                                + listDataChild.get(
                                listDataHeader.get(groupPosition)).get(
                                childPosition), Toast.LENGTH_SHORT)
                        .show();
                return false;
            }
        });

        return mView;
    }

    /*
     * Preparing the list data
     */
    private void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        // Adding child data
        listDataHeader.add(getString(R.string.oac_2018));
        listDataHeader.add(getString(R.string.oac_speakers));
        listDataHeader.add(getString(R.string.ooh_expo));
        listDataHeader.add(getString(R.string.oaa_expo));

        // Adding child data
        List<String> conventionList = new ArrayList<String>();
//        conventionList.add("What can we help you with?");
//        conventionList.add("What can we help you with?");

        List<String> expoList = new ArrayList<String>();
//        expoList.add("What can we help you with?");
//        expoList.add("What can we help you with?");

        List<String> awardsList = new ArrayList<String>();
//        awardsList.add("What can we help you with?");
//        awardsList.add("What can we help you with?");

        listDataChild.put(listDataHeader.get(0), conventionList); // Header, Child data
        listDataChild.put(listDataHeader.get(1), expoList);
        listDataChild.put(listDataHeader.get(2), awardsList);
        listDataChild.put(listDataHeader.get(3), awardsList);
    }
}
