/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.pets;

import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.android.pets.data.PetContract.PetEntry;
import com.example.android.pets.data.PetDbHelper;

/**
 * Displays list of pets that were entered and stored in the app.
 */
public class CatalogActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static PetDbHelper mDbHelper;
    private static  SQLiteDatabase db;
    private PetCursorAdapter mAdapter;
    private static ListView listView;
    private static View EmptyView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog);

        // Setup FAB to open EditorActivity
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CatalogActivity.this, EditorActivity.class);
                startActivity(intent);
            }
        });

        // To access our database, we instantiate our subclass of SQLiteOpenHelper,
        // and pass the context, which is the current activity.
        mDbHelper = new PetDbHelper(this);
        // Create and/or open a database to read from it
        listView = (ListView)findViewById(R.id.list);

         EmptyView = (View)findViewById(R.id.empty_view);
        listView.setEmptyView(EmptyView);

        mAdapter = new PetCursorAdapter(this,null);

        listView.setAdapter(mAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Uri uri= ContentUris.withAppendedId(PetEntry.CONTENT_URI,id);

                Intent intent= new Intent(CatalogActivity.this,EditorActivity.class);
                intent.setData(uri);
                startActivity(intent);
            }
        });

        getLoaderManager().initLoader(URL_LOADER,null,this);
    }

    private void InsertDummyData(){
        ContentValues values= new ContentValues();
        values.put(PetEntry.COLUMN_PET_NAME,"Toto");
        values.put(PetEntry.COLUMN_PET_BREED,"Terrier");
        values.put(PetEntry.COLUMN_PET_GENDER,PetEntry.GENDER_MALE);
        values.put(PetEntry.COLUMN_PET_WEIGHT,7);
        Uri uri = getContentResolver().insert(PetEntry.CONTENT_URI,values);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_catalog.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_catalog, menu);
        return true;
    }

    private void showDeleteConfirmationDialog(){
        AlertDialog.Builder builder= new  AlertDialog.Builder(this);
        builder.setMessage(R.string.delete_pets);
        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deletePets();
            }
        });

        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(dialog!=null){
                    dialog.dismiss();
                }
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void deletePets() {

        getContentResolver().delete(PetEntry.CONTENT_URI,null,null);
        listView.setEmptyView(EmptyView);
       //   Toast toast = new Toast(this);
        //toast.makeText(this,"All pets were deleted!!",Toast.LENGTH_SHORT);
        //toast.show();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Insert dummy data" menu option
            case R.id.action_insert_dummy_data:
               InsertDummyData();

                return true;
            // Respond to a click on the "Delete all entries" menu option
            case R.id.action_delete_all_entries:
                showDeleteConfirmationDialog();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private final static int URL_LOADER= 0;
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        String[] projection = {PetEntry._ID, PetEntry.COLUMN_PET_NAME, PetEntry.COLUMN_PET_BREED};
        switch (id){
            case URL_LOADER:
                return new CursorLoader(this,PetEntry.CONTENT_URI,projection,null,null,null);
            default:
                //invalid id passed
                return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        //attach cursor to adapter to refresh data as soon as the query is fetched
        mAdapter.swapCursor(data);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        //delete reference to current cursor to prevent memory leaks
        mAdapter.swapCursor(null);
    }
}
