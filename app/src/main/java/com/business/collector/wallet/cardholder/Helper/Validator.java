package com.business.collector.wallet.cardholder.Helper;

public class Validator {

    public boolean validatePhone(String line){
        char[] chars = line.toCharArray();
        StringBuilder phone = new StringBuilder();
        for(char c : chars){
            if(Character.isDigit(c))
                phone.append(c);
        }
        int MAX_PHONE_DIGITS = 17;
        int MIN_PHONE_DIGITS = 6;
        return phone.length() >= MIN_PHONE_DIGITS && phone.length() <= MAX_PHONE_DIGITS;
    }

    public boolean validateEmail(String line){
        StringBuilder stringBuilder = new StringBuilder();
        char[] text = line.trim().toLowerCase().toCharArray();
        int count = 0;
        for (char c : text) {
            if (c == '@')
                count++;
        }
        if (count == 1) {
            for (char c : text) {
                if (Character.isDigit(c) || Character.isLetter(c) || c == '_' || c == '.' || c == '@')
                    stringBuilder.append(c);
            }

            char first = stringBuilder.charAt(0);
            char last = stringBuilder.charAt(stringBuilder.length() -1);

            return first != '@' && first != '.' && last != '@' && last != '.';
        }
        return false;
    }

    public boolean validateAddress(String line){
        String[] strings = line.trim().split(" ");
        for(String word : strings){
            if(word.contains("/"))
                return true;

            StringBuilder stringBuilder = new StringBuilder();
            char[] chars = word.toCharArray();
            for(char c : chars){
                if(Character.isDigit(c))
                    stringBuilder.append(c);
            }
            if(stringBuilder.length() >= 4 && stringBuilder.length() <= 6)
                return true;
        }
        return false;
    }

//    private boolean checkFirstDigit(String line){
//        char[] chars = line.toCharArray();
//        StringBuilder phone = new StringBuilder();
//        for(char c : chars){
//            if(Character.isDigit(c) || c == '+')
//                phone.append(c);
//        }
//        return phone.charAt(0) == '0' || phone.charAt(0) == '+';
//    }
//
//    private boolean checkPhWord(String line){
//        String[] strings = line.split(" ");
//        for(String word : strings){
//            String ph = word.toLowerCase();
//            if(ph.equals("ph") || ph.equals("ph:") || ph.equals("ph#") || ph.equals("ph-") || ph.equals("ph_"))
//                return true;
//        }
//        return false;
//    }
}
