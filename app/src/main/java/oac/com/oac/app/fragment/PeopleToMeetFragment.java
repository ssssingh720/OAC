package oac.com.oac.app.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.oacasia.R;
import oac.com.oac.app.adapter.PeopleToMeetPagerAdapter;

/**
 * Created by Sudhir Singh on 09,July,2018
 * ESS,
 * B-65,Sector 63,Noida.
 */
public class PeopleToMeetFragment extends BaseFragment {


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.people_to_meet_frag, container, false);


        ViewPager viewPager = (ViewPager)mView. findViewById(R.id.pager);
        PeopleToMeetPagerAdapter adapter = new PeopleToMeetPagerAdapter(getChildFragmentManager());

        // Add Fragments to adapter one by one
        adapter.addFragment(new MeetingSentFragment(), getString(R.string.request_send));
        adapter.addFragment(new MeetingCameFragment(), getString(R.string.request_camer));
        viewPager.setAdapter(adapter);

        TabLayout tabLayout = (TabLayout) mView.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);


        return  mView;
    }
}
