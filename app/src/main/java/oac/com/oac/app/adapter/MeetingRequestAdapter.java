package oac.com.oac.app.adapter;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.telephony.PhoneNumberUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.oacasia.R;
import oac.com.oac.app.activities.MeetingProfileDetailActivity;
import oac.com.oac.app.activities.NetworkingProfileDetailActivity;
import oac.com.oac.app.fragment.MeFragment;
import oac.com.oac.app.modal.MeetingRequestDetail;
import oac.com.oac.app.modal.Networking;
import oac.com.oac.app.modal.Notification;

/**
 * Created by Sudhir Singh on 09,July,2018
 * ESS,
 * B-65,Sector 63,Noida.
 */
public class MeetingRequestAdapter extends RecyclerView.Adapter<MeetingRequestAdapter.ViewHolder> {

    private Context context;
    private List<MeetingRequestDetail> videoDetailList;

    public MeetingRequestAdapter(Context context, List<MeetingRequestDetail> videoDetailList) {
        this.context = context;
        this.videoDetailList = videoDetailList;
    }

    @Override
    public MeetingRequestAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                              int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.networking_dtl_adapter, parent, false);

        return new MeetingRequestAdapter.ViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(MeetingRequestAdapter.ViewHolder holder, int position) {

        holder.imgSpeaker.setTag(position);

        holder.txtSpeakerName.setText(videoDetailList.get(position).getToUserName());
        holder.txtSpeakerDesig.setText(videoDetailList.get(position).getDesignation());
        holder.txtSpeakerCompany.setText(videoDetailList.get(position).getCompany());

            holder.lnrChattingView.setVisibility(View.GONE);

        holder.imgSpeaker.setTag(position);
        Picasso.with(context).load(videoDetailList.get(position).getImage()).placeholder(R.drawable.app_logo)
                .error(R.drawable.app_logo)
                .into(holder.imgSpeaker);

        holder.lnrNetworkingDtl.setTag(position);
        holder.lnrNetworkingDtl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int positionCliked = (Integer) v.getTag();

                Intent videoDescription = new Intent(context, MeetingProfileDetailActivity.class);
                videoDescription.putExtra("NETWORKING_PROFILE_DETAIL", videoDetailList.get(positionCliked));
                context.startActivity(videoDescription);

//                int counter = 0;
//                for (counter = 0; counter < videoDetailList.size(); counter++) {
//                    if (videoDetailList.get(counter).isSelected()) {
//                        videoDetailList.get(counter).setSelected(false);
//                        notifyItemChanged(counter);
//                        break;
//                    }
//                }
//
//                if (counter != positionCliked) {
//                    if (videoDetailList.get(positionCliked).isSelected()) {
//                        videoDetailList.get(positionCliked).setSelected(false);
//                    } else {
//                        videoDetailList.get(positionCliked).setSelected(true);
//                    }
//                    notifyItemChanged(positionCliked);
//                }
            }
        });

        holder.imgChat.setTag(holder.getAdapterPosition());
        holder.imgChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int positionClicked = (Integer) v.getTag();
                openWhatsApp(videoDetailList.get(positionClicked).getPhone());
            }
        });

        holder.imgMail.setTag(holder.getAdapterPosition());
        holder.imgMail.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int positionClicked = (Integer) view.getTag();
                        sendEmail(videoDetailList.get(positionClicked).getEmail());
                    }
                }
        );

        holder.imgCall.setTag(holder.getAdapterPosition());
        holder.imgCall.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (checkPermission(context)) {
                            int positionClicked = (Integer) view.getTag();
                            String phoneNo = videoDetailList.get(positionClicked).getPhone();
                            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNo));
                            context.startActivity(intent);
                        } else {
                            Toast.makeText(context, "Permission required", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );

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

    private void sendEmail(String emailTO) {
        Intent email = new Intent(Intent.ACTION_SEND);
        email.putExtra(Intent.EXTRA_EMAIL, new String[]{emailTO});
        email.putExtra(Intent.EXTRA_SUBJECT, "");
        email.putExtra(Intent.EXTRA_TEXT, "");

        //need this to prompts email client only
        email.setType("message/rfc822");
        try {
            context.startActivity(Intent.createChooser(email, "Send mail..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(context, "There is no mailing app installed.", Toast.LENGTH_SHORT).show();
        }
    }

    private void openWhatsApp(String smsNumber) {
//        String smsNumber = "917835846055";
        boolean isWhatsappInstalled = whatsappInstalledOrNot("com.whatsapp");
        if (isWhatsappInstalled) {

            Intent sendIntent = new Intent("android.intent.action.MAIN");
            sendIntent.setComponent(new ComponentName("com.whatsapp", "com.whatsapp.Conversation"));
            sendIntent.putExtra("jid", PhoneNumberUtils.stripSeparators(smsNumber) + "@s.whatsapp.net");//phone number without "+" prefix

            context.startActivity(sendIntent);
        } else {
            Uri uri = Uri.parse("market://details?id=com.whatsapp");
            Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
            Toast.makeText(context, "WhatsApp not Installed",
                    Toast.LENGTH_SHORT).show();
            context.startActivity(goToMarket);
        }
    }

    private boolean whatsappInstalledOrNot(String uri) {
        PackageManager pm = context.getPackageManager();
        boolean app_installed = false;
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            app_installed = true;
        } catch (PackageManager.NameNotFoundException e) {
            app_installed = false;
        }
        return app_installed;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private boolean checkPermission(final Context context) {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if (currentAPIVersion >= android.os.Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, Manifest.permission.CALL_PHONE)) {
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
                    alertBuilder.setCancelable(true);
                    alertBuilder.setTitle("Permission necessary");
                    alertBuilder.setMessage("External storage permission is necessary");
                    alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.CALL_PHONE}, MeFragment.MY_PERMISSIONS_REQUEST_MAKE_CALL);
                        }
                    });
                    AlertDialog alert = alertBuilder.create();
                    alert.show();
                } else {
                    ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.CALL_PHONE}, MeFragment.MY_PERMISSIONS_REQUEST_MAKE_CALL);
                }
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }



    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imgSpeaker;
        TextView txtSpeakerName;
        TextView txtSpeakerDesig;
        TextView txtSpeakerCompany;
        LinearLayout lnrChattingView;
        LinearLayout lnrNetworkingDtl;

        View imgChat;
        View imgMail;
        View imgCall;

        ViewHolder(View view) {
            super(view);

            lnrChattingView = (LinearLayout) view.findViewById(R.id.lnrChattingView);
            txtSpeakerName = (TextView) view.findViewById(R.id.txtSpeakerName);
            txtSpeakerCompany = (TextView) view.findViewById(R.id.txtSpeakerTopic);
            imgSpeaker = (ImageView) view.findViewById(R.id.imgSpeaker);
            txtSpeakerDesig = (TextView) view.findViewById(R.id.txtSpeakerDesig);
            lnrNetworkingDtl = (LinearLayout) view.findViewById(R.id.lnrNetworkingDtl);

            imgChat = view.findViewById(R.id.imgAdaChat);
            imgMail = view.findViewById(R.id.lnrAdaMail);
            imgCall = view.findViewById(R.id.lnrAdaCall);

        }
    }
}
