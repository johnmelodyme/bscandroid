package com.starlabbioscience.bloodsugarcontrol.service;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Looper;
import android.util.Log;

import com.google.gson.Gson;
import com.starlabbioscience.bloodsugarcontrol.MusicPlayer;
import com.starlabbioscience.bloodsugarcontrol.model.AuthenticationXhrModel;
import com.starlabbioscience.bloodsugarcontrol.model.UserModel;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javax.annotation.ParametersAreNullableByDefault;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;

import cn.pedant.SweetAlert.SweetAlertDialog;
import io.realm.Realm;

public class TokenService extends AsyncTask<String, String, String> {
    private static final String TAG = LoginService.class.getName();
    private final WeakReference<Context> contextWeakReference;
    private final String URL;
    private final Map<String, String> URL_DATA;

    public TokenService(Context context) {
        // put your URL here
        URL = "https://sponline.xyz/starlabs/verifyToken.php";

        String ERROR_MESSAGE = "Access Denied";
        // parameter in  doInBackground
        URL_DATA = new HashMap<>();
        // assign context but in weak reference because of memory leak warning
        contextWeakReference = new WeakReference<>(context);
    }

    protected String doInBackground(String... strings) {
        // put your variable array/parameter/data here
        URL_DATA.put("e", strings[0]);

        StringBuilder stringBuilder = new StringBuilder();
        for (Map.Entry<String, String> entry : URL_DATA.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            stringBuilder.append(key).append("=").append(value).append("&");
        }
        // remove the end line &
        String parameter = stringBuilder.deleteCharAt(stringBuilder.lastIndexOf("&")).toString();

        String access = "granted";
        Looper.prepare();
        try {
            SSLContext sslcontext = SSLContext.getInstance("TLS");
            sslcontext.init(null, null, null);
            SSLSocketFactory sslSocketFactory = sslcontext.getSocketFactory();

            java.net.URL urlObj = new URL(URL);
            final HttpsURLConnection conn = (HttpsURLConnection) urlObj.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Accept-Charset", "UTF-8");

            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setSSLSocketFactory(sslSocketFactory);

            conn.connect();

            DataOutputStream dataOutputStream = new DataOutputStream(conn.getOutputStream());
            dataOutputStream.writeBytes("email=admin1@admin.com&password=123");
            // bind the parameter to stream
            //dataOutputStream.writeBytes(parameter);

            dataOutputStream.flush();
            dataOutputStream.close();

            try {
                InputStream in = new BufferedInputStream(conn.getInputStream());
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                StringBuilder result = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }

                // check server response
                Log.d(TAG, "result from server: " + result.toString());

                // sync server response into POJO -> Plain old java object
                final AuthenticationXhrModel authenticationXhrModel = new Gson().fromJson(result.toString(), AuthenticationXhrModel.class);

                // Recheck sync model
                Log.d(TAG, authenticationXhrModel.toString());

                // since the object to the gson

                //transfer the data to local db
                // check the local db if existed with that id if not then insert
                Looper.prepare();
                final Realm realm = Realm.getDefaultInstance();
                if (authenticationXhrModel.getData().getStatus().equals("0")) {
                    realm.executeTransaction(new Realm.Transaction() {
                        @Override
                        @ParametersAreNullableByDefault
                        public void execute(Realm realm1) {
                            // maybe all ready consider in block?
                            //         realm1.beginTransaction();
                            String Id = String.valueOf(authenticationXhrModel.getData().getId());
                            UserModel userModel = realm1.where(UserModel.class).equalTo("id", Id).findFirst();

                            if (userModel == null) {
                                // add new record
                                Log.d(TAG, "Create a new record");
                                // userModel.setPhone(authenticationXhrModel.getData().setPhone());
                                UserModel userModel1 = realm1.createObject(UserModel.class);
                                userModel1.setId(authenticationXhrModel.getData().getId());

                                userModel1.setFirst_name(authenticationXhrModel.getData().getFirst_name());
                                userModel1.setEmail(authenticationXhrModel.getData().getEmail());
                                userModel1.setToken(authenticationXhrModel.getData().getToken());
                            } else {
                                /// hmm realm not default to string override ?
                                //Log.d(TAG,userModel.toString());
                                // how come not null ?
                                if (userModel.getEmail() == null) {
                                    Log.d(TAG, "Create a new record");
                                    // userModel.setPhone(authenticationXhrModel.getData().setPhone());
                                    UserModel userModel1 = realm1.createObject(UserModel.class);
                                    userModel1.setId(authenticationXhrModel.getData().getId());

                                    userModel1.setFirst_name(authenticationXhrModel.getData().getFirst_name());
                                    userModel1.setEmail(authenticationXhrModel.getData().getEmail());
                                    userModel1.setToken(authenticationXhrModel.getData().getToken());
                                } else {
                                    Log.d(TAG, "Record all ready existed");
                                    Log.d(TAG, "update the record");
                                    userModel.setFirst_name(authenticationXhrModel.getData().getFirst_name());
                                    userModel.setEmail(authenticationXhrModel.getData().getEmail());
                                    userModel.setToken(authenticationXhrModel.getData().getToken());
                                }
                            }
                            Context context = contextWeakReference.get();
                            if (context != null) {
                                Log.d(TAG, "context is null");

                                Intent intent = new Intent(context, MusicPlayer.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                context.startActivity(intent);
                            } else {
                                Log.d(TAG, "context is null");
                            }
                        }

                    });
                } else {
                    access = "denied";
                }

            } catch (IOException e) {
                Log.d(TAG, Objects.requireNonNull(e.getMessage()));
                Log.d(TAG, "Something wrong with connection to server");
                new SweetAlertDialog(contextWeakReference.get(), SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Opsss")
                        .setContentText("We have issue connection to server.Please try later")
                        .show();
            } finally {
                conn.disconnect();

            }
        } catch (Exception e) {
            Log.d(TAG, Objects.requireNonNull(e.getMessage()));

            access = "Network Error";
        }

        return access;
    }

    protected void onPostExecute(String string) {
        Log.d(TAG, "post execute");
        if (string.equals("Network Error")) {
            Log.d(TAG, "Something wrong with connection to server");
            Context context = contextWeakReference.get();
            if (context != null) {
                Log.d(TAG, "context is not null");
                new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Opsss")
                        .setContentText("We have issue connection to server.Please try later")
                        .show();

            } else {
                Log.d(TAG, "context is null");
            }
        }
    }
}

