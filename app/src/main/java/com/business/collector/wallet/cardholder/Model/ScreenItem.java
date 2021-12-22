package com.business.collector.wallet.cardholder.Model;

public class ScreenItem {
    private String screenHeading, screenDescription;
    private int screenImage;

    public ScreenItem(String screenHeading, String screenDescription, int screenImage) {
        this.screenHeading = screenHeading;
        this.screenDescription = screenDescription;
        this.screenImage = screenImage;
    }

    public String getScreenHeading() {
        return screenHeading;
    }

    public String getScreenDescription() {
        return screenDescription;
    }

    public int getScreenImage() {
        return screenImage;
    }

    public void setScreenHeading(String screenHeading) {
        this.screenHeading = screenHeading;
    }

    public void setScreenDescription(String screenDescription) {
        this.screenDescription = screenDescription;
    }

    public void setScreenImage(int screenImage) {
        this.screenImage = screenImage;
    }
}
