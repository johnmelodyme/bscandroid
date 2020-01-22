package com.starlabbioscience.bloodsugarcontrol.service;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.starlabbioscience.bloodsugarcontrol.R;
import com.starlabbioscience.bloodsugarcontrol.adapter.CardTopUpHistoryAdapter;
import com.starlabbioscience.bloodsugarcontrol.model.CardTopUpHistoryRespondModel;
import com.starlabbioscience.bloodsugarcontrol.model.CardTopUpHistoryXhrModel;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;

public class CardTopUpHistoryService extends AsyncTask<String, Void, List<CardTopUpHistoryRespondModel>> {
    private static final String TAG = CardTopUpHistoryService.class.getName();
    private final String URL;
    private final Map<String, String> URL_DATA;

    private final CardTopUpHistoryAdapter CardTopUpHistoryAdapter;
    private final WeakReference<Context> contextWeakReference;

    public CardTopUpHistoryService(CardTopUpHistoryAdapter CardTopUpHistoryAdapter1, Context context) {
        // put your URL here
        URL = "https://starlabsbioscience.co.uk/bloodsugar/api/Topup/history";
        // parameter in  doInBackground
        URL_DATA = new HashMap<>();

        CardTopUpHistoryAdapter = CardTopUpHistoryAdapter1;
        contextWeakReference = new WeakReference<>(context);

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        Log.d(TAG, "Downloading CardTopUp history");
    }

    @Override
    protected List<CardTopUpHistoryRespondModel> doInBackground(String... strings) {

        // put your variable array/parameter/data here

        // token key
        URL_DATA.put("token", strings[0]);

        StringBuilder stringBuilder = new StringBuilder();
        for (Map.Entry<String, String> entry : URL_DATA.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            stringBuilder.append(key).append("=").append(value).append("&");
        }
        // remove the end line &
        //String parameter = stringBuilder.deleteCharAt(stringBuilder.lastIndexOf("&")).toString();

        List<CardTopUpHistoryRespondModel> cardTopUpHistoryRespondModel = new ArrayList<>();
        try {
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
            String string = "token=" + strings[0];
            httpsURLConnection.getOutputStream().write(string.getBytes(StandardCharsets.UTF_8));

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

                final CardTopUpHistoryXhrModel CardTopUpHistoryXhrModel = new Gson().fromJson(result.toString(), CardTopUpHistoryXhrModel.class);

                if (CardTopUpHistoryXhrModel.getStatus()) {
                    // Recheck sync model
                    Log.d(TAG, CardTopUpHistoryXhrModel.toString());
                    cardTopUpHistoryRespondModel = CardTopUpHistoryXhrModel.getData();
                } else {
                    Log.d(TAG, "something wrong getting CardTopUp list");
                }


            } catch (Exception e) {
                Log.d(TAG, Objects.requireNonNull(e.getMessage()));
                Log.d(TAG, "ada error1 " + Objects.requireNonNull(e.getMessage()));
            } finally {
                httpsURLConnection.disconnect();

            }
        } catch (Exception e) {
            Log.d(TAG, Objects.requireNonNull(e.getMessage()));
        }
        return cardTopUpHistoryRespondModel;
    }

    @Override
    protected void onPostExecute(List<CardTopUpHistoryRespondModel> cardTopUpHistoryRespondModels) {
        super.onPostExecute(cardTopUpHistoryRespondModels);
        Context context = contextWeakReference.get();
        if (context != null) {
            ProgressBar progressBar = ((Activity) context).findViewById(R.id.cardTopUpHistoryProgressBar);
            progressBar.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.GONE);

        }
        if (cardTopUpHistoryRespondModels.size() > 0) {
            //bind the data to adapter
            CardTopUpHistoryAdapter.execute(cardTopUpHistoryRespondModels);
            CardTopUpHistoryAdapter.notifyDataSetChanged();
            Log.d(TAG, "Assign data to adapter");
        } else {
            // message to end user record not found.sweet android
            Log.d(TAG, "record not found: CardTopUp history");
            // don't leave empty white page..just give some information
            if (context != null) {
                TextView textView = ((Activity) context).findViewById(R.id.emptyRecordReload);
                textView.setVisibility(View.VISIBLE);
            }
        }
    }
}
