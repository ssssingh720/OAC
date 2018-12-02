package oac.com.oac.app.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

import org.oacasia.R;
import oac.com.oac.app.networking.RequestManager;

/**
 * Created by Sudhir Singh on 07,July,2018
 * ESS,
 * B-65,Sector 63,Noida.
 */
public class ImagePagerAdapter extends RecyclingPagerAdapter {

    private Context context;
    private List<String> imageIdList;

    private int size;
    private boolean isInfiniteLoop;

    public ImagePagerAdapter(Context context, List<String> imageIdList) {
        this.context = context;
        this.imageIdList = imageIdList;
//        this.size = ListUtils.getSize(imageIdList);
        this.size = imageIdList.size();
        isInfiniteLoop = false;
    }

    @Override
    public int getCount() {
        // Infinite loop
//        return isInfiniteLoop ? Integer.MAX_VALUE : ListUtils.getSize(imageIdList);
        return isInfiniteLoop ? Integer.MAX_VALUE : imageIdList.size();
    }

    /**
     * get really position
     *
     * @param position
     * @return
     */
    private int getPosition(int position) {
        return isInfiniteLoop ? position % size : position;
    }

    @Override
    public View getView(int position, View view, ViewGroup container) {
        ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = holder.imageView = new ImageView(context);
            holder.imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.MATCH_PARENT));
//            holder.imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        Picasso.with(context).load
                (RequestManager.LIVE_SERVER + imageIdList.get(getPosition(position)))
                .into(holder.imageView);

        return view;
    }

    /**
     * @return the isInfiniteLoop
     */
    public boolean isInfiniteLoop() {
        return isInfiniteLoop;
    }

    /**
     * @param isInfiniteLoop the isInfiniteLoop to set
     */
    public ImagePagerAdapter setInfiniteLoop(boolean isInfiniteLoop) {
        this.isInfiniteLoop = isInfiniteLoop;
        return this;
    }

    private static class ViewHolder {

        ImageView imageView;
    }

}
