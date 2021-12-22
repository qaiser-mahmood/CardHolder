package com.business.collector.wallet.cardholder.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.business.collector.wallet.cardholder.Adapter.TranslationAdapter;
import com.business.collector.wallet.cardholder.Helper.AdObserver;
import com.business.collector.wallet.cardholder.Helper.CardParcel;
import com.business.collector.wallet.cardholder.Helper.UndoRedoAction;
import com.business.collector.wallet.cardholder.Helper.Validator;
import com.business.collector.wallet.cardholder.R;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Stack;

import static com.business.collector.wallet.cardholder.Model.Constants.EXTRA_BUSINESS_CARD_PARCEL;
import static com.business.collector.wallet.cardholder.Model.Constants.EXTRA_BUSINESS_IMAGE_PATH;
import static com.business.collector.wallet.cardholder.Model.Constants.EXTRA_DATA;
import static com.business.collector.wallet.cardholder.Model.Constants.EXTRA_ID;
import static com.business.collector.wallet.cardholder.Model.Constants.EXTRA_TRANSLATION;
import static com.business.collector.wallet.cardholder.Model.Constants.FIRST_TIME_TRANS_VIEW;
import static com.business.collector.wallet.cardholder.Model.Constants.SHARED_PREFS;

public class TranslationActivity extends AppCompatActivity {

