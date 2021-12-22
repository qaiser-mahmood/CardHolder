package com.business.collector.wallet.cardholder.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.business.collector.wallet.cardholder.Adapter.IntroPageAdapter;
import com.business.collector.wallet.cardholder.Model.ScreenItem;
import com.business.collector.wallet.cardholder.R;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class IntroActivity extends AppCompatActivity {
    private TabLayout tabLayout;
    private Button buttonNext, buttonGetStarted;
    private int position = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_intro);
        Objects.requireNonNull(getSupportActionBar()).hide();

        final List<ScreenItem> screenItemList = new ArrayList<>();
        screenItemList.add(new ScreenItem("HOME SCREEN", getResources().getString(R.string.swipe_left_a_card), R.drawable.left_swipe));
        screenItemList.add(new ScreenItem("HOME SCREEN", getResources().getString(R.string.swipe_right_a_card), R.drawable.right_swipe));
        screenItemList.add(new ScreenItem("VIEW SCREEN", getResources().getString(R.string.view_screen_description), R.drawable.view_screen));
        screenItemList.add(new ScreenItem("EDIT SCREEN", getResources().getString(R.string.edit_screen_description), R.drawable.edit_screen));
        screenItemList.add(new ScreenItem("HELP SCREEN", getResources().getString(R.string.help_screen_description), R.drawable.ic_contact_large));

        final ViewPager viewPager = findViewById(R.id.view_pager_introActivity);
        final IntroPageAdapter introPageAdapter = new IntroPageAdapter(this, screenItemList);
        viewPager.setAdapter(introPageAdapter);

        tabLayout = findViewById(R.id.tab_layout_introActivity);
        tabLayout.setupWithViewPager(viewPager);
        buttonNext = findViewById(R.id.button_next_introActivity);
        buttonGetStarted = findViewById(R.id.button_getStarted_introActivity);

        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                position = viewPager.getCurrentItem();
                if(position < screenItemList.size()){
                    position++;
                    viewPager.setCurrentItem(position);
                }

                if(position == screenItemList.size() - 1){
                    loadGetStarted();
                }
            }
        });

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if(tab.getPosition() == screenItemList.size() - 1){
                    loadGetStarted();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) { }

            @Override
            public void onTabReselected(TabLayout.Tab tab) { }
        });

        buttonGetStarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void loadGetStarted(){
        buttonGetStarted.setVisibility(View.VISIBLE);
        buttonNext.setVisibility(View.INVISIBLE);
        tabLayout.setVisibility(View.INVISIBLE);
    }
}
