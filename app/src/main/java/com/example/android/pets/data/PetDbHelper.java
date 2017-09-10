package com.example.android.pets.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.example.android.pets.data.PetContract.PetEntry;

/**
 * Created by nirmal vats on 7/11/2017.
 */

public class PetDbHelper extends SQLiteOpenHelper {

    private final static String DATABASE_NAME= "shelter.db";
    private final static int DATABASE_VERSION = 1;
    private final static String CREATE_TABLE="CREATE TABLE "+ PetEntry.TABLE_NAME+"("+
                                                 PetEntry._ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"+
                                                 PetEntry.COLUMN_PET_NAME +" TEXT,"+
                                                 PetEntry.COLUMN_PET_BREED +" TEXT,"+
                                                 PetEntry.COLUMN_PET_GENDER+" INTEGER, "+
                                                  PetEntry.COLUMN_PET_WEIGHT+" INTEGER )";
    private static final String UPDATE_STATEMENT = "DROP TABLE IF EXISTS "+ PetEntry.TABLE_NAME;


    public  PetDbHelper(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {


    }
}
