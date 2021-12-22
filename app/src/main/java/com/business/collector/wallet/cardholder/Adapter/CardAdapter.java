package com.business.collector.wallet.cardholder.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.business.collector.wallet.cardholder.GlideApp;
import com.business.collector.wallet.cardholder.Interface.OnItemClickListener;
import com.business.collector.wallet.cardholder.Model.Card;
import com.business.collector.wallet.cardholder.R;

import java.util.ArrayList;
import java.util.List;

import static com.business.collector.wallet.cardholder.Model.Constants.SPLIT_TAG;

public class CardAdapter extends ListAdapter<Card, CardViewHolder> implements Filterable {
    private OnItemClickListener listener;
    private Context context;
    private List<Card> cardListFull;

    public CardAdapter(Context context) {
        super(DIFF_CALLBACK);
        this.context = context;
    }

    private static final DiffUtil.ItemCallback<Card> DIFF_CALLBACK = new DiffUtil.ItemCallback<Card>() {
        @Override
        public boolean areItemsTheSame(@NonNull Card oldItem, @NonNull Card newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Card oldItem, @NonNull Card newItem) {
            return oldItem.getBusinessName().equals(newItem.getBusinessName())
                    && oldItem.getBusinessAddress().equals(newItem.getBusinessAddress())
                    && oldItem.getBusinessPhone().equals(newItem.getBusinessPhone())
                    && oldItem.getBusinessEmail().equals(newItem.getBusinessEmail())
                    && oldItem.getImagePath().equals(newItem.getImagePath());
        }
    };

    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.card_item, parent, false);
        return new CardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CardViewHolder holder, final int position) {
        final Card currentCard = getItem(position);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener != null && position != RecyclerView.NO_POSITION){
                    listener.onItemClick(currentCard);
                }
            }
        });

        String[] nameArray = currentCard.getBusinessName().split(SPLIT_TAG);
        String[] phoneArray = currentCard.getBusinessPhone().split(SPLIT_TAG);
        String[] emailArray = currentCard.getBusinessEmail().split(SPLIT_TAG);
        String[] addressArray = currentCard.getBusinessAddress().split(SPLIT_TAG);
        String[] imagePathArray = currentCard.getImagePath().split(SPLIT_TAG);

        if(nameArray.length > 0)
            holder.textViewBusinessName.setText(nameArray[0]);
        if(addressArray.length > 0)
            holder.textViewBusinessAddress.setText(addressArray[0]);
        if(phoneArray.length > 0)
            holder.textViewBusinessPhone.setText(phoneArray[0]);
        if(emailArray.length > 0)
            holder.textViewBusinessEmail.setText(emailArray[0]);

        if(imagePathArray.length > 0){
            GlideApp
                    .with(holder.itemView.getContext())
                    .load(imagePathArray[0])
                    .into(holder.imageViewCardImage);
        }

    }

    public Card getCardAt(int position){
        return getItem(position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }

    @Override
    public Filter getFilter() {
        return searchFilter;
    }

    public void setCardListFull(List<Card> cardListFull){
        this.cardListFull = cardListFull;
    }

    private Filter searchFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Card> filteredList = new ArrayList<>();
            if(constraint == null || constraint.length() == 0){
                filteredList.addAll(cardListFull);
            }else {
                String searchString = constraint.toString().toLowerCase().trim();
                for(Card card : cardListFull){
                    if(card.getBusinessName().toLowerCase().trim().contains(searchString)
                            || card.getBusinessPhone().toLowerCase().trim().contains(searchString)
                            || card.getBusinessEmail().toLowerCase().trim().contains(searchString)
                            || card.getBusinessAddress().toLowerCase().trim().contains(searchString)){
                        filteredList.add(card);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            submitList((List<Card>) results.values);
        }
    };
}

