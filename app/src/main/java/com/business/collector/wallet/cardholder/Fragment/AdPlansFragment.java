package com.business.collector.wallet.cardholder.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.business.collector.wallet.cardholder.Interface.PurchaseCallbackInterface;
import com.business.collector.wallet.cardholder.R;

import static com.business.collector.wallet.cardholder.Model.Constants.SKU_ITEM_AD_FREE_1_MONTH_SUB;
import static com.business.collector.wallet.cardholder.Model.Constants.SKU_ITEM_AD_FREE_1_YEAR_SUB;
import static com.business.collector.wallet.cardholder.Model.Constants.SKU_ITEM_AD_FREE_3_MONTH_SUB;
import static com.business.collector.wallet.cardholder.Model.Constants.SKU_ITEM_AD_FREE_6_MONTH_SUB;

public class AdPlansFragment extends Fragment {
    private static final String ARG_MONTHLY_PLAN_PRICE = "com.business.collector.wallet.cardholder.ARG_MONTHLY_PLAN_PRICE";
    private static final String ARG_QUARTERLY_PLAN_PRICE = "com.business.collector.wallet.cardholder.ARG_QUARTERLY_PLAN_PRICE";
    private static final String ARG_HALF_YEARLY_PLAN_PRICE = "com.business.collector.wallet.cardholder.ARG_HALF_YEARLY_PLAN_PRICE";
    private static final String ARG_YEARLY_PLAN_PRICE = "com.business.collector.wallet.cardholder.ARG_YEARLY_PLAN_PRICE";
    private String monthlyPlanPrice, quarterlyPlanPrice, halfYearlyPlanPrice, yearlyPlanPrice;

    private PurchaseCallbackInterface purchaseCallbackInterface;

    public AdPlansFragment() {
        // Required empty public constructor
    }

    public static AdPlansFragment newInstance(String monthlyPlanPrice, String quarterlyPlanPrice, String halfYearlyPlanPrice, String yearlyPlanPrice) {
        AdPlansFragment fragment = new AdPlansFragment();
        Bundle args = new Bundle();
        args.putString(ARG_MONTHLY_PLAN_PRICE, monthlyPlanPrice);
        args.putString(ARG_QUARTERLY_PLAN_PRICE, quarterlyPlanPrice);
        args.putString(ARG_HALF_YEARLY_PLAN_PRICE, halfYearlyPlanPrice);
        args.putString(ARG_YEARLY_PLAN_PRICE, yearlyPlanPrice);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null){
            monthlyPlanPrice = getArguments().getString(ARG_MONTHLY_PLAN_PRICE);
            quarterlyPlanPrice = getArguments().getString(ARG_QUARTERLY_PLAN_PRICE);
            halfYearlyPlanPrice = getArguments().getString(ARG_HALF_YEARLY_PLAN_PRICE);
            yearlyPlanPrice = getArguments().getString(ARG_YEARLY_PLAN_PRICE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_ad_plans, container, false);
        TextView textViewPriceMonthlyPlan = fragmentView.findViewById(R.id.text_view_price_monthly_plan);
        TextView textViewPrice3MonthPlan = fragmentView.findViewById(R.id.text_view_price_3month_plan);
        TextView textViewPrice6MonthPlan = fragmentView.findViewById(R.id.text_view_price_6month_plan);
        TextView textViewPriceYearlyPlan = fragmentView.findViewById(R.id.text_view_price_yearly_plan);
        textViewPriceMonthlyPlan.setText(monthlyPlanPrice);
        textViewPrice3MonthPlan.setText(quarterlyPlanPrice);
        textViewPrice6MonthPlan.setText(halfYearlyPlanPrice);
        textViewPriceYearlyPlan.setText(yearlyPlanPrice);
        Button buttonMonthlyPlan = fragmentView.findViewById(R.id.button_monthly_plan);
        Button button3MonthPlan = fragmentView.findViewById(R.id.button_3month_plan);
        Button button6MonthPlan = fragmentView.findViewById(R.id.button_6month_plan);
        Button buttonYearlyPlan = fragmentView.findViewById(R.id.button_yearly_plan);
        buttonMonthlyPlan.setOnClickListener(monthlyListener);
        button3MonthPlan.setOnClickListener(quarterlyListener);
        button6MonthPlan.setOnClickListener(halfYearlyListener);
        buttonYearlyPlan.setOnClickListener(yearlyListener);

        return fragmentView;
    }

    private View.OnClickListener monthlyListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            purchaseCallbackInterface.selectedPurchase(SKU_ITEM_AD_FREE_1_MONTH_SUB);
        }
    };

    private View.OnClickListener quarterlyListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            purchaseCallbackInterface.selectedPurchase(SKU_ITEM_AD_FREE_3_MONTH_SUB);
        }
    };

    private View.OnClickListener halfYearlyListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            purchaseCallbackInterface.selectedPurchase(SKU_ITEM_AD_FREE_6_MONTH_SUB);
        }
    };

    private View.OnClickListener yearlyListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            purchaseCallbackInterface.selectedPurchase(SKU_ITEM_AD_FREE_1_YEAR_SUB);
        }
    };

    public void setPurchaseCallbackInterface(PurchaseCallbackInterface purchaseCallbackInterface) {
        this.purchaseCallbackInterface = purchaseCallbackInterface;
    }
}