    private TranslationAdapter adapter;
    private Intent intent;
    private List<String> adapterData;
    private Stack<UndoRedoAction> undoStack = new Stack<>();
    private Intent copyIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_translation);

        setTitle("Translation");
        ActionBar actionBar = Objects.requireNonNull(getSupportActionBar());
        actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorYellow)));
        actionBar.setHomeAsUpIndicator(R.drawable.ic_close);

        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);

        intent = getIntent();
        CoordinatorLayout coordinatorLayout = findViewById(R.id.coordinator_layout_Translation);
        AdView adView = findViewById(R.id.ad_view_translation);
        getLifecycle().addObserver(new AdObserver(adView));

        List<String> stringList = intent.getStringArrayListExtra(EXTRA_DATA);
        String string = null;
        assert stringList != null;
        for(String s : stringList){
            if(s != null)
                string = s;
        }
        if(string != null){
            coordinatorLayout.removeView(adView);
        }

        boolean firstTimeView = sharedPreferences.getBoolean(FIRST_TIME_TRANS_VIEW, true);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(FIRST_TIME_TRANS_VIEW, false);
        editor.apply();

        if(firstTimeView)
            startActivity(new Intent(this, PopupActivity.class));

        intent = getIntent();
        copyIntent = new Intent(intent);
        List<String> lineList = intent.getStringArrayListExtra(EXTRA_TRANSLATION);

        RecyclerView recyclerViewTranslation = findViewById(R.id.recycler_view_Translation);
        recyclerViewTranslation.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewTranslation.setHasFixedSize(true);

        assert lineList != null;
        adapterData = lineListToAdapterData(lineList);
        adapter = new TranslationAdapter(this);
        recyclerViewTranslation.setAdapter(adapter);
        adapter.submitList(adapterData);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.START | ItemTouchHelper.END, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                int fromPosition = viewHolder.getAdapterPosition();
                int toPosition = target.getAdapterPosition();

                Collections.swap(adapterData, fromPosition, toPosition);
                adapter.notifyItemMoved(fromPosition, toPosition);

                return false;
            }

            @Override
            public float getSwipeThreshold(@NonNull RecyclerView.ViewHolder viewHolder) {
                return 0.5f;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                if(direction == ItemTouchHelper.LEFT) {
                    int position = viewHolder.getAdapterPosition();
                    String text = adapter.getTextAt(position);
                    undoStack.push(new UndoRedoAction(position, text));
                    adapterData.remove(text);
                    adapter.notifyItemRemoved(viewHolder.getAdapterPosition());
                    adapter.notifyItemRangeChanged(viewHolder.getAdapterPosition(), adapterData.size());
                }else if(direction == ItemTouchHelper.RIGHT) {
                    int position = viewHolder.getAdapterPosition();
                    String belowText = adapter.getTextAt(position);
                    String topText = adapter.getTextAt(position - 1); //One position above the swiped item
                    String newItem = "";
                    if(position - 1 == 0 || topText.equals("Address:") || topText.equals("Phone:") || topText.equals("Email:")) {
                        adapterData.set(position, belowText);
                        adapter.notifyItemChanged(position);
                    }else{
                        undoStack.push(new UndoRedoAction(position, topText, belowText));
                        newItem = topText + " " + belowText;
                        adapterData.set(position - 1, newItem);
                        adapterData.remove(position);

                        adapter.notifyItemChanged(position - 1);
                        adapter.notifyItemRemoved(position);
                        adapter.notifyItemRangeChanged(position, adapterData.size());
                    }
                }
            }

            @Override
            public int getSwipeDirs(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                int position = viewHolder.getAdapterPosition();
                return createSwipeFlags(position, recyclerView, viewHolder);
            }

            private int createSwipeFlags(int position, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder){
                String currentItem = adapter.getTextAt(position).trim();
                if(position == 0 || currentItem.equals("Address:") || currentItem.equals("Phone:") || currentItem.equals("Email:"))
                    return 0;
                else
                    return super.getSwipeDirs(recyclerView, viewHolder);
            }

        }).attachToRecyclerView(recyclerViewTranslation);

    } //onCreate ends

    private List<String> lineListToAdapterData(List<String> lineList){

        List<String> nameList = new ArrayList<>();
        List<String> phoneList = new ArrayList<>();
        List<String> emailList = new ArrayList<>();
        List<String> addressList = new ArrayList<>();
        Validator validator = new Validator();
        for(String line : lineList){
            if(validator.validateEmail(line)){
                emailList.add(line);
            }else if(validator.validatePhone(line)){
                phoneList.add(line);
            }else if(validator.validateAddress(line)){
                addressList.add(line);
            }else {
                if(line.trim().length() > 1){
                    nameList.add(line);
                }
            }
        }

        List<String> adapterData = new ArrayList<>();
        adapterData.add("Name:");
        adapterData.addAll(nameList);
        adapterData.add("Address:");
        adapterData.addAll(addressList);
        adapterData.add("Phone:");
        adapterData.addAll(phoneList);
        adapterData.add("Email:");
        adapterData.addAll(emailList);

        return adapterData;
    }

    private CardParcel adapterDataToParcel(){
        Bundle bundle = intent.getExtras();
        assert bundle != null;
        CardParcel oldParcel = bundle.getParcelable(EXTRA_BUSINESS_CARD_PARCEL);

        assert oldParcel != null;
        CardParcel newParcel = new CardParcel(oldParcel.getCategoryList());
        List<String> adapterData = adapter.getCurrentList();

        //Append old and translated data to card parcel
        newParcel.addNameList(oldParcel.getNameList());
        newParcel.addNameList(adapterData.subList(adapterData.indexOf("Name:")+1, adapterData.indexOf("Address:")));

        newParcel.addAddressList(oldParcel.getAddressList());
        newParcel.addAddressList(adapterData.subList(adapterData.indexOf("Address:")+1, adapterData.indexOf("Phone:")));

        newParcel.addPhoneList(oldParcel.getPhoneList());
        newParcel.addPhoneList(adapterData.subList(adapterData.indexOf("Phone:")+1, adapterData.indexOf("Email:")));

        newParcel.addEmailList(oldParcel.getEmailList());
        newParcel.addEmailList(adapterData.subList(adapterData.indexOf("Email:")+1, adapterData.size()));

        newParcel.cleanParcel();

        return newParcel;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.translation_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.translation_edit_done) {
            ArrayList<String> imagePathList = intent.getStringArrayListExtra(EXTRA_BUSINESS_IMAGE_PATH);
            int extraId = intent.getIntExtra(EXTRA_ID, -1);
            CardParcel cardParcel = adapterDataToParcel();
            Bundle bundle = new Bundle();
            bundle.putParcelable(EXTRA_BUSINESS_CARD_PARCEL, cardParcel);

            Intent result = new Intent(this, AddEditCardActivity.class);
            result.putExtras(bundle);
            result.putStringArrayListExtra(EXTRA_BUSINESS_IMAGE_PATH, imagePathList);
            ArrayList<String> arrayList = copyIntent.getStringArrayListExtra(EXTRA_DATA);
            result.putStringArrayListExtra(EXTRA_DATA, arrayList);
            result.putExtra(EXTRA_ID, extraId);
            startActivity(result);
            finish();
        }else if(item.getItemId() == R.id.translation_edit_undo){
            undoAction();
        }else if(item.getItemId() == R.id.translation_guide){
            startActivity(new Intent(this, PopupActivity.class));
        }else if(item.getItemId() == android.R.id.home){
            Intent addEditIntent = new Intent(this, AddEditCardActivity.class);
            addEditIntent.putExtras(copyIntent);
            startActivity(addEditIntent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void undoAction(){
        if(undoStack.isEmpty()) return;

        UndoRedoAction action = undoStack.pop();
        int position = action.getPosition();
        String topText = action.getTextTop();
        String belowText = action.getTextBelow();
        if(belowText.isEmpty()){ //last action was delete. So insert item to undo the delete
            adapterData.add(position, topText);
            adapter.notifyItemInserted(position);
            adapter.notifyItemRangeChanged(position, adapterData.size());
        }else { //last action was concatenation. So decompose items to Undo concatenation action
            adapterData.set(position-1, topText);
            adapter.notifyItemChanged(position-1);

            adapterData.add(position, belowText);
            adapter.notifyItemInserted(position);
            adapter.notifyItemRangeChanged(position, adapterData.size());
        }
    }
}
