package oac.com.oac.app.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.oacasia.R;

import java.util.List;

import oac.com.oac.app.activities.FeedbackSessionActivity;
import oac.com.oac.app.activities.OACFeedbackActivity;
import oac.com.oac.app.activities.QNAFeedBackActivity;
import oac.com.oac.app.modal.FeedParams;
import oac.com.oac.app.modal.Notification;

import static oac.com.oac.app.activities.NotificationActivity.NOTIF_STATUS;

/**
 * Created by Sudhir Singh on 09,July,2018
 * ESS,
 * B-65,Sector 63,Noida.
 */
public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {

    private Activity context;
    private List<Notification> videoDetailList;

    // Provide a suitable constructor (depends on the kind of dataset)
    public NotificationAdapter(Activity context, List<Notification> videoDetailList) {
        this.context = context;
        this.videoDetailList = videoDetailList;
    }

    @Override
    public NotificationAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                             int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.notification_adapter, parent, false);

        return new NotificationAdapter.ViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(NotificationAdapter.ViewHolder holder, int position) {

        holder.txtNotificationTitle.setText(videoDetailList.get(position).getTitle());
        holder.txtNotificationDesc.setText(videoDetailList.get(position).getDescription());
        holder.txtNotificationTime.setText(videoDetailList.get(position).getDate());

        holder.lnrNotification.setTag(position);
        holder.lnrNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int positionCliked = (Integer) v.getTag();

                if (videoDetailList.get(positionCliked).getType().equalsIgnoreCase(context.getString(R.string.feedbackSession))) {
                    Intent OACFeedbackIntent = new Intent(context, FeedbackSessionActivity.class);
                    String typeID = String.valueOf(videoDetailList.get(positionCliked).getTypeId());
                    OACFeedbackIntent.putExtra(FeedParams.TYPE_ID, typeID);
                    context.startActivityForResult(OACFeedbackIntent, NOTIF_STATUS);
                } else if (videoDetailList.get(positionCliked).getType().equalsIgnoreCase(context.getString(R.string.qnasessionfeedback))) {
                    Intent OACFeedbackIntent = new Intent(context, QNAFeedBackActivity.class);
                    String typeID = String.valueOf(videoDetailList.get(positionCliked).getTypeId());
                    OACFeedbackIntent.putExtra(FeedParams.TYPE_ID, typeID);
                    context.startActivityForResult(OACFeedbackIntent, NOTIF_STATUS);
                } else if (videoDetailList.get(positionCliked).getType().equalsIgnoreCase(context.getString(R.string.oacfeedback))) {
                    Intent OACFeedbackIntent = new Intent(context, OACFeedbackActivity.class);
                    OACFeedbackIntent.putExtra("RATE_TEXT", videoDetailList.get(positionCliked).getTitle());
                    context.startActivityForResult(OACFeedbackIntent, NOTIF_STATUS);
                } else if (videoDetailList.get(positionCliked).getType().equalsIgnoreCase(context.getString(R.string.feedbackexposession))) {
                    Intent OACFeedbackIntent = new Intent(context, OACFeedbackActivity.class);
                    OACFeedbackIntent.putExtra("RATE_TEXT", videoDetailList.get(positionCliked).getTitle());
                    context.startActivityForResult(OACFeedbackIntent, NOTIF_STATUS);
                } else if (videoDetailList.get(positionCliked).getType().equalsIgnoreCase(context.getString(R.string.oaafeedbacksession))) {
                    Intent OACFeedbackIntent = new Intent(context, OACFeedbackActivity.class);
                    OACFeedbackIntent.putExtra("RATE_TEXT", videoDetailList.get(positionCliked).getTitle());
                    context.startActivityForResult(OACFeedbackIntent, NOTIF_STATUS);
                }

//                Intent videoDescription = new Intent(context, ProfileDetailActivity.class);
//                videoDescription.putExtra("PROFILE_DETAIL", videoDetailList.get(positionCliked));
//                context.startActivity(videoDescription);

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

        public TextView txtNotificationTitle;
        public TextView txtNotificationTime;
        public TextView txtNotificationDesc;
        public LinearLayout lnrNotification;

        public ViewHolder(View view) {
            super(view);

            lnrNotification = (LinearLayout) view.findViewById(R.id.lnrNotification);
            txtNotificationTitle = (TextView) view.findViewById(R.id.txtNotificationTitle);
            txtNotificationDesc = (TextView) view.findViewById(R.id.txtNotificationDesc);
            txtNotificationTime = (TextView) view.findViewById(R.id.txtNotificationTime);
        }
    }
}
