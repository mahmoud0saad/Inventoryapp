package com.example.android.inventoryapp2;

import android.content.Context;
import android.content.CursorLoader;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;

public class ProductLoader extends CursorLoader {
    private Uri mUri;
    private Context mContext;
    private String mProjection[], mSelection, mSelectionArgs[], mSorter;

    public ProductLoader(@NonNull Context context, Uri uri, String[] projection, String selection, String selectionArgs[], String sortOrder) {
        super(context);

        //get the new values and set in my variable
        mContext = context;
        mProjection = projection;
        mSelection = selection;
        mUri = uri;
        mSelectionArgs = selectionArgs;
        mSorter = sortOrder;

    }

    @Override
    public Cursor loadInBackground() {
        //do query
        return mContext.getContentResolver().query(mUri, mProjection, mSelection, mSelectionArgs, mSorter);
    }
}
