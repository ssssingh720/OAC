package oac.com.oac.app.fragment;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.util.Base64;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.firebase.iid.FirebaseInstanceId;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.HashMap;

import org.oacasia.R;
import oac.com.oac.app.activities.ChangePasswordActivity;
import oac.com.oac.app.activities.EditProfileActivity;
import oac.com.oac.app.activities.SignInActivity;
import oac.com.oac.app.load_toast.LoadToast;
import oac.com.oac.app.manager.SharedPrefManager;
import oac.com.oac.app.modal.BaseVO;
import oac.com.oac.app.modal.FeedParams;
import oac.com.oac.app.modal.ImageVo;
import oac.com.oac.app.modal.ResponseError;
import oac.com.oac.app.utils.APIMethods;
import oac.com.oac.app.utils.CircleTransform;
import oac.com.oac.app.utils.ImageUtility;

import static android.app.Activity.RESULT_OK;
import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE;

/**
 * Created by Sudhir Singh on 08,July,2017
 * ESS,
 * B-65,Sector 63,Noida.
 */
public class MeFragment extends BaseFragment implements View.OnClickListener {
    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;
    public static final int MY_PERMISSIONS_REQUEST_MAKE_CALL = 125;
    private static final int PICK_FROM_CAMERA = 41;
    private static final int PICK_FROM_GALLERY = 42;


    private static final int PERMISSION_CAMERA_REQUEST_CODE = 2;
    private static final int PERMISSION_STORAGE_REQUEST_CODE = 3;
    private static final int REQUEST_OPEN_CAMERA_SETTING_PAGE = 4;
    private static final int REQUEST_OPEN_GALLERY_SETTING_PAGE = 5;
    private static final int OPEN_CAMERA_FROM_M_OR_GREATER = 1;
    private static final int OPEN_CAMERA_FROM_L_OR_LOWER = 2;
    private static final int OPEN_GALLARY_FROM_M_OR_GREATER = 3;
    private static final int OPEN_GALLARY_FROM_L_OR_LOWER = 4;
    String mStoredImagePath = "";
    //    Drawable userImage;
//    String playerImageString;
//    String image_path, image_name;
    File file;
    private ImageView mIvCard;
    private Uri mUri;
    private String IMAGE_NAME = "";
    private ImageView imgUpdateProf;
    private Switch switch_notification;
    private LoadToast loadToast;

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public static boolean checkPermission(final Context context) {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if (currentAPIVersion >= android.os.Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
                    alertBuilder.setCancelable(true);
                    alertBuilder.setTitle("Permission necessary");
                    alertBuilder.setMessage("External storage permission is necessary");
                    alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                        }
                    });
                    AlertDialog alert = alertBuilder.create();
                    alert.show();
                } else {
                    ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                }
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.me_fragment, container, false);

        imgUpdateProf = (ImageView) mView.findViewById(R.id.imgUpdateProf);
        Button btnSignOut = (Button) mView.findViewById(R.id.btnSignOut);
        Button btnEditInfo = (Button) mView.findViewById(R.id.btnEditInfo);
        Button btnChangePwd = (Button) mView.findViewById(R.id.btnChangePwd);
        TextView txtUserName = (TextView) mView.findViewById(R.id.txtUserName);
        switch_notification = (Switch) mView.findViewById(R.id.switch_notification);

        String user_image = SharedPrefManager.getInstance().getSharedDataString(FeedParams.IMAGE);
        String user_name = SharedPrefManager.getInstance().getSharedDataString(FeedParams.NAME);

        txtUserName.setText(user_name);
        if (user_image != null && user_image.trim().length() > 0)
            Picasso.with(getActivity()).load(user_image).placeholder(R.drawable.app_logo)
                    .error(R.drawable.app_logo).transform(new CircleTransform())
                    .resize(100, 100).into(imgUpdateProf);

        Display display = getActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int height = size.y;
        loadToast = new LoadToast(getActivity());
        loadToast.setText("Please Wait...");
        loadToast.setTranslationY(height / 2);
        loadToast.setTextColor(Color.BLACK).setBackgroundColor(Color.WHITE)
                .setProgressColor(getResources().getColor(R.color.colorPrimary));

