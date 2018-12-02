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
public class JuryAdapter extends RecyclerView.Adapter<JuryAdapter.ViewHolder> {

    private Context context;
    private List<SpeakerDetail> videoDetailList;

    public JuryAdapter(Context context, List<SpeakerDetail> videoDetailList) {
        this.context = context;
        this.videoDetailList = videoDetailList;
    }

    @Override
    public JuryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_jury, parent, false);

        return new JuryAdapter.ViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(JuryAdapter.ViewHolder holder, int position) {

        holder.imgJury.setTag(position);

        holder.txtJuryName.setTextColor(context.getResources().getColor(R.color.colorPrimary));
        holder.txtJuryName.setText(videoDetailList.get(position).getName());

//        holder.txtJuryProf.setVisibility(View.GONE);
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
//            holder.txtJuryProf.setText(Html.fromHtml(videoDetailList.get(position).getProfile(),Html.FROM_HTML_MODE_LEGACY));
//        } else {
//            holder.txtJuryProf.setText(Html.fromHtml(videoDetailList.get(position).getProfile()));
//        }
        holder.txtJuryDesig.setText(videoDetailList.get(position).getDesignation());
        holder.txtJuryCompany.setText(videoDetailList.get(position).getCompanyName());

        holder.imgJury.setTag(position);
        Picasso.with(context).load(videoDetailList.get(position).getImage()).placeholder(R.drawable.app_logo)
                .error(R.drawable.app_logo)
                .into(holder.imgJury);

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

        public TextView txtJuryName;
        public ImageView imgJury;
        public TextView txtJuryDesig;
        public TextView txtJuryCompany;
//        public TextView txtJuryProf;
        LinearLayout lnrSpeakerDetail;

        public ViewHolder(View view) {
            super(view);

            lnrSpeakerDetail = (LinearLayout) view.findViewById(R.id.lnrSpeakerDetail);
            txtJuryName = (TextView) view.findViewById(R.id.txtJuryName);
//            txtJuryProf = (TextView) view.findViewById(R.id.txtJuryProf);
            imgJury = (ImageView) view.findViewById(R.id.imgJury);
            txtJuryDesig = (TextView) view.findViewById(R.id.txtJuryDesig);
            txtJuryCompany = (TextView) view.findViewById(R.id.txtJuryCompany);
        }
    }
}
