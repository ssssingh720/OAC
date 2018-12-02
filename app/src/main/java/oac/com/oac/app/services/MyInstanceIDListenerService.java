/**
 * Copyright 2015 Google Inc. All Rights Reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package oac.com.oac.app.services;

import android.util.Log;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import org.json.JSONObject;

import oac.com.oac.app.manager.SharedPrefManager;
import oac.com.oac.app.modal.BaseVO;
import oac.com.oac.app.modal.FeedParams;
import oac.com.oac.app.networking.RequestManager;
import oac.com.oac.app.networking.ServerCallback;
import oac.com.oac.app.utils.APIMethods;


public class MyInstanceIDListenerService extends FirebaseInstanceIdService {


    private static final String TAG = MyInstanceIDListenerService.class.getSimpleName();

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();

        Log.e(TAG, "Token Body: " + refreshedToken);
        // Saving reg id to shared preferences
//       if( SharedPrefManager.getInstance().getSharedDataString(FeedParams.USER_ID).trim().length()>0) {
//           storeRegIdInPref(refreshedToken);
//       }

        SharedPrefManager prefManager = SharedPrefManager.getInstance();
        prefManager.init(getApplicationContext());
        prefManager.setSharedData(FeedParams.GCM_TOKEN, refreshedToken);

        // sending reg id to your server
//        sendRegistrationToServer(refreshedToken);
//
//        // Notify UI that registration has completed, so the progress indicator can be hidden.
//        Intent registrationComplete = new Intent(Config.REGISTRATION_COMPLETE);
//        registrationComplete.putExtra("token", refreshedToken);
//        LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
    }

    private void sendRegistrationToServer(final String token) {
        // sending gcm token to server
        Log.e(TAG, "sendRegistrationToServer: " + token);

    }

//    private void storeRegIdInPref(String token) {
//
//        SharedPrefManager prefManager = SharedPrefManager.getInstance();
//        prefManager.init(getApplicationContext());
//        prefManager.setSharedData(FeedParams.GCM_TOKEN, token);
//
//        prefManager.setSharedData(TMS_CONSTANTS.SENT_TOKEN_TO_SERVER, false);
//
//        try {
//            JSONObject obj = new JSONObject();
//            String user_id = SharedPrefManager.getInstance().getSharedDataString(FeedParams.USER_ID);
//            obj.put("user_id", user_id);
//            obj.put("device_id", token);
//            obj.put("device_type", "android");
//
//
//            RequestManager.getInstance(getApplicationContext()).placeRequest(APIMethods.REGISTER_NOTIFICATION, BaseVO.class, this, obj, true, null);
//
//        } catch (Exception e) {
//
//        }
//
//
//    }

//    @Override
//    public void complete(int code) {
//
//    }

//    @Override
//    public void onAPIResponse(Object response, String apiMethod) {
//
//        if (apiMethod.equalsIgnoreCase(APIMethods.REGISTER_NOTIFICATION)) {
//            BaseVO tokenInfo = (BaseVO) response;
//            if (tokenInfo.getMessageCode() == 0) {
//                SharedPrefManager prefManager = SharedPrefManager.getInstance();
//                prefManager.init(getApplicationContext());
//                prefManager.setSharedData(TMS_CONSTANTS.SENT_TOKEN_TO_SERVER, true);
//            }else{
//                Toast.makeText(getApplicationContext(), ""+tokenInfo.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        }
//    }
//
//    @Override
//    public void onErrorResponse(VolleyError error, String apiMethod) {
//        Toast.makeText(getApplicationContext(), "Could not Register for Notification.", Toast.LENGTH_SHORT).show();
//    }
}