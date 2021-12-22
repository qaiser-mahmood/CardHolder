package com.business.collector.wallet.cardholder.Adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.business.collector.wallet.cardholder.R;

class CardViewHolder extends RecyclerView.ViewHolder {
    TextView textViewBusinessName;
    TextView textViewBusinessAddress;
    TextView textViewBusinessPhone;
    TextView textViewBusinessEmail;
    ImageView imageViewCardImage;

    public CardViewHolder(@NonNull final View itemView) {
        super(itemView);

        textViewBusinessName = itemView.findViewById(R.id.text_view_business_name);
        textViewBusinessAddress = itemView.findViewById(R.id.text_view_business_address);
        textViewBusinessPhone = itemView.findViewById(R.id.text_view_business_phone);
        textViewBusinessEmail = itemView.findViewById(R.id.text_view_business_email);
        imageViewCardImage = itemView.findViewById(R.id.image_view_card_image);
    }
}
