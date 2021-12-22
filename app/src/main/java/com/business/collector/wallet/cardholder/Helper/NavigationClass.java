package com.business.collector.wallet.cardholder.Helper;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;

public class NavigationClass extends Utility {
    private String businessAddress;
    public NavigationClass(Context context, View view, String businessAddress) {
        super(context, view);
        this.businessAddress = businessAddress;
    }

    @Override
    protected void buttonClick() {
        Uri mapUri = Uri.parse("geo:0,0?z=10&q=" + businessAddress);
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, mapUri);
        context.startActivity(Intent.createChooser(mapIntent, "Choose map"));
    }
}
