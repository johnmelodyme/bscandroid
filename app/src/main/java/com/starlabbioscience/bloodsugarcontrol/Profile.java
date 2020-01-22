package com.starlabbioscience.bloodsugarcontrol;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.starlabbioscience.bloodsugarcontrol.model.UserModel;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import io.realm.Realm;

/**
 * @AUTHOR : JOHN MELODY ME || HAFIZAN ||
 * @COPYRIGHT : BRAINTECH SDN BHD || STARLABS BIOSCIENCE SDN BHD
 * @PROJECT: BLOOD SUGAR CONTROL
 */
public class Profile extends AppCompatActivity {
    private static final String TAG = Profile.class.getName();

    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        // force portrait
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        context = Profile.this;

        // Create own title here
        // opens "starlabs.realm"
        Realm.init(context);
        try (Realm realm = Realm.getDefaultInstance()) {
            UserModel userModel = realm.where(UserModel.class).findFirst();
            if (userModel != null) {
                // let see what it store
                Log.d(TAG, userModel.toString());
                TextView textView = findViewById(R.id.profileName);
                textView.setText(userModel.getFirst_name());

                TextView profileLastDate = findViewById(R.id.profileLastDate);
                Locale locale = new Locale("ms", "MY");

                SimpleDateFormat outSDF = new SimpleDateFormat("dd-MM-yyyy", locale);
                SimpleDateFormat inSDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", locale);
                try {
                    Date date = inSDF.parse(userModel.getValid_date());
                    profileLastDate.setText(outSDF.format(Objects.requireNonNull(date)));
                } catch (final Exception e) {
                    Log.d(TAG, Objects.requireNonNull(e.getMessage()));
                }

            }
        }

        setButtonClickEvent();

    }

    private void setButtonClickEvent() {
        Log.d(TAG, "Button event ");

        findViewById(R.id.profilePageToMusicPage).setOnClickListener(v -> {
            Intent intent = new Intent(context, MusicPlayer.class);
            startActivity(intent);
        });

        findViewById(R.id.profilePageToPreferencePage).setOnClickListener(v -> {
            Intent intent = new Intent(context, Preference.class);
            startActivity(intent);
        });

        // quick action
        findViewById(R.id.paymentToReloadPage).setOnClickListener(v -> {
            Intent intent = new Intent(context, CardTopUp.class);
            startActivity(intent);
        });

        findViewById(R.id.historyToReloadPage).setOnClickListener(v -> {
            Intent intent = new Intent(context, CardTopUpHistory.class);
            startActivity(intent);
        });

        // social

        findViewById(R.id.profileToFacebookPage).setOnClickListener(v -> {
            Intent TOFACEBOOKPAGE = new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://web.facebook.com/StarLabsBioscience?_rdc=1&_rdr"));
            startActivity(TOFACEBOOKPAGE);
        });

        findViewById(R.id.profileToWebsite).setOnClickListener(v -> {
            Intent web = new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://www.starlabs.com.my/"));
            startActivity(web);
        });

        findViewById(R.id.profileToWhatsapp).setOnClickListener(v -> {
            Intent TOWHATSAPP = new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://wa.me/6013-9319018?text=[This%20is%20automated%20Message].%20I%20Would%20like%20to%20know%20more.%20"));
            startActivity(TOWHATSAPP);
        });

    }
}
