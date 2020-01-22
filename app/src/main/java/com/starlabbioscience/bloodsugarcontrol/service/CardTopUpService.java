package com.starlabbioscience.bloodsugarcontrol.service;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Looper;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatEditText;

import com.google.gson.Gson;
import com.starlabbioscience.bloodsugarcontrol.CardTopUpHistory;
import com.starlabbioscience.bloodsugarcontrol.R;
import com.starlabbioscience.bloodsugarcontrol.model.CardTopUpXhrModel;
import com.starlabbioscience.bloodsugarcontrol.model.CardTopUpXhrWrongModel;
import com.starlabbioscience.bloodsugarcontrol.model.UserModel;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
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

public class CardTopUpService extends AsyncTask<String, String, String> {
    private static final String TAG = CardTopUpService.class.getName();
    private final String URL;
    private final Map<String, String> URL_DATA;

    private final WeakReference<Context> contextWeakReference;

    public CardTopUpService(Context context) {
        // put your URL here
        URL = "https://starlabsbioscience.co.uk/bloodsugar/api/Topup/topup";
        // parameter in  doInBackground
        URL_DATA = new HashMap<>();
        // assign context but in weak reference because of memory leak warning
        contextWeakReference = new WeakReference<>(context);
    }


    protected String doInBackground(String... strings) {
        // put your variable array/parameter/data here
        URL_DATA.put("card_number", strings[0]);
        URL_DATA.put("token", strings[1]);

        StringBuilder stringBuilder = new StringBuilder();
        for (Map.Entry<String, String> entry : URL_DATA.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            stringBuilder.append(key).append("=").append(value).append("&");
        }
        // remove the end line &
        String parameter = stringBuilder.deleteCharAt(stringBuilder.lastIndexOf("&")).toString();

        String access = "granted";
        try {
            Looper.prepare();

            SSLContext sslcontext = SSLContext.getInstance("TLS");
            sslcontext.init(null, null, null);
            SSLSocketFactory sslSocketFactory = sslcontext.getSocketFactory();

            URL urlObj = new URL(URL);
            final HttpsURLConnection httpsURLConnection = (HttpsURLConnection) urlObj.openConnection();
            httpsURLConnection.setDoOutput(true);
            httpsURLConnection.setRequestMethod("POST");
            httpsURLConnection.setRequestProperty("Accept-Charset", "UTF-8");

            httpsURLConnection.setReadTimeout(10000);
            httpsURLConnection.setConnectTimeout(15000);
            httpsURLConnection.setSSLSocketFactory(sslSocketFactory);
            httpsURLConnection.connect();

            DataOutputStream dataOutputStream = new DataOutputStream(httpsURLConnection.getOutputStream());
            // bind the parameter to stream
            Log.d(TAG, "parameter" + parameter);
            dataOutputStream.writeBytes(parameter);

            dataOutputStream.flush();
            dataOutputStream.close();


            try {
                InputStream in = new BufferedInputStream(httpsURLConnection.getInputStream());
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                StringBuilder result = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }

                // check server response
                Log.d(TAG, "result from server: " + result.toString());
                String string = "false";

                if (result.toString().contains(string)) {
                    // diff model
                    CardTopUpXhrWrongModel cardTopUpXhrWrongModel = new Gson().fromJson(result.toString(), CardTopUpXhrWrongModel.class);
                    Log.d(TAG, cardTopUpXhrWrongModel.toString());

                    // nothing be done..
                    access = cardTopUpXhrWrongModel.getMessage();
                    Log.d(TAG, "Server message : " + cardTopUpXhrWrongModel.getMessage());

                } else {
                    final CardTopUpXhrModel cardTopUpXhrModel = new Gson().fromJson(result.toString(), CardTopUpXhrModel.class);
                    Log.d(TAG, " here " + cardTopUpXhrModel.toString());

                    // check the local db if existed with that id if not then insert
                    Realm.init(contextWeakReference.get());

                    final Realm realm = Realm.getDefaultInstance();
                    if (cardTopUpXhrModel.getData().getStatus().equals("1")) {
                        realm.executeTransaction(new Realm.Transaction() {
                            @Override
                            @ParametersAreNullableByDefault
                            public void execute(Realm realm1) {
                                UserModel userModel = realm.where(UserModel.class).findFirst();
                                if (userModel != null) {
                                    userModel.setValid_date(cardTopUpXhrModel.getData().getValid_date());
                                }
                            }
                        });

                    }
                    // later update realm
                }

            } catch (Exception e) {
                Log.d(TAG, Objects.requireNonNull(e.getMessage()));
            } finally {
                httpsURLConnection.disconnect();

            }
        } catch (
                Exception e) {
            Log.d(TAG, Objects.requireNonNull(e.getMessage()));
        }

        return access;
    }

    protected void onPostExecute(String string) {

        // sweet alert respond if success  true  redirect intent

        Context context = contextWeakReference.get();
        if (context != null) {
            Log.d(TAG, "context is not null");
            if (string.equals("granted")) {
                // later changer.. redirect to history purchase
                Intent intent = new Intent(context, CardTopUpHistory.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            } else {
                new SweetAlertDialog(context)
                        .setTitleText(string).show();
                // reset the edit text view
                AppCompatEditText appCompatEditText = ((Activity) context).findViewById(R.id.cardTopUpReloadNumber);
                appCompatEditText.setText("");

                TextView textView = ((Activity) context).findViewById(R.id.txtOk);
                String ok = context.getString(R.string.ok);
                textView.setText(ok);

            }
        } else {
            Log.d(TAG, "context is null");
        }

    }
}
