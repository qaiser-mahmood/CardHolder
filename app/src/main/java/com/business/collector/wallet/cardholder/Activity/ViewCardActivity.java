package com.business.collector.wallet.cardholder.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.business.collector.wallet.cardholder.Helper.AdObserver;
import com.business.collector.wallet.cardholder.Helper.CardParcel;
import com.business.collector.wallet.cardholder.GlideApp;
import com.business.collector.wallet.cardholder.Helper.EmailClass;
import com.business.collector.wallet.cardholder.Helper.MessageClass;
import com.business.collector.wallet.cardholder.Helper.NavigationClass;
import com.business.collector.wallet.cardholder.Helper.PhoneClass;
import com.business.collector.wallet.cardholder.Helper.Utility;
import com.business.collector.wallet.cardholder.R;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.business.collector.wallet.cardholder.Model.Constants.EXTRA_BUSINESS_CARD_PARCEL;
import static com.business.collector.wallet.cardholder.Model.Constants.EXTRA_BUSINESS_IMAGE_PATH;
import static com.business.collector.wallet.cardholder.Model.Constants.EXTRA_DATA;

public class ViewCardActivity extends AppCompatActivity {

    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_card);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        RecyclerView recyclerView = findViewById(R.id.recycler_view_ViewCard);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        intent = getIntent();
        CoordinatorLayout coordinatorLayout = findViewById(R.id.coordinator_layout_ViewCard);
        AdView adView = findViewById(R.id.ad_view_viewCard);
        getLifecycle().addObserver(new AdObserver(adView));

        Bundle bundle = intent.getExtras();
        assert bundle != null;
        CardParcel cardParcel = bundle.getParcelable(EXTRA_BUSINESS_CARD_PARCEL);
        assert cardParcel != null;
        cardParcel.cleanParcel();
        final List<String> nameList = cardParcel.getNameList();
        List<String> phoneList = cardParcel.getPhoneList();
        List<String> emailList = cardParcel.getEmailList();
        List<String> addressList = cardParcel.getAddressList();
        List<String> imagePathList = intent.getStringArrayListExtra(EXTRA_BUSINESS_IMAGE_PATH);
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

        final List<String> cardData = new ArrayList<>();
        cardData.addAll(cardData.size(), nameList); cardData.add(cardData.size(), "");
        cardData.addAll(cardData.size(), phoneList); cardData.add(cardData.size(), "");
        cardData.addAll(cardData.size(), emailList); cardData.add(cardData.size(), "");
        cardData.addAll(cardData.size(), addressList);

        final Map<String, List<String>> cardMap = new HashMap<>();
        cardMap.put("Name", nameList); cardMap.put("Phone", phoneList);
        cardMap.put("Email", emailList); cardMap.put("Address", addressList);

        String bName = nameList.size() > 0 ? nameList.get(0) : "Business Name";
        setTitle(bName);

        ImageView imageViewLeft = findViewById(R.id.image_view_card_imageLeft_ViewCard);
        ImageView imageViewRight = findViewById(R.id.image_view_card_imageRight_ViewCard);
        assert imagePathList != null;
        String imagePathLeft = imagePathList.size() > 0 ? imagePathList.get(0) : "";
        String imagePathRight = imagePathList.size() > 1 ? imagePathList.get(1) : "";

        GlideApp
                .with(this)
                .load(imagePathLeft)
                .placeholder(R.drawable.ic_credit_card)
                .into(imageViewLeft);

        GlideApp
                .with(this)
                .load(imagePathRight)
                .placeholder(R.drawable.ic_credit_card)
                .into(imageViewRight);

        class StringViewHolder extends RecyclerView.ViewHolder{
            TextView textView1;
            TextView textView2;

            public StringViewHolder(@NonNull View itemView) {
                super(itemView);
                textView1 = itemView.findViewById(R.id.text_view_text1_ViewCard);
                textView2 = itemView.findViewById(R.id.text_view_text2_ViewCard);
            }
        }

        RecyclerView.Adapter adapter = new RecyclerView.Adapter<StringViewHolder>(){

            @NonNull
            @Override
            public StringViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
                View view = layoutInflater.inflate(R.layout.list_item_viewcard, parent, false);
                return new StringViewHolder(view);
            }

            @Override
            public void onBindViewHolder(@NonNull StringViewHolder holder, int position) {
                String value = cardData.get(position);
                for(Map.Entry<String, List<String>> entry : cardMap.entrySet()){
                    List<String> valueList = entry.getValue();
                    if(valueList.contains(value)){
                        holder.textView1.setVisibility(View.GONE);
                        holder.textView2.setText(value);

                        switch (entry.getKey()){
                            case "Phone":{
                                new PhoneClass(ViewCardActivity.this, holder.itemView, holder.textView2.getText().toString()).click();
                                break;
                            }
                            case "Email":{
                                String[] recipients = new String[]{holder.textView2.getText().toString()};
                                String recipientName = nameList.size() > 0 ? nameList.get(0) : "";
                                new EmailClass(ViewCardActivity.this, holder.itemView, recipients, recipientName).click();
                                break;
                            }
                            case "Address":{
                                new NavigationClass(ViewCardActivity.this, holder.itemView, holder.textView2.getText().toString()).click();
                                break;
                            }
                            default:
                                new Utility(ViewCardActivity.this, holder.itemView).click();
                        }
                    }
                }

                if(value.isEmpty()){
                    if(position == Objects.requireNonNull(cardMap.get("Name")).size()) {
                        holder.textView1.setText(getResources().getText(R.string.phone));
                    }
                    else if(position == Objects.requireNonNull(cardMap.get("Name")).size() +
                            Objects.requireNonNull(cardMap.get("Phone")).size() + 1) {

                        holder.textView1.setText(getResources().getText(R.string.email));
                    }
                    else if(position == Objects.requireNonNull(cardMap.get("Name")).size() +
                            Objects.requireNonNull(cardMap.get("Phone")).size() +
                            Objects.requireNonNull(cardMap.get("Email")).size() + 2) {

                        holder.textView1.setText(getResources().getText(R.string.address));
                    }

                    holder.textView1.setBackgroundColor(getResources().getColor(R.color.colorYellow));
                    holder.textView2.setVisibility(View.GONE);
                }
                else {
                    holder.textView2.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public int getItemCount() {
                return cardData.size();
            }
        };

        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        TextView textViewBack = findViewById(R.id.text_view_Back_ViewCard);
        textViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animateBlink(v);
                Intent intent1 = new Intent(ViewCardActivity.this, MainActivity.class);
                startActivity(intent1);
                finish();
            }
        });

        TextView textViewEdit = findViewById(R.id.text_view_Edit_ViewCard);
        textViewEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animateBlink(v);
                Intent editIntent = new Intent(ViewCardActivity.this, AddEditCardActivity.class);
                editIntent.putExtras(intent);
                startActivity(editIntent);
//                finish();
            }
        });

        ImageButton imageButtonPhone = findViewById(R.id.image_button_phone_ViewCard);
        ImageButton imageButtonMessage = findViewById(R.id.image_button_message_ViewCard);
        ImageButton imageButtonEmail = findViewById(R.id.image_button_email_ViewCard);
        ImageButton imageButtonNavigate = findViewById(R.id.image_button_navigate_ViewCard);

        String bPhone = phoneList.size() > 0 ? phoneList.get(0) : "";
        String bAddress = addressList.size() > 0 ? addressList.get(0) : "";

        new EmailClass(this, imageButtonEmail, emailList.toArray(new String[0]), bName).click();
        new PhoneClass(this, imageButtonPhone, bPhone).click();
        new MessageClass(this, imageButtonMessage, bPhone, bName).click();
        new NavigationClass(this, imageButtonNavigate, bAddress).click();
    }

    private void animateBlink(View v){
        Animation animation = new AlphaAnimation(0.3f, 1.0f);
        animation.setDuration(700);
        v.startAnimation(animation);
    }
}
