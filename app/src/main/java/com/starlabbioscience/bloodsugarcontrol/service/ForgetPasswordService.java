package com.starlabbioscience.bloodsugarcontrol.service;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Looper;
import android.util.Log;
import android.widget.Button;

import com.google.gson.Gson;
import com.starlabbioscience.bloodsugarcontrol.R;
import com.starlabbioscience.bloodsugarcontrol.model.ForgetXhrModel;

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

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;

import cn.pedant.SweetAlert.SweetAlertDialog;

class ForgetPasswordService  extends AsyncTask<String, String, String> {
    private static final String TAG = LoginService.class.getName();
    private final WeakReference<Context> contextWeakReference;
    private final String URL;
    private final Map<String, String> URL_DATA;


    public ForgetPasswordService(Context context) {
        // put your URL here
        URL = "https://starlabsbioscience.co.uk/bloodsugar/api/authentication/forget";
        String ERROR_MESSAGE = "Access Denied";
        // parameter in  doInBackground
        URL_DATA = new HashMap<>();
        // assign context but in weak reference because of memory leak warning
        contextWeakReference = new WeakReference<>(context);


    }

    protected String doInBackground(String... strings) {
        // put your variable array/parameter/data here
        URL_DATA.put("email", strings[0]);

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

                final ForgetXhrModel forgetXhrModel = new Gson().fromJson(result.toString(), ForgetXhrModel.class);

                // Recheck sync model
                Log.d(TAG, forgetXhrModel.toString());



            } catch (IOException e) {
                Log.d(TAG, Objects.requireNonNull(e.getMessage()));
                Log.d(TAG, "Something wrong with connection to server");
                new SweetAlertDialog(contextWeakReference.get(), SweetAlertDialog.ERROR_TYPE)
                        .setTitleText(contextWeakReference.get().getString(R.string.warning))
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
                        .setTitleText(context.getString(R.string.warning))
                        .setContentText("We have issue connection to server.Please try later")
                        .show();
                Button button = ((Activity) context).findViewById(R.id.loginButton);

                button.setClickable(true);
            } else {
                Log.d(TAG, "context is null");
            }
        }
    }
}
