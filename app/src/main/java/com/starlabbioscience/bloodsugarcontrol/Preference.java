package com.starlabbioscience.bloodsugarcontrol;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;

/**
 * @AUTHOR : JOHN MELODY ME || HAFIZAN ||
 * @COPYRIGHT : BRAINTECH SDN BHD || STARLABS BIOSCIENCE SDN BHD
 * @PROJECT: BLOOD SUGAR CONTROL
 */

public class Preference extends AppCompatActivity {

    private static final String TAG = Preference.class.getName();
    private Context context;
    private ListView listView;
    private final String[] SETTINGS;

    {
        SETTINGS = new String[]{"Bluetooth Setting", "Report Error", "About Us"};
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preference);

        context = Preference.this;
        // force portrait
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        listView = findViewById(R.id.settinglistview);
        ArrayAdapter arrayAdapter = new ArrayAdapter<>(context,
                R.layout.support_simple_spinner_dropdown_item, SETTINGS);
        listView.setAdapter(arrayAdapter);
        ON_CLICK_LIST_ITEM();
    }

    private void ON_CLICK_LIST_ITEM() {
        listView.setOnItemClickListener((parent, view, position, id) -> {
            String CLICKED;
            CLICKED = (String) parent.getItemAtPosition(position);
            switch (CLICKED) {
                case "Report Error":
                    EMAIL_TO_DEVELOPER();
                    break;
                case "About Us":
                    Intent web;
                    web = new Intent(Intent.ACTION_VIEW,
                            Uri.parse("https://www.starlabs.com.my/"));
                    startActivity(web);
                    break;
                case "Bluetooth Setting":
                    Intent openDeviceBluetoothSetting;
                    openDeviceBluetoothSetting = new Intent();
                    openDeviceBluetoothSetting.setAction(android.provider.Settings.ACTION_BLUETOOTH_SETTINGS);
                    startActivity(openDeviceBluetoothSetting);
                    for (int BSC = 0b0; BSC < 0b1010; BSC++) {
                        String connectNstBsc;
                        connectNstBsc = getResources().getString(R.string.bluetooth_connect);
                        Toast.makeText(context, connectNstBsc,
                                Toast.LENGTH_SHORT)
                                .show();
                    }
                    break;
                default:
                    System.out.println("--");
                    break;
            }
        });
    }

    private void EMAIL_TO_DEVELOPER() {
        Intent emailTODeveloper = new Intent(Intent.ACTION_SEND);
        emailTODeveloper.setType("text/plain");
        emailTODeveloper.putExtra(Intent.EXTRA_EMAIL, new String[]
                {"john@braintechgroup.com"});
        emailTODeveloper.putExtra(Intent.EXTRA_CC, new String[]
                {"ang@braintechgroup.com"});
        emailTODeveloper.putExtra(Intent.EXTRA_SUBJECT,
                " Hey Developer, I have some suggestion and Issue!");
        emailTODeveloper.putExtra(Intent.EXTRA_TEXT,
                "Hey Developer, I have some suggestion and Issue!");
        try {
            startActivity(Intent.createChooser(emailTODeveloper, "Pick a Email Platform: "));
        } catch (Exception exception) {
            Log.d(TAG, Objects.requireNonNull(exception.getMessage()));
        }
    }
}
