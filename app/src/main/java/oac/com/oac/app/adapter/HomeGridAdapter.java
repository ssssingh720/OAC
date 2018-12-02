package oac.com.oac.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import org.oacasia.R;

/**
 * Created by Sudhir Singh on 07,July,2017
 * ESS,
 * B-65,Sector 63,Noida.
 */
public class HomeGridAdapter extends BaseAdapter {

    private int home_images[] = {R.drawable.about_us,R.drawable.agenda,R.drawable.speaker,R.drawable.jury,R.drawable.ques_analysis,
            R.drawable.venue,R.drawable.sponspors,R.drawable.quiz,R.drawable.networking};
    private Context context;

    public HomeGridAdapter(Context context) {
        this.context = context;
    }

    // 2
    @Override
    public int getCount() {
        return home_images.length;
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

        viewHolder.imgHomeIcon.setImageResource(home_images[position]);

        return convertView;
    }

    private class ViewHolder {
        private final ImageView imgHomeIcon;

        public ViewHolder(ImageView imgHomeIcon) {
            this.imgHomeIcon = imgHomeIcon;
        }
    }
}
