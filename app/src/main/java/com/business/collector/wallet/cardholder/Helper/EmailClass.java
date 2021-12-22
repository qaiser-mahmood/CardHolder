package com.business.collector.wallet.cardholder.Helper;

import android.content.Context;
import android.content.Intent;
import android.view.View;

public class EmailClass extends Utility {
    private String[] emailArray;
    private String emailSubject;
    public EmailClass(Context context, View view, String[] emailArray, String emailSubject) {
        super(context, view);
        this.emailArray = emailArray;
        this.emailSubject = emailSubject;
    }

    @Override
    protected void buttonClick() {
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setType("message/rfc822");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, emailArray);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, emailSubject);

        context.startActivity(Intent.createChooser(emailIntent, "Choose an email client"));
    }
}
