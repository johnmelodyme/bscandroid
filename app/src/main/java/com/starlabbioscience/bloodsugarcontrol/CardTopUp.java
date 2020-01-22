package com.starlabbioscience.bloodsugarcontrol;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;

import com.starlabbioscience.bloodsugarcontrol.model.UserModel;
import com.starlabbioscience.bloodsugarcontrol.service.CardTopUpService;

import java.util.Objects;
import java.util.Optional;

import cn.pedant.SweetAlert.SweetAlertDialog;
import io.realm.Realm;

/**
 * @AUTHOR : JOHN MELODY ME || HAFIZAN ||
 * @COPYRIGHT : BRAINTECH SDN BHD || STARLABS BIOSCIENCE SDN BHD
 * @PROJECT: BLOOD SUGAR CONTROL
 */
public class CardTopUp extends AppCompatActivity {
    private static final String TAG = CardTopUp.class.getName();
    private Context context;
    private int totalTopUpCharacter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reload);
        // force portrait
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        context = CardTopUp.this;
        // put your variable value here
        totalTopUpCharacter = 14;

        Realm.init(context);

        setReloadToolbar();
        setKeyInCardNumber();
        setSubmitTopUp();
    }

    private void setReloadToolbar() {

        // take the main link id
        findViewById(R.id.reloadPageToMusicPage).setOnClickListener(v -> {
            Intent intent = new Intent(context, MusicPlayer.class);
            startActivity(intent);
            Log.d(TAG, "wong intent");

        });

        findViewById(R.id.reloadPageToReloadHistoryPageButton).setOnClickListener(v -> {
            Log.d(TAG, "err why execute diff");
            Intent intent = new Intent(context, CardTopUpHistory.class);
            startActivity(intent);
        });

    }

    private void setKeyInCardNumber() {

        AppCompatEditText cardTopUpReloadNumber = findViewById(R.id.cardTopUpReloadNumber);

        cardTopUpReloadNumber.setOnEditorActionListener((v, actionId, event) -> {
            Log.d(TAG, "test");
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                setButtonTopUpProcessClick();
            }

            return false;
        });

        cardTopUpReloadNumber.setOnFocusChangeListener((view, hasFocus) -> {
            if (hasFocus) {
                cardTopUpReloadNumber.setHint(getString(R.string.topUpNumberPlaceHolder));
            } else {
                cardTopUpReloadNumber.setHint("");
            }
        });
        // get the length and put to end user
        cardTopUpReloadNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    int charLength = 0;
                    if (cardTopUpReloadNumber.getText() != null) {
                        charLength = cardTopUpReloadNumber.getText().length();
                    }
                    String rightHelperText = charLength + "/" + totalTopUpCharacter;
                    Log.d(TAG, "Total : " + rightHelperText);
                    TextView textView = findViewById(R.id.totalPinDigit);
                    textView.setText(rightHelperText);

                } catch (Exception exception) {
                    Log.d(TAG, "see if any error exception");
                }
            }
        });
        findViewById(R.id.txtOne).setOnClickListener(v -> {
            String concat = cardTopUpReloadNumber.getText() + String.valueOf(1);
            cardTopUpReloadNumber.setText(concat);
        });

        findViewById(R.id.txtTwo).setOnClickListener(v -> {
            String concat = cardTopUpReloadNumber.getText() + String.valueOf(2);
            cardTopUpReloadNumber.setText(concat);
        });

        findViewById(R.id.txtThree).setOnClickListener(v -> {
            String concat = cardTopUpReloadNumber.getText() + String.valueOf(3);
            cardTopUpReloadNumber.setText(concat);
        });

        findViewById(R.id.txtFour).setOnClickListener(v -> {
            String concat = cardTopUpReloadNumber.getText() + String.valueOf(4);
            cardTopUpReloadNumber.setText(concat);
        });

        findViewById(R.id.txtFive).setOnClickListener(v -> {
            String concat = cardTopUpReloadNumber.getText() + String.valueOf(5);
            cardTopUpReloadNumber.setText(concat);
        });

        findViewById(R.id.txtSix).setOnClickListener(v -> {
            String concat = cardTopUpReloadNumber.getText() + String.valueOf(6);
            cardTopUpReloadNumber.setText(concat);
        });

        findViewById(R.id.txtSeven).setOnClickListener(v -> {
            String concat = cardTopUpReloadNumber.getText() + String.valueOf(7);
            cardTopUpReloadNumber.setText(concat);
        });

        findViewById(R.id.txtEight).setOnClickListener(v -> {
            String concat = cardTopUpReloadNumber.getText() + String.valueOf(8);
            cardTopUpReloadNumber.setText(concat);
        });

        findViewById(R.id.txtNine).setOnClickListener(v -> {
            String concat = cardTopUpReloadNumber.getText() + String.valueOf(9);
            cardTopUpReloadNumber.setText(concat);
        });

        findViewById(R.id.txtZero).setOnClickListener(v -> {
            String concat = cardTopUpReloadNumber.getText() + String.valueOf(0);
            cardTopUpReloadNumber.setText(concat);
        });

        findViewById(R.id.txtClear).setOnClickListener(v -> cardTopUpReloadNumber.setText(removeLastCharOptional(Objects.requireNonNull(cardTopUpReloadNumber.getText()).toString())));

    }

    private void setSubmitTopUp() {
        TextView textView = findViewById(R.id.txtOk);
        textView.setOnClickListener(v -> setButtonTopUpProcessClick());
    }

    private void setButtonTopUpProcessClick() {

        AppCompatEditText cardTopUpReloadNumber = findViewById(R.id.cardTopUpReloadNumber);
        TextView textView = findViewById(R.id.txtOk);

        textView.setText("..");

        // if the card number length  equal 14 then go else not
        if (Objects.requireNonNull(cardTopUpReloadNumber.getText()).length() == totalTopUpCharacter) {
            CardTopUpService topUpService = new CardTopUpService(context);
            // opens "starlabs.realm"
            try (Realm realm = Realm.getDefaultInstance()) {
                UserModel userModel = realm.where(UserModel.class).findFirst();
                // old style but assert is joke
                if (userModel != null) {
                    Log.d(TAG, userModel.toString());
                    topUpService.execute(Objects.requireNonNull(cardTopUpReloadNumber.getText()).toString(), userModel.getToken(), userModel.getId());
                }
            }
        } else {
            // warning the user not enough character
            new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("Oops...")
                    .setContentText(getString(R.string.notEnough14Character))
                    .show();
            textView.setText(getString(R.string.ok));

        }

    }
    private String removeLastCharOptional(String s) {
        return Optional.ofNullable(s)
                .filter(str -> str.length() != 0)
                .map(str -> str.substring(0, str.length() - 1))
                .orElse(s);
    }
}
