package com.example.android.inventoryapp2;

import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.android.inventoryapp2.data.ProductContact.ProductEntity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EditorActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    @BindView(R.id.increase_btn)
    Button increaseButton;
    @BindView(R.id.decrease_btn)
    Button decreaseButton;
    @BindView(R.id.delete_product_btn)
    Button deleteButton;
    @BindView(R.id.save_product_btn)
    Button saveButton;
    @BindView(R.id.contact_phone_btn)
    Button contactButton;
    @BindView(R.id.product_amount_edit_text)
    EditText amountEditText;
    @BindView(R.id.product_name_edit_text)
    EditText nameEditText;
    @BindView(R.id.product_price_edit_text)
    EditText priceEditText;
    @BindView(R.id.supplier_name_edit_text)
    EditText supplierNameEditText;
    @BindView(R.id.supplier_phone_edit_text)
    EditText supplierPhoneeEditText;
    @BindView(R.id.spinner_increment)
    Spinner spinnerIncrementBy;

    private int PRODUCT_LOADER_VERSION = 101;
    private Uri mUri;
    private int mIncrementBy = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);


        ButterKnife.bind(this);
        //recieve intent
        final Intent intent = getIntent();


        //get uri from intent
        mUri = intent.getData();

        if ((mUri != null)) {
            //if it will update will go here
            setTitle(getString(R.string.edit_label));
            //get data from database and set in edits text
            getLoaderManager().initLoader(PRODUCT_LOADER_VERSION, null, this);

        } else {
            //else it will insert will go here
            //hidden contact , delete and save buttons when show insert layout
            contactButton.setVisibility(Button.GONE);
            deleteButton.setVisibility(Button.GONE);
            saveButton.setVisibility(Button.GONE);
            setTitle(getString(R.string.insert_label));

        }

        //action increase
        increaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //get text from amount edit text and convert to string
                String amountValue = amountEditText.getText().toString();

                //check if amount value is empty
                if (amountValue.isEmpty()) {
                    //if empty assign to 0
                    amountValue = "0";
                }

                //convert value to integer
                int amount = Integer.valueOf(amountValue);

                //add the number of increment is selected in amount
                amount += mIncrementBy;

                //set the number to edit text  and convert to string
                amountEditText.setText(String.valueOf(amount));
            }
        });

        //action decrease
        decreaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //get text from amount edit text and convert to string
                String amountValue = amountEditText.getText().toString();

                //check if amount value is empty
                if (amountValue.isEmpty()) {
                    //if empty assign to 0
                    amountValue = "0";
                }

                //convert value to integer
                int amount = Integer.valueOf(amountValue);

                //check if amount equal 0
                if ((amount - mIncrementBy) >= 0) {
                    //if not equal decrease it number
                    //set the number to edit text after mius the number determine is mIncrementBy and convert to string
                    amount = amount - mIncrementBy;
                    amountEditText.setText(String.valueOf(amount));
                }
            }
        });

        //action for contact button
        contactButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //create intent to call phone
                Intent intentCallPhone = new Intent(Intent.ACTION_DIAL);

                //set data in intent
                //the data should uri , for it should use method parse and start to 'tel:'
                intentCallPhone.setData(Uri.parse("tel:" + supplierPhoneeEditText.getText().toString()));

                //go to phone call
                startActivity(intentCallPhone);
            }
        });

        //action for delete button
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createAlertDialogDelete();
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //save button it save any change do in product by update data in database
                //create content values
                ContentValues contentValues = new ContentValues();

                //put key and string in it
                contentValues.put(ProductEntity.COLUMN_PRODUCT_NAME, nameEditText.getText().toString());
                contentValues.put(ProductEntity.COLUMN_PRODUCT_PRICE, priceEditText.getText().toString());
                contentValues.put(ProductEntity.COLUMN_PRODUCT_QUANTITY, amountEditText.getText().toString());
                contentValues.put(ProductEntity.COLUMN_PRODUCT_PHONE_SUPPLIER, supplierPhoneeEditText.getText().toString());
                contentValues.put(ProductEntity.COLUMN_PRODUCT_NAME_SUPPLIER, supplierNameEditText.getText().toString());

                //get content resolver and update the product
                int resultUpdate = getContentResolver().update(mUri, contentValues, null, null);

                //inform user state of process update
                if (resultUpdate == 0) {
                    //if no row update inform by toast it
                    Toast.makeText(EditorActivity.this, R.string.toast_attention_null_values, Toast.LENGTH_SHORT).show();
                } else {
                    //if it update done inform in toast
                    Toast.makeText(EditorActivity.this, R.string.toast_update_done, Toast.LENGTH_SHORT).show();

                    //after it update go to home
                    finish();
                }


            }
        });

        //set the spinner
        setUpSpinner();
    }

    public void setUpSpinner() {
        final ArrayAdapter adapterSpinner = ArrayAdapter.createFromResource(this,
                R.array.increment_by_spinner,
                android.R.layout.simple_list_item_1
        );
        adapterSpinner.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        spinnerIncrementBy.setAdapter(adapterSpinner);

        spinnerIncrementBy.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int positioin, long l) {
                String num = adapterView.getItemAtPosition(positioin).toString();
                mIncrementBy = Integer.valueOf(num);
                spinnerIncrementBy.setSelection(positioin);
                spinnerIncrementBy.setVisibility(Spinner.VISIBLE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                mIncrementBy = 1;
            }
        });
    }

    public void createAlertDialogDelete() {
        //create alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        //set message that show in alert Dialog
        builder.setMessage(R.string.message_delete_alert);

        //create positive button and set name and click listener
        builder.setPositiveButton(R.string.delete_button_positive, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //do delete the product
                int rowDelete = getContentResolver().delete(mUri, null, null);

                //Toast show depending in delect successful or not
                if (rowDelete == 0) {
                    //if row deleted is 0 then the delete have with problem
                    Toast.makeText(getBaseContext(), R.string.toast_message_delete_faile, Toast.LENGTH_SHORT).show();
                } else {
                    //otherwise , the delete was successful and we can display the toast
                    Toast.makeText(getBaseContext(), R.string.toast_message_delete_done, Toast.LENGTH_SHORT).show();
                }

                //go to home
                finish();
            }
        });

        //create negative button with name and click listener
        builder.setNegativeButton(R.string.delete_button_negative, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                if (dialogInterface != null) {
                    //if user click negative button then dismiss the alert dialog
                    dialogInterface.dismiss();
                }
            }
        });
        //create alert dialog by builder
        AlertDialog alertDialog = builder.create();

        //show alert dialog
        alertDialog.show();
    }


    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        //check if muri is null or not
        if (mUri != null) {
            //if muri is not null then we in update layout
            //should hidden item of insert in database
            //get item of insert in database
            MenuItem menuItem = menu.findItem(R.id.action_save_insert);

            //hidden it item
            menuItem.setVisible(false);
        }

        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //get menu inflater and inflate the menu editor
        getMenuInflater().inflate(R.menu.menu_editor, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //get it of item
        int id = item.getItemId();

        switch (id) {
            case R.id.action_save_insert:
                //associate xml by java
                //get string from edit text for name and price and amount and supplier name and supplier phone
                String name = nameEditText.getText().toString().trim();
                String price = priceEditText.getText().toString().trim();
                String amount = amountEditText.getText().toString().trim();
                String supplierName = supplierNameEditText.getText().toString().trim();
                String supplierPhone = supplierPhoneeEditText.getText().toString().trim();

                //create content values
                ContentValues contentValues = new ContentValues();

                //set key and value in content value
                contentValues.put(ProductEntity.COLUMN_PRODUCT_NAME, name);
                contentValues.put(ProductEntity.COLUMN_PRODUCT_QUANTITY, amount);
                contentValues.put(ProductEntity.COLUMN_PRODUCT_PRICE, price);
                contentValues.put(ProductEntity.COLUMN_PRODUCT_NAME_SUPPLIER, supplierName);
                contentValues.put(ProductEntity.COLUMN_PRODUCT_PHONE_SUPPLIER, supplierPhone);

                //insert values in database
                Uri uriInsertResult = getContentResolver().insert(ProductEntity.URI_CONTENT, contentValues);

                if (uriInsertResult == null) {
                    //insert failed
                    Toast.makeText(this, R.string.toast_attention_null_values, Toast.LENGTH_SHORT).show();
                } else {
                    //insert done
                    Toast.makeText(this, R.string.toast_insert_done, Toast.LENGTH_SHORT).show();
                    //go to home
                    finish();
                }


            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;

            default:
                break;

        }

        return true;
    }

    @Override
    public Loader onCreateLoader(int i, Bundle bundle) {
        //create the projection
        String projection[] = {ProductEntity.COLUMN_PRODUCT_NAME_SUPPLIER,
                ProductEntity.COLUMN_PRODUCT_NAME,
                ProductEntity.COLUMN_PRODUCT_PHONE_SUPPLIER,
                ProductEntity.COLUMN_PRODUCT_PRICE,
                ProductEntity.COLUMN_PRODUCT_QUANTITY
        };

        //create the loader and send context and my uri and projection to work in background
        return new ProductLoader(this, mUri, projection, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        //check if cursor is null and cursor is in first of row
        if (cursor != null && cursor.moveToFirst()) {

            //get index of name , price , amount ,supplier name and phone name
            int nameIndex = cursor.getColumnIndex(ProductEntity.COLUMN_PRODUCT_NAME);
            int priceIndex = cursor.getColumnIndex(ProductEntity.COLUMN_PRODUCT_PRICE);
            int amountIndex = cursor.getColumnIndex(ProductEntity.COLUMN_PRODUCT_QUANTITY);
            int supplierNameIndex = cursor.getColumnIndex(ProductEntity.COLUMN_PRODUCT_NAME_SUPPLIER);
            int supplierPhoneIndex = cursor.getColumnIndex(ProductEntity.COLUMN_PRODUCT_PHONE_SUPPLIER);

            //get text from cursor for index name , price , amount ,supplier name and phone name  and save in string
            String name = cursor.getString(nameIndex);
            String price = cursor.getString(priceIndex);
            String amount = cursor.getString(amountIndex);
            String supplierName = cursor.getString(supplierNameIndex);
            String supplierPhone = cursor.getString(supplierPhoneIndex);

            //set text in edit text special
            amountEditText.setText(amount);
            nameEditText.setText(name);
            priceEditText.setText(price);
            supplierNameEditText.setText(supplierName);
            supplierPhoneeEditText.setText(supplierPhone);
        }
    }


    @Override
    public void onLoaderReset(Loader loader) {
        //when call loader reset should rest all edit text
        amountEditText.setText("");
        nameEditText.setText("");
        priceEditText.setText("");
        supplierNameEditText.setText("");
        supplierPhoneeEditText.setText("");
    }
}
