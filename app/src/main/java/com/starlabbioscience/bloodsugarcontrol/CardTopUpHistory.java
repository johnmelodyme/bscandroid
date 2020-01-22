package com.starlabbioscience.bloodsugarcontrol;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.starlabbioscience.bloodsugarcontrol.adapter.CardTopUpHistoryAdapter;
import com.starlabbioscience.bloodsugarcontrol.model.UserModel;
import com.starlabbioscience.bloodsugarcontrol.service.CardTopUpHistoryService;

import java.util.Objects;

import io.realm.Realm;

public class CardTopUpHistory extends AppCompatActivity {
    private static final String TAG = CardTopUpHistory.class.getName();
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reload_history);
        context = CardTopUpHistory.this;
        // force portrait
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        setReloadToolbar();
        getDataFromServer();
    }

    private void setReloadToolbar() {

        // take the main link id
        findViewById(R.id.reloadHistoryPageToMusicPage).setOnClickListener(v -> {
            Intent intent = new Intent(context, MusicPlayer.class);
            startActivity(intent);
            Log.d(TAG, "wong intent");

        });

        findViewById(R.id.reloadHistoryPageToProfilePageButton).setOnClickListener(v -> {
            Log.d(TAG, "err why execute diff");
            Intent intent = new Intent(context, Profile.class);
            startActivity(intent);
        });

    }

    private void getDataFromServer() {

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);

        RecyclerView recyclerView = findViewById(R.id.cardTopUpHistoryLists);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        // bind form to recycle view
        CardTopUpHistoryAdapter cardTopUpHistoryAdapter = new CardTopUpHistoryAdapter();
        recyclerView.setAdapter(cardTopUpHistoryAdapter);
        // always vertical
        DividerItemDecoration mDividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL);
        mDividerItemDecoration.setDrawable(Objects.requireNonNull(context.getDrawable(R.drawable.divider_bottom)));
        recyclerView.addItemDecoration(mDividerItemDecoration);

        Realm.init(context);
        try (Realm realm = Realm.getDefaultInstance()) {
            UserModel userModel = realm.where(UserModel.class).findFirst();
            if (userModel != null) {
                CardTopUpHistoryService cardTopUpHistoryService = new CardTopUpHistoryService(cardTopUpHistoryAdapter, context);
                cardTopUpHistoryService.execute(userModel.getToken());
            }
        }

    }

}
