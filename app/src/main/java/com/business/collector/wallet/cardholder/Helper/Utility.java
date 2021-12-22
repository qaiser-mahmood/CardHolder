package com.business.collector.wallet.cardholder.Helper;

import android.content.Context;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

public class Utility {
    protected Context context;
    private View view;

    public Utility(Context context, View view){
        this.context = context;
        this.view = view;
    }

    public void click(){
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animateBlink(v);
                buttonClick();
            }
        });
    }

    protected void buttonClick(){}

    private void animateBlink(View v){
        Animation animation = new AlphaAnimation(0.3f, 1.0f);
        animation.setDuration(700);
        v.startAnimation(animation);
    }
}
