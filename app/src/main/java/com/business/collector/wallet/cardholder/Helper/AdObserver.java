package com.business.collector.wallet.cardholder.Helper;

import android.content.Context;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class AdObserver implements LifecycleObserver {
    private AdView adView;
    public AdObserver(AdView adView){
        this.adView = adView;
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    public void onCreate()
    {
        if(adView != null){
            AdRequest adRequest = new AdRequest.Builder().build();
            adView.loadAd(adRequest);
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    public void onPause()
    {
        if(adView != null)
            adView.pause();
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    public void onResume()
    {
        if(adView != null)
            adView.resume();
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    public void onDestroy()
    {
        if(adView != null)
            adView.destroy();
    }
}
