package com.starlabbioscience.bloodsugarcontrol;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.starlabbioscience.bloodsugarcontrol.model.UserModel;
import com.starlabbioscience.bloodsugarcontrol.service.LoginService;
import com.starlabbioscience.bloodsugarcontrol.service.TokenService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import cn.pedant.SweetAlert.SweetAlertDialog;
import io.realm.Realm;

/**
 * @AUTHOR : JOHN MELODY ME || HAFIZAN ||
 * @COPYRIGHT : BRAINTECH SDN BHD || STARLABS BIOSCIENCE SDN BHD
 * @PROJECT: BLOOD SUGAR CONTROL
 */
public class Authentication extends AppCompatActivity {
    private static final String TAG = Authentication.class.getName();
    private Context context;
    private Button registerButton;
    private BluetoothAdapter BA;
  @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_outhentication);
        context = Authentication.this;
        BA  = BluetoothAdapter.getDefaultAdapter();
        setSimple();

        registerButton = findViewById(R.id.Register);

        registerButton.setOnClickListener(v ->
                new SweetAlertDialog(Authentication.this, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Under Construction")
                        .show());

    }

    private void setSimple() {


            TextView password = findViewById(R.id.password_edit_test);

            CheckBox checkBox = findViewById(R.id.Showpass);

            checkBox.setOnCheckedChangeListener((button, isChecked) -> {
                if (!isChecked) {
                    //HIDE PASSWORD::
                    password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                } else {
                    // SHOW PASSWORD:
                    password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
            });
        // keyboard when clicked done
        password.setOnEditorActionListener((v, actionId, event) -> {
            Log.d(TAG, "is running on editor action listener");
            setButtonProcessClick();
            return false;
        });
        findViewById(R.id.loginButton).setOnClickListener(v -> {
            setButtonProcessClick();
        });
    }
    private void setDetail() {
        Button button = findViewById(R.id.loginButton);
        button.setClickable(false);

        Realm.init(Authentication.this);
        try (Realm realm = Realm.getDefaultInstance()) {
            UserModel userModel = realm.where(UserModel.class).findFirst();
            if (userModel != null) {
                // check if got internet access ?
                // check valid date token . if valid token is good no need to request server
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                try {
                    // what if token not existed . Weird but true
                    if (userModel.getTokenValidDate() != null) {
                        if (Objects.requireNonNull(simpleDateFormat.parse(userModel.getTokenValidDate())).compareTo(simpleDateFormat.parse(simpleDateFormat.format(new Date()))) < 0) {
                            ConnectivityManager conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                            assert conMgr != null;
                            NetworkInfo netInfo = conMgr.getActiveNetworkInfo();
                            if (netInfo == null) {
                                Log.d(TAG, "We have connection issue to server");
                                new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                                        .setTitleText(getString(R.string.warning))
                                        .setContentText("We have issue connection to server.Please try later")
                                        .show();
                            } else {
                                TokenService tokenService = new TokenService(context);
                                tokenService.execute();
                            }

                        } else {
                            Log.d(TAG, "date token valid");
                            Intent intent = new Intent(Authentication.this, MusicPlayer.class);
                            startActivity(intent);
                        }
                    } else {
                        // request new one ..
                        TokenService tokenService = new TokenService(context);
                        tokenService.execute();
                    }
                } catch (ParseException exception) {
                    Log.e(TAG, Objects.requireNonNull(exception.getMessage()));
                }
            } else {
                // Login Button Config:
                EditText password = findViewById(R.id.password_edit_test);

                CheckBox checkBox = findViewById(R.id.Showpass);
                checkBox.setOnCheckedChangeListener((button1, isChecked) -> {
                    if (!isChecked) {
                        // hide password
                        password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    } else {
                        // show password
                        password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    }
                });

                // keyboard when clicked done
                password.setOnEditorActionListener((v, actionId, event) -> {
                    setButtonProcessClick();
                    return false;
                });
                findViewById(R.id.loginButton).setOnClickListener(v -> setButtonProcessClick());

            }

        }

    }

    private void setButtonProcessClick() {
        Button loginButton = findViewById(R.id.loginButton);

        loginButton.setClickable(false);
        loginButton.setText(getString(R.string.loadingText));

        TextView email = findViewById(R.id.email_edit_text);
        TextView password = findViewById(R.id.password_edit_test);
        String Email = email.getText().toString();
        String Password = password.getText().toString();
        /**

         String message;
         boolean success = true;

         if (TextUtils.isEmpty(Email)) {
             message = "PLEASE ENTER YOUR email";
             email.setError(message);
             Log.d(TAG, message);
             success = false;
         } else {
             Log.d(TAG, "$USER email ENTERED");
         }

         if (TextUtils.isEmpty(Password)) {
             message = "PLEASE ENTER YOUR password";
             password.setError(message);
             Log.d(TAG, message);
             success = false;
         } else {
             Log.d(TAG, "$USER password ENTERED");
         }

         if (Password.length() <= 0b110) {
             message = "PLEASE ENTER A STRONG password (MUST BE AT LEAST 6 LETTERS OR NUMBERS) ";
             password.setError(message);
             Log.d(TAG, message);
             success = false;
         } else {
             Log.d(TAG, "$USER password IS STRONG");
         }
         if (success) {
             LoginService loginService = new LoginService(context);
             loginService.execute(Email, Password);
         }
         ***/
        LoginService loginService = new LoginService(context);
        loginService.execute(Email, Password);

            // temp by pass

            //}

    }
}
