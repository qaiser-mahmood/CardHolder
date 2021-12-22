package com.business.collector.wallet.cardholder.Model;

import androidx.annotation.Nullable;

import java.util.Objects;

public class CategoryClass {
    private String text, value, category;
    private boolean selected = false;

    public CategoryClass(String category, String text, String value) {
        this.category = category;
        this.text = text;
        this.value = value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(text, value);
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if(obj instanceof CategoryClass){
            CategoryClass categoryClass = (CategoryClass) obj;
            return text.equals(categoryClass.text) && value.equals(categoryClass.value);
        }
       return false;
    }

    public String getCategory() {
        return category;
    }

    public String getText() {
        return text;
    }

    public String getValue() {
        return value;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
