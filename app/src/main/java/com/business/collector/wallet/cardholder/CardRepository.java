package com.business.collector.wallet.cardholder;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.business.collector.wallet.cardholder.Model.Card;
import com.business.collector.wallet.cardholder.Model.CardDao;
import com.business.collector.wallet.cardholder.Model.CardDatabase;

import java.util.List;

public class CardRepository {
    private CardDao cardDao;
    private LiveData<List<Card>> allCards;

    public CardRepository(Application application) {
        CardDatabase cardDatabase = CardDatabase.getInstance(application);
        cardDao = cardDatabase.cardDao();
        allCards = cardDao.getAllCards();
    }

    public void insert(Card card){
        new InsertAsyncTask(cardDao).execute(card);
    }

    public void update(Card card){
        new UpdateAsyncTask(cardDao).execute(card);
    }

    public void delete(Card card){
        new DeleteAsyncTask(cardDao).execute(card);
    }

    public void deleteAllCards(){
        new DeleteAllAsyncTask(cardDao).execute();
    }

    public LiveData<List<Card>> getAllCards(){
        return allCards;
    }

    private static class InsertAsyncTask extends AsyncTask<Card, Void, Void>{
        private CardDao cardDao;

        public InsertAsyncTask(CardDao cardDao) {
            this.cardDao = cardDao;
        }

        @Override
        protected Void doInBackground(Card... cards) {
            cardDao.insert(cards[0]);
            return null;
        }
    }

    private static class UpdateAsyncTask extends AsyncTask<Card, Void, Void>{
        private CardDao cardDao;

        public UpdateAsyncTask(CardDao cardDao) {
            this.cardDao = cardDao;
        }

        @Override
        protected Void doInBackground(Card... cards) {
            cardDao.update(cards[0]);
            return null;
        }
    }

    private static class DeleteAsyncTask extends AsyncTask<Card, Void, Void>{
        private CardDao cardDao;

        public DeleteAsyncTask(CardDao cardDao) {
            this.cardDao = cardDao;
        }

        @Override
        protected Void doInBackground(Card... cards) {
            cardDao.delete(cards[0]);
            return null;
        }
    }

    private static class DeleteAllAsyncTask extends AsyncTask<Void, Void, Void>{
        private CardDao cardDao;

        public DeleteAllAsyncTask(CardDao cardDao) {
            this.cardDao = cardDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            cardDao.deleteAllCards();
            return null;
        }
    }
}
