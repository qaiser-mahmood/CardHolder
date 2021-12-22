package com.business.collector.wallet.cardholder.Adapter;

import android.content.res.ColorStateList;
import android.os.Build;
import android.text.Editable;
import android.text.TextWatcher;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.business.collector.wallet.cardholder.Model.CategoryClass;
import com.business.collector.wallet.cardholder.R;

import java.util.List;

public class AddEditAdapter extends ListAdapter<CategoryClass, AddEditAdapter.CategoryViewHolder> {
    private static final int LAYOUT_ADD = -1;
    private boolean layoutAddButton = false;
    private List<CategoryClass> dataList;

    public AddEditAdapter(List<CategoryClass> dataList) {
        super(DIFF_CALLBACK);
        this.dataList = dataList;
    }

    private static DiffUtil.ItemCallback<CategoryClass> DIFF_CALLBACK = new DiffUtil.ItemCallback<CategoryClass>() {
        @Override
        public boolean areItemsTheSame(@NonNull CategoryClass oldItem, @NonNull CategoryClass newItem) {
            return oldItem.getCategory().equals(newItem.getCategory());
        }

        @Override
        public boolean areContentsTheSame(@NonNull CategoryClass oldItem, @NonNull CategoryClass newItem) {
            return oldItem.getCategory().equals(newItem.getCategory())
                    && oldItem.getText().equals(newItem.getText())
                    && oldItem.getValue().equals(newItem.getValue());
        }
    };

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        if (viewType == LAYOUT_ADD) {
            View view0 = layoutInflater.inflate(R.layout.list_item_addbutton_addeditcard, parent, false);
            return new CategoryViewHolder(view0, viewType);
        }else {

            View view1 = layoutInflater.inflate(R.layout.list_item_addeditcard, parent, false);
            return new CategoryViewHolder(view1, viewType);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final CategoryViewHolder holder, final int position) {
        final CategoryClass currentCategoryClass = getItem(position);

        if(layoutAddButton){
            holder.imageButtonAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (currentCategoryClass.getCategory()){
                        case "Name":
                            dataList.add(position, new CategoryClass("Name", "Name", ""));
                            break;
                        case "Phone":
                            dataList.add(position, new CategoryClass("Phone", "Phone", ""));
                            break;
                        case "Email":
                            dataList.add(position, new CategoryClass("Email", "Email", ""));
                            break;
                        case "Address":
                            dataList.add(position, new CategoryClass("Address", "Address", ""));
                            break;
                        default:
                    }
                    notifyItemInserted(position);
                    notifyItemRangeChanged(position, getItemCount());
                }
            });

            holder.textViewAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.imageButtonAdd.callOnClick();
                }
            });

            holder.textViewAdd.setText(currentCategoryClass.getText());

        }else {
            holder.imageButtonDelete.setOnClickListener(new View.OnClickListener() {
                boolean visible;
                @Override
                public void onClick(View v) {
                    TransitionManager.beginDelayedTransition(holder.linearLayoutRowItems);
                    visible = !visible;
                    holder.buttonDelete.setBackground(v.getResources().getDrawable(R.drawable.rounded_corner_rectangle_red));
                    holder.buttonDelete.setVisibility(visible ? View.VISIBLE : View.GONE);
                    if(holder.buttonDelete.getVisibility() == View.VISIBLE) {
                        holder.editTextValue.setClickable(false);
                        holder.editTextValue.setFocusable(false);
                        holder.editTextValue.setFocusableInTouchMode(false);
                    }else {
                        holder.editTextValue.setClickable(true);
                        holder.editTextValue.setFocusable(true);
                        holder.editTextValue.setFocusableInTouchMode(true);
                    }

                    holder.buttonDelete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dataList.remove(position);
                            notifyItemRemoved(position);
                            notifyItemRangeChanged(position, getItemCount());
                            holder.buttonDelete.setVisibility(View.GONE);
                        }
                    });
                }
            });

            holder.radioButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for(int i=0; i< dataList.size(); i++){
                        if(!dataList.get(i).getValue().isEmpty() && dataList.get(i).getCategory().equals(currentCategoryClass.getCategory())){
                            if(i != position) {
                                dataList.get(i).setSelected(false);
                                notifyItemChanged(i);
                            }
                            else {
                                dataList.get(i).setSelected(true);
                            }
                        }
                    }
                }
            });

            holder.editTextValue.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {}

                @Override
                public void afterTextChanged(Editable s) {
                    dataList.get(position).setValue(s.toString());
                }
            });

//            holder.textViewText.setText(currentCategoryClass.getText());
            holder.editTextValue.setText(currentCategoryClass.getValue());
            holder.radioButton.setChecked(currentCategoryClass.isSelected());
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(position == getItemCount()-1){
            layoutAddButton = true;
            return LAYOUT_ADD;
        }
        else {
            String currentCategory = dataList.get(position).getCategory();
            String nextCategory = dataList.get(position + 1).getCategory();
            if (!currentCategory.equals(nextCategory)) {
                layoutAddButton = true;
                return LAYOUT_ADD;
            } else {
                layoutAddButton = false;
                return position;
            }
        }
    }

    static class CategoryViewHolder extends RecyclerView.ViewHolder {
//        TextView textViewText;
        TextView textViewAdd;
        EditText editTextValue;
        ImageButton imageButtonDelete, imageButtonAdd;
        RadioButton radioButton;
        Button buttonDelete;
        LinearLayout linearLayoutRowItems;

        CategoryViewHolder(@NonNull View itemView, int viewType) {
            super(itemView);
            if(viewType == LAYOUT_ADD){
                imageButtonAdd = itemView.findViewById(R.id.imageButtonAdd_addEditCard);
                textViewAdd = itemView.findViewById(R.id.textViewAdd_AddEditCard);
            }else {
//                textViewText = itemView.findViewById(R.id.textViewText_AddEditCard);
                editTextValue = itemView.findViewById(R.id.editTextValue_AddEditCard);
                imageButtonDelete = itemView.findViewById(R.id.imageButtonDelete_AddEditCard);
                radioButton = itemView.findViewById(R.id.radioButton_AddEditCard);
                buttonDelete = itemView.findViewById(R.id.buttonDelete_AddEditCard);
                linearLayoutRowItems = itemView.findViewById(R.id.linear_layout_rowItems_AddEditCard);
                radioButton.setButtonTintList(ColorStateList.valueOf(ContextCompat.getColor(itemView.getContext(), R.color.colorOrange)));
            }
        }
    } //View holder ends
}
