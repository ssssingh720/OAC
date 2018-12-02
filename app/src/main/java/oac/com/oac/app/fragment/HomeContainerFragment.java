package oac.com.oac.app.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.oacasia.R;
import oac.com.oac.app.iHelper.IFragListener;

/**
 * Created by Sudhir Singh on 06,July,2017
 * ESS,
 * B-65,Sector 63,Noida.
 */
public class HomeContainerFragment extends BaseFragment {

    IFragListener iFragListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.frame_container, container, false);

        HomeFragment homeFragment = new HomeFragment();
        FragmentManager manager = getFragmentManager();
        manager.beginTransaction()
                .replace(R.id.frm_container, homeFragment)
                .commit();

        return mView;
    }

    public void initialzeView(){
//        HomeFragment homeFragment = new HomeFragment();
        FragmentManager manager = getFragmentManager();

//        manager.beginTransaction()
//                .replace(R.id.frm_container, homeFragment)
//                .commit();

        manager.popBackStack();
    }

    public boolean isAvailableFragments(){
        FragmentManager manager = getFragmentManager();
        Fragment fr = manager.findFragmentById(R.id.frm_container);
        int size=manager.getFragments().size();
        manager.getBackStackEntryCount();
        if(fr!=null && fr instanceof HomeFragment){
            return false;
        }else{
            return true;
        }

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        iFragListener = (IFragListener) context;
    }
}
