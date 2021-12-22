package com.business.collector.wallet.cardholder.Fragment;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.business.collector.wallet.cardholder.R;

import static com.business.collector.wallet.cardholder.Model.Constants.DATE_MAYBE_LATER_SHOW_DIALOG;
import static com.business.collector.wallet.cardholder.Model.Constants.DATE_NEXT_SHOW_DIALOG;
import static com.business.collector.wallet.cardholder.Model.Constants.DAYS_MAYBE_LATER;
import static com.business.collector.wallet.cardholder.Model.Constants.DAYS_UNTIL_PROMPT;
import static com.business.collector.wallet.cardholder.Model.Constants.DONT_SHOW_AGAIN;
import static com.business.collector.wallet.cardholder.Model.Constants.ONE_DAY_MILLIS;
import static com.business.collector.wallet.cardholder.Model.Constants.SHARED_PREFS;

public class AppRaterFragment extends Fragment {

    private SharedPreferences sharedPreferences;

    public AppRaterFragment() {
        // Required empty public constructor
    }

    public static AppRaterFragment newInstance() {
        return new AppRaterFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = requireActivity().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_app_rater, container, false);
        TextView textViewRateIt = view.findViewById(R.id.text_view_rate_app);
        TextView textViewRemindLater = view.findViewById(R.id.text_view_remind_later);
        TextView textViewNoThanks = view.findViewById(R.id.text_view_dont_show_again);
        textViewRateIt.setOnClickListener(rateItListener);
        textViewRemindLater.setOnClickListener(remindLaterListener);
        textViewNoThanks.setOnClickListener(noThanksListener);
        return view;
    }

    private View.OnClickListener rateItListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            long dateNextShowDialog = System.currentTimeMillis() + DAYS_UNTIL_PROMPT * ONE_DAY_MILLIS;
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putLong(DATE_NEXT_SHOW_DIALOG, dateNextShowDialog);
            editor.putLong(DATE_MAYBE_LATER_SHOW_DIALOG, dateNextShowDialog + System.currentTimeMillis() + DAYS_MAYBE_LATER * ONE_DAY_MILLIS);
            editor.apply();

            try {
                Uri appUri = Uri.parse("market://details?id=" + requireActivity().getPackageName());
                requireActivity().startActivity(new Intent(Intent.ACTION_VIEW, appUri));
            }catch (ActivityNotFoundException e){
                Uri appUri = Uri.parse("http://play.google.com/store/apps/details?id=" + requireActivity().getPackageName());
                requireActivity().startActivity(new Intent(Intent.ACTION_VIEW, appUri));
            }
        }
    };

    private View.OnClickListener remindLaterListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            long dateMaybeLaterShowDialog = System.currentTimeMillis() + DAYS_MAYBE_LATER * ONE_DAY_MILLIS;
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putLong(DATE_MAYBE_LATER_SHOW_DIALOG, dateMaybeLaterShowDialog);
            editor.putLong(DATE_NEXT_SHOW_DIALOG, dateMaybeLaterShowDialog + System.currentTimeMillis() + DAYS_UNTIL_PROMPT * ONE_DAY_MILLIS);
            editor.apply();
            requireActivity().onBackPressed();
        }
    };

    private View.OnClickListener noThanksListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean(DONT_SHOW_AGAIN, true);
            editor.apply();
            requireActivity().onBackPressed();
        }
    };

}
