package com.example.nguyenkhoahung.changephonenumberprefix;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity implements AsyncResponse {
    // Request code for READ_CONTACTS. It can be any number > 0.
    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 100;
    ListView lvAllContact;
    Button btnGetAllContactFromPhone;
    TextView tvResult, tvTotalRecord;
    LoadContactAsyncTask contactAsyncTask;
    List<ContactDTO> listAllContact;
    ListAdapter listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        listAllContact = new ArrayList<>();
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        init();
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        btnGetAllContactFromPhone.setOnClickListener(new GetContactClick());
        listAdapter = new ListAdapter(this, R.layout.list_contact_item, listAllContact);
        lvAllContact.setAdapter(listAdapter);
    }

    @Override
    public void processFinish(List<ContactDTO> contactDTOS) {
        tvTotalRecord.setText("Total record: " + contactDTOS.size());
        listAllContact = contactDTOS;
        listAdapter.setListContact(listAllContact);
        listAdapter.notifyDataSetChanged();
        tvResult.setText("Load contact succesfull !");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                showAllContact();
            } else {
                Toast.makeText(getApplicationContext(), "Can't show contact if you deny", Toast.LENGTH_SHORT);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void init() {
        lvAllContact = findViewById(R.id.lvAllContact);
        btnGetAllContactFromPhone = findViewById(R.id.btnGetAllContactFromPhone);
        tvResult = findViewById(R.id.tvShowAlert);
        tvTotalRecord = findViewById(R.id.tvTotalRecord);
    }

    private List<ContactDTO> getAllContact() {
        List<ContactDTO> listAllContact = new ArrayList<>();
        ContactDTO contact = new ContactDTO();
        ContentResolver contentResolver = getContentResolver();
        Cursor cursor = contentResolver.query(ContactsContract.Data.CONTENT_URI, null, null, null, null);
        return listAllContact;
    }

    private void showAllContact() {
        contactAsyncTask = new LoadContactAsyncTask(MainActivity.this);
        contactAsyncTask.delegate = this;
        contactAsyncTask.execute();
    }

    private class GetContactClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, PERMISSIONS_REQUEST_READ_CONTACTS);
            } else {
                showAllContact();
            }
        }
    }
}
