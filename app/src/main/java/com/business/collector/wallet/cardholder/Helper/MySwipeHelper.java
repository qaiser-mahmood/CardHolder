package com.business.collector.wallet.cardholder.Helper;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.business.collector.wallet.cardholder.Interface.MyButtonClickListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

public abstract class MySwipeHelper extends ItemTouchHelper.SimpleCallback {

    private int buttonWidth;
    private RecyclerView recyclerView;
    private List<MyButton> buttonList;
    private GestureDetector gestureDetector;
    private int swipePosition = -1;
    private float swipeThreshold = 0.5f;
    private Map<Integer, List<MyButton>> buttonBuffer;
    private Queue<Integer> removerQueue;
    private boolean leftIsVisible = false;
    private boolean rightIsVisible = false;
    private int displayWidth, displayHeight;

    private GestureDetector.SimpleOnGestureListener gestureListener = new GestureDetector.SimpleOnGestureListener(){
        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            for(MyButton button: buttonList){
                if(button.onClick(e.getX(), e.getY()))
                    break;
            }
            return true;
        }
    };

    private View.OnTouchListener onTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if(swipePosition < 0)
                return false;
            Point point = new Point((int) event.getRawX(), (int) event.getRawY());

            RecyclerView.ViewHolder swipeViewHolder = recyclerView.findViewHolderForAdapterPosition(swipePosition);
            assert swipeViewHolder != null;
            View swipedItem = swipeViewHolder.itemView;
            Rect rect = new Rect();
            swipedItem.getGlobalVisibleRect(rect);

            if(event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_MOVE)
            {
                if(rect.top < point.y && rect.bottom > point.y)     //Check if the touch point within the boundary of swiped item
                    gestureDetector.onTouchEvent(event);
                else {

                    removerQueue.add(swipePosition);
                    swipePosition = -1;

                    recoverSwipedItem();
                }
            }
            return false;
        }
    };


    public MySwipeHelper(Context context, RecyclerView recyclerView, int buttonWidth) {
        super(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        this.displayWidth = displayMetrics.widthPixels;
        this.displayHeight = displayMetrics.heightPixels;

        this.recyclerView = recyclerView;
        this.buttonList = new ArrayList<>();
        this.gestureDetector = new GestureDetector(context, gestureListener);
        this.recyclerView.setOnTouchListener(onTouchListener);
        this.buttonBuffer = new HashMap<>();
        this.buttonWidth = buttonWidth;

        removerQueue = new LinkedList<Integer>(){
            @Override
            public boolean add(Integer integer) {
                if(contains(integer))
                    return false;
                else
                    return super.add(integer);
            }
        };

        attachSwipe();
    }

    private void attachSwipe() {
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(this);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    private synchronized void recoverSwipedItem() {
        while (!removerQueue.isEmpty())
        {
            int pos = removerQueue.poll();
            if(pos > -1) {
                recyclerView.getAdapter().notifyItemChanged(pos);
            }
        }
    }

    public class MyButton {
        private String text;
        private int imageResId, textSize, color, pos;
        private RectF clickRegion;
        private MyButtonClickListener listener;
        private Context context;
        private boolean rightButton;

        public MyButton(Context context, String text, int textSize, int imageResId, int color, boolean rightButton, MyButtonClickListener listener) {
            this.text = text;
            this.imageResId = imageResId;
            this.textSize = textSize;
            this.color = color;
            this.listener = listener;
            this.context = context;
            this.rightButton = rightButton;
        }

        boolean onClick(float x, float y)
        {
            if(clickRegion != null && clickRegion.contains(x, y) && (leftIsVisible || rightIsVisible))
            {
                listener.onClick(pos);
                return true;
            }
            return false;
        }

        void onDraw(Canvas c, RectF rectF, int pos){
            Paint p = new Paint();
            p.setColor(color);
            c.drawRect(rectF, p);

            if(imageResId == 0)  //If just show text
            {
                drawText(c, rectF, p);
            }
            else {
                drawIconAndText(c, rectF, p);
            }
            clickRegion = rectF;
            this.pos = pos;
        }

        private void drawText(Canvas c, RectF rectF, Paint p){
            p.setColor(Color.BLACK);
            p.setTextSize(textSize);

            Rect r = new Rect();
            float cHeight = rectF.height();
            float cWidth = rectF.width();
            p.setTextAlign(Paint.Align.LEFT);
            p.getTextBounds(text, 0, text.length(), r);
            float x=0, y=0;

            x = cWidth/2f - r.width()/2f - r.left;
            y = cHeight/2f + r.height()/2f -r.bottom;
            c.drawText(text, rectF.left+x, rectF.top+y, p);
        }

        private void drawIconAndText(Canvas c, RectF rectF, Paint p){
            Drawable d = ContextCompat.getDrawable(context, imageResId);
            Bitmap bitmap = drawableToBitmap(d);

            //Text
            p.setColor(Color.BLACK);
            p.setTextSize(textSize);

            Rect r = new Rect();
            float cHeight = rectF.height();
            float cWidth = rectF.width();
            p.setTextAlign(Paint.Align.LEFT);
            p.getTextBounds(text, 0, text.length(), r);

            assert d != null;
            c.drawBitmap(bitmap, (rectF.left + rectF.right - d.getIntrinsicWidth())/2, ( rectF.top + rectF.bottom - d.getIntrinsicHeight() - r.height())/2, p);    //Adjust this value to center the drawable icon;

            float x=0, y=0;
            x = cWidth/2f - r.width()/2f - r.left;      //adjust x and y to center the text
            y = cHeight/2f + r.height()/2f -r.bottom + d.getIntrinsicHeight()/2f;
            c.drawText(text, rectF.left+x, rectF.top+y, p);
        }
    }

    private Bitmap drawableToBitmap(Drawable d) {
        if(d instanceof BitmapDrawable)
            return ((BitmapDrawable) d).getBitmap();

        Bitmap bitmap = Bitmap.createBitmap(d.getIntrinsicWidth(), d.getIntrinsicHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        d.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        d.draw(canvas);
        return bitmap;
    }

    //Override

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        int pos = viewHolder.getAdapterPosition();
        if(swipePosition != pos)
            removerQueue.add(swipePosition);
        swipePosition = pos;
        if(buttonBuffer.containsKey(swipePosition))
            buttonList = buttonBuffer.get(swipePosition);
        else
            buttonList.clear();

        buttonBuffer.clear();
        swipeThreshold = 0.5f * buttonList.size() * buttonWidth;
        recoverSwipedItem();
    }

    public float getSwipeThreshold(RecyclerView.ViewHolder viewHolder) {
        return swipeThreshold;
    }

    @Override
    public float getSwipeEscapeVelocity(float defaultValue) {
        return 0.1f * defaultValue;
    }

    @Override
    public float getSwipeVelocityThreshold(float defaultValue) {
        return 5.0f * defaultValue;
    }

    @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive)
    {
        int pos = viewHolder.getAdapterPosition();
        float translationX = dX;
        View itemView = viewHolder.itemView;
        if(pos < 0)
        {
            swipePosition = pos;
            return;
        }
        if(actionState == ItemTouchHelper.ACTION_STATE_SWIPE){
            List<MyButton> buffer = new ArrayList<>();
            if(!buttonBuffer.containsKey(pos))
            {
                instantiateMyButton(viewHolder, buffer);
                buttonBuffer.put(pos, buffer);
            }else {
                buffer = buttonBuffer.get(pos);
            }
            translationX = 2 * dX * buffer.size() * buttonWidth / itemView.getWidth();      //Controls the swipe responsiveness e.g multiply by two to make it double
//            translationX = dX * buffer.size() * buttonWidth / itemView.getWidth();      //Controls the swipe responsiveness e.g multiply by two to make it double

            List<MyButton> leftButtonList = new ArrayList<>();
            List<MyButton> rightButtonList = new ArrayList<>();

            for(MyButton button: buffer)
            {
                if (button.rightButton)
                    rightButtonList.add(button);
                else
                    leftButtonList.add(button);
            }

            if(dX < 0)      //Left swipe
            {
                leftIsVisible = false;
                rightIsVisible = true;
                drawButtonRight(c, itemView, rightButtonList, pos, translationX);
            }
            else if(dX > 0)  //Right swipe
            {
                rightIsVisible = false;
                leftIsVisible = true;
                drawButtonLeft(c, itemView, leftButtonList, pos, translationX);
            }else {
                leftIsVisible = false;
                rightIsVisible = false;
            }
        }
        super.onChildDraw(c, recyclerView, viewHolder, translationX, dY, actionState, isCurrentlyActive);
    }

    private void drawButtonLeft(Canvas c, View itemView, List<MyButton> buffer, int pos, float translationX) {
        float left = itemView.getLeft();
        float dButtonWidth = -1 * translationX / buffer.size();
        for(MyButton button: buffer){
            float right = left - dButtonWidth;
            button.onDraw(c, new RectF(left, itemView.getTop(), right, itemView.getBottom()), pos);
            left = right;
        }
    }

    private void drawButtonRight(Canvas c, View itemView, List<MyButton> buffer, int pos, float translationX) {
        float right = itemView.getRight();
        float dButtonWidth = -1 * translationX / buffer.size();
        for(MyButton button: buffer){
            float left = right - dButtonWidth;
            button.onDraw(c, new RectF(left, itemView.getTop(), right, itemView.getBottom()), pos);
            right = left;
        }
    }

    protected abstract void instantiateMyButton(RecyclerView.ViewHolder viewHolder, List<MyButton> buffer);
}
