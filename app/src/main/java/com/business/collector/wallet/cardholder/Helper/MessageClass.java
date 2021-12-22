package com.business.collector.wallet.cardholder.Helper;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;

public class MessageClass extends Utility {
    private String phoneNumber;
    private String subject;
    public MessageClass(Context context, View view, String phoneNumber, String subject) {
        super(context, view);
        this.phoneNumber = phoneNumber;
        this.subject = subject;
    }

    @Override
    protected void buttonClick() {
        String dial = "sms:" + phoneNumber;
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(dial));
        intent.putExtra("sms_body", subject + "\n");
        context.startActivity(intent);
    }
}
