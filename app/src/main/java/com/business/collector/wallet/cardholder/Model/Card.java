package com.business.collector.wallet.cardholder.Model;

import androidx.annotation.Nullable;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Objects;

@Entity(tableName = "card_table")
public class Card {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String businessName;
    private String businessAddress;
    private String businessPhone;
    private String businessEmail;
    private String imagePath;

    public Card(String businessName, String businessAddress, String businessPhone, String businessEmail, String imagePath) {
        this.businessName = businessName;
        this.businessAddress = businessAddress;
        this.businessPhone = businessPhone;
        this.businessEmail = businessEmail;
        this.imagePath = imagePath;
    }

    @Override
    public int hashCode() {
        return Objects.hash(businessName, businessAddress, businessPhone, businessEmail, imagePath);
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if(obj instanceof Card){
            Card card = (Card) obj;
            return card.getBusinessName().equals(businessName)
                    && card.getBusinessAddress().equals(businessAddress)
                    && card.getBusinessPhone().equals(businessPhone)
                    && card.getBusinessEmail().equals(businessEmail)
                    && card.getImagePath().equals(imagePath);
        }
        return false;
    }

    public int getId() {
        return id;
    }

    public String getBusinessName() {
        return businessName;
    }

    public String getBusinessAddress() {
        return businessAddress;
    }

    public String getBusinessPhone() {
        return businessPhone;
    }

    public String getBusinessEmail() {
        return businessEmail;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setId(int id) {
        this.id = id;
    }
}
