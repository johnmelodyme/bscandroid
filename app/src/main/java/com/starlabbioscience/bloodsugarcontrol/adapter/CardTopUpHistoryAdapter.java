package com.starlabbioscience.bloodsugarcontrol.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.starlabbioscience.bloodsugarcontrol.R;
import com.starlabbioscience.bloodsugarcontrol.model.CardTopUpHistoryRespondModel;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;


public class CardTopUpHistoryAdapter extends RecyclerView.Adapter<CardTopUpHistoryAdapter.CardTopUpHistoryAdapterViewHolder> {
    private static final String TAG = CardTopUpHistoryAdapter.class.getName();

    private List<? extends CardTopUpHistoryRespondModel> cardTopUpHistoryRespondModels;

    public CardTopUpHistoryAdapter() {

    }

    public void execute(List<CardTopUpHistoryRespondModel> topUpModel1) {
        cardTopUpHistoryRespondModels = topUpModel1;

    }

    @NonNull
    @Override
    public CardTopUpHistoryAdapter.CardTopUpHistoryAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new CardTopUpHistoryAdapter.CardTopUpHistoryAdapterViewHolder(inflater.inflate(R.layout.activity_reload_list, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CardTopUpHistoryAdapter.CardTopUpHistoryAdapterViewHolder holder, int position) {

        Log.d(TAG, "model size" + cardTopUpHistoryRespondModels.size());
        if (cardTopUpHistoryRespondModels.size() > 0) {
            //Log.d(TAG,cardTopUpHistoryRespondModels.get(position).getUsed_date());
            String duration = "Duration : " + cardTopUpHistoryRespondModels.get(position).getDuration()+" Month";
            holder.duration.setText(duration);

            Locale locale = new Locale("ms", "MY");
            SimpleDateFormat outSDF = new SimpleDateFormat("dd-MM-yyyy",locale);
            SimpleDateFormat inSDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",locale);

            try {
                Date date = inSDF.parse(cardTopUpHistoryRespondModels.get(position).getUsed_date());
                String purchaseDate = "Purchase Date : " + outSDF.format(Objects.requireNonNull(date));
                holder.usedDate.setText(purchaseDate);

            } catch (final Exception e) {
                Log.d(TAG, Objects.requireNonNull(e.getMessage()));
            }
        } else {
            Log.d(TAG, "something wrong");
        }

    }

    @Override
    public int getItemCount() {
        return cardTopUpHistoryRespondModels == null ? 0 : cardTopUpHistoryRespondModels.size();
    }

    static class CardTopUpHistoryAdapterViewHolder extends RecyclerView.ViewHolder {

        private final TextView duration;
        private final TextView usedDate;

        CardTopUpHistoryAdapterViewHolder(View view) {
            super(view);
            // normal would be  layoutInflater

            // bind object field
            // e.g foodPhotoUrl = itemView.findViewById(R.id.foodListPhoto);

            // wait layout not changed yet
            duration = itemView.findViewById(R.id.reloadHistoryDuration);
            usedDate = itemView.findViewById(R.id.reloadHistoryDate);


        }

    }


}
