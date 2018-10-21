package com.example.android.inventoryapp2.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;


public class ProductProvider extends ContentProvider {
    public static final int PRODUCTS = 100;
    public static final int PRODUCT_ID = 101;
    public static UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        uriMatcher.addURI(ProductContact.CONTENT_AUTHORITY, ProductContact.PATH, PRODUCTS);
        uriMatcher.addURI(ProductContact.CONTENT_AUTHORITY, ProductContact.PATH + "/#", PRODUCT_ID);
    }

    ProductDpHelper dpHelper;

    @Override
    public boolean onCreate() {
        dpHelper = new ProductDpHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        //create cursor it return
        Cursor cursor = null;

        //match uri for code
        int match = uriMatcher.match(uri);

        //get readable database
        SQLiteDatabase sqLiteDatabase = dpHelper.getReadableDatabase();

        //use switch to determine code for products code or product_id code
        switch (match) {
            case PRODUCTS:
                //call method query to get item we want from database and return cursor
                cursor = sqLiteDatabase.query(ProductContact.ProductEntity.PRODUCT_TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                ContentValues contentValues = new ContentValues();
                contentValues.put(ProductContact.ProductEntity.COLUMN_PRODUCT_NAME, "man");
                contentValues.put(ProductContact.ProductEntity.COLUMN_PRODUCT_PRICE, 3.2);
                contentValues.put(ProductContact.ProductEntity.COLUMN_PRODUCT_QUANTITY, 4);
                contentValues.put(ProductContact.ProductEntity.COLUMN_PRODUCT_NAME_SUPPLIER, "man");
                break;
            case PRODUCT_ID:
                //first get name of column of id
                selection = ProductContact.ProductEntity.COLUMN_ID + "=?";

                //get id from uri and set in selection args
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};

                //call method query to get item we want from database and return cursor
                cursor = sqLiteDatabase.query(ProductContact.ProductEntity.PRODUCT_TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            default:
                throw new IllegalArgumentException("query method unknown URI " + uri);
        }

        //set notification when call insert method or again change the cursor in ui
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        //return cursor
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        int match = uriMatcher.match(uri);
        switch (match) {
            case PRODUCTS:
                return ProductContact.ProductEntity.CONTENT_LIST_TYPE;
            case PRODUCT_ID:
                return ProductContact.ProductEntity.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException("get type method unknown URI" + uri);
        }

    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        int match = uriMatcher.match(uri);
        Uri resultUri;
        switch (match) {
            case PRODUCTS:
                resultUri = insertProduct(uri, contentValues);
                break;
            default:
                throw new IllegalArgumentException(" insert method unknown URI" + uri + "the match is " + match);
        }

        if (resultUri == null)
            return null;
        return resultUri;
    }

    private Uri insertProduct(Uri uri, ContentValues contentValues) {

        if (isInputInValid(contentValues)) {
            return null;
        }

        SQLiteDatabase sqLiteDatabase = dpHelper.getWritableDatabase();
        long id = sqLiteDatabase.insert(ProductContact.ProductEntity.PRODUCT_TABLE_NAME, null, contentValues);
        if (id == -1) {
            return null;
        }

        getContext().getContentResolver().notifyChange(uri, null);

        return ContentUris.withAppendedId(uri, id);
    }


    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        int match = uriMatcher.match(uri);
        int result;
        switch (match) {
            case PRODUCTS:
                result = deleteProduct(uri, selection, selectionArgs);
                break;
            case PRODUCT_ID:
                selection = ProductContact.ProductEntity.COLUMN_ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                result = deleteProduct(uri, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("delete method unknown URI" + uri);
        }

        return result;
    }

    private int deleteProduct(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase sqLiteDatabase = dpHelper.getWritableDatabase();
        int rowsDeleted = sqLiteDatabase.delete(ProductContact.ProductEntity.PRODUCT_TABLE_NAME, selection, selectionArgs);
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String selection, @Nullable String[] selectionArgs) {
        int match = uriMatcher.match(uri);
        int result;
        switch (match) {
            case PRODUCTS:
                result = updateProduct(uri, contentValues, selection, selectionArgs);
                break;
            case PRODUCT_ID:
                selection = ProductContact.ProductEntity.COLUMN_ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                result = updateProduct(uri, contentValues, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("update method unknown URI" + uri);
        }

        return result;
    }

    private int updateProduct(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {
        if (isInputInValid(contentValues)) {
            return 0;
        }


        SQLiteDatabase sqLiteDatabase = dpHelper.getWritableDatabase();
        int rowsUpdated = sqLiteDatabase.update(ProductContact.ProductEntity.PRODUCT_TABLE_NAME, contentValues, selection, selectionArgs);
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }

    private boolean isInputInValid(ContentValues contentValues) {
        if (contentValues.containsKey(ProductContact.ProductEntity.COLUMN_PRODUCT_NAME)) {
            String name = contentValues.get(ProductContact.ProductEntity.COLUMN_PRODUCT_NAME).toString();
            if (name == null || name.isEmpty()) {
                return true;
            }
        }

        if (contentValues.containsKey(ProductContact.ProductEntity.COLUMN_PRODUCT_QUANTITY)) {
            String amount = contentValues.get(ProductContact.ProductEntity.COLUMN_PRODUCT_QUANTITY).toString();
            if (amount == null || amount.isEmpty()) {
                return true;
            }
        }

        if (contentValues.containsKey(ProductContact.ProductEntity.COLUMN_PRODUCT_PRICE)) {
            String price = contentValues.get(ProductContact.ProductEntity.COLUMN_PRODUCT_PRICE).toString();
            if (price == null || price.isEmpty()) {
                return true;
            }
        }

        if (contentValues.containsKey(ProductContact.ProductEntity.COLUMN_PRODUCT_NAME_SUPPLIER)) {
            String supplierName = contentValues.get(ProductContact.ProductEntity.COLUMN_PRODUCT_NAME_SUPPLIER).toString();
            if (supplierName == null || supplierName.isEmpty()) {
                return true;
            }
        }

        if (contentValues.containsKey(ProductContact.ProductEntity.COLUMN_PRODUCT_PHONE_SUPPLIER)) {
            String supplierPhone = contentValues.get(ProductContact.ProductEntity.COLUMN_PRODUCT_PHONE_SUPPLIER).toString();
            if (supplierPhone == null || supplierPhone.isEmpty()) {
                return true;
            }
        }

        return false;
    }
}
