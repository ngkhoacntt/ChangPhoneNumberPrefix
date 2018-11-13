package com.example.nguyenkhoahung.changephonenumberprefix;

import android.Manifest;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements AsyncResponse {
    // Request code for READ_CONTACTS. It can be any number > 0.
    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 100;
    ListView lvAllContact;
    Button btnGetAllContactFromPhone, btnConvertSeletedContact, btnGetSimContact,btnSelectAll,btnUnSelectAll;
    TextView tvResult, tvTotalRecord;
    LoadContactAsyncTask contactAsyncTask;
    EditContactAsyncTask editContactAsyncTask;
    List<ContactDTO> listAllContact, listContactSelected;
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
        GetContactClick getContactClick = new GetContactClick();
        btnGetAllContactFromPhone.setOnClickListener(getContactClick);
        btnGetSimContact.setOnClickListener(getContactClick);
        btnConvertSeletedContact.setOnClickListener(getContactClick);
        listAdapter = new ListAdapter(this, R.layout.list_contact_item, listAllContact);
        lvAllContact.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                ContactDTO contact = (ContactDTO) adapterView.getItemAtPosition(position);
                if(contact.isSelected()){
                    contact.setSelected(false);
                }else {
                    contact.setSelected(true);
                }
                listAdapter.notifyDataSetChanged();
            }
        });
        lvAllContact.setAdapter(listAdapter);
        btnSelectAll.setOnClickListener(getContactClick);
        btnUnSelectAll.setOnClickListener(getContactClick);
    }

    @Override
    public void processFinish(List<ContactDTO> contactDTOS) {
        tvTotalRecord.setText("Total record: " + contactDTOS.size());
        listAllContact = contactDTOS;
        listAdapter.setListContact(listAllContact);
        listAdapter.notifyDataSetChanged();
        tvResult.setText("Load contact successful !");
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
        btnConvertSeletedContact = findViewById(R.id.btnConvertSelectedContact);
        btnGetSimContact = findViewById(R.id.btnGetAllContactFromSim);
        btnSelectAll = findViewById(R.id.btnSelectAll);
        btnUnSelectAll = findViewById(R.id.btnUnSelectAll);
    }

    private List<ContactDTO> getAllContact() {
        List<ContactDTO> listAllContact = new ArrayList<>();
        ContactDTO contact = new ContactDTO();
        ContentResolver contentResolver = getContentResolver();
        Cursor cursor = contentResolver.query(ContactsContract.Data.CONTENT_URI, null, null, null, null);
        return listAllContact;
    }

    private void showAllContact() {
        contactAsyncTask = new LoadContactAsyncTask(MainActivity.this, ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
        contactAsyncTask.delegate = this;
        contactAsyncTask.execute();
    }

    private void showAllSimContact() {
        contactAsyncTask = new LoadContactAsyncTask(MainActivity.this, Uri.parse("content://icc/adn"));
        contactAsyncTask.delegate = this;
        contactAsyncTask.execute();
    }
    private class GetContactClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.btnGetAllContactFromPhone:
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, PERMISSIONS_REQUEST_READ_CONTACTS);
                    } else {
                        showAllContact();
                    }
                    break;

                case R.id.btnGetAllContactFromSim:
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, PERMISSIONS_REQUEST_READ_CONTACTS);
                    } else {
                        showAllSimContact();
                    }
                    break;
                case R.id.btnConvertSelectedContact:
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, PERMISSIONS_REQUEST_READ_CONTACTS);
                    } else {
                        convertNumber(listAllContact);
                    }
                    break;
                case R.id.btnSelectAll:
                    for (ContactDTO contact :
                            listAllContact) {
                        contact.setSelected(true);
                    }
                    listAdapter.notifyDataSetChanged();
                    break;
                case R.id.btnUnSelectAll:
                    for (ContactDTO contact :
                            listAllContact) {
                        contact.setSelected(false);
                    }
                    listAdapter.notifyDataSetChanged();
                    break;
            }
        }
    }

    private void convertNumber(List<ContactDTO> listSelectContact){
        if(Common.isEmptyList(listSelectContact)){
            Toast.makeText(getApplicationContext(),"Please select contact !",Toast.LENGTH_SHORT).show();
        } else {
            editContactAsyncTask = new EditContactAsyncTask(MainActivity.this,listSelectContact);
            editContactAsyncTask.delegate = this;
            editContactAsyncTask.execute();
        }
    }
}
