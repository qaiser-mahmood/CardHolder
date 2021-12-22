package com.business.collector.wallet.cardholder.Activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.business.collector.wallet.cardholder.Adapter.AddEditAdapter;
import com.business.collector.wallet.cardholder.Helper.AdObserver;
import com.business.collector.wallet.cardholder.Helper.CardParcel;
import com.business.collector.wallet.cardholder.GlideApp;
import com.business.collector.wallet.cardholder.Model.CategoryClass;
import com.business.collector.wallet.cardholder.R;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.Line;
import com.google.android.gms.vision.text.Text;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.ArrayList;
import java.util.List;

import static com.business.collector.wallet.cardholder.Model.Constants.EXTRA_BUSINESS_CARD_PARCEL;
import static com.business.collector.wallet.cardholder.Model.Constants.EXTRA_BUSINESS_IMAGE_PATH;
import static com.business.collector.wallet.cardholder.Model.Constants.EXTRA_DATA;
import static com.business.collector.wallet.cardholder.Model.Constants.EXTRA_ID;
import static com.business.collector.wallet.cardholder.Model.Constants.EXTRA_TRANSLATION;

public class AddEditCardActivity extends AppCompatActivity {
    private Intent intent;
    private boolean previewImageLeft = false;
    private boolean previewImageRight = false;
    private List<String> categoryList = new ArrayList<>();
    private ArrayList<String> imagePathList = new ArrayList<>();
    private ArrayList<String> extraDataList = new ArrayList<>();
    private AddEditAdapter addEditAdapter;
    private String adString = null;
    private InterstitialAd interstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_card);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("EDIT");

        RecyclerView recyclerView = findViewById(R.id.recycler_view_addEditCard);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        intent = getIntent();
        CoordinatorLayout coordinatorLayout = findViewById(R.id.coordinator_layout_AddEditCard);
        AdView adView = findViewById(R.id.ad_view_addEditCard);
        getLifecycle().addObserver(new AdObserver(adView));
        interstitialAd = new InterstitialAd(this);
        interstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
        if(!interstitialAd.isLoading() && !interstitialAd.isLoaded()){
            interstitialAd.loadAd(new AdRequest.Builder().build());
        }

        extraDataList = intent.getStringArrayListExtra(EXTRA_DATA);

        assert extraDataList != null;
        for(String s : extraDataList){
            if(s != null)
                adString = s;
        }
        if(adString != null){
            coordinatorLayout.removeView(adView);
        }

        Bundle bundle = intent.getExtras();
        assert bundle != null;
        CardParcel cardParcel = bundle.getParcelable(EXTRA_BUSINESS_CARD_PARCEL);
        assert cardParcel != null;
        categoryList = cardParcel.getCategoryList();

        imagePathList = intent.getStringArrayListExtra(EXTRA_BUSINESS_IMAGE_PATH);

        final ImageView imageViewLeft = findViewById(R.id.image_view_card_imageLeft_AddEditCard);
        final ImageView imageViewRight = findViewById(R.id.image_view_card_imageRight_AddEditCard);
        TextView textViewAddPhotoLeft = findViewById(R.id.text_view_AddPhotoLeft_AddEditCard);
        TextView textViewAddPhotoRight = findViewById(R.id.text_view_AddPhotoRight_AddEditCard);

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

        List<CategoryClass> dataList = parcelToAdapterData(cardParcel);

        addEditAdapter = new AddEditAdapter(dataList);

        recyclerView.setAdapter(addEditAdapter);
        addEditAdapter.submitList(dataList);

        TextView textViewCancel = findViewById(R.id.text_view_Cancel_AddEditCard);
        textViewCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animateBlink(v);
                int id = intent.getIntExtra(EXTRA_ID, -1);
                if(id != -1){
                    Intent intent1 = new Intent(AddEditCardActivity.this, ViewCardActivity.class);
                    intent1.putExtras(intent);
                    startActivity(intent1);
                }else {
                    Intent intent1 = new Intent(AddEditCardActivity.this, MainActivity.class);
                    startActivity(intent1);
                }
                finish();
            }
        });

        imageViewLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animateBlink(v);
                previewImageLeft = true;
                previewImageRight = false;
                CropImage.startPickImageActivity(AddEditCardActivity.this); // Start the activity to choose image or take by camera
            }
        });

        imageViewRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animateBlink(v);
                previewImageLeft = false;
                previewImageRight = true;
                CropImage.startPickImageActivity(AddEditCardActivity.this); // Start the activity to choose image or take by camera
            }
        });

        textViewAddPhotoLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageViewLeft.callOnClick();
            }
        });

        textViewAddPhotoRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageViewRight.callOnClick();
            }
        });

        TextView textViewSave = findViewById(R.id.text_view_Save_AddEditCard);
        textViewSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animateBlink(v);
                saveCardData();
            }
        });
    } //onCreate ends

    private List<CategoryClass> parcelToAdapterData(CardParcel cardParcel){
        List<CategoryClass> categoryClassList = new ArrayList<>();
        for (String category : categoryList) {
            switch (category) {
                case "Name":
                    categoryClassList.addAll(stringToCatList(category, cardParcel.getNameList()));
                    break;
                case "Phone":
                    categoryClassList.addAll(stringToCatList(category, cardParcel.getPhoneList()));
                    break;
                case "Email":
                    categoryClassList.addAll(stringToCatList(category, cardParcel.getEmailList()));
                    break;
                case "Address":
                    categoryClassList.addAll(stringToCatList(category, cardParcel.getAddressList()));
                    break;
                default:
            }
        }
        return categoryClassList;
    }

    private List<CategoryClass> stringToCatList(String category, List<String> list){
        List<CategoryClass> categoryClassList = new ArrayList<>();
        for(int i=0; i<list.size(); i++){
            CategoryClass categoryClass = new CategoryClass(category, category, list.get(i));
            if(i == 0)
                categoryClass.setSelected(true);

            categoryClassList.add(categoryClass);
        }

        categoryClassList.add(new CategoryClass(category, "Add " + category, ""));

        return categoryClassList;
    }

    private void arrangeList(List<CategoryClass> list){
        int selectedIndex = -1;
        for(int i=0; i<list.size(); i++){
            if(list.get(i).isSelected()) {
                selectedIndex = i;
                break;
            }
        }
        if(selectedIndex != -1) {
            CategoryClass categoryClass = list.remove(selectedIndex);
            list.add(0, categoryClass);
        }
    }

    private CardParcel adapterDataToParcel(List<CategoryClass> categoryClassList){
        List<String> categories = new ArrayList<>();
        for(CategoryClass categoryClass : categoryClassList) {
            if(!categories.contains(categoryClass.getCategory()))
                categories.add(categoryClass.getCategory());
        }

        List<CategoryClass> nameCatList = new ArrayList<>();
        List<CategoryClass> phoneCatList = new ArrayList<>();
        List<CategoryClass> emailCatList = new ArrayList<>();
        List<CategoryClass> addressCatList = new ArrayList<>();
        for(CategoryClass categoryClass : categoryClassList){
            switch (categoryClass.getCategory()){
                case "Name":
                    if(!categoryClass.getValue().isEmpty())
                        nameCatList.add(categoryClass);
                    break;
                case "Phone":
                    if(!categoryClass.getValue().isEmpty())
                        phoneCatList.add(categoryClass);
                    break;
                case "Email":
                    if(!categoryClass.getValue().isEmpty())
                        emailCatList.add(categoryClass);
                    break;
                case "Address":
                    if(!categoryClass.getValue().isEmpty())
                        addressCatList.add(categoryClass);
                    break;
                default:
            }
        }

        arrangeList(nameCatList);
        arrangeList(phoneCatList);
        arrangeList(emailCatList);
        arrangeList(addressCatList);

        CardParcel cardParcel = new CardParcel(categories);

        for(CategoryClass c : nameCatList){
            cardParcel.addName(c.getValue());
        }

        for(CategoryClass c : phoneCatList){
            cardParcel.addPhone(c.getValue());
        }

        for(CategoryClass c : emailCatList){
            cardParcel.addEmail(c.getValue());
        }

        for(CategoryClass c : addressCatList){
            cardParcel.addAddress(c.getValue());
        }

        return cardParcel;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode != RESULT_OK) return;

        switch (requestCode){
            case CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE: {
                Uri imageUri = CropImage.getPickImageResultUri(this, data);

                //Start crop activity
                CropImage
                        .activity(imageUri)
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setMultiTouchEnabled(true)
                        .start(this);
            }
            break;
            case CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE: {
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                assert result != null;
                String imagePath = result.getUri().getPath();

                if (previewImageLeft) {
                    assert imagePath != null;
                    imagePathList.add(0, imagePath);

                    GlideApp
                            .with(this)
                            .load(imagePath)
                            .into((ImageView) findViewById(R.id.image_view_card_imageLeft_AddEditCard));
                }

                if (previewImageRight) {
                    assert imagePath != null;
                    imagePathList.add(1, imagePath);

                    GlideApp
                            .with(this)
                            .load(imagePath)
                            .into((ImageView) findViewById(R.id.image_view_card_imageRight_AddEditCard));
                }

                //Start translation
                TextRecognizer textRecognizer = new TextRecognizer.Builder(getApplicationContext()).build();
                if (!textRecognizer.isOperational())
                    Toast.makeText(this, "OCR is not available for this image", Toast.LENGTH_SHORT).show();
                else {
                    Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
                    Frame frame = new Frame.Builder().setBitmap(bitmap).build();
                    SparseArray<TextBlock> textBlocks = textRecognizer.detect(frame);

                    ArrayList<String> lineList = new ArrayList<>();

                    for (int i = 0; i < textBlocks.size(); i++) {
                        TextBlock textBlock = textBlocks.valueAt(i);
                        List<? extends Text> components = textBlock.getComponents();
                        for (Text component : components) {
                            String value = component.getValue();
                            if (component instanceof Line && value != null && !value.isEmpty())
                                lineList.add(value.trim());
                        }
                    }

                    //Start translationTable activity
                    CardParcel cardParcel = adapterDataToParcel(addEditAdapter.getCurrentList());
                    Bundle bundle = new Bundle();
                    bundle.putParcelable(EXTRA_BUSINESS_CARD_PARCEL, cardParcel);

                    final Intent translationIntent = new Intent(this, TranslationActivity.class);
                    translationIntent.putStringArrayListExtra(EXTRA_TRANSLATION, lineList);
                    translationIntent.putStringArrayListExtra(EXTRA_BUSINESS_IMAGE_PATH, imagePathList);
                    translationIntent.putStringArrayListExtra(EXTRA_DATA, extraDataList);
                    translationIntent.putExtras(bundle);
                    int id = intent.getIntExtra(EXTRA_ID, -1);
                    translationIntent.putExtra(EXTRA_ID, id);

                    if(adString != null)
                    {
                        startActivity(translationIntent);
                        finish();
                    }
                    else
                    {
                        if(interstitialAd.isLoaded())
                            interstitialAd.show();
                        else {
                            startActivity(translationIntent);
                            finish();
                        }
                        interstitialAd.setAdListener(new AdListener(){
                            @Override
                            public void onAdClosed() {
                                startActivity(translationIntent);
                                if(!interstitialAd.isLoading() && !interstitialAd.isLoaded()){
                                    interstitialAd.loadAd(new AdRequest.Builder().build());
                                }
                                finish();
                            }
                        });
                    }
                }
            }
            break;
            default:
        }
    }

    private void saveCardData(){
        CardParcel cardParcel = adapterDataToParcel(addEditAdapter.getCurrentList());
        Bundle bundle = new Bundle();
        cardParcel.cleanParcel();
        bundle.putParcelable(EXTRA_BUSINESS_CARD_PARCEL, cardParcel);
        Intent data = new Intent(AddEditCardActivity.this, MainActivity.class);
        data.putExtras(bundle);
        data.putStringArrayListExtra(EXTRA_BUSINESS_IMAGE_PATH, imagePathList);
        data.putStringArrayListExtra(EXTRA_DATA, extraDataList);

        int id = getIntent().getIntExtra(EXTRA_ID, -1);
        if(id != -1){
            data.putExtra(EXTRA_ID, id);
        }else {
            data.putExtra(EXTRA_ID, -2);
        }
        startActivity(data);
        finish();
    }

    private void animateBlink(View v){
        Animation animation = new AlphaAnimation(0.3f, 1.0f);
        animation.setDuration(700);
        v.startAnimation(animation);
    }
}
