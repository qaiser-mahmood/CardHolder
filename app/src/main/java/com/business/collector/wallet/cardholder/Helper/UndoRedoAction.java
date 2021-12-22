package com.business.collector.wallet.cardholder.Helper;

public class UndoRedoAction {
    private int position;
    private String textTop="";
    private String textBelow="";

    public UndoRedoAction(int position, String textTop) {
        this.position = position;
        this.textTop = textTop;
    }

    public UndoRedoAction(int position, String textTop, String textBelow) {
        this.position = position;
        this.textTop = textTop;
        this.textBelow = textBelow;
    }

    public int getPosition() {
        return position;
    }

    public String getTextTop() {
        return textTop;
    }

    public String getTextBelow() {
        return textBelow;
    }

}
