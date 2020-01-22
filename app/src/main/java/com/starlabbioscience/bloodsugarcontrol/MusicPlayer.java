package com.starlabbioscience.bloodsugarcontrol;

/*
 *****************************************************
 * THIS APPLICATION NAMELY ```BLOOD SUGAR CONTROL```
 * THE PURPOSE AND CONCEPT OF THIS APPLICATION CONSIST
 * OF ```SPOTIFY-LIKE``` AND AUDIO ONLY AVAILABLE ON
 * PARTICULAR DEVICE WITH THE BLUETOOTH UUID MATCHING.
 * **************************************************
 */

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothHeadset;
import android.bluetooth.BluetoothProfile;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.media.AudioDeviceInfo;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.starlabbioscience.bloodsugarcontrol.model.UserModel;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;

import cn.pedant.SweetAlert.SweetAlertDialog;
import io.realm.Realm;
//import java.util.UUID;
/**
 * @AUTHOR : JOHN MELODY ME || HAFIZAN ||
 * @COPYRIGHT : BRAINTECH SDN BHD || STARLABS BIOSCIENCE SDN BHD
 * @PROJECT: BLOOD SUGAR CONTROL
 * @DATE_STARTED: 2 JANUARY 2020
 */

public class MusicPlayer extends AppCompatActivity {
    private static final String TAG = MusicPlayer.class.getName();
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            Log.d(TAG, "action " + action);

