package com.example.android.inventoryapp2;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.android.inventoryapp2.data.ProductContact.ProductEntity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    @BindView(R.id.products_list_view)
    ListView listViewProducts;
    @BindView(R.id.fab)
    FloatingActionButton fabButton;

    private int LOADER_VERSION = 100;
    private ProductCursorAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //set the butterKneife
        ButterKnife.bind(this);


        //set adapter
        mAdapter = new ProductCursorAdapter(this, null);

        //set empty view
        listViewProducts.setEmptyView(findViewById(R.id.empty_list_view));
        //set adapter in list view
        listViewProducts.setAdapter(mAdapter);

        //inti the loader
        getLoaderManager().initLoader(LOADER_VERSION, null, this);


        listViewProducts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long id) {
                //intent to go editor activity
                Intent intent = new Intent(MainActivity.this, EditorActivity.class);

                //upload the url in intent
                intent.setData(ContentUris.withAppendedId(ProductEntity.URI_CONTENT, id));

                //start activity
                startActivity(intent);
            }
        });


        fabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //intent to go editor activity
                Intent intent = new Intent(MainActivity.this, EditorActivity.class);

                //start activity
                startActivity(intent);
            }
        });
    }


    @Override
    public android.content.Loader<Cursor> onCreateLoader(int i, Bundle bundle) {

        String projection[] = new String[]{
                ProductEntity.COLUMN_ID,
                ProductEntity.COLUMN_PRODUCT_NAME,
                ProductEntity.COLUMN_PRODUCT_QUANTITY,
                ProductEntity.COLUMN_PRODUCT_PRICE,
                ProductEntity.COLUMN_PRODUCT_NAME_SUPPLIER,
                ProductEntity.COLUMN_PRODUCT_PHONE_SUPPLIER
        };

        return new ProductLoader(this,
                ProductEntity.URI_CONTENT,
                projection,
                null,
                null,
                null
        );

    }

    @Override
    public void onLoadFinished(android.content.Loader<Cursor> loader, Cursor cursor) {
        mAdapter.swapCursor(cursor);

    }

    @Override
    public void onLoaderReset(android.content.Loader<Cursor> loader) {
        mAdapter.swapCursor(null);

    }


}
