package com.business.collector.wallet.cardholder.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.business.collector.wallet.cardholder.GlideApp;
import com.business.collector.wallet.cardholder.Model.ScreenItem;
import com.business.collector.wallet.cardholder.R;

import java.util.List;

public class IntroPageAdapter extends PagerAdapter {
    private Context context;
    private List<ScreenItem> itemList;

    public IntroPageAdapter(Context context, List<ScreenItem> itemList) {
        this.context = context;
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.screen_layout, null);
        ImageView imageViewScreen = view.findViewById(R.id.image_view_screen_layout);
        TextView textViewHeading = view.findViewById(R.id.text_view_heading_screen_layout);
        TextView textViewDescription = view.findViewById(R.id.text_view_description_screen_layout);

        GlideApp
                .with(context)
                .load(itemList.get(position).getScreenImage())
                .placeholder(R.drawable.ic_launcher_background)
                .fitCenter()
                .into(imageViewScreen);
        textViewHeading.setText(itemList.get(position).getScreenHeading());
        textViewDescription.setText(itemList.get(position).getScreenDescription());

        container.addView(view);
        return view;
    }

    @Override
    public int getCount() {
        return itemList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
