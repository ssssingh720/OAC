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
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.List;

import org.oacasia.R;
import oac.com.oac.app.fragment.MeFragment;
import oac.com.oac.app.modal.Networking;
import oac.com.oac.app.recyclerview_slide.Extension;

/**
 * Created by Sudhir Singh on 07,July,2017
 * ESS,
 * B-65,Sector 63,Noida.
 */
public class NetworkingAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int ITEM_TYPE_ACTION_WIDTH = 1001;
    private List<Networking> listSalesVoucher;
    private Activity mContext;

    public NetworkingAdapter(Activity context, List<Networking> listSalesVoucher) {
        mContext = context;
        this.listSalesVoucher = listSalesVoucher;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public static boolean checkPermission(final Context context) {
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

    private LayoutInflater getLayoutInflater() {
        return LayoutInflater.from(mContext);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = getLayoutInflater().inflate(R.layout.networking_option_adapter, parent, false);
        if (viewType == ITEM_TYPE_ACTION_WIDTH)
            return new ItemSwipeWithActionWidthNoSpringViewHolder(view);

        return new ItemSwipeWithActionWidthNoSpringViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {

        ItemBaseViewHolder baseViewHolder = (ItemBaseViewHolder) holder;

        baseViewHolder.bind(listSalesVoucher.get(position));
        baseViewHolder.mViewContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listSalesVoucher.size() > 1) {
//                    Intent approvalDetail = new Intent(mContext, PendingBidInfo.class);
//                    approvalDetail.putExtra("BID_INFO", listSalesVoucher.get(holder.getAdapterPosition()));
//
//                    if (Build.VERSION.SDK_INT > 20) {
////                    Pair<View, String> pair1 = Pair.create((View) ((SalesRecyclerAdapter.ItemBaseViewHolder) holder).textId, mContext.getResources().getString(R.string.document_id));
////                    Pair<View, String> pair2 = Pair.create((View) ((SalesRecyclerAdapter.ItemBaseViewHolder) holder).mVoucherCreatedDate, mContext.getResources().getString(R.string.document_date));
////
////                    ActivityOptionsCompat options = ActivityOptionsCompat.
////                            makeSceneTransitionAnimation(mContext, pair1, pair2);
////                    mContext.startActivity(approvalDetail, options.toBundle());
//                        mContext.startActivity(approvalDetail);
//                    } else {
//                        mContext.startActivity(approvalDetail);
//                        mContext.overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
//                    }
                }
            }
        });
        if (holder instanceof ItemSwipeWithActionWidthViewHolder) {
            ItemSwipeWithActionWidthViewHolder viewHolder = (ItemSwipeWithActionWidthViewHolder) holder;


            viewHolder.imgChat.setTag(holder.getAdapterPosition());
            viewHolder.imgChat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int positionClicked = (Integer) v.getTag();
                    openWhatsApp(listSalesVoucher.get(positionClicked).getPhone());
                }
            });

            viewHolder.imgMail.setTag(holder.getAdapterPosition());
            viewHolder.imgMail.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            int positionClicked = (Integer) view.getTag();
                            sendEmail(listSalesVoucher.get(positionClicked).getEmail());
                        }
                    }
            );

            viewHolder.imgCall.setTag(holder.getAdapterPosition());
            viewHolder.imgCall.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (checkPermission(mContext)) {
                                int positionClicked = (Integer) view.getTag();
                                String phoneNo = listSalesVoucher.get(positionClicked).getPhone();
                                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNo));
                                mContext.startActivity(intent);
                            } else {
                                Toast.makeText(mContext, "Permission required", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
            );

        }
    }

    @Override
    public int getItemViewType(int position) {
        return ITEM_TYPE_ACTION_WIDTH;
    }


    //  private void doDelete(int adapterPosition, Dialog dialog) {
