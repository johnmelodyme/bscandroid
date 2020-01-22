package com.starlabbioscience.bloodsugarcontrol;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.crashlytics.android.Crashlytics;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.security.ProviderInstaller;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;

import javax.net.ssl.SSLContext;

import io.fabric.sdk.android.Fabric;
import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * @AUTHOR : JOHN MELODY ME || HAFIZAN ||
 * @COPYRIGHT : BRAINTECH SDN BHD || STARLABS BIOSCIENCE SDN BHD
 * @PROJECT: BLOOD SUGAR CONTROL
 */
public class SplashScreen extends AppCompatActivity {
    private static final String TAG = Authentication.class.getName();
    Handler handler;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Fabric.with(this, new Crashlytics());
        context = SplashScreen.this;
        // layout
        setContentView(R.layout.activity_splash_screen);

        // force portrait
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        // local database
        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder().name("starlabs.realm").build();
        Realm.setDefaultConfiguration(config);

        // splash screen
        handler = new Handler();
        handler.postDelayed(() -> {
            Intent TOAUTHENTICATION;
            TOAUTHENTICATION = new Intent(context, Authentication.class);
            startActivity(TOAUTHENTICATION);
        }, 5010);
        setUpdateSsl();
    }

    /**
     * To force update ssl shake hand bugs
     *
     * @apiNote SL handshake aborted: ssl=0x742352a540: I/O error during system call, Connection reset by peer
     */
    private void setUpdateSsl() {
        try {
            ProviderInstaller.installIfNeeded(getApplicationContext());
            SSLContext sslContext;
            sslContext = SSLContext.getInstance("TLSv1.2");
            sslContext.init(null, null, null);
            sslContext.createSSLEngine();
        } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException
                | NoSuchAlgorithmException | KeyManagementException e) {
            //e.printStackTrace();
            Log.d(TAG, Objects.requireNonNull(e.getMessage()));

        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }
}
