package com.example.android.inventoryapp2.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public class ProductContact {

    public static final String CONTENT_AUTHORITY = "com.example.android.inventoryapp2";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH = "produ";


    public static class ProductEntity implements BaseColumns {
        public static final String PRODUCT_TABLE_NAME = "products";

        public static final String COLUMN_ID = BaseColumns._ID;
        public static final String COLUMN_PRODUCT_NAME = "name_product";
        public static final String COLUMN_PRODUCT_PRICE = "price_product";
        public static final String COLUMN_PRODUCT_QUANTITY = "quantity";
        public static final String COLUMN_PRODUCT_NAME_SUPPLIER = "name_supplier";
        public static final String COLUMN_PRODUCT_PHONE_SUPPLIER = "phone_supplier";

        public static final Uri URI_CONTENT = Uri.withAppendedPath(BASE_CONTENT_URI, PATH);

        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH;

        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH;

    }
}
