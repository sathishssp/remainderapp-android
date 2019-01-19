package com.trident.krishna.mp3alarm.adaptersmms;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.trident.krishna.mp3alarm.Constantsmms;
import com.trident.krishna.mp3alarm.ContentShowmms;
import com.trident.krishna.mp3alarm.modelsmms.HistoryModel;

import java.util.List;

/**
 * Created by Shoukhin on 7/9/2018.
 */

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ItemViewHolder> {
    private List<HistoryModel> historyList;
    private Context context;
    private boolean isDefinedAsMySchedule = false;

    public HistoryAdapter(List<HistoryModel> historyList, Context context) {
        this.historyList = historyList;
        this.context = context;
    }

    public void definedAsMySchedule() {
        isDefinedAsMySchedule = true;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(com.trident.krishna.mp3alarm.R.layout.alarm_history_viewmms, parent, false);
        return new ItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ItemViewHolder holder, final int position) {
        holder.historyImageView.setImageResource(historyList.get(position).getImageResourceID());
        holder.historyMessageTextView.setText(historyList.get(position).getMessage());
        holder.historySetterNameTextView.setText(historyList.get(position).getSetterName());
        Glide.with(context).load(historyList.get(position).getImageResourceID())
                .into(holder.historyImageView);
//        holder.historyDateTextView.setText(historyList.get(position).getFormattedDate());
//        holder.historyTimeTextView.setText(historyList.get(position).getFormattedTime());

        if (isDefinedAsMySchedule) {
            holder.receiverTextView.setText("To: ");
        }

        holder.historyRootLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ContentShowmms.class);
                intent.putExtra(Constantsmms.ID, historyList.get(position).getId());
                intent.putExtra(Constantsmms.TABLE_NAME, historyList.get(position).getTableName());
                intent.putExtra(Constantsmms.PHONE_STATUS, -1);
                intent.putExtra(Constantsmms.PREVIEW, true);

                if (isDefinedAsMySchedule)
                    intent.putExtra(Constantsmms.RECEIVER, Constantsmms.TO);
                else
                    intent.putExtra(Constantsmms.RECEIVER, Constantsmms.FROM);

                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return historyList.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        ImageView historyImageView;
        TextView historyMessageTextView;
        TextView receiverTextView;
        TextView historySetterNameTextView;
        TextView historyDateTextView;
        TextView historyTimeTextView;
        LinearLayout historyRootLayout;

        public ItemViewHolder(View view) {
            super(view);
            historyImageView = view.findViewById(com.trident.krishna.mp3alarm.R.id.schedule_image_view);
            historyMessageTextView = view.findViewById(com.trident.krishna.mp3alarm.R.id.schedule_message_text_view);
            historySetterNameTextView = view.findViewById(com.trident.krishna.mp3alarm.R.id.schedule_setter_name_text_view);
            historyDateTextView = view.findViewById(com.trident.krishna.mp3alarm.R.id.schedule_date_text_view);
            historyTimeTextView = view.findViewById(com.trident.krishna.mp3alarm.R.id.schedule_time_text_view);
            receiverTextView = view.findViewById(com.trident.krishna.mp3alarm.R.id.receiver_text_view);
            historyRootLayout = view.findViewById(com.trident.krishna.mp3alarm.R.id.history_root_layout);
        }
    }
}