            if (Objects.requireNonNull(action).equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                }
            }
        }
    };
    // constant .Shouldn't be changed
    final private String authorizedDevice = "NST-BSC";
    // UI- DECLARE:
    private Button SETTING;
    private BluetoothAdapter BLUETOOTHADAPTER;
    private BluetoothHeadset BLUETOOTHHEADSET;
    private TextView BLUETOOTHSTATUS;
    //UUID uuid = UUID.fromString("00000000-0000-1000-8000-00805F9B34FB");
    private Set<BluetoothDevice> PAIREDDEVICES;
    private AudioManager audioManager = null;
    private MediaPlayer mediaPlayer;
    private Date currentDate;
    private ImageView lowlowlow;
    private Animation animation;
    private Button HOME, NOTI, HIS, ME;

    // onStartBluetooth HEADSET SETTINGS:
    private final BluetoothProfile.ServiceListener profileListener = new BluetoothProfile.ServiceListener() {
        public void onServiceConnected(int profile, BluetoothProfile proxy) {
            if (profile == BluetoothProfile.HEADSET) {
                BLUETOOTHHEADSET = (BluetoothHeadset) proxy;
            }
        }

        public void onServiceDisconnected(int profile) {
            if (profile == BluetoothProfile.HEADSET) {
                BLUETOOTHHEADSET = null;
            }
        }
    };
    private Context context;

    @Override
    public void onStart() {
        super.onStart();
        onStartBluetooth();
        // mediaPlayer.setVolume(0b0, 0b0);
    }

    private void onStartBluetooth() {
        if (BLUETOOTHADAPTER == null) {
            String Bluetooth, err;
            err = getResources().getString(R.string.bluetooth_err);
            Bluetooth = "BLUETOOTH NOT SUPPORTED IN THIS DEVICE";
            Toast.makeText(context, Bluetooth,
                    Toast.LENGTH_LONG)
                    .show();
            BLUETOOTHSTATUS.setText(err);
            Log.d(TAG, err);
        } else {
            if (!BLUETOOTHADAPTER.isEnabled()) {
                new SweetAlertDialog(MusicPlayer.this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Enable Bluetooth?")
                        .setContentText("Application will not work without bluetooth enabled")
                        .setConfirmText("Yes")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                BLUETOOTHADAPTER.enable();
                                sDialog.dismissWithAnimation();
                            }
                        })
                        .setCancelButton("No", new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                //Magic("Application will not work without bluetooth enabled");
                                new SweetAlertDialog(MusicPlayer.this, SweetAlertDialog.WARNING_TYPE)
                                        .setTitleText("Application will not work without Bluetooth Enabled.")
                                        .setConfirmText("Enable Now")
                                        .setCancelButton("No Thanks", new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sDialog) {
                                                finish();
                                                Magic(" \" Application will not work without \bbluetooth enabled\b \" ");
                                                sDialog.dismissWithAnimation();
                                            }
                                        })
                                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                BLUETOOTHADAPTER.enable();
                                                Log.d(TAG, "Requesting Bluetooth");
                                            }
                                        })
                                        .show();
                                sDialog.dismissWithAnimation();
                            }
                        })
                        .show();
            } else {
                Log.d(TAG, "BLUETOOTH REQUEST FAILED");
            }
        }
    }

    private void Magic(String s) {
        Toast.makeText(MusicPlayer.this, s,
                Toast.LENGTH_SHORT)
                .show();
    }

    private void INIT_DECLARATION() {
        mediaPlayer = MediaPlayer.create(MusicPlayer.this, R.raw.sample);
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        //  mediaPlayer.setVolume(0b0, 0b0);
        mediaPlayer.setLooping(true);

        BLUETOOTHADAPTER = BluetoothAdapter.getDefaultAdapter();
        PAIREDDEVICES = BLUETOOTHADAPTER.getBondedDevices();

        lowlowlow = findViewById(R.id.lowlowlow);
        animation = AnimationUtils.loadAnimation(MusicPlayer.this, R.anim.rowtate);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = MusicPlayer.this;
        // force portrait
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        currentDate = new Date();
        Realm.init(context);
        audioManager = (AudioManager) context.getSystemService(AUDIO_SERVICE);

        // disabled the speaker
        Objects.requireNonNull(audioManager).setSpeakerphoneOn(false);

        audioManager.setBluetoothScoOn(true);
        // depreciated
        //audioManager.setBluetoothA2dpOn(true);
        // there is an issue when you off  bluetooth it will divert the song to speaker even thou we have default speaker off
        IntentFilter filter = new IntentFilter(BluetoothAdapter.ACTION_CONNECTION_STATE_CHANGED);
        registerReceiver(mReceiver, filter);
        INIT_DECLARATION();

        final int TIME, FF, RR;
        TIME = mediaPlayer.getCurrentPosition();
        FF = 0b1001110001000;
        RR = 0b1001110001000;

        //@TODO: merge
        ///////////////////////////////////////NAV//////////////////////////

        // @TODO : LONG CLICKED PRESSED :
        HOME = findViewById(R.id.Home);
        NOTI = findViewById(R.id.Notification);
        HIS = findViewById(R.id.History);
        ME = findViewById(R.id.Profile);

        HOME.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent HOME;
                HOME = new Intent(MusicPlayer.this, MusicPlayer.class);
                startActivity(HOME);
            }
        });

        NOTI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent TOWEBSITE = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://www.facebook.com/StarLabsBioscience"));
                startActivity(TOWEBSITE);
            }
        });

        HIS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent HISTORY;
                HISTORY = new Intent(MusicPlayer.this, CardTopUpHistory.class);
                startActivity(HISTORY);
            }
        });

        ME.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ME;
                ME = new Intent(MusicPlayer.this, Profile.class);
                startActivity(ME);
            }
        });


        // Establish connection to the proxy.
        if (BLUETOOTHADAPTER != null) {

        BLUETOOTHADAPTER.getProfileProxy(MusicPlayer.this, profileListener, BluetoothProfile.HEADSET);
        //BLUETOOTHADAPTER.closeProfileProxy(BLUETOOTHHEADSET);
        if (PAIREDDEVICES.size() > 0b0) {
            for (BluetoothDevice BLUETOOTHDEVICE : PAIREDDEVICES) {
                if (BLUETOOTHDEVICE.getName().equals(this.authorizedDevice)) {
                    break;
                }
            }
        }
  } else {
            Log.d(TAG, "make sure you use real device to test bluetooth");
        }


        findViewById(R.id.PLAY_BUTTON).setOnClickListener(v -> {

            // force the speaker to false if quit
            Objects.requireNonNull(audioManager).setSpeakerphoneOn(false);

            AudioDeviceInfo[] audioManagerDevices = audioManager.getDevices(AudioManager.GET_DEVICES_OUTPUTS);
            boolean playSound = false;
            String errorMessage = null;
            for (AudioDeviceInfo audioDeviceInfo : audioManagerDevices) {
                // let see here which can be re used for play button
                Log.d(TAG + "product name", audioDeviceInfo.getProductName().toString());
                Log.d(TAG + "product id ", String.valueOf(audioDeviceInfo.getId()));
                // okay let block other

                if (audioDeviceInfo.getProductName().toString().equals(authorizedDevice)) {

                    // check must valid date and status must be one .
                    try (Realm realm = Realm.getDefaultInstance()) {
                        UserModel userModel = realm.where(UserModel.class).findFirst();
                        if (userModel != null) {

                                // current date
                            Locale locale = new Locale( "ms" , "MY" );

                            SimpleDateFormat inSDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",locale);
                            Log.d(TAG,"user model"+userModel.toString());
                            try {
                                Date date = inSDF.parse(userModel.getValid_date());
                                if(userModel.getStatus().equals("0")){
                                    Log.d(TAG, "Not Authorized user "+userModel.getFirst_name()+" "+userModel.getToken());

                                    errorMessage = getString(R.string.pleaseCallAdmin);
                                }else{
                                    Log.d(TAG,"access granted for the user ");
                                }
                                if(date != null) {
                                    if (date.before(currentDate)) {
                                        errorMessage = "Valid date lesser then current date " + userModel.getValid_date().toString();
                                        Log.d(TAG, errorMessage);


                                    }
                                }

                                if(currentDate.before(date) && userModel.getStatus().equals("1") || userModel.getStatus().equals("true")){
                                    if (mediaPlayer == null) {
                                        try {
                                            mediaPlayer = MediaPlayer.create(MusicPlayer.this, R.raw.sample);
                                            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

                                            mediaPlayer.setVolume(0b0, 0b0);
                                            mediaPlayer.setLooping(true);
                                        } catch (Exception e) {
                                            Log.d(TAG, Objects.requireNonNull(e.getMessage()));
                                        }
                                    }
                                    if (mediaPlayer.isPlaying()) {
                                        mediaPlayer.setVolume(0b0,0b0);
                                        mediaPlayer.pause();
                                        findViewById(R.id.PLAY_BUTTON).setBackgroundResource(R.drawable.ic_play_circle_filled_black_24dp);
                                        Toast.makeText(context, getString(R.string.pause), Toast.LENGTH_SHORT).show();
                                    } else {
                                        mediaPlayer.setVolume(0b1010000, 0b1010000);
                                        mediaPlayer.start();
                                        findViewById(R.id.PLAY_BUTTON).setBackgroundResource(R.drawable.pausebutton);
                                        Toast.makeText(context, getString(R.string.play), Toast.LENGTH_SHORT).show();
                                        lowlowlow.startAnimation(animation);
                                    }
                                    playSound = true;
                                    // seem got double the same name in android. weird.
                                    break;
                                }else{
                                    Log.d(TAG,"weird something date");
                                }
                            } catch (final Exception e) {
                                Log.d(TAG, Objects.requireNonNull(e.getMessage()));
                            }
                        }
                    }

                }
            }
            if (!playSound) {
                if (errorMessage == null) {
                    errorMessage = getString(R.string.bluetoothMissing);
                }
                new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Oops...")
                        .setContentText(errorMessage)
                        .show();
            }

        });


        findViewById(R.id.previous).setOnClickListener(v -> {
            AudioDeviceInfo[] audioManagerDevices = audioManager.getDevices(AudioManager.GET_DEVICES_OUTPUTS);
            boolean playSound = false;
            for (AudioDeviceInfo audioDeviceInfo : audioManagerDevices) {
                // let see here which can be re used for play button
                Log.d(TAG + "product name", audioDeviceInfo.getProductName().toString());
                Log.d(TAG + "product id ", String.valueOf(audioDeviceInfo.getId()));
                // okay let block other

                if (audioDeviceInfo.getProductName().toString().equals(authorizedDevice)) {
                    if (TIME + RR >= 0b0) {
                        mediaPlayer.seekTo(TIME - RR);
                        Log.d(TAG, "MUSIC rewindButton -5 SECONDS");
                    } else {
                        Log.d(TAG, "LOG: rewindButton FAILED");
                    }
                    playSound = true;
                    break;
                }
            }
            if (playSound) {
                Toast.makeText(context, getString(R.string.Rewind), Toast.LENGTH_SHORT).show();
            }
        });


        findViewById(R.id.forward).setOnClickListener(v -> {
            AudioDeviceInfo[] audioManagerDevices = audioManager.getDevices(AudioManager.GET_DEVICES_OUTPUTS);
            boolean playSound = false;
            for (AudioDeviceInfo audioDeviceInfo : audioManagerDevices) {
                // let see here which can be re used for play button
                Log.d(TAG + "product name", audioDeviceInfo.getProductName().toString());
                Log.d(TAG + "product id ", String.valueOf(audioDeviceInfo.getId()));
                // okay let block other

                if (audioDeviceInfo.getProductName().toString().equals(authorizedDevice)) {
                    if (TIME + FF <= mediaPlayer.getDuration()) {
                        mediaPlayer.seekTo(TIME + FF);
                        Log.d(TAG, "MUSIC forwardButton +5 SECONDS");
                    } else {
                        Log.d(TAG, "LOG: forwardButton FAILED");
                    }
                    playSound = true;
                    break;
                }
            }
            if (playSound) {
                String Forward;
                Forward = getResources()
                        .getString(R.string.forward);

                Toast.makeText(context, Forward,
                        Toast.LENGTH_SHORT)
                        .show();
            }
        });

        findViewById(R.id.PLAY_BUTTON).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                // force the speaker to false if quit
                Objects.requireNonNull(audioManager).setSpeakerphoneOn(false);

                AudioDeviceInfo[] audioManagerDevices = audioManager.getDevices(AudioManager.GET_DEVICES_OUTPUTS);
                boolean playSound = false;
                for (AudioDeviceInfo audioDeviceInfo : audioManagerDevices) {
                    // let see here which can be re used for play button
                    Log.d(TAG + "product name", audioDeviceInfo.getProductName().toString());
                    Log.d(TAG + "product id ", String.valueOf(audioDeviceInfo.getId()));
                    // okay let block other

                    if (audioDeviceInfo.getProductName().toString().equals(authorizedDevice)) {


                        if (mediaPlayer.isPlaying()) {
                            //mediaPlayer.setVolume(0b0,0b0);
                            findViewById(R.id.PLAY_BUTTON).setBackgroundResource(R.drawable.ic_play_circle_filled_black_24dp);


                            mediaPlayer.pause();
                            // not sure why using pause
                            mediaPlayer.stop();
                            mediaPlayer.release();
                            mediaPlayer = null;
                        }
                        playSound = true;

                        break;
                    }
                }
                if (!playSound) {
                    new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Oops...")
                            .setContentText(getString(R.string.bluetoothMissing))
                            .show();

                } else {

                    String stop;
                    stop = getResources().getString(R.string.stop);

                    Toast.makeText(context, stop
                            , Toast.LENGTH_SHORT)
                            .show();
                    return false;
                }
                return false;
            }
        });
    }

    /*
    // ON CLICK FUNCTION::
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.profile:
                Intent TOPROFILE;
                TOPROFILE = new Intent(MusicPlayer.this, Profile.class);
                startActivity(TOPROFILE);
                break;

            case R.id.reload:
                Intent TORELOAD;
                TORELOAD = new Intent(MusicPlayer.this, CardTopUp.class);
                startActivity(TORELOAD);
                break;

            case R.id.news:
                Intent toFacebookNewsPage = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://web.facebook.com/StarLabsBioscience?_rdc=1&_rdwr"));
                startActivity(toFacebookNewsPage);
                break;

            case R.id.fb:
                Intent TOFACEBOOKPAGE = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://web.facebook.com/StarLabsBioscience?_rdc=1&_rdr"));
                startActivity(TOFACEBOOKPAGE);
                break;

            case R.id.web:
                Intent TOWEBSITE = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://www.starlabs.com.my/"));
                startActivity(TOWEBSITE);
                break;

            case R.id.ws:
                Intent TOWHATSAPP = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://wa.me/6013-9319018?text=[This%20is%20automated%20Message].%20I%20Would%20like%20to%20know%20more.%20"));
                startActivity(TOWHATSAPP);
        }
        */
   @Override
    protected void onDestroy() {
        super.onDestroy();
        mediaPlayer.release();
        // any run handler clear up ..
    }
}
