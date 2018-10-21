package com.example.android.inventoryapp2.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.android.inventoryapp2.data.ProductContact.ProductEntity;

public class ProductDpHelper extends SQLiteOpenHelper {
    public static final int VERSION_DB = 1;
    public static final String DATABASE_NAME = "products.dp";

    public static final String SQL_CREATE_TABLE_PRODUCT_QUERY = "CREATE TABLE " +
            ProductEntity.PRODUCT_TABLE_NAME + "(" +
            ProductEntity.COLUMN_ID + " INTEGER  PRIMARY KEY AUTOINCREMENT," +
            ProductEntity.COLUMN_PRODUCT_NAME + " TEXT ," +
            ProductEntity.COLUMN_PRODUCT_PRICE + " INTEGER ," +
            ProductEntity.COLUMN_PRODUCT_QUANTITY + " INTEGER ," +
            ProductEntity.COLUMN_PRODUCT_NAME_SUPPLIER + " TEXT ," +
            ProductEntity.COLUMN_PRODUCT_PHONE_SUPPLIER + " TEXT " +
            ");";
    public static final String SQL_DELETE_TABLE_PRODUCT_QUERY = "DROP TABLE " + ProductEntity.PRODUCT_TABLE_NAME + ";";

    public ProductDpHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION_DB);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_CREATE_TABLE_PRODUCT_QUERY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(SQL_DELETE_TABLE_PRODUCT_QUERY);
        sqLiteDatabase.execSQL(SQL_CREATE_TABLE_PRODUCT_QUERY);
    }
}
