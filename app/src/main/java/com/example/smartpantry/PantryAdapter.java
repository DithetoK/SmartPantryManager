package com.example.smartpantry;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import  java.util.List;

public class PantryAdapter extends ArrayAdapter<Ingredient>{
    public PantryAdapter(Context context, List<Ingredient> items){
        super(context, 0 , items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        if(convertView == null){
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.item_pantry, parent, false);
        }

        Ingredient item = getItem(position);
        TextView itemNameText = convertView.findViewById(R.id.itemNameText);
        TextView itemQuantityText = convertView.findViewById(R.id.itemQuantityText);
        TextView itemExpiryText = convertView.findViewById(R.id.itemExpiryText);

        itemNameText.setText(item.name);
        itemQuantityText.setText(item.quantityText());

        if( item.expiry == null || item.expiry.isEmpty()){
            itemExpiryText.setText("");
        } else{
            itemExpiryText.setText("Expiry Date: " + item.expiry);
        }
        return convertView;
    }
}
