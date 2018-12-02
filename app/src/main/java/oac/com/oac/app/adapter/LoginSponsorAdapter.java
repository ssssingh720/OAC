package oac.com.oac.app.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

import org.oacasia.R;
import oac.com.oac.app.modal.Sponsors;

/**
 * Created by Sudhir Singh on 11,July,2017
 * ESS,
 * B-65,Sector 63,Noida.
 */
public class LoginSponsorAdapter extends RecyclerView.Adapter<LoginSponsorAdapter.ViewHolder> {

    private Context context;
    private List<Sponsors> sponsorsList;

    // Provide a suitable constructor (depends on the kind of dataset)
    public LoginSponsorAdapter(Context context, List<Sponsors> sponsorsList) {
        this.context = context;
        this.sponsorsList = sponsorsList;
    }

    @Override
    public LoginSponsorAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                             int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.home_grid_adapter, parent, false);

        return new ViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.imgHomeIcon.setTag(position);

        Picasso.with(context).load(sponsorsList.get(position).getImage()).placeholder(R.drawable.app_logo)
                .error(R.drawable.app_logo)
                .into(holder.imgHomeIcon);
    }

    @Override
    public int getItemCount() {
        return sponsorsList.size();
//        return 30;
    }

      static class ViewHolder extends RecyclerView.ViewHolder {

          ImageView imgHomeIcon;

          ViewHolder(View view) {
            super(view);
            imgHomeIcon = (ImageView) view.findViewById(R.id.imgHomeIcon);
        }
    }
}