//        switch_notification.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (isChecked) {
//                    turnOnNotification();
//                } else {
//                    turnOffNotification();
//                }
//            }
//        });

//        SharedPrefManager.getInstance().setSharedData(FeedParams.NOTIFICATION_STATUS, true);
        switch_notification.setChecked(SharedPrefManager.getInstance().getSharedDataBoolean(FeedParams.NOTIFICATION_STATUS));
        switch_notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean status = SharedPrefManager.getInstance().getSharedDataBoolean(FeedParams.NOTIFICATION_STATUS);
                if (status) {
                    turnOffNotification();
                } else {
//                    switch_notification.setChecked(true);
                    turnOnNotification();
                }
            }
        });


        btnSignOut.setOnClickListener(this);
        btnEditInfo.setOnClickListener(this);
        btnChangePwd.setOnClickListener(this);
        imgUpdateProf.setOnClickListener(this);

        return mView;

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btnSignOut:

                loadToast.show();
                HashMap<String, String> params = new HashMap<>();
                String userId = SharedPrefManager.getInstance().getSharedDataString(FeedParams.USER_ID);
                String token = SharedPrefManager.getInstance().getSharedDataString(FeedParams.TOKEN);
                params.put(FeedParams.USER_ID, userId);
                params.put(FeedParams.TOKEN, token);

                placeRequest(APIMethods.SIGN_OUT, BaseVO.class, params, true, null);

                break;

            case R.id.btnEditInfo:

                Intent editProfileIntent = new Intent(getActivity(), EditProfileActivity.class);
                startActivity(editProfileIntent);

                break;

            case R.id.btnChangePwd:

                Intent changePwdIntent = new Intent(getActivity(), ChangePasswordActivity.class);
                startActivity(changePwdIntent);

                break;

            case R.id.imgUpdateProf:
