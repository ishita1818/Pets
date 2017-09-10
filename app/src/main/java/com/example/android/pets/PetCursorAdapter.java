package com.example.android.pets;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.android.pets.data.PetContract;

/**
 * Created by nirmal vats on 7/17/2017.
 */

public class PetCursorAdapter extends CursorAdapter {

   public PetCursorAdapter(Context context, Cursor cursor){
        super(context,cursor,0);
    }
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        return LayoutInflater.from(context).inflate(R.layout.list_item_layout,parent,false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView name = (TextView)view.findViewById(R.id.name);
        TextView breed =(TextView)view.findViewById(R.id.summary);
        int nameColumnIndex= cursor.getColumnIndex(PetContract.PetEntry.COLUMN_PET_NAME);
        int breedColumnIndex= cursor.getColumnIndex(PetContract.PetEntry.COLUMN_PET_BREED);

        String thisName = cursor.getString(nameColumnIndex);
        String thisBreed = cursor.getString(breedColumnIndex);

        name.setText(thisName);
        breed.setText(thisBreed);
    }
}
