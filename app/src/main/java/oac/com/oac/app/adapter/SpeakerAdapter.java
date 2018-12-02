package oac.com.oac.app.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import org.oacasia.R;
import oac.com.oac.app.activities.ProfileDetailActivity;
import oac.com.oac.app.modal.SpeakerDetail;

/**
 * Created by Sudhir Singh on 07,July,2017
 * ESS,
 * B-65,Sector 63,Noida.
 */
public class SpeakerAdapter extends RecyclerView.Adapter<SpeakerAdapter.ViewHolder> {

    private Context context;
    private List<SpeakerDetail> videoDetailList;

    // Provide a suitable constructor (depends on the kind of dataset)
    public SpeakerAdapter(Context context, List<SpeakerDetail> videoDetailList) {
        this.context = context;
        this.videoDetailList = videoDetailList;
    }

    @Override
    public SpeakerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                        int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_speaker, parent, false);

        return new ViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.imgSpeaker.setTag(position);

//        holder.txtSpeakerName.setTextColor(context.getResources().getColor(R.color.colorPrimaryDark));
//        holder.txtSpeakerDesig.setTextColor(context.getResources().getColor(R.color.colorPrimary));
        holder.txtSpeakerName.setText(videoDetailList.get(position).getName());
//        holder.txtSpeakerDesig.setVisibility(View.GONE);
        holder.txtSpeakerDesig.setText(videoDetailList.get(position).getDesignation());
        holder.txtSpeakerTopic.setText(videoDetailList.get(position).getCompanyName());
        Picasso.with(context).load(videoDetailList.get(position).getImage()).placeholder(R.drawable.app_logo)
                .error(R.drawable.app_logo)
                .into(holder.imgSpeaker);

        holder.imgSpeaker.setTag(position);

        holder.lnrSpeakerDetail.setTag(position);
        holder.lnrSpeakerDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int positionCliked = (Integer) v.getTag();

                    Intent videoDescription = new Intent(context, ProfileDetailActivity.class);
                    videoDescription.putExtra("PROFILE_DETAIL", videoDetailList.get(positionCliked));
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

        LinearLayout lnrSpeakerDetail;
        public TextView txtSpeakerName;
        public ImageView imgSpeaker;
        public TextView txtSpeakerDesig;
        public TextView txtSpeakerTopic;

        public ViewHolder(View view) {
            super(view);
            lnrSpeakerDetail=(LinearLayout)view.findViewById(R.id.lnrSpeakerDetail);
            txtSpeakerName = (TextView) view.findViewById(R.id.txtSpeakerName);
            txtSpeakerTopic = (TextView) view.findViewById(R.id.txtSpeakerTopic);
            imgSpeaker = (ImageView) view.findViewById(R.id.imgSpeaker);
            txtSpeakerDesig = (TextView) view.findViewById(R.id.txtSpeakerDesig);
        }
    }
}

