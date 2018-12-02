package oac.com.oac.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

import org.oacasia.R;
import oac.com.oac.app.modal.Sponsors;

/**
 * Created by Sudhir Singh on 07,July,2017
 * ESS,
 * B-65,Sector 63,Noida.
 */
public class SponsorsAdapter extends BaseAdapter {

    private List<Sponsors> sponsorsList;
    private Context context;

    public SponsorsAdapter(Context context, List<Sponsors> sponsorsList) {
        this.context = context;
        this.sponsorsList = sponsorsList;
    }

    // 2
    @Override
    public int getCount() {
        return sponsorsList.size();
    }

    // 3
    @Override
    public long getItemId(int position) {
        return 0;
    }

    // 4
    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // view holder pattern
        if (convertView == null) {
            final LayoutInflater layoutInflater = LayoutInflater.from(context);
            convertView = layoutInflater.inflate(R.layout.home_grid_adapter, null);

            final ImageView imgHomeIcon = (ImageView) convertView.findViewById(R.id.imgHomeIcon);

            final ViewHolder viewHolder = new ViewHolder(imgHomeIcon);
            convertView.setTag(viewHolder);
        }

        final ViewHolder viewHolder = (ViewHolder) convertView.getTag();

        Picasso.with(context).load(sponsorsList.get(position).getImage()).placeholder(R.drawable.app_logo)
                .error(R.drawable.app_logo)
                .into(viewHolder.imgHomeIcon);

        return convertView;
    }

    private class ViewHolder {
        private final ImageView imgHomeIcon;

        public ViewHolder(ImageView imgHomeIcon) {
            this.imgHomeIcon = imgHomeIcon;
        }
    }
}
