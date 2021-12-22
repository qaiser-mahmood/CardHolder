package com.business.collector.wallet.cardholder;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.business.collector.wallet.cardholder.Model.Card;

import java.util.List;

public class CardViewModel extends AndroidViewModel {
    private CardRepository repository;
    private LiveData<List<Card>> allCards;

    public CardViewModel(@NonNull Application application) {
        super(application);
        repository = new CardRepository(application);
        allCards = repository.getAllCards();
    }

    public void insert(Card card){
        repository.insert(card);
    }

    public void update(Card card){
        repository.update(card);
    }

    public void delete(Card card){
        repository.delete(card);
    }

    public void deleteAllCards(){
        repository.deleteAllCards();
    }

    public LiveData<List<Card>> getAllCards(){
        return allCards;
    }
}
