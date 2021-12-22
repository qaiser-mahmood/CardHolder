package com.business.collector.wallet.cardholder.Helper;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.view.View;

import androidx.core.app.ActivityCompat;

public class PhoneClass extends Utility {
    private String phoneNumber;
    public PhoneClass(Context context, View view, String phoneNumber) {
        super(context, view);
        StringBuilder stringBuilder = new StringBuilder();
        char[] chars = phoneNumber.toCharArray();
        for(char c : chars){
            if(Character.isDigit(c))
                stringBuilder.append(c);
        }
        this.phoneNumber = stringBuilder.toString();
    }

    @Override
    protected void buttonClick() {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        String dial = "tel:" + phoneNumber;
        context.startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
    }
}
