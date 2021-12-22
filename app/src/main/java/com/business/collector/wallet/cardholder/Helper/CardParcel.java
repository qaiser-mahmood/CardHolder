package com.business.collector.wallet.cardholder.Helper;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CardParcel implements Parcelable {
    private List<String> nameList = new ArrayList<>();
    private List<String> addressList = new ArrayList<>();
    private List<String> phoneList = new ArrayList<>();
    private List<String> emailList = new ArrayList<>();
    private List<String> categoryList;

    public CardParcel(List<String> categoryList){
        this.categoryList = categoryList;
    }

    protected CardParcel(@NonNull Parcel in) {
        categoryList = in.createStringArrayList();
        nameList = in.createStringArrayList();
        phoneList = in.createStringArrayList();
        emailList = in.createStringArrayList();
        addressList = in.createStringArrayList();
    }

    public static final Creator<CardParcel> CREATOR = new Creator<CardParcel>() {
        @Override
        public CardParcel createFromParcel(Parcel in) {
            return new CardParcel(in);
        }

        @Override
        public CardParcel[] newArray(int size) {
            return new CardParcel[size];
        }
    };

    @Override
    public int hashCode() {
        return Objects.hash(nameList, addressList, phoneList, emailList, categoryList);
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if(obj instanceof CardParcel){
            CardParcel cardParcel = (CardParcel) obj;
            return matchLists(cardParcel.getNameList(), nameList)
                    && matchLists(cardParcel.getAddressList(), addressList)
                    && matchLists(cardParcel.getPhoneList(), phoneList)
                    && matchLists(cardParcel.getEmailList(), emailList)
                    && matchLists(cardParcel.getCategoryList(), categoryList);
        }
        return false;
    }

    private boolean matchLists(@NonNull List<String> list1, @NonNull List<String> list2){
        if(list1.size() != list2.size()) return false;
        int countSimilarItems1 = 0;
        for(String s : list1){
            if(list2.contains(s))
                countSimilarItems1++;
        }

        int countSimilarItems2 = 0;
        for(String s : list2){
            if(list1.contains(s))
                countSimilarItems2++;
        }

        return countSimilarItems1 == countSimilarItems2 && countSimilarItems1 == list1.size();
    }

    public void addNameList(List<String> nameList){
        this.nameList.addAll(nameList);
    }

    public void addPhoneList(List<String> phoneList){
        this.phoneList.addAll(phoneList);
    }

    public void addEmailList(List<String> emailList){
        this.emailList.addAll(emailList);
    }

    public void addAddressList(List<String> addressList){
        this.addressList.addAll(addressList);
    }

    public void setNameList(List<String> nameList) {
        this.nameList = nameList;
    }

    public void setAddressList(List<String> addressList) {
        this.addressList = addressList;
    }

    public void setPhoneList(List<String> phoneList) {
        this.phoneList = phoneList;
    }

    public void setEmailList(List<String> emailList) {
        this.emailList = emailList;
    }

    public void addName(@NonNull String name){
        this.nameList.add(name.trim());
    }

    public void addAddress(@NonNull String address){
        this.addressList.add(address.trim());
    }

    public void addPhone(@NonNull String phone){
        this.phoneList.add(phone.trim());
    }

    public void addEmail(@NonNull String email){
        this.emailList.add(email.trim());
    }

    public void removeName(String name){
        this.nameList.remove(name);
    }

    public void removeAddress(String address){
        this.addressList.remove(address);
    }

    public void removePhone(String phone){
        this.phoneList.remove(phone);
    }

    public void removeEmail(String email){
        this.emailList.remove(email);
    }

    public List<String> getNameList() {
        return nameList;
    }

    public List<String> getAddressList() {
        return addressList;
    }

    public List<String> getPhoneList() {
        return phoneList;
    }

    public List<String> getEmailList() {
        return emailList;
    }

    public List<String> getCategoryList() {
        return categoryList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeStringList(categoryList);
        dest.writeStringList(nameList);
        dest.writeStringList(phoneList);
        dest.writeStringList(emailList);
        dest.writeStringList(addressList);
    }

    private String capitalizeFirstLetterEachWord(String string){
        String[] strings = string.split(" ");
        StringBuilder stringBuilder = new StringBuilder();
        for(String s : strings){
            String firstLetter = s.trim().substring(0, 1).toUpperCase();
            stringBuilder.append(firstLetter).append(s.substring(1).toLowerCase()).append(" ");
        }
        return stringBuilder.toString().trim();
    }

    public void cleanParcel(){
        for(String category : categoryList){
            switch (category) {
                case "Name": {
                    if(nameList.size() == 0) break;

                    List<String> cleanList = new ArrayList<>();
                    for (int i = 0; i < nameList.size(); i++) {
                        String s = nameList.get(i).trim();
                        if (!s.isEmpty())
                            cleanList.add(capitalizeFirstLetterEachWord(s));
                    }
                    nameList = cleanList;
                }
                break;
                case "Phone": {
                    if(phoneList.size() == 0) break;

                    List<String> cleanList = new ArrayList<>();
                    for (int i = 0; i < phoneList.size(); i++) {
                        if (!phoneList.get(i).isEmpty())
                            cleanList.add(phoneList.get(i));
                    }
                    phoneList = cleanList;
                }
                break;
                case "Email": {
                    if(emailList.size() == 0) break;

                    List<String> cleanList = new ArrayList<>();
                    for (int i = 0; i < emailList.size(); i++) {
                        if (!emailList.get(i).isEmpty())
                            cleanList.add(emailList.get(i).toLowerCase().trim());
                    }
                    emailList = cleanList;
                }
                break;
                case "Address": {
                    if(addressList.size() == 0) break;

                    List<String> cleanList = new ArrayList<>();
                    for (int i = 0; i < addressList.size(); i++) {
                        String s = addressList.get(i).trim();
                        if (!s.isEmpty())
                            cleanList.add(capitalizeFirstLetterEachWord(s));
                    }
                    addressList = cleanList;
                }
                break;
                default:
            }
        }
    }
}