//                boolean result = checkPermission(getActivity());
//                if (result) {
//
//                }

                selectImage();

                break;
        }
    }

    private void turnOnNotification() {
        loadToast.show();
        HashMap<String, String> params = new HashMap<>();
        //device_id, device_type, user_id, token
        String userId = SharedPrefManager.getInstance().getSharedDataString(FeedParams.USER_ID);
        String token = SharedPrefManager.getInstance().getSharedDataString(FeedParams.TOKEN);
        params.put(FeedParams.USER_ID, userId);
        params.put(FeedParams.TOKEN, token);
        String gcmToken = FirebaseInstanceId.getInstance().getToken();
        if (gcmToken != null && gcmToken.length() > 0) {
            params.put(FeedParams.DEVICE_ID, gcmToken);
        }
        params.put(FeedParams.DEVICE_TYPE, FeedParams.DEVICE_TYPE_NAME);

        placeRequest(APIMethods.REGISTER_NOTIFICATION, BaseVO.class, params, true, null);
    }

    private void turnOffNotification() {
        loadToast.show();
        HashMap<String, String> params = new HashMap<>();
        String userId = SharedPrefManager.getInstance().getSharedDataString(FeedParams.USER_ID);
        String token = SharedPrefManager.getInstance().getSharedDataString(FeedParams.TOKEN);
        params.put(FeedParams.USER_ID, userId);
        params.put(FeedParams.TOKEN, token);
        String gcmToken = FirebaseInstanceId.getInstance().getToken();
        if (gcmToken != null && gcmToken.length() > 0) {
            params.put(FeedParams.DEVICE_ID, gcmToken);
        }
        params.put(FeedParams.DEVICE_TYPE, FeedParams.DEVICE_TYPE_NAME);

        placeRequest(APIMethods.UNREGISTER_NOTIFICATION, BaseVO.class, params, true, null);
    }

    @Override
    public void onAPIResponse(Object response, String apiMethod) {
        super.onAPIResponse(response, apiMethod);
        loadToast.success();
        if (apiMethod.equalsIgnoreCase(APIMethods.SIGN_OUT)) {
            BaseVO baseVO = (BaseVO) response;

            showToast(baseVO.getMessage());

            SharedPrefManager.getInstance().clearPreference();
            Intent loginIntent = new Intent(getActivity(), SignInActivity.class);
            startActivity(loginIntent);
            getActivity().finish();

        } else if (apiMethod.equalsIgnoreCase(APIMethods.EDIT_IMAGE)) {

            ImageVo imageVo = (ImageVo) response;
            String filePath = imageVo.getFilePath();
            if (filePath != null && filePath.length() > 0) {

                SharedPrefManager.getInstance().setSharedData(FeedParams.IMAGE, filePath);
                Picasso.with(getActivity()).load(imageVo.getFilePath()).placeholder(R.drawable.app_logo)
                        .error(R.drawable.app_logo).transform(new CircleTransform())
                        .resize(100, 100).into(imgUpdateProf);

            } else {

                Picasso.with(getActivity()).load(R.drawable.app_logo).placeholder(R.drawable.app_logo)
                        .error(R.drawable.app_logo).transform(new CircleTransform())
                        .resize(100, 100).into(imgUpdateProf);

            }
        } else if (apiMethod.equalsIgnoreCase(APIMethods.REGISTER_NOTIFICATION) ||
                apiMethod.equalsIgnoreCase(APIMethods.UNREGISTER_NOTIFICATION)) {

            if (SharedPrefManager.getInstance().getSharedDataBoolean(FeedParams.NOTIFICATION_STATUS)) {
                SharedPrefManager.getInstance().setSharedData(FeedParams.NOTIFICATION_STATUS, false);
            } else {
                SharedPrefManager.getInstance().setSharedData(FeedParams.NOTIFICATION_STATUS, true);
            }
            switch_notification.setChecked(SharedPrefManager.getInstance().getSharedDataBoolean(FeedParams.NOTIFICATION_STATUS));
            BaseVO baseVO = (BaseVO) response;
            showToast(baseVO.getMessage());
        }
    }

    @Override
    public void onErrorResponse(VolleyError error, String apiMethod) {
        super.onErrorResponse(error, apiMethod);
        loadToast.error();

        if (apiMethod.equalsIgnoreCase(APIMethods.SIGN_OUT)) {

            ResponseError responseError = (ResponseError) error;
            if (responseError.getErrorMessage().equalsIgnoreCase(getResources().getString(R.string.invalid_token))) {
                SharedPrefManager.getInstance().clearPreference();
                Intent loginIntent = new Intent(getActivity(), SignInActivity.class);
                startActivity(loginIntent);
                getActivity().finish();

            }
            showToast(responseError.getErrorMessage());

        } else if (apiMethod.equalsIgnoreCase(APIMethods.EDIT_IMAGE)) {
            ResponseError responseError = (ResponseError) error;
            showToast(responseError.getErrorMessage());
        } else if (apiMethod.equalsIgnoreCase(APIMethods.REGISTER_NOTIFICATION)
                || apiMethod.equalsIgnoreCase(APIMethods.UNREGISTER_NOTIFICATION)) {

            ResponseError responseError = (ResponseError) error;
            showToast(responseError.getErrorMessage());
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] reqPermissionResult) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
            case PERMISSION_CAMERA_REQUEST_CODE:
                if (reqPermissionResult.length == 3 && reqPermissionResult[0] == PackageManager.PERMISSION_GRANTED && reqPermissionResult[1] == PackageManager.PERMISSION_GRANTED && reqPermissionResult[2] == PackageManager.PERMISSION_GRANTED) {
                    captureImageFromCamera();
                } else {
                    showToast(getString(R.string.camera_permission_decline));
                }
                break;
            case PERMISSION_STORAGE_REQUEST_CODE:
                if (reqPermissionResult.length == 2 && reqPermissionResult[0] == PackageManager.PERMISSION_GRANTED && reqPermissionResult[1] == PackageManager.PERMISSION_GRANTED) {
                    getImageFromGallery();
                } else {
                    showToast(getString(R.string.gallery_permission_cancel));
                }
                break;
        }
    }

    private void selectImage() {

        final CharSequence[] items = {"Take a photo", "Import", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Take a photo")) {

                    checkCameraPermission();

//                    // use standard intent to capture an image
//                    Intent captureIntent = new Intent(
//                            MediaStore.ACTION_IMAGE_CAPTURE);
//                    // we will handle the returned data in onActivityResult
//                    startActivityForResult(captureIntent, PICK_FROM_CAMERA);
                } else if (items[item].equals("Import")) {
                    checkGalleryPermission();
//                    Intent intent = new Intent(
//                            Intent.ACTION_PICK,
//                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                    intent.setType("image/*");
//                    startActivityForResult(
//                            Intent.createChooser(intent, "Select File"),
//                            PICK_FROM_GALLERY);

                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);

        try {
            mStoredImagePath = "";
            if (requestCode == OPEN_CAMERA_FROM_L_OR_LOWER) {
                if (resultCode == RESULT_OK) {
                    Bitmap bitmap = ImageUtility.getBitmapFromUri(getActivity(), mUri);
                    if (bitmap != null) {
                        checkAndSubmitData(bitmap);
                    }
                }
            } else if (requestCode == OPEN_CAMERA_FROM_M_OR_GREATER) {
                if (resultCode == RESULT_OK) {
                    File f = new File(Environment.getExternalStorageDirectory().toString());
                    for (File temp : f.listFiles()) {
                        if (temp.getName().equals(IMAGE_NAME)) {
                            f = temp;
                            break;
                        }
                    }
                    try {
                        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
                        mUri = getImageUri(BitmapFactory.decodeFile(f.getAbsolutePath(), bitmapOptions));
                        Bitmap bitmap = ImageUtility.getBitmapFromUri(getActivity(), mUri);
                        if (bitmap != null) {
                            checkAndSubmitData(bitmap);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } else if (requestCode == OPEN_GALLARY_FROM_M_OR_GREATER) {
                if (data != null) {
                    mUri = data.getData();
                    Bitmap bitmap = ImageUtility.getBitmapFromUri(getActivity(), mUri);
                    if (bitmap != null) {
                        checkAndSubmitData(bitmap);
                    }
                }
            } else if (requestCode == OPEN_GALLARY_FROM_L_OR_LOWER) {
                if (data != null) {
                    mUri = data.getData();
                    Bitmap bitmap = ImageUtility.getBitmapFromUri(getActivity(), mUri);
                    if (bitmap != null) {
                        checkAndSubmitData(bitmap);
                    }
                }
            }

        } catch (Exception e) {
            showToast("Image size is too large.");
        }
    }

    private void checkAndSubmitData(Bitmap bitmap) {
//        mStoredImagePath = ImageUtility.insertBitmapToAppFolder(bitmap, getActivity());
        loadToast.show();

        HashMap<String, String> params = new HashMap<>();
        String userId = SharedPrefManager.getInstance().getSharedDataString(FeedParams.USER_ID);
        String token = SharedPrefManager.getInstance().getSharedDataString(FeedParams.TOKEN);

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] data = stream.toByteArray();
        String imgString = Base64.encodeToString(data, Base64.NO_WRAP);

        if(IMAGE_NAME!=null && IMAGE_NAME.length()<=0) {
            IMAGE_NAME = "oac2018_" + String.valueOf(System.currentTimeMillis()) + ".jpg";
        }
        params.put(FeedParams.USER_ID, userId);
        params.put(FeedParams.TOKEN, token);
        params.put(FeedParams.FILE_NAME, IMAGE_NAME);
        params.put(FeedParams.IMAGE, imgString);

//        placeMultiPartRequest(APIMethods.EDIT_IMAGE, ImageVo.class, params, file, image_name);
        placeRequest(APIMethods.EDIT_IMAGE, ImageVo.class, params, true, null);

//        new UpdatePhoto().execute();
    }

    private boolean checkPermission(Activity activity, String permission) {
        boolean isGranted = ContextCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_GRANTED;
        return isGranted;
    }

    private void checkGalleryPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) && checkPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                getImageFromGallery();
            } else {
                if (!ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) && !ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_STORAGE_REQUEST_CODE);
                } else {
                    openSettingForExternalStorage();
                }
            }
        } else {
            getImageFromGallery();
        }
    }

    private void checkCameraPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkPermission(getActivity(), Manifest.permission.CAMERA) && checkPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) && checkPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                captureImageFromCamera();
            } else {
                if (!ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.CAMERA) &&
                        !ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) && !ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_CAMERA_REQUEST_CODE);
                } else {
                    openSettingForCamraAndExternal();
                }
            }
        } else {
            captureImageFromCamera();
        }
    }

    private void openSettingForCamraAndExternal() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        alertDialog.setTitle("Camera and External Permission Required ");
        alertDialog.setMessage("Show Camera and External settings?");

        alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getActivity().getPackageName(), null);
                intent.setData(uri);
                startActivityForResult(intent, REQUEST_OPEN_CAMERA_SETTING_PAGE);
                showToast(getString(R.string.go_to_permission));
            }
        });
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                showToast(getString(R.string.permission_required));
                dialog.cancel();
            }
        });
        alertDialog.show();
    }

    private void openSettingForExternalStorage() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        alertDialog.setTitle(R.string.storage_permission_required);
        alertDialog.setMessage(R.string.show_external_setting);

        alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getActivity().getPackageName(), null);
                intent.setData(uri);
                startActivityForResult(intent, REQUEST_OPEN_GALLERY_SETTING_PAGE);
                showToast(getString(R.string.grant_storage));
            }
        });
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                showToast(getString(R.string.permission_required));
                dialog.cancel();
            }
        });
        alertDialog.show();
    }

    private boolean checkValidity() {
        return mUri != null;
    }

    private void captureImageFromCamera() {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            IMAGE_NAME = "oac2018_" + String.valueOf(System.currentTimeMillis()) + ".jpg";
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            File f = new File(android.os.Environment.getExternalStorageDirectory(), IMAGE_NAME);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(getActivity(), getActivity().getApplicationContext().getPackageName() + ".provider", f));
            startActivityForResult(intent, OPEN_CAMERA_FROM_M_OR_GREATER);
        } else {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            mUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE, "oac2018_" + String.valueOf(System.currentTimeMillis()));
            intent.putExtra(MediaStore.EXTRA_OUTPUT, mUri);
            startActivityForResult(intent, OPEN_CAMERA_FROM_L_OR_LOWER);
        }
    }

    private Uri getOutputMediaFileUri(int type, String path) {
        // return Uri.fromFile(getOutputMediaFile(type, path));
        return Uri.fromFile(new File(Environment.getExternalStorageDirectory(), path));
    }

    public void getImageFromGallery() {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Intent intentGalleryM = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(Intent.createChooser(intentGalleryM, "Select Image From Gallery"), OPEN_GALLARY_FROM_M_OR_GREATER);
        } else {
            Intent intentGalleryL = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(Intent.createChooser(intentGalleryL, "Select Image From Gallery"), OPEN_GALLARY_FROM_L_OR_LOWER);
        }
    }

    private Uri getImageUri(Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 70, bytes);
        String path = MediaStore.Images.Media.insertImage(getActivity().getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

}
