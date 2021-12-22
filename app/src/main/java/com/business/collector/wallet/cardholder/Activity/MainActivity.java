package com.business.collector.wallet.cardholder.Activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.PermissionChecker;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.billingclient.api.AcknowledgePurchaseParams;
import com.android.billingclient.api.AcknowledgePurchaseResponseListener;
import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.SkuDetails;
import com.android.billingclient.api.SkuDetailsParams;
import com.android.billingclient.api.SkuDetailsResponseListener;
import com.business.collector.wallet.cardholder.Adapter.CardAdapter;
import com.business.collector.wallet.cardholder.BuildConfig;
import com.business.collector.wallet.cardholder.CardViewModel;
import com.business.collector.wallet.cardholder.Fragment.AdPlansFragment;
import com.business.collector.wallet.cardholder.Fragment.AppRaterFragment;
import com.business.collector.wallet.cardholder.Fragment.PremiumVersionFragment;
import com.business.collector.wallet.cardholder.Fragment.UnlimitedCardFragment;
import com.business.collector.wallet.cardholder.Helper.AdObserver;
import com.business.collector.wallet.cardholder.Helper.CardParcel;
import com.business.collector.wallet.cardholder.Helper.MySwipeHelper;
import com.business.collector.wallet.cardholder.Interface.MyButtonClickListener;
import com.business.collector.wallet.cardholder.Interface.OnItemClickListener;
import com.business.collector.wallet.cardholder.Interface.PurchaseCallbackInterface;
import com.business.collector.wallet.cardholder.Model.Card;
import com.business.collector.wallet.cardholder.R;
import com.google.android.gms.ads.AdView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static com.business.collector.wallet.cardholder.Model.Constants.APP_LAUNCH_COUNT;
import static com.business.collector.wallet.cardholder.Model.Constants.DATE_MAYBE_LATER_SHOW_DIALOG;
import static com.business.collector.wallet.cardholder.Model.Constants.DATE_NEXT_SHOW_DIALOG;
import static com.business.collector.wallet.cardholder.Model.Constants.DONT_SHOW_AGAIN;
import static com.business.collector.wallet.cardholder.Model.Constants.EXTRA_BUSINESS_CARD_PARCEL;
import static com.business.collector.wallet.cardholder.Model.Constants.EXTRA_BUSINESS_IMAGE_PATH;
import static com.business.collector.wallet.cardholder.Model.Constants.EXTRA_DATA;
import static com.business.collector.wallet.cardholder.Model.Constants.EXTRA_ID;
import static com.business.collector.wallet.cardholder.Model.Constants.FIRST_TIME_APP_VIEW;
import static com.business.collector.wallet.cardholder.Model.Constants.LAUNCHES_UNTIL_PROMPT;
import static com.business.collector.wallet.cardholder.Model.Constants.PERMISSION_REQUEST_CODE;
import static com.business.collector.wallet.cardholder.Model.Constants.SHARED_PREFS;
import static com.business.collector.wallet.cardholder.Model.Constants.SKU_ITEM_AD_FREE_1_MONTH_SUB;
import static com.business.collector.wallet.cardholder.Model.Constants.SKU_ITEM_AD_FREE_1_YEAR_SUB;
import static com.business.collector.wallet.cardholder.Model.Constants.SKU_ITEM_AD_FREE_3_MONTH_SUB;
import static com.business.collector.wallet.cardholder.Model.Constants.SKU_ITEM_AD_FREE_6_MONTH_SUB;
import static com.business.collector.wallet.cardholder.Model.Constants.SKU_ITEM_PREMIUM_VERSION;
import static com.business.collector.wallet.cardholder.Model.Constants.SKU_ITEM_UNLIMITED_CARD_TIER;
import static com.business.collector.wallet.cardholder.Model.Constants.SPLIT_TAG;

public class MainActivity extends AppCompatActivity implements LifecycleOwner, NavigationView.OnNavigationItemSelectedListener, PurchasesUpdatedListener, PurchaseCallbackInterface {

    private BillingClient billingClient;
    private SkuDetailsParams inAppSkuDetailsParams, subSkuDetailsParams;
    private List<SkuDetails> subSkuDetailsList;
    private List<SkuDetails> inAppSkuDetailsList;
    private String monthlyPlanPrice, quarterlyPlanPrice, halfYearlyPlanPrice, yearlyPlanPrice, cardTierUnlimitedPrice, premiumVersionPrice;
    private String monthlyPlan, quarterlyPlan, halfYearlyPlan, yearlyPlan, premiumVersion, unlimitedCardTier;
    private CoordinatorLayout coordinatorLayout;
    private AdView adView;
    private SharedPreferences sharedPreferences;

