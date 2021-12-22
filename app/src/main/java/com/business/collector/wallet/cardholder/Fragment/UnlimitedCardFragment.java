package com.business.collector.wallet.cardholder.Fragment;

import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.business.collector.wallet.cardholder.Interface.PurchaseCallbackInterface;
import com.business.collector.wallet.cardholder.R;

import java.util.Objects;

import static com.business.collector.wallet.cardholder.Model.Constants.SKU_ITEM_UNLIMITED_CARD_TIER;

public class UnlimitedCardFragment extends Fragment {
    private static final String ARG_UNLIMITED_CARD_PRICE = "com.business.collector.wallet.cardholder.ARG_UNLIMITED_CARD_PRICE";
    private PurchaseCallbackInterface purchaseCallbackInterface;
    private String unlimitedCardPrice;

    public UnlimitedCardFragment() {
        // Required empty public constructor
    }

    public static UnlimitedCardFragment newInstance(String unlimitedCardPrice) {
        UnlimitedCardFragment fragment = new UnlimitedCardFragment();
        Bundle args = new Bundle();
        args.putString(ARG_UNLIMITED_CARD_PRICE, unlimitedCardPrice);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null){
            unlimitedCardPrice = getArguments().getString(ARG_UNLIMITED_CARD_PRICE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_unlimited_card, container, false);

        TextView textViewPriceUnlimitedCardTier = fragmentView.findViewById(R.id.text_view_price_unlimited_card_tier);
        textViewPriceUnlimitedCardTier.setText(unlimitedCardPrice);

        ImageView imageViewClose = fragmentView.findViewById(R.id.image_view_close_unlimitedCardFragment);
        imageViewClose.setOnClickListener(closeListener);
        Button buttonUnlimitedCardTier = fragmentView.findViewById(R.id.button_unlimited_card_tier);
        buttonUnlimitedCardTier.setOnClickListener(unlimitedCardListener);

        return fragmentView;
    }

    private View.OnClickListener closeListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            requireActivity().onBackPressed();
        }
    };

    private View.OnClickListener unlimitedCardListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            purchaseCallbackInterface.selectedPurchase(SKU_ITEM_UNLIMITED_CARD_TIER);
        }
    };

    public void setPurchaseCallbackInterface(PurchaseCallbackInterface purchaseCallbackInterface) {
        this.purchaseCallbackInterface = purchaseCallbackInterface;
    }
}
