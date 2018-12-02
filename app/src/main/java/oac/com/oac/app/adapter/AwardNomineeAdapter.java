package oac.com.oac.app.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.oacasia.R;

import java.util.List;

import oac.com.oac.app.activities.AwardNomineeDetail;
import oac.com.oac.app.modal.AwardNominee;

/**
 * Created by Sudhir Singh on 24,July,2018
 * ESS,
 * B-65,Sector 63,Noida.
 */
public class AwardNomineeAdapter extends RecyclerView.Adapter<AwardNomineeAdapter.ViewHolder> {

    private Context context;
    private List<AwardNominee> videoDetailList;

    public AwardNomineeAdapter(Context context, List<AwardNominee> videoDetailList) {
        this.context = context;
        this.videoDetailList = videoDetailList;
    }

    @Override
    public AwardNomineeAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                             int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.award_nominee_adapter, parent, false);

        return new AwardNomineeAdapter.ViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(AwardNomineeAdapter.ViewHolder holder, int position) {


        holder.txtNominCat.setText(videoDetailList.get(position).getCode());
        holder.txtNominName.setText(videoDetailList.get(position).getName());

        holder.lnrNomDetail.setTag(position);
        holder.lnrNomDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int positionCliked = (Integer) v.getTag();

                Intent videoDescription = new Intent(context, AwardNomineeDetail.class);
                videoDescription.putExtra("NOMINEE_DETAIL", videoDetailList.get(positionCliked));
                context.startActivity(videoDescription);

            }
        });
    }

    @Override
    public int getItemCount() {
        return videoDetailList.size();
//        return 30;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView txtNominCat;
        public TextView txtNominName;
        public LinearLayout lnrNomDetail;

        public ViewHolder(View view) {
            super(view);

            lnrNomDetail = (LinearLayout) view.findViewById(R.id.lnrNomDetail);
            txtNominCat = (TextView) view.findViewById(R.id.txtNominCat);
            txtNominName = (TextView) view.findViewById(R.id.txtNominName);
        }
    }
}
