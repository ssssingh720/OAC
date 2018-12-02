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
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.HashMap;

import org.oacasia.R;
import oac.com.oac.app.load_toast.LoadToast;
import oac.com.oac.app.manager.SharedPrefManager;
import oac.com.oac.app.modal.FeedParams;
import oac.com.oac.app.modal.SpeakersVo;
import oac.com.oac.app.networking.RequestManager;
import oac.com.oac.app.utils.APIMethods;
import oac.com.oac.app.utils.Util;

/**
 * Created by Sudhir Singh on 11,July,2018
 * ESS,
 * B-65,Sector 63,Noida.
 */
public class QRCodeFragment extends BaseFragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.qr_code_fragment, container, false);

        TextView txtQrCode = (TextView) mView.findViewById(R.id.txtQrCode);
        ImageView imgQrCode = (ImageView) mView.findViewById(R.id.imgQrCode);

        Picasso.with(getActivity()).load( SharedPrefManager.getInstance().
                getSharedDataString(FeedParams.QR_CODE))
                .placeholder(R.drawable.app_logo).error(R.drawable.app_logo).into(imgQrCode);
        txtQrCode.setText(SharedPrefManager.getInstance().
                getSharedDataString(FeedParams.QR_CODE_NO));

        return mView;
    }

}