//        dialog.dismiss();
//        listSalesVoucher.remove(adapterPosition);
//        notifyItemRemoved(adapterPosition);
//    }

    @Override
    public int getItemCount() {
        return listSalesVoucher.size();
    }

    private void sendEmail(String emailTO) {
        Intent email = new Intent(Intent.ACTION_SEND);
        email.putExtra(Intent.EXTRA_EMAIL, new String[]{emailTO});
        email.putExtra(Intent.EXTRA_SUBJECT, "");
        email.putExtra(Intent.EXTRA_TEXT, "");

        //need this to prompts email client only
        email.setType("message/rfc822");
        try {
            mContext.startActivity(Intent.createChooser(email, "Send mail..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(mContext, "There is no mailing app installed.", Toast.LENGTH_SHORT).show();
        }
    }

    private void openWhatsApp(String smsNumber) {
//        String smsNumber = "917835846055";
        boolean isWhatsappInstalled = whatsappInstalledOrNot("com.whatsapp");
        if (isWhatsappInstalled) {

            Intent sendIntent = new Intent("android.intent.action.MAIN");
            sendIntent.setComponent(new ComponentName("com.whatsapp", "com.whatsapp.Conversation"));
            sendIntent.putExtra("jid", PhoneNumberUtils.stripSeparators(smsNumber) + "@s.whatsapp.net");//phone number without "+" prefix

            mContext.startActivity(sendIntent);
        } else {
            Uri uri = Uri.parse("market://details?id=com.whatsapp");
            Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
            Toast.makeText(mContext, "WhatsApp not Installed",
                    Toast.LENGTH_SHORT).show();
            mContext.startActivity(goToMarket);
        }
    }

    private boolean whatsappInstalledOrNot(String uri) {
        PackageManager pm = mContext.getPackageManager();
        boolean app_installed = false;
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            app_installed = true;
        } catch (PackageManager.NameNotFoundException e) {
            app_installed = false;
        }
        return app_installed;
    }

    public class ItemBaseViewHolder extends RecyclerView.ViewHolder {
        //slide options
        public View mViewContent;
        public View mActionContainer;
        ImageView imgSpeaker;
        TextView txtSpeakerName;
        TextView txtSpeakerDesig;
        TextView txtSpeakerCompany;

        private ItemBaseViewHolder(View itemView) {
            super(itemView);
            imgSpeaker = (ImageView) itemView.findViewById(R.id.imgSpeaker);
            txtSpeakerName = (TextView) itemView.findViewById(R.id.txtSpeakerName);
            txtSpeakerDesig = (TextView) itemView
                    .findViewById(R.id.txtSpeakerDesig);
            txtSpeakerCompany = (TextView) itemView
                    .findViewById(R.id.txtSpeakerTopic);
            //slide options
            mViewContent = itemView.findViewById(R.id.item_list_approval);
            mActionContainer = itemView.findViewById(R.id.view_list_repo_action_container);
        }

        private void bind(Networking bidData) {
            txtSpeakerName.setText(bidData.getName());
            txtSpeakerDesig.setText(bidData.getDesignation());
            txtSpeakerCompany.setText(bidData.getCompany());
            Picasso.with(mContext).load(bidData.getImage()).placeholder(R.drawable.app_logo)
                    .error(R.drawable.app_logo)
                    .into(imgSpeaker);

            imgSpeaker.setTag(bidData.getImage());
        }
    }

    class ItemSwipeWithActionWidthViewHolder extends ItemBaseViewHolder implements Extension {

        View imgChat;
        View imgMail;
        View imgCall;

        private ItemSwipeWithActionWidthViewHolder(View itemView) {
            super(itemView);
            imgChat = itemView.findViewById(R.id.imgChat);
            imgMail = itemView.findViewById(R.id.imgMail);
            imgCall = itemView.findViewById(R.id.imgCall);
        }

        @Override
        public float getActionWidth() {
            return mActionContainer.getWidth();
        }
    }

    public class ItemSwipeWithActionWidthNoSpringViewHolder extends ItemSwipeWithActionWidthViewHolder implements Extension {

        private ItemSwipeWithActionWidthNoSpringViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        public float getActionWidth() {
            return mActionContainer.getWidth();
        }
    }
}
