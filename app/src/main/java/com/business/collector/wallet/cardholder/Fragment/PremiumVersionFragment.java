package com.business.collector.wallet.cardholder.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.business.collector.wallet.cardholder.Interface.PurchaseCallbackInterface;
import com.business.collector.wallet.cardholder.R;

import static com.business.collector.wallet.cardholder.Model.Constants.SKU_ITEM_PREMIUM_VERSION;

public class PremiumVersionFragment extends Fragment {
    private static final String ARG_PREMIUM_VERSION_PRICE = "com.business.collector.wallet.cardholder.ARG_PREMIUM_VERSION_PRICE";
    private PurchaseCallbackInterface purchaseCallbackInterface;
    private String premiumVersionPrice;

    public PremiumVersionFragment() {
        // Required empty public constructor
    }

    public static PremiumVersionFragment newInstance(String premiumVersionPrice) {
        PremiumVersionFragment fragment = new PremiumVersionFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PREMIUM_VERSION_PRICE, premiumVersionPrice);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null){
            premiumVersionPrice = getArguments().getString(ARG_PREMIUM_VERSION_PRICE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_premium_version, container, false);

        TextView textViewPricePremiumVersion = fragmentView.findViewById(R.id.text_view_price_premium_version);
        textViewPricePremiumVersion.setText(premiumVersionPrice);

        ImageView imageViewClose = fragmentView.findViewById(R.id.image_view_close_premiumVersionFragment);
        imageViewClose.setOnClickListener(closeListener);

        Button buttonPremiumVersion = fragmentView.findViewById(R.id.button_premium_version);
        buttonPremiumVersion.setOnClickListener(premiumVersionListener);

        return fragmentView;
    }

    private View.OnClickListener closeListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            requireActivity().onBackPressed();
        }
    };

    private View.OnClickListener premiumVersionListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            purchaseCallbackInterface.selectedPurchase(SKU_ITEM_PREMIUM_VERSION);
        }
    };

    public void setPurchaseCallbackInterface(PurchaseCallbackInterface purchaseCallbackInterface) {
        this.purchaseCallbackInterface = purchaseCallbackInterface;
    }
}
