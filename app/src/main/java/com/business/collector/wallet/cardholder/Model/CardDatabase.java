package com.business.collector.wallet.cardholder.Model;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {Card.class}, version = 1, exportSchema = false)
public abstract class CardDatabase extends RoomDatabase {

    private static CardDatabase instance;
    public abstract CardDao cardDao();

    public static synchronized CardDatabase getInstance(Context context){
        if(instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(), CardDatabase.class,
                    "card_database")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .build();
        }
        return instance;
    }

    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback(){
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new RoomCallbackAsyncTask(instance).execute();
        }
    };

    private static class RoomCallbackAsyncTask extends AsyncTask<Void, Void, Void>{
        private CardDao cardDao;
        private RoomCallbackAsyncTask(CardDatabase db) {
            cardDao = db.cardDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            return null;
        }
    }
}
