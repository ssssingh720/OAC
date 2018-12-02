package oac.com.oac.app.fragment;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;

import com.android.volley.VolleyError;
import com.squareup.picasso.Picasso;

import org.oacasia.R;
import oac.com.oac.app.activities.SplashScreen;
import oac.com.oac.app.adapter.HomeGridAdapter;
import oac.com.oac.app.iHelper.IFragListener;
import oac.com.oac.app.load_toast.LoadToast;
import oac.com.oac.app.manager.SharedPrefManager;
import oac.com.oac.app.modal.BaseVO;
import oac.com.oac.app.modal.FeedParams;
import oac.com.oac.app.modal.ResponseError;
import oac.com.oac.app.networking.RequestManager;
import oac.com.oac.app.utils.APIMethods;
import oac.com.oac.app.utils.Util;

/**
 * Created by Sudhir Singh on 07,July,2017
 * ESS,
 * B-65,Sector 63,Noida.
 */
public class HomeFragment extends BaseFragment {

    private int positionclicked = 0;
    private LoadToast loadToast;

    IFragListener iFragListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.home_fragment, container, false);

        GridView grdHomeIcon = (GridView) mView.findViewById(R.id.grdHomeIcon);
        ImageView img_OAC_Logo = (ImageView) mView.findViewById(R.id.img_OAC_Logo);
        ImageView img_OAA_Logo = (ImageView) mView.findViewById(R.id.img_OAA_Logo);

        Picasso.with(getActivity()).load(RequestManager.LIVE_SERVER+SharedPrefManager.getInstance().getSharedDataString(FeedParams.OAA_LOGO))
                .placeholder(R.drawable.app_logo).error(R.drawable.app_logo).into(img_OAA_Logo);
        Picasso.with(getActivity()).load(RequestManager.LIVE_SERVER+SharedPrefManager.getInstance().getSharedDataString(FeedParams.OAC_LOGO))
                .placeholder(R.drawable.app_logo).error(R.drawable.app_logo).into(img_OAC_Logo);


        HomeGridAdapter homeGridAdapter = new HomeGridAdapter(getActivity());
        grdHomeIcon.setAdapter(homeGridAdapter);

        Display display = getActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int height = size.y;
        loadToast = new LoadToast(getActivity());
        loadToast.setText("Please Wait...");
        loadToast.setTranslationY(height / 2);
        loadToast.setTextColor(Color.BLACK).setBackgroundColor(Color.WHITE)
                .setProgressColor(getResources().getColor(R.color.colorPrimary));

        grdHomeIcon.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (Util.isNetworkOnline(getActivity())) {
                    changeFragment(position);
                } else {
                    showToast(getResources().getString(R.string.no_internet));
                }
            }
        });

        return mView;
    }


    private void changeFragment(int position) {


        switch (position) {

            case 0:

                AboutUsFragment fragment2 = new AboutUsFragment();
                replaceFrag(fragment2, getString(R.string.about_us_tag));

                break;

            case 1:

                AgendaFragment agendaFrag = new AgendaFragment();
                replaceFrag(agendaFrag, getString(R.string.agenda_tag));

                break;

            case 2:
                SpeakerFragment speakerFragment = new SpeakerFragment();
                replaceFrag(speakerFragment, getString(R.string.speaker_tag));

                break;

            case 3:
                JuryFragment juryFrag = new JuryFragment();
                replaceFrag(juryFrag, getString(R.string.jury_tag));

                break;

            case 4:
                loadToast.show();
                positionclicked = 4;
                placeRequest(APIMethods.EVENT_STATUS, BaseVO.class, null, true, null);


                break;

            case 5:
                VenueFragment venueFragment = new VenueFragment();
                replaceFrag(venueFragment, getString(R.string.venue_tag));

                break;

            case 6:
                SponsorsFargment sponsorsFargment = new SponsorsFargment();
                replaceFrag(sponsorsFargment, getString(R.string.sponsor_tag));

                break;

            case 7:
                loadToast.show();
                positionclicked = 7;
                placeRequest(APIMethods.EVENT_STATUS, BaseVO.class, null, true, null);

                break;

            case 8:
                loadToast.show();
                positionclicked = 8;
                placeRequest(APIMethods.EVENT_STATUS, BaseVO.class, null, true, null);

                break;
        }
    }


    private void replaceFrag(Fragment fragment, String tag) {

        iFragListener.changeFragment(tag);

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frm_container, fragment, tag);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

    }

    @Override
    public void onAPIResponse(Object response, String apiMethod) {
        super.onAPIResponse(response, apiMethod);

        loadToast.success();

        if (apiMethod.equals(APIMethods.EVENT_STATUS)) {

            BaseVO baseVO = (BaseVO) response;
            if (baseVO.getMessage().equalsIgnoreCase("Active")) {

                switch (positionclicked) {

                    case 4:
                        QuestAnsFragment questAnsFragment = new QuestAnsFragment();
                        replaceFrag(questAnsFragment, getString(R.string.quest_ans_tag));
                        break;

                    case 7:
                        QuizFragment quizFragment = new QuizFragment();
                        replaceFrag(quizFragment, getString(R.string.quiz_tag));
                        break;

                    case 8:
                        NetworkingFragment networkingFragment = new NetworkingFragment();
                        replaceFrag(networkingFragment, getString(R.string.networking_tag));
                        break;
                }
            }
        }
    }

    @Override
    public void onErrorResponse(VolleyError error, String apiMethod) {
        super.onErrorResponse(error, apiMethod);
        loadToast.error();
        if (apiMethod.equalsIgnoreCase(APIMethods.EVENT_STATUS)) {

            ResponseError responseError = (ResponseError) error;
            showToast(responseError.getErrorMessage());

        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        iFragListener = (IFragListener) context;
    }

}
