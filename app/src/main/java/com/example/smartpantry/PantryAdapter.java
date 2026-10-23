package com.example.smartpantry;
import android.content.*;
import android.view.*;
import android.widget.*;
import  java.util.*;
import java.text.*;
import android.graphics.*;

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

        SharedPreferences preferences = getContext().getSharedPreferences("settings", Context.MODE_PRIVATE);
        boolean alertsOn = preferences.getBoolean("expiry_alerts", true);

        if( item.expiry == null || item.expiry.isEmpty()){
            itemExpiryText.setText("");
        } else if(alertsOn && isExpiringSoon(item.expiry)){
            itemExpiryText.setText("Expiring soon: " + item.expiry);
            itemExpiryText.setTextColor(Color.RED);
        } else{
            itemExpiryText.setText("Expiry Date: " + item.expiry);
            itemExpiryText.setTextColor(Color.GRAY);
        }
        return convertView;
    }

    private boolean isExpiringSoon(String expiry){
        try{
            Date expiryDate = new SimpleDateFormat("yyyy-MM-dd", Locale.US).parse(expiry);
            long days = (expiryDate.getTime() - System.currentTimeMillis()) / (1000 * 60 * 60 * 24);
            return days <= 3;
        } catch (ParseException e){
            return false;
        }
    }
}
