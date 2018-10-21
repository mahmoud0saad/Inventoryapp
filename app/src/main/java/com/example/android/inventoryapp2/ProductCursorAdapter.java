package com.example.android.inventoryapp2;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.android.inventoryapp2.data.ProductContact.ProductEntity;


public class ProductCursorAdapter extends CursorAdapter {

    private Context mContext;

    public ProductCursorAdapter(@NonNull Context context, Cursor cursor) {
        super(context, cursor);
        mContext = context;
    }


    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        //inflate my product list item to list of products
        return LayoutInflater.from(mContext).inflate(R.layout.product_list_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        //associate xml by java
        TextView nameTextView = view.findViewById(R.id.name_product_text);
        TextView priceTextView = view.findViewById(R.id.price_product_text);
        TextView amountTextView = view.findViewById(R.id.amount_product_text);
        Button saleButton = view.findViewById(R.id.sale_product_btn);

        //get column index for all column need
        int idIndex = cursor.getColumnIndex(ProductEntity.COLUMN_ID);
        int nameIndex = cursor.getColumnIndex(ProductEntity.COLUMN_PRODUCT_NAME);
        int priceIndex = cursor.getColumnIndex(ProductEntity.COLUMN_PRODUCT_PRICE);
        int amountIndex = cursor.getColumnIndex(ProductEntity.COLUMN_PRODUCT_QUANTITY);

        //get value from cursor by column index
        final int id = cursor.getInt(idIndex);
        String name = cursor.getString(nameIndex);
        String price = cursor.getString(priceIndex);
        final int amount = cursor.getInt(amountIndex);

        //action the button sale
        saleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //when users click it button minus amount 1
                //check if amount equal 0 to avoid the negative values
                if (amount != 0) {
                    //create uri for one product by use id
                    Uri uri = ContentUris.withAppendedId(ProductEntity.URI_CONTENT, id);

                    //create content values
                    ContentValues contentValues = new ContentValues();

                    //put key and values in content values
                    contentValues.put(ProductEntity.COLUMN_PRODUCT_QUANTITY, amount - 1);

                    //update in database
                    mContext.getContentResolver().update(uri,
                            contentValues,
                            null,
                            null
                    );
                }
            }
        });

        //set values in text view
        nameTextView.setText(name);
        priceTextView.setText(price);
        amountTextView.setText(String.valueOf(amount));


    }
}