    private String[] permissions = new String[]
            {Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
            Manifest.permission.CALL_PHONE,
            Manifest.permission.INTERNET,
            Manifest.permission.ACCESS_NETWORK_STATE};

    private CardViewModel cardViewModel;
    private CardAdapter adapter;
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            requestPermissions(permissions, PERMISSION_REQUEST_CODE);
        }else {
            boolean permissionGranted = false;
            for(String permission : permissions){
                int permissionCheck = ContextCompat.checkSelfPermission(this, permission);
                permissionGranted = permissionCheck == PermissionChecker.PERMISSION_GRANTED;
            }
            if(permissionGranted){
                sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                boolean firstTimeView = sharedPreferences.getBoolean(FIRST_TIME_APP_VIEW, true);
                editor.putBoolean(FIRST_TIME_APP_VIEW, false);
                editor.apply();

                if(firstTimeView) {
                    Intent intent = new Intent(this, IntroActivity.class);
                    startActivity(intent);
                }
            }
        }

        Toolbar toolbar = findViewById(R.id.navigation_toolbar);
        setSupportActionBar(toolbar);

        coordinatorLayout = findViewById(R.id.coordinator_layout_MainActivity);
        adView = findViewById(R.id.ad_view_mainActivity);
        getLifecycle().addObserver(new AdObserver(adView));

        final ActionBar actionBar = Objects.requireNonNull(getSupportActionBar());
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorYellow)));
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setCustomView(R.layout.search_view_layout);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);

        SearchView searchView = findViewById(R.id.mySearchView);
        searchView.setIconifiedByDefault(false);

        //To change the color of hint text of search view
        //courtesy to https://stackoverflow.com/questions/17791388/changing-action-bar-searchview-hint-text-color
        LinearLayout ll = (LinearLayout)searchView.getChildAt(0);
        LinearLayout ll2 = (LinearLayout)ll.getChildAt(2);
        LinearLayout ll3 = (LinearLayout)ll2.getChildAt(1);
        SearchView.SearchAutoComplete autoComplete = (SearchView.SearchAutoComplete)ll3.getChildAt(0);
        autoComplete.setHintTextColor(getResources().getColor(R.color.colorGray));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });

        drawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                assert inputMethodManager != null;
                View currentFocusedView = MainActivity.this.getCurrentFocus();
                if(currentFocusedView != null)
                    inputMethodManager.hideSoftInputFromWindow(currentFocusedView.getWindowToken(), 0);
            }
        };
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();

        NavigationView navigationView = findViewById(R.id.navigation_menu);
        navigationView.setItemIconTintList(null); //To show color of icons in navigation menu
        navigationView.getMenu().getItem(4).setActionView(R.layout.remove_ad_layout);
        navigationView.setNavigationItemSelectedListener(this);

        //Billing client code
        billingClient = BillingClient.newBuilder(MainActivity.this).enablePendingPurchases().setListener(this).build();
        billingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingSetupFinished(BillingResult billingResult) {
                if (billingResult.getResponseCode() ==  BillingClient.BillingResponseCode.OK)
                {
                    Purchase.PurchasesResult inAppPurchaseResult = billingClient.queryPurchases(BillingClient.SkuType.INAPP);
                    Purchase.PurchasesResult subPurchaseResult = billingClient.queryPurchases(BillingClient.SkuType.SUBS);
                    onPurchasesUpdated(inAppPurchaseResult.getBillingResult(), inAppPurchaseResult.getPurchasesList());
                    onPurchasesUpdated(subPurchaseResult.getBillingResult(), subPurchaseResult.getPurchasesList());
                }
            }

            @Override
            public void onBillingServiceDisconnected() { }
        });

        final RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        cardViewModel = new ViewModelProvider
                .AndroidViewModelFactory(getApplication())
                .create(CardViewModel.class);

        adapter = new CardAdapter(this);
        recyclerView.setAdapter(adapter);

        final TextView textViewCardCount = findViewById(R.id.card_count_toolBar);
        FloatingActionButton buttonAddCard = findViewById(R.id.button_add_card);
        buttonAddCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if((premiumVersion != null && premiumVersion.equals(SKU_ITEM_PREMIUM_VERSION))
                        || (unlimitedCardTier != null && unlimitedCardTier.equals(SKU_ITEM_UNLIMITED_CARD_TIER))
                        || adapter.getItemCount() < BuildConfig.BUILD_VALUE) {
                    List<String> categoryList = new ArrayList<>();
                    categoryList.add("Name");
                    categoryList.add("Phone");
                    categoryList.add("Email");
                    categoryList.add("Address");
                    CardParcel cardParcel = new CardParcel(categoryList);
                    Bundle bundle = new Bundle();
                    bundle.putParcelable(EXTRA_BUSINESS_CARD_PARCEL, cardParcel);
                    ArrayList<String> imagePathList = new ArrayList<>();

                    Intent intent = new Intent(MainActivity.this, AddEditCardActivity.class);
                    intent.putExtras(bundle);
                    intent.putStringArrayListExtra(EXTRA_BUSINESS_IMAGE_PATH, imagePathList);
                    ArrayList<String> stringArrayList = new ArrayList<>(Arrays.asList(monthlyPlan, quarterlyPlan, halfYearlyPlan, yearlyPlan, premiumVersion));
                    intent.putStringArrayListExtra(EXTRA_DATA, stringArrayList);
                    startActivity(intent);
                } else {
                    textViewCardCount.setBackgroundColor(getResources().getColor(R.color.colorRed));
                    textViewCardCount.setTextColor(getResources().getColor(R.color.colorBlack));
                    Toast.makeText(MainActivity.this, "Please upgrade or\nDelete some card to add more", Toast.LENGTH_SHORT).show();
                }
            }
        });

        cardViewModel.getAllCards().observe(this, new Observer<List<Card>>() {
            @Override
            public void onChanged(List<Card> cards) {
                adapter.setCardListFull(cards);
                adapter.submitList(cards);
                textViewCardCount.setText(String.valueOf(cards.size()));
            }
        });

        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(Card card) {
                Bundle bundle = new Bundle();
                bundle.putParcelable(EXTRA_BUSINESS_CARD_PARCEL, cardToParcel(card));
                ArrayList<String> imagePathList = new ArrayList<>(Arrays.asList(card.getImagePath().split(SPLIT_TAG)));
                Intent intent = new Intent(MainActivity.this, ViewCardActivity.class);
                intent.putExtras(bundle);
                intent.putStringArrayListExtra(EXTRA_BUSINESS_IMAGE_PATH, imagePathList);
                intent.putExtra(EXTRA_ID, card.getId());
                ArrayList<String> stringArrayList = new ArrayList<>(Arrays.asList(monthlyPlan, quarterlyPlan, halfYearlyPlan, yearlyPlan, premiumVersion));
                intent.putStringArrayListExtra(EXTRA_DATA, stringArrayList);
                startActivity(intent);
            }
        });

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int buttonWidth = displayMetrics.widthPixels / 16;
        final int buttonTextSize = displayMetrics.widthPixels / 24;
        final String swipeBackgroundColor = "#" + Integer.toHexString(getResources().getColor((R.color.colorLightYellow)));

        new MySwipeHelper(this, recyclerView, buttonWidth) {
            @Override
            protected void instantiateMyButton(RecyclerView.ViewHolder viewHolder, List<MyButton> buffer) {
                buffer.add(new MyButton(MainActivity.this,
                        "TEXT",
                        buttonTextSize,
                        R.drawable.ic_message,
                        Color.parseColor(swipeBackgroundColor),
                        true,
                        new MyButtonClickListener() {
                            @Override
                            public void onClick(int pos) {
                                String subject = adapter.getCardAt(pos).getBusinessName().split(SPLIT_TAG)[0];
                                StringBuilder phoneNumber = new StringBuilder();
                                char[] chars = adapter.getCardAt(pos).getBusinessPhone().split(SPLIT_TAG)[0].toCharArray();
                                for(char c : chars){
                                    if(Character.isDigit(c))
                                        phoneNumber.append(c);
                                }
                                String dial = "sms:" + phoneNumber;
                                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(dial));
                                intent.putExtra("sms_body", subject + "\n");
                                startActivity(intent);
                            }
                        }));

                buffer.add(new MyButton(MainActivity.this,
                        "NAV",
                        buttonTextSize,
                        R.drawable.ic_navigation,
                        Color.parseColor(swipeBackgroundColor),
                        true,
                        new MyButtonClickListener() {
                            @Override
                            public void onClick(int pos) {
                                String address = adapter.getCardAt(pos).getBusinessAddress().split(SPLIT_TAG)[0];
                                Uri mapUri = Uri.parse("geo:0,0?z=10&q=" + address);
                                Intent mapIntent = new Intent(Intent.ACTION_VIEW, mapUri);
                                startActivity(Intent.createChooser(mapIntent, "Choose map"));
                            }
                        }));

                buffer.add(new MyButton(MainActivity.this,
                        "CALL",
                        buttonTextSize,
                        R.drawable.ic_phone,
                        Color.parseColor(swipeBackgroundColor),
                        true,
                        new MyButtonClickListener() {
                            @Override
                            public void onClick(int pos) {
                                if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                    return;
                                }
                                StringBuilder phoneNumber = new StringBuilder();
                                char[] chars = adapter.getCardAt(pos).getBusinessPhone().split(SPLIT_TAG)[0].toCharArray();
                                for(char c : chars){
                                    if(Character.isDigit(c))
                                        phoneNumber.append(c);
                                }

                                String dial = "tel:" + phoneNumber.toString();
                                startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
                            }
                        }));

                buffer.add(new MyButton(MainActivity.this,
                        "MAIL",
                        buttonTextSize,
                        R.drawable.ic_email,
                        Color.parseColor(swipeBackgroundColor),
                        true,
                        new MyButtonClickListener() {
                            @Override
                            public void onClick(int pos) {
                                Card card = adapter.getCardAt(pos);
                                String[] emailArray = card.getBusinessEmail().split(SPLIT_TAG);
                                String emailSubject = card.getBusinessName().split(SPLIT_TAG)[0];
                                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                                emailIntent.setType("message/rfc822");
                                emailIntent.putExtra(Intent.EXTRA_EMAIL, emailArray);
                                emailIntent.putExtra(Intent.EXTRA_SUBJECT, emailSubject);
                                startActivity(Intent.createChooser(emailIntent, "Choose an email client"));
                            }
                        }));

                buffer.add(new MyButton(MainActivity.this,
                        "Delete",
                        buttonTextSize,
                        R.drawable.ic_delete_forever,
                        Color.parseColor(swipeBackgroundColor),
                        false,
                        new MyButtonClickListener() {
                            @Override
                            public void onClick(final int pos) {
                                final AlertDialog dialog = new AlertDialog.Builder(MainActivity.this)
                                        .setTitle("Delete Card")
                                        .setMessage("Do you want to delete card?")
                                        .setNegativeButton("No", null)
                                        .setPositiveButton("Yes", null)
                                        .show();

                                Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                                positiveButton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialog.dismiss();
                                        cardViewModel.delete(adapter.getCardAt(pos));
                                        Toast.makeText(MainActivity.this, "Card deleted", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }));
            }
        };

        Intent data = getIntent();
        int id = data.getIntExtra(EXTRA_ID, -1);
        if(id != -1){
            saveCardToDatabase(data, id);
            finish();
        }
    } //onCreate ends

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == PERMISSION_REQUEST_CODE && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_DENIED)
        {
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            {
                requestPermissions(permissions, PERMISSION_REQUEST_CODE);
            }
        }
        else if(requestCode == PERMISSION_REQUEST_CODE && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
        {
            sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            boolean firstTimeView = sharedPreferences.getBoolean(FIRST_TIME_APP_VIEW, true);
            editor.putBoolean(FIRST_TIME_APP_VIEW, false);
            editor.apply();

            if(firstTimeView) {
                Intent intent = new Intent(this, IntroActivity.class);
                startActivity(intent);
            }
        }
    }

    private void setupBilling(){
        billingClient = BillingClient.newBuilder(MainActivity.this).enablePendingPurchases().setListener(this).build();
        billingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingSetupFinished(BillingResult billingResult) {
                if (billingResult.getResponseCode() ==  BillingClient.BillingResponseCode.OK) {
                    Purchase.PurchasesResult inAppPurchaseResult = billingClient.queryPurchases(BillingClient.SkuType.INAPP);
                    Purchase.PurchasesResult subPurchaseResult = billingClient.queryPurchases(BillingClient.SkuType.SUBS);
                    onPurchasesUpdated(inAppPurchaseResult.getBillingResult(), inAppPurchaseResult.getPurchasesList());
                    onPurchasesUpdated(subPurchaseResult.getBillingResult(), subPurchaseResult.getPurchasesList());

                    subSkuDetailsParams = SkuDetailsParams.newBuilder()
                            .setSkusList(Arrays.asList(SKU_ITEM_AD_FREE_1_MONTH_SUB, SKU_ITEM_AD_FREE_3_MONTH_SUB, SKU_ITEM_AD_FREE_6_MONTH_SUB, SKU_ITEM_AD_FREE_1_YEAR_SUB))
                            .setType(BillingClient.SkuType.SUBS)
                            .build();

                    inAppSkuDetailsParams = SkuDetailsParams.newBuilder()
                            .setSkusList(Arrays.asList(SKU_ITEM_UNLIMITED_CARD_TIER, SKU_ITEM_PREMIUM_VERSION))
                            .setType(BillingClient.SkuType.INAPP)
                            .build();

                    billingClient.querySkuDetailsAsync(subSkuDetailsParams, new SkuDetailsResponseListener() {
                        @Override
                        public void onSkuDetailsResponse(BillingResult billingResult, List<SkuDetails> list) {
                            if(billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK){
                                subSkuDetailsList = list;
                                for(SkuDetails skuDetails : subSkuDetailsList){
                                    if(skuDetails.getSku().equals(SKU_ITEM_AD_FREE_1_MONTH_SUB))
                                        monthlyPlanPrice = skuDetails.getPrice();
                                    else if(skuDetails.getSku().equals(SKU_ITEM_AD_FREE_3_MONTH_SUB))
                                        quarterlyPlanPrice = skuDetails.getPrice();
                                    else if(skuDetails.getSku().equals(SKU_ITEM_AD_FREE_6_MONTH_SUB))
                                        halfYearlyPlanPrice = skuDetails.getPrice();
                                    else if(skuDetails.getSku().equals(SKU_ITEM_AD_FREE_1_YEAR_SUB))
                                        yearlyPlanPrice = skuDetails.getPrice();
                                }
                            }
                        }
                    });

                    billingClient.querySkuDetailsAsync(inAppSkuDetailsParams, new SkuDetailsResponseListener() {
                        @Override
                        public void onSkuDetailsResponse(BillingResult billingResult, List<SkuDetails> list) {
                            if(billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK){
                                inAppSkuDetailsList = list;
                                for(SkuDetails skuDetails : inAppSkuDetailsList){
                                    if(skuDetails.getSku().equals(SKU_ITEM_UNLIMITED_CARD_TIER))
                                        cardTierUnlimitedPrice = skuDetails.getPrice();
                                    else if(skuDetails.getSku().equals(SKU_ITEM_PREMIUM_VERSION))
                                        premiumVersionPrice = skuDetails.getPrice();
                                }
                            }
                        }
                    });
                }
            }

            @Override
            public void onBillingServiceDisconnected() { }
        });
    }

    @Override
    protected void onPause() {
        if(billingClient != null)
            billingClient.endConnection();
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setupBilling();
    }

    @Override
    protected void onDestroy() {
        if(billingClient != null)
            billingClient.endConnection();
        super.onDestroy();
    }

    @Override
    public void onPurchasesUpdated(BillingResult billingResult, @Nullable List<Purchase> list) {
        if(billingResult.getResponseCode() == BillingClient.BillingResponseCode.USER_CANCELED){
            Toast.makeText(this, "Purchase cancelled", Toast.LENGTH_SHORT).show();
        }
        else if(list != null && !list.isEmpty() && (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK || billingResult.getResponseCode() == BillingClient.BillingResponseCode.ITEM_ALREADY_OWNED))
        {
            for (Purchase purchase : list) {
                handlePurchase(purchase);
            }
        }
    }

    private void handlePurchase(final Purchase purchase) {
        if(purchase.getPurchaseState() == Purchase.PurchaseState.PURCHASED){
            if(purchase.getSku().equals(SKU_ITEM_UNLIMITED_CARD_TIER))
            {
                unlimitedCardTier = SKU_ITEM_UNLIMITED_CARD_TIER;

            }
            else if(purchase.getSku().equals(SKU_ITEM_PREMIUM_VERSION))
            {
                premiumVersion = SKU_ITEM_PREMIUM_VERSION;
                monthlyPlan = null;
                quarterlyPlan = null;
                coordinatorLayout.removeView(adView);
                halfYearlyPlan = null;
                yearlyPlan = null;
            }
            else if(purchase.getSku().equals(SKU_ITEM_AD_FREE_1_MONTH_SUB))
            {
                monthlyPlan = SKU_ITEM_AD_FREE_1_MONTH_SUB;
                quarterlyPlan = null;
                coordinatorLayout.removeView(adView);
                halfYearlyPlan = null;
                yearlyPlan = null;
            }
            else if(purchase.getSku().equals(SKU_ITEM_AD_FREE_3_MONTH_SUB))
            {
                quarterlyPlan = SKU_ITEM_AD_FREE_3_MONTH_SUB;
                coordinatorLayout.removeView(adView);
                monthlyPlan = null;
                halfYearlyPlan = null;
                yearlyPlan = null;
            }
            else if(purchase.getSku().equals(SKU_ITEM_AD_FREE_6_MONTH_SUB))
            {
                halfYearlyPlan = SKU_ITEM_AD_FREE_6_MONTH_SUB;
                coordinatorLayout.removeView(adView);
                quarterlyPlan = null;
                monthlyPlan = null;
                yearlyPlan = null;
            }
            else if(purchase.getSku().equals(SKU_ITEM_AD_FREE_1_YEAR_SUB))
            {
                yearlyPlan = SKU_ITEM_AD_FREE_1_YEAR_SUB;
                coordinatorLayout.removeView(adView);
                quarterlyPlan = null;
                halfYearlyPlan = null;
                monthlyPlan = null;
            }

            if(!purchase.isAcknowledged())
            {
                AcknowledgePurchaseParams acknowledgePurchaseParams = AcknowledgePurchaseParams
                        .newBuilder().setPurchaseToken(purchase.getPurchaseToken()).build();
                billingClient.acknowledgePurchase(acknowledgePurchaseParams, new AcknowledgePurchaseResponseListener() {
                    @Override
                    public void onAcknowledgePurchaseResponse(BillingResult billingResult) {
                        Toast.makeText(MainActivity.this, "Your purchase acknowledged!\nThanks for shopping with us!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }else if(purchase.getPurchaseState() == Purchase.PurchaseState.PENDING){
            Toast.makeText(this, "Please follow the instructions to complete the purchase.", Toast.LENGTH_SHORT).show();
        }
    }

    private CardParcel cardToParcel(Card card){
        CardParcel cardParcel = new CardParcel(Arrays.asList("Name", "Phone", "Email", "Address"));
        List<String> nameList = new ArrayList<>(Arrays.asList(card.getBusinessName().split(SPLIT_TAG)));
        List<String> phoneList = new ArrayList<>(Arrays.asList(card.getBusinessPhone().split(SPLIT_TAG)));
        List<String> emailList = new ArrayList<>(Arrays.asList(card.getBusinessEmail().split(SPLIT_TAG)));
        List<String> addressList = new ArrayList<>(Arrays.asList(card.getBusinessAddress().split(SPLIT_TAG)));

        cardParcel.setNameList(nameList);
        cardParcel.setPhoneList(phoneList);
        cardParcel.setEmailList(emailList);
        cardParcel.setAddressList(addressList);
        cardParcel.cleanParcel();
        return cardParcel;
    }

    private void saveCardToDatabase(Intent data, int id) {
        Bundle bundle = data.getExtras();
        assert bundle != null;

        CardParcel cardParcel = bundle.getParcelable(EXTRA_BUSINESS_CARD_PARCEL);
        assert cardParcel != null;

        ArrayList<String> imagePathList = data.getStringArrayListExtra(EXTRA_BUSINESS_IMAGE_PATH);
        assert imagePathList != null;

        List<String> nameList = cardParcel.getNameList();
        List<String> phoneList = cardParcel.getPhoneList();
        List<String> emailList = cardParcel.getEmailList();
        List<String> addressList = cardParcel.getAddressList();

        String businessName = listToString(nameList);
        String businessAddress = listToString(addressList);
        String businessPhone = listToString(phoneList);
        String businessEmail = listToString(emailList);
        String businessImagePath = listToString(imagePathList);

        Card card = new Card(businessName, businessAddress, businessPhone, businessEmail, businessImagePath);

        if(id == -2){
            cardViewModel.insert(card);
            Toast.makeText(this, "Card saved", Toast.LENGTH_SHORT).show();
        }else {
            card.setId(id);
            cardViewModel.update(card);

            Bundle bundle1 = new Bundle();
            bundle1.putParcelable(EXTRA_BUSINESS_CARD_PARCEL, cardToParcel(card));
            Intent intent = new Intent(MainActivity.this, ViewCardActivity.class);
            intent.putExtras(bundle1);
            intent.putExtra(EXTRA_ID, id);
            intent.putStringArrayListExtra(EXTRA_BUSINESS_IMAGE_PATH, imagePathList);
            ArrayList<String> stringArrayList = data.getStringArrayListExtra(EXTRA_DATA);
            intent.putStringArrayListExtra(EXTRA_DATA, stringArrayList);
            startActivity(intent);
            Toast.makeText(this, "Card updated", Toast.LENGTH_SHORT).show();
        }
    }

    private String listToString(List<String> list){
        StringBuilder stringBuilder = new StringBuilder();
        for(String s : list){
            stringBuilder.append(s);
            stringBuilder.append(SPLIT_TAG);
        }
        if(list.size() > 0)
            stringBuilder.delete(stringBuilder.toString().lastIndexOf(SPLIT_TAG), stringBuilder.toString().length());
        return stringBuilder.toString();
    }

    @Override
    public void onBackPressed() {
        int count = getSupportFragmentManager().getBackStackEntryCount();
        if (drawerLayout.isDrawerOpen(GravityCompat.START))
        {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else
        {
            if (count == 0)
            {
                if(sharedPreferences.getBoolean(DONT_SHOW_AGAIN, false))
                {
                    super.onBackPressed();
                    return;
                }
                SharedPreferences.Editor editor = sharedPreferences.edit();
                long launchCount = sharedPreferences.getLong(APP_LAUNCH_COUNT, 0) + 1;
                editor.putLong(APP_LAUNCH_COUNT, launchCount);
                long dateNextShowDialog = sharedPreferences.getLong(DATE_NEXT_SHOW_DIALOG, 0);
                long dateMaybeLaterShowDialog = sharedPreferences.getLong(DATE_MAYBE_LATER_SHOW_DIALOG, 0);
                if(dateNextShowDialog == 0){
                    editor.putLong(DATE_NEXT_SHOW_DIALOG, System.currentTimeMillis());
                }

                if(dateMaybeLaterShowDialog == 0){
                    editor.putLong(DATE_MAYBE_LATER_SHOW_DIALOG, System.currentTimeMillis());
                }
                editor.apply();
                if(launchCount >= LAUNCHES_UNTIL_PROMPT)
                {
                    long currentTime = System.currentTimeMillis();
                    if(currentTime >= sharedPreferences.getLong(DATE_MAYBE_LATER_SHOW_DIALOG, currentTime) || currentTime >= sharedPreferences.getLong(DATE_NEXT_SHOW_DIALOG, currentTime))
                    {
                        //start app rater fragment
                        AppRaterFragment appRaterFragment = AppRaterFragment.newInstance();
                        FragmentManager fragmentManager = getSupportFragmentManager();
                        FragmentTransaction transaction = fragmentManager.beginTransaction();
                        transaction.addToBackStack(getClass().getSimpleName());
                        transaction.setCustomAnimations(R.anim.slide_in_right, 0, 0, R.anim.slide_out_right);
                        transaction.addToBackStack(null);
                        transaction.replace(R.id.fragment_container_navigation, appRaterFragment);
                        transaction.commit();
                    }
                    else
                    {
                        super.onBackPressed();
                    }
                }
                else
                {
                    super.onBackPressed();
                }
            }
            else
            {
                getSupportFragmentManager().popBackStack();
            }
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.buy_premium_version:{
                PremiumVersionFragment fragment = PremiumVersionFragment.newInstance(premiumVersionPrice);
                fragment.setPurchaseCallbackInterface(this);
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.addToBackStack(getClass().getSimpleName());
                transaction.setCustomAnimations(R.anim.slide_in_right, 0, 0, R.anim.slide_out_right);
                transaction.addToBackStack(null);
                transaction.replace(R.id.fragment_container_navigation, fragment);
                transaction.commit();
                break;
            }
            case R.id.buy_unlimited_card_tier:{
                UnlimitedCardFragment fragment = UnlimitedCardFragment.newInstance(cardTierUnlimitedPrice);
                fragment.setPurchaseCallbackInterface(this);
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.addToBackStack(getClass().getSimpleName());
                transaction.setCustomAnimations(R.anim.slide_in_right, 0, 0, R.anim.slide_out_right);
                transaction.addToBackStack(null);
                transaction.replace(R.id.fragment_container_navigation, fragment);
                transaction.commit();
                break;
            }
            case R.id.delete_all_cards:{
                final AlertDialog dialog = new AlertDialog.Builder(MainActivity.this)
                        .setTitle("DELETE ALL CARDS")
                        .setMessage("Do you want to delete all cards?\nThis action can not be undone.")
                        .setNegativeButton("No", null)
                        .setPositiveButton("Yes", null)
                        .show();

                Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                positiveButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        cardViewModel.deleteAllCards();
                        Toast.makeText(MainActivity.this, "All cards deleted", Toast.LENGTH_SHORT).show();
                    }
                });
                break;
            }
            case R.id.contact_us:{
                String[] emailArray = {"quickresponse.qmsoftwares@gmail.com"};
                String emailSubject = "Feedback of card holder app";
                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                emailIntent.setType("message/rfc822");
                emailIntent.putExtra(Intent.EXTRA_EMAIL, emailArray);
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, emailSubject);
                startActivity(Intent.createChooser(emailIntent, "Choose an email client"));
                break;
            }
            case R.id.backup_data:{
                final AlertDialog dialog = new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Info")
                        .setMessage(getResources().getString(R.string.auto_backup_dialog))
                        .setPositiveButton("OK", null)
                        .show();

                Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                positiveButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent backupIntent = new Intent(Settings.ACTION_PRIVACY_SETTINGS);
                        startActivity(backupIntent);
                        dialog.dismiss();
                    }
                });
                break;
            }
            case R.id.remove_ads:{
                AdPlansFragment fragment = AdPlansFragment.newInstance(monthlyPlanPrice, quarterlyPlanPrice, halfYearlyPlanPrice, yearlyPlanPrice);
                fragment.setPurchaseCallbackInterface(this);
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.addToBackStack(getClass().getSimpleName());
                transaction.setCustomAnimations(R.anim.slide_in_right, 0, 0, R.anim.slide_out_right);
                transaction.addToBackStack(null);
                transaction.replace(R.id.fragment_container_navigation, fragment);
                transaction.commit();
                break;
            }
            case R.id.my_subscriptions:{
                StringBuilder stringBuilder = new StringBuilder();
                if(monthlyPlan != null && monthlyPlan.equals(SKU_ITEM_AD_FREE_1_MONTH_SUB))
                    stringBuilder.append("https://play.google.com/store/account/subscriptions?sku=").append(SKU_ITEM_AD_FREE_1_MONTH_SUB).append("&package=").append(getPackageName());
                else if(quarterlyPlan != null && quarterlyPlan.equals(SKU_ITEM_AD_FREE_3_MONTH_SUB))
                    stringBuilder.append("https://play.google.com/store/account/subscriptions?sku=").append(SKU_ITEM_AD_FREE_3_MONTH_SUB).append("&package=").append(getPackageName());
                else if(halfYearlyPlan != null && halfYearlyPlan.equals(SKU_ITEM_AD_FREE_6_MONTH_SUB))
                    stringBuilder.append("https://play.google.com/store/account/subscriptions?sku=").append(SKU_ITEM_AD_FREE_6_MONTH_SUB).append("&package=").append(getPackageName());
                else if(yearlyPlan != null && yearlyPlan.equals(SKU_ITEM_AD_FREE_1_YEAR_SUB))
                    stringBuilder.append("https://play.google.com/store/account/subscriptions?sku=").append(SKU_ITEM_AD_FREE_1_YEAR_SUB).append("&package=").append(getPackageName());
                else
                    stringBuilder.append("https://play.google.com/store/account/subscriptions");

                Uri subscriptionUri = Uri.parse(stringBuilder.toString());
                startActivity(new Intent(Intent.ACTION_VIEW, subscriptionUri));
                break;
            }
            case R.id.app_help:{
                Intent intent = new Intent(this, IntroActivity.class);
                startActivity(intent);
                break;
            }
            default:
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void selectedPurchase(String skuTag) {
        if(inAppSkuDetailsList != null){
            for (SkuDetails skuDetails : inAppSkuDetailsList) {
                if(skuDetails.getSku().equals(skuTag)){
                    BillingFlowParams billingFlowParams = BillingFlowParams.newBuilder()
                            .setSkuDetails(skuDetails)
                            .build();
                    billingClient.launchBillingFlow(this, billingFlowParams);
                    getSupportFragmentManager().popBackStack();
                    return;
                }
            }
        }

        if(subSkuDetailsList != null){
            for (SkuDetails skuDetails : subSkuDetailsList) {
                if(skuDetails.getSku().equals(skuTag)){
                    BillingFlowParams billingFlowParams = BillingFlowParams.newBuilder()
                            .setSkuDetails(skuDetails)
                            .build();
                    billingClient.launchBillingFlow(this, billingFlowParams);
                    getSupportFragmentManager().popBackStack();
                    return;
                }
            }
        }
    }
}