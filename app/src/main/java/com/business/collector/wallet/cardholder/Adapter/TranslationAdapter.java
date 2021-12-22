package com.business.collector.wallet.cardholder.Adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.business.collector.wallet.cardholder.R;

public class TranslationAdapter extends ListAdapter<String, TranslationAdapter.MyViewHolder> {
    private Context context;


    public TranslationAdapter(Context context) {
        super(DIFF_CALLBACK);
        this.context = context;
    }

    private static DiffUtil.ItemCallback<String> DIFF_CALLBACK = new DiffUtil.ItemCallback<String>() {
        @Override
        public boolean areItemsTheSame(@NonNull String oldItem, @NonNull String newItem) {
            return oldItem.equals(newItem);
        }

        @Override
        public boolean areContentsTheSame(@NonNull String oldItem, @NonNull String newItem) {
            return oldItem.equals(newItem);
        }
    };



    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_translation, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        String currentItem = getItem(position);
        if(currentItem.equals("Name:") || currentItem.equals("Address:") || currentItem.equals("Phone:") || currentItem.equals("Email:")){
            holder.textViewListItem.setText(currentItem);
            holder.textViewListItem.setTypeface(holder.textViewListItem.getTypeface(), Typeface.BOLD);
            holder.textViewListItem.setTextColor(context.getResources().getColor(android.R.color.black));
            holder.textViewListItem.setBackgroundColor(context.getResources().getColor(android.R.color.transparent));
        }else {
            holder.textViewListItem.setText(currentItem);
            holder.textViewListItem.setTypeface(null, Typeface.NORMAL);
            holder.textViewListItem.setTextColor(context.getResources().getColor(R.color.colorDarkGray));
            holder.textViewListItem.setBackgroundColor(context.getResources().getColor(R.color.colorGray));
        }
    }

    public String getTextAt(int position){
        if(position > 0)
            return getItem(position);
        return "";
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView textViewListItem;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewListItem = itemView.findViewById(R.id.text_view_listItem_Translation);
        }
    }
}
